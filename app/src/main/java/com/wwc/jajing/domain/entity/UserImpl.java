package com.wwc.jajing.domain.entity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;
import com.wwc.jajing.activities.AboutToBreakThrough;
import com.wwc.jajing.activities.DashboardActivity;
import com.wwc.jajing.domain.value.AvailabilityTime;
import com.wwc.jajing.domain.value.PhoneNumber;
import com.wwc.jajing.domain.value.UserStatus;
import com.wwc.jajing.permissions.PermissionManager;
import com.wwc.jajing.permissions.PermissionManager.Permissions;
import com.wwc.jajing.realmDB.CallsModel;
import com.wwc.jajing.realmDB.ContactModel;
import com.wwc.jajing.realmDB.RepeatDaysModel;
import com.wwc.jajing.services.JJOnAwayService;
import com.wwc.jajing.settings.time.TimeSettingTaskManager;
import com.wwc.jajing.system.JJApplication;
import com.wwc.jajing.system.JJSystemImpl;
import com.wwc.jajing.system.JJSystemImpl.Services;

import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.wwc.jajing.system.JJApplication.getRealmInstance;

/*
 * Represents the User.
 *
 * Singleton. DO NOT  NEW THIS CLASS UP...
 * you can retreive the instance thorugh the system's registry.
 *
 *
 */

public class UserImpl extends SugarRecord implements User {

    private static final String TAG = "UserImpl";
    private Long id;
    private String availabilityStatus = AVAILAILBLE;
    private String availabilityTime;

    @Ignore
    private mEndTaskTimerTask currentEndTask;

    @Ignore
    private boolean isMakingCall;

    @Ignore
    private AvailabilityTime availTime;

    @Ignore
    public static final String AVAILAILBLE = "AVAILABLE";

    @Ignore
    private Context context;

    String daysRepeat="";

    AlarmManager alarmManager;

    @Ignore
    private static final UserImpl instance = new UserImpl(
            (Context) JJSystemImpl.getInstance().getSystemService(
                    Services.CONTEXT));

    @Ignore
    private PermissionManager pm = null;

    // constructor left public because ORM requires this to be public
    public UserImpl() {

    }

    // constructor left public because ORM requires this to be public
    public UserImpl(Context context) {
      //  super(context);
        this.context = context;
        System.out.println("this.context in constructor "+this.context);

    }

    public static UserImpl getInstance() {
        return instance;
    }

    public Long getId() {
        return this.id;
    }

    @Override
    public void makeCall(PhoneNumber phoneNumber) {
        this.setIsMakingCall(true);
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callIntent.setData(Uri.parse("tel:" + phoneNumber.toString()));

        System.out.println("callIntent = " + this.context);

        if(this.context==null){
            this.context = new JJApplication();
        }

        System.out.println("callIntent after = " + new JJApplication());

        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        context.startActivity(callIntent);
    }

    public boolean isMakingCall() {
        return this.isMakingCall;
    }

    public void setIsMakingCall(boolean isMakingCall) {
        this.isMakingCall = isMakingCall;
        Log.d(TAG, "User is now making a new call. Field:ismakingcall was set to:" + isMakingCall);
    }


