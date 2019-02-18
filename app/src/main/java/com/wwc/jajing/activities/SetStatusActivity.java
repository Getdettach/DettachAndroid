package com.wwc.jajing.activities;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.wwc.R;
import com.wwc.jajing.fragment.SetStatusFragment;
import com.wwc.jajing.fragment.TimeSettingFragment;

public class SetStatusActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        toolBarView();
        onClickFragmentNavigation(new SetStatusFragment());
    }

    /**
     * On Back Press Event.
     */
    @Override
    public void onBackPressed() {
        Fragment activeFragment = getActiveFragment();
        if (activeFragment != null) {
            if (activeFragment instanceof SetStatusFragment) {
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
