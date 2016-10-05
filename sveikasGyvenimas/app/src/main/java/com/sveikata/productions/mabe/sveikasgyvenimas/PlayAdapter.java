package com.sveikata.productions.mabe.sveikasgyvenimas;

/**
 * Created by Martyno on 2016.10.03.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Benas on 9/18/2016.
 */
public class PlayAdapter extends  RecyclerView.Adapter<PlayAdapter.ViewHolder> {


    private Context context;
    private ArrayList<PlayInfoHolder> play_info_holder;
    private SharedPreferences sharedPreferences;
    private LayoutInflater layoutInflater;


    public PlayAdapter(Context context, ArrayList<PlayInfoHolder> play_info_holder) {
        this.context = context;
        this.play_info_holder = play_info_holder;

        layoutInflater = LayoutInflater.from(context);
        sharedPreferences = context.getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);

    }

    public void remove(int position) {
        play_info_holder.remove(position);
        notifyItemRemoved(position);

    }

    public void add(PlayInfoHolder info, int position) {
        play_info_holder.add(position,info);
        notifyDataSetChanged();
    }




    @Override
    public int getItemViewType(int position) {
        return play_info_holder.get(position).getType();
    }

    @Override
    public PlayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       PlayAdapter.ViewHolder viewHolder = null;

        Log.i("type", String.valueOf(viewType));

        switch (viewType){
            case 0:
                View ask_question = layoutInflater.inflate(R.layout.calculator_preview, parent, false);
                viewHolder = new ViewHolder(ask_question, 0);
                return viewHolder;

            case 1:

                break;

            case 2:
                View send_challenge = layoutInflater.inflate(R.layout.send_challenge, parent,false);
                viewHolder = new ViewHolder(send_challenge,2);
                return viewHolder;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PlayAdapter.ViewHolder holder, int position) {
        PlayInfoHolder data = play_info_holder.get(position);


        switch (data.getType()){

        }

    }

    @Override
    public int getItemCount() {
        return play_info_holder.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        //Calculators
        private TextView calculator_text;
        private ImageView arrow_left;
        private ImageView arrow_right;
        private ImageView calculator_preview_image;
        private Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        private Animation top_down_anim = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/Verdana.ttf");
        private int which_image;
        boolean isAnimRunning = false;


        //Send challenge

        private Spinner challenges_spinner;
        private TextView create_challenge_manually;
        private EditText mail_receiver;
        private AppCompatButton send_challenge;

        public ViewHolder(View itemView, int type) {
            super(itemView);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    isAnimRunning = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isAnimRunning = false;
                    switch (which_image){
                        case 0:
                            calculator_preview_image.setImageResource(R.drawable.calories_calculator);
                            break;
                        case 1:
                            calculator_preview_image.setImageResource(R.drawable.water_calculator);
                            break;
                        case 2:
                            calculator_preview_image.setImageResource(R.drawable.your_drink);
                            break;
                        case 3:
                            calculator_preview_image.setImageResource(R.drawable.info_sheet);
                            break;
                        case 4:
                            calculator_preview_image.setImageResource(R.drawable.limit_calculator);
                            break;
                    }
                    calculator_preview_image.startAnimation(top_down_anim);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            switch (type) {

                case 0:
                    arrow_left = (ImageView) itemView.findViewById(R.id.arrow_left);
                    arrow_right = (ImageView) itemView.findViewById(R.id.arrow_right);
                    calculator_preview_image = (ImageView)itemView.findViewById(R.id.calculator_preview_image);
                    calculator_preview_image.setImageResource(R.drawable.calories_calculator);
                    calculator_text = (TextView) itemView.findViewById(R.id.calculator_text);


                    calculator_text.setTypeface(tf);

                    arrow_left.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!isAnimRunning){
                                which_image--;
                                if(which_image<0){
                                    which_image=4;
                                }
                                calculator_preview_image.startAnimation(animation);
                            }
                        }
                    });

                    arrow_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!isAnimRunning){
                                which_image++;
                                if(which_image>=4){
                                    which_image=0;
                                }

                                calculator_preview_image.startAnimation(animation);
                            }
                        }
                    });

                    calculator_preview_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            launchCalculator(which_image);
                        }
                    });


                case 1:

                    break;

                case 2:
                    TextView textView = (TextView) itemView.findViewById(R.id.textView6);
                    textView.setTypeface(tf);

                    challenges_spinner = (Spinner) itemView.findViewById(R.id.challenges_created);
                    final ArrayAdapter<CharSequence> gender_adapter = ArrayAdapter.createFromResource(context,
                            R.array.challenges, R.layout.spinner_item_challenge);
                    gender_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    challenges_spinner.setAdapter(gender_adapter);



                    create_challenge_manually = (TextView) itemView.findViewById(R.id.create_challenge_manually);
                    mail_receiver = (EditText) itemView.findViewById(R.id.mail_receiver);
                    create_challenge_manually.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            context.startActivity(new Intent(context, CreateChallengeManually.class));
                        }
                    });

                    send_challenge = (AppCompatButton) itemView.findViewById(R.id.send_challenge);
                    send_challenge.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!CheckingUtils.isNetworkConnected(context)){
                                CheckingUtils.createErrorBox("Norint nusiųsti iššūkį, tau reikia interneto", context);
                                return;
                            }

                            String challenge = challenges_spinner.getSelectedItem().toString();
                            String mail = mail_receiver.getText().toString();
                            String username = sharedPreferences.getString("username", "");
                            String password = sharedPreferences.getString("password", "");
                            String title =null;
                            String time =null;

                            if(challenge.equals("Nevartoti alkoholio 7 dienas")){
                                 title = "Nevartoju alkoholio";
                                 time = "7";
                            }

                            if(challenge.equals("Gerti bent penkias stiklines vandens per dieną(21diena)")){
                                title = "Gerti vandenį";
                                time = "21";
                            }

                            if(mail.isEmpty()){
                                mail_receiver.setError("Kam nusiųsti?");
                                return;
                            }
                            new ServerManager(context,"SEND_CHALLENGE").execute("SEND_CHALLENGE", challenge,mail,time,title, username,password);
                        }
                    });

            }

        }
    }

    private void launchCalculator(int which_img){
        Uri uri = null;
        switch (which_img){
            case 0:
                uri = Uri.parse("http://www.megaukismaistu.lt/2016/kaloriju-iseikvojimo-skaiciuokle");
                break;
            case 1:
                uri = Uri.parse("http://www.sulieknek.lt/skaiciuokles/skysciu-paros-normos-skaiciuokle/");
                break;
            case 2:
                uri = Uri.parse("https://www.drinkiq.com/en-gb/whats-in-your-drink/");
                break;
            case 3:
                uri = Uri.parse("http://www.los.lt/kiek-kaloriju-suvartojama-dirbant-ir-sportuojant/");
                break;
            case 4:
                uri = Uri.parse("https://www.drinkiq.com/en-gb/drink-calculator/");
                break;

        }

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);

    }
}