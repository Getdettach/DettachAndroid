package com.wwc.jajing.services;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
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

import com.wwc.R;
import com.wwc.jajing.activities.TimeSettingActivity;
import com.wwc.jajing.activities.UpdateTimeActivity;
import com.wwc.jajing.domain.entity.TimeSetting;
import com.wwc.jajing.domain.entity.User;
import com.wwc.jajing.domain.services.CallManager;
import com.wwc.jajing.domain.value.AvailabilityTime;
import com.wwc.jajing.fragment.DashboardFragment;
import com.wwc.jajing.listeners.CSLonAvailable;
import com.wwc.jajing.listeners.CSLonUnavailable;
import com.wwc.jajing.realmDB.RepeatDaysModel;
import com.wwc.jajing.settings.time.TimeSettingTaskManager;
import com.wwc.jajing.system.JJSystem;
import com.wwc.jajing.system.JJSystemImpl;
import com.wwc.jajing.system.JJSystemImpl.Services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
public class JJOnAwayServiceBKP extends Service {


    private static final String TAG = "JJOnAwayService";
    private TelephonyManager tm;
    private CallManager cm;

    private PhoneStateListener cslOnUnavailble;
    private PhoneStateListener cslOnAvailble;

    private User user;

    long difference,startDiff,diff;

    Notification notification;

    Long timeId=0L;

    TimeSetting timeSetting;

    String startTime,endtime,status;

    boolean allowCal = true;

    public JJOnAwayServiceBKP() {
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

        System.out.println("ONSTART");

        List<TimeSetting> timeList = TimeSetting.listAll(TimeSetting.class);

        for (TimeSetting aTimeSetting : timeList) {

            if(aTimeSetting.getId() != 1 && aTimeSetting.isOn())
            {
                timeSetting = TimeSetting.findById(TimeSetting.class, aTimeSetting.getId());

                startTime = timeSetting.getStartTime();
                endtime = timeSetting.getEndTime();
                timeId = timeSetting.getId();

                break;
            }
        }

        timeDifference();

        this.startAwayForegruondService();
        // We want this service to stop running if it gets shut down b/c of memory problems
        return START_NOT_STICKY;
    }

    @SuppressLint("NewApi")
    private void startAwayForegruondService() {
        notification = new Notification.Builder(this).setContentTitle("Dettach").setContentText("You will be Unavailable to reach")
                .setSmallIcon(R.drawable.logo).build();

        System.out.println("startDiff = " + startDiff);

        if(!checkScheduleJob()) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    startForeground(1, notification);

                    startServiceWork();

                    System.out.println("Handler Ex in start");

                }
            }, startDiff);
        }
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

    }


    public void startServiceWork(){

        if(!checkScheduleJob()){

            System.out.println("JOB SCHEDULED TODAY");

            if (this.user.isAvailable())
                this.user.goUnavailable(this.user.getUserStatus().getAvailabilityStatus(), this.user.getAvailabilityTime());

            scheduleJob();

        }else{
            System.out.println("JOB NOT SCHEDULED TODAY");
        }


        this.registerCSLonUnavailable();
        this.unregisterCSLonAvailable();

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
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String strtTime = df.format(c.getTime()) + " " + startTime;
        String endTime = df.format(c.getTime()) + " " + endtime;

        String curretntTime = formatter.format(c.getTime());

        Log.v("strtTime", strtTime);
        Log.v("endTime", endTime);
        Date startDate = null, endDate = null, currentDate = null;
        try {
            startDate = formatter.parse(strtTime);
            endDate = formatter.parse(endTime);
            currentDate = formatter.parse(curretntTime);

            Log.v("startDate", startDate.toString());
            Log.v("endDate", endDate.toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.v("Differnsee", "" + printDifference(currentDate, endDate));

        Log.v("START Differnsee", "" + printDifference(currentDate, startDate));

        difference = printDifference(currentDate, endDate);

        startDiff = printDifference(currentDate, startDate);

        diff = difference - 300000;
    }


    public void scheduleJob(){

        System.out.println("HANDLER EXECUTED ---------> ");

//        timeDifference();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                RealmResults<RepeatDaysModel> daysList = getRealmInstance().where(RepeatDaysModel.class).findAll();
                System.out.println("daysList  = " + daysList);

                RealmResults<RepeatDaysModel> repeatDays = getRealmInstance().where(RepeatDaysModel.class).equalTo("id", timeId.toString()).findAll();
                System.out.println("repeatDays ID = " + repeatDays);

                if (repeatDays.size() > 0 && (repeatDays.get(0).getIdFlag().compareTo("0") != 0)) {

                    if ((repeatDays.get(0).getDays() == null || repeatDays.get(0).getDays().compareTo("") == 0) && repeatDays.get(0).getIdFlag().compareTo("2") == 0) {

                        System.out.println("IF IN IF");

                        turnOff("0");
                        user.goAvailable();
                        stopSelf();

                    } else {

                        System.out.println("ELSE IF");

                        stopSelf();
//                        stopForeground(true);
                    }

                } else {

                    System.out.println("ELSE");

                    turnOff("0");
                    user.goAvailable();
                    stopSelf();
                }

//                System.out.println("difference = " + difference);

            }
        }, difference);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                AlertDialog alert = new AlertDialog.Builder(getApplicationContext()).create();

                if (alert.isShowing()) {
                    alert.dismiss();
                }

                alertWindow();

//                System.out.println("difference = " + difference);

            }
        }, diff);

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
        builder.setIcon(R.drawable.logo);
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
                Intent extendTim  = new Intent(JJOnAwayServiceBKP.this,UpdateTimeActivity.class);
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
        long different = endDate.getTime() - startDate.getTime();

//        System.out.println("startDate : " + startDate);
//        System.out.println("endDate : " + endDate);
//        System.out.println("different : " + different);

        long diff = endDate.getTime() - startDate.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        return TimeUnit.MINUTES.toMillis(minutes);

    }

    public void registerCSLonAvailable() {

        tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        this.cslOnAvailble = new CSLonAvailable();
        tm.listen(this.cslOnAvailble, PhoneStateListener.LISTEN_CALL_STATE);
        Log.d(TAG, "CSLonAvailable registered");

    }

    public void unregisterCSLonAvailable() {

        tm.listen(this.cslOnAvailble, PhoneStateListener.LISTEN_NONE);
        Log.d(TAG, "CSLonAvailable unregistered");

    }

    public void unregisterCSLonUnavailable() {

        tm.listen(this.cslOnUnavailble, PhoneStateListener.LISTEN_NONE);
        Log.d(TAG, "CSLonUnavailable unregistered");

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


        Log.d(TAG, "JJOnAwayService Destroyed.");

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

    private void updateDashboardAvailabilityStatus() {
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
