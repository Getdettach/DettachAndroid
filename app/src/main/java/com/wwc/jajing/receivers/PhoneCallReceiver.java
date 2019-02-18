package com.wwc.jajing.receivers;

/**
 * Created by Shiju on 9/28/2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.wwc.jajing.domain.entity.TimeSetting;
import com.wwc.jajing.domain.entity.User;
import com.wwc.jajing.domain.services.CallManagerAbstract;
import com.wwc.jajing.listeners.CSLonUnavailable;
import com.wwc.jajing.realmDB.CallsModel;
import com.wwc.jajing.settings.time.TimeSettingTaskManager;
import com.wwc.jajing.sms.JJSMSManager;
import com.wwc.jajing.sms.JJSMSMessenger;
import com.wwc.jajing.system.JJSystem;
import com.wwc.jajing.system.JJSystemImpl;

import java.lang.reflect.Method;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.wwc.jajing.system.JJApplication.getRealmInstance;


public class PhoneCallReceiver extends BroadcastReceiver {
    private static final String TAG = null;
    String incommingNumber;

    User user;

    private TelephonyManager tm;

    private PhoneStateListener cslOnUnavailble;

    private JJSMSManager jjsmsManger;

    private JJSMSMessenger jjsmsMessenger;

    private CallManagerAbstract callManager;

    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if (null == bundle)
            return;
        //  SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            // Java reflection to gain access to TelephonyManager's
            // ITelephony getter
//            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Log.e(TAG, "Phonecall receiver call");

//            Log.v(TAG, "Get getTeleService...");
//            Class c = Class.forName(tm.getClass().getName());
//            Method m = c.getDeclaredMethod("getITelephony");
//            m.setAccessible(true);
//            com.android.internal.telephony.ITelephony telephonyService = (ITelephony) m.invoke(tm);

//            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
//            System.out.println("stateStr = " + stateStr);

//            TimeSetting timeSetting = TimeSetting.findById(TimeSetting.class, TimeSettingTaskManager.getInstance().getTimeSettingIdClosestToBeingDone());

//            if (intent != null) {
//                Bundle b = intent.getExtras();
//                incommingNumber = b.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
//                Log.v(TAG, incommingNumber);
//
//            }
//            JJSystem jjSystem = JJSystemImpl.getInstance();
//            this.user = (User) jjSystem.getSystemService(JJSystemImpl.Services.USER);

//            JJSystem jjSystem = JJSystemImpl.getInstance();
//            this.user = (User) jjSystem.getSystemService(
//                    JJSystemImpl.Services.USER);
//
//            this.jjsmsManger = (JJSMSManager) jjSystem.getSystemService(JJSystemImpl.Services.SMS_MANAGER);
//
//            if(this.jjsmsManger == null) {
//                throw new IllegalStateException("jjsmsManager should not be null!");
//            }
//            this.jjsmsMessenger  = jjsmsManger.getMessenger();
//
//            this.callManager = (CallManagerAbstract) jjSystem.getSystemService(JJSystemImpl.Services.CALL_MANAGER);
//
//            this.callManager.sendCallToVoicemail();


            this.registerCSLonUnavailable();

//            getRealmInstance().executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    CallsModel callsModel = realm.createObject(CallsModel.class); // Create managed objects directly
//                    if (incommingNumber != null)
//                        callsModel.setNumber(incommingNumber);
//                    callsModel.setType(user.getUserStatus()
//                            .getAvailabilityStatus());
//                    callsModel.setTime(String.valueOf(System.currentTimeMillis()));
//                }
//            });
//
//            RealmResults<CallsModel> callerList = getRealmInstance().where(CallsModel.class).findAll();
//            System.out.println("call history= " + callerList);




//            if (!user.getUserStatus()
//                    .getAvailabilityStatus().equalsIgnoreCase("AVAILABLE") && stateStr.compareTo("RINGING") == 0) {
//                telephonyService = (ITelephony) m.invoke(tm);
//                telephonyService.silenceRinger();
//                telephonyService.endCall();
//                getRealmInstance().executeTransaction(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//                        CallsModel callsModel = realm.createObject(CallsModel.class); // Create managed objects directly
//                        if (incommingNumber != null)
//                            callsModel.setNumber(incommingNumber);
//                        callsModel.setType(user.getUserStatus()
//                                .getAvailabilityStatus());
//                        callsModel.setTime(String.valueOf(System.currentTimeMillis()));
//                    }
//                });
//                try {
//
//                    TimeSetting mainTimeSetting = TimeSetting.findById(TimeSetting.class,
//                            1);
//
//                    String msg = "The person you are trying to reach is On an Important Call and will be unavailable until unknown gmt+05:30 Download the App: www.dettach.com";
//
//                    msg = msg.replace("unknown gmt+05:30", mainTimeSetting.getEndTime());
//
//                    SmsManager smsManager = SmsManager.getDefault();
//                    smsManager.sendTextMessage(incommingNumber, null, msg, null, null);
//                    Toast.makeText(context, "Message Sent",
//                            Toast.LENGTH_LONG).show();
//                } catch (Exception e) {
//                    Toast.makeText(context, "Message not Sent",
//                            Toast.LENGTH_LONG).show();
//                    e.printStackTrace();
//                }
//
//                Log.v(TAG, "BYE BYE BYE");
//            }
//
//
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,
                    "FATAL ERROR: could not connect to telephony subsystem");
            Log.e(TAG, "Exception object: " + e);
        }


    }

    public void registerCSLonUnavailable() {

        this.cslOnUnavailble = new CSLonUnavailable();
        tm.listen(this.cslOnUnavailble, PhoneStateListener.LISTEN_CALL_STATE);
        Log.e(TAG, "CSLonUnavailable registered");

    }
}
