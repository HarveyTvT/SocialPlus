package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import models.APIRequest.WeiboAPI;
import models.APIRequest.WeiboUtils;
import models.Account.User;
import models.Midform.SocialMessage;
import models.Process.AfterProcess;
import models.Process.PreProcess;
import models.Results.Outcome;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

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
        SocialMessage message = SocialMessage.getSocialMessage(id);

        if (message == null){
            return notFound("Not this message");
        }
        Outcome outcome = Outcome.getResult(id);
        if (outcome == null){
            AfterProcess afterProcess = new AfterProcess();
            PreProcess preProcess = new PreProcess(weiboToken);
            preProcess.workFlow(message);
            outcome = afterProcess.workFlow(message);
            return ok(Json.toJson(outcome));
        }
        else{
            return ok(Json.toJson(outcome));
        }
    }

    @Security.Authenticated(Secured.class)
    public Result weiboEntryIdTest(String uid,String id){
        String email = session("email");
        User user = User.getUser(email);
        String weiboToken = user.getToken().getWeibo().get("token");
        String weiboUrl = WeiboUtils.parseIdToUrl(uid, id);
        SocialMessage message = SocialMessage.getSocialMessage(id);

        if (message == null){
            return notFound("Not this message");
        }
        Outcome outcome = Outcome.getResult(id);
        if (outcome == null){
            PreProcess preProcess = new PreProcess(weiboToken);
            preProcess.workFlow(message);
            AfterProcess afterProcess = new AfterProcess();
            outcome = afterProcess.workFlow(message);
            return ok(Json.toJson(outcome));
        }
        else{
            return ok(Json.toJson(outcome));
        }
    }

    @Security.Authenticated(Secured.class)
    public Result urlToId(String url) {
        String email = session("email");
        User user = User.getUser(email);
        String weiboToken = user.getToken().getWeibo().get("token");
        String weiboId = WeiboUtils.parseUrlToId(url);
        String weiboUid = WeiboUtils.parseUrlToUid(url);

        return ok(String.format("id:%s\nuid:%s",weiboId,weiboUid));
    }

    @Security.Authenticated(Secured.class)
    public Result idToUrl(String uid,String id){
        String email = session("email");
        User user = User.getUser(email);
        String weiboToken = user.getToken().getWeibo().get("token");

        return ok(WeiboUtils.parseIdToUrl(uid, id));
    }

}
