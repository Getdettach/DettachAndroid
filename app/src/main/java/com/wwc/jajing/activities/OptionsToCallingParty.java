package com.wwc.jajing.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.wwc.R;
import com.wwc.jajing.component.GetJSONResponse;
import com.wwc.jajing.interfaces.VolleyInterface;
import com.wwc.jajing.realmDB.ContactModel;
import com.wwc.jajing.realmDB.HasAppModel;
import com.wwc.jajing.realmDB.RegisterDeviceModel;
import com.wwc.jajing.realmDB.SetStatusModel;
import com.wwc.jajing.sms.JJSMS;
import com.wwc.jajing.sms.JJSMSManager;
import com.wwc.jajing.sms.JJSMSMessenger;
import com.wwc.jajing.sms.JJSMSSenderPhoneNumber;
import com.wwc.jajing.system.JJSystem;
import com.wwc.jajing.system.JJSystemImpl;
import com.wwc.jajing.system.JJSystemImpl.Services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.wwc.jajing.system.JJApplication.getRealmInstance;


public class OptionsToCallingParty extends Activity {


    private static final String TAG = "OptionsToCallingParty";
    // we expect the JJSMS
    public static final String JJSMS = "JJSMS";
    // we expect the reason why the call-receiver is not available to answer
    // phone
    public static final String REASON = "AVAILABILITYSTATUS";
    public static final String TIME_BACK = "AVAILABILITYTIME";


    private TextView callReceivingPhoneNumberView,callReceivingCallerName;
    private TextView callReveingReasonBeingBusyView,callReveingTime;
    private Button doNotDisturbButton;

    private JJSMSSenderPhoneNumber callReceivingPhoneNumber;
    private String callReceivingReasonForBeingUnavailable;
    private String callReceivingTimeBack;

    private String callerName="Unknown";

    private Timer timer = new Timer();
    public boolean bl_unabletoreachcall;
    private Context context;


    private JJSMSManager jjsmsManger = (JJSMSManager) JJSystemImpl.getInstance().getSystemService(Services.SMS_MANAGER);
    private JJSMSMessenger jjsmsMessenger = jjsmsManger.getMessenger();
    private JJSMS jjsms;

    String myNumber,id;


