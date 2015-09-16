package models.Process;


import com.fasterxml.jackson.databind.JsonNode;
import models.APIRequest.WeiboAPI;
import models.Midform.SocialMessage;
import models.Midform.SocialUser;
import models.RawConverter.ConverterUtil;
import play.Logger;
import play.libs.F.Callback;
import play.libs.F.Promise;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhuoli on 15/8/31.
 */
public class PreProcess {
    private int count = 500;
    private String weiboToken;
    private WeiboAPI weiboAPI;

    public PreProcess(String token){
        weiboToken = token;
        weiboAPI = new WeiboAPI(weiboToken);
    }

    public void workFlow(SocialMessage message){
        String id = message.getId();
        getMessageRecursion(id);
    }

    public void getMessage(List<String> ids){
        if  (count <= 0){
            return;
        }
        count --;
        for (String id : ids){
            getMessageOnce(id,reposts->{
                if (reposts == null){
                    return;
                }
                else{
                    getMessage(reposts);
                }
            });
        }
    }

    public void getMessageOnce(String id,Callback<List<String>> callback){
        SocialMessage message = SocialMessage.getSocialMessage(id);
        if (message == null || message.getRepostList().length != 0){
            return;
        }

        Promise<JsonNode> userPromise = weiboAPI.getSocialUser(message.getAuthor());
        Promise<JsonNode> repostListPromise = weiboAPI.getSocialMessageList(message.getId());

        Promise.sequence(userPromise,repostListPromise).onRedeem(results -> {
            JsonNode userJson = results.get(0);
            Logger.info(userJson.toString());
            JsonNode repostListJson = results.get(1);
            SocialUser user = ConverterUtil.getUser(userJson);
            List<String> repostArray = ConverterUtil.getRepostList(repostListJson);
            message.setRepostList(repostArray.toArray(new String[0]));
            callback.invoke(repostArray);

            SocialUser.save(user);
            SocialMessage.update(message);
        });
    }


    public void getMessageRecursion(String originalId){
        String id = originalId;
        List<String> ids = new ArrayList<>();
        ids.add(id);
        getMessage(ids);
    }


    public void getAuthor(String uid){
        Promise<JsonNode> userPromise = weiboAPI.getSocialUser(uid);
        userPromise.onRedeem(userJson -> {
            SocialUser user = ConverterUtil.getUser(userJson);
            SocialUser.save(user);
        });
    }

}
