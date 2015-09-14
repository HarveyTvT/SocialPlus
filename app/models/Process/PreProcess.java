package models.Process;


import com.fasterxml.jackson.databind.JsonNode;
import models.APIRequest.WeiboAPI;
import models.Midform.SocialMessage;
import models.Midform.SocialUser;
import models.RawConverter.ConverterUtil;
import play.libs.F.Promise;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhuoli on 15/8/31.
 */
public class PreProcess {
    private int count = 5;
    private String weiboToken;

    public PreProcess(String token){
        weiboToken = token;
    }

    public void getMessage(List<String> ids){
        if (ids.size() == 0 || count > 0){
            return;
        }
        else{
            count --;
            for (String id : ids){
                List<String>list = getMessageOnce(id);
                getMessage(list);
            }
        }
    }

    public List<String> getMessageOnce(String id){
        SocialMessage message = SocialMessage.getSocialMessage(id);
        WeiboAPI weiboAPI = new WeiboAPI(weiboToken);
        Promise<JsonNode> userPromise = weiboAPI.getSocialUser(message.getAuthor());
        Promise<JsonNode> repostListPromise = weiboAPI.getSocialMessageList(message.getId());

        ArrayList<String> repostList = new ArrayList<>();
        Promise.sequence(userPromise,repostListPromise).onRedeem(results -> {
            JsonNode userJson = results.get(0);
            JsonNode repostListJson = results.get(1);
            SocialUser user = ConverterUtil.getUser(userJson);
            List<String> repostArray = ConverterUtil.getRepostList(repostListJson);
            repostList.addAll(repostArray);
            message.setRepostList(repostList.toArray(new String[0]));

            SocialUser.save(user);
            SocialMessage.save(message);
        });
        return repostList;
    }


    public void getMessageRecursion(String originalId){
        String id = originalId;
        List<String> ids = new ArrayList<>();
        ids.add(originalId);
        getMessage(ids);
    }

}
