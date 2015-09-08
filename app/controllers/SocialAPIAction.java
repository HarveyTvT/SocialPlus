package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.APIRequest.WeiboAPI;
import models.Account.Token;
import models.Account.User;
import play.mvc.Controller;
import play.mvc.Security;

/**
 * Created by lizhuoli on 15/9/2.
 */
public class SocialAPIAction extends Controller {

    private Token token;
    private String url;


    SocialAPIAction(String socialUrl){
        super();
        String email = session("email");
        token = User.getUser(email).getToken();
        url = socialUrl;
    }

    @Security.Authenticated(Secured.class)
    public JsonNode getSocialUser(){
        switch (url){
            case "weibo":{
                String weiboToken = token.getWeibo().get("token");
                WeiboAPI weiboAPI = new WeiboAPI(weiboToken);
            }
        }
        return null;
    }
    public JsonNode getSocialMessage(){
        return null;
    }
    public JsonNode getSocialComment(){
        return null;
    }
    public JsonNode getSocialTimeLine(){
        return null;
    }

}
