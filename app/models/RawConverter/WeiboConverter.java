package models.RawConverter;

import com.fasterxml.jackson.databind.JsonNode;
import models.APIRequest.WeiboAPI;
import models.APIRequest.WeiboUtils;
import models.Midform.SocialComment;
import models.Midform.SocialMessage;
import models.Midform.SocialUser;
import play.libs.F.Promise;
import java.util.List;

/**
 * Created by lizhuoli on 15/9/2.
 */
public class WeiboConverter implements RawConverter{
    private final String weiboToken;
    public WeiboConverter(String token){
        weiboToken = token;
    }

    @Override
    public void convertMessage(Promise<List<JsonNode>> promiseList,String url) {
        WeiboAPI weiboAPI = new WeiboAPI(weiboToken);

        promiseList.onRedeem(jsonList->{
            JsonNode messageJson = jsonList.get(0);
            JsonNode repostsListJson = jsonList.get(1);

            JsonNode userNode = messageJson.path("user");

            String id = messageJson.get("id").asText();
            String createTime = messageJson.get("created_at").asText();
            String content = messageJson.get("text").asText();
            String authorId = userNode.get("id").asText();
            String source = messageJson.has("retweeted_status") ? "null" :
                    messageJson.get("retweeted_status").get("id").asText();
            String location = messageJson.get("geo").asText();
            String repostsCount = messageJson.get("reposts_count").asText();
            String commentCount = messageJson.get("comments_count").asText();
            String[] tags = WeiboUtils.getMessageTags(content);
            String[] repostsList = repostsListJson.findValuesAsText("statuses").toArray(new String[0]);

            String userId = userNode.get("id").asText();
            String userName = userNode.get("screen_name").asText();
            String userGender = userNode.get("gender").asText();
            String userWeight = String.format("%d", ConverterUtil.calculateWeight(
                            userNode.get("followers_count").asInt(),
                            userNode.get("friends_count").asInt(),
                            userNode.get("favourites_count").asInt()
                    )
            );
            String userLocation = userNode.get("province").asText();

            weiboAPI.getUserFriendList(userId).onRedeem(result -> {
                String[] friendList = (String[])result.findValuesAsText("ids").toArray(new String[0]);
                SocialUser author = getUser(friendList,userId,userName,
                        userGender,userWeight,userLocation);
                SocialMessage message = getMessage(tags,repostsList,id,createTime,url,content,
                        source,location,repostsCount,commentCount,authorId);
                SocialUser.save(author);
                SocialMessage.save(message);
            });
        });

    }

    @Override
    public void convertUser(Promise<List<JsonNode>> promiseList) {

    }

    @Override
    public void convertComment(Promise<List<JsonNode>> promiseList) {

    }

    @Override
    public SocialUser getUser(String[] list,String... args) {
        SocialUser socialUser = new SocialUser();
        socialUser.setId(args[0]);
        socialUser.setName(args[1]);
        socialUser.setGender(args[2]);
        socialUser.setWeight(args[3]);
        socialUser.setLocation(args[4]);
        socialUser.setFriendList(list);

        return socialUser;
    }

    @Override
    public SocialMessage getMessage(String[] tags,String[] repostsList,
                                    String... args) {
        SocialMessage socialMessage = new SocialMessage();
        socialMessage.setId(args[0]);
        socialMessage.setCreateTime(args[1]);
        socialMessage.setUrl(args[2]);
        socialMessage.setContent(args[3]);
        socialMessage.setTags(tags);
        socialMessage.setLocation(args[4]);
        socialMessage.setAuthor(args[5]);
        socialMessage.setRepostCount(args[6]);
        socialMessage.setCommentCount(args[7]);
        socialMessage.setRepostList(repostsList);
        socialMessage.setAuthor(args[8]);

        return socialMessage;
    }

    @Override
    public SocialComment getComment(String... args) {
        return null;
    }
}
