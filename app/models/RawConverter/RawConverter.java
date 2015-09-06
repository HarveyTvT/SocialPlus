package models.RawConverter;

import com.fasterxml.jackson.databind.JsonNode;
import models.SocialComment;
import models.SocialMessage;
import models.SocialUser;
import play.libs.F.Promise;

import java.util.List;

/**
 * Created by lizhuoli on 15/9/2.
 */
public interface RawConverter {
    public SocialUser convertUser(Promise<JsonNode> json);
    public SocialMessage convertMessage(Promise<JsonNode> json);
    public SocialComment convertComment(Promise<JsonNode> json);
    public List<SocialUser> convertUserList(Promise<JsonNode> json);
    public List<SocialMessage> convertMessageList(Promise<JsonNode> json);
    public List<SocialComment> convertCommentList(Promise<JsonNode> json);
}