package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.APIRequest.WeiboUtils;
import models.Account.User;
import models.Midform.SocialMessage;
import models.Process.AfterProcess;
import models.Process.PreProcess;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import models.Results.Outcome;
import play.mvc.Result;
import play.mvc.Security;
import views.html.query;
import views.html.result;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by harvey on 15-9-9.
 * 这个类是系统分析服务的入口,
 * 返回分析后的页面
 */
public class Service extends Controller {
    @Security.Authenticated(Secured.class)
    public Result analysis() {
        String url = Form.form().bindFromRequest().get("url");
        session("url",url);
        String email = session("email");
        User user = User.getUser(email);
        String weiboToken = user.getToken().getWeibo().get("token");
        String id = WeiboUtils.parseUrlToId(url);

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String time = dateFormat.format(date);

        SocialMessage message = SocialMessage.getSocialMessage(id);

        if (message == null){
            return notFound("Not found this message");
        }
        Outcome outcome = Outcome.getResult(id);
        if (outcome == null){
            AfterProcess afterProcess = new AfterProcess();
            PreProcess preProcess = new PreProcess(weiboToken);
            preProcess.workFlow(message);
            outcome = afterProcess.workFlow(message);
        }

        return ok(result.render(url,time));
    }

    @Security.Authenticated(Secured.class)
    public Result getResult(String url){
        String id = WeiboUtils.parseUrlToId(url);
        Outcome outcome = Outcome.getResult(id);
        JsonNode result = Json.toJson(outcome);
        response().setContentType("application/json;charset=utf-8");
        session().remove("url");
        play.Logger.debug("getResult used");
        return ok(result);
    }

    public Result getMap() throws FileNotFoundException {
        response().setContentType("application/json;charset=utf-8");
        File file = new File("/home/harvey/WorkSpace/IdeaProjects/SocialPlus/public/data/China.json");
        InputStream is = new FileInputStream(file);
        JsonNode json = Json.parse(is);
        return ok(json);
    }

    @Security.Authenticated(Secured.class)
    public Result getUserInfo(String url){
        String baseUrl = "http://sinacn.weibodangan.com/user/%s/?status=%s";
        String baseUserUrl = "http://sinacn.weibodangan.com/user/%s";
        String uid = WeiboUtils.parseUrlToUid(url);
        String id = WeiboUtils.parseUrlToId(url);
        String thirdPartUrl = String.format(baseUrl, uid, id);
        String thirdPartUserUrl = String.format(baseUserUrl,uid);

        Document document;
        Document userDoc;
        try {
            document = Jsoup.connect(thirdPartUrl).get();
            userDoc = Jsoup.connect(thirdPartUserUrl).get();
        }
        catch (Exception e){
            e.printStackTrace();
            return internalServerError("");
        }
        Element userElem = userDoc.select(".avatar").get(0);
        String name = userElem.attr("title");
        String avatar = userElem.attr("src");

        Element userCountElem = userDoc.select(".table.table-bordered").get(0);

        String followCount = userCountElem.select("tr:eq(0) td").get(0).text();
        String fanCount = userCountElem.select("tr:eq(1) td").get(0).text();
        String weiboCount = userCountElem.select("tr:eq(2) td").get(0).text();


        String weiboDivId = String.format("weibo%s", id);
        Element div = document.getElementById(weiboDivId);
        String weiboContent = div.getElementsByTag("span").first().text();
        weiboContent = WeiboUtils.delHTMLTag(weiboContent);

        Elements span = div.getElementsByTag("div").get(0)
                .getElementsByTag("small").get(0)
                .getElementsByTag("span");

        String client = span.get(0).text();
        String datetime = span.get(1).text();
        String repostCount = span.get(3).text();
        String commentCount = span.get(5).text();

        ObjectNode json = Json.newObject();
        ObjectNode user = Json.newObject();
        user.put("name",name);
        user.put("avatar",avatar);
        user.put("followCount",followCount);
        user.put("fanCount",fanCount);
        user.put("weiboCount",weiboCount);

        json.put("user",user);
        json.put("content",weiboContent);
        json.put("client",client);
        json.put("datetime",datetime);
        json.put("repostCount",repostCount);
        json.put("commentCount", commentCount);

        return ok(json);
    }

}