    /*
     * Called only ONCE
     *
     * GOOD PLACE TO PUT ANY INITIAL WORK
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_to_calling_party);

        // Init our views
        callReceivingPhoneNumberView = (TextView) findViewById(R.id.txtCallingPhoneNumber);
        callReveingReasonBeingBusyView = (TextView) findViewById(R.id.textCallingDescription);
        callReveingTime = (TextView) findViewById(R.id.textCallingTime);
        callReceivingCallerName = (TextView)findViewById(R.id.txtCallerName);
        JJSystem jjSystem = JJSystemImpl.getInstance();
        this.context=(Context) jjSystem.getSystemService(Services.CONTEXT);
        Log.e(TAG, "OptionsToCallingParty"+"Oncreatecalled");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "OptionsToCallingParty"+"Onstartcalled");
        Intent intent = getIntent();
        this.jjsms = (JJSMS) intent.getSerializableExtra(this.JJSMS);
        this.callReceivingPhoneNumber = (JJSMSSenderPhoneNumber) jjsms.getSendersPhoneNumber();

        try{

            String number =  this.callReceivingPhoneNumber.toString().length() > 8 ? this.callReceivingPhoneNumber.toString().substring(this.callReceivingPhoneNumber.toString().length() - 8) : this.callReceivingPhoneNumber.toString();

            RealmResults<ContactModel> contactNumber = getRealmInstance().where(ContactModel.class).endsWith("number",number).findAll();

            if(contactNumber.size()>0){
                callerName = contactNumber.get(0).getName();
            }else{

            }

        }catch(Exception e){

            e.printStackTrace();

        }

        Log.d(TAG, "The callRecevingPhoneNumber is:" + callReceivingPhoneNumber);
        Log.d(TAG, "The availabilityStatus is:" + jjsms.getFromExtras(this.REASON));
        Log.d(TAG, jjsms.getRawJJSMS());

        this.callReceivingReasonForBeingUnavailable = (String) jjsms
                .getFromExtras(this.REASON);


        this.callReceivingTimeBack = (String) jjsms
                .getFromExtras(this.TIME_BACK);

        this.injectText();

    }

    /*@Override
    protected void onStop() {
        Log.e(TAG, "OptionsToCallingParty"+"Onstopcalled");
        super.onStop();
        this.finish();
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "OptionsToCallingParty"+"Onpausecalled");

    }

    private void injectText() {
        callReceivingPhoneNumberView.setText(callReceivingPhoneNumber
                .toString());
        callReveingReasonBeingBusyView.setText(this.callReceivingReasonForBeingUnavailable);
        callReveingTime.setText(this.callReceivingTimeBack);
        callReceivingCallerName.setText(callerName);
    }

    public void requestToDisturbHandler(View view) {
        //to keep track of the request have to attach a requestid

//        String jjsmsRequestStr = "#D/DISTURB?";
//        this.jjsmsMessenger.sendRawSms(new JJSMS(jjsmsRequestStr), this.jjsms.getSendersPhoneNumber().toString());

        if(!Constants.haveNetworkConnection(getApplicationContext())){
            Snackbar.make(view,"No Internet Connection",Snackbar.LENGTH_SHORT).show();
            return;
        }

        /*try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            String packageName = componentInfo.getPackageName();

//								System.out.println("Package Name === " + packageName);
//								System.out.println("Context === " + this.context);
//								System.out.println("getAppli = " + appInfo);

            if (packageName.compareTo("com.wwc") == 0) {

									Intent mveInt = new Intent();
									mveInt.setAction(Intent.ACTION_MAIN);
									mveInt.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
									mveInt.addCategory(Intent.CATEGORY_HOME);
									this.context.startActivity(mveInt);
            }else{

            }
        }catch(Exception e){
            e.printStackTrace();
        }*/
        RealmResults<RegisterDeviceModel> deviceValues = getRealmInstance().where(RegisterDeviceModel.class).findAll();

