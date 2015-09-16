package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.APIRequest.WeiboUtils;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import models.Results.Outcome;
import play.mvc.Result;
import play.mvc.Security;
import views.html.result;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by harvey on 15-9-9.
 * 这个类是系统分析服务的入口,
 * 返回分析后的页面
 */
public class Service extends Controller {
    @Security.Authenticated(Secured.class)
    public Result analysis() {
        String url = Form.form().bindFromRequest().get("url");
        return ok(result.render(url));
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

}
