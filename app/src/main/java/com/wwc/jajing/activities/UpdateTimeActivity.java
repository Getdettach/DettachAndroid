package com.wwc.jajing.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wwc.R;
import com.wwc.jajing.domain.entity.TimeSetting;
import com.wwc.jajing.domain.entity.User;
import com.wwc.jajing.domain.value.AvailabilityTime;
import com.wwc.jajing.services.JJOnAwayService;
import com.wwc.jajing.settings.time.TimeSettingTaskManager;
import com.wwc.jajing.system.JJSystemImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UpdateTimeActivity extends AppCompatActivity implements View.OnClickListener{

    User user;

    Button btnTen,btnFifteen,btnTwenty,btnThirty;

    TimeSetting timeSetting;

    int addVal = 0;

    String endTime,startTime;

    Long timeId;

    TimeSetting aTimeSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_time);

        TextView statusTxt = (TextView)findViewById(R.id.status_name);

        TextView endTimeTxt = (TextView)findViewById(R.id.end_time);


        user = (User) JJSystemImpl.getInstance().getSystemService(
                JJSystemImpl.Services.USER);

        String statusName = user.getUserStatus().getAvailabilityStatus();

        TimeSetting timeSetting;

//        List<TimeSetting> timeList = TimeSetting.listAll(TimeSetting.class);
//
//        timeSetting = TimeSetting.findById(TimeSetting.class, timeList.get(0).getId());


        List<TimeSetting> timeList = TimeSetting.listAll(TimeSetting.class);

        for (TimeSetting aTimeSetting : timeList) {

            if(aTimeSetting.getId() != 1 && aTimeSetting.isOn())
            {
                aTimeSettings = TimeSetting.findById(TimeSetting.class, aTimeSetting.getId());
                System.out.println(" is ON = " + aTimeSettings.getId());

                timeId = aTimeSettings.getId();

                break;
            }

        }


//        timeSetting = TimeSetting.findById(TimeSetting.class, TimeSettingTaskManager.getInstance().getTimeSettingIdClosestToBeingDone());

        endTime = aTimeSettings.getEndTime();

        startTime = aTimeSettings.getStartTime();

        statusTxt.setText(statusName);
        endTimeTxt.setText(endTime);


        btnTen = (Button)findViewById(R.id.btn_ten);
        btnFifteen = (Button)findViewById(R.id.btn_fifteen);
        btnTwenty = (Button)findViewById(R.id.btn_twenty);
        btnThirty = (Button)findViewById(R.id.btn_thirty);

        registerClickHandlers();

    }

    private void registerClickHandlers() {
        btnTen.setOnClickListener(this);
        btnFifteen.setOnClickListener(this);
        btnTwenty.setOnClickListener(this);
        btnThirty.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_ten:
                addVal = 10;
                break;
            case R.id.btn_fifteen:
                addVal = 15;
                break;
            case R.id.btn_twenty:
                addVal = 60;
                break;
            case R.id.btn_thirty:
                addVal = 120;
                break;
        }

        continueHome();

    }


    public void continueHome() {

        Toast.makeText(this, "Time added "+addVal, Toast.LENGTH_LONG).show();

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

        try {
            Date d = sdf.parse(endTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.MINUTE, addVal);
            endTime = sdf.format(cal.getTime());

//            System.out.println("endTime = " + endTime);

        }catch(Exception e){
            e.printStackTrace();
        }


        goUnavailable();

//        Calendar calendar = Calendar.getInstance();
//
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR));
//        calendar.set(Calendar.MINUTE,calendar.get(Calendar.MINUTE));
//        calendar.set(Calendar.SECOND,00);
//
//        Intent myIntent = new Intent(this, JJOnAwayService.class);
//        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent,PendingIntent.FLAG_CANCEL_CURRENT);
//        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


        stopService(new Intent(this,JJOnAwayService.class));
        startService(new Intent(this, JJOnAwayService.class));

        Intent i = new Intent(this, StatusActivity.class);
        this.startActivity(i);
        finish();
    }

    private void goUnavailable() {

        this.user.goUnavailable(this.user.getUserStatus().getAvailabilityStatus(),this.startTime, new AvailabilityTime(this.endTime),timeId);

    }

    @Override
    protected void onPause() {
        super.onPause();

        System.out.println("ON PAUSE");

    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("ON RESUME");
    }


}
