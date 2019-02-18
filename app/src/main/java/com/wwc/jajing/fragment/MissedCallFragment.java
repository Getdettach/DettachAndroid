package com.wwc.jajing.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
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
import com.wwc.jajing.domain.entity.CallerImpl;
import com.wwc.jajing.domain.entity.MissedCall;
import com.wwc.jajing.domain.entity.TimeSetting;
import com.wwc.jajing.domain.entity.User;
import com.wwc.jajing.realmDB.CallsModel;
import com.wwc.jajing.realmDB.HasAppModel;
import com.wwc.jajing.realmDB.OptionsModel;
import com.wwc.jajing.system.JJSystem;
import com.wwc.jajing.system.JJSystemImpl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

import static com.wwc.jajing.system.JJApplication.getRealmInstance;

/**
 * Created by vivek_e on 9/12/2016.
 */
public class MissedCallFragment extends BaseFragment {

   RealmResults<CallsModel> callsModelRealmResults,callModelRealmResults;

    RealmResults<CallsModel> callsModelRealmResults_calldate;   //sathish


    private LinearLayout call_lay;
    TextView txt_empty;

    User user;
    TimeSetting timeSetting;
    String strtTime, endTime;

    String dateString;

    Date dateFormat;

    SimpleDateFormat df;
    public static String str_tab1;
   public  Handler h = new Handler();
   public  int delay = 5000; //15 seconds
   public Runnable runnable;
    //Timer t;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        View dashboardView = inflater.inflate(R.layout.activity_missed_calls, container, false);
        call_lay = (LinearLayout) dashboardView.findViewById(R.id.call_lay);
        txt_empty = (TextView) dashboardView.findViewById(R.id.txt_empty);
        if(HistoryFragment.str_delete=="deletemissedcalls")
        {
            HistoryFragment.str_delete="";
            //Toast.makeText(getActivity(),"callmissedcallfragment",Toast.LENGTH_SHORT).show();
            getRealmInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.clear(CallsModel.class);
                }
            });

            //callModelRealmResults.clear();


        }
            str_tab1="Tab1";
            //timeCalculation();

//        getRealmInstance().executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                realm.clear(CallsModel.class);
//            }
//        });

//        ActivityCompat.requestPermissions(getActivity(),
//                new String[]{Manifest.permission.READ_CALL_LOG},
//                1);



        JJSystem jjSystem = JJSystemImpl.getInstance();
        this.user = (User) jjSystem.getSystemService(JJSystemImpl.Services.USER);

