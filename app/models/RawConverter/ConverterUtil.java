package models.RawConverter;

import models.Midform.SocialMessage;

/**
 * Created by lizhuoli on 15/9/8.
 */
public class ConverterUtil {
    public static int calculateWeight(int follow,int star,int favorite){
        return (follow + star + favorite)/3;
    }

    public static SocialMessage setMessage(String id,String fuck){
        return null;
    }
}
