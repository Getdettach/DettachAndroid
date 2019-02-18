package com.wwc.jajing.realmDB;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by infmac1 on 11/10/16.
 */

public class MissedMessageModel extends RealmObject {

    private String number;
    private String type;
    private String time;
    private String date;
    private String message;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
