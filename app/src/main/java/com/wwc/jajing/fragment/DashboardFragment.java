package com.wwc.jajing.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.orm.SugarContext;
import com.wwc.R;
import com.wwc.jajing.activities.LogActivity;
import com.wwc.jajing.activities.SetStatusActivity;
import com.wwc.jajing.activities.UpdateTimeActivity;
import com.wwc.jajing.domain.entity.TimeSetting;
import com.wwc.jajing.domain.entity.User;
import com.wwc.jajing.domain.services.CallManager;
import com.wwc.jajing.domain.value.AvailabilityTime;
import com.wwc.jajing.permissions.CallerPermission;
import com.wwc.jajing.realmDB.CallerModel;
import com.wwc.jajing.realmDB.CallsModel;
import com.wwc.jajing.realmDB.RepeatDaysModel;
import com.wwc.jajing.services.JJOnAwayService;
import com.wwc.jajing.settings.time.TimeSettingTaskManager;
import com.wwc.jajing.system.JJSystem;
import com.wwc.jajing.system.JJSystemImpl;

import android.Manifest;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.RealmResults;

import static com.wwc.jajing.system.JJApplication.getRealmInstance;


/**
 * Created by vivek_e on 9/12/2016.
 */
public class DashboardFragment extends BaseFragment {

    private static final String TAG = "MainActivity";
    public static final String DASHBOARD_INTENT = "com.exmaple.jajingprototype.intent.DASHBOARD_NOTIFICATION_AVAILABILITY_STATUS";

    /* For Navigation Drawer */
    private Button buttonStatus;
    private TextView textHeading;
    private TextView textCallersCanForceDisturb;
    private Button buttonAvailable;

    private ImageView imgDettach;

    //	private User user;
    private CallManager cm;
    private String status;

    String todayVal;

    boolean allowCal=true;

    boolean checkToday;

    private IntentFilter intentFilter = new IntentFilter(
            DASHBOARD_INTENT);

    // public static final String AWAY_STATUS = "awayStatus";
    private User user = (User) JJSystemImpl.getInstance().getSystemService(
            JJSystemImpl.Services.USER);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        View dashboardView = inflater.inflate(R.layout.content_main, container, false);
        setCustomActionBar("Dashboard");
        this.buttonStatus = (Button) dashboardView.findViewById(R.id.buttonStatus);
        this.buttonAvailable = (Button) dashboardView.findViewById(R.id.buttonAvailable);
        this.textHeading = (TextView) dashboardView.findViewById(R.id.textHeading);
        this.textCallersCanForceDisturb = (TextView) dashboardView.findViewById(R.id.textCallersCanForceDisturb);
        this.imgDettach = (ImageView)dashboardView.findViewById(R.id.img_dettach_logo);


//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
////        calendar.set(Calendar.DAY_OF_WEEK,1);
//        calendar.set(Calendar.HOUR_OF_DAY, 16);
//        calendar.set(Calendar.MINUTE,30);
//        calendar.set(Calendar.SECOND,00);
//
//        Intent myIntent = new Intent(getActivity(), JJOnAwayService.class);
//        PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 0, myIntent,PendingIntent.FLAG_CANCEL_CURRENT);
//        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);


        getActivity().registerReceiver(this.dashboardReceiver, this.intentFilter);
        // CACHE JJSYSTEM
        JJSystem jjSystem = JJSystemImpl.getInstance();
        // set our user
        this.user = (User) jjSystem.getSystemService(JJSystemImpl.Services.USER);
        // set our call manager
        this.cm = (CallManager) jjSystem
                .getSystemService(JJSystemImpl.Services.CALL_MANAGER);


        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS);
        if (result != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_SMS},
                    1);
        }


        result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALL_LOG);
        if (result != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CALL_LOG},
                    1);
        }

        result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS);
        if (result != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.SEND_SMS},
                    1);
        }



        buttonStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(buttonStatus.getText().equals("Still Not Available")){
                    Snackbar.make(view, "Oops! You already running a status", Snackbar.LENGTH_LONG).show();
                }else{
                    awayOptionsMenu();
                }


            }
        });

        buttonAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!user.getUserStatus()
                        .getAvailabilityStatus().equalsIgnoreCase("AVAILABLE"))
                    goAvailable();
                else
                    Snackbar.make(view, "Please Set unavailable status first.", Snackbar.LENGTH_LONG).show();
            }
        });

        List<TimeSetting> timeList = TimeSetting.listAll(TimeSetting.class);

        for (TimeSetting aTimeSetting : timeList) {

            if(aTimeSetting.getId() != 1 && aTimeSetting.isOn())
            {
                TimeSetting aTimeSettings = TimeSetting.findById(TimeSetting.class, aTimeSetting.getId());
//                System.out.println("aTimeSettings = " + aTimeSettings.getStatus());
            }
        }

