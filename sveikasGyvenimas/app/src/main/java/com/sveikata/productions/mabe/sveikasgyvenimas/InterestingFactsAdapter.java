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

public class InterestingFactsAdapter extends  RecyclerView.Adapter<InterestingFactsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FactInfoHolder> factDataHolder;
    private SharedPreferences sharedPreferences;
    private LayoutInflater layoutInflater;

    public InterestingFactsAdapter(Context context, ArrayList<FactInfoHolder> factHolder) {
        this.context = context;
        this.factDataHolder = factHolder;

        layoutInflater = LayoutInflater.from(context);

        sharedPreferences = context.getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);

    }

    public void remove(int position) {
        factDataHolder.remove(position);
        notifyItemRemoved(position);

    }

    public void add(FactInfoHolder info, int position) {
        factDataHolder.add(position,info);
        notifyDataSetChanged();
    }


    @Override
    public InterestingFactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //TODO: inflate layout

        View view;
        ViewHolder holder;

        switch (viewType){

            //Regular fact
            case 0:
                view = View.inflate(context, R.layout.facts_item, parent);
                holder = new ViewHolder(view);
                return holder;

            //Add fact button (for admin)
            case 1:
                view = View.inflate(context, R.layout.add_fact_button_layout, parent);
                holder = new ViewHolder(view);
                return holder;
        }

        return null;


    }

    @Override
    public void onBindViewHolder(InterestingFactsAdapter.ViewHolder holder, int position) {
        FactInfoHolder data = factDataHolder.get(position);

        holder.fact_title.setText(data.getFactTitle());
        holder.fact_source.setText(data.getFactSource());
        holder.fact_body.setText(data.getFactBody());
    }

    @Override
    public int getItemCount() {
        return factDataHolder.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView fact_title;
        private TextView fact_body;
        private ImageView fact_image;
        private TextView fact_source;

        public ViewHolder(View itemView) {
            super(itemView);

            fact_title = (TextView) itemView.findViewById(R.id.fact_title);
            fact_body = (TextView) itemView.findViewById(R.id.fact_text);
            fact_image = (ImageView) itemView.findViewById(R.id.fact_image);
            fact_source = (TextView) itemView.findViewById(R.id.fact_source);
        }
    }
}