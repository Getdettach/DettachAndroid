package com.wwc.jajing.realmDB;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by infmac1 on 16/11/16.
 */

public class SetStatusModel extends RealmObject {

    private Date startTime;
    private Date endTime;
    private String statusName;
    private String timeId;
    private String statusId;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getTimeId() {
        return timeId;
    }

    public void setTimeId(String timeId) {
        this.timeId = timeId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
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
