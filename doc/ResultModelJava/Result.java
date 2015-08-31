package com.socialplus;

import org.json.*;
import java.util.ArrayList;

public class Result {
	
    private Gender gender;
    private String id;
    private ArrayList<Forward> forward;
    private double emotion;
    private ArrayList<Time> time;
    private ArrayList<Location> location;
    private ArrayList<String> tag;
    private String lastdate;
    
    
	public Result () {
		
	}	
        
    public Result (JSONObject json) {
    
        this.gender = new Gender(json.optJSONObject("gender"));
        this.id = json.optString("_id");

        this.forward = new ArrayList<Forward>();
        JSONArray arrayForward = json.optJSONArray("forward");
        if (null != arrayForward) {
            int forwardLength = arrayForward.length();
            for (int i = 0; i < forwardLength; i++) {
                JSONObject item = arrayForward.optJSONObject(i);
                if (null != item) {
                    this.forward.add(new Forward(item));
                }
            }
        }
        else {
            JSONObject item = json.optJSONObject("forward");
            if (null != item) {
                this.forward.add(new Forward(item));
            }
        }

        this.emotion = json.optDouble("emotion");

        this.time = new ArrayList<Time>();
        JSONArray arrayTime = json.optJSONArray("time");
        if (null != arrayTime) {
            int timeLength = arrayTime.length();
            for (int i = 0; i < timeLength; i++) {
                JSONObject item = arrayTime.optJSONObject(i);
                if (null != item) {
                    this.time.add(new Time(item));
                }
            }
        }
        else {
            JSONObject item = json.optJSONObject("time");
            if (null != item) {
                this.time.add(new Time(item));
            }
        }


        this.location = new ArrayList<Location>();
        JSONArray arrayLocation = json.optJSONArray("location");
        if (null != arrayLocation) {
            int locationLength = arrayLocation.length();
            for (int i = 0; i < locationLength; i++) {
                JSONObject item = arrayLocation.optJSONObject(i);
                if (null != item) {
                    this.location.add(new Location(item));
                }
            }
        }
        else {
            JSONObject item = json.optJSONObject("location");
            if (null != item) {
                this.location.add(new Location(item));
            }
        }


        this.tag = new ArrayList<String>();
        JSONArray arrayTag = json.optJSONArray("tag");
        if (null != arrayTag) {
            int tagLength = arrayTag.length();
            for (int i = 0; i < tagLength; i++) {
                String item = arrayTag.optString(i);
                if (null != item) {
                    this.tag.add(item);
                }
            }
        }
        else {
            String item = json.optString("tag");
            if (null != item) {
                this.tag.add(item);
            }
        }

        this.lastdate = json.optString("lastdate");

    }
    
    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Forward> getForward() {
        return this.forward;
    }

    public void setForward(ArrayList<Forward> forward) {
        this.forward = forward;
    }

    public double getEmotion() {
        return this.emotion;
    }

    public void setEmotion(double emotion) {
        this.emotion = emotion;
    }

    public ArrayList<Time> getTime() {
        return this.time;
    }

    public void setTime(ArrayList<Time> time) {
        this.time = time;
    }

    public ArrayList<Location> getLocation() {
        return this.location;
    }

    public void setLocation(ArrayList<Location> location) {
        this.location = location;
    }

    public ArrayList<String> getTag() {
        return this.tag;
    }

    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
    }

    public String getLastdate() {
        return this.lastdate;
    }

    public void setLastdate(String lastdate) {
        this.lastdate = lastdate;
    }


    
}
