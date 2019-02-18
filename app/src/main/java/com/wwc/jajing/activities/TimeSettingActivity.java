package com.wwc.jajing.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.wwc.R;
import com.wwc.jajing.fragment.DashboardFragment;
import com.wwc.jajing.fragment.MenuTimeSettingFragment;
import com.wwc.jajing.fragment.TimeSettingFragment;

/**
 * Created by Vivek on 18-09-2016.
 */

public class TimeSettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        toolBarView();
//        onClickFragmentNavigation(new TimeSettingFragment());
        onClickFragmentNavigation(new MenuTimeSettingFragment());
    }

    /**
     * On Back Press Event.
     */
    @Override
    public void onBackPressed() {
        Fragment activeFragment = getActiveFragment();
        if (activeFragment != null) {
            if (activeFragment instanceof MenuTimeSettingFragment) {
                if (isNavigationDrawerShowing()) {
                    toggleNavigationDrawer();
                } else {
                    finish();
                }
            } else {
                onBackHandling(activeFragment);
            }
        } else {
            this.finish();
        }
    }

    private void onBackHandling(Fragment activeFragment) {
//        if (activeFragment instanceof DashboardFragment) {
//            ((DashboardFragment) activeFragment).onBackPressed();
//        }
    }
}