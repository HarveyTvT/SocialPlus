package models.Process;

import models.Midform.SocialMessage;
import models.Midform.SocialUser;
import models.Results.Gender;
import models.Results.Outcome;
import play.Logger;
import utils.ValueComparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static models.Midform.SocialMessage.getSocialMessage;
import static models.Midform.SocialUser.getSocialUser;

/**
 * Created by lizhuoli on 15/8/31.
 */
public class AfterProcess {
    private List<SocialMessage> reposts = new ArrayList<>();
    private List<String> users = new ArrayList<>();
    private List<HashMap<String,String>> nodeResult = new ArrayList<>();
    private List<HashMap<String,Integer>> linkResult = new ArrayList<>();
    private HashMap<String,Integer> messageMap = new HashMap<>();

    public Outcome workFlow(SocialMessage message) {
        Outcome outcome = new Outcome();
        prologue(message, outcome);
        getGender(message, outcome);
        getTags(message, outcome);
        getTimeline(message, outcome);
        getNodesAndLinks(message, outcome);
        getKeyUser(message, outcome);
        getKeyRepost(message, outcome);
        getLocations(message, outcome);
        getEmotion(message, outcome);
        Outcome.save(outcome);
        return outcome;
    }

    /**
     * The prologue of workflow, pass some basic info like
     * id, content, author, tags to
     *
     * @param message
     * @param outcome
     */
    private void prologue(SocialMessage message, Outcome outcome) {
        outcome.setId(message.getId());
        outcome.setAuthor(
                getSocialUser(message.getAuthor()).getName()
        );
        outcome.setContent(message.getContent());
        //get all socialmessage
        for (String repost_id : message.getRepostList()) {
            SocialMessage temp = getSocialMessage(repost_id);
            if (temp != null){
                reposts.add(temp);
            }
        }
        //get all users
        List<String> users_dump = new ArrayList<>();

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
    }

    /**
     * @param message
     * @param outcome
     */
    private void getEmotion(SocialMessage message, Outcome outcome) {

    }

    /**
     * Calculate the gender of all users
     *
     * @param message
     * @param outcome
     */
    private void getGender(SocialMessage message, Outcome outcome) {
        List<String> genders = new ArrayList<>();

        for (String user : users) {
            SocialUser temp = getSocialUser(user);
            if (temp != null){
                genders.add(temp.getGender());
            }
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
        List<Long> times = new ArrayList<>();
        List<HashMap<String, Long>> result = new ArrayList<>();
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
        outcome.setTime(result);
    }

    /**
     * Get all node in re-post
     * @param originMessage
     * @param outcome
     */

    private void getNodesAndLinks(SocialMessage originMessage, Outcome outcome) {

        String[] originRepost = new String[1];
        originRepost[0] = originMessage.getId();
        getNodesAndLinkRecursion(originRepost, -1);
        outcome.setNodes(nodeResult);
        outcome.setLinks(linkResult);
    }

    private void getNodesAndLinkRecursion(String[] repostLists,int layer){
        if (repostLists == null || repostLists.length == 0 && layer > 4){
            return;
        }
        layer++;
        for(int i = 0;i < repostLists.length;i++){
            String repostId = repostLists[i];
            if (repostId.equals("")) return;
            SocialMessage temp = SocialMessage.getSocialMessage(repostId);
            String authorId = temp != null ? temp.getAuthor() : null;
            SocialUser author = SocialUser.getSocialUser(authorId);
            String authorName = author != null ? author.getName() : "未知";

            HashMap<String,String> group = new HashMap<>();
            group.put("name", authorName);
            group.put("group", String.valueOf(layer));
            nodeResult.add(group);

            String[] repostList = null;
            if (!(temp == null || temp.getRepostList().length == 0 && layer > 4)){
                repostList = temp.getRepostList();
                HashMap<String,Integer> link = new HashMap<>();
                link.put("source",nodeResult.size()-i-1);
                link.put("target",nodeResult.size()-1);
                linkResult.add(link);
            }

            /*
            用于关键用户，添加到所有转发微博集合，除过作者自身
             */
            if(layer != 0){
                String repostValue = temp != null ? temp.getRepostCount() : "0";
                Integer repostCount = Integer.valueOf(repostValue);
                messageMap.put(repostId, repostCount);
            }

            getNodesAndLinkRecursion(repostList, layer);
        }
    }


    private void getKeyUser(SocialMessage message, Outcome outcome){
        ValueComparator valueComparator = new ValueComparator(messageMap);
        SortedMap<String,Integer> sortedMessageMap = new TreeMap(valueComparator);
        sortedMessageMap.putAll(messageMap);
        String keyMessageId = sortedMessageMap.lastKey();
        String keyUserId = SocialMessage.getSocialMessage(keyMessageId).getAuthor();
        outcome.setKeyUser(keyUserId);
    }

    private void getKeyRepost(SocialMessage message, Outcome outcome){
        ValueComparator valueComparator = new ValueComparator(messageMap);
        List<String> sortedList = new ArrayList<>(messageMap.keySet());
        Collections.sort(sortedList,valueComparator);

        List<HashMap<String,String>> keyRepostList = new ArrayList<>();
        for (int i = 0;i < (sortedList.size() < 5 ? sortedList.size() : 5);i++){
            HashMap<String,String> userMap = new HashMap<>();
            SocialMessage tempMessage = SocialMessage.getSocialMessage(
                    sortedList.get(sortedList.size()-i-1));
            String userId = tempMessage.getAuthor();
            String name = SocialUser.getSocialUser(userId).getName();
            userMap.put("name",name != null ? name : "未知");
            userMap.put("repost",tempMessage.getRepostCount());
            keyRepostList.add(userMap);
        }
        outcome.setKeyRepost(keyRepostList);
    }

    private void getLocations(SocialMessage message, Outcome outcome) {
        List<HashMap<String, Integer>> result = new ArrayList<>();
        int[] provinces = new int[101];
        for (String user : users) {
            SocialUser temp = getSocialUser(user);
            int province = Integer.valueOf(
                    temp != null ? temp.getLocation() : "0"
            );
            if (province < 100) {
                provinces[province]++;
            } else {
                provinces[100]++;
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss'.000'");
        try {
            Date date = sdf.parse(UtcTime);
            time = date.getTime();
        } catch (ParseException e) {
            time = -1;
            e.printStackTrace();
        }
        return time;
    }

}
