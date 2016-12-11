package com.sveikata.productions.mabe.sveikasgyvenimas;



public class Alcohol {

    private int resource;
    private String calories;
    private String name;
    private String protein;
    private String carbohydrates;
    private String lipids;



    public Alcohol(int resource, String calories, String name, String protein, String carbohydrates, String lipids){

        this.resource = resource;
        this.calories=calories;
        this.name = name;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.lipids = lipids;


    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(String carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public String getLipids() {
        return lipids;
    }

    public void setLipids(String lipids) {
        this.lipids = lipids;
    }

    public int getResource() {
        return resource;
    }

    public String getCalories() {
        return calories;
    }

    public String getName() {
        return name;
    }

}
