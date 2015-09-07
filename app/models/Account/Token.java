package models.Account;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by harvey on 15-8-31.
 */
public class Token {

    private Map<String,String> weibo = new HashMap<>();

    private Map<String,String> renren = new HashMap<>();

    private Map<String,String> twitter = new HashMap<>();

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

    public Map<String, String> getTwitter() {
        return twitter;
    }

    public void setTwitter(Map<String, String> twitter) {
        this.twitter = twitter;
    }

    public Map[] getAll(){
        Map<String,String>[] maps = new HashMap[3];
        maps[0] = weibo;
        maps[1] = renren;
        maps[2] = twitter;
        return maps;
    }

}
