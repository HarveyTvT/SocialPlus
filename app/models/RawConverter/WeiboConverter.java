package models.RawConverter;

import com.fasterxml.jackson.databind.JsonNode;
import models.APIRequest.WeiboAPI;
import models.APIRequest.WeiboUtils;
import models.Midform.SocialComment;
import models.Midform.SocialMessage;
import models.Midform.SocialUser;
import play.libs.F.Promise;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhuoli on 15/9/2.
 */
public class WeiboConverter implements RawConverter{
    private final String weiboToken;
    private final String weiboUrl;
    public WeiboConverter(String token,String url){
        weiboToken = token;
        weiboUrl = url;
    }

    @Override
    public void convertMessage(Promise<List<JsonNode>> promiseList){
        WeiboAPI weiboAPI = new WeiboAPI(weiboToken);

        promiseList.onRedeem(jsonList -> {
            JsonNode messageList = jsonList.get(0).get("reposts");

            if (messageList.size() == 0){
                return;
            }

            //First get the original message json
            JsonNode originalJson = messageList.get(0).get("retweeted_status");
            SocialMessage originalMessage = getMessage(originalJson, true);
            List<String> repostList = new ArrayList<String>();
            //Next get the repost message json array, process each message with its author
            for (JsonNode messageJson : messageList) {
                //add each repost messageId to original message repostList
                repostList.add(ConverterUtil.weiboPrefix(messageJson.get("id").asText()));

                SocialMessage repostMessage = getMessage(messageJson, false);
                SocialUser author = getUser(messageJson.get("user"));

                SocialUser.save(author);
                SocialMessage.save(repostMessage);
            }
            originalMessage.setRepostList(repostList.toArray(new String[0]));
            SocialMessage.save(originalMessage);
        });
    }

    @Override
    public void convertUser(Promise<List<JsonNode>> promiseList){

    }

    @Override
    public void convertComment(Promise<List<JsonNode>> promiseList){

    }

    @Override
    public SocialUser getUser(JsonNode userJson) {
        SocialUser socialUser = new SocialUser();

        String id = ConverterUtil.weiboPrefix(userJson.get("id").asText());
        String name = userJson.get("screen_name").asText();
        String gender = userJson.get("gender").asText();
        String weight = String.format("%d", ConverterUtil.calculateWeight(
                        userJson.get("followers_count").asInt(),
                        userJson.get("friends_count").asInt(),
                        userJson.get("favourites_count").asInt()
                )
        );
        String location = userJson.get("province").asText();


        socialUser.setId(id);
        socialUser.setName(name);
        socialUser.setGender(gender);
        socialUser.setWeight(weight);
        socialUser.setLocation(location);

        return socialUser;
    }

    @Override
    public SocialMessage getMessage(JsonNode messageJson,Boolean isOriginal) {
        SocialMessage socialMessage = new SocialMessage();

        String id = ConverterUtil.weiboPrefix(messageJson.get("id").asText());
        String createTime = messageJson.get("created_at").asText();
        String content = messageJson.get("text").asText();
        String authorId = ConverterUtil.weiboPrefix(messageJson.path("user").
                get("id").asText());
        String location = messageJson.get("geo").asText();
        String repostsCount = messageJson.get("reposts_count").asText();
        String commentCount = messageJson.get("comments_count").asText();
        String[] tags = WeiboUtils.getMessageTags(content);
        String source = null;
        String[] repostList = null;

        if (isOriginal){
            repostList = null;
        }
        else{
            source = ConverterUtil.weiboPrefix(messageJson.get("retweeted_status")
                    .get("id").asText());
        }

        socialMessage.setId(id);
        socialMessage.setCreateTime(createTime);
        socialMessage.setUrl(weiboUrl);
        socialMessage.setContent(content);
        socialMessage.setTags(tags);
        socialMessage.setLocation(location);
        socialMessage.setAuthor(authorId);
        socialMessage.setRepostCount(repostsCount);
        socialMessage.setCommentCount(commentCount);
        socialMessage.setRepostList(repostList);

        return socialMessage;
    }

    @Override
    public SocialComment getComment(JsonNode commentJson) {
        return null;
    }
}
