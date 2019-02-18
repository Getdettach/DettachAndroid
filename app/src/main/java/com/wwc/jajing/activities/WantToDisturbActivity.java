package com.wwc.jajing.activities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.wwc.R;
import com.wwc.jajing.component.GetJSONResponse;
import com.wwc.jajing.domain.entity.Caller;
import com.wwc.jajing.domain.entity.CallerImpl;
import com.wwc.jajing.domain.entity.User;
import com.wwc.jajing.domain.services.CallManagerAbstract;
import com.wwc.jajing.interfaces.VolleyInterface;
import com.wwc.jajing.permissions.PermissionManager;
import com.wwc.jajing.realmDB.CallsModel;
import com.wwc.jajing.realmDB.ContactModel;
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
import org.json.JSONObject;

import io.realm.RealmResults;

import static com.wwc.jajing.system.JJApplication.getRealmInstance;

public class WantToDisturbActivity extends Activity {

    private static final String TAG = "WantsToDisturb";
    // we expect the JJSMS
    public static final String JJSMS = "JJSMS";

    private TextView callersPhoneNumber,callersName;
//    private JJSMSSenderPhoneNumber callersPNumber;

    private User user;
    private JJSMSManager jjsmsManager;
    private JJSMSMessenger jjsmsMessenger;

   public boolean dont_allow;
//    private JJSMS jjsms;

    String callerNumber;

    private String callerName="Unknown";

    private Caller currentCaller;
    public static String str_allowstatus;

    String myNumber;

    final Handler handler = new Handler();
    private CallManagerAbstract callManager;
    public static String str_progressclose;
    public static String str_connectingcall;

    /*
     * Called only ONCE
     *
     * GOOD PLACE TO PUT ANY INITIAL WORK
     */

