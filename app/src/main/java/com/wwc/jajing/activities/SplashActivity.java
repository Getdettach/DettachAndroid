package com.wwc.jajing.activities;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.IntentCompat;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.wwc.R;
import com.wwc.jajing.component.GetJSONResponse;
import com.wwc.jajing.component.MySingleton;
import com.wwc.jajing.domain.entity.TimeSetting;
import com.wwc.jajing.domain.entity.User;
import com.wwc.jajing.domain.services.CallManager;
import com.wwc.jajing.domain.value.PhoneNumber;
import com.wwc.jajing.fragment.AndroidBug5497Workaround;
import com.wwc.jajing.fragment.BaseFragment;
import com.wwc.jajing.fragment.ContactsFragment;
import com.wwc.jajing.interfaces.VolleyInterface;
import com.wwc.jajing.realmDB.CallsModel;
import com.wwc.jajing.realmDB.ContactModel;
import com.wwc.jajing.realmDB.ExtendTimeModel;
import com.wwc.jajing.realmDB.HasAppModel;
import com.wwc.jajing.realmDB.RegisterDeviceModel;
import com.wwc.jajing.realmDB.SetStatusModel;
import com.wwc.jajing.services.DetachHasAppServices;
import com.wwc.jajing.services.MyFirebaseInstanceIDService;
import com.wwc.jajing.system.JJSystem;
import com.wwc.jajing.system.JJSystemImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.wwc.jajing.system.JJApplication.getRealmInstance;

public class SplashActivity extends Activity {

    //	private User user;
    private CallManager cm;
    private String status;

    Cursor cursor;

    String name;
    String phoneNumber;

    public static int contactCount;

    String deviceId, simSerialNumber;

    LinearLayout numberLayout;
    private ProgressBar progressBar = null;
    private RelativeLayout rl_main;
    ImageView sendNumber;
    EditText editTextNumber;

    View view;
    Map<String, String> hm = new HashMap<String, String>();

    public boolean godashboard = false, bl_loginstatus;

    private Dialog dialog;

    RealmResults<RegisterDeviceModel> deviceValues;

    boolean deviceExist = false;
    SharedPreferences preferences;

    private Timer timer = new Timer();

    // public static final String AWAY_STATUS = "awayStatus";
    private User user = (User) JJSystemImpl.getInstance().getSystemService(
            JJSystemImpl.Services.USER);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {if(!Telephony.Sms.getDefaultSmsPackage(getApplicationContext()).equals(getApplicationContext().getPackageName())) {
            //Store default sms package name
            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                    getApplicationContext().getPackageName());
            startActivity(intent);
        }
        }*/
