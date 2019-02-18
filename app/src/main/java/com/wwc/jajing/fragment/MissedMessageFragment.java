package com.wwc.jajing.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.wwc.R;
import com.wwc.jajing.domain.entity.TimeSetting;
import com.wwc.jajing.domain.entity.User;
import com.wwc.jajing.permissions.MarshMallowPermission;
import com.wwc.jajing.realmDB.CallsModel;
import com.wwc.jajing.realmDB.MessageModel;
import com.wwc.jajing.realmDB.MissedMessageModel;
import com.wwc.jajing.system.JJSystem;
import com.wwc.jajing.system.JJSystemImpl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.wwc.jajing.system.JJApplication.getRealmInstance;

/**
 * Created by vivek_e on 9/12/2016.
 */
public class MissedMessageFragment extends BaseFragment {

    private LinearLayout call_lay;
    RealmResults<MissedMessageModel> messageModelRealmResults,msgModelRealmResults;

    MarshMallowPermission marshMallowPermission;
    User user;
    TimeSetting timeSetting;
//    String strtTime, endTime;

    TextView txt_empty;

    String dateString;

    Date dateFormat;

    SimpleDateFormat df;



    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        View dashboardView = inflater.inflate(R.layout.activity_missed_calls, container, false);
        call_lay = (LinearLayout) dashboardView.findViewById(R.id.call_lay);
        txt_empty = (TextView) dashboardView.findViewById(R.id.txt_empty);
//        getRealmInstance().executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                realm.clear(MessageModel.class);
//            }
//        });
        Uri mSmsQueryUri = Uri.parse("content://sms/inbox");
        Cursor cursor = null;

        JJSystem jjSystem = JJSystemImpl.getInstance();
        this.user = (User) jjSystem.getSystemService(JJSystemImpl.Services.USER);
        Log.e("Enterinto missed message fragment","called");

//        timeSetting = TimeSetting.findById(TimeSetting.class, 1);

        marshMallowPermission = new MarshMallowPermission(getActivity());

//        ActivityCompat.requestPermissions(getActivity(),
//                new String[]{Manifest.permission.READ_SMS},
//                1);

//        Calendar c = Calendar.getInstance();
//
//        final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
//        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//        strtTime = df.format(c.getTime()) + " " + timeSetting.getStartTime();
//        endTime = df.format(c.getTime()) + " " + timeSetting.getEndTime();
//        Date startDate = null, endDate = null;
//        try {
//            startDate = formatter.parse(strtTime);
//            endDate = formatter.parse(endTime);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


//        RealmResults<MissedMessageModel> missedMessage = getRealmInstance().where(MissedMessageModel.class).findAll();
        if(HistoryFragment.str_deletetab1=="deletemissedmessages")
        {
            HistoryFragment.str_deletetab1="";
            //Toast.makeText(getActivity(),"callmissedmessagefragment", Toast.LENGTH_SHORT).show();
            getRealmInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.clear(MissedMessageModel.class);
                }
            });

            //callModelRealmResults.clear();


        }

        customOptions();
        return dashboardView;
    }



    private void customOptions() {

        try {

            df = new SimpleDateFormat("dd/MM/yyyy");

            Calendar calendar = Calendar.getInstance();
            Date date = df.parse(df.format(calendar.getTime()));

            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, -3);
            Date newDate = calendar.getTime();

            dateString = df.format(newDate);

            dateFormat = df.parse(dateString);

            System.out.println("date = " + dateFormat);
            messageModelRealmResults = getRealmInstance().where(MissedMessageModel.class).greaterThanOrEqualTo("callDateFormat",dateFormat).findAllSorted("type",true);
//            messageModelRealmResults = getRealmInstance().where(MissedMessageModel.class).greaterThanOrEqualTo("callDateFormat",dateFormat).findAllSorted("callDateFormat",RealmResults.SORT_ORDER_DESCENDING);
//            callsModelRealmResults = getRealmInstance().where(CallsModel.class).greaterThanOrEqualTo("callDateFormat",dateFormat).findAllSorted("type",true);


            if (call_lay.getChildCount() > 0)
                call_lay.removeAllViews();

            if (messageModelRealmResults.size() > 0) {
                call_lay.setVisibility(View.VISIBLE);
                txt_empty.setVisibility(View.GONE);

            } else {
                call_lay.setVisibility(View.GONE);
                txt_empty.setText("No Missed Messages.");
                txt_empty.setVisibility(View.VISIBLE);
            }

            String type="";

    //        System.out.println("callsModelRealmResults = " + callsModelRealmResults);


            ArrayList<String> getDate=new ArrayList<String>();
            System.out.println("callsModelRealmResults = " + messageModelRealmResults);


            for (int i = 0; i < messageModelRealmResults.size(); i++) {

                final MissedMessageModel obj = messageModelRealmResults.get(i);

                LayoutInflater layoutInflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View view = layoutInflator.inflate(R.layout.fragment_tab_missed_call, null);

                //commend by sathish this code for listed message based status
                if(type.compareTo(obj.getType())!=0){

                    type  = obj.getType();

                    TextView text_status = (TextView) view.findViewById(R.id.status_name);

                    LinearLayout adHistory = (LinearLayout)view.findViewById(R.id.adhistory_layout);

                    text_status.setText(obj.getType());

                    msgModelRealmResults = getRealmInstance().where(MissedMessageModel.class).contains("type",type).findAllSorted("time",RealmResults.SORT_ORDER_DESCENDING);

                    for (int j = 0; j < msgModelRealmResults.size(); j++) {

                        final MissedMessageModel objNew = msgModelRealmResults.get(j);

                        LayoutInflater layoutInflatorHistory = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View viewHistory = layoutInflatorHistory.inflate(R.layout.history_message_list, null);

                        if(type.compareTo(objNew.getType()) == 0){

                            final TextView text_no = (TextView) viewHistory.findViewById(R.id.contact_number);
                            final TextView text_name = (TextView) viewHistory.findViewById(R.id.contact_name);
                            final TextView text_msg = (TextView) viewHistory.findViewById(R.id.contact_message);
                            RelativeTimeTextView text_time = (RelativeTimeTextView) viewHistory.findViewById(R.id.time);


//                            text_time.setReferenceTime(Long.parseLong(objNew.getTime()));
                            text_msg.setText(objNew.getMessage());

                            if(objNew.getCallerName().compareTo("Unknown")==0){
                                text_name.setText(objNew.getNumber());
                            }else{
                                text_name.setText(objNew.getCallerName());
                            }
                            Date ms_time = new Date(Long.parseLong(objNew.getTime()));
                            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
                            String dateFormatted = formatter.format(ms_time);

                            text_time.setText(dateFormatted);

                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialNumber(text_no.getText().toString());
                                }
                            });
                            adHistory.addView(viewHistory);
                        }
                    }

                    call_lay.addView(view);
                }
//                call_lay.addView(view);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        dismissProgressDialog();
    }


    private void dialNumber(String no) {


        Intent msgIntent = new Intent(Intent.ACTION_MAIN);
        msgIntent.setType("vnd.android-dir/mms-sms");
        startActivity(msgIntent);

    }


}