   /* //Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            RealmResults<RegisterDeviceModel> deviceValues = getRealmInstance().where(RegisterDeviceModel.class).findAll();

            if(deviceValues.size()>0){

                Constants.accessToken  = deviceValues.get(0).getAccestoken();
                myNumber = deviceValues.get(0).getPhoneNumber();

            }

            sendNotifyResponse("D");
        }
    };*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wants_to_disturb);

        //CACHE SYSTEM
        JJSystem jjSystem = JJSystemImpl.getInstance();
        user = (User) jjSystem.getSystemService(Services.USER);
        jjsmsManager = (JJSMSManager) jjSystem.getSystemService(Services.SMS_MANAGER);
        jjsmsMessenger = jjsmsManager.getMessenger();

        this.callManager = (CallManagerAbstract) jjSystem.getSystemService(Services.CALL_MANAGER);

        // Init our views
        callersPhoneNumber = (TextView) findViewById(R.id.textCallersPhoneNumber);
        callersName = (TextView)findViewById(R.id.textCallersName);

//        runnable.run();





    }

    @Override
    protected void onStart() {
        super.onStart();

//        Intent intent = getIntent();
//        this.jjsms = (JJSMS) intent.getSerializableExtra(this.JJSMS);
//        this.callersPNumber = (JJSMSSenderPhoneNumber) jjsms
//                .getSendersPhoneNumber();


        Bundle b = getIntent().getExtras();

        callerNumber = b.getString("Number");



        try{

            String number =  callerNumber.length() > 8 ? callerNumber.substring(callerNumber.toString().length() - 8) : callerNumber.toString();

            RealmResults<ContactModel> contactNumber = getRealmInstance().where(ContactModel.class).endsWith("number",number).findAll();

            if(contactNumber.size()>0){
                callerName = contactNumber.get(0).getName();
            }

        }catch(Exception e){

            e.printStackTrace();

        }


//        Log.d(TAG, jjsms.getRawJJSMS());

        callersName.setText(callerName);

        callersPhoneNumber.setText(callerNumber + " \n is calling and wants to disturb you");

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void allowCallerToDisturbHandler(View view)  // do this when we allow caller to disturb
    {
        str_allowstatus="allow";
        str_connectingcall="connect";
        Log.e(TAG, "Allowclick");

        dont_allow=true;
        handler.removeCallbacksAndMessages(null);

       String numberH = callerNumber.length() > 8 ? callerNumber.substring(callerNumber.length() - 8) : callerNumber;

        RealmResults<CallsModel> callsModel = getRealmInstance().where(CallsModel.class).endsWith("number",numberH).findAll();

        if(callsModel.size()>0)
        {
            callerNumber = callsModel.get(0).getNumber();
        }

        Log.e(TAG, "Finished giving caller permissions"+callerNumber);

        List<CallerImpl> callerImpl = CallerImpl.find(CallerImpl.class,"number=?",callerNumber);

        if(callerImpl.size()>0){
            CallerImpl callerperm = callerImpl.get(0);
            callerperm.setAllowPermission(1);
            callerperm.save();


            callManager.silenceRinger(false);
        }

        RealmResults<RegisterDeviceModel> deviceValues = getRealmInstance().where(RegisterDeviceModel.class).findAll();


        if(deviceValues.size()>0){

            Constants.accessToken  = deviceValues.get(0).getAccestoken();
            myNumber = deviceValues.get(0).getPhoneNumber();


        }

        sendNotifyResponse("A");

        this.finish();

    }


    public void sendNotifyResponse(String message){

        Map<String, String> params = new HashMap<>();
        params.put("AccessToken",Constants.accessToken);
        params.put("SenderNumber",myNumber);
        params.put("ReceiverNumber",callerNumber);
        params.put("from", "receiver");
        params.put("message", message);



        new GetJSONResponse(getApplicationContext()).RequestJsonToServer(getApplicationContext(), "DetachFCMNotification", params, new VolleyInterface() {

            @Override
            public void onSuccessResponse(final JSONObject result) {

                //System.out.println("result in Splash Activity = " + result);
                Log.e(TAG, "result*****"+result);
            }

            @Override
            public void onErroeResponse(VolleyError result) {

               // System.out.println("ERROE IN SPLASH " + result);
                Log.e(TAG, "errorresponse*****"+result);

            }

            @Override
            public void onSuccessResponse(JSONArray result) {

            }
        });

//        handler.postDelayed(runnable, 3*1000);
    }



   /* public void getServerResponse(String myNumber,String status,String id){

        Map<String, String> params = new HashMap<>();
        params.put("AccessToken",Constants.accessToken);
        params.put("Caller",callerNumber.toString());
        params.put("Receiver",myNumber);
        params.put("Scheduler","1");
        params.put("AllowDenyStatus",status);
        params.put("StatusId",id);

        new GetJSONResponse(getApplicationContext()).RequestJsonToServer(getApplicationContext(),  "DetachCallStatus", params, new VolleyInterface() {

            @Override
            public void onSuccessResponse(final JSONObject result) {

                System.out.println("result in Splash Activity = " + result);

            }

            @Override
            public void onErroeResponse(VolleyError result) {

                System.out.println("ERROE IN SPLASH "+result);

            }

            @Override
            public void onSuccessResponse(JSONArray result) {

            }
        });
    }*/


    public void denyCallerToDisturbHandler(View view)
    {
        str_connectingcall="connect";
        Log.e(TAG, "Denyclick");
        dont_allow=true;
        handler.removeCallbacksAndMessages(null);

        RealmResults<RegisterDeviceModel> deviceValues = getRealmInstance().where(RegisterDeviceModel.class).findAll();

        if(deviceValues.size()>0){

            Constants.accessToken  = deviceValues.get(0).getAccestoken();
            myNumber = deviceValues.get(0).getPhoneNumber();

        }

        sendNotifyResponse("D");

        this.finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dont_allow)
        {
            dont_allow=false;


        }
        else
        {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms


                    RealmResults<RegisterDeviceModel> deviceValues = getRealmInstance().where(RegisterDeviceModel.class).findAll();

                    if(deviceValues.size()>0){

                        Constants.accessToken  = deviceValues.get(0).getAccestoken();
                        myNumber = deviceValues.get(0).getPhoneNumber();

                    }

                    sendNotifyResponse("D");

                    finish();
//                    Toast.makeText(WantToDisturbActivity.this, "check", Toast.LENGTH_SHORT).show();
                    // handler.postDelayed(this, 2000);
                }
            },60000);
        }
    }
}













/*

private final int EVENT1 = 1;
private Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Event1:
                Toast.makeText(MyActivity.this, "Event 1", Toast.LENGTH_SHORT).show();
                break;

            default:
                Toast.makeText(MyActivity.this, "Unhandled", Toast.LENGTH_SHORT).show();
                break;
        }
    }
};*/










