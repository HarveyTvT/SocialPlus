package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import models.APIRequest.WeiboAPI;
import models.User;
import org.mongodb.morphia.Datastore;
import play.*;
import play.libs.F.Promise;
import play.mvc.*;

import utils.DbUtil;

import javax.inject.Inject;

import play.libs.ws.*;

import java.util.List;


public class Application extends Controller {

    @Inject WSClient ws;


    //delegateCall
    @With(ComposingAction.class)
    public Result delegateCall(){
        if (ctx().args.get("secret") != "test"){//get AOP args
            return forbidden("fuck not allow call");
        }
        return ok("Call delagete");
    }

    //asyncRequest
    public Promise<Result> asyncRequest() {
        WSRequest request = ws.url("http://www.baidu.com");
        System.out.println(request.getUrl());
        Promise<String> respond = request.get().map(response -> {
            return response.getBody();
        });
        respond.onRedeem(value -> {//Promise then when success
            Logger.info(value);
        });
        respond.onFailure(error -> {//Promise error when failure
            Logger.error(error.toString());
        });
        return respond.map(text -> ok(text));
    }


    public Promise<Result> getWeiboMessage(String email,String url){
        Datastore dataStore = DbUtil.getDataStore();
        List<User> users = dataStore.createQuery(User.class)
                .filter("email",email).asList();
        String weiboToken = users.get(0).getToken().getWeibo().get("token");

        WeiboAPI weiboAPI = new WeiboAPI(url, weiboToken, ws);
        Promise<JsonNode> jsonNodePromise = weiboAPI.getSocialMessage(weiboAPI.parseUrlToId(url));

        return jsonNodePromise.map(json -> ok(json));
    }

}
