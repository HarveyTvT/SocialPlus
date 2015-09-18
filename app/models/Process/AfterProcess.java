package models.Process;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.AsyncRequest;
import edu.fudan.example.nlp.KeyWordExtraction;
import models.Midform.SocialMessage;
import models.Midform.SocialUser;
import models.Results.Gender;
import models.Results.Outcome;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import play.Logger;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.libs.F.Promise;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import utils.ConstUtil;
import utils.ValueComparator;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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
    private int[] layerResult = new int[6];
    private int maxRepostPath= 0;
    private List<String> maxRepostPathList = new ArrayList<>();

    public Outcome workFlow(SocialMessage message) {
        Outcome outcome = new Outcome();
        prologue(message, outcome);
        getGender(message, outcome);
        getTimeline(message, outcome);
        getNodesAndLinks(message, outcome);
        getLayerPercent(message, outcome);
        getRepostPath(message, outcome);
        getKeyUser(message, outcome);
//        getKeyRepost(message, outcome);
        getTags(message, outcome);
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
            if(!repost_id.equals("")) {
                SocialMessage temp = getSocialMessage(repost_id);
                if (temp != null) {
                    reposts.add(temp);
                }
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
        String content = message.getContent();
        Set<String> messageSet = messageMap.keySet();
        for (String set : messageSet){
            SocialMessage tempMessage = SocialMessage.getSocialMessage(set);
            String tempContent = tempMessage != null ? tempMessage.getContent() : null;
            if (tempContent != null){
                content += tempContent;
            }
        }
        Logger.debug("Content : " + content);
        List<String> keyWords = KeyWordExtraction.getKeyExtract(content);
        Logger.debug("List : " + keyWords.toString());
        outcome.setTags(keyWords);
    }


    /**
     * @param message
     * @param outcome
     */

    private void getEmotion(SocialMessage message, Outcome outcome) {
        //get content
        String content = message.getContent();
        Logger.info("Weibo content : " + content);
        String param = "[\"" + content + "\"]";
        //web request
        String url = "http://api.bosonnlp.com/sentiment/analysis?weibo";
        String token = "JI1cZUGm.3765.PHr4LP7cYiO4";
        WSClient ws = WS.client();

        WSRequest request = ws.url(url).setHeader("Content-Type", "application/json")
                .setHeader("X-Token",token)
                .setHeader("Accept", "application/json");

        Promise<WSResponse> response = request.post(param);
        JsonNode json = response.get(5000).asJson();
        Double emotion = json.get(0).get(0).asDouble();
        Logger.debug("Emotion : " + emotion.toString());
        outcome.setEmotion(emotion);
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
        int dot = 0;
        HashMap<String, Long> temp = new HashMap<>();
        temp.put("date", times.get(dot));
        temp.put("number", 1L);
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
        HashMap<String, String> group = new HashMap<>();
        group.put("name", SocialUser.getSocialUser(originMessage.getAuthor()).getName());
        group.put("group", "0");
        nodeResult.add(group);

        String[] originRepost = originMessage.getRepostList();
        getNodesAndLinkRecursion(originRepost, 0, 0, new ArrayList<>());

        outcome.setNodes(nodeResult);
        outcome.setLinks(linkResult);
    }

    private int getNodesAndLinkRecursion(String[] repostLists,int layer,int offset,List<String> repostPathList){
        offset++;
        if (layer < 6 && layer > -1){
            layerResult[layer]++;//层级数量比例分析
        }
        if (repostLists == null || repostLists.length == 0 && layer > 5){
            return offset;
        }
        layer++;
        for(int i = 0;i < repostLists.length;i++) {
            String repostId = repostLists[i];
            if (repostId.equals("")) return offset;
            SocialMessage temp = SocialMessage.getSocialMessage(repostId);
            String authorId = temp != null ? temp.getAuthor() : null;
            SocialUser author = SocialUser.getSocialUser(authorId);
            String authorName = author != null ? author.getName() : "未知用户";
            String[] repostList = temp != null ? temp.getRepostList() : null;
            HashMap<String, String> group = new HashMap<>();

            group.put("name", authorName);
            group.put("group", String.valueOf(layer));
            nodeResult.add(group);

            int thisNodePointer = nodeResult.size();
            if (!(temp == null || temp.getRepostList().length == 0 && layer > 5) || author == null) {
                if (layer > maxRepostPath){
                    repostPathList.add(repostId);
                    maxRepostPathList = repostPathList;
                    maxRepostPath = layer;
                }
                offset += getNodesAndLinkRecursion(repostList, layer, 0, repostPathList);
                HashMap<String, Integer> link = new HashMap<>();
                link.put("source", nodeResult.size() - offset);
                link.put("target", thisNodePointer - 1);
                linkResult.add(link);
            }

            /*
            用于关键用户，添加到所有转发微博集合，除过作者自身
             */
            String repostValue = temp != null ? temp.getRepostCount() : "0";
            Integer repostCount = Integer.valueOf(repostValue);
            messageMap.put(repostId, repostCount);
        }
        return offset;
    }

    private void getLayerPercent(SocialMessage message, Outcome outcome){
        double total = 0;
        //messageMap除去作者
        for(double layer : layerResult){
            total += layer;
        }
        List<Double> layerList = new ArrayList<>();
        double last = 100;//保留整数，为了确保和为100
        for (int i = 1;i < layerResult.length;i++){
            double percent = layerResult[i] * 100 / total;
            last -= percent;
            layerList.add(percent);
        }
        layerList.add(layerResult.length - 1, last);

        outcome.setLayer(layerList);
    }

    private void getKeyUser(SocialMessage message, Outcome outcome){
        ValueComparator valueComparator = new ValueComparator(messageMap);
        SortedMap<String,Integer> sortedMessageMap = new TreeMap(valueComparator);
        sortedMessageMap.putAll(messageMap);
        String keyMessageId = sortedMessageMap.lastKey();
        SocialMessage temp = SocialMessage.getSocialMessage(keyMessageId);
        String content = temp.getContent();
        String repostCount = temp.getRepostCount();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss'.000'");
        String datetime = String.valueOf(convertTime(temp.getCreateTime()));
        String name = SocialUser.getSocialUser(temp.getAuthor()).getName();
        String avatar = String.format(ConstUtil.weiboAvatarUrl, temp.getAuthor());

        HashMap<String,String> tempMap = new HashMap<>();
        tempMap.put("content",content);
        tempMap.put("repostCount", repostCount);
        tempMap.put("datetime",datetime);
        tempMap.put("name",name);
        tempMap.put("avatar", avatar);
        outcome.setKeyUser(tempMap);
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
            userMap.put("name",name != null ? name : "未知用户");
            userMap.put("repost",tempMessage.getRepostCount());
            keyRepostList.add(userMap);
        }
        outcome.setKeyRepost(keyRepostList);
    }

    /**
     * 最长单条转发链
     * @param message
     * @param outcome
     */
    private void getRepostPath(SocialMessage message, Outcome outcome){
        List<HashMap<String,String>> repostPathMap = new ArrayList<>();
        for (String repostPath : maxRepostPathList){
            HashMap<String,String> tempMap = new HashMap();
            SocialMessage tempMessage = SocialMessage.getSocialMessage(repostPath);
            String uid = tempMessage != null ? tempMessage.getAuthor() : null;
            SocialUser tempUser = SocialUser.getSocialUser(uid);
            String name = tempUser != null ? tempUser.getName() : "未知用户";
            String avatar = tempUser != null ? String.format(ConstUtil.weiboAvatarUrl, uid) :
                    "/assets/images/unknown_user.png";
            String url = String.format("http://www.weibo.com/%s",uid != null ? uid : "");
            tempMap.put("name",name);
            tempMap.put("url",url);
            tempMap.put("avatar",avatar);
            repostPathMap.add(tempMap);
        }
        outcome.setRepostPath(repostPathMap);
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
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
