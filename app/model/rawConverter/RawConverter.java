package model.rawConverter;

import com.fasterxml.jackson.databind.JsonNode;
import model.SocialMessage;
import model.SocialUser;

/**
 * Created by lizhuoli on 15/9/2.
 */
public interface RawConverter {
    public SocialUser convertUser(JsonNode json);
    public SocialMessage convertMessage(JsonNode json);
}
