package com.wwc.jajing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.wwc.R;
import com.wwc.jajing.activities.Constants;
import com.wwc.jajing.activities.StatusActivity;
import com.wwc.jajing.component.GetJSONResponse;
import com.wwc.jajing.domain.entity.CallerImpl;
import com.wwc.jajing.domain.entity.TimeSetting;
import com.wwc.jajing.domain.entity.User;
import com.wwc.jajing.domain.value.AvailabilityTime;
import com.wwc.jajing.interfaces.VolleyInterface;
import com.wwc.jajing.realmDB.ExtendTimeModel;
import com.wwc.jajing.realmDB.SetStatusModel;
import com.wwc.jajing.services.JJOnAwayService;
import com.wwc.jajing.system.JJSystem;
import com.wwc.jajing.system.JJSystemImpl;
import com.wwc.jajing.util.DateHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
 * Created by infmac1 on 07/11/16.
 */

public class MenuTimeSettingFragment extends BaseFragment{

    int[] buttonIds = {R.id.btn_1,R.id.btn_2,R.id.btn_4,R.id.btn_8,R.id.btn_24,
            R.id.btn_5,R.id.btn_10,R.id.btn_15,R.id.btn_30,R.id.btn_45};

    final Button[] timeButtons=new Button[10] ;

// TimeSetting timeSetting;

    Long timeId = 1L;

    View dashboardView;

    private User user;

    String startTimeDB,endTimeDB,startTime,endTime;

    Date sDate, eDate;
    public static boolean bl_quicktime;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        dashboardView = inflater.inflate(R.layout.timesetting_fragment, container, false);
        setCustomActionBar("Set Time");

        JJSystem jjSystem = JJSystemImpl.getInstance();
        this.user = (User) jjSystem.getSystemService(JJSystemImpl.Services.USER);

        for(int i = 0;i < buttonIds.length; i++){

            timeButtons[i] = (Button)dashboardView.findViewById(buttonIds[i]);
            timeButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(Constants.haveNetworkConnection(getActivity())) {
                        onButtonClicK(v);
                    }else{
                        Toast.makeText(getActivity(),"No Internet Connection Available",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        return dashboardView;

    }


    public void onButtonClicK(View v) {

        showProgressDialog();
        bl_quicktime=true;

        final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

        int time = Integer.parseInt(v.getTag().toString());

        try {

            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
            Calendar cal = Calendar.getInstance();

            startTime = formatter.format(cal.getTime());
            startTimeDB = sdf.format(cal.getTime());


            Calendar cal1 = Calendar.getInstance();
            Date d = sdfDate.parse(formatter.format(cal1.getTime()));
            cal.setTime(d);
            cal.add(Calendar.MINUTE, time);
            endTime = df.format(cal.getTime()) + " " + sdf.format(cal.getTime());
            endTimeDB = sdf.format(cal.getTime());

            Map<String, String> params = new HashMap<>();

            params.put("DetachUserStatusID", "");
            params.put("StartTime", startTime);
            params.put("AccessToken", Constants.accessToken);
            params.put("EndTime", endTime);
            params.put("TimeID", timeId.toString());
            params.put("Status", "Busy");
            params.put("IsActive", "1");


            new GetJSONResponse(getActivity()).RequestJsonToServer(getActivity(), "DetachStatus", params, new VolleyInterface() {

                @Override
                public void onSuccessResponse(JSONObject result) {

                    System.out.println("Menu Success state for update status:----" + result + "");

                    setTimeStatusSuccess(result);

                }

                @Override
                public void onErroeResponse(VolleyError result) {
                    System.out.println("Menu Error state for update status:----" + result + "");
                }

                @Override
                public void onSuccessResponse(JSONArray result) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();

        }
/*} else {
DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
TimeSetting aTimeSettings = TimeSetting.findById(TimeSetting.class, timeId);
aTimeSettings.setEndTime(timeSetting.getEndTime());
aTimeSettings.save();


goUnavailable(timeId);


}*/
    }


    public void setTimeStatusSuccess(final JSONObject result){

        List<TimeSetting> timeListOff = TimeSetting.listAll(TimeSetting.class);

        for (TimeSetting aTimeSetting : timeListOff) {

            if(aTimeSetting.getId() != 1 && aTimeSetting.isOn())
            {
                TimeSetting aTimeSettings = TimeSetting.findById(TimeSetting.class, aTimeSetting.getId());
                aTimeSettings.off();
                aTimeSettings.save();
            }
        }


        TimeSetting timeSetting = new TimeSetting(getContext(), startTimeDB, endTimeDB, null, "Busy",1);
        timeSetting.save();

        List<TimeSetting> timeList = TimeSetting.listAll(TimeSetting.class);

        for (TimeSetting aTimeSetting : timeList) {

            if(aTimeSetting.getId() != 1 && aTimeSetting.isOn())
            {
                TimeSetting aTimeSettings = TimeSetting.findById(TimeSetting.class, aTimeSetting.getId());
// System.out.println("is ON TimeSettings = " + aTimeSettings.getId());

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

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");

        sDate=null ;
        eDate =null;

        try{
            sDate = formatter.parse(startTime);
            eDate = formatter.parse(endTime);
        }catch(Exception e){
            e.printStackTrace();
        }

        getRealmInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.clear(SetStatusModel.class);
                realm.clear(ExtendTimeModel.class);
            }
        });

        getRealmInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ExtendTimeModel extendTimeModel = realm.createObject(ExtendTimeModel.class); // Create managed objects directly
                if (extendTimeModel != null) {
                    extendTimeModel.setTimeId(timeId.toString());
                    extendTimeModel.setStartId("1");
                }
            }
        });


        getRealmInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                SetStatusModel setStatusModel = realm.createObject(SetStatusModel.class);

                try {

                    JSONObject jsonResponse = new JSONObject(result.toString());
                    setStatusModel.setStatusId(jsonResponse.getString("DetachUserStatusID"));
                    setStatusModel.setStatusName("Busy");
                    setStatusModel.setTimeId(timeId.toString());
                    setStatusModel.setStartTime(sDate);
                    setStatusModel.setEndTime(eDate);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        TimeSetting aTimeSettings = TimeSetting.findById(TimeSetting.class, timeId);
        aTimeSettings.setEndTime(endTimeDB);
        aTimeSettings.save();

        goUnavailable(timeId);

        dismissProgressDialog();

        getActivity().stopService(new Intent(getActivity(), JJOnAwayService.class));
        getActivity().startService(new Intent(getActivity(), JJOnAwayService.class));

        Intent i = new Intent(getActivity(), StatusActivity.class);
        this.startActivity(i);
        getActivity().finish();

    }




    private void goUnavailable(Long id) {


// TimeSetting aTimeSettings = TimeSetting.findById(TimeSetting.class, timeId);
// aTimeSettings.setEndTime(sdf.format(cal.getTime()));
// aTimeSettings.save();


        TimeSetting aTimeSettings = TimeSetting.findById(TimeSetting.class, id);

        this.user.goUnavailable(aTimeSettings.getStatus(),
                aTimeSettings.getStartTime(), new AvailabilityTime(aTimeSettings.getEndTime()), id);


    }


}