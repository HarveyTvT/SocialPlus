package controllers;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by harvey on 15-9-3.
 * This is an class to secure index and admin page
 */
public class Secured extends Security.Authenticator {
    @Override
    public Result onUnauthorized(Http.Context context) {
        return redirect(routes.UserAction.login());
    }

    @Override
    public String getUsername(Http.Context context) {
        return context.session().get("email");
    }
}
