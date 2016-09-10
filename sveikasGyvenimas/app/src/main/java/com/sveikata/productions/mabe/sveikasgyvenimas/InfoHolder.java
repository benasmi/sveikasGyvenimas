package com.sveikata.productions.mabe.sveikasgyvenimas;

/**
 * Created by Martyno on 2016.09.10.
 */
public class InfoHolder {

    public String type;
    public String event_name;
    public String event_location_and_date;
    public String event_description;

    public InfoHolder(String type, String event_name, String event_location_and_date, String event_description) {
        this.type = type;
        this.event_description = event_description;
        this.event_name = event_name;
        this.event_location_and_date = event_location_and_date;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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