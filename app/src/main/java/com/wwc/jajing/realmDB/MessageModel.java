package com.wwc.jajing.realmDB;

import io.realm.RealmObject;

/**
 * Created by Vivek on 25-09-2016.
 */
public class MessageModel extends RealmObject{
    private String number;
    private String message;
    private String date;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
}
