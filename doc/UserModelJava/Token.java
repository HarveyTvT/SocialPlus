package com.socialplus;

import org.json.*;


public class Token {
	
    private Weibo weibo;
    private Renren renren;
    
    
	public Token () {
		
	}	
        
    public Token (JSONObject json) {
    
        this.weibo = new Weibo(json.optJSONObject("weibo"));
        this.renren = new Renren(json.optJSONObject("renren"));

    }
    
    public Weibo getWeibo() {
        return this.weibo;
    }

    public void setWeibo(Weibo weibo) {
        this.weibo = weibo;
    }

    public Renren getRenren() {
        return this.renren;
    }

    public void setRenren(Renren renren) {
        this.renren = renren;
    }


    
}
