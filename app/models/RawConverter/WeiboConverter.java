package models.RawConverter;

import com.fasterxml.jackson.databind.JsonNode;
import models.SocialComment;
import models.SocialMessage;
import models.SocialUser;

import java.util.List;

/**
 * Created by lizhuoli on 15/9/2.
 */
public class WeiboConverter implements RawConverter{
    @Override
    public SocialMessage convertMessage(JsonNode json) {
        return null;
    }

    @Override
    public SocialUser convertUser(JsonNode json) {
        return null;
    }

    @Override
    public SocialComment convertComment(JsonNode json) {
        return null;
    }

    @Override
    public List<SocialComment> convertCommentList(JsonNode json) {
        return null;
    }

    @Override
    public List<SocialMessage> convertMessageList(JsonNode json) {
        return null;
    }

    @Override
    public List<SocialUser> convertUserList(JsonNode json) {
        return null;
    }
}