    @Override
    public boolean goUnavailable(String aReason, String aStartTime, AvailabilityTime anAvailabilityTime) {
        this.setAvailabilityStatus(aReason);
        this.setAvailabilityTime(anAvailabilityTime);
        this.save();

        int dayOfTheWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        String start = TimeSetting.dateFormatterTIME.format(TimeSetting.getDateOfTimeString(aStartTime));
        if (!TimeSetting.isValidTimeInterval(start, anAvailabilityTime.getAvailabilityTimeString())) { //check to make sure its a valid interval
            //Toast.makeText(context, "invalid interval", Toast.LENGTH_SHORT).show();
            return false;
        }

//        if (TimeSetting.isSartTimeInFuture(start)) {
////            Toast.makeText(context, "You status will start in future.", Toast.LENGTH_SHORT).show();
//            TimeSetting mainTimeSetting = TimeSetting.findById(TimeSetting.class,
//                    1);
//            mainTimeSetting.setStartTime(start);
//            mainTimeSetting.setEndTime(anAvailabilityTime.getAvailabilityTimeString());
//            Log.d(TAG, "setting start time:" + start);
//            Log.d(TAG, "setting end time:" + anAvailabilityTime.getAvailabilityTimeString());
//            mainTimeSetting.save();
//            TimeSettingTaskManager taskManager = TimeSettingTaskManager.getInstance();
//            taskManager.scheduelNewStartTaskForTimeSettingInFutureLaterToday(mainTimeSetting);
//            return false;
//        }

//        this.startJJOnAwayService();

        TimeSetting mainTimeSetting = TimeSetting.findById(TimeSetting.class,
                1);
        mainTimeSetting.setStartTime(start);
        mainTimeSetting.setEndTime(anAvailabilityTime.getAvailabilityTimeString());
        mainTimeSetting.setStatus(aReason);
//        Log.d(TAG, "setting start time:" + start);
//        Log.d(TAG, "setting end time:" + anAvailabilityTime.getAvailabilityTimeString());
        mainTimeSetting.save();


        TimeSettingTaskManager taskManager = TimeSettingTaskManager.getInstance();
        taskManager.turnTimeSettingOn(1);
        return true;

    }


    @Override
    public boolean goUnavailable(String aReason, String aStartTime, AvailabilityTime anAvailabilityTime,Long id) {

        this.setAvailabilityStatus(aReason);
        this.setAvailabilityTime(anAvailabilityTime);
        this.save();

        int dayOfTheWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        String start = TimeSetting.dateFormatterTIME.format(TimeSetting.getDateOfTimeString(aStartTime));
        if (!TimeSetting.isValidTimeInterval(start, anAvailabilityTime.getAvailabilityTimeString())) { //check to make sure its a valid interval
            //Toast.makeText(context, "invalid interval", Toast.LENGTH_SHORT).show();
            return false;
        }

        TimeSetting mainTimeSetting = TimeSetting.findById(TimeSetting.class,id);
        mainTimeSetting.setStartTime(start);
        mainTimeSetting.setEndTime(anAvailabilityTime.getAvailabilityTimeString());
        mainTimeSetting.setStatus(aReason);
//        Log.d(TAG, "setting start time:" + start);
//        Log.d(TAG, "setting end time:" + anAvailabilityTime.getAvailabilityTimeString());
        mainTimeSetting.save();


        TimeSettingTaskManager taskManager = TimeSettingTaskManager.getInstance();
        taskManager.turnTimeSettingOn(id);
        return true;

    }



    @Override
    public boolean goUnavailable(String aReason, String aStartTime, AvailabilityTime anAvailabilityTime, TimeSetting.Days[] days, final Long id, final String repeatFlag) {

        this.setAvailabilityStatus(aReason);
        this.setAvailabilityTime(anAvailabilityTime);
        this.save();

        String format = "";

        int dayOfTheWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        String start = TimeSetting.dateFormatterTIME.format(TimeSetting.getDateOfTimeString(aStartTime));
        if (!TimeSetting.isValidTimeInterval(start, anAvailabilityTime.getAvailabilityTimeString())) { //check to make sure its a valid interval
            //Toast.makeText(context, "invalid interval", Toast.LENGTH_SHORT).show();
            return false;
        }

        TimeSetting mainTimeSetting = TimeSetting.findById(TimeSetting.class,id);
        mainTimeSetting.setStartTime(start);
        mainTimeSetting.setEndTime(anAvailabilityTime.getAvailabilityTimeString());
        mainTimeSetting.setStatus(aReason);

        daysRepeat= "";

        if(days != null) {

            for (TimeSetting.Days aDay : days) {

                int val = TimeSetting.Days.valueOf(aDay.toString()).ordinal();

//                setAlarmToCheckUpdates(val,start);

                String temp = (aDay.getAbbrev() + " | ");
                daysRepeat += temp;
            }

        }

//        System.out.println("daysRepeat = " + daysRepeat);

        final RealmResults<RepeatDaysModel> repeatDays = getRealmInstance().where(RepeatDaysModel.class).equalTo("id", id.toString()).findAll();

        if (repeatDays.size() == 0) {

            getRealmInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RepeatDaysModel repeatModel = realm.createObject(RepeatDaysModel.class); // Create managed objects directly
                    if (daysRepeat != null) {
                        repeatModel.setId(id.toString());
                        repeatModel.setDays(daysRepeat);
                        repeatModel.setIdFlag(repeatFlag);
                        repeatModel.setAvailable("1");
                    }

                }
            });

            RealmResults<RepeatDaysModel> daysList = getRealmInstance().where(RepeatDaysModel.class).findAll();
