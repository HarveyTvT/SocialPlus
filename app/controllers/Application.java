package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import models.APIRequest.WeiboAPI;
import models.APIRequest.WeiboUtils;
import models.Account.User;
import models.Midform.SocialMessage;
import models.Process.AfterProcess;
import models.Process.PreProcess;
import models.RawConverter.RawConverter;
import models.RawConverter.WeiboConverter;
import play.Logger;
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
        Promise<JsonNode> jsonNodePromise = weiboAPI.getSocialMessage(WeiboUtils.parseUrlToId(url));

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
        Promise<JsonNode> jsonNodePromise = weiboAPI.getSocialMessageList(WeiboUtils.parseUrlToId(url));

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
        String id = WeiboUtils.parseUrlToId(url);

        PreProcess preProcess = new PreProcess(weiboToken);
        preProcess.getMessageRecursion(id);
//        AfterProcess afterProcess = new AfterProcess();
//        SocialMessage message = SocialMessage.getSocialMessage(id);
//        afterProcess.workFlow(message);

        return ok("All ok");
    }

    @Security.Authenticated(Secured.class)
    public Result weiboEntryIdTest(String uid,String id){
        String email = session("email");
        User user = User.getUser(email);
        String weiboToken = user.getToken().getWeibo().get("token");
        String weiboUrl = WeiboUtils.parseIdToUrl(uid,id);

        PreProcess preProcess = new PreProcess(weiboToken);
        preProcess.workFlow(id);
//        AfterProcess afterProcess = new AfterProcess();
//        SocialMessage message = SocialMessage.getSocialMessage(id);
//        afterProcess.workFlow(message);

        return ok("All ok");
    }

    @Security.Authenticated(Secured.class)
    public Result urlToId(String url) {
        String email = session("email");
        User user = User.getUser(email);
        String weiboToken = user.getToken().getWeibo().get("token");

        return ok(WeiboUtils.parseUrlToId(url));
    }

    @Security.Authenticated(Secured.class)
    public Result idToUrl(String uid,String id){
        String email = session("email");
        User user = User.getUser(email);
        String weiboToken = user.getToken().getWeibo().get("token");

        return ok(WeiboUtils.parseIdToUrl(uid, id));
    }
}
