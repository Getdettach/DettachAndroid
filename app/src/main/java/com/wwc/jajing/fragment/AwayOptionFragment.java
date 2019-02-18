package com.wwc.jajing.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.wwc.R;
import com.wwc.jajing.activities.AwayActivity;
import com.wwc.jajing.activities.Constants;
import com.wwc.jajing.activities.CustomAvailabilityStatus;
import com.wwc.jajing.activities.PlainAlertDialog;
import com.wwc.jajing.activities.SettimeDialog;
import com.wwc.jajing.activities.StatusActivity;
import com.wwc.jajing.activities.TimeSettingActivity;
import com.wwc.jajing.activities.callbacks.onUserActivitySelect;
import com.wwc.jajing.component.GetJSONResponse;
import com.wwc.jajing.domain.entity.CallerImpl;
import com.wwc.jajing.domain.entity.TimeSetting;
import com.wwc.jajing.domain.entity.User;
import com.wwc.jajing.domain.value.AvailabilityTime;
import com.wwc.jajing.interfaces.VolleyInterface;
import com.wwc.jajing.realmDB.ExtendTimeModel;
import com.wwc.jajing.realmDB.OptionsModel;
import com.wwc.jajing.realmDB.SetStatusModel;
import com.wwc.jajing.services.JJOnAwayService;
import com.wwc.jajing.settings.time.TimeSettingId;
import com.wwc.jajing.settings.time.TimeSettingValidator;
import com.wwc.jajing.system.JJSystemImpl;
import com.wwc.jajing.util.DateHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.wwc.jajing.system.JJApplication.getRealmInstance;

/**
 * Created by vivek_e on 9/12/2016.
 */
