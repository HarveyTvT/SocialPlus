package models.Process;

import models.Midform.SocialMessage;
import models.Results.Gender;
import models.Results.Outcome;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.lingjoin.keyExtractor.KeyExtractor.getKeyWords;
import static com.lingjoin.sentimentAnalysis.LJSentimentAnalysisMethod.LJST_GetParagraphSent;
import static models.Midform.SocialMessage.getSocialMessage;
import static models.Midform.SocialUser.getSocialUser;

/**
 * Created by lizhuoli on 15/8/31.
 */
public class AfterProcess {
    private List<SocialMessage> reposts = new ArrayList<SocialMessage>();
    private List<String> users = new ArrayList<>();

    /**
     * WorkFlow of PreProcess
     *
     * @param message
     * @return
     */
    public Outcome WorkFlow(SocialMessage message) {
        Outcome outcome = new Outcome();
        prologue(message, outcome);
        getGender(message, outcome);
        getTags(message, outcome);
        getTimeline(message, outcome);
        getNodes(message, outcome);
        getLinks(message, outcome);
        getLocations(message, outcome);
        getEmotion(message, outcome);
        Outcome.save(outcome);
        return outcome;
    }

    /**
     * The prologue of workflow, pass some basic info like
     * id, url, content, author, tags to
     *
     * @param message
     * @param outcome
     */
    private void prologue(SocialMessage message, Outcome outcome) {
        outcome.setId(message.getId());
        outcome.setUrl(message.getUrl());
        outcome.setAuthor(message.getAuthor());
        outcome.setContent(message.getContent());
        //get all socialmessage
        for (String repost_id : message.getRepostList()) {
            reposts.add(getSocialMessage(repost_id));
        }
        //get all users
        List<String> users_dump = new ArrayList<String>();
        users_dump.add(message.getAuthor());

        for (SocialMessage repostMessage : reposts) {
            users_dump.add(
                    repostMessage.getAuthor()
            );
        }

        Set set = new HashSet(users_dump);

        users = new ArrayList<String>(set);
    }

    /**
     * Extract the key words of the content
     *
     * @param message
     * @param outcome
     */
    private void getTags(SocialMessage message, Outcome outcome) {
        String messageStr = new String();
        messageStr = message.getContent();
        for (SocialMessage socialMessage : reposts) {
            messageStr += socialMessage.getContent();
        }
        String[] result = getKeyWords(messageStr);
        outcome.setTags(Arrays.asList(result));
    }

    /**
     * @param message
     * @param outcome
     */
    private void getEmotion(SocialMessage message, Outcome outcome) {
        String messageStr = new String();
        messageStr = message.getContent();
        for (SocialMessage socialMessage : reposts) {
            messageStr += socialMessage.getContent();
        }
        play.Logger.info(messageStr);
        String resultStr = LJST_GetParagraphSent(messageStr);
        String[] result = resultStr.split("\n");
        result[result.length - 1] = "";
        List<Integer> scores = new ArrayList<>();
        for (int i = 0; i < result.length - 1; i++) {
            String e = result[i];
            int score = Integer.valueOf(e.split("/")[1]);
            scores.add(score);
        }
        double positive = scores.get(0) + scores.get(1) + scores.get(6);
        double passive = scores.get(2) + scores.get(3) + scores.get(4) + scores.get(5);

        double emotion = (positive) / (positive + passive);
        BigDecimal db = new BigDecimal(emotion);
        db = db.setScale(4, BigDecimal.ROUND_UP);
        double val = db.doubleValue();
        outcome.setEmotion(val);
    }

