package com.wwc.jajing.realmDB;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by infmac1 on 16/11/16.
 */

public class RegisterDeviceModel extends RealmObject {

    @PrimaryKey
    private int id;

    private String deviceId;
    private String phoneNumber;
    private String simSerialNumber;
    private String accestoken;
    private String registerId;

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }

    public String getSimSerialNumber() {
        return simSerialNumber;
    }

    public void setSimSerialNumber(String simSerialNumber) {
        this.simSerialNumber = simSerialNumber;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAccestoken() {
        return accestoken;
    }

    public void setAccestoken(String accestoken) {
        this.accestoken = accestoken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
