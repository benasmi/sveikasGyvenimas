package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.service.chooser.ChooserTarget;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benas on 9/18/2016.
 */
public class RecyclerAdapterQuestions extends  RecyclerView.Adapter<RecyclerAdapterQuestions.ViewHolder> {

    private Context context;
    private ArrayList<QuestionsDataHolder> questionsDataHolder;
    private SharedPreferences sharedPreferences;
    private LayoutInflater layoutInflater;

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
    public RecyclerAdapterQuestions.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ask_question = layoutInflater.inflate(R.layout.faq_layout, parent, false);
        RecyclerAdapterQuestions.ViewHolder viewHolder = new ViewHolder(ask_question);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapterQuestions.ViewHolder holder, int position) {
        QuestionsDataHolder data = questionsDataHolder.get(position);

        holder.question_title.setText(data.getQuestionTitle());
        holder.question_body.setText(data.getQuestionBody());
    }

    @Override
    public int getItemCount() {
        return questionsDataHolder.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        //Views
        private TextView question_title;
        private TextView question_body;

        private RelativeLayout layout;
        private RelativeLayout question_title_layout;
        private RelativeLayout question_body_layout;
        private ImageView arrow;


        private boolean isClicked = false;

        public ViewHolder(View itemView) {
            super(itemView);

            //View init
            question_body = (TextView) itemView.findViewById(R.id.question_body);
            question_title = (TextView) itemView.findViewById(R.id.question_title);

            question_title_layout = (RelativeLayout) itemView.findViewById(R.id.question_title_layout);
            question_body_layout = (RelativeLayout) itemView.findViewById(R.id.question_body_layout);
            layout = (RelativeLayout) itemView.findViewById(R.id.text_wrap);
            arrow = (ImageView) itemView.findViewById(R.id.arrow_image);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/bevan.ttf");

            question_title.setTypeface(tf);

            //Expansion on click
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    float extraHeight = CheckingUtils.convertPixelsToDp(22, context);
                    float extraHeightCollapsed = CheckingUtils.convertPixelsToDp(10, context);
                    final float expandedHeight = question_title.getLayout().getHeight() + question_body.getLayout().getHeight() + extraHeight;
                    final float collapsedHeight = question_title.getLayout().getHeight() + extraHeightCollapsed;

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


        }
    }
}


