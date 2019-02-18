package com.wwc.jajing.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.wwc.R;
import com.wwc.jajing.component.GetJSONResponse;
import com.wwc.jajing.domain.entity.CallerImpl;
import com.wwc.jajing.domain.entity.TimeSetting;
import com.wwc.jajing.domain.entity.User;
import com.wwc.jajing.domain.value.AvailabilityTime;
import com.wwc.jajing.fragment.AwayOptionFragment;
import com.wwc.jajing.fragment.BaseFragment;
import com.wwc.jajing.interfaces.VolleyInterface;
import com.wwc.jajing.realmDB.ExtendTimeModel;
import com.wwc.jajing.realmDB.SetStatusModel;
import com.wwc.jajing.services.JJOnAwayService;
import com.wwc.jajing.system.JJSystem;
import com.wwc.jajing.system.JJSystemImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;

import static com.wwc.jajing.system.JJApplication.getRealmInstance;

/**
 * Created by infmac1 on 03/01/17.
 */

public class SettimeDialog extends DialogFragment implements DialogInterface.OnDismissListener, View.OnClickListener {
    int[] buttonIds = {R.id.btn_1,R.id.btn_2,R.id.btn_4,R.id.btn_8,R.id.btn_24,
            R.id.btn_5,R.id.btn_10,R.id.btn_15,R.id.btn_30,R.id.btn_45};

    final Button[] timeButtons=new Button[10] ;
    View view;
    AlertDialog.Builder builder;
    private Dialog dialog = null;
    String startTimeDB,endTimeDB,startTime,endTime;
    Long timeId = 1L;
    Date sDate, eDate;
    ImageView imgv_close;
    private User user;
    SharedPreferences preferences;
    public static boolean bl_status;
    String str_status;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient mDialog construction
        builder = new AlertDialog.Builder(getActivity());
       // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();


        view = inflater.inflate(R.layout.settime_dialog, null);
        //builder.setTitle("Custom Status");
        imgv_close=(ImageView)view.findViewById(R.id.imgv_close);
        JJSystem jjSystem = JJSystemImpl.getInstance();
        this.user = (User) jjSystem.getSystemService(JJSystemImpl.Services.USER);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
          // save = (Button) view.findViewById(R.id.buttonSaveCustomMessage);
        //customMessage = (EditText) view.findViewById(R.id.editCustomMessage);
//        editCustomdesc = (EditText) view.findViewById(R.id.editCustomdesc);

        //customLayout = (LinearLayout)view.findViewById(R.id.custom_layout);

        //save.setOnClickListener(this);
        builder.setCancelable(true);
        // Inflate and set the layout for the mDialog
        // Pass null as the parent view because its going in the mDialog layout
        builder.setView(view);

        //params.gravity = Gravity.LEFT;


       // customMessage.setSingleLine();

        /*customMessage.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // hide virtual keyboard
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(customMessage.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });*/
        for(int i = 0;i < buttonIds.length; i++){

            timeButtons[i] = (Button)view.findViewById(buttonIds[i]);
            final int finalI = i;
            timeButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(Constants.haveNetworkConnection(getActivity())) {
                        //Toast.makeText(getActivity(),"timebuttons=="+timeButtons[finalI],Toast.LENGTH_LONG).show();
                        onButtonClicK(v);

                    }else{
                        Toast.makeText(getActivity(),"No Internet Connection Available",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    imgv_close.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v)
    {

        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
        getDialog().dismiss();

        //new AwayOptionFragment().setTitle("Set Your Status");

        //Toast.makeText(getActivity(),"canceldialog",Toast.LENGTH_SHORT).show();


    }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onClick(View v) {

    }
    public void onButtonClicK(View v) {
         bl_status=true;

        showProgressDialog();

        final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

        int time = Integer.parseInt(v.getTag().toString());
        //Toast.makeText(getActivity(),"Status==="+preferences.getString("status"," "),Toast.LENGTH_SHORT).show();

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
            //params.put("Status", "Busy");
            params.put("Status", preferences.getString("status",""));
            params.put("IsActive", "1");


            new GetJSONResponse(getActivity()).RequestJsonToServer(getActivity(), "DetachStatus", params, new VolleyInterface() {

                @Override
                public void onSuccessResponse(JSONObject result) {

                    //System.out.println("Menu Success state for update status:----" + result + "");
                    Log.e("SettimeDialog","Menu Success state for update status:----" + result);

                    setTimeStatusSuccess(result);

                }

                @Override
                public void onErroeResponse(VolleyError result) {
                    //System.out.println("Menu Error state for update status:----" + result + "");
                    Log.e("SettimeDialog","Menu Error state for update status:----" + result);
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
    /**
     * Creates and displays a progress dialog if one does not already exist
     */
    protected void showProgressDialog() {
        try {
            if (dialog == null) {
                dialog = new Dialog(getActivity(), R.style.CustomProgressTheme);
                dialog.setContentView(R.layout.custom_progress);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Dismiss Progress Dialog
     */
    protected void dismissProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dialog = null;
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


        TimeSetting timeSetting = new TimeSetting(getContext(), startTimeDB, endTimeDB, null, preferences.getString("status"," "),1);
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
                     //setStatusModel.setStatusName("Busy");
                    setStatusModel.setStatusName(preferences.getString("status"," "));
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
        /*SharedPreferences.Editor edit=preferences.edit();
        edit.putBoolean("booleanstatus",bl_status);
        edit.commit();*/

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
