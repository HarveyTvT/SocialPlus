package models.Process;

import models.Midform.SocialMessage;
import models.Results.Result;

/**
 * Created by lizhuoli on 15/8/31.
 */
public class PreProcess {

    public PreProcess(){

    }

    public  Result WorkFlow(SocialMessage message){
        SocialMessage[] repostList = message.getRepostList();
        return null;
    }

    public static void main(String... args){
        SocialMessage socialMessage = new SocialMessage();
        socialMessage.setId("1122334");
        socialMessage.setRepostList(
                new SocialMessage[]{
                        new SocialMessage(), new SocialMessage()
                }
        );
        SocialMessage.save(socialMessage);
    }
}
