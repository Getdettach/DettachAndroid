package com.wwc.jajing.realmDB;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by infmac1 on 10/11/16.
 */

public class ExtendTimeModel extends RealmObject {

    private String timeId;
    private Date startTime;
    private Date endTime;
    private String startId;


    public String getTimeId() {
        return timeId;
    }

    public void setTimeId(String timeId) {
        this.timeId = timeId;
    }

    public String getStartId() {
        return startId;
    }

    public void setStartId(String startId) {
        this.startId = startId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
