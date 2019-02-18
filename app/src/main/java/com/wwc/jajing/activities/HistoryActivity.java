package com.wwc.jajing.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.wwc.R;
import com.wwc.jajing.fragment.DashboardFragment;
import com.wwc.jajing.fragment.HistoryFragment;
import com.wwc.jajing.fragment.StartTimerDialogFrament;
public class HistoryActivity extends BaseActivity {


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
