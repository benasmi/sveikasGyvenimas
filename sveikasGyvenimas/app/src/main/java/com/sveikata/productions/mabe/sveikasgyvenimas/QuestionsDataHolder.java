package com.sveikata.productions.mabe.sveikasgyvenimas;

/**
 * Created by Benas on 9/18/2016.
 */
public class QuestionsDataHolder {

    private String question_title;
    private String question_body;


    public QuestionsDataHolder(String question_title, String question_body){
        this.question_title = question_title;
        this.question_body = question_body;
    }

    public String getQuestionTitle() {
        return question_title;
    }

    public void setQuestionTitle(String question_title) {
        this.question_title = question_title;
    }

    public String getQuestionBody() {
        return question_body;
    }

    public void setQuestionBody(String question_body) {
        this.question_body = question_body;
    }
}
