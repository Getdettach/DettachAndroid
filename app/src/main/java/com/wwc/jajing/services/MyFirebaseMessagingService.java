package com.wwc.jajing.services;

/**
 * Created by infmac1 on 29/11/16.
 */

/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wwc.jajing.activities.AboutToBreakThrough;
import com.wwc.jajing.activities.DoNotDisturbOptions;
import com.wwc.jajing.activities.PleaseWait;
import com.wwc.jajing.activities.SplashActivity;
import com.wwc.jajing.activities.TimeSettingActivity;
import com.wwc.jajing.activities.WantToDisturbActivity;
import com.wwc.jajing.fragment.MenuTimeSettingFragment;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public static  String str_closeprogress;

    Intent intent;


    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

       Log.e(TAG, "From: " + remoteMessage.getFrom());

//        System.out.println("DATA VALUES "+remoteMessage.getData());

        sendNotification(remoteMessage);
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param
     */
    private void sendNotification(RemoteMessage remoteMessage) {

//        {message =Sajan, time=01-12-2016 07:02:45 PM, from =Dinesh, callerNumber=9785412030}
        Log.e(TAG, "remoteMessage*******=========="+remoteMessage);
        String calNumber = remoteMessage.getData().get("callerNumber");
        String from = remoteMessage.getData().get("nFrom");
        String message = remoteMessage.getData().get("nMessage");

        if(Character.toString(calNumber.charAt(0)).equalsIgnoreCase(" ")){
            calNumber.trim();
            calNumber = "+"+calNumber;
        }

        if(from.compareTo("Caller")==0){
            intent = new Intent(this, WantToDisturbActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("Number",calNumber);
            startActivity(intent);
        }else{

            if(message.compareTo("A")==0){
                str_closeprogress="close";
                Intent i = new Intent(getApplicationContext(), AboutToBreakThrough.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("jjsms", calNumber);
                startActivity(i);
            }else if(message.compareTo("D")==0){
                str_closeprogress="close";
                Intent i = new Intent(getApplicationContext(), DoNotDisturbOptions.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("JJSMS", calNumber);
                startActivity(i);
            }
        }

    }
}
