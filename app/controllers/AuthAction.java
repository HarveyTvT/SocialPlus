package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Account.Token;
import models.Account.User;
import models.AsyncRequest;
import play.libs.F.Promise;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.authResult;

import javax.inject.Inject;
import java.util.HashMap;

import static utils.ConstUtil.*;

/**
 * Created by lizhuoli on 15/8/31.
 */
public class AuthAction extends Controller {
    @Inject WSClient ws;
    
    @Security.Authenticated(Secured.class)
    public Promise<Result> weiboCallback(){
        String email = session("email");
//        if (email == null){
//            return Promise.promise(() -> forbidden("Not login"));
//        }

        String code = request().getQueryString("code");
        String baseURL = "https://api.weibo.com/oauth2/access_token";
        String parameter = String.format("client_id=%s&client_secret=%s&" +
                        "grant_type=authorization_code&redirect_uri=%s&code=%s",
                weiboAppKey, weiboSecret,weiboRedirectUrl, code);

        AsyncRequest request = new AsyncRequest(ws,baseURL,null);
        User user = User.getUser(email);
        Token token = new Token();

        Promise<JsonNode> jsonNodePromise = request.post(parameter);
        return jsonNodePromise.map(value -> {
            try {
                String weiboToken = value.findPath("access_token").asText();
                String weiboExpireTime = value.findPath("expires_in").asText();

                HashMap<String, String> tokenMap = new HashMap<>();
                tokenMap.put("token", weiboToken);
                tokenMap.put("expire", weiboExpireTime);
                token.setWeibo(tokenMap);
                user.setToken(token);

                return ok(authResult.render("微博","成功"));
            }
            catch (Exception e){
                return forbidden(authResult.render("微博","失败"));
            }
        });
    }
}
