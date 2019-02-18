package com.wwc.jajing.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Vivek on 25-09-2016.
 */

public class StartTimerDialogFrament extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "StartTimeTimePickerFragment";

    public StartTimerDialogFrament() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

        tpd.setTitle("start time");

        // Create a new instance of TimePickerDialog and return it
        return tpd;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        //instead of c.set(Calendar.HOUR, hour);
        c.set(Calendar.MINUTE, minute);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

        TimeSettingFragment.setStartTime(sdf.format(c.getTime()));


//        System.out.println("sdf.format(c.getTime()) = " + sdf.format(c.getTime()));

    }
}

