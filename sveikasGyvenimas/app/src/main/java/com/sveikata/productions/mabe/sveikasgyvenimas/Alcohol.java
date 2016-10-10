package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.graphics.Bitmap;

/**
 * Created by Benas on 10/9/2016.
 */

public class Alcohol {

    private int resource;
    private String volume;
    private String name;

    public Alcohol(int resource, String volume, String name){

        this.resource = resource;
        this.volume=volume;
        this.name = name;


    }

    public int getResource() {
        return resource;
    }

    public String getVolume() {
        return volume;
    }

    public String getName() {
        return name;
    }
}
