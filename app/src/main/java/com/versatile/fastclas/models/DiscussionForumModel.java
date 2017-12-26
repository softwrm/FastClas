package com.versatile.fastclas.models;

import java.util.ArrayList;



public class DiscussionForumModel {

    public String session_name;
    public String question_id;
    public String question;
    public String user_name;
    public String posted_time;
    public String answer_count;
    public ArrayList<String> user_name_arraylist;
    public ArrayList<String> answers_arraylist;

    public DiscussionForumModel(String session_name, String question_id, String question, String user_name, String posted_time, String answer_count,
                                ArrayList<String> user_name_arraylist, ArrayList<String> answers_arraylist) {
        this.session_name = session_name;
        this.question_id = question_id;
        this.question = question;
        this.user_name = user_name;
        this.posted_time = posted_time;
        this.answer_count = answer_count;
        this.user_name_arraylist = user_name_arraylist;
        this.answers_arraylist = answers_arraylist;
    }
}
