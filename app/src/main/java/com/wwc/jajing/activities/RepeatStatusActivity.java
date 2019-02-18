package com.wwc.jajing.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.wwc.R;
import com.wwc.jajing.domain.entity.TimeSetting;
import com.wwc.jajing.domain.entity.User;
import com.wwc.jajing.domain.value.AvailabilityTime;
import com.wwc.jajing.receivers.TimeSettingReceiver;
import com.wwc.jajing.system.JJSystemImpl;

import java.util.List;

public class RepeatStatusActivity extends AppCompatActivity {

    private List<TimeSetting> aListOfTimeSettings;

    private TableLayout aTimeSettingTabeLayout;

    private static final String TAG = "RepeatStatus";

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat_status);

        user = (User) JJSystemImpl.getInstance().getSystemService(
                JJSystemImpl.Services.USER);

        this.aTimeSettingTabeLayout = (TableLayout) findViewById(R.id.tableTimeSettingTableLayout);

        this.aListOfTimeSettings = TimeSetting.listAll(TimeSetting.class);
//        Log.d(TAG, "time settings list size:" + aListOfTimeSettings.size());
//
//        for(TimeSetting tm : aListOfTimeSettings){
//            System.out.println("tm = " + tm.getId());
//            System.out.println("tm.getStartTime() = " + tm.getStartTime());
//            System.out.println("tm.getEndTime() = " + tm.getEndTime());
//            System.out.println("tm.getStatus() = " + tm.getStatus());
//        }


        loadTimeSettings(aListOfTimeSettings);

    }

    private void loadTimeSettings(List<TimeSetting> timeSettings) {
        if (timeSettings.size() <= 1) {
            TextView tv = new TextView(this);
            tv.setText("You have not added any time settings at this time.");
            tv.setPadding(10, 10, 10, 10);
            tv.setTextColor(Color.WHITE);
            aTimeSettingTabeLayout.addView(tv);
        }

        for (TimeSetting aTimeSetting : this.aListOfTimeSettings) {
            if (aTimeSetting.getId() != 1 && aTimeSetting.getEndTime() != null && aTimeSetting.getStartTime() != null) {
                this.createTimeSettingView(aTimeSetting);
            }
            Log.d(TAG, aTimeSetting.getId().toString());
        }
    }


    private void createTimeSettingView(TimeSetting aTimeSetting) {
        // we need a parent table row to encapsulate
        TableLayout aParentTableLayout = new TableLayout(this);
        TableLayout.LayoutParams myTableLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myTableLayoutParams.setMargins(10, 10, 10, 10);
        aParentTableLayout.setLayoutParams(myTableLayoutParams);

        LinearLayout aParentLinLayout = new LinearLayout(this);
        aParentLinLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams myLinLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        myLinLayoutParams.setMargins(10, 5, 10, 5);
        aParentLinLayout.setLayoutParams(myLinLayoutParams);

        aParentLinLayout.setBackgroundColor(Color.BLACK);


        aParentTableLayout.setId(Integer.parseInt(aTimeSetting.getId()
                .toString()));
        aParentTableLayout.setBackgroundColor(Color.BLACK);


        TableRow aTableRowStatus = new TableRow(this);
        aTableRowStatus.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView statusView= new TextView(this);
        statusView.setText(aTimeSetting.getStatus());
        statusView.setTextColor(Color.WHITE);


        // we need a table row
        TableRow aTableRow = new TableRow(this);
        aTableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        // this row has 4 children - 3 text views and a button
        // text view 1 is the start time
        TextView startTimeView = new TextView(this);
        startTimeView.setPadding(10, 10, 10, 10);
        startTimeView.setText(aTimeSetting.getStartTime());
        // TAG*****
        startTimeView.setTag("startTimeView" + aTimeSetting.getId().toString());
        if (aTimeSetting.isOn()) {
            startTimeView.setTextAppearance(this, R.style.onTimeSetting);
        } else {
            startTimeView.setTextAppearance(this, R.style.offTimeSetting);

        }
        // text view 2 is the separator
        TextView separator = new TextView(this);
        separator.setPadding(10, 10, 10, 10);
        separator.setText("-");
        // TAG*****
        separator.setTag("-" + aTimeSetting.getId().toString());
        if (aTimeSetting.isOn()) {
            separator.setTextAppearance(this, R.style.onTimeSetting);
        } else {
            separator.setTextAppearance(this, R.style.offTimeSetting);

        }
        

        // textviw 3 end time
        TextView endTimeView = new TextView(this);
        endTimeView.setPadding(10, 10, 10, 10);
        endTimeView.setText(aTimeSetting.getEndTime());
        // TAG*****
        endTimeView.setTag("endTimeView" + aTimeSetting.getId().toString());
        if (aTimeSetting.isOn()) {
            endTimeView.setTextAppearance(this, R.style.onTimeSetting);
        } else {
            endTimeView.setTextAppearance(this, R.style.offTimeSetting);

        }

        // empty view to act as a separator
        // add a view to the table layout
        TextView randomText = new TextView(this);
        randomText.setText("hghjgjhgjh");
        randomText.setTextColor(Color.BLACK);


        // toggle button is last
        ToggleButton tb = new ToggleButton(this);
        // TAG*****
        tb.setTag(aTimeSetting.getId());
        tb.setOnCheckedChangeListener(new mToggleButtonListener());
        if (aTimeSetting.isOn()) {
            tb.setTextColor(Color.parseColor("#ff33b5e5"));
            tb.setChecked(true);
        } else {
            tb.setChecked(false);
            tb.setTextColor(Color.BLACK);
        }

//        tb.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//
//            }
//        });


        // add these child view first table row

        aTableRowStatus.addView(statusView);

        aTableRow.addView(startTimeView);
        aTableRow.addView(separator);
        aTableRow.addView(endTimeView);
        aTableRow.addView(randomText);
        aTableRow.addView(tb);

        // add a view to the table layout
        View aLine = new View(this);
        aLine.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
        aLine.setBackgroundColor(Color.parseColor("#444444"));

        // add a second view this will be the days of the week the time setting
        // is ON for
        TextView daysTimeSettingIsOn = new TextView(this);
        daysTimeSettingIsOn.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        daysTimeSettingIsOn.setText(aTimeSetting
                .getDaysOfTheWeekTimeSettingIsActiveFor(aTimeSetting));
        // TAG*****
        daysTimeSettingIsOn.setTag("daysTimeSettingIsOn"
                + aTimeSetting.getId().toString());
        daysTimeSettingIsOn.setPadding(0, 0, 0, 20);
        if (aTimeSetting.isOn()) {
            daysTimeSettingIsOn.setTextColor(Color.parseColor("#ffffbb33"));
        } else {
            daysTimeSettingIsOn.setTextColor(Color.parseColor("#444444"));
        }
        // append all this to parent table

        aParentLinLayout.addView(aTableRowStatus);

        aParentTableLayout.addView(aTableRow);
        aParentTableLayout.addView(aLine);
        aParentTableLayout.addView(daysTimeSettingIsOn);

        aParentLinLayout.addView(aParentTableLayout);



        // appending
        this.aTimeSettingTabeLayout.addView(aParentLinLayout);
//         aTimeSettingTabeLayout.addView(aLine);
//         aTimeSettingTabeLayout.addView(daysTimeSettingIsOn);

    }

    private class mToggleButtonListener implements
            CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {

            if (isChecked) {

                buttonView.setChecked(false);

                // The toggle is enabled
//                Intent i = new Intent()
//                        .setAction("com.exmaple.jajingprototype.system.event.TIME_SETTING_ON");
//                i.putExtra(TimeSettingReceiver.EXTRA_TIME_SETTING_ID,
//                        Long.parseLong(buttonView.getTag().toString()));
//                sendBroadcast(i);

                TimeSetting timeSetting = TimeSetting.findById(TimeSetting.class, Long.valueOf(buttonView.getTag().toString()));
                String startTime = timeSetting.getStartTime();
                String endTime = timeSetting.getEndTime();
                String statusName = timeSetting.getStatus();

//                user.goUnavailable(statusName, startTime, new AvailabilityTime(endTime));

//                System.out.println("timeSetting.getId() = " + timeSetting.getId());

                Intent timeId = new Intent(RepeatStatusActivity.this,TimeSettingActivity.class);
                timeId.putExtra("START_TIME",startTime);
                timeId.putExtra("END_TIME",endTime);
                timeId.putExtra("STATUS",statusName);
                timeId.putExtra("TIME_ID",timeSetting.getId());
                startActivity(timeId);

                finish();



            } else {
                // The toggle is disabled
                Intent i = new Intent()
                        .setAction("com.exmaple.jajingprototype.system.event.TIME_SETTING_OFF");
                i.putExtra(TimeSettingReceiver.EXTRA_TIME_SETTING_ID,
                        Long.parseLong(buttonView.getTag().toString()));
                sendBroadcast(i);

            }
            toggleTimeSettingView(buttonView, isChecked);

        }

    }

    private void toggleTimeSettingView(CompoundButton buttonView,
                                       boolean isChecked) {
        String aTimeSettingID = buttonView.getTag().toString();
        TableLayout aTableLayoutWithTimeSettingId = (TableLayout) findViewById(Integer
                .valueOf(aTimeSettingID));
        // change the color of our views
        if (aTableLayoutWithTimeSettingId != null) {
            TextView startTimeView = (TextView) aTableLayoutWithTimeSettingId
                    .findViewWithTag("startTimeView" + aTimeSettingID);
            TextView endTimeView = (TextView) aTableLayoutWithTimeSettingId
                    .findViewWithTag("endTimeView" + aTimeSettingID);
            TextView separator = (TextView) aTableLayoutWithTimeSettingId
                    .findViewWithTag("-" + aTimeSettingID);
            TextView daysTimeSettingIsOnView = (TextView) aTableLayoutWithTimeSettingId
                    .findViewWithTag("daysTimeSettingIsOn" + aTimeSettingID);

            if (isChecked) {
                buttonView.setTextColor(Color.parseColor("#ff33b5e5"));

                startTimeView.setTextColor(Color.parseColor("#ffffbb33"));
                endTimeView.setTextColor(Color.parseColor("#ffffbb33"));
                separator.setTextColor(Color.parseColor("#ffffbb33"));
                daysTimeSettingIsOnView.setTextColor(Color.parseColor("#ffffbb33"));
            } else {
                startTimeView.setTextColor(Color.parseColor("#444444"));
                endTimeView.setTextColor(Color.parseColor("#444444"));
                separator.setTextColor(Color.parseColor("#444444"));
                daysTimeSettingIsOnView.setTextColor(Color.parseColor("#444444"));
                buttonView.setTextColor(Color.BLACK);

            }
        }


    }

}
