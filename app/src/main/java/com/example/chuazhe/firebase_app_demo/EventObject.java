package com.example.chuazhe.firebase_app_demo;

import java.io.Serializable;

/**
 * Created by Aaron on 5/17/2018.
 */

public class EventObject implements Serializable {

    private int ID;
    private String date;
    private String eName;
    private String type;
    private String starttime;
    private String endtime;
    private String location;

    public int getID() {
        return ID;
    }

    public String getDate() {
        return date;
    }

    public String getEventName() {
        return eName;
    }

    public String getType() {
        return type;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getLocation() {
        return location;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setEventName(String eName) {
        this.eName = eName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
