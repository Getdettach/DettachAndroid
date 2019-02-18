package com.wwc.jajing.settings.time;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.wwc.jajing.domain.entity.TimeSetting;
import com.wwc.jajing.domain.entity.User;
import com.wwc.jajing.domain.value.AvailabilityTime;
import com.wwc.jajing.realmDB.RepeatDaysModel;
import com.wwc.jajing.realmDB.TimeModel;
import com.wwc.jajing.services.JJOnAwayService;
import com.wwc.jajing.system.JJSystem;
import com.wwc.jajing.system.JJSystemImpl;
import com.wwc.jajing.system.JJSystemImpl.Services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.wwc.jajing.system.JJApplication.getRealmInstance;

/*
 * responsible for managing all time setting tasks that will occur or have ended.
 * 
 * will be notified initially of day chnage on system boot
 * 
 * 
 */
public class TimeSettingTaskManager implements onDayChangeListner {

    private static final String TAG = "TimeSettingTaskManager";

    // we need to keep a collection of the tasks we have issued on particular
    // time settings
    // so we can cancel them before they get run if we want to
    private final HashMap<Long, TimerTask> timeSettingStartTasks = new HashMap<Long, TimerTask>();
    private final HashMap<Long, TimerTask> timeSettingEndTasks = new HashMap<Long, TimerTask>();

    private final HashMap<Long, TimeSetting> listOfTimeSettingsThatAreOn = new HashMap<Long, TimeSetting>();

    private static TimeSettingTaskManager instance;

    public final Timer aTimer = new Timer();

    private Context context;
    private User user;

    private TimeSettingTaskManager() {
        //CACHE SYSTEM
        JJSystem jjSystem = JJSystemImpl.getInstance();

        user = (User) jjSystem.getSystemService(Services.USER);
        context = (Context) jjSystem.getSystemService(Services.CONTEXT);
    }

    public Long getTimeSettingIdClosestToBeingDone() {
        TimeSetting result = null;
        for (TimeSetting aTimeSetting : listOfTimeSettingsThatAreOn.values()) {
            if (result == null) {
                result = aTimeSetting;
            }
            Long endTime = aTimeSetting.getEndTimeAsDate().getTime();
            if (endTime <= result.getEndTimeAsDate().getTime()) {
                result = aTimeSetting;
            }

        }
        if (result == null) return 1L;
        return result.getId();
    }


    public void turnTimeSettingOn(long timeSettingId) {
        // we need to turn the time setting on

        TimeSetting aTimeSetting=null;

        if(timeSettingId == 1){
            aTimeSetting = new TimeSetting();
            aTimeSetting.findById(TimeSetting.class,timeSettingId);
        }else{
            aTimeSetting = aTimeSetting.findById(TimeSetting.class,timeSettingId);
        }

        aTimeSetting.on();
        aTimeSetting.save();

        // now we need to register this time setting as ON with our task mananger
        this.registerTimeSettingAsOn(aTimeSetting);
        // assign time setting a task to execute
        this.assignTaskToTimeSetting(aTimeSetting);
    }

    public void turnTimeSettingOff(final Long timeSettingId) {
        // we need to turn the time setting off

        TimeSetting aTimeSetting = new TimeSetting();
        aTimeSetting.findById(TimeSetting.class, timeSettingId);
        aTimeSetting.off();
        aTimeSetting.save();

        // lets cancel time setting


    }

    private void cancelTimeSetting(TimeModel aTimeSetting) {
        this.cancelStartTask(aTimeSetting);
        this.cancelEndTask(aTimeSetting);
        aTimer.purge();
    }

    public static final TimeSettingTaskManager getInstance() {
        if (instance == null) {
            instance = new TimeSettingTaskManager();
            return instance;
        }
        return instance;
    }

    private void cancelStartTask(TimeModel aTimeSetting) {
        if (this.timeSettingStartTasks.get(aTimeSetting.getStartTime()) != null) {
            this.timeSettingStartTasks.get(aTimeSetting.getStartTime()).cancel();
            this.timeSettingStartTasks.remove(aTimeSetting.getStartTime());

            Log.d(TAG,
                    "removed start task from time setting tasks. Size is now:"
                            + this.timeSettingStartTasks.size());
        } else {
            Log.d(TAG,
                    "could not remove start task from time setting tasks. Size is now:"
                            + this.timeSettingStartTasks.size());

        }
    }

