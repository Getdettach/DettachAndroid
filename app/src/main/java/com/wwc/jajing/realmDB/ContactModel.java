package com.wwc.jajing.realmDB;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Vivek on 25-09-2016.
 */
public class ContactModel extends RealmObject{

//    @PrimaryKey
//    private int id;

    private String name;
    private String number;
    private String picture;
//    private String accessToken;
//    private String availTime;
//    private String AllowStatus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

//    public String getAccessToken() {
//        return accessToken;
//    }
//
//    public void setAccessToken(String accessToken) {
//        this.accessToken = accessToken;
//    }
//
//    public String getAvailTime() {
//        return availTime;
//    }
//
//    public void setAvailTime(String availTime) {
//        this.availTime = availTime;
//    }
//
//    public String getAllowStatus() {
//        return AllowStatus;
//    }
//
//    public void setAllowStatus(String allowStatus) {
//        AllowStatus = allowStatus;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
}
