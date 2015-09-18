package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Account.User;
import play.libs.oauth.OAuth;
import play.libs.oauth.OAuth.*;
import play.mvc.Security;
import utils.ConstUtil;
import views.html.*;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.HashMap;

import com.google.common.base.Strings;

/**
 * Created by lizhuoli on 15/8/31.
 */
public class AuthAction extends Controller {

    @Security.Authenticated(Secured.class)
    public Promise<Result> weiboAuth(){
        String email = session("email");
        String code = request().getQueryString("code");
        String baseURL = ConstUtil.weiboAccessTokenUrl;
        String parameter = String.format("client_id=%s&client_secret=%s&grant_type=authorization_code&redirect_uri=%s&code=%s",
                ConstUtil.weiboAppKey, ConstUtil.weiboSecret, ConstUtil.weiboRedirectUrl, code);

        AsyncRequest request = new AsyncRequest(baseURL, null);

        Promise<JsonNode> jsonNodePromise = request.post(parameter);
        return jsonNodePromise.map(value -> {
            try {
                String weiboToken = value.findPath("access_token").asText();
                Long weiboExpireTime = value.findPath("expires_in").asLong();

                User user = User.getUser(email);
                HashMap<String, String> tokenMap = new HashMap<>();
                session("token",weiboToken);
                tokenMap.put("token", weiboToken);
                tokenMap.put("expire", String.format("%d",System.currentTimeMillis() + weiboExpireTime));
                user.getToken().setWeibo(tokenMap);
                User.saveUser(user);

                return redirect(routes.UserAction.index());
            }
            catch (Exception e){
                return redirect(routes.UserAction.login());
            }
        });
    }

    @Security.Authenticated(Secured.class)
    public Result twitterAuth() {
        String email = session("email");
        ConsumerKey key = new ConsumerKey(ConstUtil.twitterAppKey, ConstUtil.twitterSecret);

        OAuth twitter = new OAuth(new ServiceInfo(
                "https://api.twitter.com/oauth/request_token",
                "https://api.twitter.com/oauth/access_token",
                "https://api.twitter.com/oauth/authorize", key)
        );
        String verifier = request().getQueryString("oauth_verifier");

        if (Strings.isNullOrEmpty(verifier)) {
            RequestToken requestToken = twitter.retrieveRequestToken(ConstUtil.twitterRedirectUrl);
            session("token", requestToken.token);
            session("secret", requestToken.secret);
            return forbidden(authResult.render("Twitter", "失败"));
        } else {
            RequestToken requestToken;
            if (session().containsKey("token")) {
                requestToken = new RequestToken(session("token"), session("secret"));
            } else {
                requestToken = null;
            }
            RequestToken accessToken = twitter.retrieveAccessToken(requestToken, verifier);
            session("token", accessToken.token);
            session("secret", accessToken.secret);
            User user = User.getUser(email);
            HashMap<String, String> tokenMap = new HashMap<>();
            tokenMap.put("token", accessToken.token);
            tokenMap.put("expire", String.format("%s",Long.MAX_VALUE));//Twitter token no expire time
            user.getToken().setTwitter(tokenMap);
            User.saveUser(user);

            return ok(authResult.render("Twitter", "成功"));
        }
    }
}
