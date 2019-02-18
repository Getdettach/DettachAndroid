package com.wwc.jajing.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.android.volley.VolleyError;
import com.wwc.R;
import com.wwc.jajing.component.GetJSONResponse;
import com.wwc.jajing.domain.entity.TimeSetting;
import com.wwc.jajing.domain.entity.User;
import com.wwc.jajing.interfaces.VolleyInterface;
import com.wwc.jajing.realmDB.ContactModel;
import com.wwc.jajing.realmDB.HasAppModel;
import com.wwc.jajing.realmDB.RegisterDeviceModel;
import com.wwc.jajing.system.JJSystem;
import com.wwc.jajing.system.JJSystemImpl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.wwc.jajing.system.JJApplication.getRealmInstance;

public class LoginstatusActivity extends AppCompatActivity {
    SharedPreferences preferences;
    // public static final String AWAY_STATUS = "awayStatus";
    private User user = (User) JJSystemImpl.getInstance().getSystemService(
            JJSystemImpl.Services.USER);
    private String status;
    public  boolean godashboard=false,bl_loginstatus;
    Cursor cursor ;
    String name;
    String phoneNumber;

    public static int contactCount;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginstatus);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Realm realm = Realm.getInstance(getApplicationContext());

        // CACHE JJSYSTEM
        JJSystem jjSystem = JJSystemImpl.getInstance();
        this.user = (User) jjSystem.getSystemService(JJSystemImpl.Services.USER);
        this.status = this.user.getUserStatus().getAvailabilityStatus();
        this.updateAvailabilityStatus((this.user.getUserStatus()
                .getAvailabilityStatus() != null) ? this.user.getUserStatus()
                .getAvailabilityStatus() : "Not Set!");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//sathish
        Log.e("LoginstatusActivity","SplashActivity.str_checkstatus------->>>"+preferences.getString("checkstatus",""));
        if(preferences.getString("checkstatus","").equals("splashcheckstatus"))
        {
            //Log.e("LoginstatusActivity","SplashActivity.str_checkstatus------->>>"+SplashActivity.str_checkstatus);
            //SplashActivity.str_checkstatus="";
            RealmResults<RegisterDeviceModel> deviceValues = realm.where(RegisterDeviceModel.class).findAll();
            Constants.accessToken  = deviceValues.get(0).getAccestoken();
            Constants.registerId = deviceValues.get(0).getRegisterId();

            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

            Calendar c = Calendar.getInstance();

            TimeSetting timeSetting;

            String startTime = null,endtime=null;

            List<TimeSetting> timeList = TimeSetting.listAll(TimeSetting.class);

            for (TimeSetting aTimeSetting : timeList) {

                if(aTimeSetting.getId() != 1 && aTimeSetting.isOn())
                {
                    timeSetting = TimeSetting.findById(TimeSetting.class, aTimeSetting.getId());

                    startTime = timeSetting.getStartTime();
                    endtime = timeSetting.getEndTime();

                    break;
                }
            }

            Realm realmC = Realm.getInstance(getApplicationContext());
            RealmResults<ContactModel> contactNumber = realmC.where(ContactModel.class).findAll();

            if(contactNumber.size()==0) {

                try {

                    cursor = getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

                    contactCount = cursor.getCount();

                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            realmC.close();

            Log.e("LoginstatusActivity","Constants.accessToken------->>>"+Constants.accessToken);
            Log.e("LoginstatusActivity","Constants.registerId------>>>"+Constants.registerId);

        }else
        {

        }
//sathish
        if(preferences.getBoolean("Status",false))
        {
            Log.e("LoginstatusActivity","godashboardif------->>>"+godashboard);
            if(preferences.getBoolean("godash",false)) {
                Log.e("LoginStatusActivity","godashboard Handler if=="+preferences.getBoolean("godash",false));
                Intent intent = new Intent(getApplicationContext(), SetStatusActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }else{
                Log.e("LoginStatusActivity","godashboard Handlerelse=="+preferences.getBoolean("godash",false));
                Intent intent = new Intent(getApplicationContext(), StatusActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }else
        {
            Log.e("LoginstatusActivity","godashboardelse------->>>"+preferences.getBoolean("Status",false));
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }
        new ContactsAsync().execute();

    }
    @Override
    public void onStart() {
        super.onStart();

    }
    private void updateAvailabilityStatus(String status) {
        Log.e("LoginstatusActivity","status====="+status);
        if (status.equalsIgnoreCase("AVAILABLE")) {

            godashboard=true;


        } else {
            godashboard=false;

        }
        Log.e("LoginstatusActivity","godashboard====="+godashboard);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("godash",godashboard);
        editor.commit();

    }
    private class ContactsAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                cursor = getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                int contactIdIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
                int nameIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int phoneNumberIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int photoIdIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID);
//
//
//                contactCount = cursor.getCount();
//
//                System.out.println("Cursor length------> "+cursor.getCount());

                cursor.moveToFirst();
                do {
                    String idContact = cursor.getString(contactIdIdx);
                    name = cursor.getString(nameIdx);
                    phoneNumber = cursor.getString(phoneNumberIdx);

                    phoneNumber = phoneNumber.replaceAll(" ","");
//                    System.out.println("phon = " + phoneNumber);

                    String number =  phoneNumber.length() > 8 ? phoneNumber.substring(phoneNumber.length() - 8) : phoneNumber;

                    Realm realm = Realm.getInstance(getApplicationContext());

                    RealmResults<ContactModel> contactNumber = realm.where(ContactModel.class).endsWith("number",number).findAll();

                    if(contactNumber.size()==0){
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                ContactModel callsModel = realm.createObject(ContactModel.class); // Create managed objects directly
                                callsModel.setNumber(phoneNumber);
                                callsModel.setName(name);
                            }
                        });
                    }


//                    realm.executeTransaction(new Realm.Transaction() {
//                        @Override
//                        public void execute(Realm realm) {
//                            ContactModel callsModel = realm.createObject(ContactModel.class); // Create managed objects directly
//                            callsModel.setNumber(phoneNumber);
//                            callsModel.setName(name);
////                        callsModel.setPicture(finalImage_uri);
//                        }
//                    });
//
//                    realm.close();

                    //...
                } while (cursor.moveToNext());

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }



            return null;
        }

        @Override
        protected void onPostExecute(String result) {

//            if(godashboard) {
//                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
//            }else{
//                Intent intent = new Intent(getApplicationContext(), StatusActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
//            }


            ContactsUpload contactsUpload= new ContactsUpload();
            contactsUpload.execute();

//            ExceuteHandler();


        }

        @Override
        protected void onPreExecute() {

        }
    }

    private class ContactsUpload extends AsyncTask<String, Void, String> {

        JSONObject userData;
        JSONObject userData1 = new JSONObject();
        JSONArray userDataArray = new JSONArray();

        @Override
        protected String doInBackground(String... params) {

            Realm realm = Realm.getInstance(getApplicationContext());

            RealmResults<ContactModel> contactModel = realm.where(ContactModel.class).findAll();

//            System.out.println("contacyt Lisyt "+contactModel);

            try {

                for(int i =0 ;i<contactModel.size();i++){

                    userData = new JSONObject();
                    userData.put("UserName", contactModel.get(i).getName().toString());
                    userData.put("MobileNumber", contactModel.get(i).getNumber().toString());

                    userDataArray.put(userData);
                }

                userData1.put("Contacts",userDataArray);
                userData1.put("AccessToken",Constants.accessToken);
//                userData1.put("AccessToken",Constants.accessToken);


            }catch(Exception e){
                e.printStackTrace();
            }

            realm.close();

//            System.out.println("contactModel userData = " + userData1);

            new GetJSONResponse(getApplicationContext()).RequestJsonToServerArray(getApplicationContext(),  "DetachContacts", userData1, new VolleyInterface() {

                @Override
                public void onSuccessResponse(final JSONObject result) {

                    System.out.println("result in Splash Activity = " + result);

                }

                @Override
                public void onErroeResponse(VolleyError result) {

                }

                @Override
                public void onSuccessResponse(JSONArray result) {

//                    System.out.println("Contacts in Splash Activity = " + result);

                    getRealmInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.clear(HasAppModel.class);
                        }
                    });

                    for(int i =0;i<result.length();i++ ){

                        try {

                            final JSONObject jsonObj = result.getJSONObject(i);

                            String number =  jsonObj.getString("MobileNumber");

                            number =  number.length() > 8 ? number.substring(number.length() - 8) : number;

                            Realm realm = Realm.getInstance(getApplicationContext());

                            RealmResults<HasAppModel> hasAppModel = realm.where(HasAppModel.class).endsWith("number",number).findAll();

                            if(hasAppModel.size() == 0) {

                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {

                                        HasAppModel hasAppModel = realm.createObject(HasAppModel.class); // Create managed objects directly

                                        try{
                                            hasAppModel.setNumber(jsonObj.getString("MobileNumber"));
                                            hasAppModel.setAccessToken(jsonObj.getString("AccessToken"));
                                            hasAppModel.setCalAllowStatus("0");
                                            hasAppModel.setStatusName(jsonObj.getString("Status"));
                                            hasAppModel.setAvailTime(jsonObj.getString("EndTime"));
                                            hasAppModel.setIsActive(String.valueOf(jsonObj.getBoolean("IsActive")));
                                            hasAppModel.setId(jsonObj.getString("DetachUserStatusID"));
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                            else
                            {
                                realm.beginTransaction();
                                hasAppModel.get(0).setAccessToken(jsonObj.getString("AccessToken"));
                                hasAppModel.get(0).setCalAllowStatus("0");
                                hasAppModel.get(0).setStatusName(jsonObj.getString("Status"));
                                hasAppModel.get(0).setAvailTime(jsonObj.getString("EndTime"));
                                hasAppModel.get(0).setIsActive(String.valueOf(jsonObj.getBoolean("IsActive")));
                                hasAppModel.get(0).setId(jsonObj.getString("DetachUserStatusID"));
                                realm.commitTransaction();
                            }

                            realm.close();

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            /*if(!deviceExist){
//                ExceuteHandler();
            }*/

        }

        @Override
        protected void onPreExecute() {

        }

    }
}
