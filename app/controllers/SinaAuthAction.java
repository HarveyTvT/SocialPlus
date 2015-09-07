package controllers;

import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;
import utils.ConstUtil;

import javax.inject.Inject;

/**
 * Created by harvey on 15-9-6.
 */
public class SinaAuthAction extends Controller {
    @Inject
    WSClient ws;

    public Result getAuth(){
        String SinaAuthUrl = String.format("https://api.weibo.com/oauth2/authorize?" +
                "client_id=%s&redirect_url=%s&display=%s&response_type=%s&forcelogin=true",
                ConstUtil.weiboAppKey,ConstUtil.weiboRedirectUrl,
                "default","code");
        String res = String.format("<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"refresh\" content=\"1;url=%s\" />\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<script type=\"text/javascript\">\n" +
                "x=document.getElementsByTagName(\"meta\")[0];\n" +
                "document.write(\"Http equiv: \" + x.httpEquiv);\n" +
                "</script>\n" +
                "\n" +SinaAuthUrl+
                "</body>\n" +
                "</html>",SinaAuthUrl);
        return ok(res).as("html");
    }
}
