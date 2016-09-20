package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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
    public void onBindViewHolder(InterestingFactsAdapter.ViewHolder holder, int position) {
        FactInfoHolder data = factDataHolder.get(position);

        switch(data.getViewType()){
            //Fact layout
            case 0:
                holder.fact_title.setText(data.getFactTitle());
                holder.fact_source.setText(data.getFactSource());
                holder.fact_body.setText(data.getFactBody());

                if(!data.getFactImageUrl().isEmpty()) {
                    new bitmapDownloadTask(holder.fact_image, data.getFactImageUrl()).execute();
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
                    break;

                //Button layout (admin)
                case 1:
                    add_fact_button = (AppCompatButton) itemView.findViewById(R.id.add_fact_button);
                    add_fact_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            context.startActivity(new Intent(context, InsertFactActiivty.class));
                        }
                    });
                    break;
            }

        }
    }



    class bitmapDownloadTask extends AsyncTask<String, Void, Void>{

        ImageView image;
        String url;
        Bitmap bitmap;

        public bitmapDownloadTask(ImageView image, String url){
            this.image = image;
            this.url = url;
        }

        @Override
        protected Void doInBackground(String... strings) {
            bitmap = getBitmapFromURL(url);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            image.setImageBitmap(bitmap);
        }

        private Bitmap getBitmapFromURL(String src) {
            try {
                java.net.URL url = new java.net.URL(src);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

    }
}