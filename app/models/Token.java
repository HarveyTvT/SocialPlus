package models;

import java.util.*;

/**
 * Created by harvey on 15-8-31.
 */
public class Token {

    private Map<String,String> weibo = new HashMap<>();

    private Map<String,String> renren = new HashMap<>();

    /****************************************************
     *                          setter and getter                                 *
     *                          of all attributes                                    *
     ****************************************************/

    public Map<String,String> getWeibo() {
        return weibo;
    }

    public void setWeibo(Map<String,String> weibo) {
        this.weibo = weibo;
    }

    public Map<String,String> getRenren() {
        return renren;
    }

    public void setRenren(Map<String,String> renren) {
        this.renren = renren;
    }
}
