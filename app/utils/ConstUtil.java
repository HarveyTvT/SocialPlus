package utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created by lizhuoli on 15/9/2.
 */
public class ConstUtil {
    static{
        Config config = ConfigFactory.load();
        weiboAppKey = config.getString("app.weibo.appkey");
        weiboSecret = config.getString("app.weibo.secret");
        weiboRedirectUrl = config.getString("app.weibo.redirectURL");
    }

    public static final String UrlRegex =
            "^((http[s]?|ftp):\\/)?\\/?([^:\\/\\s]+)((\\/\\w+)*\\/)([\\w\\-\\.]+[^#?\\s]+)(.*)?(#[\\w\\-]+)?$";

    /**
     * SHA1 salt string
     */
    public static final String SHA1_SALT=
             "3faf0c40f18ecd2455671a4aaa88855ea4f85ad7";

    /**
     * weibo auth url (which is clicked in the browser)
     */
    public static final String weiboAuthUrl = String.format("https://api.weibo.com/oauth2/authorize?client_id=%s&redirect_uri=%s&response_type=code",
            ConstUtil.weiboAppKey,ConstUtil.weiboRedirectUrl);

    /**
     * renren auth url (which is clicked in the browser)
     */
    public static final String renrenAuthUrl = "http://www.renren.com/callback";

    /*****************************************************************
      　　　　　　　　　微博相关的信息　　　　　　　　　　　
     *****************************************************************/
    public static final String weiboAppKey;

    public static final String weiboSecret;

    public static final String weiboRedirectUrl;
}
