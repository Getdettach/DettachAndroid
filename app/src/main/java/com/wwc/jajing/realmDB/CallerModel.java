package com.wwc.jajing.realmDB;

import com.wwc.jajing.domain.entity.Caller;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by infmac1 on 03/10/16.
 */
public class CallerModel extends RealmObject {

    @PrimaryKey
    private int id;

    private String number;
    private String name;
    private int hasApp = 0;
    private String haspermission="SEND_SMS";

    public void setHaspermission(String haspermission) {
        this.haspermission = haspermission;
    }

    public String getHaspermission() {
        return haspermission;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public int getHasApp() {
        return hasApp;
    }

    public void setHasApp(int hasApp) {
        this.hasApp = hasApp;
    }


}
