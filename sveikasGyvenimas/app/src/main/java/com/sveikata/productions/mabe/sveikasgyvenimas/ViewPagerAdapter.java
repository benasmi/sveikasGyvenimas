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

        switch (position){
            case 0:
                return new HealthyLifeActivity();

            case 1:
                return new PlayActivity();

            case 2:
                return new InterestingFactsActivity();

            case 3:
                return new AskQuestionsActivity();

        }

        return null;
    }

    @Override
    public int getCount() {

        return TabActivityLoader.tabIcons.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return null;
    }
}
