package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.graphics.Bitmap;

/**
 * Created by Benas on 10/9/2016.
 */

public class Alcohol {

    private int resource;
    private float volume;
    private String name;

    public Alcohol(int resource, float volume, String name){

        this.resource = resource;
        this.volume=volume;
        this.name = name;


    }

    public int getResource() {
        return resource;
    }

    public float getVolume() {
        return volume;
    }

    public String getName() {
        return name;
    }
}
