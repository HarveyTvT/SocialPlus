package models.Midform;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import utils.DbUtil;

import java.util.List;

/**
 * Created by lizhuoli on 15/9/2.
 */
@Entity("socialcomment")
public class SocialComment {
    public SocialComment(){}
    @Id
    private String id;//"weibo-uid"
    private String createTime;//"Mon Oct 27 11:04:04 +0800 2014"
    private String content;


    public static void save(SocialComment comment){
        Datastore datastore = DbUtil.getDataStore();
        datastore.save(comment);
    }

    public static SocialComment getSocialComment(String id){
        Datastore datastore = DbUtil.getDataStore();
        List<SocialComment> comments = datastore.createQuery(SocialComment.class)
                    .filter("_id",id ).asList();
        if(!comments.isEmpty()){
            return comments.get(0);
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
}
