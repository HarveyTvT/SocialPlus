package models.Midform;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import utils.DbUtil;

import java.util.List;

/**
 * Created by lizhuoli on 15/8/31.
 */
@Entity("socialmessage")
public class SocialMessage {
    public SocialMessage(){
        createTime = "Mon Oct 27 11:04:04 +0800 2014";
        url = "http://weibo.com/2691199564/CzhEbku3Z?from=page_1005052691199564_profile&wvr=6&mod=weibotime";
        content = "nothing";
        tags =new String[]{"tag1","tag2" } ;
        location = "beijing";
        repostCount = "123";
        commentCount = "321";
        author = new SocialUser();
        author.setFriendList(
                new SocialUser[]{
                        new SocialUser(), new SocialUser()
                }
        );
        comments = new SocialComment[]{
                new SocialComment(), new SocialComment(), new SocialComment()
        };
    }
    @Id
    private String id;
    private String createTime;
    private String url;
    private String content;
    private String[] tags;
    @Embedded("repostlist")
    private SocialMessage[] repostList;
    @Embedded("source")
    private SocialMessage source;
    private String location;
    private String repostCount;
    private String commentCount;
    @Embedded("author")
    private SocialUser author;
    @Embedded("comments")
    private SocialComment[] comments;

    public static void save(SocialMessage socialMessage){
        Datastore datastore = DbUtil.getDataStore();
        datastore.save(socialMessage);
    }

    public static SocialMessage getSocialMessage(String id){
        Datastore datastore = DbUtil.getDataStore();
        List<SocialMessage> socialMessages = datastore.createQuery(SocialMessage.class)
                .filter("_id",id).asList();
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

    public SocialMessage[] getRepostList() {
        return repostList;
    }

    public void setRepostList(SocialMessage[] repostList) {
        this.repostList = repostList;
    }

    public SocialMessage getSource() {
        return source;
    }

    public void setSource(SocialMessage source) {
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

    public SocialUser getAuthor() {
        return author;
    }

    public void setAuthor(SocialUser author) {
        this.author = author;
    }

    public SocialComment[] getComments() {
        return comments;
    }

    public void setComments(SocialComment[] comments) {
        this.comments = comments;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
