package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import pl.droidsonroids.gif.GifImageView;

public class RecyclerAdapterQuestions extends  RecyclerView.Adapter<RecyclerAdapterQuestions.ViewHolder> {




    private Context context;
    private ArrayList<QuestionsDataHolder> questionsDataHolder;
    private SharedPreferences sharedPreferences;
    private LayoutInflater layoutInflater;
    private boolean show = false;

    public RecyclerAdapterQuestions(Context context, ArrayList<QuestionsDataHolder> questionsDataHolder) {
        this.context = context;
        this.questionsDataHolder = questionsDataHolder;

        layoutInflater = LayoutInflater.from(context);

        sharedPreferences = context.getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);

    }

    public void remove(int position) {
        questionsDataHolder.remove(position);
        notifyItemRemoved(position);

    }

    public void add(QuestionsDataHolder info, int position) {
        questionsDataHolder.add(position,info);
        notifyDataSetChanged();
    }




    @Override
    public int getItemViewType(int position) {
        return questionsDataHolder.get(position).getType();
    }

    @Override
    public RecyclerAdapterQuestions.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerAdapterQuestions.ViewHolder viewHolder = null;

        switch (viewType){
            case 0:
                View ask_question = layoutInflater.inflate(R.layout.faq_layout, parent, false);
                 viewHolder = new ViewHolder(ask_question, 0);
                return viewHolder;
            case 1:
                View ask_question_admin = layoutInflater.inflate(R.layout.faq_layout_ask_question, parent, false);
                viewHolder = new ViewHolder(ask_question_admin, 1);
                return viewHolder;
            case 2:
                View faq_two_lines = layoutInflater.inflate(R.layout.faq_layout_two_lines, parent, false);
                viewHolder = new ViewHolder(faq_two_lines, 2);
                return viewHolder;
            case 3:
                View phone_help = layoutInflater.inflate(R.layout.phone_help_layout, parent, false);
                viewHolder = new ViewHolder(phone_help, 3);
                return viewHolder;

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapterQuestions.ViewHolder holder, int position) {
        QuestionsDataHolder data = questionsDataHolder.get(position);
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.slide_in_left);
        Animation animation_right = AnimationUtils.loadAnimation(context,R.anim.slide_in_right);

        switch (data.getType()){
            case 0:
                holder.question_title.setText(data.getQuestionTitle());
                holder.question_body.setText(data.getQuestionBody());
                if(position%2==0){
                    holder.layout.startAnimation(animation);
                }else{
                    holder.layout.startAnimation(animation_right);
                }
                break;

            case 2:
                holder.question_title_two.setText(data.getQuestionTitle());
                holder.question_body_two.setText(data.getQuestionBody());
                if(position%2==0){
                    holder.layout_two.startAnimation(animation);
                }else{
                    holder.layout_two.startAnimation(animation_right);
                }
                break;

        }


    }

    @Override
    public int getItemCount() {
        return questionsDataHolder.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private GifImageView phone_help;
        //Views for one line faq
        private TextView question_title;
        private TextView question_body;
        private RelativeLayout layout;
        private RelativeLayout question_title_layout;
        private RelativeLayout question_body_layout;
        private ImageView arrow;

        //Views for two line faq
        private TextView question_title_two;
        private TextView question_body_two;
        private RelativeLayout layout_two;
        private RelativeLayout question_title_layout_two;
        private RelativeLayout question_body_layout_two;
        private ImageView arrow_two;


        //Ask Question layout
        private TextView faq_txt;
        private GifImageView ask_pro;



        private boolean isClicked = false;

        public ViewHolder(View itemView, int type) {
            super(itemView);

            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/bevan.ttf");
            switch (type){



                case 0:
                    //View init
                    question_body = (TextView) itemView.findViewById(R.id.question_body);
                    question_title = (TextView) itemView.findViewById(R.id.question_title);

                    question_title_layout = (RelativeLayout) itemView.findViewById(R.id.question_title_layout);
                    question_body_layout = (RelativeLayout) itemView.findViewById(R.id.question_body_layout);
                    layout = (RelativeLayout) itemView.findViewById(R.id.text_wrap);
                    arrow = (ImageView) itemView.findViewById(R.id.arrow_image);


                    question_title.setTypeface(tf);

                    //Expansion on click
                    layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            float additionalHeight = CheckingUtils.convertPixelsToDp(5, context);
                            float collapsedHeight = CheckingUtils.convertPixelsToDp(35, context);
                            float expandedHeight =question_body.getLayout().getHeight() + collapsedHeight + additionalHeight;

                            ResizeAnimation expand = new ResizeAnimation(layout, (int) collapsedHeight, (int) expandedHeight);
                            expand.setDuration(200);
                            ResizeAnimation shrink = new ResizeAnimation(layout, (int) expandedHeight, (int) collapsedHeight);
                            shrink.setDuration(200);
                            layout.startAnimation(isClicked ? expand : shrink);

                            Animation animation = AnimationUtils.loadAnimation(context, R.anim.flip);
                            arrow.startAnimation(animation);

                            animation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    arrow.setImageResource(isClicked ?  R.drawable.arrow_up : R.drawable.arrow_down);

                                }
                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });

                            isClicked = !isClicked;
                        }
                    });
                    break;

                case 1:

                    //Ask pro
                    Typeface tf_txt = Typeface.createFromAsset(context.getAssets(), "fonts/comforta.ttf");

                    faq_txt = (TextView) itemView.findViewById(R.id.faq_txt);
                    faq_txt.setTypeface(tf_txt);

                    ask_pro = (GifImageView) itemView.findViewById(R.id.ask_pro);

                    ask_pro.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            context.startActivity(new Intent(context, AskProfessionalActivity.class));
                        }
                    });

                    break;
                case 2:
                    //View init
                    question_body_two = (TextView) itemView.findViewById(R.id.question_body_two);
                    question_title_two = (TextView) itemView.findViewById(R.id.question_title_two);

                    question_title_layout_two = (RelativeLayout) itemView.findViewById(R.id.question_title_layout_two);
                    question_body_layout_two = (RelativeLayout) itemView.findViewById(R.id.question_body_layout_two);
                    layout_two = (RelativeLayout) itemView.findViewById(R.id.text_wrap_two);
                    arrow_two = (ImageView) itemView.findViewById(R.id.arrow_image_two);

                    question_title_two.setTypeface(tf);

                    //Expansion on click
                    layout_two.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            float additionalHeight = CheckingUtils.convertPixelsToDp(5, context);
                            float collapsedHeight = CheckingUtils.convertPixelsToDp(60, context);
                            float expandedHeight =question_body_two.getLayout().getHeight() + collapsedHeight + additionalHeight;

                            ResizeAnimation expand = new ResizeAnimation(layout_two, (int) collapsedHeight, (int) expandedHeight);
                            expand.setDuration(200);
                            ResizeAnimation shrink = new ResizeAnimation(layout_two, (int) expandedHeight, (int) collapsedHeight);
                            shrink.setDuration(200);
                            layout_two.startAnimation(isClicked ? expand : shrink);

                            Animation animation = AnimationUtils.loadAnimation(context, R.anim.flip);
                            arrow_two.startAnimation(animation);

                            animation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    arrow_two.setImageResource(isClicked ?  R.drawable.arrow_up : R.drawable.arrow_down);

                                }
                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });

                            isClicked = !isClicked;
                        }
                    });
                    break;

                case 3:
                    phone_help = (GifImageView) itemView.findViewById(R.id.phone_help);

                    phone_help.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                    break;

            }



        }
    }

}