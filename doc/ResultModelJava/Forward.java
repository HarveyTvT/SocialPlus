package com.socialplus;

import org.json.*;


public class Forward {
	
    private String to;
    private String from;
    
    
	public Forward () {
		
	}	
        
    public Forward (JSONObject json) {
    
        this.to = json.optString("to");
        this.from = json.optString("from");

    }
    
    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }


    
}
