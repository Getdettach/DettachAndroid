package com.wwc.jajing.realmDB;

import io.realm.RealmObject;

/**
 * Created by infmac1 on 16/11/16.
 */

public class HasAppModel extends RealmObject {

    private String id;
    private String number;
    private String picture;
    private String accessToken;
    private String availTime;
    private String calAllowStatus;
    private String receAllowStatus;
    private String statusName;

    private String isActive;

    public String getIsActive() {
        return this.isActive;
    }

    public void setIsActive(String active) {
        isActive = active;
    }

    public String getReceAllowStatus() {
        return receAllowStatus;
    }

    public void setReceAllowStatus(String receAllowStatus) {
        this.receAllowStatus = receAllowStatus;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAvailTime() {
        return availTime;
    }

    public void setAvailTime(String availTime) {
        this.availTime = availTime;
    }

    public String getCalAllowStatus() {
        return calAllowStatus;
    }

    public void setCalAllowStatus(String calAllowStatus) {
        this.calAllowStatus = calAllowStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
