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

    public static final String[] question_13 = {"Kaip alkoholis veikia mūsų organizmą šaltyje?", "Šildo- žmogus sušyla, parausta ir jaučiasi geriau", "Šaldo – išsiplečia kraujagyslės ir greičiau atiduoda kūno šilumą į šaltą aplinką", "Alkoholis nei šildo, nei šaldo", "Vis dar bandau padaryti ledus iš alaus", "Šaldo – išsiplečia kraujagyslės ir greičiau atiduoda kūno šilumą į šaltą aplinką"};
    public static final String[] question_14 = {"Kiek mililitrų 5% alaus sudaro 1 standartinį alkoholio vienetą?", "200ml", "100ml", "500ml", "150ml", "200ml"};
    public static final String[] question_15 = {"Kiek mililitrų saldaus alkoholinio koktelio (3 %) sudaro standartinį alkoholio vienetą?", "333ml", "250ml", "500ml", "100ml", "333ml"};
    public static final String[] question_16 = {"Koks vidutinis alkoholio pašalinimo iš kraujo greitis?", "0,1 promilės/val", "1 promilė/val", "0,5 promilės/val", "0,25 promilės/val", "0,1 promilės/val"};
    public static final String[] question_17 = {"Ką galima tirti, kad būtų nustatyta alkoholio koncentracija organizme?", "Šlapimą", "Kraują", "Iškvėptą orą", "Visi variantai teisingi", "Visi variantai teisingi"};
    public static final String[] question_18 = {"Koks yra standartinis alkoholio vienetas?", "10 g 100 % gryno etanolio", "15 g 100 % gryno ethanolio", "10 g 50 % etanolio", "15 g 50 % etanolio", "10 g 100 % gryno etanolio"};
    public static final String[] question_19 = {"Koks alkoholio kiekis yra nepavojingas sveikatai?", "Nėra nepavojingo kiekio", "Ne daugiau 1 promilės", "Ne daugiau 2 promilių", "Ne daugiau 3 promilių", "Nėra nepavojingo kiekio"};
    public static final String[] question_20 = {"Koks alkoholio poveikis kaulams?", "Alkoholis mažina kaulų tankį, silpnina jų atsparumą krūviui, trikdo kalcio ir fosforo apykaitą", "Alkoholis apsunkina kaulų gijimą, ilgina gijimo trukmę", "Alkoholis stiprina kaulų atsparumą smūgiams, didina kaulų tankį", "Alkoholis mažina kaulų tankį, silpnina jų atsparumą krūviui, trikdo kalcio ir fosforo apykaitą ir apsunkina kaulų gijimą", "Alkoholis mažina kaulų tankį, silpnina jų atsparumą krūviui, trikdo kalcio ir fosforo apykaitą ir apsunkina kaulų gijimą"};
    public static final String[] question_21 = {"Koks alkoholio poveikis raumenims??", "Alkoholis skatina baltymų sintezę, didina raumeninę masę", "Alkoholis slopina baltymų sintezę, neleidžia augti raumenų masei", "Alkoholio vartojimas slopina augimo hormono išsiskyrimą", "Neleidžia augti raumenų masei IR slopina augimo hormono išsiskyrimą", "Neleidžia augti raumenų masei IR slopina augimo hormono išsiskyrimą"};
    public static final String[] question_22 = {"Koks alkoholio poveikis smegenims?", "Alkoholis slopina smegenis iki komos, iš pradžių sukeldamas euforiją, dėmesio sutrikimus, vėliau sukeldamas nerimą, drebėjimą.", "Alkoholis gali sukelti traukulius", "Alkoholis sukelia smegenų baltosios medžiagos nykimą, ypač smegenėlių, priekinės žievės ir hipokampo srities", "Visi išvardinti atvejai", "Visi išvardinti atvejai"};
    public static final String[] question_23 = {"Kiek g baltymų turi alkoholis?", "Neturi", "1g/100g", "2g/100g", "5g/100g", "Neturi"};
    public static final String[] question_24 = {"Koks alkoholio poveikis hormonams?", "Mažina testosterono gamybą vyrams", "Sutrikdo menustracijų ciklą merginoms", "Skatina augimo hormono išsiskyrimą", "Mažina testosterono gamybą vyrams ir trikdo menstruacijų tikslą moterims", "Mažina testosterono gamybą vyrams ir trikdo menstruacijų tikslą moterims"};
    public static final String[] question_25 = {"Koks alkoholio poveikis virškinamajam traktui?", "Alkoholis sukelia skrandžio uždegimą, didina tikimybę bakterijoms prasiskverbti iš žarnyno į kraują", "Alkoholis gerina skrandžio veiklą, todėl labai tinkamas sutrikus skrandžio veiklai ir vemiant", "Alkoholis mažina maisto pasisavinimą iš virškinamojo trakto į kraują, todėl geriantys žmonės lieknėja", "Alkoholis padeda kasai išskirti virškinimo fermentus į virškinimo traktą", "Alkoholis sukelia skrandžio uždegimą, didina tikimybę bakterijoms prasiskverbti iš žarnyno į kraują"};
    public static final String[] question_26 = {"Alkoholis sukelia skrandžio uždegimą, didina tikimybę bakterijoms prasiskverbti iš žarnyno į kraują", "Kraujyje", "Skrandyje", "Kepenyse", "Smegenyse", "Kepenyse"};
    public static final String[] question_27 = {"Kas gresia, jei nėščia moteris vartoja alkoholį?", "Vaikas gali gimti su veido deformacijomis, mažesnio svorio, gresia protinis atsilikimas", "Gresia pagimdyti dvynius", "Gresia skyrybos", "Visi paminėti atvejai", "Vaikas gali gimti su veido deformacijomis, mažesnio svorio, gresia protinis atsilikimas"};
    public static final String[] question_28 = {"Kas gresia vartojant alkoholį su narkotikais?", "Gresia mirtis", "Gresia vėmimas", "Gresia pamiršti kelią, kuriuo galima grįžti į namus", "Gresia užspringimas", "Gresia mirtis"};
    public static final String[] question_29 = {"Kokia šalis užima pirmą vietą pagal suvartojamo alkoholio kiekį asmeniui?", "Lietuva", "Bulgarija", "Prancūzija", "Vokietija", "Lietuva"};
    public static final String[] question_30 = {"Kas laikomi alkoholio surogatais?", "Etanolis", "Metanolis", "Etilenglikolis", "Metanolis ir Etilenglikolis", "Metanolis ir Etilenglikolis"};
    public static final String[] question_31 = {"Koks alkoholio poveikis širdžiai?", "Sukelia širdies ritmo sutrikimus", "Tiesiogiai pažeidžia širdies raumenį", "Padeda širdies kraujagyslėms geriau dirbti", "Sukelia širdies ritmo problemas bei žeidžia širdies raumenį", "Sukelia širdies ritmo problemas bei žeidžia širdies raumenį"};

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

                question_title = question_13[0];
                question_answer_1 = question_13[1];
                question_answer_2 = question_13[2];
                question_answer_3 = question_13[3];
                question_answer_4 = question_13[4];
                rightAnswer = question_13[5];
                answer1.setTextSize(14);
                answer2.setTextSize(14);
                answer3.setTextSize(14);
                answer4.setTextSize(14);
                break;
            case 13:

                question_title = question_14[0];
                question_answer_1 = question_14[1];
                question_answer_2 = question_14[2];
                question_answer_3 = question_14[3];
                question_answer_4 = question_14[4];
                rightAnswer = question_14[5];
                break;
            case 14:

                question_title = question_15[0];
                question_answer_1 = question_15[1];
                question_answer_2 = question_15[2];
                question_answer_3 = question_15[3];
                question_answer_4 = question_15[4];
                rightAnswer = question_15[5];
                break;
            case 15:

                question_title = question_16[0];
                question_answer_1 = question_16[1];
                question_answer_2 = question_16[2];
                question_answer_3 = question_16[3];
                question_answer_4 = question_16[4];
                rightAnswer = question_16[5];
                break;
            case 16:

                question_title = question_17[0];
                question_answer_1 = question_17[1];
                question_answer_2 = question_17[2];
                question_answer_3 = question_17[3];
                question_answer_4 = question_17[4];
                rightAnswer = question_17[5];
                break;
            case 17:

                question_title = question_18[0];
                question_answer_1 = question_18[1];
                question_answer_2 = question_18[2];
                question_answer_3 = question_18[3];
                question_answer_4 = question_18[4];
                rightAnswer = question_18[5];
                break;
            case 18:

                question_title = question_19[0];
                question_answer_1 = question_19[1];
                question_answer_2 = question_19[2];
                question_answer_3 = question_19[3];
                question_answer_4 = question_19[4];
                rightAnswer = question_19[5];
                break;
            case 19:

                question_title = question_20[0];
                question_answer_1 = question_20[1];
                question_answer_2 = question_20[2];
                question_answer_3 = question_20[3];
                question_answer_4 = question_20[4];
                rightAnswer = question_20[5];

                answer1.setTextSize(14);
                answer2.setTextSize(14);
                answer3.setTextSize(14);
                answer4.setTextSize(14);
                break;
            case 20:

                question_title = question_21[0];
                question_answer_1 = question_21[1];
                question_answer_2 = question_21[2];
                question_answer_3 = question_21[3];
                question_answer_4 = question_21[4];
                rightAnswer = question_21[5];

                answer2.setTextSize(14);
                answer3.setTextSize(14);
                answer4.setTextSize(14);
                break;
            case 21:

                question_title = question_22[0];
                question_answer_1 = question_22[1];
                question_answer_2 = question_22[2];
                question_answer_3 = question_22[3];
                question_answer_4 = question_22[4];
                rightAnswer = question_22[5];
                answer1.setTextSize(14);
                answer2.setTextSize(14);
                answer3.setTextSize(14);
                answer4.setTextSize(14);
                break;
            case 22:

                question_title = question_23[0];
                question_answer_1 = question_23[1];
                question_answer_2 = question_23[2];
                question_answer_3 = question_23[3];
                question_answer_4 = question_23[4];
                rightAnswer = question_23[5];
                break;
            case 23:

                question_title = question_24[0];
                question_answer_1 = question_24[1];
                question_answer_2 = question_24[2];
                question_answer_3 = question_24[3];
                question_answer_4 = question_24[4];
                rightAnswer = question_24[5];
                answer4.setTextSize(14);
                break;
            case 24:

                question_title = question_25[0];
                question_answer_1 = question_25[1];
                question_answer_2 = question_25[2];
                question_answer_3 = question_25[3];
                question_answer_4 = question_25[4];
                rightAnswer = question_25[5];
                answer1.setTextSize(14);
                answer2.setTextSize(14);
                answer3.setTextSize(14);
                answer4.setTextSize(14);
                break;
            case 25:

                question_title = question_26[0];
                question_answer_1 = question_26[1];
                question_answer_2 = question_26[2];
                question_answer_3 = question_26[3];
                question_answer_4 = question_26[4];
                rightAnswer = question_26[5];
                break;
            case 26:

                question_title = question_27[0];
                question_answer_1 = question_27[1];
                question_answer_2 = question_27[2];
                question_answer_3 = question_27[3];
                question_answer_4 = question_27[4];
                rightAnswer = question_27[5];
                answer1.setTextSize(14);
                break;
            case 27:

                question_title = question_28[0];
                question_answer_1 = question_28[1];
                question_answer_2 = question_28[2];
                question_answer_3 = question_28[3];
                question_answer_4 = question_28[4];
                rightAnswer = question_28[5];
                break;
            case 28:

                question_title = question_29[0];
                question_answer_1 = question_29[1];
                question_answer_2 = question_29[2];
                question_answer_3 = question_29[3];
                question_answer_4 = question_29[4];
                rightAnswer = question_29[5];
                break;
            case 29:

                question_title = question_30[0];
                question_answer_1 = question_30[1];
                question_answer_2 = question_30[2];
                question_answer_3 = question_30[3];
                question_answer_4 = question_30[4];
                rightAnswer = question_30[5];
                break;
            case 30:

                question_title = question_31[0];
                question_answer_1 = question_31[1];
                question_answer_2 = question_31[2];
                question_answer_3 = question_31[3];
                question_answer_4 = question_31[4];
                rightAnswer = question_31[5];
                break;

            case 31:

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

        if(question<=30){
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