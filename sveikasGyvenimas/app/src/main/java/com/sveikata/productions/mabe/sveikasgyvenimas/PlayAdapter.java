package com.sveikata.productions.mabe.sveikasgyvenimas;

/**
 * Created by Martyno on 2016.10.03.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


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
                View layout_preview = layoutInflater.inflate(R.layout.calculator_preview, parent, false);
                viewHolder = new ViewHolder(layout_preview, 0);
                return viewHolder;
            case 1:
                View new_challenge = layoutInflater.inflate(R.layout.new_challenge_layout, parent, false);
                viewHolder = new ViewHolder(new_challenge, 1);
                return viewHolder;

            case 2:
                View send_challenge = layoutInflater.inflate(R.layout.send_challenge, parent,false);
                viewHolder = new ViewHolder(send_challenge,2);
                return viewHolder;

            case 3:
                View completed_challenge = layoutInflater.inflate(R.layout.challenge_icon, parent, false);
                viewHolder = new ViewHolder(completed_challenge, 3);
                return viewHolder;
            case 4:
                View challenge_in_progress = layoutInflater.inflate(R.layout.challenge_in_progress, parent, false);
                viewHolder = new ViewHolder(challenge_in_progress, 4);
                return viewHolder;

            case 5:
                View get_question = layoutInflater.inflate(R.layout.get_random_question, parent, false);
                viewHolder = new ViewHolder(get_question, 5);
                return viewHolder;


        }

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(PlayAdapter.ViewHolder holder, int position) {
        PlayInfoHolder data = play_info_holder.get(position);

        switch (data.getType()){

            case 0: //Preview calculators
                break;
            case 1: //current challenge layout
                holder.challenge_title.setText(data.getChallengeTitle());
                holder.challenge_description.setText(data.getChallengeDescription());
                holder.challenge_sender.setText(data.getChallengeSender());
                holder.timer.setText("Iššūkis truks " + data.getChallengeTime() + " d.");
                break;
            case 2: //Send challenge layout
                break;
            case 3: //Completed challenges
                holder.completed_challenge_description.setText(data.getChallengeDescription());
                holder.completed_challenge_title.setText(data.getChallengeTitle());
                holder.completed_challenge_sender.setText(data.getChallengeTitle());
                break;
            case 4: //Challenge in progress layout
                holder.challenge_in_progress_description.setText(data.getChallengeDescription());
                holder.challenge_in_progress_title.setText(data.getChallengeTitle());
                holder.challenge_in_progress_sender.setText(data.getChallengeTitle());
                countDownStart(holder.days_proggress, holder.hours_progress, holder.minutes_progress, holder.seconds_progress, data.getChallengeTime());

                break;




        }

    }


    public void countDownStart(final TextView dayView, final TextView hoursView, final TextView minutesView, final TextView secondsView, final String data) {

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd");
                    // Here Set your Event Date
                    Date futureDate = dateFormat.parse(data);
                    Date currentDate = new Date();
                    if (!currentDate.after(futureDate)) {
                        long diff = futureDate.getTime()
                                - currentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;
                        dayView.setText("" + String.format("%02d", days));
                        hoursView.setText("" + String.format("%02d", hours));
                        minutesView.setText("" + String.format("%02d", minutes));
                        secondsView.setText("" + String.format("%02d", seconds));
                    } else {
                        String username = sharedPreferences.getString("username", "");
                        String password = sharedPreferences.getString("password", "");
                        handler.removeCallbacks(this);

                        new ServerManager(context, "COMPLETE_CHALLENGE").execute("COMPLETE_CHALLENGE", username, password);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        handler.postDelayed(runnable, 0);
    }

    @Override
    public int getItemCount() {
        return play_info_holder.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private Typeface verdanaFont;


        //Calculator preview layout
        private TextView calculator_text;
        private TextView challenge_text;
        private ImageView arrow_left;
        private ImageView arrow_right;
        private ImageView calculator_preview_image;
        private Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        private Animation top_down_anim = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/Verdana.ttf");
        private int which_image;
        boolean isAnimRunning = false;





        //Send challenge
        private AppCompatButton go_to_send_activity;


        //New challenge layout
        private TextView timer;
        private TextView challenge_title;
        private TextView challenge_description;
        private ImageButton accept_challenge;
        private ImageButton decline_challenge;
        private TextView challenge_sender;

        //Challenge in progress layout
        private TextView challenge_in_progress_title;
        private TextView challenge_in_progress_description;
        private TextView challenge_in_progress_sender;

        //Completed challenge layout
        private TextView completed_challenge_title;
        private TextView completed_challenge_description;
        private TextView completed_challenge_sender;

        public TextView days_proggress;
        public TextView hours_progress;
        public TextView minutes_progress;
        public TextView seconds_progress;

        private ImageButton failed;

        private ImageView get_random_question;
        public ViewHolder(View itemView, int type) {
            super(itemView);

            verdanaFont = Typeface.createFromAsset(context.getAssets(),"fonts/Verdana.ttf");

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

                case 0: //Calculator previews

                    arrow_left = (ImageView) itemView.findViewById(R.id.arrow_left);
                    arrow_right = (ImageView) itemView.findViewById(R.id.arrow_right);
                    calculator_preview_image = (ImageView)itemView.findViewById(R.id.calculator_preview_image);
                    calculator_preview_image.setImageResource(R.drawable.calories_calculator);
                    challenge_text = (TextView) itemView.findViewById(R.id.chellanges_text);

                    challenge_text.setTypeface(verdanaFont);


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

                    break;

                case 1: //New challenge layout


                    challenge_description = (TextView) itemView.findViewById(R.id.new_challenge_description);
                    challenge_title = (TextView) itemView.findViewById(R.id.new_challenge_title);
                    accept_challenge = (ImageButton) itemView.findViewById(R.id.accept_button);
                    decline_challenge = (ImageButton) itemView.findViewById(R.id.decline_button);
                    challenge_sender = (TextView) itemView.findViewById(R.id.challenge_sender);
                    timer = (TextView) itemView.findViewById(R.id.days);
                    timer.setTypeface(tf);

                    //Accepting challenge
                    accept_challenge.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!CheckingUtils.isNetworkConnected(context)){
                                CheckingUtils.createErrorBox("Norint priimti iššūkį, jums reikia interneto", context);
                                return;
                            }else{
                                PlayActivity.shouldAddInfo=true;
                                HealthyLifeActivity.addData=true;
                                InterestingFactsActivity.addFactsFirstTime=true;
                                AskQuestionsActivity.addFAQData=true;

                                new ServerManager(context, "").execute("ACCEPT_CHALLENGE", sharedPreferences.getString("username", ""), sharedPreferences.getString("password", ""));

                            }

                        }
                    });
                    decline_challenge.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!CheckingUtils.isNetworkConnected(context)){
                                CheckingUtils.createErrorBox("Norint nusiųsti iššūkį, tau reikia interneto", context);
                                return;
                            }else{
                                PlayActivity.shouldAddInfo=true;
                                HealthyLifeActivity.addData=true;
                                InterestingFactsActivity.addFactsFirstTime=true;
                                AskQuestionsActivity.addFAQData=true;

                                remove(getAdapterPosition());
                                new ServerManager(context, "").execute("DECLINE_CHALLENGE", sharedPreferences.getString("username", ""), sharedPreferences.getString("password", ""));

                            }


                        }
                    });


                    break;

                case 2:

                            go_to_send_activity = (AppCompatButton) itemView.findViewById(R.id.go_to_send_challenge_activity);

                            go_to_send_activity.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    context.startActivity(new Intent(context, CreateChallengeManually.class));
                                }
                            });


                    break;


                case 3: //Trophy layout
                    completed_challenge_title = (TextView) itemView.findViewById(R.id.trophy_title);
                    completed_challenge_description = (TextView) itemView.findViewById(R.id.trophy_description);
                    completed_challenge_sender = (TextView) itemView.findViewById(R.id.trophy_sender);
                    break;


                case 4: //Challenge in progress layout
                    challenge_in_progress_description = (TextView) itemView.findViewById(R.id.challenge_in_progress_description);
                    challenge_in_progress_title = (TextView) itemView.findViewById(R.id.challenge_in_progress_title);
                    challenge_in_progress_sender = (TextView) itemView.findViewById(R.id.challenge_in_progress_sender);

                    days_proggress = (TextView) itemView.findViewById(R.id.days);
                    hours_progress = (TextView) itemView.findViewById(R.id.hours );
                    minutes_progress = (TextView) itemView.findViewById(R.id.minutes);
                    seconds_progress = (TextView) itemView.findViewById(R.id.seconds);

                    days_proggress.setTypeface(verdanaFont);
                    hours_progress.setTypeface(verdanaFont);
                    minutes_progress.setTypeface(verdanaFont);
                    seconds_progress.setTypeface(verdanaFont);

                    failed = (ImageButton) itemView.findViewById(R.id.failed_button);
                    failed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!CheckingUtils.isNetworkConnected(context)){
                                CheckingUtils.createErrorBox("FAIL", context);
                                return;
                            }else{

                                String username = sharedPreferences.getString("username", "");
                                String password = sharedPreferences.getString("password", "");

                                remove(getAdapterPosition());

                                new ServerManager(context, "I_FAILED_CHALLENGE").execute("I_FAILED_CHALLENGE", username, password);


                            }
                        }
                    });
                    break;

                case 5:

                    get_random_question = (ImageView) itemView.findViewById(R.id.get_random_question);

                    get_random_question.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            context.startActivity(new Intent(context, QuizActivity.class));
                        }
                    });


            }

        }
    }

    // //////////////COUNT DOWN START/////////////////////////

    // //////////////COUNT DOWN END/////////////////////////

    private void launchCalculator(int which_img)
    {
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