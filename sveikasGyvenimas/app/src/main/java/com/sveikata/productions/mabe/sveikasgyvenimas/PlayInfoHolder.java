package com.sveikata.productions.mabe.sveikasgyvenimas;

/**
 * Created by Martynas on 2016-10-03.
 */
public class PlayInfoHolder {

    private int type;
    private String challenge_title;
    private String challenge_description;
    private String challenge_time;

    public PlayInfoHolder(int type, String challenge_description, String challenge_time, String challenge_title){
        this.type = type;
        this.challenge_title = challenge_title;
        this.challenge_description = challenge_description;
        this.challenge_time = challenge_time;
    }

    public PlayInfoHolder(int type){
        this.type = type;
    }

    public String getChallengeTitle() {
        return challenge_title;
    }

    public void setChallengeTitle(String challenge_title) {
        this.challenge_title = challenge_title;
    }

    public String getChallengeDescription() {
        return challenge_description;
    }

    public void setChallengeDescription(String challenge_description) {
        this.challenge_description = challenge_description;
    }

    public String getChallengeTime() {
        return challenge_time;
    }

    public void setChallengeTime(String challenge_time) {
        this.challenge_time = challenge_time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
