package models.Process;

import models.Message;
import models.Midform.SocialMessage;
import models.Midform.SocialUser;
import models.Results.Gender;
import models.Results.Outcome;

import java.util.List;
import java.util.ArrayList;

import static models.Midform.SocialMessage.getSocialMessage;
import static models.Midform.SocialUser.getSocialUser;

/**
 * Created by lizhuoli on 15/8/31.
 */
public class PreProcess {

    public PreProcess(){

    }

    private List<SocialMessage> reposts = new ArrayList<SocialMessage>();
    /**
     * WorkFlow of PreProcess
     * @param message
     * @return
     */
    public Outcome WorkFlow(SocialMessage message) {
        Outcome outcome = new Outcome();
        prologue(message, outcome);
        getGender(message, outcome);
        getTimeline(message, outcome);
        getNodes(message, outcome);
        getLinks(message, outcome);
        Outcome.save(outcome);
        return outcome;
    }

    /**
     * The prologue of workflow, pass some basic info like
     * id, url, content, author, tags to
     * @param message
     * @param outcome
     */
    private void prologue(SocialMessage message, Outcome outcome) {
        outcome.setId(message.getId());
        outcome.setUrl(message.getUrl());
        outcome.setTags(message.getTags());
        outcome.setAuthor(message.getAuthor());

        for (String repost_id : message.getRepostList()) {
            reposts.add(getSocialMessage(repost_id));
        }
    }

    /**
     * Calculate the gender of all users
     * @param message
     * @param outcome
     */
    private void getGender(SocialMessage message, Outcome outcome) {
        List<String> genders = new ArrayList<String>();
        SocialUser author = getSocialUser(message.getAuthor());
        genders.add(author.getGender());

        for (SocialMessage repostMessage : reposts) {
            genders.add(
                    getSocialUser(repostMessage.getAuthor()).getGender()
            );
        }
        int male = 0;
        int female = 0;
        int other = 0;
        for (String gender : genders) {
            switch (gender) {
                case "m":
                    male++;
                    break;
                case "f":
                    female++;
                    break;
                case "o":
                    other++;
                    break;
                default:
                    break;
            }
        }
        Gender gender = new Gender();
        gender.setFemale(female);
        gender.setMale(male);
        gender.setOther(other);
        outcome.setGender(gender);
    }

    /**
     * Get the timeline
     * @param message
     * @param outcome
     */
    private void getTimeline(SocialMessage message, Outcome outcome) {
        List<String> times = new ArrayList<String>();
        times.add(message.getCreateTime());

    }

    /**
     *Get all node in repost
     * @param message
     * @param outcome
     */
    private void getNodes(SocialMessage message, Outcome outcome) {

    }
    /**
     * Get all links in repost
     * @param message
     * @param outcome
     */
    private void getLinks(SocialMessage message, Outcome outcome) {

    }

    public static void main(String... args){
        PreProcess preProcess = new PreProcess();
        SocialMessage message = getSocialMessage("message1");
        Outcome outcome = preProcess.WorkFlow(message);
        Outcome.save(outcome);
    }
}
