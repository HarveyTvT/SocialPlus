package utils;

/**
 * Created by lizhuoli on 15/9/2.
 */
public abstract class ConstUtil {
    public final static String getURLRegex(){
        return "/^((http[s]?|ftp):\\/)?\\/?([^:\\/\\s]+)((\\/\\w+)*\\/)([\\w\\-\\.]+[^#?\\s]+)(.*)?(#[\\w\\-]+)?$/";
    }
}
