package com.wwc.jajing.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.wwc.R;
import com.wwc.jajing.fragment.ContactsFragment;
import com.wwc.jajing.fragment.ContactsFragmentEdit;

/**
 * Created by Vivek on 25-09-2016.
 */
public class ContactsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        toolBarView();

        onClickFragmentNavigation(new ContactsFragmentEdit(this));
    }

    /**
     * On Back Press Event.
     */
    @Override
    public void onBackPressed() {
        Fragment activeFragment = getActiveFragment();
        if (activeFragment != null) {
            if (activeFragment instanceof ContactsFragmentEdit) {
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


}