package com.wwc.jajing.realmDB;

import io.realm.RealmObject;

/**
 * Created by infmac1 on 14/10/16.
 */

public class RepeatDaysModel extends RealmObject {

    private String days;
    private String id;
    private String idFlag;
    private String available;

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getIdFlag() {
        return idFlag;
    }

    public void setIdFlag(String idFlag) {
        this.idFlag = idFlag;
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        days = days;
    }
}
