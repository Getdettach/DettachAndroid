package com.wwc.jajing.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.wwc.R;
import com.wwc.jajing.activities.HistoryActivity;
import com.wwc.jajing.domain.entity.MissedMessage;
import com.wwc.jajing.realmDB.CallsModel;
import com.wwc.jajing.realmDB.ContactModel;

import io.realm.Realm;
import io.realm.RealmResults;


public class   HistoryFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private RelativeLayout rl_clearall;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String str_delete,str_deletetab1;
    public static String str_tab1,str_tab2;
    public static String str_deletedialog;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static  int tabposition;

//    private OnFragmentInteractionListener mListener;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View history_view = inflater.inflate(R.layout.fragment_history, container, false);
        setCustomActionBar("History");
        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager = (ViewPager) history_view.findViewById(R.id.container2);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);
        rl_clearall=(RelativeLayout) history_view.findViewById(R.id.rl_clear);

        rl_clearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*if(str_tab1=="Tab1")
                {
                    str_deletedialog="Are you sure you want to clear Missed calls?";
                }else if(str_tab2=="Tab2")
                {
                    str_deletedialog="Are you sure you want to clear Missed Messages?";
                }else
                {
                    str_deletedialog="Are you sure you want to clear Missed calls?";

                }*/

                if(tabposition==0)
                {
                    str_deletedialog="Are you sure you want to clear Missed calls?";

                }else if(tabposition==1)
                {
                    str_deletedialog="Are you sure you want to clear Missed Messages?";

                }else
                {
                    str_deletedialog="Are you sure you want to clear Missed calls?";
                }
                alertDialog(str_deletedialog);
            }
        });

        Log.i("mSectionsPagerAdapter", mViewPager.toString());
        tabLayout = (TabLayout) history_view.findViewById(R.id.tabs2);
        tabLayout.setupWithViewPager(mViewPager);
//        mViewPager.setOnPageChangeListener(this);
        TabLayout.Tab tab = tabLayout.getTabAt(mViewPager.getCurrentItem());
        tab.select();
        setupTabIcons();
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                 //Toast.makeText(getActivity(), position + "", Toast.LENGTH_LONG).show();
                if(position==0)
                {
                    //str_tab1="Tab1";
                    tabposition=position;

                }else if(position==1)
                {
                    //str_tab2="Tab2";
                    tabposition=position;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /*if(str_tab1=="Tab1")
        {
            str_tab1=" ";
            mViewPager.setCurrentItem(0);


            Toast.makeText(getActivity(),"tab0call",Toast.LENGTH_SHORT).show();


        }else if(str_tab2=="Tab2")
        {
            str_tab2=" ";
            mViewPager.setCurrentItem(1);

            Toast.makeText(getActivity(),"tab12call",Toast.LENGTH_SHORT).show();

        }else
        {
            mViewPager.setCurrentItem(0);
        }
   */
        if(tabposition==0)
        {
            mViewPager.setCurrentItem(0);
            //Toast.makeText(getActivity(),"tab0call",Toast.LENGTH_SHORT).show();

        }else if(tabposition==1)
        {

            mViewPager.setCurrentItem(1);
            //Toast.makeText(getActivity(),"tab12call",Toast.LENGTH_SHORT).show();

        }else
        {
            mViewPager.setCurrentItem(0);
        }
        return history_view;
    }
public void alertDialog(String str_deletedialog)
{
    final Dialog dialog = new Dialog(getActivity());
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setCancelable(false);
    dialog.setContentView(R.layout.alertdialog_xml);
    //dialog.getWindow().getAttributes().windowAnimations = dialogAnimation_2;
    RelativeLayout rl_cancel=(RelativeLayout)dialog.findViewById(R.id.rl_cancel);
    RelativeLayout rl_ok=(RelativeLayout)dialog.findViewById(R.id.rl_ok);
    TextView txtv_alertmsg=(TextView)dialog.findViewById(R.id.txtv_alertmessage);
    txtv_alertmsg.setText(str_deletedialog);
    rl_cancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();

        }
    });
    rl_ok.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*if(str_tab1=="Tab1")
            {
                str_delete="deletemissedcalls";
            }else if(str_tab2=="Tab2")
            {
                str_deletetab1="deletemissedmessages";
            }
*/

            if(tabposition==0)
            {
                str_delete="deletemissedcalls";

            }else if(tabposition==1)
            {
                str_deletetab1="deletemissedmessages";
            }

                           /* //context.finish();
                            Realm realmC = Realm.getInstance(getActivity());
                            RealmResults<CallsModel> contactNumber = realmC.where(CallsModel.class).findAll();
                            contactNumber.deleteAll;
                            realmC.close();*/


            Intent in =new Intent(getActivity(), HistoryActivity.class);
            getActivity().startActivity(in);
            getActivity().finish();




            dialog.dismiss();

        }
    });

    dialog.show();
}

    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabOne.setText("Missed Calls");
//        tabOne.setGravity(View.TEXT_ALIGNMENT_CENTER);

//        tabOne.setPadding(5,5,5,5);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Missed Messages");
//        tabTwo.setGravity(View.TEXT_ALIGNMENT_CENTER);
//        tabTwo.setPadding(5,5,5,5);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

    }


    Boolean first = true;
    public void refresh() {
        //yout code in refresh.
        Log.i("Refresh", "YES");
        mSectionsPagerAdapter.notifyDataSetChanged();
        //MissedCallFragment tab1 = new MissedCallFragment();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (first && positionOffset == 0 && positionOffsetPixels == 0) {
            onPageSelected(0);
            first = false;
        }
    }

    @Override
    public void onPageSelected(int position) {

        int i = tabLayout.getSelectedTabPosition();
        if (i == 0) {

        }
        else{
            //Toast.makeText(getActivity(), tabLayout.getSelectedTabPosition() + "", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);
            switch (position) {
                case 0:
                    MissedCallFragment tab1 = new MissedCallFragment();

                    return tab1;
                case 1:
                    MissedMessageFragment tab2 = new MissedMessageFragment();
                    //str_tab2="Tab2";
                    return tab2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:

                    return "SECTION 1";
                case 1:

                    return "SECTION 2";
//                case 2:
//                    return "SECTION 3";
            }
            return null;
        }
    }


}
