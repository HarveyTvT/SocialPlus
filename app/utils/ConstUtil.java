package utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created by lizhuoli on 15/9/2.
 */
public class ConstUtil {
    public static final Config config = ConfigFactory.load();

    public static final String UrlRegex =
            "^((http[s]?|ftp):\\/)?\\/?([^:\\/\\s]+)((\\/\\w+)*\\/)([\\w\\-\\.]+[^#?\\s]+)(.*)?(#[\\w\\-]+)?$";

    public static final String SHA1_SALT=
             "3faf0c40f18ecd2455671a4aaa88855ea4f85ad7";

    public static final String weiboAuthUrl = String.format("https://api.weibo.com/oauth2/authorize?client_id=%s&redirect_uri=%s&response_type=code",weiboAppKey,weiboRedirectUrl);

    public static final String renrenAuthUrl = "http://www.renren.com/callback";

    public static final int weiboExipreMax = 86400;

    public static final int renrenExpireMax = 86400;
    public static final String XinZhiToken =
            "23ICNCQ44G";

    /*****************************************************************
      　　　　　　　　　微博相关的信息　　　　　　　　　　　
     *****************************************************************/
    public static final String weiboAppKey = config.getString("app.weibo.appkey");

    public static final String weiboSecret = config.getString("app.weibo.secret");

    public static final String weiboRedirectUrl = config.getString("app.weibo.redirectURL");
}
