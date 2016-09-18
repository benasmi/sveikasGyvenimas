package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

    public RecyclerAdapterQuestions(Context context, ArrayList<QuestionsDataHolder> questionsDataHolder) {
        this.context = context;
        this.questionsDataHolder = questionsDataHolder;


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
        return null;
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

        private boolean isClicked = false;

        public ViewHolder(View itemView) {
            super(itemView);

            //View init
            question_body = (TextView) itemView.findViewById(R.id.question_body);
            question_title = (TextView) itemView.findViewById(R.id.question_title);
            question_title_layout = (RelativeLayout) itemView.findViewById(R.id.question_title_layout);
            question_body_layout = (RelativeLayout) itemView.findViewById(R.id.question_body_layout);
            layout = (RelativeLayout) itemView.findViewById(R.id.text_wrap);

            //Expansion on click
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    float expandedHeight = question_title_layout.getHeight() + question_body_layout.getHeight();
                    float collapsedHeight = question_title_layout.getHeight();

                    ResizeAnimation expand = new ResizeAnimation(layout, (int) CheckingUtils.convertPixelsToDp(collapsedHeight, context), (int) CheckingUtils.convertPixelsToDp(expandedHeight, context));
                    expand.setDuration(200);
                    ResizeAnimation shrink = new ResizeAnimation(layout, (int) CheckingUtils.convertPixelsToDp(expandedHeight, context), (int) CheckingUtils.convertPixelsToDp(collapsedHeight, context));
                    shrink.setDuration(200);
                    layout.startAnimation(isClicked ? expand : shrink);

                    isClicked = !isClicked;
                }
            });
        }
    }
}
