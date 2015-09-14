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
    public void convertUser(Promise<JsonNode> jsonPromise);
    public void convertMessage(Promise<JsonNode> jsonPromise);
    public void convertComment(Promise<JsonNode> jsonPromise);
}
