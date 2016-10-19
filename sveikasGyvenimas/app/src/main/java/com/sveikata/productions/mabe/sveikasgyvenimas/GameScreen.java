package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



import java.util.ArrayList;

public class GameScreen extends AppCompatActivity {


    private ArrayList<Alcohol> alcoholList = new ArrayList<Alcohol>();
    private TextView top_title;
    private TextView top_volume;
    private ImageView top_image;

    private TextView bottom_title;
    private TextView bottom_volume;
    private ImageView bottom_image;
    private TextView which_stronger;

    private TextView highscore_txt;
    private TextView score_txt;

    private SharedPreferences sharedPreferences;

    private RelativeLayout root;
    private int new_score;
    private int highscore;
    private boolean canClick = true;
    private int top;
    private int bottom;
    private Handler handler;
    private String top_volume_value;
    private String bottom_volume_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Verdana.ttf");
        Typeface tfComforta = Typeface.createFromAsset(getAssets(), "fonts/comforta.ttf");
        sharedPreferences = getSharedPreferences("DataPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        CheckingUtils.changeNotifBarColor("#2c3e50", getWindow());

        new_score = 0;
        highscore = sharedPreferences.getInt("highscore", 0);

        final Animation down = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        highscore_txt = (TextView) findViewById(R.id.highscore);
        score_txt = (TextView) findViewById(R.id.score);
        highscore_txt.setText("Geriausias rezultatas: " + String.valueOf(highscore));
        score_txt.setText("Rezultatas: " + "0");

        top_title = (TextView) findViewById(R.id.top_title);
        top_volume = (TextView) findViewById(R.id.top_volume);
        top_image = (ImageView) findViewById(R.id.top_image);

        bottom_title = (TextView) findViewById(R.id.bottom_title);
        bottom_volume = (TextView) findViewById(R.id.bottom_volume);
        bottom_image = (ImageView) findViewById(R.id.bottom_image);

        which_stronger = (TextView) findViewById(R.id.which_is_stronger);
        which_stronger.setTypeface(tfComforta);
        score_txt.setTypeface(tf);
        highscore_txt.setTypeface(tf);

        top_title.setTypeface(tf);
        top_volume.setTypeface(tf);

        bottom_title.setTypeface(tf);
        bottom_volume.setTypeface(tf);

        addAllTypes();

        top = generateNewTop();
        bottom = generateNewBottom();

        final ResizeAnimation expand_top = new ResizeAnimation(top_volume, (int) CheckingUtils.convertPixelsToDp(30, GameScreen.this), 0);
        expand_top.setDuration(1);

        final ResizeAnimation expand_bottom = new ResizeAnimation(bottom_volume, (int) CheckingUtils.convertPixelsToDp(30, GameScreen.this), 0);
        expand_bottom.setDuration(1);


        top_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(top < bottom){

                    if(new_score>highscore){
                        sharedPreferences.edit().putInt("highscore", new_score).commit();
                    }
                    CheckingUtils.vibrate(GameScreen.this, 100);
                    startActivity(new Intent(GameScreen.this, YouLostHigherLowerAcitivity.class).putExtra("score", String.valueOf(new_score)).putExtra("highscore", String.valueOf(highscore)));
                }else{
                    new_score++;
                    score_txt.setText("Rezultatas: " + String.valueOf(new_score));

                    if(canClick){
                        bottom_volume.startAnimation(expand_bottom);
                        top_volume.startAnimation(expand_top);
                        canClick=false;
                    }


                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            canClick =true;
                            bottom = generateNewBottom();
                            top = generateNewTop();
                        }
                    },1500);
                }
            }
        });

        bottom_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottom < top){

                    if(new_score>highscore){
                        sharedPreferences.edit().putInt("highscore", new_score).commit();
                    }
                    startActivity(new Intent(GameScreen.this, YouLostHigherLowerAcitivity.class).putExtra("score", String.valueOf(new_score)).putExtra("highscore", String.valueOf(highscore)));
                    CheckingUtils.vibrate(GameScreen.this, 100);

                }else{
                    new_score++;
                    score_txt.setText("Rezultatas: " + String.valueOf(new_score));

                    if(canClick){
                    bottom_volume.startAnimation(expand_bottom);
                    top_volume.startAnimation(expand_top);
                        canClick=false;
                    }

                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            canClick =true;
                            bottom = generateNewBottom();
                            top = generateNewTop();
                        }
                    },1500);

                }
            }
        });

    }


    private void addAllTypes(){
        alcoholList.add(new Alcohol(R.drawable.light_beer1, "0.05%–1.2%", "Lengvas alus(nealkoholinis)"));
        alcoholList.add(new Alcohol(R.drawable.kvass2, "0.05%–1.5%", "Gira"));
        alcoholList.add(new Alcohol(R.drawable.kefir3, "0.2%–2.0%", "Kefyras"));
        alcoholList.add(new Alcohol(R.drawable.kombucha3, "0.5%–1.5%", "Kombuča"));
        alcoholList.add(new Alcohol(R.drawable.boza4, "1%", "Boza"));
        alcoholList.add(new Alcohol(R.drawable.chocha5, "1%–11% (dažniausiai 1%–6%)", "Čiča"));
        alcoholList.add(new Alcohol(R.drawable.cider6, "2%–8.5%", "Sidras"));
        alcoholList.add(new Alcohol(R.drawable.beer7, "2%–12% (dažniausiai 4%–6%)", "Alus"));
        alcoholList.add(new Alcohol(R.drawable.alcopops8, "4%", "Kokteiliai"));
        alcoholList.add(new Alcohol(R.drawable.malt_liquor9, "5%+", "Nuovirų alkoholinis gėrimas"));
        alcoholList.add(new Alcohol(R.drawable.makgeoli10, "6.5%–7%", "Korėjiečių ryžių vynas"));
        alcoholList.add(new Alcohol(R.drawable.barley_wine11, "8%–15%", "Miežių vynas"));
        alcoholList.add(new Alcohol(R.drawable.mead12, "8%–16%", "Midus"));
        alcoholList.add(new Alcohol(R.drawable.wine13, "9%–16%", "Vynas"));
        alcoholList.add(new Alcohol(R.drawable.sugar_wine13, "15%–17%", "Saldus vynas"));
        alcoholList.add(new Alcohol(R.drawable.desert_wine14, "14%–25%", "Desertinis vynas"));
        alcoholList.add(new Alcohol(R.drawable.rice_wine15, "dažniausiai 15%", "Ryžių vynas"));
        alcoholList.add(new Alcohol(R.drawable.liquer16, "15%–55%", "Likeris"));
        alcoholList.add(new Alcohol(R.drawable.fortified_wine_17, "18%–22%", "Spirituotas vynas"));
        alcoholList.add(new Alcohol(R.drawable.shochu19, "25%–45% (dažniausiai 25%)", "Japoniškas gėrimas 'Shochu'"));
        alcoholList.add(new Alcohol(R.drawable.bitters20, "28%–45%", "Trauktinė"));
        alcoholList.add(new Alcohol(R.drawable.apple_jack21, "30%-40%", "Obuolių trauktinė"));
        alcoholList.add(new Alcohol(R.drawable.tequila22, "32%–60% (dažniausiai 40%)", "Tekila"));
        alcoholList.add(new Alcohol(R.drawable.vodka23, "35%–50%(Europoje min. 37,5%)", "Vodka"));
        alcoholList.add(new Alcohol(R.drawable.brandy24, "35%–60% (dažniausiai 40%)", "Brendis"));
        alcoholList.add(new Alcohol(R.drawable.grappa25, "37.5%–60%", "Itališkas brendis 'Grappa'"));
        alcoholList.add(new Alcohol(R.drawable.rum26, "37.5%–80%", "Romas"));
        alcoholList.add(new Alcohol(R.drawable.ouzo27, "37.5%+", "Graikiškas - 'Ouzo'"));
        alcoholList.add(new Alcohol(R.drawable.cachaca28, "38%–54%", "Pitu cachaca"));
        alcoholList.add(new Alcohol(R.drawable.sotol29, "38%–60%", "Spiritas su žolelėmis"));
        alcoholList.add(new Alcohol(R.drawable.stroh30, "38%–80%", "Subrandintas brendis"));
        alcoholList.add(new Alcohol(R.drawable.gin32, "40%–50%", "Džinas"));
        alcoholList.add(new Alcohol(R.drawable.whisky33, "40%–68% (dažniausiai 40%, 43% or 46%)", "Viskis"));
        alcoholList.add(new Alcohol(R.drawable.baiju34, "40%–60%", "Grūdų vynas"));
        alcoholList.add(new Alcohol(R.drawable.chacha35, "40%–70%", "Gruziškas brendis 'Cacha'"));
        alcoholList.add(new Alcohol(R.drawable.centerbe36, "70%", "Itališkas likeris 'Centerbe'"));
        alcoholList.add(new Alcohol(R.drawable.palinka37, "42%–86%", "Vengriškas vaisių brendis"));
        alcoholList.add(new Alcohol(R.drawable.rakia38, "42%–86%", "Bulgariškas vaisių brendis"));
        alcoholList.add(new Alcohol(R.drawable.absinthe39, "45%–89.9%", "Žaliasis anyžinis spiritas"));
        alcoholList.add(new Alcohol(R.drawable.tuica40, "45%–60% (dažniausiai 52%)", "Tradicinis rumunų spiritas"));
        alcoholList.add(new Alcohol(R.drawable.arak41, "60%–65%", "Rytų Azijiečių anyžinis spiritas"));
        alcoholList.add(new Alcohol(R.drawable.poitin42, "60%–95%", "Airių tradicinis gėrimas " + "'Poitin'"));
        alcoholList.add(new Alcohol(R.drawable.cocoroco43, "93%–96%", "Cukranendrių spiritas"));
        alcoholList.add(new Alcohol(R.drawable.rectified_spirit44, "95%–96%", "Grynas spiritas"));


    }

    private int generateNewTop(){
        Animation right_animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);

        final ResizeAnimation shrink_top = new ResizeAnimation(top_volume, 0, (int) CheckingUtils.convertPixelsToDp(0, GameScreen.this));
        shrink_top.setDuration(0);
        top_volume.startAnimation(shrink_top);

        int top = (int) (Math.random()*alcoholList.size());
        String top_title_value = alcoholList.get(top).getName();
        top_volume_value  = alcoholList.get(top).getVolume();
        int top_image_value  = alcoholList.get(top).getResource();

        top_title.setText(top_title_value);
        top_volume.setText(top_volume_value);
        top_image.setImageResource(top_image_value);
        top_image.invalidate();
        top_image.startAnimation(right_animation);


        return top;
    }

    private int generateNewBottom(){
        Animation left_animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        final ResizeAnimation shrink_bottom = new ResizeAnimation(top_volume, 0, (int) CheckingUtils.convertPixelsToDp(0, GameScreen.this));
        shrink_bottom.setDuration(0);
        bottom_volume.startAnimation(shrink_bottom);

        int bottom = (int) (Math.random()*alcoholList.size());

        String bottom_title_value  = alcoholList.get(bottom).getName();
        bottom_volume_value  = alcoholList.get(bottom).getVolume();
        int bottom_image_value  = alcoholList.get(bottom).getResource();

        bottom_title.setText(bottom_title_value);
        bottom_volume.getLayoutParams().height=0;
        bottom_volume.setText(bottom_volume_value);
        bottom_image.setImageResource(bottom_image_value);
        top_image.invalidate();
        bottom_image.startAnimation(left_animation);

        return bottom;
    }




}
