package com.wwc.jajing.fragment;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.wwc.R;
import com.wwc.jajing.activities.Constants;
import com.wwc.jajing.activities.MyTimeSettings;
import com.wwc.jajing.activities.RepeatStatusActivity;
import com.wwc.jajing.activities.StatusActivity;
import com.wwc.jajing.domain.entity.TimeSetting;
import com.wwc.jajing.domain.entity.User;
import com.wwc.jajing.domain.value.AvailabilityTime;
import com.wwc.jajing.realmDB.RepeatDaysModel;
import com.wwc.jajing.realmDB.TimeModel;
import com.wwc.jajing.receivers.TimeSettingReceiver;
import com.wwc.jajing.services.JJOnAwayService;
import com.wwc.jajing.settings.time.TimeSettingId;
import com.wwc.jajing.settings.time.TimeSettingTaskManager;
import com.wwc.jajing.settings.time.TimeSettingValidator;
import com.wwc.jajing.system.JJSystemImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;

import static com.wwc.jajing.system.JJApplication.getRealmInstance;

/**
 * Created by Vivek on 18-09-2016.
 */
public class TimeSettingFragment extends BaseFragment {


    private static final String TAG = "TimeSettings";

    public static String startTime;
    public static String endTime;
    public String status;
    public long timeId;

    private TextView buttonStartTime,buttonEndTime,buttonSetStatus;
    private static TextView textStartDisplay;
    private static TextView textEndDisplay;
    private Button buttonSaveTimeSetting;

    private CheckBox checkboxRepeat;
    private CheckBox checkboxEveryDay;

    private TableLayout tableDaysOfTheWeek;

    TimeModel timeModel;

    String repeatFlag="0";

    User user;
    TimeSetting timeSetting;
    private HashMap<TimeSetting.Days, Boolean> daysToRepeatCollection = new HashMap<TimeSetting.Days, Boolean>();

    private int idOfCheckboxChecked;
    static View dashboardView;

    private DialogFragment timeFrag;

    private static TimeSettingFragment instance;

    private List<TimeSetting> aListOfTimeSettings;

    private TableLayout aTimeSettingTabeLayout;

    TimeSetting.Days[] days;

    TextView textMon,textSun,awayStatus,textTu,textWed,textThu,textFri,textSat;





    public TimeSettingFragment() {
        super();
        instance = this;
    }

