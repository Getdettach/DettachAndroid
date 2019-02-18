package com.wwc.jajing.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.wwc.R;
import com.wwc.jajing.interfaces.ActivityNavigator;
import com.wwc.jajing.interfaces.FragmentNavigator;

import java.util.UUID;


/**
 * BaseFragment will be the base class for all fragment.
 *
 * @version 1.0
 */
@SuppressLint("NewApi")
public class BaseFragment extends Fragment implements ActivityNavigator {

    private Context context;
    private Dialog dialog = null;
    private static BaseFragment topFragment;
    private static BaseFragment topAliveFragment;
    protected FragmentNavigator mFragmentNavigator;
    public SharedPreferences user_details_pref;
    public Animation bottomUp;
    public Animation upBottom;
    public android.support.v7.app.AlertDialog commonAlertDialog;

    private static BaseFragment getTopAliveFragment() {
        return topAliveFragment;
    }

    private static BaseFragment getTopFragment() {
        return topFragment;
    }


    /**
     * (non-Javadoc) Description:
     *
     * @param savedInstanceState - saved instance state
     * @see Fragment#onActivityCreated(Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mFragmentNavigator = (FragmentNavigator) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    /**
     * (non-Javadoc) Description:
     * \
     *
     * @param requestCode - request code
     * @param resultCode  - result code
     * @param data        - data
     * @see Fragment#onActivityResult(int, int,
     * Intent)
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState - saved instance state
     * @see Fragment#onCreate(Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getActivity();
        } catch (Exception e) {
            handleException(e);
        }
    }


    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           - inflater
     * @param container          - container
     * @param savedInstanceState - saved instance state
     * @return view
     * @see Fragment#onCreateView(LayoutInflater,
     * ViewGroup, Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return null;
    }


    /**
     * Called when the fragment is no longer in use.
     *
     * @see Fragment#onDestroy()
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        boolean isDestroyed = true;
        if (topAliveFragment == this) {
            topAliveFragment = null;
        }
        context = null;
    }

    /**
     * Called when the Fragment is no longer resumed.
     *
     * @see Fragment#onPause()
     */
    @Override
    public void onPause() {
        super.onPause();
        topFragment = null;
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();
        if (view != null && imm.isAcceptingText())
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Returns the current active fragment
     *
     * @return current fragment
     */
    protected Fragment getActiveFragment() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName();
        return getFragmentManager().findFragmentByTag(tag);
    }

    /**
     * Called when the fragment is visible to the user and actively running
     *
     * @see Fragment#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
        topFragment = BaseFragment.this;
        topAliveFragment = BaseFragment.this;
    }


    /**
     * Called when the Fragment is visible to the user.
     *
     * @see Fragment#onStart()
     */
    @Override
    public void onStart() {
        super.onStart();

    }


    /**
     * Called when the Fragment is no longer started.
     *
     * @see Fragment#onStop()
     */
    @Override
    public void onStop() {
        super.onStop();

    }

    /**
     * Initialize the contents of the standard options menu.
     *
     * @param menu     - menu
     * @param inflater - inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     *
     * @param item - menu item
     * @return boolean
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * @param item
     * @return
     */
    public boolean onContextItemSelected(MenuItem item) {
        return false;
    }


    /**
     * estimate exception type
     *
     * @param e
     */
    private void handleException(Exception e) {
        e.printStackTrace();
    }


    /**
     * Use custom layout for action bar font size based on layout size used
     * by device
     *
     * @param title
     */
    protected void setCustomActionBar(String title) {
        if (getActionBar() != null) {
            try {
                getActionBar().setDisplayShowCustomEnabled(true);
                getActionBar().setDisplayShowTitleEnabled(false);
                getActionBar().setDisplayHomeAsUpEnabled(false);
                getActionBar().setHomeButtonEnabled(true);
                getActionBar().setDisplayShowHomeEnabled(true);
                getActionBar().setIcon(android.R.color.transparent);
            } catch (Exception e) {
                e.printStackTrace();
            }

            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            @SuppressLint("InflateParams")
            View customView = inflater.inflate(R.layout.actionbar_text, null);
            TextView mText = ((TextView) customView.findViewById(R.id.title));
            mText.setText(title);
            getActionBar().setCustomView(customView);
//                    getActionBar().setTitle("");
//                    TextView mTitle = (TextView) MeritlaneApplication.getmToolbar().findViewById(R.id.tool_bar_title);
//            mTitle.setText(title);
        }
    }

    protected void hideHome() {
        if (getActionBar() != null) {
            getActionBar().setDefaultDisplayHomeAsUpEnabled(false);
            getActionBar().setDisplayShowHomeEnabled(false);
        }
    }

    /**
     * Get Action Bar
     *
     * @return
     */
    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    /**
     * Get Device Id
     *
     * @return
     */
    public String getDeviceID() {
        String deviceId = UUID.randomUUID().toString();
        try {
            deviceId = android.provider.Settings.Secure.getString(BaseFragment.this.getContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceId;
    }


    /**
     * Get IMEI Number
     *
     * @param ctx
     * @return
     */
    private String getIMEINumber(Context ctx) {
        String id = "";

        try {
            if (ctx != null) {
                TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
                id = telephonyManager.getDeviceId();
                return id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;

    }

    public Context getContext() {
        return getActivity();
    }

    /**
     * Creates and displays a progress dialog if one does not already exist
     */
    protected void showProgressDialog() {
        try {
            if (dialog == null) {
                dialog = new Dialog(getActivity(), R.style.CustomProgressTheme);
                dialog.setContentView(R.layout.custom_progress);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Dismiss Progress Dialog
     */
    protected void dismissProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dialog = null;
    }

    public void activityStackHandling(Context context, Intent intent) {
        String className = "class " + getContext().getPackageName();
        if (!isSimilarActivity(className + ".jajing.activities.DashboardActivity")) {
            getActivity().finish();
        }
        startActivity(intent);
    }

    public boolean isSimilarActivity(String activityClass) {
        return activityClass != null && getActivity() != null && getActivity().getClass().toString().equals(activityClass);
    }

    /**
     * Clear the top of the stack
     */
    protected void clearBackStack() {
        FragmentManager manager = getFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void clearAllStack() {
        FragmentManager fm = getFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    @Override
    public void onBackPressed() {
    }
}
