package models;


import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by harvey on 15-8-31.
 */
@Entity("user")
public class User  {

    public User(){

    }

    @Id
    private String id;

    private String email;

    private String password;

    private boolean validated;

    @Embedded("token")
    private Token token;


    @Embedded("result_id")
    private List<String> result_id = new ArrayList<String>();


    /****************************************************
     *                          setter and getter                                 *
     *                          of all attributes                                    *
     ****************************************************/

    public List<String> getResult_id() {
        return result_id;
    }

    public void setResult_id(List<String> result_id) {
        this.result_id = result_id;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
