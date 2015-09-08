package models.Midform;

/**
 * Created by lizhuoli on 15/9/2.
 */
public class SocialComment {
    public SocialComment(){
        id = "123412";
        createTime = "11223344";
        content = "nothing";
    }
    private String id;
    private String createTime;
    private String content;


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
