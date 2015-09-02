package models;

import java.util.*;

/**
 * Created by harvey on 15-8-31.
 */
public class Token {

    private List<String> weibo = new ArrayList<String>();

    private List<String> renren = new ArrayList<String>();

    /****************************************************
     *                          setter and getter                                 *
     *                          of all attributes                                    *
     ****************************************************/

    public List<String> getWeibo() {
        return weibo;
    }

    public void setWeibo(List<String> weibo) {
        this.weibo = weibo;
    }

    public List<String> getRenren() {
        return renren;
    }

    public void setRenren(List<String> renren) {
        this.renren = renren;
    }
}
