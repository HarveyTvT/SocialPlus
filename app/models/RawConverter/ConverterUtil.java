package models.RawConverter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.APIRequest.WeiboUtils;
import models.Midform.SocialComment;
import models.Midform.SocialMessage;
import models.Midform.SocialUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhuoli on 15/9/8.
 */
public class ConverterUtil {
    public static int calculateWeight(int follow,int star,int favorite){
        return (follow + star + favorite)/3;
    }

    static public SocialUser getUser(JsonNode userJson) {
        SocialUser socialUser = new SocialUser();

        String id = userJson.get("id").asText();
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

    static public SocialMessage getMessage(JsonNode messageJson,Boolean isOriginal) {
        SocialMessage socialMessage = new SocialMessage();

        String id = messageJson.get("id").asText();
        String createTime = messageJson.get("created_at").asText();
        String content = messageJson.get("text").asText();
        String authorId = messageJson.path("user").
                get("id").asText();
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
            source = messageJson.get("retweeted_status").get("id").asText();
        }

        socialMessage.setId(id);
        socialMessage.setCreateTime(createTime);
        socialMessage.setContent(content);
        socialMessage.setTags(tags);
        socialMessage.setLocation(location);
        socialMessage.setAuthor(authorId);
        socialMessage.setRepostCount(repostsCount);
        socialMessage.setCommentCount(commentCount);
        socialMessage.setRepostList(repostList);

        return socialMessage;
    }

    static public SocialComment getComment(JsonNode commentJson) {
        return null;
    }

    static public List<String> getRepostList(JsonNode repostListJson){
        if (repostListJson.size() == 0){
            return new ArrayList<>();
        }
        repostListJson = repostListJson.get("statuses");
        ObjectMapper mapper = new ObjectMapper();
        List<String> repostList = mapper.convertValue(repostListJson, new ArrayList<String>().getClass());
        return repostList;
    }
}
