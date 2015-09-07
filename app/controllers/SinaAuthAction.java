package controllers;

import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

import static utils.ConstUtil.WeiboRedirectUrl;

/**
 * Created by harvey on 15-9-6.
 */
public class SinaAuthAction extends Controller {
    @Inject
    WSClient ws;

    private static final String AppKey = "3062699641";
    private static final String AppSecret = "b5e1bb821b41789627c1cd07de4a3d81";

    public Result getAuth(){
        String SinaAuthUrl = String.format("https://api.weibo.com/oauth2/authorize?" +
                "client_id=%s&redirect_url=%s&display=%s&response_type=%s&forcelogin=true",
                AppKey,WeiboRedirectUrl,
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