//            System.out.println("call history if = " + daysList);

        } else {

            getRealmInstance().beginTransaction();
            repeatDays.get(0).setDays(daysRepeat);
            repeatDays.get(0).setIdFlag(repeatFlag);
            getRealmInstance().commitTransaction();

            RealmResults<RepeatDaysModel> daysList = getRealmInstance().where(RepeatDaysModel.class).findAll();
//            System.out.println("call history else = " + daysList);
        }

//        Log.d(TAG, "setting start time USER IMPL :" + start);
//        Log.d(TAG, "setting end time:" + anAvailabilityTime.getAvailabilityTimeString());
        mainTimeSetting.save();


        TimeSettingTaskManager taskManager = TimeSettingTaskManager.getInstance();
        taskManager.turnTimeSettingOn(id);


        alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);

        Intent cancelServiceIntent = new Intent(context, JJOnAwayService.class);
        PendingIntent cancelServicePendingIntent = PendingIntent.getBroadcast( context, 0, cancelServiceIntent, PendingIntent.FLAG_CANCEL_CURRENT );
        alarmManager.cancel(cancelServicePendingIntent);


        Calendar calendar = Calendar.getInstance();

        String[] time = start.split(":");
        int hr = Integer.parseInt(time[0]);

        String[]min = time[1].split(" ");
        int mn = Integer.parseInt(min[0]);

        format = min[1];

        if(format!=null && format.compareTo("AM")!=0){
            hr = hr+12;
        }



        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hr);
        calendar.set(Calendar.MINUTE,mn);
        calendar.set(Calendar.SECOND,00);

        Intent myIntent = new Intent(context, JJOnAwayService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, myIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        this.alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);



        return true;

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



    /*
     * Time setting activity, will go unavailable immediately and be set to return in the future
     * The start time is now.
     *
     */
    public void goUnavailable(String aReason, AvailabilityTime aTimeWillBeBack) {
        this.setAvailabilityStatus(aReason);
        this.setAvailabilityTime(aTimeWillBeBack);
        this.save();

        int dayOfTheWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        String now = TimeSetting.dateFormatterTIME.format(TimeSetting.getTimeNow());

        if (!TimeSetting.isValidTimeInterval(now, aTimeWillBeBack.getAvailabilityTimeString()))
            return;

//        this.startJJOnAwayService();

        //schdule a new task end task to run, for jajing to stop
        Long nowMilli = TimeSetting.getTimeNow().getTime();
        Long endMilli = aTimeWillBeBack.getTime();
        Log.d(TAG, "endMilli" + endMilli);
        Long end = endMilli - nowMilli;


        //keep a reference to this task just in case users changes his availability time
        mEndTaskTimerTask pendingTask = new mEndTaskTimerTask();
        if (this.currentEndTask != null) {
            //cancel current task
            this.currentEndTask.cancel();
            Log.d(TAG, "current end task cancelled");

        }
        this.currentEndTask = pendingTask;
        TimeSettingTaskManager.getInstance().aTimer.schedule(this.currentEndTask, end);

    }

    private class mEndTaskTimerTask extends TimerTask {
        private volatile Looper mMyLooper;

        @Override
        public void run() {


        }

    }


    private void setAvailabilityTime(AvailabilityTime anAvailabilityTime) {
        this.availTime = anAvailabilityTime;
        this.availabilityTime = anAvailabilityTime.getAvailabilityTimeString();
//        Log.d(TAG, "availability time is now: " + this.availabilityTime);
    }

    private void startJJOnAwayService() {


        System.out.println("TAG = " + TAG);

        Intent service = new Intent(context, JJOnAwayService.class);
//        context.startService(service);
    }

    /*
     *
     *
     */
    @Override
    public void goAvailable() {
        this.setAvailabilityStatus(this.AVAILAILBLE);

//        alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
//
//        Intent cancelServiceIntent = new Intent(context, JJOnAwayService.class);
//        PendingIntent cancelServicePendingIntent = PendingIntent.getBroadcast( context, 0, cancelServiceIntent, PendingIntent.FLAG_CANCEL_CURRENT );
//        alarmManager.cancel(cancelServicePendingIntent);

        //TODO - may cause bug issue here...
        this.availabilityTime = "";
        this.save();
        this.stopJJOnAwayService();


        if (TimeSetting.findById(TimeSetting.class, 1L) != null) {
            TimeSettingTaskManager.getInstance().turnTimeSettingOff(1L);

        }

    }


    private void stopJJOnAwayService() {

      // stopService(new Intent(JJOnAwayService.MY_SERVICE));

        if(context!=null) {
            Intent intent = new Intent(context, JJOnAwayService.class);
            context.stopService(intent);
        }

    }


    public UserStatus getUserStatus() {

//        System.out.println("this.availTime = " + this.availTime);
//        System.out.println("this.availabilityTime = " + this.availabilityTime);
        
        if (this.availTime != null) {
            UserStatus us = new UserStatus(availabilityStatus, this.availTime.getAvailabilityTimeString());
            return us;
        } else {
            //FIXED BUG, app would not start if user status was null
//            UserStatus us = new UserStatus(availabilityStatus, "Unknown");
            UserStatus us = new UserStatus(availabilityStatus, this.availabilityTime);
            return us;

        }

    }

    public void setAvailabilityStatus(String status) {
        this.availabilityStatus = status;
        this.save();

    }


    @Override
    public boolean isAvailable() {
        if (this.availabilityStatus == null) {
            throw new IllegalStateException("Availability status cannot be null!");
        }

        if (this.availabilityStatus.equals(this.AVAILAILBLE))
            return true;
        else
            return false;
    }

    /*
     * Delegates to Permission Manager
     *
     */
    @Override
    public void givePermission(Caller aPermissable,
                               Permissions aPermission) {
        if (aPermissable == null)
            throw new IllegalArgumentException("Caller cannot be null. Please make sure caller is persisted before giving permissions to him.");
        PermissionManager pm = this.getPermissionManager();
        pm.attachPermission(aPermissable, pm.getPermission(aPermission), true);

//        Log.d(TAG, "Permission has be granted!" + aPermission.toString());

    }

    /*
     * Delegates to Permission Manager
     *
     *
     */
    @Override
    public void denyPermission(Caller aPermissable,
                               Permissions aPermission) {
        if (aPermissable == null)
            throw new IllegalArgumentException("Caller cannot be null. Please make sure caller is persisted before removing permissions from him.");
        PermissionManager pm = this.getPermissionManager();
        pm.attachPermission(aPermissable, pm.getPermission(aPermission), false);
        Log.d(TAG, "Permission has be revoked!" + aPermission.toString());


    }

    private PermissionManager getPermissionManager() //lazy load permission manager
    {
        if (this.pm == null) {
            this.pm = PermissionManager.getInstance();
            return this.pm;
        }
        return this.pm;
    }

    private boolean isJJOnAwayServiceRunning() {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (JJOnAwayService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public AvailabilityTime getAvailabilityTime() {
        if (this.availabilityTime == null || this.availabilityTime.equalsIgnoreCase("")) {
            int dayOfTheWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            //Days today = Days.values()[dayOfTheWeek - 1];
            Long nowMilli = TimeSetting.getTimeNow().getTime();
            Long pastMilli = nowMilli - 5000;
            Date pastDate = new Date();
            pastDate.setTime(pastMilli);

            String now = TimeSetting.dateFormatterTIME.format(pastDate);
            this.availTime = new AvailabilityTime(now);
            Log.d(TAG, this.availTime.getAvailabilityTimeString());
            Log.d(TAG, "availabiity time was not set, setting default availability time...");
            return this.availTime;
        }
        Log.d(TAG, "availability Time is :" + this.availabilityTime);
        AvailabilityTime anAvailabilityTime = new AvailabilityTime(this.availabilityTime);
        this.setAvailabilityTime(anAvailabilityTime);
        return this.availTime;
    }


}
