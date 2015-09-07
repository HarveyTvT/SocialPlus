package models.APIRequest;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.F.Promise;
import play.libs.ws.WSClient;

/**
 * Created by lizhuoli on 15/9/7.
 */
public class TwitterAPI {
    private String twitterToken;

    private WSClient ws;
    TwitterAPI(String token, WSClient wsClient){
        twitterToken = token;
        ws = wsClient;
    }

    public Promise<JsonNode> getSocialUser(){
        return null;
    }
}
