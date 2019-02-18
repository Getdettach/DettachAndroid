package com.wwc.jajing.util;

import android.Manifest;

public class Constants {
    public static final int PERMISSIONS_REQUEST_CODE = 0;
    public static final String[] appPermissions = {
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            //Manifest.permission.WRITE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            //Manifest.permission.MODIFY_PHONE_STATE,
            //Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.GET_ACCOUNTS
    };
}