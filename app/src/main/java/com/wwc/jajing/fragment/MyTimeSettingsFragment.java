package com.wwc.jajing.fragment;

/**
 * Created by Shiju on 9/26/2016.
 */


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.wwc.R;
import com.wwc.jajing.activities.Constants;
import com.wwc.jajing.activities.MyTimeSettings;
import com.wwc.jajing.activities.StatusActivity;
import com.wwc.jajing.domain.entity.TimeSetting;
import com.wwc.jajing.realmDB.TimeModel;
import com.wwc.jajing.receivers.TimeSettingReceiver;
import com.wwc.jajing.settings.time.TimeSettingTaskManager;

import java.util.HashMap;
import java.util.List;

public class MyTimeSettingsFragment extends BaseFragment {

    private static final String TAG = "MyTimeSettings";

    private List<TimeSetting> aListOfTimeSettings;

    private TableLayout aTimeSettingTabeLayout;

    String start,end;

    private static TextView textStartDisplay;
    private static TextView textEndDisplay,textStatusName;

    private Button buttonSaveTimeSetting;


    private CheckBox checkboxRepeat;
    private CheckBox checkboxEveryDay;

    private TableLayout tableDaysOfTheWeek;

    TimeModel timeModel;


    private HashMap<TimeSetting.Days, Boolean> daysToRepeatCollection = new HashMap<TimeSetting.Days, Boolean>();

    private int idOfCheckboxChecked;

    TimeSetting timeSetting;


    public static final String EXTRA_REFRSH_AND_CLOSE = "refresh_and_close";
    View dashboardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        dashboardView = inflater.inflate(R.layout.activity_list_of_time_settings, container, false);
        setCustomActionBar("My Status");

        timeSetting = TimeSetting.findById(TimeSetting.class, TimeSettingTaskManager.getInstance().getTimeSettingIdClosestToBeingDone());

        this.aListOfTimeSettings = TimeSetting.listAll(TimeSetting.class);
      //  Log.d(TAG, "time settings list size:" + aListOfTimeSettings.size());
        this.aTimeSettingTabeLayout = (TableLayout) dashboardView.findViewById(R.id.tableTimeSettingTableLayout);

       this.loadTimeSettings(aListOfTimeSettings);
        //  this.aListOfTimeSettings = TimeSetting.listAll(TimeSetting.class);
        //  Log.d(TAG, "time settings list size:" + aListOfTimeSettings.size());
        this.aTimeSettingTabeLayout = (TableLayout) dashboardView.findViewById(R.id.tableTimeSettingTableLayout);

        this.textStartDisplay = (TextView) dashboardView.findViewById(R.id.textStartDisplay);
        this.textEndDisplay = (TextView) dashboardView.findViewById(R.id.textEndDisplay);

        this.textStatusName = (TextView)dashboardView.findViewById(R.id.txt_status);

        textStartDisplay.setText("Start Time: " + timeSetting.getStartTime());
        textEndDisplay.setText("End Time: " + timeSetting.getEndTime());

//        textStartDisplay.setText("Start Time: " + Constants.startTime);
//        textEndDisplay.setText("End Time: " + Constants.endTime);
        textStatusName.setText(Constants.statusName);


