package models.Midform;

/**
 * Created by lizhuoli on 15/9/2.
 */
public class SocialUser {
    public SocialUser(){
        name = "name";
        gender = "m";
        weight = "0";
        location = "北京";
    }

    private String id;
    private String name;
    private String gender;
    private String weight;
    private String location;
    private SocialUser[] friendList;

    /*****************************************************************
                                Setter and getter of all attributes
     *****************************************************************/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public SocialUser[] getFriendList() {
        return friendList;
    }

    public void setFriendList(SocialUser[] friendList) {
        this.friendList = friendList;
    }
}
