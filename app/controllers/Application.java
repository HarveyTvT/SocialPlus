package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import model.User;
import org.junit.Assert;
import play.*;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;

import scala.reflect.internal.FatalError;
import views.html.*;

public class Application extends Controller {

    public Result index() {
        return ok(index.render("fuck"));
    }

    public Result addUser() {
        session("user",request().remoteAddress());
        User user = Form.form(User.class).bindFromRequest().get();
        System.out.println(user.toString());
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
}
