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
    public void convertMessage(Promise<List<JsonNode>> promiseList,String url);
    public void convertComment(Promise<List<JsonNode>> promiseList);

    public SocialUser getUser(String[] list,String... args);
    public SocialMessage getMessage(String[] tags,String[] repostsList,
                                    String... args);
    public SocialComment getComment(String... args);
}