/*

package com.wwc.jajing.activities;

        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;
        import java.util.Timer;
        import java.util.TimerTask;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.android.volley.VolleyError;
        import com.wwc.R;
        import com.wwc.jajing.component.GetJSONResponse;
        import com.wwc.jajing.domain.entity.Caller;
        import com.wwc.jajing.domain.entity.CallerImpl;
        import com.wwc.jajing.domain.entity.User;
        import com.wwc.jajing.domain.services.CallManagerAbstract;
        import com.wwc.jajing.interfaces.VolleyInterface;
        import com.wwc.jajing.permissions.PermissionManager;
        import com.wwc.jajing.realmDB.CallsModel;
        import com.wwc.jajing.realmDB.ContactModel;
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
        import org.json.JSONObject;

        import io.realm.RealmResults;

        import static com.wwc.jajing.system.JJApplication.getRealmInstance;

public class WantToDisturbActivity extends Activity {

    private static final String TAG = "WantsToDisturb";
    // we expect the JJSMS
    public static final String JJSMS = "JJSMS";

    private TextView callersPhoneNumber,callersName;
//    private JJSMSSenderPhoneNumber callersPNumber;

    private User user;
    private JJSMSManager jjsmsManager;
    private JJSMSMessenger jjsmsMessenger;
//    private JJSMS jjsms;

    String callerNumber;

    private String callerName="Unknown";

    private Caller currentCaller;

    String myNumber;

    private CallManagerAbstract callManager;

    */
