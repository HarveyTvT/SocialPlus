package com.lingjoin.keyExtractor;

public class KeyExtractor {

    static {
        if (CLibraryKeyExtractor.instance.KeyExtract_Init("", 1, "")) {
            play.Logger.info("KeyExtractor初始化成功");
        } else {
            play.Logger.error("KeyExtractor初始化失败");
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        String content = "，全球旅行必备话中蒙友好合作，谈中国周边外交，论亚洲国家相处之道，强调互尊互信、聚同化异、守望相助、合作共赢，共创中蒙关系发展新时代，共促亚洲稳定繁荣";
        String[] results = getKeyWords(content);
        for (String result : results) {
            System.out.println(result);
        }
    }

    public static String[] getKeyWords(String content) {
        String KeyWordStr = CLibraryKeyExtractor.instance.KeyExtract_GetKeyWords(content, 100, false);
        play.Logger.info("getKeyWords" + KeyWordStr);
        CLibraryKeyExtractor.instance.KeyExtract_Exit();
        String[] KeyWords = KeyWordStr.split("#");
        return KeyWords;
    }
}
