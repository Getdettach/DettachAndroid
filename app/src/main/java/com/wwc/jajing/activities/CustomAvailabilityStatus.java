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
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wwc.R;
import com.wwc.jajing.fragment.AwayOptionFragment;
import com.wwc.jajing.realmDB.CallsModel;
import com.wwc.jajing.realmDB.ContactModel;
import com.wwc.jajing.realmDB.OptionsModel;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.wwc.jajing.system.JJApplication.getRealmInstance;


public class CustomAvailabilityStatus extends DialogFragment implements DialogInterface.OnDismissListener, OnClickListener {

    Button save;
    EditText customMessage;
    EditText editCustomdesc;

    LinearLayout customLayout;
    SharedPreferences preferences;
    AwayOptionFragment activity;
    public static final int DIALOG_FRAGMENT = 1;

    public CustomAvailabilityStatus(AwayOptionFragment context)
    {
        this.activity=context;
    }

    public  CustomAvailabilityStatus()
    {

    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient mDialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.activity_custom_message, null);
        builder.setTitle("Custom Status");


        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        save = (Button) view.findViewById(R.id.buttonSaveCustomMessage);
        customMessage = (EditText) view.findViewById(R.id.editCustomMessage);
//        editCustomdesc = (EditText) view.findViewById(R.id.editCustomdesc);

        customLayout = (LinearLayout)view.findViewById(R.id.custom_layout);

        save.setOnClickListener(this);
        builder.setCancelable(true);
        // Inflate and set the layout for the mDialog
        // Pass null as the parent view because its going in the mDialog layout
        builder.setView(view);

        customMessage.setSingleLine();

        customMessage.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // hide virtual keyboard
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(customMessage.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });



        // Create the AlertDialog object and return it
        return builder.create();
    }

//    public static void hideSoftKeyboard(View view) {
//        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }

    @Override
    public void onClick(View view) {
        final String message = this.customMessage.getText().toString();
//        final String description = this.editCustomdesc.getText().toString();
        final String description ="";
        if (this.isValidCustomMessage(message, description)) {
            dismiss();

            final RealmResults<OptionsModel> optionDetails = getRealmInstance().where(OptionsModel.class).findAll();

            if(optionDetails.size() == 0) {

                getRealmInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        OptionsModel optionsModel = realm.createObject(OptionsModel.class); // Create managed objects directly
                        optionsModel.setMessage(message);
//                        optionsModel.setDescription(description);
                    }
                });

            }else{
                getRealmInstance().beginTransaction();
                optionDetails.get(0).setMessage(message);
                getRealmInstance().commitTransaction();
            }

          /*  getActivity().finish();

            Intent i = new Intent(getActivity(), AwayOptionActivity.class);
            this.startActivity(i);*/   //04 sat


            SettimeDialog menuFragment = new SettimeDialog();
            menuFragment.setCancelable(true);
            menuFragment.setTargetFragment(activity, DIALOG_FRAGMENT);

            menuFragment.show(getActivity().getSupportFragmentManager(), "CustomAvailabilityStatus");
            SharedPreferences.Editor edit=preferences.edit();
            edit.putString("status",message);
            edit.commit();
        }
    }

    private boolean isValidCustomMessage(String aCustomMessage, String aCustomDescription) {
        if (aCustomMessage.trim().equalsIgnoreCase("")) {
            customMessage.setError("Invalid Status");
            return false;
        }
//        else if (aCustomDescription.trim().equalsIgnoreCase("")) {
//            editCustomdesc.setError("Invalid Description");
//            return false;
//        }
        else
            return true;
    }



}
