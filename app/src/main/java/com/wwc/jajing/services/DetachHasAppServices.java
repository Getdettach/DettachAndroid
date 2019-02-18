package com.wwc.jajing.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.IntentCompat;

import com.android.volley.VolleyError;
import com.wwc.jajing.activities.Constants;
import com.wwc.jajing.activities.SetStatusActivity;
import com.wwc.jajing.activities.StatusActivity;
import com.wwc.jajing.component.GetJSONResponse;
import com.wwc.jajing.interfaces.VolleyInterface;
import com.wwc.jajing.realmDB.CallsModel;
import com.wwc.jajing.realmDB.HasAppModel;
import com.wwc.jajing.realmDB.RegisterDeviceModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.wwc.jajing.system.JJApplication.getRealmInstance;

/**
 * Created by Sajen on 14/11/16.
 */

public class DetachHasAppServices extends Service {

    private Timer timer = new Timer();

//    JSONObject hasAppData;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Realm realm = Realm.getInstance(getApplicationContext());

        RealmResults<RegisterDeviceModel> deviceAccessToken = realm.where(RegisterDeviceModel.class).findAll();
        Constants.accessToken = deviceAccessToken.get(0).getAccestoken();

        realm.close();

        ExceuteHandler();

    }

    public void ExceuteHandler(){

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                Realm realm = Realm.getInstance(getApplicationContext());

                RealmResults<HasAppModel> hasAppUsers = realm.where(HasAppModel.class).findAll();

//                System.out.println("hasAppUsers in SERVICE = " + hasAppUsers);

                if(hasAppUsers.size() > 0){
                    sendRequestToServer(hasAppUsers);
                }

                realm.close();

            }
        }, 0, 60*1000);
    }

    public void sendRequestToServer(RealmResults<HasAppModel> hasAppUsers){

        JSONObject userData;
        JSONObject userData1 = new JSONObject();
        JSONArray userDataArray = new JSONArray();

        try {

            for(int i =0 ;i<hasAppUsers.size();i++){

                userData = new JSONObject();
                userData.put("UserName", "Check");
                userData.put("MobileNumber", hasAppUsers.get(i).getNumber().toString());

                userDataArray.put(userData);
            }

            userData1.put("Contacts",userDataArray);
            userData1.put("AcessToken",Constants.accessToken);
//                userData1.put("AccessToken",Constants.accessToken);


        }catch(Exception e){
            e.printStackTrace();
        }


        new GetJSONResponse(getApplicationContext()).RequestJsonToServerArray(getApplicationContext(), "DetachContacts", userData1, new VolleyInterface() {

            @Override
            public void onSuccessResponse(JSONObject result) {

            }

            @Override
            public void onErroeResponse(VolleyError result) {

//                System.out.println("ERROE IN SPLASH "+result);

            }
            @Override
            public void onSuccessResponse(final JSONArray result) {

//                System.out.println("Service Result = " + result);

                for(int i = 0;i<result.length();i++){

                    try {

                        final JSONObject jsonObj = result.getJSONObject(i);

                        Realm realm = Realm.getInstance(getApplicationContext());

                        final RealmResults<HasAppModel> hasAppModel = realm.where(HasAppModel.class).equalTo("number", jsonObj.getString("MobileNumber" +
                                "")).findAll();

                        realm.beginTransaction();

                        hasAppModel.get(0).setNumber(jsonObj.getString("MobileNumber"));
                        hasAppModel.get(0).setAccessToken(jsonObj.getString("AccessToken"));
                        hasAppModel.get(0).setCalAllowStatus("0");
                        hasAppModel.get(0).setStatusName(jsonObj.getString("Status"));
                        hasAppModel.get(0).setAvailTime(jsonObj.getString("EndTime"));
                        hasAppModel.get(0).setIsActive(String.valueOf(jsonObj.getBoolean("IsActive")));
                        hasAppModel.get(0).setId(jsonObj.getString("DetachUserStatusID"));

                        realm.commitTransaction();

                        realm.close();

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

            }


        });


    }

}
