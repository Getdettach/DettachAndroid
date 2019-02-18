package com.wwc.jajing.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wwc.R;
import com.wwc.jajing.activities.ContactsActivity;
import com.wwc.jajing.activities.DashboardActivity;
import com.wwc.jajing.activities.HistoryActivity;
import com.wwc.jajing.activities.LogActivity;
import com.wwc.jajing.activities.MessageActivity;
import com.wwc.jajing.activities.SetStatusActivity;
import com.wwc.jajing.activities.StatusActivity;
import com.wwc.jajing.activities.TimeSettingActivity;
import com.wwc.jajing.adapter.MenuListAdapter;
import com.wwc.jajing.component.Section;
import com.wwc.jajing.component.SectionItem;
import com.wwc.jajing.domain.entity.User;
import com.wwc.jajing.system.JJSystemImpl;

import java.util.List;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends BaseFragment {

    private DrawerLayout mDrawerLayout;
    private View mFragmentContainerView;
    private Fragment fragment = null;

    private final int homeMenuId = Menu.FIRST;
    private final int historyMenuId = Menu.FIRST + 1;
//    private final int settingMenuId = Menu.FIRST + 2;
//    private final int statusMenuId = Menu.FIRST + 3;

    private final int timeSettingsId = Menu.FIRST + 2;
    private final int contactsMenuId = Menu.FIRST + 3;

//    private final int historyMenuId = Menu.FIRST;
//    private final int messageMenuId = Menu.FIRST + 1;
//    private final int settingMenuId = Menu.FIRST + 2;
//    private final int statusMenuId = Menu.FIRST + 3;
//    private final int contactsMenuId = Menu.FIRST + 4;

    public NavigationDrawerFragment() {

    }

    User user = (User) JJSystemImpl.getInstance().getSystemService(
            JJSystemImpl.Services.USER);

    /**
     * @param savedInstanceState - saved instance state
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        mFragmentContainerView = getActivity().findViewById(R.id.navigation_drawer);
    }

    /**
     * @param inflater           - inflater
     * @param container          - container
     * @param savedInstanceState - saved instance state
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        ListView mDrawerListView = (ListView) view.findViewById(R.id.listView);
        LinearLayout navigationLayout = (LinearLayout) view.findViewById(R.id.navigationLayout);
        navigationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(mFragmentContainerView);
            }
        });
        mDrawerListView.setOnItemClickListener(listener);
        List<SectionItem> sectionList = createMenu();
        MenuListAdapter menuListAdapter = new MenuListAdapter(getActivity(), sectionList);
        mDrawerListView.setAdapter(menuListAdapter);
        menuListAdapter.notifyDataSetChanged();
        return view;
    }


    /**
     * Click Listener for each list item from navigation drawer.
     */
    private final ListView.OnItemClickListener listener = new ListView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            String className = "class " + getContext().getPackageName();
            switch (view.getId()) {
                case historyMenuId:
                   /* if (!isSimilarActivity(className + ".jajing.activities.HistoryActivity")) {
                        activityStackHandling(getActivity(), new Intent(getActivity(), HistoryActivity.class));
                    }*/
                    activityStackHandling(getActivity(), new Intent(getActivity(), HistoryActivity.class));
                    break;
//                case settingMenuId:
//                    if (!isSimilarActivity(className + ".jajing.activities.TimeSettingActivity")) {
////                        activityStackHandling(getActivity(), new Intent(getActivity(), TimeSettingActivity.class));
//                    }
//                    break;
//
//                case statusMenuId:
//                    if (!isSimilarActivity(className + ".jajing.activities.StatusActivity")) {
//                        activityStackHandling(getActivity(), new Intent(getActivity(), StatusActivity.class));
//                    }
//                    break;

                case timeSettingsId:


                    if(user.getUserStatus().getAvailabilityStatus().compareTo("AVAILABLE")==0) {

                        if (!isSimilarActivity(className + ".jajing.activities.TimesettingsActivity")) {
                            activityStackHandling(getActivity(), new Intent(getActivity(), TimeSettingActivity.class));
                        }
                    }else{
                        if (!isSimilarActivity(className + ".jajing.activities.StatusActivity")) {
                            Toast.makeText(getActivity(),"All ready set the time",Toast.LENGTH_SHORT);
                            activityStackHandling(getActivity(), new Intent(getActivity(), StatusActivity.class));


                        }
                    }
                    break;

                case contactsMenuId:
                    /*if (!isSimilarActivity(className + ".jajing.activities.ContactsActivity")) {
                        activityStackHandling(getActivity(), new Intent(getActivity(), ContactsActivity.class));
                    }*/
                    activityStackHandling(getActivity(), new Intent(getActivity(), ContactsActivity.class));
                    break;
//                case messageMenuId:
//                    if (!isSimilarActivity(className + ".jajing.activities.MessageActivity")) {
//                        activityStackHandling(getActivity(), new Intent(getActivity(), MessageActivity.class));
//                    }
//                    break;

                case homeMenuId:

                    if(user.getUserStatus().getAvailabilityStatus().compareTo("AVAILABLE")==0) {
                        if (!isSimilarActivity(className + ".jajing.activities.SetStatusActivity")) {
                            activityStackHandling(getActivity(), new Intent(getActivity(), SetStatusActivity.class));
                        }
                    }else{
                        if (!isSimilarActivity(className + ".jajing.activities.StatusActivity")) {
                            activityStackHandling(getActivity(), new Intent(getActivity(), StatusActivity.class));
                        }
                    }
                    break;

                default:
                    break;
            }
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
    };

    /**
     * Checks the current status of the navigation drawer whether it is in open or closed state.
     *
     * @return
     */
    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Add Items to Navigation drawer.
     *
     * @return
     */
    private List<SectionItem> createMenu() {

        Section section = new Section();
        // Common menu items between all activities
        section.addSectionItem(homeMenuId, R.string.home, "ic_home");
        section.addSectionItem(historyMenuId, R.string._history, "ic_call_white");
//        section.addSectionItem(messageMenuId, R.string.message, "ic_chat_white");
//        section.addSectionItem(settingMenuId, R.string.time_setting, "ic_alarm_white");
//        section.addSectionItem(statusMenuId, R.string.status, "ic_event_note_white");
        section.addSectionItem(timeSettingsId, R.string.time_setting, "ic_alarm_white");
        section.addSectionItem(contactsMenuId, R.string.contact, "ic_contacts_white");
        return section.getSectionItems();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDrawerLayout = null;
        mFragmentContainerView = null;
    }

    /**
     * Use custom layout for action bar font size based on layout size used
     * by device
     */
    public void setCustomActionBar() {
        if (getActionBar() != null) {
            try {
                getActionBar().setDisplayShowCustomEnabled(false);
                getActionBar().setDisplayShowTitleEnabled(false);
                getActionBar().setDisplayHomeAsUpEnabled(false);
                getActionBar().setHomeButtonEnabled(false);
                getActionBar().setDisplayShowHomeEnabled(false);
                getActionBar().setIcon(android.R.color.transparent);
            } catch (Exception e) {
                e.printStackTrace();
            }

            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View customView = inflater.inflate(R.layout.actionbar_text, null);
            ((TextView) customView.findViewById(R.id.title)).setText("Home");
            getActionBar().setCustomView(customView);
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
}