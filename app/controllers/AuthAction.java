package controllers;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import models.AsyncRequest;
import models.Token;
import models.User;
import org.mongodb.morphia.Datastore;
import play.mvc.*;
import play.libs.ws.*;
import play.libs.F.Promise;
import utils.DbUtil;

import java.util.HashMap;

/**
 * Created by lizhuoli on 15/8/31.
 */
public class AuthAction extends Controller {
    @Inject WSClient ws;
    private Datastore datastore = DbUtil.getDataStore();
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

        User user = new User();
        Token token = new Token();

        Promise<JsonNode> jsonNodePromise = request.post(parameter);
        return jsonNodePromise.map(value -> {
            String weiboToken = value.findPath("access_token").asText();
            String weiboExpireTime = value.findPath("expires_in").asText();
//            String weiboUid = value.findPath("uid").asText();

            HashMap<String,String> tokenMap = new HashMap<>();
            tokenMap.put("token",weiboToken);
            tokenMap.put("expire",weiboExpireTime);
            token.setWeibo(tokenMap);
            user.setToken(token);

            datastore.save(user);
            return ok(weiboToken);
        });
    }



}