    private void cancelEndTask(TimeModel aTimeSetting) {
        if (this.timeSettingEndTasks.get(aTimeSetting.getEndTime()) != null) {
            this.timeSettingEndTasks.get(aTimeSetting.getEndTime()).cancel();
            //this.timeSettingEndTasks.remove(aTimeSetting.getId());

            Log.d(TAG, "removed end task from time setting tasks. Size is now:"
                    + this.timeSettingEndTasks.size());
        } else {
            Log.d(TAG,
                    "could not remove end task from time setting tasks. Size is now:"
                            + this.timeSettingEndTasks.size());

        }
    }

    private void registerTimeSettingAsOn(TimeSetting aTimeSetting) {

        this.listOfTimeSettingsThatAreOn
                .put(aTimeSetting.getId(), aTimeSetting);
        Log.d(TAG, "registering time settingId:" + aTimeSetting.getId());

    }

    private void unregisterTimeSettingAsOn(TimeModel aTimeSetting) {
        this.listOfTimeSettingsThatAreOn.remove(aTimeSetting.getStartTime());
//        Log.d(TAG, "unregistering time settingId:" + aTimeSetting.getId());

    }

    /*
     * Will be called initially on boot
     */
    @Override
    public void onDayChange() {

        System.out.println("DAY CHANGED ");

        // update time setting "ON" list
//        this.updateListOfTimeSettingsThatAreOn();
//
//        this.assignTaskToday();
//
//        // when the day changes loop through the list of time settings and
//        // figure out which ones apply today
//        List<TimeSetting> listOfTimeSettingThatApplyToday = this
//                .getListOfTimeSettingsThatApplyToday();
//        // the time settings that apply today should be assignd the task of
//        // turning jajjing on/off at appropriate times
//        this.assignTaskToEachTimeSetting(listOfTimeSettingThatApplyToday);
//
//        Log.d(TAG,
//                "onDayChange for TimeSettingTaskManager is finished running ");

    }


    private void assignTaskToday(){

        System.out.println("DAY CHANGED ---> ");

//        boolean allowCal =true;
//
//        String startTime = "",endtime="";
//        Long timeId=0L;
//
//        RealmResults<RepeatDaysModel> repeatDays = getRealmInstance().where(RepeatDaysModel.class).equalTo("id", timeId.toString()).findAll();
//
//        if(repeatDays.size()>0){
//
//            String[] daySplit = repeatDays.get(0).getDays().split(" | ");
//
//            for(int i = 0;i<daySplit.length;i++){
//
//                String day = daySplit[i];
//
//                if(hasTimeSettingForToday(day)){
//                    allowCal = false;
//                    break;
//                }
//            }
//
//        }
//
//        List<TimeSetting> timeList = TimeSetting.listAll(TimeSetting.class);
//
//        for (TimeSetting aTimeSetting : timeList) {
//
//            if(aTimeSetting.getId() != 1 && aTimeSetting.isOn())
//            {
//                TimeSetting timeSetting = TimeSetting.findById(TimeSetting.class, aTimeSetting.getId());
//
//                startTime = timeSetting.getStartTime();
//                endtime = timeSetting.getEndTime();
//                timeId = timeSetting.getId();
//
//                break;
//            }
//        }
//
//        if(!allowCal){
//
//            Calendar c = Calendar.getInstance();
//
//            final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
//            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//            String strtTime = df.format(c.getTime()) + " " + startTime;
//            String endTime = df.format(c.getTime()) + " " + endtime;
//
//            String curretntTime = formatter.format(c.getTime());
//
//            Log.v("strtTime", strtTime);
//            Log.v("endTime", endTime);
//            Date startDate = null, endDate = null, currentDate = null;
//            try {
//                startDate = formatter.parse(strtTime);
//                endDate = formatter.parse(endTime);
//                currentDate = formatter.parse(curretntTime);
//
//                Log.v("startDate", startDate.toString());
//                Log.v("endDate", endDate.toString());
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            Log.v("Differnsee", "" + printDifference(currentDate, endDate));
//
//            long difference = printDifference(currentDate, startDate);
//
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    context.stopService(new Intent(context,JJOnAwayService.class));
//
//                    context.startService(new Intent(context, JJOnAwayService.class));
//
////                System.out.println("difference = " + difference);
//
//                }
//            }, difference);
//
//        }
    }

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



