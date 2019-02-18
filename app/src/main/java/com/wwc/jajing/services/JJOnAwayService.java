package com.wwc.jajing.services;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.wwc.R;
import com.wwc.jajing.activities.Constants;
import com.wwc.jajing.activities.SetStatusActivity;
import com.wwc.jajing.activities.SplashActivity;
import com.wwc.jajing.activities.TimeSettingActivity;
import com.wwc.jajing.activities.UpdateTimeActivity;
import com.wwc.jajing.component.GetJSONResponse;
import com.wwc.jajing.domain.entity.TimeSetting;
import com.wwc.jajing.domain.entity.User;
import com.wwc.jajing.domain.services.CallManager;
import com.wwc.jajing.domain.services.CallManagerAbstract;
import com.wwc.jajing.domain.value.AvailabilityTime;
import com.wwc.jajing.fragment.DashboardFragment;
import com.wwc.jajing.interfaces.VolleyInterface;
import com.wwc.jajing.listeners.CSLonAvailable;
import com.wwc.jajing.listeners.CSLonUnavailable;
import com.wwc.jajing.realmDB.CallsModel;
import com.wwc.jajing.realmDB.ExtendTimeModel;
import com.wwc.jajing.realmDB.HasAppModel;
import com.wwc.jajing.realmDB.RegisterDeviceModel;
import com.wwc.jajing.realmDB.RepeatDaysModel;
import com.wwc.jajing.realmDB.SetStatusModel;
import com.wwc.jajing.settings.time.TimeSettingTaskManager;
import com.wwc.jajing.sms.JJSMSManager;
import com.wwc.jajing.sms.JJSMSMessenger;
import com.wwc.jajing.system.JJSystem;
import com.wwc.jajing.system.JJSystemImpl;
import com.wwc.jajing.system.JJSystemImpl.Services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.wwc.jajing.system.JJApplication.getRealmInstance;

/*
 * This is the service that should run on the background when the user DOES NOT WANT TO BE DISTURBED.
 * 
 * This service is responsible for:
 * -- activating a "onCallStateListener" that will listen for incoming phone calls.
 * -- silencing the phone
 * 
 * 
 * 
 * 
 */
public class JJOnAwayService extends Service {


    private static final String TAG = "JJOnAwayService";
    private TelephonyManager tm;
    private CallManager cm;

    private PhoneStateListener cslOnUnavailble;
    private PhoneStateListener cslOnAvailble;

    private User user;
    Timer repeatTask = new Timer();

    long difference,startDiff,diff;

    Notification notification,notificationavailable;

    Long timeId=0L;

    TimeSetting timeSetting;

    String startTime,endtime,status;

    boolean allowCal = true;

    private JJSMSManager jjsmsManger;

    private JJSMSMessenger jjsmsMessenger;

    private CallManagerAbstract callManager;

    public Handler handler = null;
    public static Runnable runnable = null;
    public static boolean bl_availablestatus;

    String state;

    public JJOnAwayService() {
        super();
        //CACHE SYSTEM FOR PERFORMANCE
        JJSystem jjSystem = JJSystemImpl.getInstance();
        this.cm = (CallManager) jjSystem.getSystemService(Services.CALL_MANAGER);
        this.user = (User) jjSystem.getSystemService(Services.USER);

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "service is no longer bound to AwayActivity.");

