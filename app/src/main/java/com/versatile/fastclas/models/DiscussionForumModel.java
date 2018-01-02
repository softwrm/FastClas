package com.versatile.fastclas.models;

import java.util.ArrayList;



public class DiscussionForumModel {

    public String user_name;
    public String question_id;
    public String session_id;
    public String question;
    public String post_on;
    public String number_of_answers;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getPost_on() {
        return post_on;
    }

    public void setPost_on(String post_on) {
        this.post_on = post_on;
    }

    public String getNumber_of_answers() {
        return number_of_answers;
    }

    public void setNumber_of_answers(String number_of_answers) {
        this.number_of_answers = number_of_answers;
    }
}
