package com.sveikata.productions.mabe.sveikasgyvenimas;

/**
 * Created by Martyno on 2016.09.10.
 */
public class InfoHolder {


    public String event_name;
    public String event_location_and_date;
    public String event_description;
    public String recycler_view_type;
    public String project_name;
    public String project_description;

    public InfoHolder(String event_name, String event_location_and_date, String event_description, String recycler_view_type, String project_name, String project_description) {
        this.recycler_view_type = recycler_view_type;
        this.event_description = event_description;
        this.event_name = event_name;
        this.project_description = project_description;
        this.project_name = project_name;
        this.event_location_and_date = event_location_and_date;

    }

    public String getRecycler_view_type() {
        return recycler_view_type;
    }

    public void setRecycler_view_type(String recycler_view_type) {
        this.recycler_view_type = recycler_view_type;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getProject_description() {
        return project_description;
    }

    public void setProject_description(String project_description) {
        this.project_description = project_description;
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