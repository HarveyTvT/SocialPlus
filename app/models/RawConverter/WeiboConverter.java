package models.RawConverter;

import com.fasterxml.jackson.databind.JsonNode;
import models.APIRequest.WeiboAPI;
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
    public WeiboConverter(String token){
        weiboToken = token;
    }

    @Override
    public void convertMessage(Promise<JsonNode> jsonPromise){
        WeiboAPI weiboAPI = new WeiboAPI(weiboToken);

        jsonPromise.onRedeem(json -> {
            JsonNode messageList = json.get("reposts");

            if (messageList.size() == 0) {
                return;
            }

            //First get the original message json
            JsonNode originalJson = messageList.get(0).get("retweeted_status");
            SocialMessage originalMessage = ConverterUtil.getMessage(originalJson,true);
            List<String> repostList = new ArrayList<String>();
            //Next get the repost message json array, process each message with its author
            for (JsonNode messageJson : messageList) {
                //add each repost messageId to original message repostList
                repostList.add(messageJson.get("id").asText());

                SocialMessage repostMessage = ConverterUtil.getMessage(messageJson,false);
                SocialUser author = ConverterUtil.getUser(messageJson.get("user"));

                SocialUser.save(author);
                SocialMessage.save(repostMessage);
            }
            originalMessage.setRepostList(repostList.toArray(new String[0]));
            SocialMessage.save(originalMessage);
        });
    }

    @Override
    public void convertUser(Promise<JsonNode> jsonPromise){
        jsonPromise.map(json -> {
            SocialUser user = ConverterUtil.getUser(json);
            return user;
        });
    }

    @Override
    public void convertComment(Promise<JsonNode> jsonPromise){

    }
}