        return super.onUnbind(intent);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        this.startAwayForegruondService();
        // We want this service to stop running if it gets shut down b/c of memory problems
        return START_STICKY;
    }

    @SuppressLint("NewApi")
    private void startAwayForegruondService() {

        Intent intent = new Intent(this, SplashActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0,intent, 0);

        notification = new Notification.Builder(this).setContentIntent(pIntent).setContentTitle("Dettach").setContentText("You will be Unavailable to reach").setSmallIcon(R.drawable.logo1).build();
        notificationavailable = new Notification.Builder(this).setContentIntent(pIntent).setContentTitle("Dettach").setContentText("Available call").setSmallIcon(R.drawable.logo1).build();
    }


    /*
     * The system calls this method when the service is first created, to
     * perform one-time setup procedures
     *
     *
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "JJOnAwayService service created");

        try {

            getTimeSettings();
            Log.e("startDiff======", String.valueOf(startDiff));
            timeDifference();


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    startForeground(1, notification);

                    JJSystem jjSystem = JJSystemImpl.getInstance();
                    user = (User) jjSystem.getSystemService(
                            JJSystemImpl.Services.USER);

                    jjsmsManger = (JJSMSManager) jjSystem.getSystemService(JJSystemImpl.Services.SMS_MANAGER);

                    if (jjsmsManger == null) {
                        throw new IllegalStateException("jjsmsManager should not be null!");
                    }
                    jjsmsMessenger = jjsmsManger.getMessenger();

                    callManager = (CallManagerAbstract) jjSystem.getSystemService(JJSystemImpl.Services.CALL_MANAGER);

                    callManager.disconnectCall();
                    callManager.silenceRinger(true);

                    Realm realm = Realm.getInstance(getApplicationContext());

                    getTimeSettings();

                    RealmResults<RegisterDeviceModel> deviceValues = getRealmInstance().where(RegisterDeviceModel.class).findAll();

                    if(deviceValues.size()>0) {

                        Constants.accessToken = deviceValues.get(0).getAccestoken();
                    }

                    final RealmResults<ExtendTimeModel> extendTimeModel = realm.where(ExtendTimeModel.class).equalTo("timeId", timeId.toString()).equalTo("startId", "0").findAll();

                   Log.e("extendTimeModel = " , String.valueOf(extendTimeModel.size()));

                    if (extendTimeModel.size() > 0) {
                        realm.beginTransaction();
                        extendTimeModel.get(0).setStartId("1");
                        realm.commitTransaction();
                        setTimeSettings("1");

                    }

                    realm.close();

                    startServiceWork();

                }
            }, startDiff);
        }catch(Exception e){
            e.printStackTrace();
        }

        this.registerCSLonUnavailable();
        this.unregisterCSLonAvailable();

    }


    public void startServiceWork(){

        scheduleJob();


    }

    public void getTimeSettings(){

        List<TimeSetting> timeList = TimeSetting.listAll(TimeSetting.class);
            Log.i("Time_list", timeList.toString());
        for (TimeSetting aTimeSetting : timeList) {

            if(aTimeSetting.getId() != 1 && aTimeSetting.isOn())
            {
                timeSetting = TimeSetting.findById(TimeSetting.class, aTimeSetting.getId());

                startTime = timeSetting.getStartTime();
                Log.e(TAG,"StartTime_getStartTime"+startTime);
                endtime = timeSetting.getEndTime();
                Log.e(TAG,"EndTime_getEndTime"+endtime);
                timeId = timeSetting.getId();
                Log.e(TAG,"TimeID_getTimeID"+timeId);
                break;
            }
        }

    }


    public boolean checkScheduleJob(){

        RealmResults<RepeatDaysModel> repeatDays = getRealmInstance().where(RepeatDaysModel.class).equalTo("id", timeId.toString()).findAll();

        if(repeatDays.size()>0){

            String[] daySplit = repeatDays.get(0).getDays().split(" | ");

            for(int i = 0;i<daySplit.length;i++){

                String day = daySplit[i];

                if(hasTimeSettingForToday(day)){
                    allowCal = false;
                    break;
                }
            }

        }else{
            allowCal = false;
        }

        return allowCal;
    }


    public void timeDifference(){

        Calendar c = Calendar.getInstance();

        final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
       // final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        //SimpleDateFormat endtimedf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String strtTime = df.format(c.getTime()) + " " + startTime;

        String endTime = df.format(c.getTime()) + " " + endtime;

        /*try {
            //Log.e("startDate*******", startDate.toString());
            Log.e("endDate******", endTime);
            int dif = now.compareTo(formatter.parse(endtime));
            Log.e("dif*******", String.valueOf(dif));
            Log.e("date*******", String.valueOf(now));

        } catch (ParseException e) {
            e.printStackTrace();
        }*/


        try {
            Date sDate = formatter.parse(strtTime);
            Date eDate = formatter.parse(endTime);

            if(eDate.getTime() <= sDate.getTime()) {

                Calendar cal = Calendar.getInstance();
                cal.setTime(eDate);
                cal.add(Calendar.DATE,1);

                System.out.println("eDate.getTime() aftr "+formatter.format(cal.getTime())+" get date val ");

                endTime = formatter.format(cal.getTime());


            }else{

                endTime = df.format(c.getTime()) + " " + endtime;

            }

        }catch(Exception e){
            e.printStackTrace();

            endTime = df.format(c.getTime()) + " " + endtime;
        }

        String curretntTime = formatter.format(c.getTime());

        Date startDate = null, endDate = null, currentDate = null;
        try {
            startDate = formatter.parse(strtTime);
            endDate = formatter.parse(endTime);
            currentDate = formatter.parse(curretntTime);

            Log.e("startDate======", startDate.toString());
            Log.e("endDate========", endDate.toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("SAJEN ");

        difference = printDifference(currentDate, endDate);

        startDiff = printDifference(currentDate, startDate);

        diff = difference - 300000;

        Log.e("difference======", String.valueOf(difference));
        Log.e("startDiff========", String.valueOf(startDiff));
        Log.e("diff========", String.valueOf(diff));
    }


    public void scheduleJob(){

//        System.out.println("HANDLER EXECUTED ---------> ");

//        timeDifference();
        Log.e("Schedulejob", "difference======" + difference);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                String str_startTime="",str_endTime="",str_timeId="";
                //Toast.makeText(getApplicationContext(), "check", Toast.LENGTH_SHORT).show();
                //DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
                Realm realm = Realm.getInstance(getApplicationContext());
                RealmResults<SetStatusModel> setStatusModel = realm.where(SetStatusModel.class).findAll();

                // String str_startTime="",str_endTime="",str_timeId="";
                if(setStatusModel.size()>0){

                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");

                      str_startTime = formatter.format(setStatusModel.get(0).getStartTime());
                      str_endTime = formatter.format(setStatusModel.get(0).getEndTime());
                     str_timeId = setStatusModel.get(0).getTimeId();

                    // Log.e("Schedulejob","relamStarttime======"+strDate);
                    Log.e("Schedulejob","relamendtime======"+str_endTime);
                    Log.e("Schedulejob","relamstarttime======"+str_startTime);
                    Log.e("Schedulejob","relamstrtimeid======"+str_timeId);


                }

                Log.e("Schedulejob", "relamendtime======" + str_endTime);
                Date start_date = null;
                Date end_date = null;
                Calendar c = Calendar.getInstance();
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
                String currentdateandtime = formatter.format(c.getTime());
                try {
                    end_date=formatter.parse(str_endTime);
                    // long  count = (end_date == null) ? 0 : Long.parseLong(String.valueOf(end_date));
                    start_date=formatter.parse(currentdateandtime);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
               // Log.e("JJonawaystrenddatemilliseconds--------->>>>", String.valueOf(end_date.getTime()));
                //Log.e("JJonawayscurrentdatedatemilliseconds--------->>>>", String.valueOf(System.currentTimeMillis()));
                //||System.currentTimeMillis() > end_date.getTime()
                // ||System.currentTimeMillis() > str_endTime.getTime()
                //if(currentdateandtime.equals(str_endTime))
                try
                {
                    if(start_date.getTime()>=end_date.getTime())
                    {
                        //Toast.makeText(getApplicationContext(), "currentdateandtime=="+currentdateandtime+"str_endTime=="+str_endTime, Toast.LENGTH_LONG).show();
                        //Log.e()
                        turnOff("0");
                        stopForeground(true);
                        //bl_availablestatus=true;

                        RealmResults<RegisterDeviceModel> deviceValues = getRealmInstance().where(RegisterDeviceModel.class).findAll();

                        if (deviceValues.size() > 0) {

                            Constants.accessToken = deviceValues.get(0).getAccestoken();
                        }

                        setTimeSettings("0");
                        user.goAvailable();
                        stopForeground(true);


                        //startForeground(1, notificationavailable);

                        //Realm realm = Realm.getInstance(getApplicationContext());

                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.clear(SetStatusModel.class);
                                realm.clear(ExtendTimeModel.class);
                            }
                        });

                        realm.close();


                        stopSelf();
                     //handler.removeCallbacksAndMessages(null);
//                  Intent intent = new Intent(getApplicationContext(), SetStatusActivity.class);
//                  startActivity(intent);
                    }
                }catch(Exception e)
                {
                    e.printStackTrace();
                }


                handler.postDelayed(this, 20000);
            }
        }, 20000);

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(getApplicationContext(), "currentdateandtime==",Toast.LENGTH_LONG).show();
                turnOff("0");

                RealmResults<RegisterDeviceModel> deviceValues = getRealmInstance().where(RegisterDeviceModel.class).findAll();

                if (deviceValues.size() > 0) {

                    Constants.accessToken = deviceValues.get(0).getAccestoken();
                }

                setTimeSettings("0");
                user.goAvailable();
                stopForeground(true);
                //startForeground(1, notificationavailable);

                Realm realm = Realm.getInstance(getApplicationContext());

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.clear(SetStatusModel.class);
                        realm.clear(ExtendTimeModel.class);
                    }
                });

                realm.close();


                stopSelf();
