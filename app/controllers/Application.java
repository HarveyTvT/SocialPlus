package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import models.APIRequest.WeiboAPI;
import models.Account.User;
import models.RawConverter.RawConverter;
import models.RawConverter.WeiboConverter;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.util.List;


public class Application extends Controller {

    @Security.Authenticated(Secured.class)
    public Promise<Result> getWeiboUser(String uid){
        String email = session("email");
        User user = User.getUser(email);
        String weiboToken = user.getToken().getWeibo().get("token");

        WeiboAPI weiboAPI = new WeiboAPI(weiboToken);
        Promise<JsonNode> jsonNodePromise = weiboAPI.getSocialUser(uid);

        return jsonNodePromise.map(json -> ok(json));
    }

    @Security.Authenticated(Secured.class)
    public Promise<Result> getWeiboMessage(String url){
        String email = session("email");
        User user = User.getUser(email);
        String weiboToken = user.getToken().getWeibo().get("token");

        WeiboAPI weiboAPI = new WeiboAPI(weiboToken);
        Promise<JsonNode> jsonNodePromise = weiboAPI.getSocialMessage(weiboAPI.parseUrlToId(url));

        return jsonNodePromise.map(json -> ok(json));
    }

    @Security.Authenticated(Secured.class)
    public Promise<Result> getWeiboComment(String cid){
        String email = session("email");
        User user = User.getUser(email);
        String weiboToken = user.getToken().getWeibo().get("token");

        WeiboAPI weiboAPI = new WeiboAPI(weiboToken);
        Promise<JsonNode> jsonNodePromise = weiboAPI.getSocialComment(cid);

        return jsonNodePromise.map(json -> ok(json));
    }

    @Security.Authenticated(Secured.class)
    public Promise<Result> getWeiboUserList(String uids){
        String email = session("email");
        User user = User.getUser(email);
        String weiboToken = user.getToken().getWeibo().get("token");

        WeiboAPI weiboAPI = new WeiboAPI(weiboToken);
        Promise<JsonNode> jsonNodePromise = weiboAPI.getSocialUserList(uids);

        return jsonNodePromise.map(json -> ok(json));
    }

    @Security.Authenticated(Secured.class)
    public Promise<Result> getWeiboMessageList(String url){
        String email = session("email");
        User user = User.getUser(email);
        String weiboToken = user.getToken().getWeibo().get("token");

        WeiboAPI weiboAPI = new WeiboAPI(weiboToken);
        Promise<JsonNode> jsonNodePromise = weiboAPI.getSocialMessageList(weiboAPI.parseUrlToId(url));

        return jsonNodePromise.map(json -> ok(json));
    }

    @Security.Authenticated(Secured.class)
    public Promise<Result> getWeiboCommentList(String cids){
        String email = session("email");
        User user = User.getUser(email);
        String weiboToken = user.getToken().getWeibo().get("token");

        WeiboAPI weiboAPI = new WeiboAPI(weiboToken);
        Promise<JsonNode> jsonNodePromise = weiboAPI.getSocialCommentList(cids);

        return jsonNodePromise.map(json -> ok(json));
    }

    @Security.Authenticated(Secured.class)
    public Result weiboEntryTest(String url){
        String email = session("email");
        User user = User.getUser(email);
        String weiboToken = user.getToken().getWeibo().get("token");

        WeiboAPI weiboAPI = new WeiboAPI(weiboToken);
        RawConverter converter = new WeiboConverter(weiboToken,url);

        String id = weiboAPI.parseUrlToId(url);
        Promise<List<JsonNode>> promiseList = Promise.sequence(weiboAPI.getSocialMessageList(id));

        converter.convertMessage(promiseList);

        return ok("Database insert ok");
    }

    public Result url(String url) {
        String email = session("email");
        User user = User.getUser(email);
        String weiboToken = user.getToken().getWeibo().get("token");

        WeiboAPI weiboAPI = new WeiboAPI(weiboToken);
        return ok(weiboAPI.parseUrlToId(url));
    }
}
