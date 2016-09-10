package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
    public int getItemCount() {
        return infoHolder.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.schedule_item, parent, false);
        ViewHolder holder = new ViewHolder(view, 0);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        InfoHolder data = infoHolder.get(position);

        holder.event_date_and_place.setText(data.getEvent_location_and_date());
        holder.event_name.setText(data.getEvent_name());
        holder.event_description.setText(data.getEvent_description());

    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RelativeLayout layout;
        private TextView event_name;
        private TextView event_date_and_place;
        private TextView event_description;
        private boolean isClicked = true;

        public ViewHolder(View itemView, int type) {

            super(itemView);

            event_date_and_place = (TextView) itemView.findViewById(R.id.event_date_and_place);
            event_name = (TextView) itemView.findViewById(R.id.event_name);
            event_description = (TextView) itemView.findViewById(R.id.event_description);

            layout = (RelativeLayout) itemView.findViewById(R.id.text_wrap);
            layout.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            ResizeAnimation expand = new ResizeAnimation(view, (int) CheckingUtils.convertPixelsToDp(110, context), (int) CheckingUtils.convertPixelsToDp(55, context));
            expand.setDuration(200);
            ResizeAnimation shrink = new ResizeAnimation(view, (int) CheckingUtils.convertPixelsToDp(55, context), (int) CheckingUtils.convertPixelsToDp(110, context));
            shrink.setDuration(200);
            view.startAnimation(isClicked ? expand : shrink);

            isClicked = !isClicked;
        }
    }

}

