package models.APIRequest;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.AsyncRequest;
import play.Logger;
import play.libs.F.Promise;

/**
 * Created by lizhuoli on 15/9/2.
 */
public class WeiboAPI{

    /**说明:微博含有uid, mid, cid分别对应用户id, 微博id, 评论id, 但微博还有一个内部id(从mid加密来)
     * 其中内部id可以根据url中提取到的mid然后转换成id,在WeiboUtils中已有, 此处接口皆用内部id
     */

    private String weiboToken;

    public WeiboAPI(String token){
        weiboToken = token;
    }

    /**
     * 根据uid返回单个用户信息
     * @param uid
     * @return
     */
    public Promise<JsonNode> getSocialUser(String uid){
        String baseUrl = "https://api.weibo.com/2/users/show.json";
        String parameter = String.format("access_token=%s&uid=%s", weiboToken, uid);

        AsyncRequest request = new AsyncRequest(baseUrl,parameter);
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

        AsyncRequest request = new AsyncRequest(baseUrl,parameter);
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

        AsyncRequest request = new AsyncRequest(baseUrl,parameter);
        Promise<JsonNode> jsonNodePromise = request.get();
        jsonNodePromise.onFailure(err -> {
            Logger.error("Error at getting Weibo comment: " + err);
        });

        return jsonNodePromise;
    }

    /**
     * 根据uid数组批量获取用户列表
     * @param uids
     * @return
     */
    public Promise<JsonNode> getSocialUserList(String uids){
        String baseUrl = "https://api.weibo.com/2/users/counts.json";
        String parameter = String.format("access_token=%s&uids=%s", weiboToken, uids);

        AsyncRequest request = new AsyncRequest(baseUrl,parameter);
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
        String baseUrl = "https://api.weibo.com/2/statuses/repost_timeline/ids.json";
        String parameter = String.format("access_token=%s&id=%s&page=%s&count=%s",
                weiboToken, id, 1, 200);//最后的参数为单页转发微博上限

        AsyncRequest request = new AsyncRequest(baseUrl,parameter);
        Promise<JsonNode> jsonNodePromise = request.get();
        jsonNodePromise.onFailure(err -> {
            Logger.error("Error at getting Weibo message list: " + err);
        });

        return jsonNodePromise;
    }

    /**
     * 根据cid数组批量获取评论列表
     * @param cids
     * @return
     */
    public Promise<JsonNode> getSocialCommentList(String cids){
        String baseUrl = "https://api.weibo.com/2/comments/show_batch.json";
        String parameter = String.format("access_token=%s&cids=%s",
                weiboToken, cids);

        AsyncRequest request = new AsyncRequest(baseUrl,parameter);
        Promise<JsonNode> jsonNodePromise = request.get();
        jsonNodePromise.onFailure(err -> {
            Logger.error("Error at getting Weibo comment list: " + err);
        });

        return jsonNodePromise;
    }

    /**
     * 获取用户关注的好友列表
     * @return
     */

    public Promise<JsonNode> getUserFriendList(String uid){
        String baseUrl = "https://api.weibo.com/2/friendships/friends.json";
        String parameter = String.format("access_token=%s&uid=%s&count=%d",
                weiboToken, uid, 100);//最后参数为单页用户上限

        AsyncRequest request = new AsyncRequest(baseUrl,parameter);
        Promise<JsonNode> jsonNodePromise = request.get();
        jsonNodePromise.onFailure(err -> {
            Logger.error("Error at getting Weibo friend list: " + err);
        });

        return jsonNodePromise;
    }

}
