package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.Logger;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.libs.F.Promise;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.mvc.Controller;

/**
 * Created by lizhuoli on 15/8/31.
 */
public class AsyncRequest extends Controller {
    private WSClient ws;
    private String url;
    private String parameter;

    public AsyncRequest(String requestUrl, String requestParam) {
        ws = WS.client();
        url = requestUrl;
        parameter = requestParam;
    }

    /**
     * POST request
     * if arg != null, use urlencode to send HTTP body
     * else use just post method and use URL query
     * @return
     */
    public Promise<JsonNode> post(String formData){
        WSRequest request = ws.url(url);
        Promise<WSResponse> responsePromise = request
                .setContentType("application/x-www-form-urlencoded")
                .post(formData);
        Promise<JsonNode> jsonNodePromise = responsePromise.map(value -> value.asJson());

        responsePromise.onFailure(error -> {
            Logger.error(error.toString());
        });

        return jsonNodePromise;
    }

    /**
     * GET Request
     * no args. Use the construct method and send with URL query
     *
     * @return
     */
    public Promise<JsonNode> get(){
        String requestUrl = String.format("%s?%s",url,parameter);
        WSRequest request = ws.url(requestUrl);
        Promise<WSResponse> responsePromise = request.get();
        Promise<JsonNode> jsonNodePromise = responsePromise.map(value -> value.asJson());

        responsePromise.onFailure(error -> {
            Logger.error(error.toString());
        });

        return jsonNodePromise;
    }
}