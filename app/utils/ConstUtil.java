package utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created by lizhuoli on 15/9/2.
 */
public class ConstUtil {
    static {
        Config config = ConfigFactory.load();
        weiboAppKey = config.getString("max.weibo.appkey");
        weiboSecret = config.getString("max.weibo.secret");
        weiboRedirectUrl = config.getString("max.weibo.redirectURL");
        twitterAppKey = config.getString("app.twitter.appkey");
        twitterSecret = config.getString("app.twitter.secret");
        twitterRedirectUrl = config.getString("app.twitter.redirectURL");
    }


    public static final String UrlRegex =
            "^((http[s]?|ftp):\\/)?\\/?([^:\\/\\s]+)((\\/\\w+)*\\/)([\\w\\-\\.]+[^#?\\s]+)(.*)?(#[\\w\\-]+)?$";

    public static final String HashtagRegex =
            "#(.+?)#";

    public static final String SHA1_SALT=
             "3faf0c40f18ecd2455671a4aaa88855ea4f85ad7";

    public static final String weiboAuthUrl = String.format("https://api.weibo.com/oauth2/authorize?" +
            "client_id=%s&redirect_uri=%s&" +
            "response_type=code",
            ConstUtil.weiboAppKey,ConstUtil.weiboRedirectUrl);

    public static final String weiboAccessTokenUrl = "https://api.weibo.com/oauth2/access_token";

    /*****************************************************************
      　　　　　　　　　微博相关的信息　　　　　　　　　　　
     *****************************************************************/
    public static final String weiboAppKey;

    public static final String weiboSecret;

    public static final String weiboRedirectUrl;


    public static final String renrenAuthUrl = "https://renren.com";
    /**
     *
     */
    public static final String twitterAppKey;

    public static final String twitterSecret;

    public static final String twitterRedirectUrl;

    public static final String twitterAuthUrl = "https://api.twitter.com/oauth/request_token";
}
