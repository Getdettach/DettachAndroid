package com.wwc.jajing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wwc.R;
import com.wwc.jajing.activities.AwayOptionActivity;


public class SetStatusFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    View dashboardView;

    Button btnSetStatus;

    // TODO: Rename and change types of parameters

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        dashboardView = inflater.inflate(R.layout.fragment_set_status, container, false);

        setCustomActionBar("Status");

        this.btnSetStatus = (Button)dashboardView.findViewById(R.id.btn_set_status);

        btnSetStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent awayIntent = new Intent(getActivity(), AwayOptionActivity.class);
                startActivity(awayIntent);
                getActivity().finish();

            }
        });

        return dashboardView;
    }


}
