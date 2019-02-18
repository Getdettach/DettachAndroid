package com.wwc.jajing.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wwc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vivek_e on 9/12/2016.
 */
public class LogFragment extends BaseFragment {
    private TabLayout tabLayout;
    private static ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        View dashboardView = inflater.inflate(R.layout.fragment_missed_log, container, false);
        setCustomActionBar("Dettach Logs");
        viewPager = (ViewPager) dashboardView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) dashboardView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return dashboardView;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFrag(new MissedCallFragment(), "Calls");
        adapter.addFrag(new MissedMessageFragment(), "Messages");
//        adapter.addFrag(new ContactsFragment(), "Contacts");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