    public static TimeSettingFragment getInstance() {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        dashboardView = inflater.inflate(R.layout.activity_time_picker, container, false);
        setCustomActionBar("Time Setting");
        this.buttonStartTime = (TextView) dashboardView.findViewById(R.id.buttonStartTime);
        this.textStartDisplay = (TextView) dashboardView.findViewById(R.id.textStartDisplay);
        this.textEndDisplay = (TextView) dashboardView.findViewById(R.id.textEndDisplay);
        this.buttonEndTime = (TextView) dashboardView.findViewById(R.id.buttonEndTime);
        this.buttonSetStatus = (TextView) dashboardView.findViewById(R.id.button_set_status);
        this.checkboxRepeat = (CheckBox) dashboardView.findViewById(R.id.checkboxRepeat);
        this.checkboxEveryDay = (CheckBox) dashboardView.findViewById(R.id.checkboxEveryDay);
        this.buttonSaveTimeSetting = (Button) dashboardView.findViewById(R.id.buttonSaveTimeSetting);
        textMon = (TextView) dashboardView.findViewById(R.id.textMon);
        awayStatus = (TextView) dashboardView.findViewById(R.id.textStatus);
        textTu = (TextView) dashboardView.findViewById(R.id.textTu);
        textWed = (TextView) dashboardView.findViewById(R.id.textWed);
        textThu = (TextView) dashboardView.findViewById(R.id.textThu);
        textFri = (TextView) dashboardView.findViewById(R.id.textFri);
        textSat = (TextView) dashboardView.findViewById(R.id.textSat);
        textSun = (TextView) dashboardView.findViewById(R.id.textSun);

        this.tableDaysOfTheWeek = (TableLayout) dashboardView.findViewById(R.id.tableDaysOfTheWeek);


        if (getActivity().getIntent() != null) {

            startTime = getActivity().getIntent().getStringExtra("START_TIME");

            endTime = getActivity().getIntent().getStringExtra("END_TIME");

            status = getActivity().getIntent().getStringExtra("STATUS");

            timeId = getActivity().getIntent().getLongExtra("TIME_ID",1L);

        }

        user = (User) JJSystemImpl.getInstance().getSystemService(
                JJSystemImpl.Services.USER);

      //  timeSetting = TimeSetting.findById(TimeSetting.class, this.getIntent().getLongExtra(EXTRA_KEY_TIME_SETTING_ID, 1L));
//        Log.d(TAG, TimeSettingTaskManager.getInstance().getTimeSettingIdClosestToBeingDone().toString());

//        System.out.println("time timeId = " + timeId);
//        System.out.println("time status = " + status);
//        System.out.println("time endTime = " + endTime);


        List<TimeSetting> timeList = TimeSetting.listAll(TimeSetting.class);

        for (TimeSetting aTimeSetting : timeList) {

            if(aTimeSetting.getId() != 1 && aTimeSetting.isOn())
            {
                TimeSetting aTimeSettings = TimeSetting.findById(TimeSetting.class, aTimeSetting.getId());
//                System.out.println(" is ON = " + aTimeSettings.getId());

                timeId = aTimeSettings.getId();

                break;
            }
        }

        timeSetting = TimeSetting.findById(TimeSetting.class, timeId);

        if(user.getUserStatus().getAvailabilityStatus().compareTo("AVAILABLE")==0 && status == null ){

            startTime = " You have not chosen a start time.";
            endTime = " You have not chosen an end time.";

            buttonSetStatus.setVisibility(View.VISIBLE);

        } else if(user.getUserStatus().getAvailabilityStatus().compareTo("AVAILABLE")!=0 && status == null){

//            System.out.println("start = " + startTime);
//            System.out.println("endTime = " + endTime);

            status = user.getUserStatus().getAvailabilityStatus();

            startTime = timeSetting.getStartTime();
            endTime = timeSetting.getEndTime();

            RealmResults<RepeatDaysModel> repeatDays = getRealmInstance().where(RepeatDaysModel.class).equalTo("id", String.valueOf(timeId)).findAll();

//            System.out.println("repeatDays = " + repeatDays);
//            System.out.println("repeatDays.get(0).getDays() = " + repeatDays.get(0).getDays());

            if(repeatDays.size()>0 && repeatDays.get(0).getIdFlag().compareTo("1")==0){
                checkboxEveryDay.setChecked(true);
                checkboxRepeat.setChecked(false);
            }else if(repeatDays.size()>0 && repeatDays.get(0).getIdFlag().compareTo("2")==0){
                checkboxEveryDay.setChecked(false);
                checkboxRepeat.setChecked(true);

                checkboxHandler(checkboxRepeat);
            }

            buttonSetStatus.setVisibility(View.GONE);

        }else{

            RealmResults<RepeatDaysModel> repeatDays = getRealmInstance().where(RepeatDaysModel.class).equalTo("id", String.valueOf(timeId)).findAll();
//
//            System.out.println("repeatDays = " + repeatDays);
//            System.out.println("repeatDays.get(0).getDays() = " + repeatDays.get(0).getDays());
            

            if(repeatDays.size()>0 && repeatDays.get(0).getIdFlag().compareTo("1")==0){
                checkboxEveryDay.setChecked(true);
                checkboxRepeat.setChecked(false);
            }else if(repeatDays.size()>0 && repeatDays.get(0).getIdFlag().compareTo("2")==0){
                checkboxEveryDay.setChecked(false);
                checkboxRepeat.setChecked(true);

                checkboxHandler(checkboxRepeat);
            }

            buttonSetStatus.setVisibility(View.GONE);
        }

        displayStartTimeToUser(startTime);
        displayEndTimeToUser(endTime);

        awayStatus.setText(status);

        buttonSaveTimeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Your Time is set.", Snackbar.LENGTH_LONG).show();
                saveTimeSetting(buttonSaveTimeSetting);
            }
        });

        buttonStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showTimePickerDialog(buttonStartTime);

            if(user.getUserStatus().getAvailabilityStatus().compareTo("AVAILABLE")==0 && status==null)
            {
                timeFrag = new mTimePickerTimeSettings(true);
            }else{
                timeFrag = new mTimePickerTimeSettings(true,startTime,endTime);
            }


            timeFrag.setRetainInstance(true);
            timeFrag.show(getActivity().getFragmentManager(), "timePicker");

            }
        });
        buttonEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showTimePickerDialog(buttonEndTime);

            if(user.getUserStatus().getAvailabilityStatus().compareTo("AVAILABLE")==0 && status ==null)
            {
                timeFrag = new mTimePickerTimeSettings(false);
            }else{
                timeFrag = new mTimePickerTimeSettings(false,startTime,endTime);
            }
            timeFrag.setRetainInstance(true);
            timeFrag.show(getActivity().getFragmentManager(), "timePicker");
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

        timeModel = new TimeModel();


        this.buttonSetStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent reaptInt = new Intent(getActivity(),RepeatStatusActivity.class);
                startActivity(reaptInt);
            }
        });

