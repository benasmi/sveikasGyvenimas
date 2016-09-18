package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benas on 9/18/2016.
 */
public class RecyclerAdapterQuestions extends  RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<QuestionsDataHolder> questionsDataHolder;
    private String is_administrator;
    private SharedPreferences sharedPreferences;

    public RecyclerAdapterQuestions(Context context, ArrayList<QuestionsDataHolder> questionsDataHolder, String is_administrator) {
        this.context = context;
        this.questionsDataHolder = questionsDataHolder;
        this.is_administrator = is_administrator;

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
        QuestionsDataHolder data = questionsDataHolder.get(position);
        //return data.getRecycler_view_type();
        return 0;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        final QuestionsDataHolder data = questionsDataHolder.get(position);

    }

    @Override
    public int getItemCount() {
        return questionsDataHolder.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }





}
