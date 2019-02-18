package com.wwc.jajing.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.wwc.R;
import com.wwc.jajing.activities.AwayOptionActivity;
import com.wwc.jajing.activities.Constants;
import com.wwc.jajing.activities.DashboardActivity;
import com.wwc.jajing.activities.LogActivity;
import com.wwc.jajing.activities.PlainAlertDialog;
import com.wwc.jajing.activities.SetStatusActivity;
import com.wwc.jajing.activities.SettimeDialog;
import com.wwc.jajing.activities.StatusActivity;
import com.wwc.jajing.activities.TimeSettingActivity;
import com.wwc.jajing.activities.callbacks.onUserActivitySelect;
import com.wwc.jajing.component.GetJSONResponse;
import com.wwc.jajing.domain.entity.TimeSetting;
import com.wwc.jajing.domain.entity.User;
import com.wwc.jajing.domain.value.AvailabilityTime;
import com.wwc.jajing.domain.value.UserStatus;
import com.wwc.jajing.interfaces.VolleyInterface;
import com.wwc.jajing.listeners.CSLonUnavailable;
import com.wwc.jajing.realmDB.CallsModel;
import com.wwc.jajing.realmDB.ContactModel;
import com.wwc.jajing.realmDB.ExtendTimeModel;
import com.wwc.jajing.realmDB.MessageModel;
import com.wwc.jajing.realmDB.RepeatDaysModel;
import com.wwc.jajing.realmDB.SetStatusModel;
import com.wwc.jajing.realmDB.TimeModel;
import com.wwc.jajing.services.JJOnAwayService;
import com.wwc.jajing.settings.time.TimeSettingTaskManager;
import com.wwc.jajing.settings.time.TimeSettingValidator;
import com.wwc.jajing.system.JJSystem;
import com.wwc.jajing.system.JJSystemImpl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
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

import static com.wwc.jajing.sms.JJSMS.TAG;
import static com.wwc.jajing.system.JJApplication.getRealmInstance;

/**
 * Created by Vivek on 25-09-2016.
 */
public class StatusFragment extends BaseFragment implements View.OnClickListener {

    private Button btnStatusChange, btnExtendtime, btnReset, btnOk, btnCancel,
            btnFive,btnTen,btnFifteen,btnOneHour,btnTwoHour;

    TextView txtStatus, txtEndTime;
    private RelativeLayout layoutEmpty,layoutStatus,layoutExtendTime;

    View dashboardView;
    private User user;

    ImageView imgCloseIcon;

    String statusName,startTimeSDB,endTimeSDB;

    String id;

    TimeSetting timeSetting;

    Long timeId = 1L;

    boolean allowCal = true;
    private DialogFragment timeFrag;

    String   str_timewithdate;
    int addVal;
    final Handler handler = new Handler();

    private static StatusFragment instance;

    public StatusFragment() {
        super();
        instance = this;
    }

    public static StatusFragment getInstance() {
        return instance;
    }

    SharedPreferences preferences;
//    preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);

        dashboardView = inflater.inflate(R.layout.activity_status_remainder, container, false);
        setCustomActionBar("Status");
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        btnStatusChange = (Button) dashboardView.findViewById(R.id.button_Chane_status);
        btnExtendtime = (Button) dashboardView.findViewById(R.id.button_extend_time);
        btnReset = (Button) dashboardView.findViewById(R.id.button_reset);
        txtStatus = (TextView) dashboardView.findViewById(R.id.textStatus);
        txtEndTime = (TextView) dashboardView.findViewById(R.id.textEndTime);

        layoutEmpty = (RelativeLayout)dashboardView.findViewById(R.id.layout_empty);
        layoutStatus = (RelativeLayout)dashboardView.findViewById(R.id.layout_status);

        layoutExtendTime = (RelativeLayout)dashboardView.findViewById(R.id.layout_extend);

        btnFive = (Button) dashboardView.findViewById(R.id.btn_five);
        btnTen = (Button) dashboardView.findViewById(R.id.btn_ten);
        btnFifteen = (Button) dashboardView.findViewById(R.id.btn_fifteen);
        btnOneHour = (Button) dashboardView.findViewById(R.id.btn_one_hour);
        btnTwoHour = (Button) dashboardView.findViewById(R.id.btn_two_hour);

