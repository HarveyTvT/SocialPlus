package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import utils.DbUtil;
import models.User;
import org.mongodb.morphia.Datastore;
import play.*;
import play.data.Form;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.*;

import views.html.*;

import javax.inject.Inject;

import play.libs.ws.*;

public class Application extends Controller {

    @Inject WSClient ws;

    public Result index() {
        return ok(index.render("fuck"));
    }

    public Result addUser() {
        session("user",request().remoteAddress());
        User user = Form.form(User.class).bindFromRequest().get();
        Datastore datastore = DbUtil.getDataStore();
        datastore.save(user);
        return ok("Got request " + request() + "!");
    }

    public Result getUser(String user) {
        response().setContentType("text/html");
        return redirect(routes.Application.index());
    }

    public Result getUserOptional(String user){
        response().setContentType("text/html");
        if (user != null){
            return ok(user.toUpperCase());
        }
        else{
            return ok("fuck you guys..no parameter");
        }
    }

    public Result jsonTest(){
        JsonNode json = Json.toJson(Form.form().bindFromRequest().get());
        if(json.path("data").findValue("name") == null){
            return notFound("fuck you invalid json");
        }
        Result jsonResult = ok(json);
        return jsonResult;
    }

    @With(ComposingAction.class)
    public Result delegateCall(){
        if (ctx().args.get("secret") != "test"){//get AOP args
            return forbidden("fuck not allow call");
        }
        return ok("Call delagete");
    }


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
        return respond.map(text -> {
            return ok(text);
        });
    }
}
