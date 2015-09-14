package models.Midform;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import utils.DbUtil;

import java.util.List;

/**
 * Created by lizhuoli on 15/9/2.
 */
@Entity("socialuser")
public class SocialUser {
    public SocialUser(){
        name = "name";
        gender = "m";
        weight = "0";
        location = "北京";
    }
    @Id
    private String id;//"uid"
    private String name;//"少雨的夏天"
    private String gender;//"m" or "f"
    private String weight;//percent
    private String location;//"12”

    public static void save(SocialUser user){
        Datastore datastore = DbUtil.getDataStore();
        datastore.save(user);
    }

    public static SocialUser getSocialUser(String id){
        Datastore datastore = DbUtil.getDataStore();
        List<SocialUser> users = datastore.createQuery(SocialUser.class)
                .filter("_id",id).asList();
        if(!users.isEmpty()){
            return users.get(0);
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

}
