package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class QuizActivity extends AppCompatActivity {

    private TextView question_textview;
    private TextView timer;
    private TextView score_textview;
    private CountDownTimer countDownTimer;
    private AppCompatButton answer1;
    private AppCompatButton answer2;
    private AppCompatButton answer3;
    private AppCompatButton answer4;

    private boolean canClick = true;
    private SharedPreferences sharedPreferences;
    public static final int questionCount = 14;

    public static final String[] question_1 = {"Kokia medžiaga turima omenyje visuomenėje kalbant apie alkoholį? ", "Etanolis", "Metanolis", "Etilenglikolis", "Alkalolis", "Etanolis"};
    public static final String[] question_2 = {"Kas turi daugiausiausiai etanolio:  butelis (500ml) 5% alkoholinio kokteilio, 200 ml 12% baltojo vyno ar mažas bokalas (325 ml) 7,5% stipraus alaus? ", "Kokteilis", "Vynas", "Alus", "Visi vienodai 24 g gryno alkoholio (etanolio) – 2,4 alkoholio vieneto", "Visi vienodai 24 g gryno alkoholio (etanolio) – 2,4 alkoholio vieneto"};
    public static final String[] question_3 = {"Kiek kalorijų turi grynas etanolis?", "7,11 kcal", "9,3 kcal", "4,0 kcal", "Neturi", "7,11 kcal"};
    public static final String[] question_4 = {"Kiek alkoholio per savaitę turi suvartoti moteris, jog tai būtų laikoma rizikingu vartojimu? ", "Daugiau nei 3 SAV (standartiniai alkoholio vienetai) (daugiau nei 600 ml 5 % alaus) vienkartinai, ar daugiau nei 7 SAV( 0,5 litro 12% stiprumo vyno)  per savaitę", "Daugiau nei 5 SAV ( 1 litras 5% stirpumo alus) vienkartinai arba 14 SAV (0,75 litro 18 % stipraus likerinio vyno) per savaitę",
            "Daugiau nei suvartoja kartu gėręs vyras", "Daugiau nei 10 SAV (2 litrai 5 % sidro) vienkartinai arba 30 SAV (0,75 litro 40 % brendžio)  per savaitę", "Daugiau nei 3 SAV (standartiniai alkoholio vienetai) (daugiau nei 600 ml 5 % alaus) vienkartinai, ar daugiau nei 7 SAV( 0,5 litro 12% stiprumo vyno)  per savaitę"};
    public static final String[] question_5 = {"Koks gėrimas kenksmingiausias kepenims?", "Visi vienodai kenksmingi", "Alus", "Prabangus viskis", "Burnos skalavimo skystis", "Visi vienodai kenksmingi"};
    public static final String[] question_6 = {"Kiel mililitrų stipraus (40%) gėrimo sudaro standartinį alkoholio vienetą?", "30", "40", "50", "60", "40"};
    public static final String[] question_7 = {"Ar maistas prieš alkoholį sumažina alkoholio absorbciją?", "Tiesa, mažina", "Ne, maistas didina alkoholio patekimą į kraują", "Ne, maistas niekaip neveikia absorbcijos",
            "Ne, maistas visiškai sustabdo alkoholio patekimą į kraują", "Tiesa, mažina"};
    public static final String[] question_8 = {"Ar maistas prieš alkoholį sumažina alkoholio kiekį patenkantį į organizmą?", "Ne, maistas tik sulėtina alkoholio patekimą į kraują, bet bendras kiekis išlieka toks pats",
            "Taip, maistas sumažina alkoholio kiekį patekusį į organizmą per pusę", "Taip, maistas nežymiai sumažina alkoholio kiekį, patekusį į organizmą", "Taip, maistas visiškai neleidžia alkoholiui patekti į kraują.", "Ne, maistas tik sulėtina alkoholio patekimą į kraują, bet bendras kiekis išlieka toks pats"};
    public static final String[] question_9 = {"Kuriam organui kenkia alkoholis?", "Kasai", "Smegenims", "Kepenims", "Visiems išvardintiems", "Visiems išvardintiems"};
    public static final String[] question_10 = {"Kiek kcal turi viena taurė (250 ml) 13% stiprumo vyno?", "80 kcal", "123 kcal ", "200 kcal", "228 kcal", "228 kcal"};
    public static final String[] question_11 = {"Kaip šalinamas alkoholis iš organizmo?", "Su šlapimu", "Su išmatomis", "Su iškvepiamu oru", "Su šlapimu ir iškvepiamu oru", "Su šlapimu ir iškvepiamu oru"};
    public static final String[] question_12 = {"Kur alkoholis nukenksminamas?", "Kraujyje", "Skrandyje", "Kepenyse", "Smegenyse", "Kepenyse"};

    public static int question;
    public static int score;
    private int highscore;

    int correct_answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        sharedPreferences = getSharedPreferences("DataPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        highscore = sharedPreferences.getInt("highscore_quiz", 0);

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        CheckingUtils.changeNotifBarColor("#2B3C50", getWindow());


        answer1 = (AppCompatButton) findViewById(R.id.quiz_answer_1);
        answer2 = (AppCompatButton) findViewById(R.id.quiz_answer_2);
        answer3 = (AppCompatButton) findViewById(R.id.quiz_answer_3);
        answer4 = (AppCompatButton) findViewById(R.id.quiz_answer_4);


        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(QuizActivity.this, QuizActivity.class ).putExtra("Tab", 1));
            }
        };


        View.OnClickListener wrongListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(canClick){

                countDownTimer.cancel();
                question++;
                canClick=false;




                switch (correct_answer){
                    case 0:
                        answer1.setBackgroundColor(Color.parseColor("#27ae60"));
                        v.setBackgroundColor(Color.parseColor("#e74c3c"));
                        break;

                    case 1:
                        answer2.setBackgroundColor(Color.parseColor("#27ae60"));
                        v.setBackgroundColor(Color.parseColor("#e74c3c"));
                        break;

                    case 2:
                        answer3.setBackgroundColor(Color.parseColor("#27ae60"));
                        v.setBackgroundColor(Color.parseColor("#e74c3c"));
                        break;

                    case 3:
                        answer4.setBackgroundColor(Color.parseColor("#27ae60"));
                        v.setBackgroundColor(Color.parseColor("#e74c3c"));
                        break;

                }

                CheckingUtils.vibrate(QuizActivity.this, 500);
                new Timer().schedule(task, 1500);
            }
            }

        };
        View.OnClickListener rightListener = new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if(canClick){


                canClick=false;
                question++;
                countDownTimer.cancel();
                v.setBackgroundColor(Color.parseColor("#27ae60"));
                new Timer().schedule(task, 1500);
                score++;
                score_textview.setText(score + "/" + question);
            }
            }
        };

        answer1.setOnClickListener(wrongListener);
        answer2.setOnClickListener(wrongListener);
        answer3.setOnClickListener(wrongListener);
        answer4.setOnClickListener(wrongListener);

        timer = (TextView) findViewById(R.id.quiz_timer);
        question_textview = (TextView) findViewById(R.id.quiz_question);
        score_textview = (TextView) findViewById(R.id.quiz_score);
        score_textview.setText(score + "/" + question);

        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Verdana.ttf");
        question_textview.setTypeface(tf);
        timer.setTypeface(tf);

        String question_title = null;
        String question_answer_1 = null;
        String question_answer_2 = null;
        String question_answer_3 = null;
        String question_answer_4 = null;
        String rightAnswer = null;

        switch (question){
            case 0:
                question_title = question_1[0];
                question_answer_1 = question_1[1];
                question_answer_2 = question_1[2];
                question_answer_3 = question_1[3];
                question_answer_4 = question_1[4];
                rightAnswer = question_1[5];

                break;
            case 1:

                question_title = question_2[0];
                question_answer_1 = question_2[1];
                question_answer_2 = question_2[2];
                question_answer_3 = question_2[3];
                question_answer_4 = question_2[4];
                rightAnswer = question_2[5];


                break;

            case 2:

                question_title = question_3[0];
                question_answer_1 = question_3[1];
                question_answer_2 = question_3[2];
                question_answer_3 = question_3[3];
                question_answer_4 = question_3[4];
                rightAnswer = question_3[5];


                break;

            case 3:

                question_title = question_4[0];
                question_answer_1 = question_4[1];
                question_answer_2 = question_4[2];
                question_answer_3 = question_4[3];
                question_answer_4 = question_4[4];
                answer1.setTextSize(14);
                answer2.setTextSize(14);
                answer3.setTextSize(14);
                answer4.setTextSize(14);
                rightAnswer = question_4[5];
                break;

            case 4:

                question_title = question_5[0];
                question_answer_1 = question_5[1];
                question_answer_2 = question_5[2];
                question_answer_3 = question_5[3];
                question_answer_4 = question_5[4];
                rightAnswer = question_5[5];
                break;

            case 5:

                question_title = question_6[0];
                question_answer_1 = question_6[1];
                question_answer_2 = question_6[2];
                question_answer_3 = question_6[3];
                question_answer_4 = question_6[4];
                rightAnswer = question_6[5];
                break;

            case 6:

                question_title = question_7[0];
                question_answer_1 = question_7[1];
                question_answer_2 = question_7[2];
                question_answer_3 = question_7[3];
                question_answer_4 = question_7[4];

                rightAnswer = question_7[5];
                break;


            case 7:

                question_title = question_8[0];
                question_answer_1 = question_8[1];
                question_answer_2 = question_8[2];
                question_answer_3 = question_8[3];
                question_answer_4 = question_8[4];
                rightAnswer = question_8[5];
                break;


            case 8:

                question_title = question_9[0];
                question_answer_1 = question_9[1];
                question_answer_2 = question_9[2];
                question_answer_3 = question_9[3];
                question_answer_4 = question_9[4];
                rightAnswer = question_9[5];
                break;


            case 9:

                question_title = question_10[0];
                question_answer_1 = question_10[1];
                question_answer_2 = question_10[2];
                question_answer_3 = question_10[3];
                question_answer_4 = question_10[4];
                rightAnswer = question_10[5];
                break;


            case 10:

                question_title = question_11[0];
                question_answer_1 = question_11[1];
                question_answer_2 = question_11[2];
                question_answer_3 = question_11[3];
                question_answer_4 = question_11[4];
                rightAnswer = question_11[5];
                break;


            case 11:

                question_title = question_12[0];
                question_answer_1 = question_12[1];
                question_answer_2 = question_12[2];
                question_answer_3 = question_12[3];
                question_answer_4 = question_12[4];
                rightAnswer = question_12[5];
                break;

            case 12:

                if(score>highscore){
                    sharedPreferences.edit().putInt("highscore_quiz", score).commit();
                }
                startActivity(new Intent(QuizActivity.this, YouLostQuizActivity.class).putExtra("score", score).putExtra("highscore_quiz", highscore));
                break;
        }

        question_textview.setText(question_title);

        generatePolnijBred(question_answer_1, question_answer_2, question_answer_3, question_answer_4);

        if(answer1.getText().toString().equals(rightAnswer)){
            answer1.setOnClickListener(rightListener);
            correct_answer = 0;
        }
        if(answer2.getText().toString().equals(rightAnswer)){
            answer2.setOnClickListener(rightListener);
            correct_answer = 1;
        }
        if(answer3.getText().toString().equals(rightAnswer)){
            answer3.setOnClickListener(rightListener);
            correct_answer = 2;
        }
        if(answer4.getText().toString().equals(rightAnswer)){
            answer4.setOnClickListener(rightListener);
            correct_answer = 3;
        }

        question_textview.setText(question_title);

        if(question<=11){
       countDownTimer = new CountDownTimer(16000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                canClick = false;
                try{

                    timer.setText(String.valueOf(0));

                    switch (correct_answer){
                        case 0:
                            answer1.setBackgroundColor(Color.parseColor("#27ae60"));
                            break;

                        case 1:
                            answer2.setBackgroundColor(Color.parseColor("#27ae60"));
                            break;

                        case 2:
                            answer3.setBackgroundColor(Color.parseColor("#27ae60"));
                            break;

                        case 3:
                            answer4.setBackgroundColor(Color.parseColor("#27ae60"));
                            break;
                    }

                    question++;
                    CheckingUtils.vibrate(QuizActivity.this, 500);
                    new Timer().schedule(task, 1500);

                }catch (Exception e){
                }

            }
        }.start();

    }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            countDownTimer.cancel();
            question = 0;
            score = 0;
        }
        return super.onKeyDown(keyCode, event);
    }

    private int random_int(int min, int max)
    {
        return (int) (Math.random()*(max-min))+min;
    }


    private void generatePolnijBred(String question_answer_1, String question_answer_2, String question_answer_3, String question_answer_4){
        int randomButtonQuestions = random_int(0,4);
        Log.i("TEST", String.valueOf(randomButtonQuestions));
        switch (randomButtonQuestions){
            case 0:

                answer1.setText(question_answer_1);
                answer2.setText(question_answer_3);
                answer3.setText(question_answer_4);
                answer4.setText(question_answer_2);
                break;
            case 1:
                answer1.setText(question_answer_2);
                answer2.setText(question_answer_1);
                answer3.setText(question_answer_3);
                answer4.setText(question_answer_4);
                break;

            case 2:
                answer1.setText(question_answer_1);
                answer2.setText(question_answer_4);
                answer3.setText(question_answer_2);
                answer4.setText(question_answer_3);
                break;

            case 3:
                answer1.setText(question_answer_4);
                answer2.setText(question_answer_3);
                answer3.setText(question_answer_2);
                answer4.setText(question_answer_1);
                break;
        }
    }

}