    private void assignTaskToEachTimeSetting(
            List<TimeSetting> aListOfTimeSettingsThatApplyToday) {
        for (TimeSetting aTimeSetting : aListOfTimeSettingsThatApplyToday) {

            this.assignTaskToTimeSetting(aTimeSetting);
        }
    }

    public void assignTaskToTimeSetting(TimeSetting aTimeSetting) {
        if (aTimeSetting.hasTimeSettingForToday()) {
            // check if time setting's end time has already passed
            if (!aTimeSetting.hasEndTimePassed()) {
                // check to see if start time has passed
                if (aTimeSetting.hasStartTimePassed()) {// do this when the time
                    // setting should be
                    // applied immediately
                    // we need the user to go unavailable immediately
                    String status = this.user.getUserStatus().getAvailabilityStatus();
                    if (status.equalsIgnoreCase("AVAILABLE")) status = "UNAVAILABLE";
                    this.user.goUnavailable(status, new AvailabilityTime(
                            aTimeSetting.getEndTime()));
                    // then we need to commit a new start task immediately
                    this.scheduelNewStartTaskToRunNow(aTimeSetting);
                    Log.d(TAG,
                            "start has passed, but end time hasn't....scheduling a start task to run now.");

                } else {// do this, when time setting will apply in the future
                    // today
                    this.scheduelNewStartTaskForTimeSettingInFutureLaterToday(aTimeSetting);
                    Log.d(TAG,
                            "start time has not passed....scheduling a start task for the future.");
                }

            } else {
                Log.d(TAG, "time setting's end time has already passed.");
            }
        } else {
            Log.d(TAG, "time setting does not apply today.");

        }

    }

    private List<TimeSetting> getListOfTimeSettingsThatApplyToday() {
        ArrayList<TimeSetting> aListOfTimeSettingsThatApplyToday = new ArrayList<TimeSetting>();
        for (TimeSetting aTimeSetting : this.listOfTimeSettingsThatAreOn
                .values()) {
            if (aTimeSetting.hasTimeSettingForToday()) {
                aListOfTimeSettingsThatApplyToday.add(aTimeSetting);
            }
        }
        return aListOfTimeSettingsThatApplyToday;
    }

    private void updateListOfTimeSettingsThatAreOn() {
        List<TimeSetting> aTimeSettingList = TimeSetting
                .listAll(TimeSetting.class);
        for (TimeSetting aTimeSetting : aTimeSettingList) {
            if (aTimeSetting.isOn()) {
                this.registerTimeSettingAsOn(aTimeSetting);
            }
        }
    }

    /*
     * We scheduel a new task that will be executed in the future some time
     * later today
     */
    public void scheduelNewStartTaskForTimeSettingInFutureLaterToday(
            TimeSetting aTimeSetting) {
        // we know this setting will take place in the fuuture so we need a task
        // that will be executed
        // between now and the time settings start time
        Long startMilli = aTimeSetting.getStartTimeAsDate().getTime();
        Long endMilli = aTimeSetting.getEndTimeAsDate().getTime();
        Long nowMilli = TimeSetting.getTimeNow().getTime();

        Long start = startMilli - nowMilli;
        if (start < 0) start = (long) 0;
        Long end = endMilli - startMilli;
        Log.d(TAG, start.toString());
        Log.d(TAG, "The task will start in " + start.toString()
                + " milleseconds. from now");

        Log.d(TAG, "The task will end in " + end.toString()
                + " milleseconds after started.");

        mStartTimerTask startTask = new mStartTimerTask(aTimeSetting, end);
        //cancel before comitting
//        this.cancelTimeSetting(aTimeSetting);
        // keep a reference to this task just in case we want to cancel it
        // before it has started
        timeSettingStartTasks.put(aTimeSetting.getId(), startTask);
        Log.d(TAG,
                "Added time setting's start task with id:"
                        + aTimeSetting.getId() + "Size is now:"
                        + this.timeSettingStartTasks.size());
        this.aTimer.schedule(startTask, start);

    }

