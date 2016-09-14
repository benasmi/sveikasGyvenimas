package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Martyno on 2016.09.14.
 */
public class MarkerInfoClass implements GoogleMap.InfoWindowAdapter {

    private View view;

    public MarkerInfoClass(View view) {
        this.view = view;
    }


    @Override
    public View getInfoContents(Marker marker) {
        return null;

    }

    @Override
    public View getInfoWindow(Marker marker) {

        View view1 = view;

        TextView truck_name = (TextView) view1.findViewById(R.id.event_name_marker);
        TextView slogan = (TextView) view1.findViewById(R.id.event_date_marker);

        truck_name.setText(marker.getTitle());
        slogan.setText(marker.getSnippet());

        return view1;
    }
}
