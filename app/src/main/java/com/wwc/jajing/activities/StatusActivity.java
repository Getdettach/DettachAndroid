package com.wwc.jajing.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.wwc.R;
import com.wwc.jajing.fragment.MissedMessageFragment;
import com.wwc.jajing.fragment.StatusFragment;

/**
 * Created by Vivek on 25-09-2016.
 */
public class StatusActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        toolBarView();

        onClickFragmentNavigation(new StatusFragment());
    }

    /**
     * On Back Press Event.
     */
    @Override
    public void onBackPressed() {
        Fragment activeFragment = getActiveFragment();
        if (activeFragment != null) {
            if (activeFragment instanceof StatusFragment) {
                if (isNavigationDrawerShowing()) {
                    toggleNavigationDrawer();
                } else {
                    finish();
                }
            } else {
//                onBackHandling(activeFragment);
            }
        } else {
            this.finish();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        System.out.println("ON PAUSE");

    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("ON RESUME");
    }
}
