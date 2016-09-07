package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by Benas on 9/7/2016.
 */
public class ViewPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {


        return null;
    }

    @Override
    public int getCount() {

        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return "S";
    }
}
