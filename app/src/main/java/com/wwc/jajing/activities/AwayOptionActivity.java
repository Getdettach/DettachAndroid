package com.wwc.jajing.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.wwc.R;
import com.wwc.jajing.fragment.AwayOptionFragment;
import com.wwc.jajing.fragment.DashboardFragment;

/**
 * Created by vivek_e on 9/12/2016.
 */
public class AwayOptionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        toolBarView();
        onClickFragmentNavigation(new AwayOptionFragment());
    }

    /**
     * On Back Press Event.
     */
    @Override
    public void onBackPressed() {
        Fragment activeFragment = getActiveFragment();
        if (activeFragment != null) {
            if (activeFragment instanceof AwayOptionFragment) {
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
//        if (activeFragment instanceof CustomAvailabilityStatus) {
//            ((CustomAvailabilityStatus) activeFragment).onBackPressed();
//        }
    }
}
