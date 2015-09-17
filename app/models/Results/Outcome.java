package models.Results;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;
import utils.DbUtil;

import java.util.*;

/**
 * Created by harvey on 15-8-31.
 */
@Entity("outcome")
public class Outcome {
    public Outcome() {

    }
    @Id
    private String id;

    private String url;

    private String content;

    private String author;

    @Embedded("keyUser")
    private HashMap<String,String> keyUser;

    @Embedded("keyRepost")
    private List<HashMap<String,String>> keyRepost = new ArrayList<>();

    @Embedded("repostPath")
    private List<HashMap<String,String>> repostPath = new ArrayList<>();

    @Embedded("time")
    private List<HashMap<String, Long>> time = new ArrayList<>();

    @Embedded("gender")
    private Gender gender = new Gender();

    @Embedded("locations")
    private List<HashMap<String, Integer>> locations = new ArrayList<>();

    private List<Double> layer = new ArrayList<>();

    private List<String> tags = new ArrayList();

    @Embedded("nodes")
    private List<HashMap<String, String>> nodes = new ArrayList<>();

    @Embedded("links")
    private List<HashMap<String, Integer>> links = new ArrayList();

    private double emotion;

    private String lastData;



    /**
     * save a outcome instance to database
     * @param outcome
     */
    public static void save(Outcome outcome) {
        Datastore datastore = DbUtil.getDataStore();
        datastore.save(outcome);
    }

    /**
     * get Outcome from database by id
     * @param id
     * @return
     */
    public static Outcome getResult(String id) {
        Datastore datastore = DbUtil.getDataStore();
        List<Outcome> outcomes = datastore.createQuery(Outcome.class)
                .filter("_id",id).asList();
        if (!outcomes.isEmpty()) {
            Outcome outcome = outcomes.get(0);
            outcome.setLastData(String.valueOf(new Date()));
            Outcome.save(outcome);
            return outcome;
        } else {
            return null;
        }
    }


    /****************************************************
     *                          setter and getter                                 *
     *                          of all attributes                                    *
     ****************************************************/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public HashMap<String,String> getKeyUser() {
        return keyUser;
    }

    public List<HashMap<String,String>> getRepostPath() {
        return repostPath;
    }

    public void setRepostPath(List<HashMap<String,String>> repostPath) {
        this.repostPath = repostPath;
    }

    public void setKeyUser(HashMap<String,String> keyUser) {
        this.keyUser = keyUser;
    }

    public List<HashMap<String, String>> getKeyRepost() {
        return keyRepost;
    }

    public void setKeyRepost(List<HashMap<String, String>> keyRepost) {
        this.keyRepost = keyRepost;
    }

    public String getLastData() {
        return lastData;
    }

    public void setLastData(String lastData) {
        this.lastData = lastData;
    }

    public double getEmotion() {
        return emotion;
    }

    public void setEmotion(double emotion) {
        this.emotion = emotion;
    }

    public List<HashMap<String, String>> getNodes() {
        return nodes;
    }

    public void setNodes(List<HashMap<String, String>> nodes) {
        this.nodes = nodes;
    }

    public List<HashMap<String, Integer>> getLocations() {
        return locations;
    }

    public void setLocations(List<HashMap<String, Integer>> locations) {
        this.locations = locations;
    }

    public List<HashMap<String, Long>> getTime() {
        return time;
    }

    public void setLinks(List<HashMap<String, Integer>> links) {
        this.links = links;
    }

    public List<HashMap<String, Integer>> getLinks() {
        return links;
    }

    public List<Double> getLayer() {
        return layer;
    }

    public void setLayer(List<Double> layer) {
        this.layer = layer;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setTime(List<HashMap<String, Long>> time) {
        this.time = time;
    }


    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
