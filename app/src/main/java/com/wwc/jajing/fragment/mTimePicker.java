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

@SuppressLint("ValidFragment")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class mTimePicker extends DialogFragment implements
        OnTimeSetListener {

    private static final String TAG = "mTimePicker";
    private boolean isTimePickerForStartTime;
    private boolean isCalled = false;

    private String screenName="";

    public mTimePicker(boolean isTimePickerForStartTime) {
        this.isTimePickerForStartTime = isTimePickerForStartTime;
    }

    public mTimePicker(boolean isTimePickerForStartTime,String screen) {
        this.isTimePickerForStartTime = isTimePickerForStartTime;
        this.screenName= screen;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

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
//            Toast.makeText(getActivity(), "Start Time is Set", Toast.LENGTH_LONG).show();
			TimeSettingTaskManager.getInstance().turnTimeSettingOff(1L);
            // set the start time
            AwayOptionFragment.getInstance().setStartTime(sdf.format(c.getTime()));
            Log.d("SA", "setting start time.");

        } else {
            // set the end time

//            System.out.println("name "+this.screenName);

            if(this.screenName.compareTo("STATUS")!=0)
                AwayOptionFragment.getInstance().setEndTime(sdf.format(c.getTime()));
            else
                StatusFragment.getInstance().setEndTime(sdf.format(c.getTime()));

//            Toast.makeText(getActivity(), "End Time is Set", Toast.LENGTH_LONG).show();
            Log.d("SA", "setting end time.");

        }

    }

}