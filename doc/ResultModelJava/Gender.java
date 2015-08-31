package com.socialplus;

import org.json.*;


public class Gender {
	
    private double other;
    private double male;
    private double female;
    
    
	public Gender () {
		
	}	
        
    public Gender (JSONObject json) {
    
        this.other = json.optDouble("other");
        this.male = json.optDouble("male");
        this.female = json.optDouble("female");

    }
    
    public double getOther() {
        return this.other;
    }

    public void setOther(double other) {
        this.other = other;
    }

    public double getMale() {
        return this.male;
    }

    public void setMale(double male) {
        this.male = male;
    }

    public double getFemale() {
        return this.female;
    }

    public void setFemale(double female) {
        this.female = female;
    }


    
}