    /**
     * Calculate the gender of all users
     *
     * @param message
     * @param outcome
     */
    private void getGender(SocialMessage message, Outcome outcome) {
        List<String> genders = new ArrayList<String>();

        for (String user : users) {
            genders.add(
                    getSocialUser(user).getGender()
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
     *
     * @param message
     * @param outcome
     */
    private void getTimeline(SocialMessage message, Outcome outcome) {
        List<Long> times = new ArrayList<Long>();
        List<HashMap<String, Long>> result = new ArrayList<HashMap<String, Long>>();
        for (SocialMessage repost : reposts) {
            times.add(convertTime(repost.getCreateTime()));
        }
        java.util.Collections.sort(times);
        ;
        int dot = 0;
        HashMap<String, Long> temp = new HashMap<>();
        temp.put("date", times.get(dot));
        temp.put("number", Long.valueOf(1));
        result.add(temp);
        times.set(dot, times.get(dot) + 1800000);
        for (int i = 0; i < times.size(); i++) {
            if (i < times.size() - 1) {
                //时间限制为1800000ms, 次数限制为3
                if (times.get(i) >= times.get(dot) && (i - dot >= 3)) {
                    HashMap<String, Long> hashMap = new HashMap<>();
                    hashMap.put("date", times.get(i));
                    hashMap.put("number", Long.valueOf(i - dot));
                    result.add(hashMap);
                    dot = i;
                    times.set(dot, times.get(dot) + 1800000);
                }
            } else {
                HashMap<String, Long> hashMap = new HashMap<>();
                hashMap.put("date", times.get(i));
                hashMap.put("number", Long.valueOf(i - dot));
                result.add(hashMap);
            }

        }
        outcome.setTimes(result);
    }

    /**
     * Get all node in re-post
     *
     * @param message
     * @param outcome
     */
    private void getNodes(SocialMessage message, Outcome outcome) {
        List<HashMap<String, String>> result = new ArrayList<>();
        HashMap<String, String> root = new HashMap<>();
        root.put("name", users.get(0));
        root.put("group", "0");

        for (String user : users) {
            HashMap<String, String> temp = new HashMap<>();
            temp.put("name", user);
            temp.put("group", "1");
            result.add(temp);
        }
        result.set(0, )
        outcome.setNodes(result);
    }

    private void getLocations(SocialMessage message, Outcome outcome) {
        List<HashMap<String, Integer>> result = new ArrayList<>();
        int[] provinces = new int[101];
        for (String user : users) {
            int province = Integer.valueOf(
                    getSocialUser(user).getLocation()
            );
            play.Logger.debug("current province is :" + String.valueOf(province));
            if (province <= 100) {
                provinces[province]++;
            } else {
                provinces[101]++;
            }
        }
        for (int i = 0; i < provinces.length; ++i) {
            if (provinces[i] > 0) {
                HashMap<String, Integer> temp = new HashMap<>();
                temp.put("id", i);
                temp.put("number", provinces[i]);
                result.add(temp);
            }
        }
        outcome.setLocations(result);
    }

    /**
     * Parse time from Mon Oct 27 11:04:04 +0800 2014 to a Long
     *
     * @param UtcTime
     * @return
     */
    private Long convertTime(String UtcTime) {
        long time;
        String[] times = UtcTime.split(" ");
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss '+0800' yyyy", Locale.ENGLISH);
        try {
            Date date = sdf.parse(UtcTime);
            time = date.getTime();
        } catch (ParseException e) {
            time = -1;
            e.printStackTrace();
        }
        return time;
    }

    /**
     * Get all links in repost
     *
     * @param message
     * @param outcome
     */
    private void getLinks(SocialMessage message, Outcome outcome) {
        int i = 0;
        List<HashMap<String, Integer>> result = new ArrayList<>();
        for (String user : users) {
            HashMap<String, Integer> temp = new HashMap<>();
            temp.put("source", 0);
            temp.put("target", ++i);
            result.add(temp);
        }
        outcome.setLinks(result);
    }

    public static void main(String... args) {
        SocialMessage message = getSocialMessage("message1");
        AfterProcess afterProcess = new AfterProcess();
        Outcome outcome = afterProcess.WorkFlow(message);
        Outcome.save(outcome);
    }
}
