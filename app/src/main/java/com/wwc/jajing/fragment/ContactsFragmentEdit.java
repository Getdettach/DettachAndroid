package com.wwc.jajing.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.wwc.R;
import com.wwc.jajing.activities.Constants;
import com.wwc.jajing.activities.ContactsActivity;
import com.wwc.jajing.component.GetJSONResponse;
import com.wwc.jajing.interfaces.VolleyInterface;
import com.wwc.jajing.realmDB.ContactModel;
import com.wwc.jajing.realmDB.HasAppModel;
import com.wwc.jajing.util.CheckConnection;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.wwc.jajing.system.JJApplication.getRealmInstance;

/**
 * Created by Vivek on 25-09-2016.
 */

public class ContactsFragmentEdit extends BaseFragment {

    private LinearLayout call_lay;

    RealmResults<ContactModel> messageModelRealmResults;

    LinearLayout loader;

    private RecyclerView recyclerView;
    private ContactsAdapter mAdapter;
    Cursor cursor ;
    String name;
    String phoneNumber;
    ArrayList<String> al_devicecontacts;
    private Dialog dialog;
    Activity context;
    private Timer timer = new Timer();
    ArrayList<ContactModel>al_contactmodel;
    public  Handler h = new Handler();
    public  int delay = 4000; //15 seconds
    public Runnable runnable;

    public SwipeRefreshLayout mSwipeRefreshLayout;
    @SuppressLint("ValidFragment")
    public ContactsFragmentEdit(ContactsActivity contactsActivity) {
        this.context=contactsActivity;

    }
    public ContactsFragmentEdit()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        View dashboardView = inflater.inflate(R.layout.fragment_contact_edit, container, false);
        setCustomActionBar("Contacts");
        al_devicecontacts=new ArrayList<>();


