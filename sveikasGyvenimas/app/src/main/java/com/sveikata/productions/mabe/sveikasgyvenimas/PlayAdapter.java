package com.sveikata.productions.mabe.sveikasgyvenimas;

/**
 * Created by Martyno on 2016.10.03.
 */
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.service.chooser.ChooserTarget;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

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
                break;

            case 3:
                View completed_challenge = layoutInflater.inflate(R.layout.challenge_icon, parent, false);
                viewHolder = new ViewHolder(completed_challenge, 3);
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
                holder.timer.setText(data.getChallengeTime());
                break;
            case 2: //Send challenge layout
                break;
            case 3: //Completed challenges
                holder.completed_challenge_description.setText(data.getChallengeDescription());
                holder.completed_challenge_title.setText(data.getChallengeTitle());
                break;

        }

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
        private int which_image;
        boolean isAnimRunning = false;

        //New challenge layout
        private TextView timer;
        private TextView challenge_title;
        private TextView challenge_description;
        private ImageButton accept_challenge;
        private ImageButton decline_challenge;

        //Completed challenge layout
        private TextView completed_challenge_title;
        private TextView completed_challenge_description;

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
                    calculator_text = (TextView) itemView.findViewById(R.id.calculator_text);
                    challenge_text = (TextView) itemView.findViewById(R.id.chellanges_text);


                    calculator_text.setTypeface(verdanaFont);
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

                    timer = (TextView) itemView.findViewById(R.id.timer);
                    challenge_description = (TextView) itemView.findViewById(R.id.new_challenge_description);
                    challenge_title = (TextView) itemView.findViewById(R.id.new_challenge_title);
                    accept_challenge = (ImageButton) itemView.findViewById(R.id.accept_button);
                    decline_challenge = (ImageButton) itemView.findViewById(R.id.decline_button);

                    timer.setTypeface(verdanaFont);

                    //Accepting challenge
                    accept_challenge.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new ServerManager(context, "").execute("ACCEPT_CHALLENGE", sharedPreferences.getString("username", ""), sharedPreferences.getString("password", ""));
                        }
                    });
                    decline_challenge.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new ServerManager(context, "").execute("DECLINE_CHALLENGE", sharedPreferences.getString("username", ""), sharedPreferences.getString("password", ""));
                        }
                    });


                    break;

                case 2:
                    break;

                case 3:
                    completed_challenge_title = (TextView) itemView.findViewById(R.id.trophy_title);
                    completed_challenge_description = (TextView) itemView.findViewById(R.id.trophy_description);
                    break;

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