package models.APIRequest;

import play.Logger;
import utils.ConstUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lizhuoli on 15/9/6.
 */

public abstract class WeiboUtils {
    static String[] str62key = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    /**
     * Convert mid to id.
     *
     * @param mid
     * @return
     */
    public static String mid2id(String mid) {
        String id = "";
        String k = mid.toString().substring(3, 4);    //will be used when the 4th char is 0
        if (!k.equals("0")) {
            for (int i = mid.length() - 4; i > -4; i = i - 4) {    //4 chars as a group
                int offset1 = i < 0 ? 0 : i;
                int offset2 = i + 4;
                String str = mid.toString().substring(offset1, offset2);
                str = str62to10(str);    //convert string to decimal base
                // if not the 1st group, add '0's if length < 7
                if (offset1 > 0) {
                    while (str.length() < 7) {
                        str = '0' + str;
                    }
                }
                id = str + id;
            }
        } else {
            for (int i = mid.length() - 4; i > -4; i = i - 4) {
                int offset1 = i < 0 ? 0 : i;
                int offset2 = i + 4;
                if (offset1 > -1 && offset1 < 1 || offset1 > 4) {
                    String str = mid.toString().substring(offset1, offset2);
                    str = str62to10(str);
                    // if not the 1st group, add '0's if length < 7
                    if (offset1 > 0) {
                        while (str.length() < 7) {
                            str = '0' + str;
                        }
                    }
                    id = str + id;
                } else {
                    String str = mid.toString().substring(offset1 + 1, offset2);
                    str = str62to10(str);
                    // if not the 1st group, add '0's if length < 7
                    if (offset1 > 0) {
                        while (str.length() < 7) {
                            str = '0' + str;
                        }
                    }
                    id = str + id;
                }
            }
        }
        return id;
    }


    /**
     * Convert id to mid.
     *
     * @param id
     * @return
     */
    public static String id2mid(String id) {
        String mid = "";
        for (int j = id.length() - 7; j > -7; j = j - 7) {    // 7 chars as a group
            int offset3 = j < 0 ? 0 : j;
            int offset4 = j + 7;
            if ((j > 0 && j < 6) && (id.substring(id.length() - 14, id.length() - 13).equals("0") && id.length() == 19)) {
                String num = id.toString().substring(offset3 + 1, offset4);
                num = int10to62(Integer.valueOf(num));    // convert 10 base to 62 base
                mid = 0 + num + mid;
                if (mid.length() == 9) {
                    mid = mid.substring(1, mid.length());
                }
            } else {
                String num = id.toString().substring(offset3, offset4);
                num = int10to62(Integer.valueOf(num));
                mid = num + mid;
            }
        }
        return mid;
    }

    /**
     * Convert 62 base to 10 base.
     *
     * @param str
     * @return
     */
    public static String str62to10(String str) {
        String i10 = "0";
        int c = 0;
        for (int i = 0; i < str.length(); i++) {
            int n = str.length() - i - 1;
            String s = str.substring(i, i + 1);
            for (int k = 0; k < str62key.length; k++) {
                if (s.equals(str62key[k])) {
                    int h = k;
                    c += (int) (h * Math.pow(62, n));
                    break;
                }
            }
            i10 = String.valueOf(c);
        }
        return i10;
    }

    /**
     * Convert 10 base to 62 base.
     *
     * @param int10
     * @return
     */
    public static String int10to62(double int10) {
        String s62 = "";
        int w = (int) int10;
        int r = 0;
        int a = 0;
        while (w != 0) {
            r = (int) (w % 62);
            s62 = str62key[r] + s62;
            a = (int) (w / 62);
            w = (int) Math.floor(a);
        }
        return s62;
    }

    /**
     * 将一个String数组转换为用指定分隔符分割开的字符串(最后一个String不含分隔符)
     * @param arrayList
     * @return
     */
    public static String arrayToString(String[] arrayList, Character split){
        String stringList = "";
        for (String string : arrayList){
            stringList += stringList + split;
        }
        stringList = stringList.substring(0, stringList.length()-1);

        return stringList;
    }

    public static String[] getMessageTags(String message) {
        List<String> tagList = new ArrayList<String>();
        Matcher matcher = Pattern.compile(ConstUtil.HashtagRegex).matcher(message);
        while (matcher.find()) {
            tagList.add(matcher.group(1));
        }
        String[] tags = tagList.toArray(new String[0]);
        return tags;
    }

    /**
     * 直接从微博URL转换为内部id
     * @return
     */
    public static String parseUrlToId(String weiboUrl){
        URL url;
        try {
            url = new URL(weiboUrl);
        }
        catch (MalformedURLException e){
            Logger.error("Unsupported weibo url");
            return null;
        }
        String weiboPath = url.getPath();
        String weiboMid = weiboPath.substring(weiboPath.length() - 9, weiboPath.length());
        String weiboId = WeiboUtils.mid2id(weiboMid);

        return weiboId;
    }

    public static String parseUrlToUid(String weiboUrl){
        URL url;
        try {
            url = new URL(weiboUrl);
        }
        catch (MalformedURLException e){
            Logger.error("Unsupported weibo url");
            return null;
        }
        String weiboPath = url.getPath();
        String weiboUid = weiboPath.substring(1, 11);

        return weiboUid;
    }

    public static String parseIdToUrl(String uid,String id){
        String weiboMid = WeiboUtils.id2mid(id);
        String weiboUrl = String.format("http://www.weibo.com/%s/%s", uid, weiboMid);

        return weiboUrl;
    }

    public static String delHTMLTag(String htmlStr) {
        Pattern p_script = Pattern.compile(ConstUtil.regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(ConstUtil.regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(ConstUtil.regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        return htmlStr.trim(); // 返回文本字符串
    }
}