        this.checkboxRepeat = (CheckBox) dashboardView.findViewById(R.id.checkboxRepeat);
        this.checkboxEveryDay = (CheckBox) dashboardView.findViewById(R.id.checkboxEveryDay);
        this.buttonSaveTimeSetting = (Button) dashboardView.findViewById(R.id.buttonSaveTimeSetting);
        final TextView textMon = (TextView) dashboardView.findViewById(R.id.textMon);
        final TextView textTu = (TextView) dashboardView.findViewById(R.id.textTu);
        final TextView textWed = (TextView) dashboardView.findViewById(R.id.textWed);
        final TextView textThu = (TextView) dashboardView.findViewById(R.id.textThu);
        final TextView textFri = (TextView) dashboardView.findViewById(R.id.textFri);
        final TextView textSat = (TextView) dashboardView.findViewById(R.id.textSat);
        final TextView textSun = (TextView) dashboardView.findViewById(R.id.textSun);
        buttonSaveTimeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Your Time is set.", Snackbar.LENGTH_LONG).show();
                saveTimeSetting(buttonSaveTimeSetting);
            }
        });

        textMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDayToRepeat(textMon);
            }
        });
        textTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDayToRepeat(textTu);
            }
        });
        textWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDayToRepeat(textWed);
            }
        });
        textThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDayToRepeat(textThu);
            }
        });
        textFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDayToRepeat(textFri);
            }
        });
        textSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDayToRepeat(textSat);
            }
        });
        textSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDayToRepeat(textSun);
            }
        });
        checkboxEveryDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkboxHandler(checkboxEveryDay);
            }
        });

        checkboxRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkboxHandler(checkboxRepeat);
            }
        });
        this.tableDaysOfTheWeek = (TableLayout) dashboardView.findViewById(R.id.tableDaysOfTheWeek);

        //  this.loadTimeSettings(aListOfTimeSettings);
        // make sure we scroll the user down to the most recently added time
        // setting if they added one
        if (getActivity().getIntent().getBooleanExtra("addedTimeSetting", false)) {
            this.scrollUserDown();

        }
        return dashboardView;
    }

    public void saveTimeSetting(View view) {
        //validate start and end time

        Intent intent = new Intent(getActivity(), StatusActivity.class);
        startActivity(intent);

    }


    public void checkboxHandler(View view) {
        CheckBox cb = (CheckBox) view;
        int viewId = view.getId();
        switch (viewId) {
            case R.id.checkboxRepeat:
                if (cb.isChecked()) {
                    this.tableDaysOfTheWeek.setVisibility(View.VISIBLE);
                    //init the days to repeat collection
                    this.initDaysToRepeatCollection();

                } else {
                    this.tableDaysOfTheWeek.setVisibility(View.GONE);

                }
                this.uncheckExcept(cb);


                break;

            case R.id.checkboxEveryDay:
                if (cb.isChecked()) {
                    this.tableDaysOfTheWeek.setVisibility(View.GONE);

                }
                this.uncheckExcept(cb);

                break;
        }


    }

    private void initDaysToRepeatCollection() {

        this.daysToRepeatCollection.put(TimeSetting.Days.SUNDAY, true);
        this.daysToRepeatCollection.put(TimeSetting.Days.MONDAY, true);
        this.daysToRepeatCollection.put(TimeSetting.Days.TUESDAY, true);

        this.daysToRepeatCollection.put(TimeSetting.Days.WEDNESDAY, true);
        this.daysToRepeatCollection.put(TimeSetting.Days.THURSDAY, true);
        this.daysToRepeatCollection.put(TimeSetting.Days.FRIDAY, true);

        this.daysToRepeatCollection.put(TimeSetting.Days.SATURDAY, true);


    }

    private void uncheckExcept(View aCheckedCheckboxToKeepChecked) {
        CheckBox[] checkBoxes = new CheckBox[]{
                (CheckBox) dashboardView.findViewById(R.id.checkboxRepeat),
                (CheckBox) dashboardView.findViewById(R.id.checkboxEveryDay)};

        for (CheckBox aCheckbox : checkBoxes) {

            if (aCheckbox.getId() == aCheckedCheckboxToKeepChecked.getId()) {
                if (aCheckbox.isChecked()) {
                    Log.d(TAG, "checbox is checked...");
                    this.idOfCheckboxChecked = aCheckedCheckboxToKeepChecked.getId();

                } else {
                    //we set the id to 99 to singal the user they need to select a frequency
                    this.idOfCheckboxChecked = 99;
                    Log.d(TAG, "setting id of checkbox to 99");
                }
                continue;
            } else {
                aCheckbox.setChecked(false);
            }
        }
    }


    public void toggleDayToRepeat(View aView) {

        TextView tv = (TextView) aView;
        String abbrev = (String) tv.getText();
        TimeSetting.Days aDay = TimeSetting.Days.getEnumByAbbrev(abbrev);
        switch (tv.getId()) {
            case R.id.textSun:
                //togle color before toggleing day, because if ui color update
                this.toggleTextViewColorForDays(aView);
                this.toggleDay(aDay);
                break;
            case R.id.textMon:
                this.toggleTextViewColorForDays(aView);
                this.toggleDay(aDay);
                break;
            case R.id.textTu:
                this.toggleTextViewColorForDays(aView);
                this.toggleDay(aDay);
                break;
            case R.id.textWed:
                this.toggleTextViewColorForDays(aView);
                this.toggleDay(aDay);
                break;
            case R.id.textThu:
                this.toggleTextViewColorForDays(aView);
                this.toggleDay(aDay);
                break;
            case R.id.textFri:
                this.toggleTextViewColorForDays(aView);
                this.toggleDay(aDay);
                break;
            case R.id.textSat:
                this.toggleTextViewColorForDays(aView);
                this.toggleDay(aDay);
                break;
        }

        Log.d(TAG, "The text:" + abbrev);

    }

    private void toggleDay(TimeSetting.Days aDay) {

        if (this.daysToRepeatCollection.get(aDay)) { //if this day is set to repeat, toggle it...
            //toggle state
            this.daysToRepeatCollection.put(aDay, false);

        } else {
            //toggle state
            this.daysToRepeatCollection.put(aDay, true);

        }


    }

    private void toggleTextViewColorForDays(View aView) {
        TextView tv = (TextView) aView;
        String abbrev = (String) tv.getText();
        TimeSetting.Days aDay = TimeSetting.Days.getEnumByAbbrev(abbrev);


        if (this.daysToRepeatCollection.get(aDay)) { //if this day is set to repeat, toggle it...
            //toggle color
            tv.setTextColor(Color.parseColor("#FFBB34"));

        } else {
            //day is not set to repeat, do this...
            tv.setTextColor(Color.parseColor("#ff33b5e5"));

        }

    }



    private void scrollUserDown() {
        getScrollView().post(new Runnable() {

            @Override
            public void run() {
                getScrollView().fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private ScrollView getScrollView() {
        return (ScrollView) dashboardView.findViewById(R.id.scrollViewMyTimeSettings);
    }

    private void loadTimeSettings(List<TimeSetting> timeSettings) {
        if (timeSettings.size() <= 1) {
            TextView tv = new TextView(getActivity());
            tv.setText("You have not added any time settings at this time.");
            tv.setPadding(10, 10, 10, 10);
            tv.setTextColor(Color.WHITE);
            aTimeSettingTabeLayout.addView(tv);
        }

        for (TimeSetting aTimeSetting : this.aListOfTimeSettings) {
            if (aTimeSetting.getId() != 1) {
                this.createTimeSettingView(aTimeSetting);
            }
            Log.d(TAG, aTimeSetting.getId().toString());
        }
    }

    private void createTimeSettingView(TimeSetting aTimeSetting) {
        // we need a parent table row to encapsulate
        TableLayout aParentTableLayout = new TableLayout(getActivity());
        TableLayout.LayoutParams myTableLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        myTableLayoutParams.setMargins(10, 10, 10, 10);
        aParentTableLayout.setLayoutParams(myTableLayoutParams);


        aParentTableLayout.setId(Integer.parseInt(aTimeSetting.getId()
                .toString()));
        aParentTableLayout.setBackgroundColor(Color.BLACK);

        // we need a table row
        TableRow aTableRow = new TableRow(getActivity());
        aTableRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        // this row has 4 children - 3 text views and a button
        // text view 1 is the start time
        TextView startTimeView = new TextView(getActivity());
        startTimeView.setPadding(10, 10, 10, 10);
        startTimeView.setText(aTimeSetting.getStartTime());
        // TAG*****
        startTimeView.setTag("startTimeView" + aTimeSetting.getId().toString());
        if (aTimeSetting.isOn()) {
            startTimeView.setTextAppearance(getActivity(), R.style.onTimeSetting);
        } else {
            startTimeView.setTextAppearance(getActivity(), R.style.offTimeSetting);

        }
        // text view 2 is the separator
        TextView separator = new TextView(getActivity());
        separator.setPadding(10, 10, 10, 10);
        separator.setText("-");
        // TAG*****
        separator.setTag("-" + aTimeSetting.getId().toString());
        if (aTimeSetting.isOn()) {
            separator.setTextAppearance(getActivity(), R.style.onTimeSetting);
        } else {
            separator.setTextAppearance(getActivity(), R.style.offTimeSetting);

        }

        // textviw 3 end time
        TextView endTimeView = new TextView(getActivity());
        endTimeView.setPadding(10, 10, 10, 10);
        endTimeView.setText(aTimeSetting.getEndTime());
        // TAG*****
        endTimeView.setTag("endTimeView" + aTimeSetting.getId().toString());
        if (aTimeSetting.isOn()) {
            endTimeView.setTextAppearance(getActivity(), R.style.onTimeSetting);
        } else {
            endTimeView.setTextAppearance(getActivity(), R.style.offTimeSetting);

        }

        // empty view to act as a separator
        // add a view to the table layout
        TextView randomText = new TextView(getActivity());
        randomText.setText("hghjgjhgjh");
        randomText.setTextColor(Color.BLACK);


        // toggle button is last
        ToggleButton tb = new ToggleButton(getActivity());
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

        // add these child view first table row
        aTableRow.addView(startTimeView);
        aTableRow.addView(separator);
        aTableRow.addView(endTimeView);
        aTableRow.addView(randomText);
        aTableRow.addView(tb);

        // add a view to the table layout
        View aLine = new View(getActivity());
        aLine.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
        aLine.setBackgroundColor(Color.parseColor("#444444"));

        // add a second view this will be the days of the week the time setting
        // is ON for
        TextView daysTimeSettingIsOn = new TextView(getActivity());
        daysTimeSettingIsOn.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
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
        aParentTableLayout.addView(aTableRow);
        aParentTableLayout.addView(aLine);
        aParentTableLayout.addView(daysTimeSettingIsOn);

        // appending
        aTimeSettingTabeLayout.addView(aParentTableLayout);
//         aTimeSettingTabeLayout.addView(aLine);
//         aTimeSettingTabeLayout.addView(daysTimeSettingIsOn);

    }

    private class mToggleButtonListener implements
            CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            if (isChecked) {
                // The toggle is enabled
                Intent i = new Intent()
                        .setAction("com.exmaple.jajingprototype.system.event.TIME_SETTING_ON");
                i.putExtra(TimeSettingReceiver.EXTRA_TIME_SETTING_ID,
                        Long.parseLong(buttonView.getTag().toString()));
                getActivity().sendBroadcast(i);
            } else {
                // The toggle is disabled
                Intent i = new Intent()
                        .setAction("com.exmaple.jajingprototype.system.event.TIME_SETTING_OFF");
                i.putExtra(TimeSettingReceiver.EXTRA_TIME_SETTING_ID,
                        Long.parseLong(buttonView.getTag().toString()));
                getActivity().sendBroadcast(i);

            }
            toggleTimeSettingView(buttonView, isChecked);

        }

    }

    private void toggleTimeSettingView(CompoundButton buttonView,
                                       boolean isChecked) {
        String aTimeSettingID = buttonView.getTag().toString();
        TableLayout aTableLayoutWithTimeSettingId = (TableLayout) dashboardView.findViewById(Integer
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
