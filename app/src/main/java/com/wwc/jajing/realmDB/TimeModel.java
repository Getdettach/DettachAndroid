package com.wwc.jajing.realmDB;

import io.realm.RealmObject;

/**
 * Created by Vivek on 24-09-2016.
 */
public class TimeModel extends RealmObject {
    private String startTime;
    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
