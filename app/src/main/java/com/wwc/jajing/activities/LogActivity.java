package com.wwc.jajing.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.wwc.R;
import com.wwc.jajing.fragment.HistoryFragment;
import com.wwc.jajing.fragment.LogFragment;
import com.wwc.jajing.fragment.MissedCallFragment;

/**
 * Created by vivek_e on 9/12/2016.
 */

public class LogActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        toolBarView();

        onClickFragmentNavigation(new HistoryFragment());
    }

    /**
     * On Back Press Event.
     */
    @Override
    public void onBackPressed() {
        Fragment activeFragment = getActiveFragment();
        if (activeFragment != null) {
            if (activeFragment instanceof HistoryFragment) {
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
