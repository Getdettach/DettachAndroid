package com.wwc.jajing.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.wwc.R;
import com.wwc.jajing.fragment.NavigationDrawerFragment;
import com.wwc.jajing.interfaces.FragmentNavigator;


public class BaseActivity extends AppCompatActivity implements FragmentNavigator {

    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private FrameLayout main_fragment_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void toolBarView() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        main_fragment_container = (FrameLayout) findViewById(R.id.container);
        NavigationDrawerFragment mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        setSupportActionBar(mToolbar);
        mNavigationDrawerFragment.setCustomActionBar();
        navigationSetUp(mDrawerLayout, mToolbar, main_fragment_container);
    }

    /**
     * Setup Navigation Drawer
     */

    private void navigationSetUp(DrawerLayout mDrawerLayout, Toolbar mToolbar, FrameLayout main_fragment_container) {
        this.mDrawerLayout = mDrawerLayout;
        this.mToolbar = mToolbar;
        this.main_fragment_container = main_fragment_container;
        if (mDrawerLayout != null) {
            // set a custom shadow that overlays the main content when the drawer opens
            mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
            // ActionBarDrawerToggle ties together the the proper interactions
            // between the navigation drawer and the action bar app icon.
            mDrawerToggle = new ActionBarDrawerToggle(
                    this,                    /* host Activity */
                    mDrawerLayout,                    /* DrawerLayout object */
                    mToolbar,             /* nav drawer image to replace 'Up' caret */
                    R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                    R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
            ) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    if (getActiveFragment() != null && !getActiveFragment().isAdded()) {
                        return;
                    }
                }

                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    super.onDrawerSlide(drawerView, slideOffset);
                    View view = getCurrentFocus();
                    if (view != null)
                        hideKeyboard(view);
                }
            };

            // Defer code dependent on restoration of previous instance state.
            mDrawerLayout.post(new Runnable() {
                @Override
                public void run() {
                    if (mDrawerToggle != null)
                        mDrawerToggle.syncState();
                }
            });
            mDrawerLayout.setDrawerListener(mDrawerToggle);
        }
    }

    /**
     * Called by the system when the device configuration changes.
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        if (mDrawerToggle != null)
            mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * (non-Javadoc) Description:
     *
     * @param menu - options menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return mNavigationDrawerFragment != null && !mNavigationDrawerFragment.isDrawerOpen() || super.onCreateOptionsMenu(menu);
    }

    /**
     * This is action being taken when a fragment is called.
     * Current fragment will be added to backStack is isStackNeeded is true, before invoking any callable fragment.
     *
     * @param fragment
     * @param isStackNeeded
     */
    private void callPrimaryFragment(Fragment fragment, boolean isStackNeeded) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        boolean fragmentPopped = fm.popBackStackImmediate(fragment.getClass().getName(), 0);
        if (!fragmentPopped && fm.findFragmentByTag(fragment.getClass().getName()) == null) {
            if (main_fragment_container != null) {
                ft.replace(main_fragment_container.getId(), fragment, fragment.getClass().getName());
            }
            if (isStackNeeded) {
                ft.addToBackStack(fragment.getClass().getName());
            }
            ft.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * Hide soft keyboard
     *
     * @param view
     */
    private void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    /**
     * (non-Javadoc) Description:
     *
     * @see android.support.v4.app.FragmentActivity#onBackPressed()
     */
    @Override
    public void onClickFragmentNavigation(Fragment fragment) {
        if (isSimilarFragment(fragment)) {
            toggleNavigationDrawer();
        } else {
            callPrimaryFragment(fragment, true);
        }
    }
    private boolean isSimilarFragment(Fragment fragment) {
        return fragment != null && getActiveFragment() != null && fragment.getClass().getName().equals(getActiveFragment().getClass().getName());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    @Override
    public void onClickFragmentNavigation(Fragment fragment, boolean isStackNeeded) {
        callPrimaryFragment(fragment, isStackNeeded);
    }

    @Override
    public Fragment getActiveFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    @Override
    public void toggleNavigationDrawer()
    {
        if (mDrawerLayout.isDrawerVisible(GravityCompat.START))
        {
            mDrawerLayout.closeDrawer(GravityCompat.START);

        } else
        {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean isNavigationDrawerShowing()
    {
        return mDrawerLayout.isDrawerVisible(GravityCompat.START);
    }

    @Override
    public void updateNavigationMenuList(Bundle arguments)
    {
        if (arguments != null)
        {
            mNavigationDrawerFragment = new NavigationDrawerFragment();
            mNavigationDrawerFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.navigation_drawer, mNavigationDrawerFragment).commitAllowingStateLoss();

        } else
        {
            mNavigationDrawerFragment = new NavigationDrawerFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.navigation_drawer, mNavigationDrawerFragment).commitAllowingStateLoss();
        }
    }
}
