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
                View ask_question = layoutInflater.inflate(R.layout.calculator_preview, parent, false);
                viewHolder = new ViewHolder(ask_question, 0);
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


        private ImageView arrow_left;
        private ImageView arrow_right;
        private ImageView calculator_preview_image;
        private Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        private Animation top_down_anim = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        private int which_image;
        boolean isAnimRunning = false;

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
                            calculator_preview_image.startAnimation(top_down_anim);
                            break;
                        case 1:
                            calculator_preview_image.setImageResource(R.drawable.water_calculator);
                            calculator_preview_image.startAnimation(top_down_anim);
                            break;
                        case 2:
                            //calculator_preview_image.setImageResource();
                            break;
                        case 3:
                            //calculator_preview_image.setImageResource();
                            break;
                        case 4:
                            //calculator_preview_image.setImageResource();
                            break;
                        case 5:
                            //calculator_preview_image.setImageResource();
                            break;
                    }
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
                    arrow_left.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(isAnimRunning){
                                which_image--;
                                if(which_image<0){
                                    which_image=5;
                                }
                                calculator_preview_image.startAnimation(animation);
                            }
                        }
                    });

                    arrow_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(isAnimRunning){
                                which_image++;
                                if(which_image>=5){
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


            }

        }
    }

    private void launchCalculator(int which_img){
        Uri uri = null;
        switch (which_img){
            case 0:
                uri = Uri.parse("https://www.google.lt/?gfe_rd=cr&ei=OtvzV_-rGO2v8wej-pagCQ");
                break;
            case 1:
                uri = Uri.parse("https://www.google.lt/?gfe_rd=cr&ei=OtvzV_-rGO2v8wej-pagCQ");
                break;
            case 2:
                uri = Uri.parse("https://www.google.lt/?gfe_rd=cr&ei=OtvzV_-rGO2v8wej-pagCQ");
                break;
            case 3:
                uri = Uri.parse("https://www.google.lt/?gfe_rd=cr&ei=OtvzV_-rGO2v8wej-pagCQ");
                break;
            case 4:
                uri = Uri.parse("https://www.google.lt/?gfe_rd=cr&ei=OtvzV_-rGO2v8wej-pagCQ");
                break;

        }

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);

    }
}