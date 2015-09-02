package models.APIRequest;

import com.fasterxml.jackson.databind.JsonNode;
import models.AsyncRequest;
import play.libs.F.Promise;
import play.libs.ws.WSClient;
import utils.ConstUtil;

import java.util.regex.Pattern;

/**
 * Created by lizhuoli on 15/9/2.
 */
public class WeiboAPI {
    private String weiboURL;
    private String weiboToken;
    private WSClient ws;
    WeiboAPI(WSClient wsClient,String url,String token){
        weiboURL = url;
        weiboToken = token;
        ws = wsClient;
    }
    public Promise<JsonNode> getSocialUser(){
        return null;
    }

    public Promise<JsonNode> getSocialMessage(){
        String baseURL = "https://api.weibo.com/2/statuses/show.json";

        String weiboURLReg = ConstUtil.getURLRegex();
        Pattern pattern = Pattern.compile(weiboURLReg);
        String weiboMid = pattern.matcher(weiboURL).group(6);

        String parameter = String.format("access_token=%s&id=", weiboToken, weiboMid);
        AsyncRequest request = new AsyncRequest(ws,baseURL,parameter);
        Promise<JsonNode> jsonNodePromise = request.get();

        jsonNodePromise.map((value)->{
            JsonNode userNode = value.path("user");
            JsonNode user = parseUser(userNode);

            String createTime = value.findPath("created_at").asText();
            String content = value.findPath("text").asText();
            String source = value.findParent("source").asText();
            String location = value.get("geo").findPath("province").asText();
            return null;
        });

        return jsonNodePromise;
    }

    public JsonNode parseMessage(JsonNode node){
        return null;
    }
    public JsonNode parseUser(JsonNode node){
        return null;
    }
}
