package com.wwc.jajing.realmDB;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Vivek on 25-09-2016.
 */
public class CallsModel extends RealmObject {

    private String number;
    private String type;
    private String time;
    private String calldate;
    private String callerName;
    private Date callDateFormat;

    public Date getCallDateFormat() {
        return callDateFormat;
    }

    public void setCallDateFormat(Date callDateFormat) {
        this.callDateFormat = callDateFormat;
    }

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public String getCalldate() {
        return calldate;
    }

    public void setCalldate(String calldate) {
        this.calldate = calldate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
