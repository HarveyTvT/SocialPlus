package models.RawConverter;

import com.fasterxml.jackson.databind.JsonNode;
import models.Midform.SocialComment;
import models.Midform.SocialMessage;
import models.Midform.SocialUser;
import play.libs.F.Promise;

import java.util.List;

/**
 * Created by lizhuoli on 15/9/2.
 */
public class WeiboConverter implements RawConverter{
    @Override
    public SocialMessage convertMessage(Promise<JsonNode> json) {
        json.map((value)->{
            JsonNode userNode = value.path("user");
//            JsonNode user = convertUser(userNode);

            String createTime = value.findPath("created_at").asText();
            String content = value.findPath("text").asText();
            String source = value.findParent("source").asText();
            String location = value.get("geo").findPath("province").asText();
            return null;
        });
        return null;
    }

    @Override
    public SocialUser convertUser(Promise<JsonNode> json) {
        return null;
    }

    @Override
    public SocialComment convertComment(Promise<JsonNode> json) {
        return null;
    }

    @Override
    public List<SocialComment> convertCommentList(Promise<JsonNode> json) {
        return null;
    }

    @Override
    public List<SocialMessage> convertMessageList(Promise<JsonNode> json) {
        return null;
    }

    @Override
    public List<SocialUser> convertUserList(Promise<JsonNode> json) {
        return null;
    }
}
