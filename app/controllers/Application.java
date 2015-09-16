package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.APIRequest.WeiboAPI;
import models.APIRequest.WeiboUtils;
import models.Account.User;
import models.Midform.SocialMessage;
import models.Process.AfterProcess;
import models.Process.PreProcess;
import models.Results.Outcome;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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

    @Security.Authenticated(Secured.class)
    public Result getWeibo(String url){
        String baseUrl = "http://sinacn.weibodangan.com/user/%s/?status=%s";
        String uid = WeiboUtils.parseUrlToUid(url);
        String id = WeiboUtils.parseUrlToId(url);
        String thirdPartUrl = String.format(baseUrl, uid, id);

        Document document;
        try {
            document = Jsoup.connect(thirdPartUrl).get();
        }
        catch (Exception e){
            e.printStackTrace();
            return internalServerError("");
        }
        String weiboDivId = String.format("weibo%s", id);
        Element div = document.getElementById(weiboDivId);
        String weiboContent = div.getElementsByTag("span").first().text();
        weiboContent = WeiboUtils.delHTMLTag(weiboContent);

        Elements span = div.getElementsByTag("div").get(0)
                .getElementsByTag("small").get(0)
                .getElementsByTag("span");

        String postWay = span.get(0).text();
        String datetime = span.get(1).text();
        String repostCount = span.get(3).text();
        String commentCount = span.get(5).text();

        ObjectNode json = Json.newObject();
        json.put("content",weiboContent);
        json.put("postWay",postWay);
        json.put("datetime",datetime);
        json.put("repostCount",repostCount);
        json.put("commentCount", commentCount);

        return ok(json);
    }
}
