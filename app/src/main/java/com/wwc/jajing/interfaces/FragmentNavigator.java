package com.wwc.jajing.interfaces;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.io.Serializable;

public interface FragmentNavigator extends Serializable {

    void onClickFragmentNavigation(Fragment fragment);

    void onClickFragmentNavigation(Fragment fragment, boolean isStackNeeded);

    Fragment getActiveFragment();

    void toggleNavigationDrawer();

    boolean isNavigationDrawerShowing();

    void updateNavigationMenuList(Bundle arguments);

}
