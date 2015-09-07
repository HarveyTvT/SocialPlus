package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import models.APIRequest.WeiboAPI;
import models.Account.User;
import play.Logger;
import play.libs.F.Promise;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;


public class Application extends Controller {
    @Inject WSClient ws;

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


    public Promise<Result> getWeiboUser(String url){
        String email = session("email");
        if (email == null){
            forbidden("not login");
        }
        User user = User.getUser(email);
        String weiboToken = user.getToken().getWeibo().get("token");

        WeiboAPI weiboAPI = new WeiboAPI(url, weiboToken, ws);
        Promise<JsonNode> jsonNodePromise = weiboAPI.getSocialMessage(weiboAPI.parseUrlToId(url));

        return jsonNodePromise.map(json -> ok(json));
    }

    public Promise<Result> getWeiboMessage(String url){
        String email = session("email");
        if (email == null){
            forbidden("not login");
        }
        User user = User.getUser(email);
        String weiboToken = user.getToken().getWeibo().get("token");

        WeiboAPI weiboAPI = new WeiboAPI(url, weiboToken, ws);
        Promise<JsonNode> jsonNodePromise = weiboAPI.getSocialMessage(weiboAPI.parseUrlToId(url));

        return jsonNodePromise.map(json -> ok(json));
    }

    public Promise<Result> getWeiboComment(String url){
        String email = session("email");
        if (email == null){
            forbidden("not login");
        }
        User user = User.getUser(email);
        String weiboToken = user.getToken().getWeibo().get("token");

        WeiboAPI weiboAPI = new WeiboAPI(url, weiboToken, ws);
        Promise<JsonNode> jsonNodePromise = weiboAPI.getSocialMessage(weiboAPI.parseUrlToId(url));

        return jsonNodePromise.map(json -> ok(json));
    }

    public Promise<Result> getWeiboUserList(String url){
        String email = session("email");
        if (email == null){
            forbidden("not login");
        }
        User user = User.getUser(email);
        String weiboToken = user.getToken().getWeibo().get("token");

        WeiboAPI weiboAPI = new WeiboAPI(url, weiboToken, ws);
        Promise<JsonNode> jsonNodePromise = weiboAPI.getSocialMessage(weiboAPI.parseUrlToId(url));

        return jsonNodePromise.map(json -> ok(json));
    }

    public Promise<Result> getWeiboMessageList(String url){
        String email = session("email");
        if (email == null){
            forbidden("not login");
        }
        User user = User.getUser(email);
        String weiboToken = user.getToken().getWeibo().get("token");

        WeiboAPI weiboAPI = new WeiboAPI(url, weiboToken, ws);
        Promise<JsonNode> jsonNodePromise = weiboAPI.getSocialMessage(weiboAPI.parseUrlToId(url));

        return jsonNodePromise.map(json -> ok(json));
    }
    public Promise<Result> getWeiboCommentList(String url){
        String email = session("email");
        if (email == null){
            forbidden("not login");
        }
        User user = User.getUser(email);
        String weiboToken = user.getToken().getWeibo().get("token");

        WeiboAPI weiboAPI = new WeiboAPI(url, weiboToken, ws);
        Promise<JsonNode> jsonNodePromise = weiboAPI.getSocialMessage(weiboAPI.parseUrlToId(url));

        return jsonNodePromise.map(json -> ok(json));
    }
}
