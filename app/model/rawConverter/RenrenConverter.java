package model.rawConverter;

import com.fasterxml.jackson.databind.JsonNode;
import model.SocialMessage;
import model.SocialUser;

/**
 * Created by lizhuoli on 15/9/2.
 */
public class RenrenConverter implements RawConverter {
    @Override
    public SocialMessage convertMessage(JsonNode json) {
        return null;
    }

    @Override
    public SocialUser convertUser(JsonNode json) {
        return null;
    }
}
