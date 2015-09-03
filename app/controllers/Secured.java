package controllers;

import play.data.Form;
import play.mvc.Http.Context;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import models.*;

/**
 * Created by harvey on 15-9-3.
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
