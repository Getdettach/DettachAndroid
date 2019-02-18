package com.wwc.jajing.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.orm.SugarDb;
import com.wwc.R;
import com.wwc.jajing.fragment.DashboardFragment;

/**
 * Created by Vivek Droid on 4/14/2016.
 */
public class DashboardActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        toolBarView();
        onClickFragmentNavigation(new DashboardFragment());

    }

    /**
     * On Back Press Event.
     */
    @Override
    public void onBackPressed() {
        Fragment activeFragment = getActiveFragment();
        if (activeFragment != null) {
            if (activeFragment instanceof DashboardFragment) {
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
