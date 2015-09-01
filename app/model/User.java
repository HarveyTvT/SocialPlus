package model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

/**
 * Created by lizhuoli on 15/8/25.
 */
@Entity("user")
public class User {
    @Id
    private ObjectId id;
    private String name;
    private Integer gender;
    private Integer location;
    private Integer commentNum;
    private Integer messageNum;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Integer getMessageNum() {
        return messageNum;
    }

    public void setMessageNum(Integer messageNum) {
        this.messageNum = messageNum;
    }

    @Override
    public String toString() {
        return "name: " + name + "\ngender: " + gender;
    }
}