        recyclerView = (RecyclerView)dashboardView.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout)dashboardView.findViewById(R.id.activity_main_swipe_refresh_layout);

        //recyclerView.setItemViewCacheSize(0);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //messageModelRealmResults = getRealmInstance().where(ContactModel.class).findAllSorted("name",true);
        /*ContactsAsync contactsAsync= new ContactsAsync();
        contactsAsync.execute();*/

        new ContactsAsync().execute();

        //messageModelRealmResults = getRealmInstance().where(ContactModel.class).findAllSorted("name",true);
        //mAdapter = new ContactsAdapter(messageModelRealmResults,getActivity());
        //recyclerView.setAdapter(mAdapter);
        //mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try
                {
                    refreshContent();
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


        return dashboardView;
    }

    public void refreshContent()
    {
        try
        {
            if (CheckConnection.isOnline(getActivity()))
            {
                new ContactsAsync().execute();
            }else
            {
                Toast.makeText(getActivity(), "There is no internet connection.", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private class ContactsAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            try {
                cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

                int contactIdIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
                int nameIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int phoneNumberIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int photoIdIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID);
                Log.e("ContactsFragmentEdit","name======="+nameIdx);
                Log.e("ContactsFragmentEdit","phoneNumber======="+phoneNumberIdx);

                //Log.e("ContactsFragment","al_devicecontacts======="+al_devicecontacts.size());
/////           contactCount = cursor.getCount();
//              System.out.println("Cursor length------> "+cursor.getCount());

                cursor.moveToFirst();
                do {
                    String idContact = cursor.getString(contactIdIdx);
                    name = cursor.getString(nameIdx);
                    phoneNumber = cursor.getString(phoneNumberIdx);
                    phoneNumber = phoneNumber.replaceAll(" ","");
                    al_devicecontacts.add(phoneNumber);

                    // System.out.println("phon = " + phoneNumber);

                    String number =  phoneNumber.length() > 8 ? phoneNumber.substring(phoneNumber.length() - 8) : phoneNumber;

                    Realm realm = Realm.getInstance(getActivity());

                    RealmResults<ContactModel> contactNumber = realm.where(ContactModel.class).endsWith("number",number).findAll();

                    Log.e("ContactsFragmentEdit","contactNumber======="+contactNumber.size());

                    if(contactNumber.size()==0){
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                ContactModel callsModel = realm.createObject(ContactModel.class); // Create managed objects directly
                                callsModel.setNumber(phoneNumber);
                                callsModel.setName(name);
                            }
                        });
                    }else
                    {
                        for(int i=0;i<contactNumber.size();i++)
                        {
                            Log.e("ContactsFragmentEdit","getName======="+contactNumber.get(i).getName());
                            Log.e("ContactsFragmentEdit","getNumber======="+contactNumber.get(i).getNumber());
                            Log.e("ContactsFragmentEdit","getPicture======="+contactNumber.get(i).getPicture());
                        }

                    }

                      //realm.close();
//                    realm.executeTransaction(new Realm.Transaction() {
//                        @Override
//                        public void execute(Realm realm) {
//                            ContactModel callsModel = realm.createObject(ContactModel.class); // Create managed objects directly
//                            callsModel.setNumber(phoneNumber);
//                            callsModel.setName(name);
////                        callsModel.setPicture(finalImage_uri);
//                        }
//                    });
//
//                    realm.close();

                    //...
                } while (cursor.moveToNext());

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();

                }
            }



            return null;
        }

        @Override
        protected void onPostExecute(String result) {

//            if(godashboard) {
//                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
//            }else{
//                Intent intent = new Intent(getApplicationContext(), StatusActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
//            }


            /*ContactsUpload contactsUpload= new ContactsUpload();

            contactsUpload.execute();*/
            Log.e("ContactsFragmentEdit","Enterintoonpostexcute=======");
            new ContactsUpload().execute();

//            ExceuteHandler();
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog();
            //messageModelRealmResults.clear();
        }
    }
    private class ContactsUpload extends AsyncTask<String, Void, String> {
        JSONObject userData;
        JSONObject userData1 = new JSONObject();
        JSONArray userDataArray = new JSONArray();
        // Realm realm = Realm.getInstance(context);
        @Override
        protected String doInBackground(String... params) {
            Log.e("ContactsFragmentEdit","Enterintodoinback=======");
            Realm realm = Realm.getInstance(getActivity());

            RealmResults<ContactModel> contactModel = realm.where(ContactModel.class).findAll();

//            System.out.println("contacyt Lisyt "+contactModel);
            Log.e("ContactsFragmentEdit","EnterintodoinbackcontactModel======="+contactModel.size());
            try {

                for(int i =0 ;i<contactModel.size();i++){

                    userData = new JSONObject();
                    userData.put("UserName", contactModel.get(i).getName().toString());
                    userData.put("MobileNumber", contactModel.get(i).getNumber().toString());

                    userDataArray.put(userData);

                }

                userData1.put("Contacts",userDataArray);
                userData1.put("AccessToken", Constants.accessToken);
//                userData1.put("AccessToken",Constants.accessToken);


            }catch(Exception e){
                e.printStackTrace();
            }

            //realm.close();

//            System.out.println("contactModel userData = " + userData1);

            new GetJSONResponse(getActivity()).RequestJsonToServerArray(getActivity(),"DetachContacts", userData1, new VolleyInterface() {

                @Override
                public void onSuccessResponse(final JSONObject result) {

//                    System.out.println("result in Splash Activity = " + result);


                }

                @Override
                public void onErroeResponse(VolleyError result) {

                }

                @Override
                public void onSuccessResponse(JSONArray result) {

//                    System.out.println("Contacts in Splash Activity = " + result);

                    Log.e("ContactsFragmentEdit","Updateresult======="+result);


                    getRealmInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            //realm.clear(HasAppModel.class);
                            //realm.close();
                        }
                    });
                    if(result.length()==0||result.equals("null"))
                    {
                        Log.e("ContactsFragmentEdit","resultnull=======");
                        messageModelRealmResults = getRealmInstance().where(ContactModel.class).findAllSorted("name",true);
                        mAdapter = new ContactsAdapter(messageModelRealmResults, getActivity());
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        dismissProgressDialog();
                        //realm.close();
                    }else
                    {
                        Log.e("ContactsFragmentEdit","resultnotequaltonull=======");
                        try {
                            JSONArray jsonArray=new JSONArray(result.toString());
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                final JSONObject jsonObj = jsonArray.getJSONObject(i);
                                String number =  jsonObj.getString("MobileNumber");
                                number =  number.length() > 8 ? number.substring(number.length() - 8) : number;

                                Log.e("ContactsFragmentEdit","resultnull=======");

                                Realm realm = Realm.getInstance(context);

                                RealmResults<HasAppModel> hasAppModel = realm.where(HasAppModel.class).endsWith("number",number).findAll();

                                if(hasAppModel.size() == 0) {

                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {

                                            HasAppModel hasAppModel = realm.createObject(HasAppModel.class); // Create managed objects directly

                                            Log.e("ContactsFragmentEdit","hasAppModelsize==0");
                                            try{

                                                hasAppModel.setNumber(jsonObj.getString("MobileNumber"));
                                                hasAppModel.setAccessToken(jsonObj.getString("AccessToken"));
                                                hasAppModel.setCalAllowStatus("0");
                                                hasAppModel.setStatusName(jsonObj.getString("Status"));
                                                hasAppModel.setAvailTime(jsonObj.getString("EndTime"));
                                                hasAppModel.setIsActive(String.valueOf(jsonObj.getBoolean("IsActive")));
                                                hasAppModel.setId(jsonObj.getString("DetachUserStatusID"));

                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }finally {
                                                realm.commitTransaction();
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    Log.e("ContactsFragmentEdit","hasAppModelsizeelse");
                                    try
                                    {
                                        realm.beginTransaction();
                                        hasAppModel.get(0).setAccessToken(jsonObj.getString("AccessToken"));
                                        hasAppModel.get(0).setCalAllowStatus("0");
                                        hasAppModel.get(0).setStatusName(jsonObj.getString("Status"));
                                        hasAppModel.get(0).setAvailTime(jsonObj.getString("EndTime"));
                                        hasAppModel.get(0).setIsActive(String.valueOf(jsonObj.getBoolean("IsActive")));
                                        hasAppModel.get(0).setId(jsonObj.getString("DetachUserStatusID"));
                                        Log.e("ContactsFragmentEdit","setIsActive"+String.valueOf(jsonObj.getBoolean("IsActive")));
                                    }catch(Exception e)
                                    {
                                        e.printStackTrace();
                                    }finally {
                                        realm.commitTransaction();
                                    }

                                }

                                realm.close();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            h.postDelayed(new Runnable() {
                public void run() {
                    //do something

                    try{
                        runnable=this;
                        //Toast.makeText(getActivity(),"handlercall",Toast.LENGTH_SHORT).show();
                        messageModelRealmResults = getRealmInstance().where(ContactModel.class).findAllSorted("name",true);
                        mAdapter = new ContactsAdapter(messageModelRealmResults,getActivity());
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        dismissProgressDialog();
                        //h.postDelayed(runnable, delay);
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }, delay);

        }

        @Override
        protected void onPreExecute() {

        }

    }
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
}
