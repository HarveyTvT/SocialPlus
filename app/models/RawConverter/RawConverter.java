package models.RawConverter;

import com.fasterxml.jackson.databind.JsonNode;
import models.Midform.SocialUser;
import models.Midform.SocialComment;
import models.Midform.SocialMessage;
import play.libs.F.Promise;

import java.util.List;

/**
 * Created by lizhuoli on 15/9/2.
 */
public interface RawConverter {
    public void convertUser(Promise<List<JsonNode>> promiseList);
    public void convertMessage(Promise<List<JsonNode>> promiseList);
    public void convertComment(Promise<List<JsonNode>> promiseList);

    public SocialUser getUser(JsonNode userJson);
    public SocialMessage getMessage(JsonNode messageJson,Boolean isOriginal);
    public SocialComment getComment(JsonNode commentJson);
}
