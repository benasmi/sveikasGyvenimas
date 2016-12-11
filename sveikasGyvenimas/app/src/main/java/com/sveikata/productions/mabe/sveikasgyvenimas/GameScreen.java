package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GameScreen extends AppCompatActivity {


    private ArrayList<Alcohol> alcoholList = new ArrayList<Alcohol>();
    private TextView top_title;
    private TextView top_volume;
    private ImageView top_image;

    private TextView bottom_title;
    private TextView bottom_volume;
    private ImageView bottom_image;
    private TextView which_stronger;

    private RelativeLayout layout;

    private TextView highscore_txt;
    private TextView score_txt;

    private TextView top_fat;
    private TextView bottom_fat;

    private TextView top_carbohydrates;
    private TextView bottom_carbohydrates;

    private TextView top_protein;
    private TextView bottom_protein;

    private boolean isFirstTime = true;

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

        top_protein = (TextView) findViewById(R.id.proteins_top);
        bottom_protein = (TextView) findViewById(R.id.proteins_bottom);

        highscore_txt.setText("Geriausias rezultatas: " + String.valueOf(highscore));
        score_txt.setText("Rezultatas: " + "0");

        top_fat = (TextView) findViewById(R.id.fat_top);
        bottom_fat = (TextView) findViewById(R.id.fat_bottom);

        top_carbohydrates = (TextView) findViewById(R.id.carbohydrates_top);
        bottom_carbohydrates = (TextView) findViewById(R.id.carbohydrates_bottom);

        top_title = (TextView) findViewById(R.id.top_title);
        top_volume = (TextView) findViewById(R.id.top_volume);
        top_image = (ImageView) findViewById(R.id.top_image);
        layout = (RelativeLayout) findViewById(R.id.activity_game_screen);
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
        isFirstTime = false;



        final ResizeAnimation expand_top = new ResizeAnimation(top_volume, (int) CheckingUtils.convertPixelsToDp(30, GameScreen.this), 0);
        expand_top.setDuration(250);

        final ResizeAnimation expand_bottom = new ResizeAnimation(bottom_volume, (int) CheckingUtils.convertPixelsToDp(30, GameScreen.this), 0);
        expand_bottom.setDuration(250);



        final Animation.AnimationListener listener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                canClick =true;
                top = generateNewTop();
                bottom = generateNewBottom();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        top_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(top > bottom){

                    if(new_score>highscore){
                        sharedPreferences.edit().putInt("highscore", new_score).commit();
                    }
                    CheckingUtils.vibrate(GameScreen.this, 100);
                    startActivity(new Intent(GameScreen.this, YouLostHigherLowerAcitivity.class).putExtra("score", String.valueOf(new_score)).putExtra("highscore", String.valueOf(highscore)));
                }else{
                    new_score++;
                    score_txt.setText("Rezultatas: " + String.valueOf(new_score));

                    if(canClick){
                        showInfo(true);
                        bottom_volume.startAnimation(expand_bottom);
                        top_volume.startAnimation(expand_top);
                        canClick=false;

                    }


                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            final ResizeAnimation shrink_bottom = new ResizeAnimation(bottom_volume, 0, bottom_volume.getHeight());
                            shrink_bottom.setDuration(250);
                            bottom_volume.startAnimation(shrink_bottom);

                            final ResizeAnimation shrink_top = new ResizeAnimation(top_volume, 0, top_volume.getHeight());
                            shrink_top.setDuration(250);
                            top_volume.startAnimation(shrink_top);

                            showInfo(false);
                            shrink_top.setAnimationListener(listener);
                            shrink_bottom.setAnimationListener(listener);

                        }
                    },3000);
                }
            }
        });

        bottom_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottom > top){

                    if(new_score>highscore){
                        sharedPreferences.edit().putInt("highscore", new_score).commit();
                    }
                    startActivity(new Intent(GameScreen.this, YouLostHigherLowerAcitivity.class).putExtra("score", String.valueOf(new_score)).putExtra("highscore", String.valueOf(highscore)));
                    CheckingUtils.vibrate(GameScreen.this, 100);

                }else{
                    new_score++;
                    score_txt.setText("Rezultatas: " + String.valueOf(new_score));

                    if(canClick){
                        showInfo(true);
                    bottom_volume.startAnimation(expand_bottom);
                    top_volume.startAnimation(expand_top);
                        canClick=false;
                    }

                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            final ResizeAnimation shrink_bottom = new ResizeAnimation(bottom_volume, 0, bottom_volume.getHeight());
                            shrink_bottom.setDuration(250);
                            bottom_volume.startAnimation(shrink_bottom);

                            final ResizeAnimation shrink_top = new ResizeAnimation(top_volume, 0, top_volume.getHeight());
                            shrink_top.setDuration(250);
                            top_volume.startAnimation(shrink_top);

                            showInfo(false);

                            shrink_top.setAnimationListener(listener);
                            shrink_bottom.setAnimationListener(listener);

                        }
                    },1500);

                }
            }
        });


    }

    private void refreshColor(){
        int color = CheckingUtils.getDominantColor(BitmapFactory.decodeResource(getResources(), alcoholList.get(top).getResource()));

        layout.setBackgroundColor(color);
        CheckingUtils.changeNotifBarColor(CheckingUtils.darker(color, 0.9f), getWindow());
    }


    private void addAllTypes(){
        alcoholList.add(new Alcohol(R.drawable.agrastas, "44", "Agrastai","0.9g","10g","0.6g"));
        alcoholList.add(new Alcohol(R.drawable.agurkas, "15", "Agurkai","0.7g","3.6g","0.1g"));
        alcoholList.add(new Alcohol(R.drawable.aviete, "52", "Avietės","1.2","12g","0.7g"));
        alcoholList.add(new Alcohol(R.drawable.braske, "32", "Braškės","0.7g","8g","0.3g"));
        alcoholList.add(new Alcohol(R.drawable.gervuoge, "43", "Gervuogės","1,4g","10g","0.5g"));
        alcoholList.add(new Alcohol(R.drawable.kriause, "57", "Kriaušės","0.4g","15g","0.1g"));
        alcoholList.add(new Alcohol(R.drawable.melyne, "44", "Mėlynės","0.7g","11,5g","0.6g"));
        alcoholList.add(new Alcohol(R.drawable.melionas, "33", "Melionai","0.8g","8g","0.2g"));
        alcoholList.add(new Alcohol(R.drawable.obuolys, "53", "Obuoliai","0.4g","13g","0.4g"));
        alcoholList.add(new Alcohol(R.drawable.serbentas, "55", "Serbentai","1.4g","14g","0.2g"));
        alcoholList.add(new Alcohol(R.drawable.slyva, "45", "Slyva","0.7g","11g","0.3g"));
        alcoholList.add(new Alcohol(R.drawable.spanguole, "46", "Spanguolės","0.4g","12g","0.1g"));
        alcoholList.add(new Alcohol(R.drawable.svarainis, "76", "Svarainis","0.4g","19.9g","0.3g"));
        alcoholList.add(new Alcohol(R.drawable.tresne, "60", "Trešnės","0.9g","14.4g","0.4g"));
        alcoholList.add(new Alcohol(R.drawable.tekse, "51", "Tekšės","2.4g","9g","0.8g"));
        alcoholList.add(new Alcohol(R.drawable.vynuoge, "66", "Vynuogės","0.6g","17g","0.4g"));
        alcoholList.add(new Alcohol(R.drawable.vysnia, "21", "Vyšnios","0.4g","12g","0.2g"));
        alcoholList.add(new Alcohol(R.drawable.zemuoge, "32", "Žemuogės","0.74g","8g","0.3g"));
        alcoholList.add(new Alcohol(R.drawable.abrikosas, "49", "Abrikosai","0.8g","12g","0.2g"));
        alcoholList.add(new Alcohol(R.drawable.ananasas, "50", "Ananasai","0.5g","13g","0.1g"));
        alcoholList.add(new Alcohol(R.drawable.apelsinas, "43", "Apelsinai","0.8g","11g","0.2g"));
        alcoholList.add(new Alcohol(R.drawable.arbuzas, "27", "Arbūzai","0.7g","6g","0.2g"));
        alcoholList.add(new Alcohol(R.drawable.avokadas, "224", "Avokadas","1.9g","6g","23.5g"));
        alcoholList.add(new Alcohol(R.drawable.baklazanas, "24", "Baklažanai","1g","6g","0.2g"));
        alcoholList.add(new Alcohol(R.drawable.bananas, "97", "Bananai","1.2g","23.1g","0.3g"));
        alcoholList.add(new Alcohol(R.drawable.cidonija, "56", "Cidonijos","0.4g","15g","0.1g"));
        alcoholList.add(new Alcohol(R.drawable.citrina, "29", "Citrinos","1.1g","9.3g","0.3g"));
        alcoholList.add(new Alcohol(R.drawable.datule, "291", "Datulės","2g","74g","0.4g"));
        alcoholList.add(new Alcohol(R.drawable.figa, "74", "Figos","0.75g","19.2g","0.3g"));
        alcoholList.add(new Alcohol(R.drawable.granatas, "68", "Granatai","0.95g","17.2g","0.3g"));
        alcoholList.add(new Alcohol(R.drawable.greipfruitas, "38", "Greipfrutai","0.6g","10g","0.2g"));
        alcoholList.add(new Alcohol(R.drawable.kivis, "60", "Kiviai","1.1g","15g","0.5g"));
        alcoholList.add(new Alcohol(R.drawable.kokosas, "354", "Kokosai","3.3g","15g","33g"));
        alcoholList.add(new Alcohol(R.drawable.mandarinas, "38", "Mandarinai","0.7g","9.4g","0.3g"));
        alcoholList.add(new Alcohol(R.drawable.mangas, "70", "Mangai","0.6g","16.5g","0.5g"));
        alcoholList.add(new Alcohol(R.drawable.papaja, "39", "Papajai","0.6g","9.8g","0.14g"));
        alcoholList.add(new Alcohol(R.drawable.persikas, "39", "Persikai","0.9g","10g","0.3g"));
        alcoholList.add(new Alcohol(R.drawable.persimonas, "70", "Persimonai","0.58g","18.6g","0.2g"));
        alcoholList.add(new Alcohol(R.drawable.pomelo, "38", "Pomelai","0.76g","9.62g","0.04g"));
        alcoholList.add(new Alcohol(R.drawable.silauoge, "57", "Šilauogės","1g","14g","0g"));
        alcoholList.add(new Alcohol(R.drawable.pasiflora, "97", "Pasifloros","2.2g","23.4g","0.7g"));
        alcoholList.add(new Alcohol(R.drawable.liciai, "66", "Ličiai","0.83g","16.53g","0.44g"));
        alcoholList.add(new Alcohol(R.drawable.duonvaisis, "103", "Duonvaisiai","1.07g","27.1g","0.2g"));
        alcoholList.add(new Alcohol(R.drawable.karambola, "33", "Karambolos","0.54g","7.8g","0.4g"));
        alcoholList.add(new Alcohol(R.drawable.lokva, "47", "Lokvos","0.43g","12.1g","0.2g"));
        alcoholList.add(new Alcohol(R.drawable.silkmedzio_uogos, "43", "Šilkmedžio uogos","1.44g","9.8g","0.4g"));



        //Sorting arraylist by cal
        Collections.sort(alcoholList, new Comparator<Alcohol>() {
            @Override
            public int compare(Alcohol a1, Alcohol a2) {
                int a1Cal = Integer.parseInt(a1.getCalories());
                int a2Cal = Integer.parseInt(a2.getCalories());
                if (a1Cal > a2Cal)
                    return 1;
                if (a1Cal < a2Cal)
                    return -1;
                return 0;
            }
        });



    }

    private void showInfo(boolean bool){
        if(bool){
            final ResizeAnimation top_anim_expand = new ResizeAnimation(top_carbohydrates, (int) CheckingUtils.convertPixelsToDp(50, this), 0);
            final ResizeAnimation bottom_anim_expand = new ResizeAnimation(bottom_carbohydrates, (int) CheckingUtils.convertPixelsToDp(50, this), 0);
            final ResizeAnimation top_anim_expand_fat = new ResizeAnimation(top_fat, (int) CheckingUtils.convertPixelsToDp(50, this), 0);
            final ResizeAnimation bottom_anim_expand_fat = new ResizeAnimation(bottom_fat, (int) CheckingUtils.convertPixelsToDp(50, this), 0);
            final ResizeAnimation top_anim_expand_protein = new ResizeAnimation(top_protein, (int) CheckingUtils.convertPixelsToDp(50, this), 0);
            final ResizeAnimation bottom_anim_expand_protein = new ResizeAnimation(bottom_protein, (int) CheckingUtils.convertPixelsToDp(50, this), 0);
            top_anim_expand.setDuration(200);
            top_anim_expand_fat.setDuration(200);
            top_anim_expand_protein.setDuration(200);
            bottom_anim_expand.setDuration(200);
            bottom_anim_expand_fat.setDuration(200);
            bottom_anim_expand_protein.setDuration(200);

            top_carbohydrates.startAnimation(top_anim_expand);
            bottom_carbohydrates.startAnimation(bottom_anim_expand);
            top_fat.startAnimation(top_anim_expand_fat);
            bottom_fat.startAnimation(bottom_anim_expand_fat);
            top_protein.startAnimation(top_anim_expand_protein);
            bottom_protein.startAnimation(bottom_anim_expand_protein);
        }else{
            final ResizeAnimation top_anim_shrink = new ResizeAnimation(top_carbohydrates, 0, top_carbohydrates.getHeight());
            final ResizeAnimation bottom_anim_shrink = new ResizeAnimation(bottom_carbohydrates, 0, bottom_carbohydrates.getHeight());
            final ResizeAnimation top_anim_shrink_fat = new ResizeAnimation(top_fat, 0, top_fat.getHeight());
            final ResizeAnimation bottom_anim_shrink_fat = new ResizeAnimation(bottom_fat, 0, bottom_fat.getHeight());
            final ResizeAnimation top_anim_shrink_protein = new ResizeAnimation(top_protein, 0, top_protein.getHeight());
            final ResizeAnimation bottom_anim_shrink_protein = new ResizeAnimation(bottom_protein, 0, bottom_protein.getHeight());
            top_anim_shrink.setDuration(200);
            bottom_anim_shrink.setDuration(200);
            top_anim_shrink_fat.setDuration(200);
            bottom_anim_shrink_fat.setDuration(200);
            top_anim_shrink_protein.setDuration(200);
            bottom_anim_shrink_protein.setDuration(200);

            bottom_carbohydrates.startAnimation(bottom_anim_shrink);
            top_carbohydrates.startAnimation(top_anim_shrink);
            bottom_fat.startAnimation(bottom_anim_shrink_fat);
            top_fat.startAnimation(top_anim_shrink_fat);
            top_protein.startAnimation(top_anim_shrink_protein);
            bottom_protein.startAnimation(bottom_anim_shrink_protein);
        }


    }

    private int generateNewTop(){
        final Animation fade_anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        int top = (int) (Math.random()*alcoholList.size());

        int image_resource = alcoholList.get(top).getResource();

        top_image.setImageResource(image_resource);

        if(!isFirstTime){
            top_image.startAnimation(fade_anim);
        }


        String top_title_value = alcoholList.get(top).getName();
        top_volume_value  = alcoholList.get(top).getCalories();
        String top_carbohydrates_value  = alcoholList.get(top).getCarbohydrates();
        String top_fat_value  = alcoholList.get(top).getLipids();
        String top_protein_value = alcoholList.get(top).getProtein();

        top_carbohydrates.setText(top_carbohydrates_value + "\nAngliavandenių");
        top_fat.setText(top_fat_value + "\nRiebalų");
        top_protein.setText(top_protein_value + "\nBaltymų");

        top_title.setText(top_title_value);
        top_volume.getLayoutParams().height=0;
        top_volume.setText(top_volume_value + " Kcal");

       refreshColor();


        return top;
    }

    private int generateNewBottom(){
        final Animation fade_anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        int bottom = (int) (Math.random()*alcoholList.size());

        while(bottom==top){
            bottom = (int) (Math.random()*alcoholList.size());
        }

        int image_resource = alcoholList.get(bottom).getResource();

        bottom_image.setImageResource(image_resource);

        if(!isFirstTime){
            bottom_image.startAnimation(fade_anim);
        }

        String bottom_title_value  = alcoholList.get(bottom).getName();
        bottom_volume_value  = alcoholList.get(bottom).getCalories();
        String bottom_carbohydrates_value  = alcoholList.get(bottom).getCarbohydrates();
        String bottom_fat_value  = alcoholList.get(bottom).getLipids();
        String bottom_protein_value = alcoholList.get(bottom).getProtein();

        bottom_carbohydrates.setText(bottom_carbohydrates_value + "\nAngliavandenių");
        bottom_fat.setText(bottom_fat_value + "\nRiebalų");
        bottom_protein.setText(bottom_protein_value + "\nBaltymų");

        bottom_title.setText(bottom_title_value);
        bottom_volume.getLayoutParams().height=0;
        bottom_volume.setText(bottom_volume_value + " Kcal");



        return bottom;
    }
}
