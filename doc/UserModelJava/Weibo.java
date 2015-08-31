package com.socialplus;

import org.json.*;


public class Weibo {
	
    private String token;
    private double expire;
    
    
	public Weibo () {
		
	}	
        
    public Weibo (JSONObject json) {
    
        this.token = json.optString("token");
        this.expire = json.optDouble("expire");

    }
    
    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public double getExpire() {
        return this.expire;
    }

    public void setExpire(double expire) {
        this.expire = expire;
    }


    
}