/*
     * Called only ONCE
     *
     * GOOD PLACE TO PUT ANY INITIAL WORK
     *//*

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wants_to_disturb);

        //CACHE SYSTEM
        JJSystem jjSystem = JJSystemImpl.getInstance();
        user = (User) jjSystem.getSystemService(Services.USER);
        jjsmsManager = (JJSMSManager) jjSystem.getSystemService(Services.SMS_MANAGER);
        jjsmsMessenger = jjsmsManager.getMessenger();

        this.callManager = (CallManagerAbstract) jjSystem.getSystemService(Services.CALL_MANAGER);



        // Init our views
        callersPhoneNumber = (TextView) findViewById(R.id.textCallersPhoneNumber);
        callersName = (TextView)findViewById(R.id.textCallersName);

    }

    @Override
    protected void onStart() {
        super.onStart();

//        Intent intent = getIntent();
//        this.jjsms = (JJSMS) intent.getSerializableExtra(this.JJSMS);
//        this.callersPNumber = (JJSMSSenderPhoneNumber) jjsms
//                .getSendersPhoneNumber();


        Bundle b = getIntent().getExtras();

        callerNumber = b.getString("Number");



        try{

            String number =  callerNumber.length() > 8 ? callerNumber.substring(callerNumber.toString().length() - 8) : callerNumber.toString();

            RealmResults<ContactModel> contactNumber = getRealmInstance().where(ContactModel.class).endsWith("number",number).findAll();

            if(contactNumber.size()>0){
                callerName = contactNumber.get(0).getName();
            }

        }catch(Exception e){

            e.printStackTrace();

        }


//        Log.d(TAG, jjsms.getRawJJSMS());

        callersName.setText(callerName);

        callersPhoneNumber.setText(callerNumber + " \n is calling and wants to disturb you");

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void allowCallerToDisturbHandler(View view)  // do this when we allow caller to disturb
    {

//        System.out.println("this Caller Phone Number= " + this.jjsms.getSendersPhoneNumber().toString());

//        currentCaller = CallerImpl.getCallerByPhoneNumber(this.callersPNumber.toString());
//        Log.d(TAG, "Found a caller with id:" + currentCaller.getId());
        // now give caller the permission to call
//        this.user.givePermission(CallerImpl.getCallerByPhoneNumber(this.callersPNumber.toString()), PermissionManager.Permissions.SEND_CALL);

        String numberH = callerNumber.length() > 8 ? callerNumber.substring(callerNumber.length() - 8) : callerNumber;

        RealmResults<CallsModel> callsModel = getRealmInstance().where(CallsModel.class).endsWith("number",numberH).findAll();

//        System.out.println("callsModel = " + callsModel);

        if(callsModel.size()>0)
        {
            callerNumber = callsModel.get(0).getNumber();
//            System.out.println("callsModel = " + callsModel.get(0).getNumber());
        }

        Log.d(TAG, "Finished giving caller permissions"+callerNumber);

        List<CallerImpl> callerImpl = CallerImpl.find(CallerImpl.class,"number=?",callerNumber);

//        System.out.println("callerImpl = " + callerImpl.size());
//        System.out.println("callerImpl = " + callerImpl.get(0).toString());

        if(callerImpl.size()>0){
            CallerImpl callerperm = callerImpl.get(0);
            callerperm.setAllowPermission(1);
            callerperm.save();


            callManager.silenceRinger(false);
        }

        RealmResults<RegisterDeviceModel> deviceValues = getRealmInstance().where(RegisterDeviceModel.class).findAll();

//        System.out.println("deviceValues = " + deviceValues);

        if(deviceValues.size()>0){

            Constants.accessToken  = deviceValues.get(0).getAccestoken();
            myNumber = deviceValues.get(0).getPhoneNumber();


//            RealmResults<SetStatusModel> setStatusModel = getRealmInstance().where(SetStatusModel.class).findAll();
//
//            setStatusModel.get(0).getStatusId();
//
//            getServerResponse(myNumber,"A",setStatusModel.get(0).getStatusId());

        }

        sendNotifyResponse("A");

        // now send a JJSMS response for DISTURB

//        this.jjsmsMessenger.sendRawSms(new JJSMS("#D/DISTURB?ISRESPONSE=TRUE,ALLOW=TRUE"), this.callersPNumber.toString());
//
//        Log.d(TAG, "Call receiving has chose to ALLLOW the caller to disturb!JJSMS Response sent.");

        this.finish();

//        Toast.makeText(this,"Work in progress",Toast.LENGTH_LONG).show();

    }


    public void sendNotifyResponse(String message){

//        System.out.println("callerNumber = " + callerNumber);
//        System.out.println("myNumber = " + myNumber);

        Map<String, String> params = new HashMap<>();
        params.put("AccessToken",Constants.accessToken);
        params.put("SenderNumber",myNumber);
        params.put("ReciverNumber",callerNumber);
        params.put("from","receiver");
        params.put("message",message);


        new GetJSONResponse(getApplicationContext()).RequestJsonToServer(getApplicationContext(),  "DetachFCMNotification", params, new VolleyInterface() {

            @Override
            public void onSuccessResponse(final JSONObject result) {

                System.out.println("result in Splash Activity = " + result);

            }

            @Override
            public void onErroeResponse(VolleyError result) {

                System.out.println("ERROE IN SPLASH "+result);

            }

            @Override
            public void onSuccessResponse(JSONArray result) {

            }
        });
    }



    public void getServerResponse(String myNumber,String status,String id){

        Map<String, String> params = new HashMap<>();
        params.put("AccessToken",Constants.accessToken);
        params.put("Caller",callerNumber.toString());
        params.put("Receiver",myNumber);
        params.put("Scheduler","1");
        params.put("AllowDenyStatus",status);
        params.put("StatusId",id);

        new GetJSONResponse(getApplicationContext()).RequestJsonToServer(getApplicationContext(),  "DetachCallStatus", params, new VolleyInterface() {

            @Override
            public void onSuccessResponse(final JSONObject result) {

                System.out.println("result in Splash Activity = " + result);

            }

            @Override
            public void onErroeResponse(VolleyError result) {

                System.out.println("ERROE IN SPLASH "+result);

            }

            @Override
            public void onSuccessResponse(JSONArray result) {

            }
        });
    }


    public void denyCallerToDisturbHandler(View view)
    {

        RealmResults<RegisterDeviceModel> deviceValues = getRealmInstance().where(RegisterDeviceModel.class).findAll();

//        System.out.println("deviceValues = " + deviceValues);

        if(deviceValues.size()>0){

            Constants.accessToken  = deviceValues.get(0).getAccestoken();
            myNumber = deviceValues.get(0).getPhoneNumber();


//            RealmResults<SetStatusModel> setStatusModel = getRealmInstance().where(SetStatusModel.class).findAll();
//            setStatusModel.get(0).getStatusId();
//
//            getServerResponse(myNumber,"D",setStatusModel.get(0).getStatusId());

        }

        sendNotifyResponse("D");


//        this.jjsmsMessenger.sendRawSms(new JJSMS("#D/DISTURB?ISRESPONSE=TRUE,ALLOW=FALSE"), this.callersPNumber.toString());
//        Log.d(TAG, "Call receiving has chose to DENY the caller to disturb!JJSMS Response sent.");

        this.finish();

    }

}
*/
