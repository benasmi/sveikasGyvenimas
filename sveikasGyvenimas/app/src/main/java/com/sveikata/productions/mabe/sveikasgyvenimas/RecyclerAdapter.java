package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Martyno on 2016.09.10.
 */
class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

    public Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<InfoHolder> infoHolder;

    public RecycleAdapter(Context context, ArrayList<InfoHolder> infoHolder) {
        this.infoHolder = infoHolder;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;

    }

    public void remove(int position) {
        infoHolder.remove(position);
        notifyItemRemoved(position);

    }


    public void add(InfoHolder info) {
        infoHolder.add(0,info);
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        InfoHolder data = infoHolder.get(position);
        int type = Integer.valueOf(data.getType());


        return type;
    }

    @Override
    public int getItemCount() {
        return infoHolder.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case 0:

//                View view = layoutInflater.inflate(R.layout.list_item, parent, false);
//                ViewHolder holder = new ViewHolder(view, 0);
//                return holder;

            case 1:

//                View view1 = layoutInflater.inflate(R.layout.message_list_item, parent, false);
//                ViewHolder holder1 = new ViewHolder(view1, 1);
//                return holder1;

        }



        return null;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        InfoHolder data = infoHolder.get(position);


        String dataType = data.getType();
        if (dataType.equals("0")) {
            //HANDLE ADMINISTRATOR
        } else {
           //HANDLE USER
        }
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView, int type) {

            super(itemView);


            switch (type) {
                case 0:

                    break;
                case 1:

                    break;
            }


        }


    }

}

