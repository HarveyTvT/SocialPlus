package controllers;

import play.Logger;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;

/**
 * Created by lizhuoli on 15/8/27.
 */
public class ComposingAction extends play.mvc.Action.Simple{
    /*
    This is a simple AOP logger.

    @Security.Authenticated -- use this before controller to make entire controller AOP
    */
    @Override
    public F.Promise<Result> call(Http.Context context) throws Throwable {
        Logger.debug("An request call: " + context.toString());
        context.args.put("secret","test");//pass an custome args AOP
        return delegate.call(context);
    }
}
