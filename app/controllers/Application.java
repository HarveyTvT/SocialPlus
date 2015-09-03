package controllers;


import play.*;
import play.libs.F.Promise;
import play.mvc.*;

import utils.EncryptUtil;
import utils.MailUtil;
import views.html.*;

import javax.inject.Inject;

import play.libs.ws.*;


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
        return respond.map(text -> {
            return ok(text);
        });
    }

}
