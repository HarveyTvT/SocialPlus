package models.APIRequest;

import com.fasterxml.jackson.databind.JsonNode;
import models.AsyncRequest;
import play.Logger;
import play.libs.F.Promise;
import play.libs.ws.WSClient;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lizhuoli on 15/9/2.
 */
public class WeiboAPI {
    /**说明:微博含有uid, mid, cid分别对应用户id, 微博id, 评论id, 但微博还有一个内部id(从mid加密来)
     * 其中内部id可以根据url中提取到的mid然后转换成id,在WeiboUtils中已有, 此处接口皆用内部id
     */

    private String weiboUrl;
    private String weiboToken;
    private String weiboId;

    private WSClient ws;
    public WeiboAPI(String url, String token, WSClient wsClient){
        weiboUrl = url;
        weiboId = parseUrlToId(weiboUrl);
        weiboToken = token;
        ws = wsClient;
    }

    /**
     * 根据uid返回单个用户信息
     * @param uid
     * @return
     */
    public Promise<JsonNode> getSocialUser(String uid){
        String baseUrl = "https://api.weibo.com/2/users/show.json";
        String parameter = String.format("access_token=%s&uid=", weiboToken, uid);

        AsyncRequest request = new AsyncRequest(ws,baseUrl,parameter);
        Promise<JsonNode> jsonNodePromise = request.get();
        jsonNodePromise.onFailure(err -> {
            Logger.error("Error at getting Weibo user: " + err);
        });

        return jsonNodePromise;
    }

    /**
     * 根据id返回单个微博信息
     * @param id
     * @return
     */
    public Promise<JsonNode> getSocialMessage(String id){
        String baseUrl = "https://api.weibo.com/2/statuses/show.json";
        String parameter = String.format("access_token=%s&id=%s",
                weiboToken, id);

        AsyncRequest request = new AsyncRequest(ws,baseUrl,parameter);
        Promise<JsonNode> jsonNodePromise = request.get();
        jsonNodePromise.onFailure(err -> {
            Logger.error("Error at getting Weibo message: " + err);
        });

        return jsonNodePromise;
    }

    /**
     * 根据id返回单个微博的评论列表
     * @param id
     * @return
     */
    public Promise<JsonNode> getSocialComment(String id){
        String baseUrl = "https://api.weibo.com/2/comments/show.json";
        String parameter = String.format("access_token=%s&id=%s&page=%s&count=%s",
                weiboToken, id, 1, 100);//最后参数为获取的单页评论最大值

        AsyncRequest request = new AsyncRequest(ws,baseUrl,parameter);
        Promise<JsonNode> jsonNodePromise = request.get();
        jsonNodePromise.onFailure(err -> {
            Logger.error("Error at getting Weibo comment: " + err);
        });

        return jsonNodePromise;
    }

    /**
     * 根据uid数组批量获取用户列表
     * @param uidList
     * @return
     */
    public Promise<JsonNode> getSocialUserList(String[] uidList){
        String baseUrl = "https://api.weibo.com/2/users/counts.json";
        String uids = WeiboUtils.arrayToString(uidList, ',');
        String parameter = String.format("access_token=%s&uids=", weiboToken, uids);

        AsyncRequest request = new AsyncRequest(ws,baseUrl,parameter);
        Promise<JsonNode> jsonNodePromise = request.get();
        jsonNodePromise.onFailure(err -> {
            Logger.error("Error at getting Weibo user list: " + err);
        });

        return jsonNodePromise;
    }

    /**
     * 根据id获取转发微博列表,即最终的转发链
     * @param id
     * @return
     */
    public Promise<JsonNode> getSocialMessageList(String id){
        String baseUrl = "https://api.weibo.com/2/statuses/repost_timeline.json";
        String parameter = String.format("access_token=%s&id=%s&page=%s&count=%s",
                weiboToken, id, 1, 200);//最后的参数为转发微博单页个数

        AsyncRequest request = new AsyncRequest(ws,baseUrl,parameter);
        Promise<JsonNode> jsonNodePromise = request.get();
        jsonNodePromise.onRedeem(value -> {
            Logger.info(value.toString());
        });
        jsonNodePromise.onFailure(err -> {
            Logger.error("Error at getting Weibo message list: " + err);
        });

        return jsonNodePromise;
    }

    /**
     * 根据cid数组批量获取评论列表
     * @param cidList
     * @return
     */
    public Promise<JsonNode> getSocialCommentList(String[] cidList){
        String baseUrl = "https://api.weibo.com/2/comments/show_batch.json";
        String uids = WeiboUtils.arrayToString(cidList, ',');
        String parameter = String.format("access_token=%s&cids=%s",
                weiboToken, cidList);

        AsyncRequest request = new AsyncRequest(ws,baseUrl,parameter);
        Promise<JsonNode> jsonNodePromise = request.get();
        jsonNodePromise.onFailure(err -> {
            Logger.error("Error at getting Weibo comment list: " + err);
        });

        return jsonNodePromise;
    }

    /**
     * 直接从微博URL转换为内部id
     * @return
     */
    public String parseUrlToId(String weiboUrl){
        URL url;
        try {
            url = new URL(weiboUrl);
        }
        catch (MalformedURLException e){
            Logger.error("Unsupported weibo url");
            return null;
        }
        String weiboPath = url.getPath();
        String weiboMid = weiboPath.substring(weiboPath.length()-9,weiboPath.length());
        String weiboId = WeiboUtils.mid2id(weiboMid);

        return weiboId;
    }
}
