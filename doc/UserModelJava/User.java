package com.socialplus;

import org.json.*;
import java.util.ArrayList;

public class User {
	
    private String email;
    private String password;
    private String id;
    private boolean validated;
    private ArrayList<String> resultId;
    private Token token;
    
    
	public User () {
		
	}	
        
    public User (JSONObject json) {
    
        this.email = json.optString("email");
        this.password = json.optString("password");
        this.id = json.optString("_id");
        this.validated = json.optBoolean("validated");

        this.resultId = new ArrayList<String>();
        JSONArray arrayResultId = json.optJSONArray("result_id");
        if (null != arrayResultId) {
            int resultIdLength = arrayResultId.length();
            for (int i = 0; i < resultIdLength; i++) {
                String item = arrayResultId.optString(i);
                if (null != item) {
                    this.resultId.add(item);
                }
            }
        }
        else {
            String item = json.optString("result_id");
            if (null != item) {
                this.resultId.add(item);
            }
        }

        this.token = new Token(json.optJSONObject("token"));

    }
    
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getValidated() {
        return this.validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public ArrayList<String> getResultId() {
        return this.resultId;
    }

    public void setResultId(ArrayList<String> resultId) {
        this.resultId = resultId;
    }

    public Token getToken() {
        return this.token;
    }

    public void setToken(Token token) {
        this.token = token;
    }


    
}
