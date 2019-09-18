package com.versatile.fastclas.models;

import java.util.ArrayList;

/**
 * Created by USER on 18-12-2017.
 */

public class UniversityModel {
    String university_id, university_name;
    ArrayList<CourseModel> courseModelArrayList;

    public ArrayList<CourseModel> getCourseModelArrayList() {
        return courseModelArrayList;
    }

    public void setCourseModelArrayList(ArrayList<CourseModel> courseModelArrayList) {
        this.courseModelArrayList = courseModelArrayList;
    }

    public String getUniversity_id() {
        return university_id;
    }

    public void setUniversity_id(String university_id) {
        this.university_id = university_id;
    }

    public String getUniversity_name() {
        return university_name;
    }

    public void setUniversity_name(String university_name) {
        this.university_name = university_name;
    }
}