//                 Intent intent = new Intent(getApplicationContext(), SetStatusActivity.class);
//                 startActivity(intent);


            }
        }, difference);*/
    }


    public void setTimeSettings(String state){
        Log.e("Schedulejob","Enterintosettimesettings======");

        Realm realm = Realm.getInstance(getApplicationContext());
        RealmResults<SetStatusModel> setStatusModel = realm.where(SetStatusModel.class).findAll();

        String str_startTime="",str_endTime="",str_timeId="";

        if(setStatusModel.size()>0){

            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");

            str_startTime = formatter.format(setStatusModel.get(0).getStartTime());
            str_endTime = formatter.format(setStatusModel.get(0).getEndTime());
            str_timeId = setStatusModel.get(0).getTimeId();

            /*Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
            String strDate = sdf.format(c.getTime());
*/
           // Log.e("Schedulejob","relamStarttime======"+strDate);
            Log.e("Schedulejob","relamendtime======"+str_endTime);


        }

        realm.close();


        Map<String, String> params = new HashMap<>();

        params.put("DetachUserStatusID", "");
        params.put("StartTime", str_startTime);
        params.put("AccessToken", Constants.accessToken);
        params.put("EndTime", str_endTime);
        params.put("TimeID", str_timeId);
        params.put("Status", user.getUserStatus().getAvailabilityStatus());
        params.put("IsActive", state);


        new GetJSONResponse(this).RequestJsonToServer(this, "DetachStatus", params, new VolleyInterface() {

            @Override
            public void onSuccessResponse(JSONObject result) {

                Log.e(result.toString(), "Menu Success state for update status:----");

            }

            @Override
            public void onErroeResponse(VolleyError result) {
                System.out.println("Menu Error state for update status:----" + result + "");
            }

            @Override
            public void onSuccessResponse(JSONArray result) {

            }
        });

    }


    public boolean hasTimeSettingForToday(String day) {

        String dayName= TimeSetting.Days.values()[this.getTodaysOrdinal()].toString();

        return dayName.startsWith(day);
    }

    private int getTodaysOrdinal() {

//        Log.d(TAG, "Toadays ordinal:" + this.convertCalendarToOrdinal(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)));
        return (this.convertCalendarToOrdinal(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)));

    }


    private int convertOrdinalToCalendar(int anOrdinal) {
        return anOrdinal + 1;
    }

    private int convertCalendarToOrdinal(int aCalendarDay) {
        return aCalendarDay - 1;

    }

    public void alertWindow(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Dettach");
        builder.setIcon(R.drawable.logo1);
        builder.setMessage("Your Time is about to expire and you will become available in 5 minutes");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
//                stopSelf();

//                turnOff();
//
//                user.goAvailable();
//                updateDashboardAvailabilityStatus();

            }
        });
        builder.setNegativeButton("Extend Time", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent extendTim  = new Intent(JJOnAwayService.this,UpdateTimeActivity.class);
                extendTim.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(extendTim);
            }
        });
        AlertDialog alert = builder.create();
        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alert.show();
    }

    public void registerCSLonUnavailable() {

        tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        this.cslOnUnavailble = new CSLonUnavailable();
        tm.listen(this.cslOnUnavailble, PhoneStateListener.LISTEN_CALL_STATE);
        Log.d(TAG, "CSLonUnavailable registered");

    }

    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    public long printDifference(Date startDate, Date endDate) {

        //milliseconds
//        long different = endDate.getTime() - startDate.getTime();

//        System.out.println("startDate JJONAWAY: " + startDate + " startDate.getTime() "+startDate.getTime());
//        System.out.println("endDate JJONAWAY: " + endDate+" endDate.getTime() "+endDate.getTime());
//        System.out.println("different : " + different);

//        strt milli = 1478582880000;1478583000000
//        end milli = 1478586660000;


        long diff = endDate.getTime() - startDate.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
       /* long seconds = (int) (diff / 1000) % 60 ;
        long minutes = (int) ((diff / (1000*60)) % 60);
        long hours   = (int) ((diff / (1000*60*60)) % 24);*/

        Log.e(TAG, "endDate.getTime()=="+endDate.getTime());
        Log.e(TAG, "startDate.getTime()"+startDate.getTime());
        Log.e(TAG, "seconds"+seconds);
        Log.e(TAG, "minutes"+minutes);
        Log.e(TAG, "hours"+hours);
        //Log.e(TAG, "days"+days);

        return TimeUnit.MINUTES.toMillis(minutes);

    }

    public void registerCSLonAvailable() {

        tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        this.cslOnAvailble = new CSLonAvailable();
        tm.listen(this.cslOnAvailble, PhoneStateListener.LISTEN_CALL_STATE);
        Log.e(TAG, "CSLonAvailable registered");

    }

    public void unregisterCSLonAvailable() {

        tm.listen(this.cslOnAvailble, PhoneStateListener.LISTEN_NONE);
        Log.e(TAG, "CSLonAvailable unregistered");

    }

    public void unregisterCSLonUnavailable() {

        tm.listen(this.cslOnUnavailble, PhoneStateListener.LISTEN_NONE);
        Log.e(TAG, "CSLonUnavailable unregistered");

    }


    /*
     * Service should implement this to clean up any resources such as threads,
     * registered listeners, receivers, etc. This is the last call the service
     * receives. Called when service is no longer in use.
     *
     *
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        // unregister our receivers

        if(this.user.getUserStatus().getAvailabilityStatus().compareTo("AVAILABLE")==0){

            this.unregisterCSLonUnavailable();
            this.registerCSLonAvailable();

            // restore phone's ring volume
            this.cm.silenceRinger(false);

            //deliver text messages that were missed
            this.cm.getRecentMissedMessageLog().deliverRecentMissedMessagesToInbox();

            //set user's availability status to available
            user.goAvailable();
            this.updateDashboardAvailabilityStatus();

        }

        stopForeground(true);


        Log.e(TAG, "JJOnAwayService Destroyed.");

        // unregister our callStateListeners



    }

    public void turnOff(String val){
        List<TimeSetting> timeList = TimeSetting.listAll(TimeSetting.class);

        for (TimeSetting aTimeSetting : timeList) {

            if(aTimeSetting.getId() != 1 && aTimeSetting.isOn())
            {
                TimeSetting aTimeSettings = TimeSetting.findById(TimeSetting.class, aTimeSetting.getId());
                aTimeSettings.off();
                aTimeSettings.save();


            }
        }
    }

    private void updateDashboardAvailabilityStatus()
    {
        Intent intent = new Intent(DashboardFragment.DASHBOARD_INTENT);
        intent.putExtra("status", this.user.getUserStatus().getAvailabilityStatus());
        this.sendBroadcast(intent);
        Log.d(TAG, "Dashboard Notification sent to update availability status!");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