        if(deviceValues.size()>0){

            Constants.accessToken  = deviceValues.get(0).getAccestoken();
            myNumber = deviceValues.get(0).getPhoneNumber();

//            String number = this.callReceivingPhoneNumber.toString().length() > 8 ? this.callReceivingPhoneNumber.toString().substring(this.callReceivingPhoneNumber.toString().length() - 8) : this.callReceivingPhoneNumber.toString();
//
//            RealmResults<HasAppModel> hasAppModel = getRealmInstance().where(HasAppModel.class).endsWith("number", number).findAll();
//
//            if (hasAppModel.size() > 0) {
//
//                id = hasAppModel.get(0).getId();
//
//                sendNotifyResponse();
//
////                getServerResponse(myNumber,id);
//
//            }else{
                sendRequestToServer(this.callReceivingPhoneNumber.toString());
//            }

            finish();
            Log.e(TAG, "request to disturb sent.");
            showProgressDialog();

        }


    }

    public void ExceuteHandler(final String myNumber,final String id){

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                getServerResponse(myNumber,id);

            }
        }, 0, 5*1000);
    }

    private void showProgressDialog() {
        Intent i = new Intent(this, PleaseWait.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("title", "Connecting Call....");
        i.putExtra("number", this.jjsms.getSendersPhoneNumber().toString());
        i.putExtra("mynumber", myNumber);
        i.putExtra("id", id);
        i.putExtra("description", "please wait");
        this.startActivity(i);
    }

    public void getServerResponse(String myNumber,String id){

        Map<String, String> params = new HashMap<>();
        params.put("AccessToken",Constants.accessToken);
        params.put("Caller",myNumber);
        //params.put("Receiver",this.callReceivingPhoneNumber.toString());
        params.put("Scheduler","1");
        params.put("AllowDenyStatus","");
        params.put("StatusId",id);

        new GetJSONResponse(getApplicationContext()).RequestJsonToServer(getApplicationContext(),  "DetachCallStatus", params, new VolleyInterface() {

            @Override
            public void onSuccessResponse(final JSONObject result) {
                Log.e(TAG, "DetachCallStatus result=="+result);
                System.out.println("result in Splash Activity = " + result);
                sendNotifyResponse();
                finish();
                Log.d(TAG, "request to disturb sent.");
                showProgressDialog();

            }

            @Override
            public void onErroeResponse(VolleyError result) {

                System.out.println("ERROE IN SPLASH "+result);
                Log.e(TAG, "DetachCallStatus VolleyError=="+result);

            }

            @Override
            public void onSuccessResponse(JSONArray result) {

            }
        });
    }

    public void sendNotifyResponse(){

        Map<String, String> params = new HashMap<>();
        params.put("AccessToken",Constants.accessToken);
        params.put("SenderNumber",myNumber);
        params.put("ReceiverNumber",this.callReceivingPhoneNumber.toString());
        params.put("from","Caller");
        params.put("message","");

        Log.e(TAG, "AccessToken=="+Constants.accessToken);
        Log.e(TAG, "SenderNumber=="+myNumber);
        Log.e(TAG, "ReciverNumber=="+this.callReceivingPhoneNumber.toString());
        Log.e(TAG, "from=="+"Caller");
        Log.e(TAG, "message=="+"HI welcome");

        new GetJSONResponse(getApplicationContext()).RequestJsonToServer(getApplicationContext(),  "DetachFCMNotification", params, new VolleyInterface() {

            @Override
            public void onSuccessResponse(final JSONObject result) {

                //System.out.println("result in Splash Activity = " + result);
                Log.e(TAG, "result*****"+result);
            }

            @Override
            public void onErroeResponse(VolleyError result) {

                //System.out.println("ERROE IN SPLASH "+result);
                Log.e(TAG, "errorresponse*****"+result);

            }

            @Override
            public void onSuccessResponse(JSONArray result) {

            }
        });


    }
    public void sendRequestToServer(final String number){

//		System.out.println("availTime = " + availTime);

        JSONObject userData;
        JSONObject userData1 = new JSONObject();
        JSONArray userDataArray = new JSONArray();

        try {

            userData = new JSONObject();
            userData.put("UserName", "Check");
            userData.put("MobileNumber", number);

            userDataArray.put(userData);

            userData1.put("Contacts", userDataArray);
            userData1.put("AcessToken", Constants.accessToken);
        }catch(Exception e){
            e.printStackTrace();
        }


        new GetJSONResponse(getApplicationContext()).RequestJsonToServerArray(getApplicationContext(), "DetachContacts", userData1, new VolleyInterface() {

            @Override
            public void onSuccessResponse(final JSONArray result) {

                //System.out.println("Service Result = " + result);
                Log.e("OptionToCallingParty","sendrequesttoserver result======"+result);

                try {

                    if (result.length() != 0) {

                        final JSONObject jsonObj = result.getJSONObject(0);

                        Realm realm = Realm.getInstance(getApplicationContext());

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

                        id = jsonObj.getString("DetachUserStatusID");

//                        getServerResponse(myNumber,id);

                        sendNotifyResponse();
                       // new addUser().execute();

                        realm.close();


                    }else if(result.length() == 0){
                            finish();
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onErroeResponse(VolleyError result) {

                System.out.println("ERROE IN SPLASH "+result);

            }

            @Override
            public void onSuccessResponse(JSONObject result) {

            }
        });


    }




    /*
     *
     * Takes to the user to DoNotDisturbOptions Activity, and passes the JJSMS
     * object with it.
     *
     * The DoNotDisturbOptions Activity is shown when the user hits "NO" on the
     * OptionsToCallingParty Activity
     */
    public void showDoNotDisturbOptions(View view) {

        bl_unabletoreachcall=true;
        Intent i = new Intent(this, DoNotDisturbOptions.class);
        i.putExtra(JJSMS, this.jjsms.getSendersPhoneNumber().toString());
        i.putExtra("unabletotakecall",bl_unabletoreachcall);
        startActivity(i);
        //finish();
    }

}
