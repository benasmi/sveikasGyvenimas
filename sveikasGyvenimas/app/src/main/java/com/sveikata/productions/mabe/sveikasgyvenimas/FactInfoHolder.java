package com.sveikata.productions.mabe.sveikasgyvenimas;

/**
 * Created by Martyno on 2016.09.19.
 */

public class FactInfoHolder {

    private String fact_title;
    private String fact_body;
    private String fact_image_url;
    private String fact_source;
    private int type;

    public FactInfoHolder(String title, String body, String image_URL, String source, int type){
        this.fact_title = title;
        this.fact_body = body;
        this.fact_image_url = image_URL;
        this.fact_source = source;
        this.type = type;
    }

    public int getViewType(){
        return type;
    }
    public void setViewType(int type){
        this.type = type;
    }

    public String getFactTitle() {
        return fact_title;
    }

    public void setFactTitle(String fact_title) {
        this.fact_title = fact_title;
    }

    public String getFactBody() {
        return fact_body;
    }

    public void setFactBody(String fact_body) {
        this.fact_body = fact_body;
    }

    public String getFactImageUrl() {
        return fact_image_url;
    }

    public void setFactImageUrl(String fact_image_url) {
        this.fact_image_url = fact_image_url;
    }

    public String getFactSource() {
        return fact_source;
    }

    public void setFactSource(String fact_source) {
        this.fact_source = fact_source;
    }
}
