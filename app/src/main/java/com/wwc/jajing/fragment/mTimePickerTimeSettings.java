package com.wwc.jajing.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

import com.wwc.jajing.settings.time.TimeSettingTaskManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@SuppressLint("ValidFragment")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class mTimePickerTimeSettings extends DialogFragment implements
        OnTimeSetListener {

    private static final String TAG = "mTimePicker";
    private boolean isTimePickerForStartTime;
    private boolean isCalled = false;

    private String startTime,endTime;

    String format = "";

    public mTimePickerTimeSettings(boolean isTimePickerForStartTime) {
        this.isTimePickerForStartTime = isTimePickerForStartTime;
    }

    public mTimePickerTimeSettings(boolean isTimePickerForStartTime,String startTime,String endTime) {
        this.isTimePickerForStartTime = isTimePickerForStartTime;
        this.startTime = startTime ;
        this.endTime= endTime;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        if (isTimePickerForStartTime && startTime!=null) {

            String[] time = startTime.split(":");
            int hr = Integer.parseInt(time[0]);

            String[]min = time[1].split(" ");
            int mn = Integer.parseInt(min[0]);
            format = min[1];

            hour = hr;
            minute = mn;


        } else if(startTime!=null) {

            String[] time = endTime.split(":");
            int hr = Integer.parseInt(time[0]);

            String[]min = time[1].split(" ");
            int mn = Integer.parseInt(min[0]);
            format = min[1];

            hour = hr;
            minute = mn;



        }

        if(format!=null && format.compareTo("AM")==0){
            hour = hour;
        }else{
            hour = hour+12;
        }


        TimePickerDialog tpd = new TimePickerDialog(getActivity(), this,
                hour, minute, DateFormat.is24HourFormat(getActivity()));


        setTimePickerTitle(tpd);

        // Create a new instance of TimePickerDialog and return it
        return tpd;
    }

    private void setTimePickerTitle(TimePickerDialog tpd) {
        if (isTimePickerForStartTime) {
            tpd.setTitle("Start");
        } else {
            tpd.setTitle("End");
        }


    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (isCalled == false) {
            setTime(hourOfDay, minute);
            isCalled = true;
            Log.d(TAG, "onTimeSet Called" + hourOfDay + ": " + minute);
        }
    }

    private void setTime(int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        // instead of c.set(Calendar.HOUR, hour);
        c.set(Calendar.MINUTE, minute);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

        if (isTimePickerForStartTime) {
            // cancel time setting if one is set
            Toast.makeText(getActivity(), "Start Time is Set", Toast.LENGTH_LONG).show();
//            TimeSettingTaskManager.getInstance().turnTimeSettingOff(1L);
            // set the start time
            TimeSettingFragment.getInstance().setStartTime(sdf.format(c.getTime()));
            Log.d("SA", "setting start time.");

        } else {
            // set the end time
            TimeSettingFragment.getInstance().setEndTime(sdf.format(c.getTime()));
            Toast.makeText(getActivity(), "End Time is Set", Toast.LENGTH_LONG).show();
            Log.d("SA", "setting end time.");
        }

    }

}