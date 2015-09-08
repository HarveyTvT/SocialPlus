package models.Process;

import models.Midform.SocialComment;
import models.Midform.SocialMessage;
import models.Midform.SocialUser;
import models.Results.Result;

/**
 * Created by lizhuoli on 15/8/31.
 */
public class PreProcess {

    public PreProcess(){

    }


    /**
     * WorkFlow of PreProcess
     * @param message
     * @return
     */
    public  Result WorkFlow(SocialMessage message){
        Result result = new Result();
        prologue(message, result);
        getGender(message, result);
        getTimer(message, result);
        getNodes(message, result);
        getLinks(message, result);
        Result.save(result);
        return result;
    }

    /**
     * The prologue of workflow, pass some basic info like
     * id, url, content, author, tags to result.
     * @param message
     * @param result
     */
    public void prologue(SocialMessage message, Result result){
        result.setId(message.getId());
        result.setUrl(message.getUrl());
        result.setTags(message.getTags());
        result.setAuthor(message.getAuthor().getName());
    }

    /**
     * Calculate the gender of all users
     * @param message
     * @param result
     */
    public void getGender(SocialMessage message, Result result){

    }

    /**
     * Get the timeline
     * @param message
     * @param result
     */
    public void getTimer(SocialMessage message, Result result){

    }

    /**
     *Get all node in repost
     * @param message
     * @param result
     */
    public void getNodes(SocialMessage message, Result result){

    }
    /**
     * Get all links in repost
     * @param message
     * @param result
     */
    public void getLinks(SocialMessage message, Result result){

    }



    public static void main(String... args){
        SocialMessage message = new SocialMessage();
        SocialComment comment = new SocialComment();
        SocialUser user = new SocialUser();
        SocialMessage.save(message);
        SocialComment.save(comment);
        SocialUser.save(user);
    }
}
