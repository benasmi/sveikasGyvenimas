package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.games.Game;

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

    private SharedPreferences sharedPreferences;

    private RelativeLayout root;
    private int new_score;
    private int highscore;

    private int top;
    private int bottom;

    private  float top_volume_value;
    private  float bottom_volume_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Verdana.ttf");
        sharedPreferences = getSharedPreferences("DataPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        new_score = 0;
        highscore = sharedPreferences.getInt("highscore", 0);

        final Animation down = AnimationUtils.loadAnimation(this, R.anim.slide_down);


        top_title = (TextView) findViewById(R.id.top_title);
        top_volume = (TextView) findViewById(R.id.top_volume);
        top_image = (ImageView) findViewById(R.id.top_image);

        bottom_title = (TextView) findViewById(R.id.bottom_title);
        bottom_volume = (TextView) findViewById(R.id.bottom_volume);
        bottom_image = (ImageView) findViewById(R.id.bottom_image);

        which_stronger = (TextView) findViewById(R.id.which_is_stronger);
        which_stronger.setTypeface(tf);
        addAllTypes();

        top = generateNewTop();
        bottom = generateNewBottom();

        top_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(top < bottom){
                    CheckingUtils.createErrorBox("Pralaimėjai", GameScreen.this, R.style.PlayDialogStyle);
                    if(new_score>highscore){
                        sharedPreferences.edit().putInt("highscore", new_score).commit();
                    }
                }else{
                    new_score++;
                    bottom = generateNewBottom();
                    top = generateNewTop();
                }
            }
        });

        bottom_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottom < top){
                    CheckingUtils.createErrorBox("Pralaimėjai", GameScreen.this, R.style.PlayDialogStyle);
                    if(new_score>highscore){
                        sharedPreferences.edit().putInt("highscore", new_score).commit();
                    }

                }else{
                    new_score++;
                    bottom = generateNewBottom();
                    top = generateNewTop();
                }
            }
        });

    }


    private void addAllTypes(){
        alcoholList.add(new Alcohol(R.drawable.accept_challenge, 0.1f, "Fruit Juice"));
        alcoholList.add(new Alcohol(R.drawable.accept_challenge, 0.2f, "Low-alcohol beer"));
        alcoholList.add(new Alcohol(R.drawable.accept_challenge, 0.3f, "Kvass"));
        alcoholList.add(new Alcohol(R.drawable.accept_challenge, 0.4f, "Kefir"));
        alcoholList.add(new Alcohol(R.drawable.accept_challenge, 0.5f, "Kombucha"));
        alcoholList.add(new Alcohol(R.drawable.accept_challenge, 0.6f, "Boza"));
        alcoholList.add(new Alcohol(R.drawable.accept_challenge, 0.7f, "Chicha"));
        alcoholList.add(new Alcohol(R.drawable.accept_challenge, 0.8f, "Cider"));
        alcoholList.add(new Alcohol(R.drawable.accept_challenge, 0.9f, "Beer"));
        alcoholList.add(new Alcohol(R.drawable.accept_challenge, 1f, "Alcopops"));
    }

    private int generateNewTop(){
        Animation right_animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);

        int top = (int) (Math.random()*10);

        String top_title_value = alcoholList.get(top).getName();
        top_volume_value  = alcoholList.get(top).getVolume();
        int top_image_value  = alcoholList.get(top).getResource();

        top_title.setText(top_title_value);
        top_volume.setText(String.valueOf(top_volume_value) + "%");
        top_image.setImageResource(top_image_value);
        top_image.startAnimation(right_animation);

        return top;
    }

    private int generateNewBottom(){
        Animation left_animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);

        int bottom = (int) (Math.random()*10);

        String bottom_title_value  = alcoholList.get(bottom).getName();
        bottom_volume_value  = alcoholList.get(bottom).getVolume();
        int bottom_image_value  = alcoholList.get(bottom).getResource();

        bottom_title.setText(bottom_title_value);
        bottom_volume.setText(String.valueOf(bottom_volume_value) + "%");
        bottom_image.setImageResource(bottom_image_value);
        bottom_image.startAnimation(left_animation);

        return bottom;
    }




}
