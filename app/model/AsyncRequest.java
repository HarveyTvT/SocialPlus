package model;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.ws.WSResponse;
import play.libs.F;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;

/**
 * Created by lizhuoli on 15/8/31.
 */
public class AsyncRequest  {
    public String url;
    public String parameter;
    private final WSClient client;

    public AsyncRequest(WSClient wsClient,String requestURL,String requestParam){
        client = wsClient;
        url = requestURL;
        parameter = requestParam;
    }

    /*
    POST
    if formData != null , use urlencode
    else use just post method
     */
    public F.Promise<JsonNode> post(String formData){
        WSRequest request = client.url(url);
        F.Promise<WSResponse> responsePromise = request
                .setContentType("application/x-www-form-urlencoded")
                .post(formData);
        F.Promise<JsonNode> jsonNodePromise = responsePromise.map(value -> {
            return value.asJson();
        });

        responsePromise.onFailure(error -> {
            System.out.println(error);
        });

        return jsonNodePromise;
    }
}