//        System.out.println("Constants.startTime " + timeSetting.getStartTime() +" end time "+timeSetting.getEndTime() +"user.getUserStatus().getAvailabilityStatus() "+user.getUserStatus().getAvailabilityStatus());

        return dashboardView;
    }

    public void continueHome() {

        goUnavailable();

//        getActivity().stopService(new Intent(getActivity(),JJOnAwayService.class));
//
//        getActivity().startService(new Intent(getActivity(), JJOnAwayService.class));

        Intent i = new Intent(getActivity(), StatusActivity.class);
        this.startActivity(i);
        getActivity().finish();
    }

    private void goUnavailable() {

//        if(timeId == 1){
//
//            this.user.goUnavailable(this.status, this.startTime, new AvailabilityTime(this.endTime));

            List<TimeSetting> timeList = TimeSetting.listAll(TimeSetting.class);

            for (TimeSetting aTimeSetting : timeList) {

                if(aTimeSetting.getId() != 1 && aTimeSetting.isOn())
                {
                    TimeSetting aTimeSettings = TimeSetting.findById(TimeSetting.class, aTimeSetting.getId());
//                    System.out.println("is ON TimeSettings = " + aTimeSettings.getId());

                    timeId = aTimeSettings.getId();

                    break;
                }

            }

//            this.user.goUnavailable(this.status, this.startTime, new AvailabilityTime(this.endTime),timeId);

        this.user.goUnavailable(this.status, this.startTime, new AvailabilityTime(this.endTime),days,timeId,repeatFlag);

//        }

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

        RealmResults<RepeatDaysModel> repeatDays = getRealmInstance().where(RepeatDaysModel.class).equalTo("id", String.valueOf(timeId)).findAll();

        this.daysToRepeatCollection.put(TimeSetting.Days.SUNDAY, false);
        this.daysToRepeatCollection.put(TimeSetting.Days.MONDAY, false);
        this.daysToRepeatCollection.put(TimeSetting.Days.TUESDAY, false);
        this.daysToRepeatCollection.put(TimeSetting.Days.WEDNESDAY, false);
        this.daysToRepeatCollection.put(TimeSetting.Days.THURSDAY, false);
        this.daysToRepeatCollection.put(TimeSetting.Days.FRIDAY, false);
        this.daysToRepeatCollection.put(TimeSetting.Days.SATURDAY, false);

        if(repeatDays.size()>0 && repeatDays.get(0).getIdFlag().compareTo("2")==0){

            setDays(repeatDays);

        }

    }



    public void setDays(RealmResults<RepeatDaysModel> repeatDays){

        String[] days = {"SUN","MON","TUE","WED","THU","FRI","SAT"};

        String[] dayName = repeatDays.get(0).getDays().split(" | ");

        for(int i= 0;i<dayName.length;i++){
            for(int j=0;j<days.length;j++){
                if(days[j].startsWith(dayName[i])){

                    switch (dayName[i])
                    {
                        case "SUN":
                            this.daysToRepeatCollection.put(TimeSetting.Days.SUNDAY, false);
                            toggleDayToRepeat(textSun);
                            break;
                        case "MON":
                            this.daysToRepeatCollection.put(TimeSetting.Days.MONDAY, false);
                            toggleDayToRepeat(textMon);
                            break;
                        case "TUE":
                            this.daysToRepeatCollection.put(TimeSetting.Days.TUESDAY, false);
                            toggleDayToRepeat(textTu);
                            break;
                        case "WED":
                            this.daysToRepeatCollection.put(TimeSetting.Days.WEDNESDAY, false);
                            toggleDayToRepeat(textWed);
                            break;
                        case "THU":
                            this.daysToRepeatCollection.put(TimeSetting.Days.THURSDAY, false);
                            toggleDayToRepeat(textThu);
                            break;
                        case "FRI":
                            this.daysToRepeatCollection.put(TimeSetting.Days.FRIDAY, false);
                            toggleDayToRepeat(textFri);
                            break;
                        case "SAT":
                            this.daysToRepeatCollection.put(TimeSetting.Days.SATURDAY, false);
                            toggleDayToRepeat(textSat);
                            break;
                    }
                }
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
            tv.setTextColor(Color.parseColor("#ff33b5e5"));

        } else {
            //day is not set to repeat, do this...
            tv.setTextColor(Color.parseColor("#FFBB34"));

        }

    }

    /*
     * When we set a start time, we need to make sure its a valid time, and its before the end time
     */
    public static void setStartTime(String aStartTime) {
        if (aStartTime.equalsIgnoreCase(endTime)) {
            Snackbar.make(dashboardView, "choose a different start time", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (isEndTimeSet() && !TimeSetting.isValidTimeInterval(aStartTime, endTime)) {

            Snackbar.make(dashboardView, "must be a valid time interval", Snackbar.LENGTH_SHORT).show();
            return;

        }

        startTime = aStartTime;

        displayStartTimeToUser(startTime);
    }

    public static boolean isStartTimeSet() {

        if (startTime != null && !startTime.contains("You have not chosen a start time"))
            return true;
        else
            return false;
    }

    public static boolean isEndTimeSet() {
        if (endTime != null && !endTime.contains("You have not chosen an end time"))
            return true;
        else
            return false;
    }

    public void setEndTime(String anEndTime) {
        if (anEndTime.equalsIgnoreCase(startTime)) {
            Snackbar.make(dashboardView, "choose a different end time", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (isStartTimeSet() && !TimeSetting.isValidTimeInterval(startTime, anEndTime)) {
            Snackbar.make(dashboardView, "must be a valid time interval", Snackbar.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, endTime + ":" + startTime);

        endTime = anEndTime;

        displayEndTimeToUser(endTime);

        ArrayList<TimeSetting.Days> daysList = new ArrayList<TimeSetting.Days>();

//        daysList.add(TimeSetting.Days.MONDAY);

        TimeSetting.Days[] daysToRepeat = daysList.toArray(new TimeSetting.Days[daysList.size()]);
        //create a new time setting
//        TimeSetting ts = new TimeSetting(getActivity(), startTime, endTime, daysToRepeat,this.user.getUserStatus().getAvailabilityStatus());

    }
    public static void displayStartTimeToUser(String aFormattedTime) {
        textStartDisplay.setText("Start Time: " + aFormattedTime);
    }

    public static void displayEndTimeToUser(String aFormattedTime) {
        textEndDisplay.setText("End Time: " + aFormattedTime);
    }


    public void saveTimeSetting(View view) {

//        System.out.println("startTime = " + startTime);
//        System.out.println("endTime = " + endTime);

        if (!this.isStartTimeSet() || !this.isEndTimeSet()) {
            Snackbar.make(dashboardView, "select both a start and end time", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (!TimeSetting.isEndTimeInFuture(endTime)) {

            Snackbar.make(dashboardView, "end time must be in future", Snackbar.LENGTH_LONG).show();

            return;
        }

        if (this.idOfCheckboxChecked == R.id.checkboxEveryDay) {

            days = new TimeSetting.Days[]{
                    TimeSetting.Days.MONDAY, TimeSetting.Days.TUESDAY, TimeSetting.Days.WEDNESDAY, TimeSetting.Days.THURSDAY, TimeSetting.Days.FRIDAY, TimeSetting.Days.SATURDAY, TimeSetting.Days.SUNDAY};


//            System.out.println("allDaysOfTheWeek = " + days);
//
//            System.out.println("time Id " +timeId);

            if(timeId != 1){
                timeSetting = TimeSetting.findById(TimeSetting.class, timeId);
                timeSetting.setStartTime(startTime);
                timeSetting.setEndTime(endTime);
                timeSetting.setStatus(status);
            }
            else{
                timeSetting = new TimeSetting(getContext(), startTime, endTime, days,status,1);
            }

            repeatFlag = "1";



        } else if (this.idOfCheckboxChecked == R.id.checkboxRepeat) {

            days = this.getDaysToRepeat();

//            System.out.println("daysToRepeat = " + days);

            if(timeId != 1){
                timeSetting = TimeSetting.findById(TimeSetting.class, timeId);
                timeSetting.setStartTime(startTime);
                timeSetting.setEndTime(endTime);
                timeSetting.setStatus(status);
            }
            else {

                timeSetting= new TimeSetting(getContext(), startTime, endTime, days, status,1);
            }

            repeatFlag="2";

        } else {

            if(timeId != 1){
                timeSetting = TimeSetting.findById(TimeSetting.class, timeId);
                timeSetting.setStartTime(startTime);
                timeSetting.setEndTime(endTime);
                timeSetting.setStatus(status);
            }
            else {
                timeSetting = new TimeSetting(getContext(), startTime, endTime, null, status,1);
            }

            repeatFlag = "0";

            Snackbar.make(dashboardView, "select a frequency", Snackbar.LENGTH_SHORT).show();
        }

//        System.out.println("timeId = " + timeId);
        

        if(user.getUserStatus().getAvailabilityStatus().compareTo("AVAILABLE")!=0 || status != null){

            timeSetting.save();


//            System.out.println("PRINT FOR DAYS "+timeSetting.hasTimeSettingForToday());

            continueHome();

        }
        else{
            Snackbar.make(dashboardView, "select valid status", Snackbar.LENGTH_SHORT).show();
        }

    }

    private TimeSetting.Days[] getDaysToRepeat() {
        ArrayList<TimeSetting.Days> daysList = new ArrayList<TimeSetting.Days>();
        for (Map.Entry<TimeSetting.Days, Boolean> entry : this.daysToRepeatCollection.entrySet()) {
            TimeSetting.Days aDay = entry.getKey();
            Boolean shouldRepeat = entry.getValue();

            if (shouldRepeat) {
                daysList.add(aDay);
            }

        }

        return daysList.toArray(new TimeSetting.Days[daysList.size()]);

    }

    /*
     * To link user to his time settings page we have to know if he added a new time setting, or not.
     * So we can correctly scroll him to the the time setting he just recently added
     */
    private void moveToMyTimeSettings(boolean addedTimeSetting) {
        Intent i = new Intent(getActivity(), MyTimeSettings.class);

        if (addedTimeSetting) {
            i.putExtra("addedTimeSetting", true);
        }
        this.startActivity(i);
    }

}
