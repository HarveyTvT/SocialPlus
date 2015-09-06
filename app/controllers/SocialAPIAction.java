package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.APIRequest.WeiboAPI;
import org.mongodb.morphia.Datastore;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import utils.DbUtil;

import javax.inject.Inject;

/**
 * Created by lizhuoli on 15/9/2.
 */
public class SocialAPIAction extends Controller {
    @Inject
    WSClient ws;
    private Datastore datastore = DbUtil.getDataStore();
    private String url;

    SocialAPIAction(String socialUrl){
        url = socialUrl;
    }

    public JsonNode getSocialUser(){
        switch (url){
            case "weibo":{
                String weiboToken = "";
                WeiboAPI weiboAPI = new WeiboAPI(url ,weiboToken, ws);
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
