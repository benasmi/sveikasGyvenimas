package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.widget.RelativeLayout;

import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Martyno on 2016.09.10.
 */
public class InfoHolder {


    public String event_name;
    public String event_location_and_date;
    public String event_description;
    public String recycler_view_type;
    public double longtitude;
    public double latitude;

    public InfoHolder(String event_name, String event_location_and_date, String event_description, String recycler_view_type, double latitude, double longtitude) {
        this.recycler_view_type = recycler_view_type;
        this.event_description = event_description;
        this.event_name = event_name;
        this.event_location_and_date = event_location_and_date;
        this.longtitude = longtitude;
        this.latitude = latitude;
    }


    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getRecycler_view_type() {
        return recycler_view_type;
    }

    public void setRecycler_view_type(String recycler_view_type) {
        this.recycler_view_type = recycler_view_type;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_location_and_date() {
        return event_location_and_date;
    }

    public void setEvent_location_and_date(String event_location_and_date) {
        this.event_location_and_date = event_location_and_date;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }
}