//        timeSetting = TimeSetting.findById(TimeSetting.class, 1);
//
//
//        Calendar c = Calendar.getInstance();
//
//        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//        strtTime = df.format(c.getTime()) + " " + timeSetting.getStartTime();
//        endTime = df.format(c.getTime()) + " " + timeSetting.getEndTime();
        customOptions();
        h.postDelayed(new Runnable() {
            public void run() {
                //do something

                try{
                    runnable=this;
                    //Toast.makeText(getActivity(),"handlercall",Toast.LENGTH_SHORT).show();
                    customOptions();
                    h.postDelayed(runnable, delay);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        }, delay);

        return dashboardView;
    }


    public void timeCalculation()
    {
        SimpleDateFormat dateFormat= new SimpleDateFormat("EEEE dd.MM.yyyy");
        Calendar currentCal = Calendar.getInstance();
        String currentdate=dateFormat.format(currentCal.getTime());
        String currhour=String.valueOf(currentCal.get(Calendar.HOUR_OF_DAY));
        String currmin=String.valueOf(currentCal.get(Calendar.MINUTE));
        String currsecond=String.valueOf(currentCal.get(Calendar.SECOND));
        String Currentdateandtime=currentdate+" , "+currhour+" : "+currmin+" : "+currsecond;


        currentCal.add(Calendar.DATE, 3);
        String toDate=dateFormat.format(currentCal.getTime());
        String tohour=String.valueOf(currentCal.get(Calendar.HOUR_OF_DAY));
        String tomin=String.valueOf(currentCal.get(Calendar.MINUTE));
        String tosecond=String.valueOf(currentCal.get(Calendar.SECOND));
        String todateandtime=toDate+" , "+tohour+" : "+tomin+" : "+tosecond;
        Log.e("MissedCallFragment","currentdate : " + Currentdateandtime+" toDate : "+todateandtime);
    }
    private void dialNumber(String no) {
        Uri number = Uri.parse("tel:" + no);
        Log.e("MissedCallFragment","number : "+number);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }

       private void customOptions() {

        try {

            df = new SimpleDateFormat("dd/MM/yyyy");

            Calendar calendar = Calendar.getInstance();
            Date date = df.parse(df.format(calendar.getTime()));


            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, -2);
            Date newDate = calendar.getTime();


            dateString = df.format(newDate);

            dateFormat = df.parse(dateString);

            System.out.println("date = " + dateFormat);

//            callsModelRealmResults = getRealmInstance().where(CallsModel.class).greaterThanOrEqualTo("callDateFormat",dateFormat).findAllSorted("time",RealmResults.SORT_ORDER_DESCENDING);
            callsModelRealmResults = getRealmInstance().where(CallsModel.class).greaterThanOrEqualTo("callDateFormat",dateFormat).findAllSorted("type",true);


            if (call_lay.getChildCount() > 0)
                call_lay.removeAllViews();

            if (callsModelRealmResults.size() > 0) {
                call_lay.setVisibility(View.VISIBLE);
                txt_empty.setVisibility(View.GONE);



            } else {
                call_lay.setVisibility(View.GONE);
                txt_empty.setText("No Missed Calls.");
                txt_empty.setVisibility(View.VISIBLE);
            }

            String type="";
            ArrayList<String> getDate=new ArrayList<String>();




            //System.out.println("callsModelRealmResults = " + callsModelRealmResults);



            for (int i = 0; i < callsModelRealmResults.size(); i++) {

                final CallsModel obj = callsModelRealmResults.get(i);

                LayoutInflater layoutInflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View view = layoutInflator.inflate(R.layout.fragment_tab_missed_call, null);



                if(type.compareTo(obj.getType())!=0) {


                    //System.out.println("Type value ::" + type + " Object value :: " + obj.getType());

                    type = obj.getType();

                    TextView text_status = (TextView) view.findViewById(R.id.status_name);

                    LinearLayout adHistory = (LinearLayout) view.findViewById(R.id.adhistory_layout);

                    text_status.setText(obj.getType());


                   // callModelRealmResults = getRealmInstance().where(CallsModel.class).contains("type",type).findAllSorted("time", RealmResults.SORT_ORDER_DESCENDING);
                    callModelRealmResults = getRealmInstance().where(CallsModel.class).contains("type",type).findAllSorted("time", RealmResults.SORT_ORDER_DESCENDING);

                    for (int j = 0; j < callModelRealmResults.size(); j++) {
                        //final CallsModel objNew = callModelRealmResults.get(j);
                        CallsModel objNew = callModelRealmResults.get(j);
                        Log.e("MissedCallFragment","callModelRealmResults-------->>>"+callModelRealmResults.get(j));

                        LayoutInflater layoutInflatorHistory = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View viewHistory = layoutInflatorHistory.inflate(R.layout.history_call_list, null);


                        if (type.compareTo(objNew.getType()) == 0) {

                            final TextView text_name = (TextView) viewHistory.findViewById(R.id.contact_name);
                            final TextView text_no = (TextView) viewHistory.findViewById(R.id.contact_number);
                            RelativeTimeTextView text_time = (RelativeTimeTextView) viewHistory.findViewById(R.id.time);

                            Log.e("MissedCallFragment","objNew.getCallerName()===="+objNew.getCallerName());
                            text_no.setText(objNew.getNumber());

                            if (objNew.getCallerName().compareTo("Unknown") == 0) {
                                text_name.setText(objNew.getNumber());
                            } else {
                                text_name.setText(objNew.getCallerName());
                            }

//                            text_no.setText(objNew.getNumber());

                            Date ms_time = new Date(Long.parseLong(objNew.getTime()));
                            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
                            String dateFormatted = formatter.format(ms_time);

                            text_time.setText(dateFormatted);

                            SimpleDateFormat dateFormat= new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
                            Calendar currentCal = Calendar.getInstance();
                            currentCal.setTime(ms_time);
                            String currentdate=dateFormat.format(currentCal.getTime());
                            String currhour=String.valueOf(currentCal.get(Calendar.HOUR_OF_DAY));
                            String currmin=String.valueOf(currentCal.get(Calendar.MINUTE));
                            String currsecond=String.valueOf(currentCal.get(Calendar.SECOND));
                            String Currentdateandtime=currentdate+" , "+currhour+" : "+currmin+" : "+currsecond;

                            currentCal.add(Calendar.DATE, 3);
                            String toDate=dateFormat.format(currentCal.getTime());
                            String tohour=String.valueOf(currentCal.get(Calendar.HOUR_OF_DAY));
                            String tomin=String.valueOf(currentCal.get(Calendar.MINUTE));
                            String tosecond=String.valueOf(currentCal.get(Calendar.SECOND));
                            String todateandtime=toDate+" , "+tohour+" : "+tomin+" : "+tosecond;
                            Log.e("MissedCallFragment","currentdate : " + Currentdateandtime+" toDate : "+todateandtime);

                            //SimpleDateFormat dateFormat1= new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
                            Calendar currentdateandtime = Calendar.getInstance();
                            String currdate=dateFormat.format(currentdateandtime.getTime());
                            Log.e("MissedCallFragment","currentdate : " + currdate+" toDate : "+toDate);
                            if(currdate.equals(toDate))
                            {
                                Log.e("MissedCallFragment","datematch");
                            }


                            viewHistory.setOnClickListener(new View.OnClickListener() {
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
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        dismissProgressDialog();
    }


}

