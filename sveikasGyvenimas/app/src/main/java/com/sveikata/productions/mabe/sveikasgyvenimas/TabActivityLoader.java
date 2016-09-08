package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

public class TabActivityLoader extends AppCompatActivity {

    public static int[] tabIcons = {R.drawable.schedule, R.drawable.play,R.drawable.facts,R.drawable.questions};

    private ViewPager viewPager;
    public static TabLayout tabLayout;
    private Toolbar myToolbar;
    private Window window;
    private boolean normal_or_reverse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_activity_loader);

        window = getWindow();

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        myToolbar.setBackgroundColor(Color.parseColor("#2ecc71"));
        setSupportActionBar(myToolbar);

        tabLayout = (TabLayout) findViewById(R.id.my_tab_layout);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), getApplicationContext()));
        viewPager.getCurrentItem();

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.setBackgroundColor(Color.parseColor("#2ecc71"));
        CheckingUtils.changeNotifBarColor("#27ae60", window);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                viewPager.setCurrentItem(tab.getPosition());
                tabLayout.getTabAt(tab.getPosition()).setIcon(tabIcons[tab.getPosition()]);


                switch (tab.getPosition()){


                    case 0:
                        CheckingUtils.changeNotifBarColor("#27ae60", window);
                        myToolbar.setBackgroundColor(Color.parseColor("#2ecc71"));
                        tabLayout.setBackgroundColor(Color.parseColor("#2ecc71"));


                        break;

                    case 1:
                        CheckingUtils.changeNotifBarColor("#d35400", window);
                        myToolbar.setBackgroundColor(Color.parseColor("#e67e22"));
                        tabLayout.setBackgroundColor(Color.parseColor("#e67e22"));

                        break;

                    case 2:
                        CheckingUtils.changeNotifBarColor("#2980b9", window);
                        myToolbar.setBackgroundColor(Color.parseColor("#3498db"));
                        tabLayout.setBackgroundColor(Color.parseColor("#3498db"));

                        break;

                    case 3:

                        CheckingUtils.changeNotifBarColor("#8e44ad", window);
                        myToolbar.setBackgroundColor(Color.parseColor("#9b59b6"));
                        tabLayout.setBackgroundColor(Color.parseColor("#9b59b6"));

                        break;


                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tabLayout.getTabAt(tab.getPosition()).setIcon(tabIcons[tab.getPosition()]);
            }
        });

    }
}