        imgCloseIcon = (ImageView)dashboardView.findViewById(R.id.close_icon);

        imgCloseIcon.setVisibility(View.GONE);

        JJSystem jjSystem = JJSystemImpl.getInstance();
        this.user = (User) jjSystem.getSystemService(JJSystemImpl.Services.USER);

        setValues();
        monitorStatus();

        return dashboardView;
    }

    public void setValues(){

        layoutExtendTime.setVisibility(View.GONE);

        List<TimeSetting> timeList = TimeSetting.listAll(TimeSetting.class);

        for (TimeSetting aTimeSetting : timeList) {

            if(aTimeSetting.getId() != 1 && aTimeSetting.isOn())
            {
                TimeSetting aTimeSettings = TimeSetting.findById(TimeSetting.class, aTimeSetting.getId());

                timeId = aTimeSettings.getId();

                break;
            }

        }

        timeSetting = TimeSetting.findById(TimeSetting.class, timeId);

        if(SettimeDialog.bl_status)
        {
            statusName = preferences.getString("status"," ");
        }else if(MenuTimeSettingFragment.bl_quicktime)
        {
            statusName="Busy";

        }
        else
        {
            statusName = this.user.getUserStatus().getAvailabilityStatus();
        }

        //statusName = this.user.getUserStatus().getAvailabilityStatus();

        Realm realm = Realm.getInstance(getActivity());

        final RealmResults<SetStatusModel> setStatusModel = realm.where(SetStatusModel.class).findAll();

        if(setStatusModel.size()>0){
            id = setStatusModel.get(0).getStatusId();

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");

            startTimeSDB = formatter.format(setStatusModel.get(0).getStartTime());
            endTimeSDB = formatter.format(setStatusModel.get(0).getEndTime());

            Log.e("StatusFragment","startTimeSDB=="+startTimeSDB);
            Log.e("StatusFragment","endTimeSDB=="+endTimeSDB);

        }

        realm.close();


        if(statusName.compareTo("AVAILABLE") == 0 ){

            layoutEmpty.setVisibility(View.VISIBLE);
            layoutStatus.setVisibility(View.GONE);

            /*final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());

            adb.setTitle("Alert");

            adb.setIcon(android.R.drawable.ic_dialog_alert);

            adb.setMessage("Oops! You don't have any status");

            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    Intent dashIntent = new Intent(getActivity(), SetStatusActivity.class);
                    startActivity(dashIntent);
                    getActivity().finish();

                }
            });

            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            adb.show();*/

        }
        else{

            layoutEmpty.setVisibility(View.GONE);
            layoutStatus.setVisibility(View.VISIBLE);

            txtStatus.setText(statusName);
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Realm realmstatus = Realm.getInstance(getActivity());
            RealmResults<SetStatusModel> statusModels=realmstatus.where(SetStatusModel.class).findAll();
            for(int i=0;i<statusModels.size();i++)
            {
                Date str_dateandtime=statusModels.get(i).getEndTime();
                str_timewithdate = formatter.format(str_dateandtime);
                Log.e(TAG, "str_dateandtime*********>>>>>>>>"+str_timewithdate);
            }
            txtEndTime.setText(timeSetting.getEndTime()+" On"+" "+str_timewithdate);

            clickListeners();

        }

    }


    public boolean hasTimeSettingForToday(String day) {

        String dayName= TimeSetting.Days.values()[this.getTodaysOrdinal()].toString();

        return dayName.startsWith(day);
    }

    private int getTodaysOrdinal() {

// Log.d(TAG, "Toadays ordinal:" + this.convertCalendarToOrdinal(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)));
        return (this.convertCalendarToOrdinal(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)));

    }


    private int convertOrdinalToCalendar(int anOrdinal) {
        return anOrdinal + 1;
    }

    private int convertCalendarToOrdinal(int aCalendarDay) {
        return aCalendarDay - 1;

    }


    private void clickListeners() {
        btnStatusChange.setOnClickListener(this);
        btnExtendtime.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnFive.setOnClickListener(this);
        btnTen.setOnClickListener(this);
        btnFifteen.setOnClickListener(this);
        btnOneHour.setOnClickListener(this);
        btnTwoHour.setOnClickListener(this);
        imgCloseIcon.setOnClickListener(this);


    }

    public void onClick(View view) {

        Intent intent;

        switch (view.getId()) {
            case R.id.button_Chane_status:
                intent = new Intent(getActivity(), AwayOptionActivity.class);
                intent.putExtra("statusScreen", "statusScreen");
                intent.putExtra("START_TIME", timeSetting.getStartTime());
                intent.putExtra("END_TIME", timeSetting.getEndTime());
                intent.putExtra("TIME_ID", timeSetting.getId().toString());
                startActivity(intent);
                getActivity().finish();

                break;
            case R.id.button_extend_time:

                if(Constants.haveNetworkConnection(getActivity())) {
                    if(layoutExtendTime.getVisibility()==View.VISIBLE)
                        layoutExtendTime.setVisibility(View.GONE);
                    else
                        layoutExtendTime.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(getActivity(),"No Internet Connection Available",Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.button_reset:
                if(Constants.haveNetworkConnection(getActivity())) {

                    SettimeDialog.bl_status=false;
                    MenuTimeSettingFragment.bl_quicktime=false;
                    reset();
                }else{
                    Toast.makeText(getActivity(),"No Internet Connection Available",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_five:
                addVal = 5;
                if(Constants.haveNetworkConnection(getActivity())) {
                    continueHome();
                }else{
                    Toast.makeText(getActivity(),"No Internet Connection Available",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_ten:
                addVal = 15;
                if(Constants.haveNetworkConnection(getActivity())) {
                    continueHome();
                }else{
                    Toast.makeText(getActivity(),"No Internet Connection Available",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_fifteen:
                addVal = 30;
                if(Constants.haveNetworkConnection(getActivity()))
                {
                    continueHome();
                }else{
                    Toast.makeText(getActivity(),"No Internet Connection Available",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_one_hour:
                addVal = 60;
                if(Constants.haveNetworkConnection(getActivity())) {
                    continueHome();
                }else{
                    Toast.makeText(getActivity(),"No Internet Connection Available",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_two_hour:
                addVal = 120;
                if(Constants.haveNetworkConnection(getActivity())) {
                    continueHome();
                }else{
                    Toast.makeText(getActivity(),"No Internet Connection Available",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.close_icon:
                layoutExtendTime.setVisibility(View.GONE);
                break;
        }
    }

    public void continueHome() {

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

        showProgressDialog();

        try {

            final String endTime;

            Calendar c = Calendar.getInstance();

            final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

            String endTime1 = df.format(c.getTime()) + " " + timeSetting.getEndTime();
            String Curr = df.format(c.getTime())+ " " + sdf.format(c.getTime());

            endTimeSDB = "";
            startTimeSDB = df.format(c.getTime())+ " " + sdf.format(c.getTime());//Wrong Value Need to update for start time from local DB (SAJEN)

            Date cDate = formatter.parse(Curr);
            Date eDate = formatter.parse(endTime1);

            if(cDate.getTime() >= eDate.getTime()) {

                Date d = sdf.parse(timeSetting.getEndTime());

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 1);
                String curr = sdf.format(cal.getTime());

                Calendar calE = Calendar.getInstance();
                calE.setTime(d);
                calE.add(Calendar.MINUTE, addVal);

                String Curr1 = df.format(c.getTime())+" "+sdf.format(cal.getTime());
                String endTim1 = df.format(c.getTime())+" "+sdf.format(calE.getTime());

                final Date cDate1 = formatter.parse(Curr1);
                final Date eDate1 = formatter.parse(endTim1);

                if(cDate1.getTime() > eDate1.getTime()){

                    endTime = sdf.format(eDate1.getTime());

                    endTimeSDB = df.format(cal.getTime())+" "+sdf.format(eDate1.getTime());

                }else{

                    dismissProgressDialog();

                    Toast.makeText(getActivity(),"Your End Time Exceeds 24 Hours",Toast.LENGTH_SHORT).show();

                    return;

                }

            }else{

                Date d = sdf.parse(timeSetting.getEndTime());

                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                cal.add(Calendar.MINUTE, addVal);
                endTime = sdf.format(cal.getTime());

                endTimeSDB = df.format(c.getTime())+" "+endTime;

                eDate = formatter.parse(endTimeSDB);

                if(cDate.getTime() > eDate.getTime()) {

                    Calendar calE = Calendar.getInstance();
                    calE.add(Calendar.DATE, 1);

                    endTimeSDB = df.format(calE.getTime())+" "+endTime;

                }


            }


// status update

//            String endTimeDB = df.format(c.getTime()) + " " + endTime;

            final Date endDate = formatter.parse(endTimeSDB);


            String activeState="0";

            Realm realm = Realm.getInstance(getActivity());
            RealmResults<ExtendTimeModel> extendTimeModel = realm.where(ExtendTimeModel.class).equalTo("timeId",timeId.toString()).findAll();
//            System.out.println("extendTimeModel = " + extendTimeModel);

            if(extendTimeModel.size()>0){
                activeState = extendTimeModel.get(0).getStartId();
            }
            realm.close();


            Map<String, String> params = new HashMap<>();
            params.put("DetachUserStatusID", id);
            params.put("AccessToken",Constants.accessToken);
            params.put("StartTime",startTimeSDB);
            params.put("EndTime", endTimeSDB);
            params.put("Status", statusName);
            params.put("TimeID", timeId.toString());
            params.put("IsActive", activeState);

            new GetJSONResponse(getActivity()).RequestJsonToServer(getActivity(), "DetachStatus", params, new VolleyInterface() {

                @Override
                public void onSuccessResponse(JSONObject result) {
//                    System.out.println("Success state for update status:----" + result + "");

                    try{
                        TimeSetting aTimeSettings = TimeSetting.findById(TimeSetting.class, timeId);
                        aTimeSettings.setEndTime(endTime);
                        aTimeSettings.save();

                        Realm realm = Realm.getInstance(getActivity());

                        RealmResults<SetStatusModel> setStatusModel = realm.where(SetStatusModel.class).equalTo("statusId",result.getString("DetachUserStatusID")).findAll();

                        if(setStatusModel.size()>0){
                            realm.beginTransaction();
                            setStatusModel.get(0).setEndTime(endDate);
                            realm.commitTransaction();
                        }

                        realm.close();

                        goUnavailable(timeId);

                        dismissProgressDialog();

                        getActivity().stopService(new Intent(getActivity(), JJOnAwayService.class));
                        getActivity().startService(new Intent(getActivity(), JJOnAwayService.class));

                        setValues();

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onErroeResponse(VolleyError result) {
                    System.out.println("Error state for update status:----" + result + "");

                }

                @Override
                public void onSuccessResponse(JSONArray result) {

                }
            });

// status update end



// System.out.println("endTime = " + endTime);

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void promptUserForTime(boolean isStartTime) {
        timeFrag = new EndTimerDialogFragment(isStartTime,timeSetting.getEndTime());
        timeFrag.setRetainInstance(true);
        timeFrag.show(getActivity().getFragmentManager(), "timePicker");
    }

    public void setEndTime(String anEndTime) {
//BUG THAT SUBMITS THE CURRENT TIME AS END TIME
        if (!TimeSetting.isEndTimeInFuture(anEndTime)) {
            Snackbar.make(dashboardView, "end time must be in future", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (!TimeSetting.isValidTimeInterval(timeSetting.getStartTime(), anEndTime)) {
            Snackbar.make(dashboardView, "Invalid Interval", Snackbar.LENGTH_LONG).show();
            return;
        }

        navigateToAway(anEndTime,timeId);

    }

    public void checkEndTime(String anEndTime){

        if (!TimeSetting.isEndTimeInFuture(anEndTime)) {
            Snackbar.make(dashboardView, "end time must be in future", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (!TimeSetting.isValidTimeInterval(timeSetting.getStartTime(), anEndTime)) {
            Snackbar.make(dashboardView, "Invalid Interval", Snackbar.LENGTH_LONG).show();
            return;
        }

    }

    private void navigateToAway(String endTime,Long timeSettingId) {

        TimeSetting aTimeSettings = TimeSetting.findById(TimeSetting.class, timeSettingId);
        aTimeSettings.setEndTime(endTime);
        aTimeSettings.save();

        goUnavailable(timeId);

        getActivity().stopService(new Intent(getActivity(),JJOnAwayService.class));
        getActivity().startService(new Intent(getActivity(), JJOnAwayService.class));

        setValues();
    }

    private void goUnavailable(Long id) {

        TimeSetting aTimeSettings = TimeSetting.findById(TimeSetting.class, id);

        this.user.goUnavailable(aTimeSettings.getStatus(),
                aTimeSettings.getStartTime(), new AvailabilityTime(aTimeSettings.getEndTime()),id);

    }



    public void reset() {

        goAvailable();

        System.out.println("user.getUserStatus().getAvailabilityStatus() = " + user.getUserStatus().getAvailabilityStatus());

    }

    public void goAvailable() {
        this.user.goAvailable();


        Map<String, String> params = new HashMap<>();
        params.put("DetachUserStatusID", id);
        params.put("AccessToken",Constants.accessToken);
        params.put("StartTime",startTimeSDB);
        params.put("EndTime", endTimeSDB);
        params.put("Status", statusName);
        params.put("TimeID", timeId.toString());
        params.put("IsActive", "0");

        new GetJSONResponse(getActivity()).RequestJsonToServer(getActivity(), "DetachStatus", params, new VolleyInterface() {

            @Override
            public void onSuccessResponse(JSONObject result) {
                try
                {
                    Log.e("StatusFragment Result:" , result.toString());

                    List<TimeSetting> timeList = TimeSetting.listAll(TimeSetting.class);

                    for (TimeSetting aTimeSetting : timeList) {

                        if(aTimeSetting.getId() != 1 && aTimeSetting.isOn())
                        {
                            TimeSetting aTimeSettings = TimeSetting.findById(TimeSetting.class, aTimeSetting.getId());
                            aTimeSettings.off();
                            aTimeSettings.save();
                        }
                    }

                    Realm realm = Realm.getInstance(getActivity());

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Log.e("StatusFragment","Enterintorelamdatabase888888888");
                            realm.clear(SetStatusModel.class);
                            realm.clear(ExtendTimeModel.class);
                            realm.commitTransaction();
                        }
                    });

                    realm.close();

                    Intent service_int = new Intent(getActivity(), JJOnAwayService.class);
                    getActivity().stopService(service_int);

                    Intent intent = new Intent(getActivity(), LogActivity.class);
                    intent.putExtra("recentFlag", true);
                    startActivity(intent);
                    getActivity().finish();

                }catch(Exception e)
                {
                    e.printStackTrace();
                }

                  }

            @Override
            public void onErroeResponse(VolleyError result) {
                System.out.println("Error state for update status:----" + result + "");

            }
            @Override
            public void onSuccessResponse(JSONArray result) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        // System.out.println("ON PAUSE");

    }

    @Override
    public void onResume() {
        super.onResume();
    /*  if(JJOnAwayService.bl_availablestatus)
    {
        JJOnAwayService.bl_availablestatus=false;
        Intent intent = new Intent(getActivity(), SetStatusActivity.class);
        startActivity(intent);
        getActivity().finish();

    }*/
// System.out.println("ON RESUME");
    }

    private void monitorStatus() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    Log.e("StatusFragment","Entering handler");
                    RealmResults<SetStatusModel> statusModelRealmResults = getRealmInstance().where(SetStatusModel.class).findAll();
                    Log.e("StatusFragment","Status: " + statusModelRealmResults.toString());
                    //if(Calendar.getInstance().getTimeInMillis() > statusModelRealmResults.get(0).getEndTime().getTime()) {
                    if(statusModelRealmResults.isEmpty()) {
                        goAvailable();
                    }
                    Log.e("StatusFragment","Exiting handler");
                } catch(Exception e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 2500);
            }
        }, 2500);
    }
}