//        RealmResults<RepeatDaysModel> daysList = getRealmInstance().where(RepeatDaysModel.class).findAll();
//        System.out.println("daysList  = " + daysList);
//
//
//        RealmResults<RepeatDaysModel> repeatDays = getRealmInstance().where(RepeatDaysModel.class).equalTo("id", "3").findAll();
//
//        String[] daySplit = repeatDays.get(0).getDays().split(" | ");
//
//        for(int i = 0;i<daySplit.length;i++){
//
//            String day = daySplit[i];
//
//            todayVal = day;
//
//            if(hasTimeSettingForToday(day)){
//                break;
//            }
//
//        }
//
//        System.out.println("DashBoard daySplit = " + todayVal);


        return dashboardView;
    }


    private void assignTaskToday(){

        final RealmResults<RepeatDaysModel> repeatDays = getRealmInstance().where(RepeatDaysModel.class).equalTo("available", "1").findAll();

        System.out.println("SAJEN in TimeTask"+repeatDays.size());
        System.out.println("repeatDays = " + repeatDays);


        if(repeatDays.size()>0){

            getRealmInstance().beginTransaction();
            String id = repeatDays.get(0).getId();
            String[] daySplit = repeatDays.get(0).getDays().split(" | ");
            getRealmInstance().commitTransaction();

            for(int i = 0;i<daySplit.length;i++){

                String day = daySplit[i];

                if(hasTimeSettingForToday(day)){
                    allowCal = false;
                    break;
                }
            }

            System.out.println("allowCal = " + allowCal);

            TimeSetting aTimeSettings = TimeSetting.findById(TimeSetting.class, Long.parseLong(id));
            aTimeSettings.on();

            SimpleDateFormat dateFormatterTIME = new SimpleDateFormat("hh:mm a");

            try {

                Date now = dateFormatterTIME.parse(dateFormatterTIME.format((new Date())));
                Date startT = dateFormatterTIME.parse(aTimeSettings.getStartTime());
                Date endT = dateFormatterTIME.parse(aTimeSettings.getEndTime());

                if (!allowCal && user.getUserStatus().getAvailabilityStatus().compareTo("AVAILABLE") == 0 && (now.equals(startT) || now.after(startT)) && (now.equals(endT) || now.before(endT))) {

                    aTimeSettings.save();
                    this.user.goUnavailable(aTimeSettings.getStatus(), aTimeSettings.getStartTime(), new AvailabilityTime(aTimeSettings.getStatus())  );

//                    getActivity().stopService(new Intent(getActivity(),JJOnAwayService.class));
//                    getActivity().startService(new Intent(getActivity(), JJOnAwayService.class));

                }


            }catch(Exception e){
                e.printStackTrace();
            }

        }
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



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Main Activity was destroyed!");
        getActivity().unregisterReceiver(this.dashboardReceiver);

    }

    @Override
    public void onStart() {
        super.onStart();

//        this.assignTaskToday();

        this.status = this.user.getUserStatus().getAvailabilityStatus();
        this.updateAvailabilityStatus((this.user.getUserStatus()
                .getAvailabilityStatus() != null) ? this.user.getUserStatus()
                .getAvailabilityStatus() : "Not Set!");

    }

    @Override
    public void onStop() {
        super.onStop();
    }


    public void awayOptionsMenu() {
        Intent intent = new Intent(getActivity(), SetStatusActivity.class);
        startActivity(intent);
    }

    public void goAvailable() {
        this.user.goAvailable();


        List<TimeSetting> timeList = TimeSetting.listAll(TimeSetting.class);

        for (TimeSetting aTimeSetting : timeList) {

            if(aTimeSetting.getId() != 1 && aTimeSetting.isOn())
            {
                TimeSetting aTimeSettings = TimeSetting.findById(TimeSetting.class, aTimeSetting.getId());
                aTimeSettings.off();
                aTimeSettings.save();
            }
        }


        Intent service_int = new Intent(getActivity(), JJOnAwayService.class);
        getActivity().stopService(service_int);

        Intent intent = new Intent(getActivity(), LogActivity.class);
        intent.putExtra("recentFlag", true);
        startActivity(intent);

    }

    private void updateAvailabilityStatus(String status) {

        if (status.equalsIgnoreCase("AVAILABLE")) {
            this.setHeading("What's Your Status?");
            this.showCallersCanForceDisturb(true);
            this.changeButtonStatusText("Not Available");
            this.changeButtonAvailableText("Available");
            this.imgDettach.setVisibility(View.VISIBLE);

        } else {
            this.setHeading("Change Status");
            this.showCallersCanForceDisturb(false);
            this.changeButtonStatusText("Still Not Available");
            this.changeButtonAvailableText("I am now Available");
            this.imgDettach.setVisibility(View.GONE);
        }
        Log.d(TAG, "Availability Status has been updated!" + status);
    }

    private void setHeading(String aHeading) {
        this.textHeading.setText(aHeading);
    }

    private void showCallersCanForceDisturb(Boolean toShow) {
        if (toShow) {
            this.textCallersCanForceDisturb.setVisibility(View.VISIBLE);
        } else {
            this.textCallersCanForceDisturb.setVisibility(View.GONE);

        }
    }

    private void changeButtonStatusText(String textForButton) {
        this.buttonStatus.setText(textForButton);
    }

    private void changeButtonAvailableText(String textForButton) {
        this.buttonAvailable.setText(textForButton);
    }

    private BroadcastReceiver dashboardReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DASHBOARD_INTENT)) {
                Log.d(TAG,
                        "dashboard intent receiveed. status field has been set.");
                // Do something with the string
                status = intent.getStringExtra("status");
                updateAvailabilityStatus(status);
            }
        }

    };

}
