package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.shape.CircleShape;
import uk.co.deanwild.materialshowcaseview.shape.RectangleShape;

public class TabActivityLoader extends AppCompatActivity {

    public static int[] tabIcons = {R.drawable.schedule_active, R.drawable.play_unactive,R.drawable.facts_unactive,R.drawable.questions_unactive};

    private ViewPager viewPager;
    public static TabLayout tabLayout;
    private Toolbar myToolbar;
    private Window window;
    private boolean normal_or_reverse;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private boolean showPlay = false;
    private boolean showFacts = false;
    private boolean showQuestions = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_activity_loader);

        AskQuestionsActivity.addFAQData = true;
        InterestingFactsActivity.addFactsFirstTime = true;
        HealthyLifeActivity.addData = true;
        PlayActivity.shouldAddInfo = true;





        window = getWindow();
        sharedPreferences = getSharedPreferences("DataPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();


        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        myToolbar.setBackgroundColor(Color.parseColor("#009688"));
        myToolbar.setTitle("Tvarkaraštis");
        setSupportActionBar(myToolbar);

        tabLayout = (TabLayout) findViewById(R.id.my_tab_layout);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), getApplicationContext()));
        viewPager.getCurrentItem();

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.setBackgroundColor(Color.parseColor("#009688"));
        CheckingUtils.changeNotifBarColor("#00796B", window);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                viewPager.setCurrentItem(tab.getPosition());
                tabLayout.getTabAt(tab.getPosition()).setIcon(tabIcons[tab.getPosition()]);

                int position = tab.getPosition();

                setTabScheduleIcon(tabLayout, position);

                switch (position){

                    case 0:
                        CheckingUtils.changeNotifBarColor("#00796B", window);
                        myToolbar.setBackgroundColor(Color.parseColor("#009688"));
                        tabLayout.setBackgroundColor(Color.parseColor("#009688"));
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
                setTabScheduleIcon(tabLayout, tab.getPosition());
            }
        });

        try{
            viewPager.setCurrentItem(getIntent().getExtras().getInt("Tab"));
        }catch(Exception e){
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_bar,menu);

        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                String username = sharedPreferences.getString("username", "");
                String password = sharedPreferences.getString("password", "");

                if(!CheckingUtils.isNetworkConnected(TabActivityLoader.this)){
                    CheckingUtils.createErrorBox("Jei norite atsijungti, įjunkite internetą", TabActivityLoader.this, R.style.CasualStyle);
                    return false;
                }else{SharedPreferences prefs = getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);
                    prefs.edit().putBoolean("isFirstTimeFaq", true).commit();
                    prefs.edit().putString("faq_data", "").commit();
                    new ServerManager(TabActivityLoader.this, "LOGOUT").execute("LOGOUT", username, password);
                }

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    private void setTabScheduleIcon(TabLayout tabLayout, int position){

        showPlay = sharedPreferences.getBoolean("showPlay", false);
        showFacts = sharedPreferences.getBoolean("showFacts", false);
        showQuestions = sharedPreferences.getBoolean("showQuestions", false);

        if(position == 0){
            tabLayout.getTabAt(0).setIcon(R.drawable.schedule_active);
            tabLayout.getTabAt(1).setIcon(R.drawable.play_unactive);
            tabLayout.getTabAt(2).setIcon(R.drawable.facts_unactive);
            tabLayout.getTabAt(3).setIcon(R.drawable.questions_unactive);
            myToolbar.setTitle("Tvarkaraštis");




        }else if(position==1){
            tabLayout.getTabAt(0).setIcon(R.drawable.schedule_unactive);
            tabLayout.getTabAt(1).setIcon(R.drawable.play_active);
            tabLayout.getTabAt(2).setIcon(R.drawable.facts_unactive);
            tabLayout.getTabAt(3).setIcon(R.drawable.questions_unactive);
            myToolbar.setTitle("Skaičiuok ir žaisk");

            if(!showPlay){
                editor.putBoolean("showPlay",true).commit();
                new MaterialShowcaseView.Builder(this)
                        .setTarget(this.myToolbar)
                        .setDismissText("SUPRATAU!")
                        .setContentText("O atėjęs į šią skiltį, Tu galėsi išbandyti įvairias skaičiuokles, pažaisti bei mesti iššūkius draugams. Argi ne šaunu?!")
                        .setFadeDuration(1000) // optional but starting animations immediately in onCreate can make them choppy
                        .setDismissTextColor(Color.parseColor("#FFEAC73C"))
                        .show();
            }

        }else if(position==2){
            tabLayout.getTabAt(0).setIcon(R.drawable.schedule_unactive);
            tabLayout.getTabAt(1).setIcon(R.drawable.play_unactive);
            tabLayout.getTabAt(2).setIcon(R.drawable.facts_active);
            tabLayout.getTabAt(3).setIcon(R.drawable.questions_unactive);
            myToolbar.setTitle("Ar gali patikėti?");

            if(!showFacts){
                editor.putBoolean("showFacts",true).commit();
                new MaterialShowcaseView.Builder(this)
                        .setTarget(this.myToolbar)
                        .setDismissText("GERAI")
                        .setDismissTextColor(Color.parseColor("#FFEAC73C"))
                        .setContentText("Tačiau tai dar ne viskas! Nepaisant pramogų, galėsi pasisemti įvairių faktų ir dalintis jais su draugais!")
                        .setFadeDuration(500) // optional but starting animations immediately in onCreate can make them choppy
                        .show();
            }
        }else if(position==3){

            tabLayout.getTabAt(0).setIcon(R.drawable.schedule_unactive);
            tabLayout.getTabAt(1).setIcon(R.drawable.play_unactive);
            tabLayout.getTabAt(2).setIcon(R.drawable.facts_unactive);
            tabLayout.getTabAt(3).setIcon(R.drawable.questions_active);
            myToolbar.setTitle("Klausk..Klausk..!");


            if(!showQuestions){
                editor.putBoolean("showQuestions",true).commit();
                new MaterialShowcaseView.Builder(this)
                        .setTarget(this.myToolbar)
                        .setDismissTextColor(Color.parseColor("#FFEAC73C"))
                        .setDismissText("ŠAUNU!")
                        .setContentText("Nori sužinoti daugiau apie projektą? Čia rasi dažniausiai užduodamus klausimus bei galėsi paklausti specialisto ko tik širdis geidžia!")
                        .setFadeDuration(500) // optional but starting animations immediately in onCreate can make them choppy
                        .show();
            }

        }


    }

}
