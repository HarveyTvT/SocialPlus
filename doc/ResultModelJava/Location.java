package com.socialplus;

import org.json.*;


public class Location {
	
    private double 023;
    private double 311;
    private double 029;
    
    
	public Location () {
		
	}	
        
    public Location (JSONObject json) {
    
        this.023 = json.optDouble("023");
        this.311 = json.optDouble("311");
        this.029 = json.optDouble("029");

    }
    
    public double get023() {
        return this.023;
    }

    public void set023(double 023) {
        this.023 = 023;
    }

    public double get311() {
        return this.311;
    }

    public void set311(double 311) {
        this.311 = 311;
    }

    public double get029() {
        return this.029;
    }

    public void set029(double 029) {
        this.029 = 029;
    }


    
}
