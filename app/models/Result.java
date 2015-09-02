package models;

import org.mongodb.morphia.annotations.*;
import java.util.List;
import java.util.ArrayList;

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

    @Property("tags")
    private List<String> tags = new ArrayList<String>();

    @Embedded("time")
    private List<Time> time = new ArrayList<Time>();

    @Embedded("gender")
    private Gender gender = new Gender();


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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
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
