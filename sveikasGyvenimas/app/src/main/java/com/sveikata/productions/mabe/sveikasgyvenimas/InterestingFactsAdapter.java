package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.share.widget.ShareButton;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

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

    public void clear(){
        factDataHolder.clear();
        notifyDataSetChanged();

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
    public int getItemViewType(int position) {
        return factDataHolder.get(position).getViewType();
    }

    @Override
    public InterestingFactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        InterestingFactsAdapter.ViewHolder holder;

        switch (viewType){

            //Regular fact
            case 0:
                view = layoutInflater.inflate(R.layout.facts_item, parent, false);
                holder = new ViewHolder(view, viewType);
                return holder;

            //Add fact button (for admin)
            case 1:
                view = layoutInflater.inflate(R.layout.add_fact_button_layout, parent, false);
                holder = new ViewHolder(view, viewType);
                return holder;
        }

        return null;


    }


    @Override
    public void onBindViewHolder(final InterestingFactsAdapter.ViewHolder holder, int position) {
        final FactInfoHolder data = factDataHolder.get(position);

        switch(data.getViewType()){
            //Fact layout
            case 0:
                holder.fact_title.setText(data.getFactTitle());
                holder.fact_source.setText(data.getFactSource());
                holder.fact_body.setText(data.getFactBody());



                holder.fb_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(holder.fact_image!=null) {
                           CheckingUtils.shareFact(data.getFactTitle(), context, data.getFactSource(), data.getFactImageUrl(), data.getFactBody());
                        }
                    }
                });

                if(!data.getFactImageUrl().isEmpty()){
                Glide
                        .with(context)
                        .load(data.getFactImageUrl())
                        .fitCenter()
                        .crossFade()
                        .into(holder.fact_image);
                }else{
                    Glide.clear(holder.fact_image);
                }

                break;

            //Button (for admin)
            case 1:
                //Init something if needed
                break;
        }
    }

    @Override
    public int getItemCount() {
        return factDataHolder.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        //Fact layout
        private TextView fact_title;
        private TextView fact_body;
        private ImageView fact_image;
        private TextView fact_source;
        private AppCompatButton fb_share;

        //Button layout
        private AppCompatButton add_fact_button;

        public ViewHolder(View itemView, int type) {
            super(itemView);


            switch (type){
                //Fact layout
                case 0:
                    fact_title = (TextView) itemView.findViewById(R.id.fact_title);
                    fact_body = (TextView) itemView.findViewById(R.id.fact_text);
                    fact_image = (ImageView) itemView.findViewById(R.id.fact_image);
                    fact_source = (TextView) itemView.findViewById(R.id.fact_source);

                    fact_source.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!factDataHolder.get(getAdapterPosition()).getFactSource().isEmpty()){
                                try{
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(factDataHolder.get(getAdapterPosition()).getFactSource()));
                                    context.startActivity(intent);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }

                        }
                    });

                    fb_share = (AppCompatButton) itemView.findViewById(R.id.facebook_share_fact);



                    Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Verdana.ttf");
                    fact_title.setTypeface(tf);



                    break;


                //Button layout (admin)
                case 1:
                    add_fact_button = (AppCompatButton) itemView.findViewById(R.id.add_fact_button);
                    add_fact_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            context.startActivity(new Intent(context, InsertFactActivity.class));
                        }
                    });

                    break;
            }

        }
    }




}