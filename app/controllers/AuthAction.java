package controllers;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import models.AsyncRequest;
import play.mvc.*;
import play.libs.ws.*;
import play.libs.F.Promise;

/**
 * Created by lizhuoli on 15/8/31.
 */
public class AuthAction extends Controller {
    @Inject WSClient ws;
    private String weiboAppKey;
    private String weiboAppSecret;
    private String weiboRedirectURL;

    public Promise<Result> weiboCallback(){
        Config config = ConfigFactory.load();
        weiboAppKey = config.getString("app.weibo.appkey");
        weiboAppSecret = config.getString("app.weibo.secret");
        weiboRedirectURL = config.getString("app.weibo.redirectURL");

        String code = request().getQueryString("code");
        String baseURL = "https://api.weibo.com/oauth2/access_token";
        String parameter = String.format("client_id=%s&client_secret=%s&grant_type=authorization_code&redirect_uri=%s&code=%s",
                weiboAppKey,weiboAppSecret,weiboRedirectURL,code);

        AsyncRequest request = new AsyncRequest(ws,baseURL,null);

//        User user = new User();

        Promise<JsonNode> jsonNodePromise = request.post(parameter);
        return jsonNodePromise.map(value -> {
            String weiboToken = value.findPath("access_token").asText();
            Integer weiboExpireTime = value.findPath("expires_in").asInt();
            String weiboUid = value.findPath("uid").asText();
//            user.setToken(weiboToken);
//            user.setExpireTime(weiboExpireTime);
            return ok(weiboToken);
        });
    }



}