	/*
     * we schedule a new start task that will run immediately
	 */

    private void scheduelNewStartTaskToRunNow(TimeSetting aTimeSetting) {
        Long nowMilli = TimeSetting.getTimeNow().getTime();
        Long endMilli = aTimeSetting.getEndTimeAsDate().getTime();

        Long end = endMilli - nowMilli;

        if (end < 0) end = (long) 0;

        mStartTimerTask startTask = new mStartTimerTask(aTimeSetting, end);
        // keep a reference to this task just in case we want to cancel it
        // before it has started

        //cancel before comitting
//        this.cancelTimeSetting(aTimeSetting);
        timeSettingStartTasks.put(aTimeSetting.getId(), startTask);
        Log.d(TAG,
                "Added time setting's start task with id:"
                        + aTimeSetting.getId() + "Size is now:"
                        + this.timeSettingStartTasks.size());
        this.aTimer.schedule(startTask, 0);
    }

    public void broadcastTimeSettingEndedNotification() {
        Log.d(TAG, "broadcasting time setting ended!");
        Intent i = new Intent("com.exmaple.jajingprototype.system.event.TIME_SETTING_ENDED");
        context.sendBroadcast(i);

    }

    /*
     * This task is responsible for turning jajajing on in X amount of
     * milliseconds from now
     */
    private class mStartTimerTask extends TimerTask {
        private static final String TAG = "mStartTimerTask";
        private volatile Looper mMyLooper;

        private Long end;
        private TimeSetting timeSetting;

        public mStartTimerTask(TimeSetting aTimeSetting, Long end) {

            this.end = end;
            this.timeSetting = aTimeSetting;

        }

        @Override
        public void run() {
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }

            // start jajing
            String status = TimeSettingTaskManager.getInstance().user.getUserStatus().getAvailabilityStatus();
            if (status.equalsIgnoreCase("AVAILABLE")) status = "UNAVAILABLE";
            TimeSettingTaskManager.this.user.goUnavailable(status, new AvailabilityTime(timeSetting.getEndTime()));
            // keep track of the task for this time setting
            //TimeSettingTaskManager.getInstance().timeSettingStartTasks.put(timeSetting.getId(), this);
            Log.d(TAG,
                    "Added timesetting's start task with id:"
                            + timeSetting.getId()
                            + " to tasks. Size is now:"
                            + TimeSettingTaskManager.this.timeSettingStartTasks
                            .size());
            // once the task has started we need to shut it down X milliseconds
            // from the start time to end time
            Log.d(TAG, "ending jajing in " + end.toString() + " milliseconds");
            // lets also remove the start task associated with the time setting from
            TimeSettingTaskManager.this.timeSettingStartTasks.remove(mStartTimerTask.this);
            // add an end task associated with this time setting
            mEndTaskTimerTask endTask = new mEndTaskTimerTask(timeSetting.getId());
            //check if we have already defined an end task for this time setting id
//            TimeSettingTaskManager.this.cancelEndTask(timeSetting);
            TimeSettingTaskManager.this.timeSettingEndTasks.put(timeSetting.getId(), endTask);

            // schdule a new task end task to run, for jajing to stop
            TimeSettingTaskManager.getInstance().aTimer.schedule(endTask, end);

        }

        @Override
        public boolean cancel() {
            Log.d(TAG, "Called cancel on TimeTask start");
            return super.cancel();


        }

        private class mEndTaskTimerTask extends TimerTask {
            private volatile Looper mMyLooper;
            private Long timeSettingId;

            public mEndTaskTimerTask(Long timeSettingId) {
                this.timeSettingId = timeSettingId;
            }

            @Override
            public void run() {
                TimeSettingTaskManager taskManager = TimeSettingTaskManager.getInstance();
                taskManager.turnTimeSettingOff(timeSettingId);
                taskManager.broadcastTimeSettingEndedNotification();


            }

            @Override
            public boolean cancel() {
                Log.d(TAG, "Called cancel on TimeTask end");
                return super.cancel();


            }


        }

    }

}