//        AndroidBug5497Workaround.assistActivity(this);

        view = findViewById(R.id.root_view);
        numberLayout = (LinearLayout) findViewById(R.id.number_layout);
        sendNumber = (ImageView) findViewById(R.id.img_send_number);
        rl_main = (RelativeLayout) findViewById(R.id.root_view);
        editTextNumber = (EditText) findViewById(R.id.phone_number);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        rl_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

            }
        });
        // CACHE JJSYSTEM
        JJSystem jjSystem = JJSystemImpl.getInstance();

        this.user = (User) jjSystem.getSystemService(JJSystemImpl.Services.USER);
        this.cm = (CallManager) jjSystem.getSystemService(JJSystemImpl.Services.CALL_MANAGER);

        //String mSerialNumber = tMgr.getSimSerialNumber();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS,Manifest.permission.READ_PHONE_NUMBERS,Manifest.permission.READ_PHONE_STATE}, 001);

        }
        else
        {
            TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

            String mPhoneNumber = tMgr.getLine1Number();
            preferences = PreferenceManager.getDefaultSharedPreferences(this);

            deviceId = tMgr.getDeviceId();
            simSerialNumber = tMgr.getSimSerialNumber();
            Log.e("SplashScreenActivity","godashboard=="+godashboard);
            Log.e("SplashScreenActivity","mPhoneNumber=="+mPhoneNumber);

            Log.e("SplashScreenActivity","mPhoneNumber=="+ mPhoneNumber);

            Realm realm = Realm.getInstance(getApplicationContext());

            deviceValues = realm.where(RegisterDeviceModel.class).findAll();

            // System.out.println("deviceValues = " + deviceValues);

            if(deviceValues.size()<=0) {

                numberLayout.setVisibility(View.VISIBLE);

                deviceExist = false;


            }else{
                try {
                    numberLayout.setVisibility(View.GONE);
                    RealmResults<RegisterDeviceModel> deviceValues = realm.where(RegisterDeviceModel.class).findAll();
                    Constants.accessToken  = deviceValues.get(0).getAccestoken();
                    Constants.registerId = deviceValues.get(0).getRegisterId();

                    // System.out.println("deviceValues wqtqet = " + deviceValues);

                    if(deviceValues.get(0).getSimSerialNumber().compareTo(simSerialNumber)!=0){

                        numberLayout.setVisibility(View.VISIBLE);

                        deviceExist = true;

                    }else{


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

                        //ContactsAsync contactsAsync= new ContactsAsync();
                        //contactsAsync.execute();
                        RealmResults<SetStatusModel> setStatusModel = realm.where(SetStatusModel.class).findAll();
                        if(setStatusModel.size()>0)
                        {
                            godashboard=true;
                        }else
                        {
                            godashboard=false;
                        }

                        new Handler().postDelayed(new Runnable() {
                            @SuppressLint("WrongConstant")
                            @Override
                            public void run() {
                                if(godashboard) {
                                    Log.e("SplashScreenActivity","godashboard Handler if=="+godashboard);
                                    Intent intent = new Intent(getApplicationContext(), SetStatusActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Log.e("SplashScreenActivity","godashboard Handlerelse=="+godashboard);
                                    Intent intent = new Intent(getApplicationContext(), StatusActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }


                            }
                        }, 2500);
                    }
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            realm.close();

            sendNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(editTextNumber.getText().length()>9){
                        if(Constants.haveNetworkConnection(getApplicationContext())){
                            responseRequest(editTextNumber.getText().toString(),user);
                        }else{
                            Snackbar.make(view,"No Internet Connection",Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    else{
                        Snackbar.make(view,"Give Valid Number",Snackbar.LENGTH_LONG).show();
                    }
                }
            });

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

            if(Constants.haveNetworkConnection(getApplicationContext())){
                //  Snackbar.make(view,"Connected With Internet",Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(view,"No Internet Connection",Snackbar.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void isDualSimOrNot(){
        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);

        String imeiSIM1 = telephonyInfo.getImeiSIM1();
        String imeiSIM2 = telephonyInfo.getImeiSIM2();

        boolean isSIM1Ready = telephonyInfo.isSIM1Ready();
        boolean isSIM2Ready = telephonyInfo.isSIM2Ready();

        boolean isDualSIM = telephonyInfo.isDualSIM();
        Log.i("Dual = "," IME1 : " + imeiSIM1 + "\n" +
                " IME2 : " + imeiSIM2 + "\n" +
                " IS DUAL SIM : " + isDualSIM + "\n" +
                " IS SIM1 READY : " + isSIM1Ready + "\n" +
                " IS SIM2 READY : " + isSIM2Ready + "\n");
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)       {
           switch (requestCode) {
               case 001:
                   if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                       TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

                       String mPhoneNumber = tMgr.getLine1Number();
                       preferences = PreferenceManager.getDefaultSharedPreferences(this);

                       deviceId = tMgr.getDeviceId();
                       simSerialNumber = tMgr.getSimSerialNumber();
                       Log.e("SplashScreenActivity","godashboard=="+godashboard);
                       Log.e("SplashScreenActivity","mPhoneNumber=="+mPhoneNumber);

                       Log.e("SplashScreenActivity","mPhoneNumber=="+ mPhoneNumber);

                       Realm realm = Realm.getInstance(getApplicationContext());

                       deviceValues = realm.where(RegisterDeviceModel.class).findAll();

                        // System.out.println("deviceValues = " + deviceValues);

                       if(deviceValues.size()<=0) {

                           numberLayout.setVisibility(View.VISIBLE);

                           deviceExist = false;


                       }else{
                           try {
                               numberLayout.setVisibility(View.GONE);
                               RealmResults<RegisterDeviceModel> deviceValues = realm.where(RegisterDeviceModel.class).findAll();
                               Constants.accessToken  = deviceValues.get(0).getAccestoken();
                               Constants.registerId = deviceValues.get(0).getRegisterId();

                                // System.out.println("deviceValues wqtqet = " + deviceValues);

                               if(deviceValues.get(0).getSimSerialNumber().compareTo(simSerialNumber)!=0){

                                   numberLayout.setVisibility(View.VISIBLE);

                                   deviceExist = true;

                               }else{


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

                                   //ContactsAsync contactsAsync= new ContactsAsync();
                                   //contactsAsync.execute();
                                   RealmResults<SetStatusModel> setStatusModel = realm.where(SetStatusModel.class).findAll();
                                   if(setStatusModel.size()>0)
                                   {
                                       godashboard=true;
                                   }else
                                   {
                                       godashboard=false;
                                   }

                                   new Handler().postDelayed(new Runnable() {
                                       @SuppressLint("WrongConstant")
                                       @Override
                                       public void run() {
                                           if(godashboard) {
                                               Log.e("SplashScreenActivity","godashboard Handler if=="+godashboard);
                                               Intent intent = new Intent(getApplicationContext(), SetStatusActivity.class);
                                               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                                               startActivity(intent);
                                               finish();
                                           }else{
                                               Log.e("SplashScreenActivity","godashboard Handlerelse=="+godashboard);
                                               Intent intent = new Intent(getApplicationContext(), StatusActivity.class);
                                               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                                               startActivity(intent);
                                               finish();
                                           }


                                       }
                                   }, 2500);
                               }
                           }catch(Exception e)
                           {
                               e.printStackTrace();
                           }
                       }
                       realm.close();


                       sendNumber.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {

                               if(editTextNumber.getText().length()>9){
                                   if(Constants.haveNetworkConnection(getApplicationContext())){
                                       responseRequest(editTextNumber.getText().toString(),user);
                                   }else{
                                       Snackbar.make(view,"No Internet Connection",Snackbar.LENGTH_SHORT).show();
                                       return;
                                   }
                               }
                               else{
                                   Snackbar.make(view,"Give Valid Number",Snackbar.LENGTH_LONG).show();
                               }
                           }
                       });


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

                       if(Constants.haveNetworkConnection(getApplicationContext())){
                        //  Snackbar.make(view,"Connected With Internet",Snackbar.LENGTH_SHORT).show();
                       }else{
                           Snackbar.make(view,"No Internet Connection",Snackbar.LENGTH_SHORT).show();
                           return;
                       }

                   }
                   break;

               default:
                   break;
           }
       }

    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void responseRequest(final String number, User user){

        //showProgressDialog();
        progressBar.setVisibility(View.VISIBLE);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Constants.registerId =  preferences.getString("registration_id", null);
        final User str_user=user;

        //  System.out.println("MyFirebasprintlneInstanceIDService.regToken = " + Constants.registerId);

        Map<String, String> params = new HashMap<>();

        params.put("DeviceNumber",deviceId);
        params.put("SimSerialNumber",simSerialNumber);
        params.put("MobileNumber",number);
        params.put("DeviceType","ANDROID");

        if(deviceExist){
            params.put("AccessToken",Constants.accessToken);
            params.put("FCMToken", Constants.registerId);
        }else{
            params.put("AccessToken","");
            params.put("FCMToken", Constants.registerId);
        }

        if(Constants.registerId == null || Constants.registerId.compareTo("")==0){
//            dismissProgressDialog();
            Snackbar.make(view,"Something went wrong!!!",Snackbar.LENGTH_LONG).show();
            return;
        }


        new GetJSONResponse(getApplicationContext()).RequestJsonToServer(getApplicationContext(), "DetachRegister", params, new VolleyInterface() {

            @SuppressLint("WrongConstant")
            @Override
            public void onSuccessResponse(final JSONObject result) {


                try {
                    JSONObject jobject=new JSONObject(result.toString());
                    String str_name=jobject.getString("name");


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                System.out.println("result in Splash Activity = " + result);

                Realm realm = Realm.getInstance(getApplicationContext());

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            try{

                               if(!deviceExist){

                                   RegisterDeviceModel registerDeviceModel = realm.createObject(RegisterDeviceModel.class);

                                   registerDeviceModel.setDeviceId(deviceId);
                                   registerDeviceModel.setPhoneNumber(number);
                                   registerDeviceModel.setSimSerialNumber(simSerialNumber);
                                   registerDeviceModel.setAccestoken(result.getString("AccessToken"));
                                   registerDeviceModel.setRegisterId(Constants.registerId);

                               }else{

                                   RealmResults<RegisterDeviceModel> registerDeviceModel = getRealmInstance().where(RegisterDeviceModel.class).equalTo("deviceId",deviceId).findAll();

//                                   getRealmInstance().beginTransaction();
                                   registerDeviceModel.get(0).setPhoneNumber(number);
                                   registerDeviceModel.get(0).setSimSerialNumber(simSerialNumber);
                                   registerDeviceModel.get(0).setRegisterId(Constants.registerId);
                                   getRealmInstance().commitTransaction();

                               }
                                /*SharedPreferences.Editor editor=preferences.edit();
                                editor.putString("checkstatus","splashcheckstatus");
                                editor.commit();*/
                                Constants.accessToken = result.getString("AccessToken");

                                ContactsAsync contactsAsync= new ContactsAsync();
                                contactsAsync.execute();

                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    });

                realm.close();
                //dismissProgressDialog();
                //this.status = this.user.getUserStatus().getAvailabilityStatus();
                updateAvailabilityStatus((str_user.getUserStatus()
                        .getAvailabilityStatus() != null) ? str_user.getUserStatus()
                        .getAvailabilityStatus() : "Not Set!");




                if(godashboard) {
                    Log.e("SplashScreenActivity","godashboardresponserequestif=="+godashboard);
                    Intent intent = new Intent(getApplicationContext(), SetStatusActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    Log.e("SplashScreenActivity","godashboardresponserequestelse=="+godashboard);
                    Intent intent = new Intent(getApplicationContext(), StatusActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

            }
            /**
             * Creates and displays a progress dialog if one does not already exist
             */
            protected void showProgressDialog() {
                try {
                    if (dialog == null) {
                        dialog = new Dialog(SplashActivity.this, R.style.CustomProgressTheme);
                        dialog.setContentView(R.layout.custom_progress);
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /**
             * Dismiss Progress Dialog
             */
            protected void dismissProgressDialog() {
                if (dialog != null && dialog.isShowing()) {
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                dialog = null;
            }
            @Override
            public void onErroeResponse(VolleyError result) {

//                System.out.println("ERROE IN SPLASH "+result);

            }

            @Override
            public void onSuccessResponse(JSONArray result) {

            }
        });


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

    public void ExceuteHandler(){
        getApplicationContext().startService(new Intent(getApplicationContext(),DetachHasAppServices.class));
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

            //dismissProgressDialog();
            progressBar.setVisibility(View.GONE);
            if(!deviceExist){
//                ExceuteHandler();
            }

        }

        @Override
        protected void onPreExecute() {

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        this.status = this.user.getUserStatus().getAvailabilityStatus();
        this.updateAvailabilityStatus((this.user.getUserStatus()
                .getAvailabilityStatus() != null) ? this.user.getUserStatus()
                .getAvailabilityStatus() : "Not Set!");

    }

    private void updateAvailabilityStatus(String status) {


        if (status.equalsIgnoreCase("AVAILABLE")) {

            godashboard=true;

        } else {
            godashboard=false;
        }

      /*  if (status.equalsIgnoreCase("AVAILABLE")) {

            godashboard=true;
            bl_loginstatus=true;

        } else {
            godashboard=false;
            bl_loginstatus=false;
        }
        SharedPreferences.Editor edit=preferences.edit();
        edit.putBoolean("Status",bl_loginstatus);
        edit.putBoolean("godashboard",godashboard);
        edit.commit();
        Log.e("SplashScreenActivity","status=="+status);
        Log.e("SplashScreenActivity","updateAvailabilityStatus=="+godashboard);*/
    }

    protected void showProgressDialog() {
        try {
            if (dialog == null) {
                dialog = new Dialog(getApplicationContext(), R.style.CustomProgressTheme);
                dialog.setContentView(R.layout.custom_progress);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Dismiss Progress Dialog
     */
    protected void dismissProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dialog = null;
    }
}
