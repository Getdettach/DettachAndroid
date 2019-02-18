package com.wwc.jajing.realmDB;

import io.realm.RealmObject;

/**
 * Created by Vivek on 24-09-2016.
 */
public class OptionsModel extends RealmObject {
    private String message;
    private String description;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
