package models.RawConverter;

import com.fasterxml.jackson.databind.JsonNode;
import models.SocialComment;
import models.SocialMessage;
import models.SocialUser;

import java.util.List;

/**
 * Created by lizhuoli on 15/9/2.
 */
public interface RawConverter {
    public SocialUser convertUser(JsonNode json);
    public SocialMessage convertMessage(JsonNode json);
    public SocialComment convertComment(JsonNode json);
    public List<SocialUser> convertUserList(JsonNode json);
    public List<SocialMessage> convertMessageList(JsonNode json);
    public List<SocialComment> convertCommentList(JsonNode json);
}
