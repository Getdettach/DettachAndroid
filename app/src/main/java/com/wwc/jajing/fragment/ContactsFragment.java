package com.wwc.jajing.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wwc.R;
import com.wwc.jajing.activities.SplashActivity;
import com.wwc.jajing.component.RoundedImageView;
import com.wwc.jajing.realmDB.ContactModel;
import com.wwc.jajing.realmDB.HasAppModel;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.wwc.jajing.system.JJApplication.getRealmInstance;

/**
 * Created by Vivek on 25-09-2016.
 */
public class ContactsFragment extends BaseFragment {

    private LinearLayout call_lay;
    RealmResults<ContactModel> messageModelRealmResults;
    String name;
    String phoneNumber;

    LinearLayout loader;

    Realm realm;

    Cursor cursor ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        View dashboardView = inflater.inflate(R.layout.fragment_contacts, container, false);
        setCustomActionBar("Contacts");
        call_lay = (LinearLayout) dashboardView.findViewById(R.id.call_lay);

        loader = (LinearLayout)dashboardView.findViewById(R.id.loader);

        loader.setVisibility(View.GONE);

//        getRealmInstance().executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                realm.clear(ContactModel.class);
//            }
//        });

        showProgressDialog();

//        showView();

        ContactsAsync contactsAsync= new ContactsAsync();
        contactsAsync.execute();


//        try {
//            messageModelRealmResults = getRealmInstance().where(ContactModel.class).findAll();
//            customOptions(messageModelRealmResults);
//        }catch(Exception e){
//
//        }



        return dashboardView;
    }

//    public void showView(){
//
//        messageModelRealmResults = getRealmInstance().where(ContactModel.class).findAll();
//
////        System.out.println("messageModelRealmResults.size() "+messageModelRealmResults.size());
////        System.out.println("SplashActivity.contactCount "+SplashActivity.contactCount);
//
//        if(SplashActivity.contactCount == messageModelRealmResults.size()){
////            customOptions(messageModelRealmResults);
////            dismissProgressDialog();
//
//            ContactsAsync contactsAsync= new ContactsAsync();
//            contactsAsync.execute();
//
//        }else{
//            ExceuteHandler();
//        }
//    }



//    public void ExceuteHandler(){
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                System.out.println("Handler Executed");
//
//                showView();
//
//            }
//        }, 2000);
//    }



    private class ContactsAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

//            try {
//                cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
//                int contactIdIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
//                int nameIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
//                int phoneNumberIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//                int photoIdIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID);
//                cursor.moveToFirst();
//                do {
//                    String idContact = cursor.getString(contactIdIdx);
//                    name = cursor.getString(nameIdx);
//                    phoneNumber = cursor.getString(phoneNumberIdx);
//                    getRealmInstance().executeTransaction(new Realm.Transaction() {
//                        @Override
//                        public void execute(Realm realm) {
//                            ContactModel callsModel = realm.createObject(ContactModel.class); // Create managed objects directly
//                            callsModel.setNumber(phoneNumber);
//                            callsModel.setName(name);
////                        callsModel.setPicture(finalImage_uri);
//                        }
//                    });
//                    //...
//                } while (cursor.moveToNext());
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (cursor != null) {
//                    cursor.close();
//                }
//            }



//            try {
//
//                showProgressDialog();
//
//                realm = Realm.getInstance(getActivity());
//
//                messageModelRealmResults = getRealmInstance().where(ContactModel.class).findAllSorted("name",true);
//
//                System.out.println("messageModelRealmResults size = " + messageModelRealmResults.size());
//
//                messageModelRealmResults = getRealmInstance().where(ContactModel.class).findAll();
//                customOptions(messageModelRealmResults);
//            }catch(Exception e){
//
//                e.printStackTrace();
//
//            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            try {

//                showProgressDialog();

                messageModelRealmResults = getRealmInstance().where(ContactModel.class).findAllSorted("name",true);
//
                customOptions(messageModelRealmResults);

//                realm.close();


                System.out.println("messageModelRealmResults = " + messageModelRealmResults.size());


            }catch(Exception e){

                e.printStackTrace();

            }
            dismissProgressDialog();

        }


        @Override
        protected void onPreExecute() {

            showProgressDialog();

        }

    }


    private void dialNumber(String no) {
        Uri number = Uri.parse("tel:" + no);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }

    private void customOptions(RealmResults<ContactModel> optionsModelRealmResults) {
        if (call_lay.getChildCount() > 0)
            call_lay.removeAllViews();

        LayoutInflater layoutInflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < optionsModelRealmResults.size(); i++) {
            final ContactModel obj = optionsModelRealmResults.get(i);
            View view = layoutInflator.inflate(R.layout.inflator_contacts, null);

            LinearLayout bgLayout = (LinearLayout)view.findViewById(R.id.contact_layout);

            if (i%2 == 0){
                bgLayout.setBackgroundColor(Color.parseColor("#20FFFFFF"));
            }
            else{
                bgLayout.setBackgroundColor(Color.parseColor("#000000"));
            }
            final TextView name = (TextView) view.findViewById(R.id.name);
            final TextView number = (TextView) view.findViewById(R.id.number);
            final ImageView availState = (ImageView)view.findViewById(R.id.img_avail_status);
            final TextView statusname = (TextView)view.findViewById(R.id.status);

            String numberCheck =  obj.getNumber().length() > 8 ? obj.getNumber().substring(obj.getNumber().length() - 8) : obj.getNumber();

            Log.e("ContactsFragment","numbercheck"+numberCheck);
            Log.e("ContactsFragment","obj.getNumber()"+obj.getNumber());


            Realm realm  = Realm.getInstance(getActivity());

            RealmResults<HasAppModel> contactNumber = realm.where(HasAppModel.class).endsWith("number",numberCheck).findAll();


            if(contactNumber.size()>0){

                statusname.setText(contactNumber.get(0).getStatusName());

                if(contactNumber.get(0).getIsActive().compareTo("true")==0){
                    availState.setImageResource(R.drawable.icon_red);

                }else{

                    availState.setImageResource(R.drawable.icon_green);
                    statusname.setText("");
                }
            }else{
                availState.setImageResource(R.drawable.icon_orang);
            }

//            RoundedImageView picture = (RoundedImageView) view.findViewById(R.id.picture);
            name.setText(obj.getName());
            number.setText(obj.getNumber());
//            Picasso.with(getActivity()).load(obj.getPicture()).into(picture);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialNumber(number.getText().toString());
                }
            });
            call_lay.addView(view);

            realm.close();
        }
    }

}