public class AwayOptionFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "AwayOptionsMenu";
    public static final String CUSTOM_MESSAGE = "CustomMessage";

    // public static final String AWAY_STATUS = "awayStatus";
    private User user = (User) JJSystemImpl.getInstance().getSystemService(
            JJSystemImpl.Services.USER);

    private String unavailabilityReason;
    private String customMessage;

    private LinearLayout driving;
    private LinearLayout family;
    private LinearLayout meeting;
    private LinearLayout crowded;
    private LinearLayout call;
    private LinearLayout custom_layout;
    private Button custom;
    private Button message;

    private String endTime;
    private String startTime,startTimeDB,endTimeDB;

    Date sDate, eDate;

    Long timeId = 1L;
    public static final int DIALOG_FRAGMENT = 1;

    private String pendingEndTime = "";
    private String pendingStartTime = "";

    private DialogFragment timeFrag;
    private RealmResults<OptionsModel> optionsModelRealmResults;
    SharedPreferences preferences;
    View dashboardView;

    private static AwayOptionFragment instance;

    public AwayOptionFragment() {
        super();
        instance = this;
    }




    public static AwayOptionFragment getInstance() {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        dashboardView = inflater.inflate(R.layout.activity_away_options, container, false);
        setCustomActionBar("Set Your Status");
        driving = (LinearLayout) dashboardView.findViewById(R.id.buttonDriving);
        family = (LinearLayout) dashboardView.findViewById(R.id.buttonFamily);
        meeting = (LinearLayout) dashboardView.findViewById(R.id.buttonMeeting);
        crowded = (LinearLayout) dashboardView.findViewById(R.id.buttonCrowded);
        call = (LinearLayout) dashboardView.findViewById(R.id.buttonImportantCall);
        custom_layout = (LinearLayout) dashboardView.findViewById(R.id.custom_layout);
        custom = (Button) dashboardView.findViewById(R.id.buttonCustom);
        message = (Button) dashboardView.findViewById(R.id.buttonCustomMessage);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        optionsModelRealmResults = getRealmInstance().where(OptionsModel.class).findAll();
        customOptions(optionsModelRealmResults);



        if(getActivity().getIntent().getStringExtra("statusScreen")!= null && getActivity().getIntent().getStringExtra("statusScreen").compareTo("statusScreen")==0){

            String id = getActivity().getIntent().getStringExtra("TIME_ID");
            String stateStart = getActivity().getIntent().getStringExtra("START_TIME");
            String stateEnd = getActivity().getIntent().getStringExtra("END_TIME");

// System.out.println("id === "+id+" startTime "+stateStart+" END "+stateEnd);

            custom.setVisibility(View.GONE);

        }else{
            custom.setVisibility(View.VISIBLE);
        }


// register click handlers
        registerClickHandlers();

        return dashboardView;
    }

    private void customOptions(RealmResults<OptionsModel> optionsModelRealmResults) {
        if (custom_layout.getChildCount() > 0)
            custom_layout.removeAllViews();

        LayoutInflater layoutInflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < optionsModelRealmResults.size(); i++) {
            final OptionsModel obj = optionsModelRealmResults.get(i);
            View view = layoutInflator.inflate(R.layout.inflator_custom, null);
            final TextView message_txt = (TextView) view.findViewById(R.id.message_txt);
            TextView description_txt = (TextView) view.findViewById(R.id.description_txt);

            message_txt.setText(obj.getMessage());
            description_txt.setText(obj.getDescription());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unavailabilityReason = message_txt.getText().toString();
                   // promptUserForTime(true);
                    SettimeDialog drivingFragment = new SettimeDialog();
                    drivingFragment.setCancelable(true);
                    drivingFragment.setTargetFragment(getActiveFragment(), DIALOG_FRAGMENT);
                    drivingFragment.show(getActivity().getSupportFragmentManager(), "CustomAvailabilityStatus");

                    SharedPreferences.Editor edit=preferences.edit();
                    edit.putString("status", unavailabilityReason);
                    edit.commit();
                    setCustomActionBar("Set Time");
                }
            });
            custom_layout.addView(view);
        }

    }

    public void disconnectCall() {
        try {

            String serviceManagerName = "android.os.ServiceManager";
            String serviceManagerNativeName = "android.os.ServiceManagerNative";
            String telephonyName = "com.android.internal.telephony.ITelephony";
            Class<?> telephonyClass;
            Class<?> telephonyStubClass;
            Class<?> serviceManagerClass;
            Class<?> serviceManagerNativeClass;
            Method telephonyEndCall;
            Object telephonyObject;
            Object serviceManagerObject;
            telephonyClass = Class.forName(telephonyName);
            telephonyStubClass = telephonyClass.getClasses()[0];
            serviceManagerClass = Class.forName(serviceManagerName);
            serviceManagerNativeClass = Class.forName(serviceManagerNativeName);
            Method getService = // getDefaults[29];
                    serviceManagerClass.getMethod("getService", String.class);
            Method tempInterfaceMethod = serviceManagerNativeClass.getMethod("asInterface", IBinder.class);
            Binder tmpBinder = new Binder();
            tmpBinder.attachInterface(null, "fake");
            serviceManagerObject = tempInterfaceMethod.invoke(null, tmpBinder);
            IBinder retbinder = (IBinder) getService.invoke(serviceManagerObject, "phone");
            Method serviceMethod = telephonyStubClass.getMethod("asInterface", IBinder.class);
            telephonyObject = serviceMethod.invoke(null, retbinder);
            telephonyEndCall = telephonyClass.getMethod("endCall");
            telephonyEndCall.invoke(telephonyObject);

        } catch (Exception e) {
            e.printStackTrace();
// Log.error(DialerActivity.this,
// "FATAL ERROR: could not connect to telephony subsystem");
// Log.error(DialerActivity.this, "Exception object: " + e);
        }
    }

    private void registerClickHandlers() {
        call.setOnClickListener(this);
        driving.setOnClickListener(this);
        family.setOnClickListener(this);
        meeting.setOnClickListener(this);
        crowded.setOnClickListener(this);
        custom.setOnClickListener(this);
        message.setOnClickListener(this);
    }

    /*
    * Used when user sets his custom message
    */
    @Override
    public void onStart() {
        super.onStart();
// Restore preferences
        SharedPreferences settings = getActivity().getSharedPreferences(CUSTOM_MESSAGE, 0);
        String customPrefs = settings.getString("custom", null);

        String customMessage = getActivity().getIntent().getStringExtra("custom");
        String customDescription = getActivity().getIntent().getStringExtra("description");
        if (customMessage != null) {
            this.customMessage = customMessage;
            this.saveCustomMessageToUserPref(customMessage);
            this.showCustomMessageButton(true);

        } else if (customPrefs != null) {
            this.customMessage = customPrefs;
            this.showCustomMessageButton(true);

        } else {
            this.showCustomMessageButton(false);

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
        {
            // After Ok code.
           // Toast.makeText(getActivity(),"Dialogcall",Toast.LENGTH_SHORT).show();
            setCustomActionBar("Set Your Status");
        } else if (resultCode == Activity.RESULT_CANCELED){
            // After Cancel code.
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.saveCustomMessageToUserPref(this.customMessage);
        getActivity().finish();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    private void saveCustomMessageToUserPref(String aCustomMessage) {

// We need an Editor object to make preference changes.
// All objects are from android.context.Context
        SharedPreferences settings = getActivity().getSharedPreferences(CUSTOM_MESSAGE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("custom", aCustomMessage);

// Commit the edits!
        editor.commit();
        Log.d(TAG, "saved custom message to user prefs.");
    }

    private void showCustomMessageButton(boolean shouldShow) {
        if (shouldShow) {
// show the button with the message te user has set
            this.message.setText(this.customMessage);
            this.message.setVisibility(View.VISIBLE);
        } else {
// hide the button
            this.message.setVisibility(View.GONE);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Toast.makeText(getActivity(),"Onresumecalled",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        //Toast.makeText(getActivity(),"Onpausecalled",Toast.LENGTH_SHORT).show();
    }

    /*
            * Makes user "unavailable"
            */
    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.buttonDriving:
                this.unavailabilityReason = "Driving/Metro";
                //this.promptUserForTime(true);
                SettimeDialog drivingFragment = new SettimeDialog();
                drivingFragment.setCancelable(true);
                drivingFragment.setTargetFragment(this, DIALOG_FRAGMENT);
                drivingFragment.show(getActivity().getSupportFragmentManager(), "CustomAvailabilityStatus");

                SharedPreferences.Editor edit=preferences.edit();
                edit.putString("status", "Driving/Metro");
                edit.commit();
                setCustomActionBar("Set Time");
                //getActionBar().setTitle("Set Time");
                break;
            case R.id.buttonFamily:
                this.unavailabilityReason = "With Family";
                //this.promptUserForTime(true);
                SettimeDialog familyFragment = new SettimeDialog();
                familyFragment.setCancelable(true);
                familyFragment.setTargetFragment(this, DIALOG_FRAGMENT);
                familyFragment.show(getActivity().getSupportFragmentManager(), "CustomAvailabilityStatus");

                SharedPreferences.Editor editfamily=preferences.edit();
                editfamily.putString("status", "With Family");
                editfamily.commit();
                setCustomActionBar("Set Time");

                break;
            case R.id.buttonMeeting:
                this.unavailabilityReason = "In Class/Crowded Place";
                //this.promptUserForTime(true);
                SettimeDialog meetingFragment = new SettimeDialog();
                meetingFragment.setCancelable(true);
                meetingFragment.setTargetFragment(this, DIALOG_FRAGMENT);
                meetingFragment.show(getActivity().getSupportFragmentManager(), "CustomAvailabilityStatus");

                SharedPreferences.Editor editmeeting=preferences.edit();
                editmeeting.putString("status", "In Class/Crowded Place");
                editmeeting.commit();
                setCustomActionBar("Set Time");
                break;
            case R.id.buttonImportantCall:
                this.unavailabilityReason = "At Work/On the Phone";
               // this.promptUserForTime(true);
                SettimeDialog importantFragment = new SettimeDialog();
                importantFragment.setCancelable(true);
                importantFragment.setTargetFragment(this, DIALOG_FRAGMENT);
                importantFragment.show(getActivity().getSupportFragmentManager(), "CustomAvailabilityStatus");

                SharedPreferences.Editor editimportantcall=preferences.edit();
                editimportantcall.putString("status", "At Work/On the Phone");
                editimportantcall.commit();
                setCustomActionBar("Set Time");
                break;
            case R.id.buttonCustomMessage:
                this.unavailabilityReason = this.customMessage.toUpperCase();
                //this.promptUserForTime(true);
                SettimeDialog customFragment = new SettimeDialog();
                customFragment.setCancelable(true);
                customFragment.setTargetFragment(this, DIALOG_FRAGMENT);
                customFragment.show(getActivity().getSupportFragmentManager(), "CustomAvailabilityStatus");

                SharedPreferences.Editor editcustom=preferences.edit();
                editcustom.putString("status", customMessage);
                editcustom.commit();
                setCustomActionBar("Set Time");
                break;
            case R.id.buttonCustom:
                CustomAvailabilityStatus menuFragment = new CustomAvailabilityStatus(this);
                menuFragment.setCancelable(true);
                menuFragment.setTargetFragment(this, DIALOG_FRAGMENT);
                menuFragment.show(getActivity().getSupportFragmentManager(), "CustomAvailabilityStatus");
                setCustomActionBar("Set Time");

                break;
            default:
                user.goAvailable();

        }

        if(getActivity().getIntent().getStringExtra("statusScreen") == null){
            Constants.statusName = this.unavailabilityReason;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void promptUserForTime(boolean isStartTime) {

        if(getActivity().getIntent().getStringExtra("statusScreen")!= null && getActivity().getIntent().getStringExtra("statusScreen").compareTo("statusScreen")==0){

            Constants.statusName = unavailabilityReason;

            startTime = getActivity().getIntent().getStringExtra("START_TIME");
            endTime = getActivity().getIntent().getStringExtra("END_TIME");

            String id = getActivity().getIntent().getStringExtra("TIME_ID");

            TimeSetting timeSetting = TimeSetting.findById(TimeSetting.class, Long.parseLong(id));

            timeSetting.setStatus(unavailabilityReason);
            timeSetting.save();

            goUnavailable(Long.parseLong(id));

            Intent dashIntent = new Intent(getActivity(), StatusActivity.class);
            startActivity(dashIntent);
            getActivity().finish();

        }else{
            timeFrag = new mTimePicker(isStartTime);
            timeFrag.setRetainInstance(true);
            timeFrag.show(getActivity().getFragmentManager(), "timePicker");
        }


    }

    public void setStartTime(String aStartTime) {
        if (!this.pendingStartTime.equalsIgnoreCase(aStartTime)) {
            this.pendingStartTime = aStartTime;
            this.startTime = this.pendingStartTime;
        }

        removeFragment();
    }

    private void removeFragment() {
        timeFrag.dismiss();
        promptUserForTime(false);// prompt for end time
    }

    public void setEndTime(String anEndTime) {
//BUG THAT SUBMITS THE CURRENT TIME AS END TIME
        if (!this.pendingEndTime.equalsIgnoreCase(anEndTime) && !anEndTime.equalsIgnoreCase("")) {
            this.pendingEndTime = anEndTime;
            this.endTime = pendingEndTime;
// Toast.makeText(getActivity(), "end time set to : " + pendingEndTime, Toast.LENGTH_SHORT).show();
        } else {
// stop code execution, there is a BUG with android calling
// onSetTimeTwice, this is a temporary fix.
// Toast.makeText(getActivity(), "could not set end time: " + anEndTime, Toast.LENGTH_SHORT).show();
            this.pendingEndTime = "";
            return;
        }
//
        if (!TimeSetting.isEndTimeInFuture(anEndTime)) {
        // Toast.makeText(getActivity(), "end time must be in future", Toast.LENGTH_SHORT).show();

         Snackbar.make(dashboardView, "end time must be in future", Snackbar.LENGTH_LONG).show();

        return;
        }

         if (!TimeSetting.isValidTimeInterval(this.startTime, pendingEndTime)) {
        // Toast.makeText(getActivity(), "invalid interval", Toast.LENGTH_SHORT).show();
        Snackbar.make(dashboardView, "invalid interval", Snackbar.LENGTH_LONG).show();
        return;
        }


        ArrayList<TimeSetting> interferingTimeSettings = TimeSettingValidator
                .getTimeSettingsThisEndTimeInterferesWith(anEndTime);


// System.out.println("interferingTimeSettings = " + interferingTimeSettings.size());

// POSSIBLE BUG HERE... should not compare size, was used as a temp fix

        if (interferingTimeSettings.size() < 1) {// check to make sure its set

            if(Constants.haveNetworkConnection(getActivity()))
            {
                navigateToAway(1L);
            }else{
//                Snackbar.make()

                Toast.makeText(getActivity(),"No Internet Connection Available",Toast.LENGTH_LONG).show();

            }



        } else {
            PlainAlertDialog
                    .alertUser(
                            getActivity(),
                            "Sorry",
                            "This time interferes with your time setting(s), turn them off?",
                            new onUserActivitySelect(
                                    getInterferingTimeSettingIds(interferingTimeSettings),
                                    this.endTime, this.unavailabilityReason),
                            true);
        }

    }

    private void goUnavailable(Long id) {

        this.user.goUnavailable(this.unavailabilityReason,
                this.startTime, new AvailabilityTime(this.endTime),id);



    }

    private void navigateToAway(Long timeSettingId) {

        Log.d(TAG,"availability time is now:"+ new AvailabilityTime(this.endTime).getAvailabilityTimeString());

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        startTimeDB = df.format(c.getTime()) + " " + startTime;
        endTimeDB = df.format(c.getTime()) + " " + endTime;


        Map<String, String> params = new HashMap<>();

        params.put("DetachUserStatusID", "");
        params.put("AccessToken",Constants.accessToken);
        params.put("StartTime",startTimeDB);
        params.put("EndTime", endTimeDB);
        params.put("Status", unavailabilityReason);
        params.put("TimeID", timeId.toString());
        params.put("IsActive", "0");

        new GetJSONResponse(getActivity()).RequestJsonToServer(getActivity(), "DetachStatus", params, new VolleyInterface() {

            @Override
            public void onSuccessResponse(final JSONObject result) {

                System.out.println("setTimeSettingSuccess = " + result);

                setTimeSettingSuccess(result);

            }

            @Override
            public void onErroeResponse(VolleyError result) {

                System.out.println("ERROE IN SPLASH "+result);

            }

            @Override
            public void onSuccessResponse(JSONArray result) {

            }
        });

    }


    public void setTimeSettingSuccess(final JSONObject result){

        System.out.println("setTimeSettingSuccess = " + result);

        TimeSetting timeSetting = new TimeSetting(getContext(), startTime, endTime, null, this.unavailabilityReason,1);
        timeSetting.save();

        List<TimeSetting> timeList = TimeSetting.listAll(TimeSetting.class);

        for (TimeSetting aTimeSetting : timeList) {

            if(aTimeSetting.getId() != 1 && aTimeSetting.isOn())
            {
                TimeSetting aTimeSettings = TimeSetting.findById(TimeSetting.class, aTimeSetting.getId());

                timeId = aTimeSettings.getId();

                break;
            }

        }

        List<CallerImpl> callerList = CallerImpl.listAll(CallerImpl.class, "allow_Permission=1");

        if(callerList.size()>0){

            for(int i = 0;i<callerList.size();i++){

                CallerImpl callerperm = callerList.get(i);
                callerperm.setAllowPermission(0);
                callerperm.save();
            }

        }


        goUnavailable(timeId);

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
        sDate=null ;
        eDate =null;

        try{
            sDate = formatter.parse(startTimeDB);
            eDate = formatter.parse(endTimeDB);
        }catch(Exception e){
            e.printStackTrace();
        }

        getRealmInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.clear(SetStatusModel.class);
            }
        });

        getRealmInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                SetStatusModel setStatusModel = realm.createObject(SetStatusModel.class);

                try {

                    JSONObject jsonResponse = new JSONObject(result.toString());
                    setStatusModel.setStatusId(jsonResponse.getString("DetachUserStatusID"));
                    setStatusModel.setStatusName(unavailabilityReason);
                    setStatusModel.setTimeId(timeId.toString());
                    setStatusModel.setStartTime(sDate);
                    setStatusModel.setEndTime(eDate);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        getActivity().stopService(new Intent(getActivity(), JJOnAwayService.class));
        getActivity().startService(new Intent(getActivity(), JJOnAwayService.class));


        getRealmInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ExtendTimeModel extendTimeModel = realm.createObject(ExtendTimeModel.class); // Create managed objects directly
                if (extendTimeModel != null) {
                    extendTimeModel.setTimeId(timeId.toString());
                    extendTimeModel.setStartId("0");
                }
            }
        });


        Intent i = new Intent(getActivity(), StatusActivity.class);
        this.startActivity(i);
        getActivity().finish();


    }

    private ArrayList<TimeSettingId> getInterferingTimeSettingIds(
            ArrayList<TimeSetting> interferingTimeSettings) {

        ArrayList<TimeSettingId> listOfTimeSettingIds = new ArrayList<TimeSettingId>();
        for (TimeSetting aTimeSetting : interferingTimeSettings) {
            listOfTimeSettingIds.add(new TimeSettingId(aTimeSetting.getId()));
        }
        return listOfTimeSettingIds;
    }
}