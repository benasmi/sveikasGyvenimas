package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.share.widget.ShareButton;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class InterestingFactsAdapter extends  RecyclerView.Adapter<InterestingFactsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FactInfoHolder> factDataHolder;
    private SharedPreferences sharedPreferences;
    private LayoutInflater layoutInflater;
    private String is_administrator;
    private boolean show = false;

    public InterestingFactsAdapter(Context context, ArrayList<FactInfoHolder> factHolder, String is_administrator) {
        this.context = context;
        this.factDataHolder = factHolder;
        this.is_administrator = is_administrator;

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

                if(data.getFactSource().isEmpty()){
                    holder.fact_source.setText("Mūsų Facebook puslapis");
                }else{
                    holder.fact_source.setText("Paspausk, kad sužinotum daugiau.");
                }


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

                    if(data.getHeight() == 0){

                        Glide
                                .with(context)
                                .load(data.getFactImageUrl())
                                .fitCenter()
                                .crossFade()
                                .animate(R.anim.slide_in_left)
                                .override(400, (int) CheckingUtils.convertPixelsToDp(400, context))
                                .into(holder.fact_image);
                    }else{
                        Glide
                                .with(context)
                                .load(data.getFactImageUrl())
                                .fitCenter()
                                .crossFade()
                                .animate(R.anim.slide_in_left)
                                .override(400,data.getHeight())
                                .into(holder.fact_image);
                    }

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
        private LinearLayout layout;

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

                    Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Verdana.ttf");
                    fact_title.setTypeface(tf);

                    if(is_administrator.equals("1")){

                        layout = (LinearLayout) itemView.findViewById(R.id.facts_item_layout);

                        layout.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {

                                String username = sharedPreferences.getString("username", "");
                                String password = sharedPreferences.getString("password", "");

                                String url = factDataHolder.get(getAdapterPosition()).getFactImageUrl();
                                String title = factDataHolder.get(getAdapterPosition()).getFactTitle();
                                String body = factDataHolder.get(getAdapterPosition()).getFactBody();
                                String source = factDataHolder.get(getAdapterPosition()).getFactSource();


                                if(!CheckingUtils.isNetworkConnected(context)){
                                    CheckingUtils.createErrorBox("Šiam veiksmui atlikti, reikalinga interneto ryšys", context, R.style.FactsDialogStyle);
                                    return false;
                                }

                                delete_fact(context,url,password,username,title,body,source,getAdapterPosition());

                                return true;
                            }
                        });

                    }


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

    public void delete_fact(final Context context, final String url, final String password,final String username, final String title, final String body, final String source , final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Ar tikrai norite pašalinti faktą ?")
                .setPositiveButton("TAIP", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new ServerManager(context, "DELETE_FACT").execute("DELETE_FACT",username, password,title,url, source, body);
                        remove(position);
                    }
                })
                .setNegativeButton("NE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
        builder.show();
        builder.create();
    }


}