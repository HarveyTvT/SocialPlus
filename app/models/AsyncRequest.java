package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.ws.WSResponse;
import play.libs.F.Promise;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;

/**
 * Created by lizhuoli on 15/8/31.
 */
public class AsyncRequest  {
    public String url;
    public String parameter;
    private final WSClient client;

    public AsyncRequest(WSClient wsClient,String requestUrl,String requestParam){
        client = wsClient;
        url = requestUrl;
        parameter = requestParam;
    }

    /*
    POST
    if formData != null , use urlencode
    else use just post method
     */
    public Promise<JsonNode> post(String formData){
        WSRequest request = client.url(url);
        Promise<WSResponse> responsePromise = request
                .setContentType("application/x-www-form-urlencoded")
                .post(formData);
        Promise<JsonNode> jsonNodePromise = responsePromise.map(value -> {
            return value.asJson();
        });

        responsePromise.onFailure(error -> {
            System.out.println(error);
        });

        return jsonNodePromise;
    }

    public Promise<JsonNode> get(){
        String requestUrl = String.format("%s?%s",url,parameter);
        WSRequest request = client.url(requestUrl);
        Promise<WSResponse> responsePromise = request.get();
        Promise<JsonNode> jsonNodePromise = responsePromise.map(value -> {
            return value.asJson();
        });

        responsePromise.onFailure(error -> {
            System.out.println(error);
        });

        return jsonNodePromise;
    }
}
