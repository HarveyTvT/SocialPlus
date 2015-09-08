package models.Results;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import utils.DbUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by harvey on 15-8-31.
 */
@Entity("result")
public class Result {
    @Id
    private String id;

    private String url;

    private String content;

    private String author;

    private String lastData;

    private double emotion;

    @Embedded("nodes")
    private List<Node> nodes = new ArrayList<Node>();

    @Embedded("locations")
    private List<Location> locations = new ArrayList<Location>();

    @Embedded("links")
    private List<Link> links = new ArrayList<Link>();

    private String[] tags ;

    @Embedded("time")
    private List<Time> time = new ArrayList<Time>();

    @Embedded("gender")
    private Gender gender = new Gender();

    /**
     * save a result instance to database
     * @param result
     */
    public static void save(Result result) {
        Datastore datastore = DbUtil.getDataStore();
        datastore.save(result);
    }

    /**
     * get Result from database by id
     * @param id
     * @return
     */
    public static Result getResult(String id){
        Datastore datastore = DbUtil.getDataStore();
        List<Result> results = datastore.createQuery(Result.class)
                .filter("_id",id).asList();
        if(!results.isEmpty()){
            Result result = results.get(0);
            result.setLastData(String.valueOf(new Date()));
            Result.save(result);
            return result;
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

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public List<Time> getTime() {
        return time;
    }

    public void setTime(List<Time> time) {
        this.time = time;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
