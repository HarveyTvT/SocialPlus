package models.Midform;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import utils.DbUtil;

import java.util.List;

/**
 * Created by lizhuoli on 15/8/31.
 */
@Entity("socialmessage")
public class SocialMessage {

    @Id
    private String id;//"weibo-uid"
    private String createTime;//"Mon Oct 27 11:04:04 +0800 2014"
    private String content;//"content"
    private String[] tags;
    private String[] repostList;
    private String source;
    private String location;//"12“
    private String repostCount;//”123“
    private String commentCount;//"213"
    private String author;
    private String[] comments;

    public static void save(SocialMessage socialMessage){
        Datastore datastore = DbUtil.getDataStore();
        datastore.save(socialMessage);
    }

    public static void update(SocialMessage socialMessage){
        Datastore datastore = DbUtil.getDataStore();
        Query<SocialMessage> query = datastore.createQuery(SocialMessage.class).field("_id").equal(Long.parseLong(socialMessage.getId()));
        UpdateOperations<SocialMessage> ops = datastore.createUpdateOperations(SocialMessage.class)
                .set("repostList",socialMessage.getRepostList());
        datastore.update(query,ops);
    }

    public static SocialMessage getSocialMessage(String id){
        Datastore datastore = DbUtil.getDataStore();
        List<SocialMessage> socialMessages = datastore.createQuery(SocialMessage.class)
                .filter("_id", Long.parseLong(id)).asList();
        if(!socialMessages.isEmpty()){
            return socialMessages.get(0);
        }else{
            return null;
        }
    }


    /*****************************************************************
                        Setter and getter of all attributes
     *****************************************************************/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getRepostList() {
        return repostList;
    }

    public void setRepostList(String[] repostList) {
        this.repostList = repostList;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String  source) {
        this.source = source;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRepostCount() {
        return repostCount;
    }

    public void setRepostCount(String repostCount) {
        this.repostCount = repostCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String  getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String[] getComments() {
        return comments;
    }

    public void setComments(String[] comments) {
        this.comments = comments;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
