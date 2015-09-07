package utils;
/**
 * Created by lizhuoli on 15/9/2.
 */
public class ConstUtil {
    public static final String UrlRegex =
            "^((http[s]?|ftp):\\/)?\\/?([^:\\/\\s]+)((\\/\\w+)*\\/)([\\w\\-\\.]+[^#?\\s]+)(.*)?(#[\\w\\-]+)?$";

    public static final String SHA1_SALT=
             "3faf0c40f18ecd2455671a4aaa88855ea4f85ad7";

    public static final String XinZhiToken =
            "23ICNCQ44G";

    /*****************************************************************
      　　　　　　　　　微博相关的信息　　　　　　　　　　　
     *****************************************************************/
    public static final String WeiboAppKey =
            "1715460016";

    public static final String WeiboSecret =
            "158bd3bebbe5382c22b1ec4fddfc1a66";

    public static final String WeiboRedirectUrl =
            "http://121.249.8.20:9000/api/weibo/auth";
}
