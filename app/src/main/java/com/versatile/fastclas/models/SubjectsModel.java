package com.versatile.fastclas.models;

/**
 * Created by Excentd11 on 4/16/2018.
 */

public class SubjectsModel {
    public String subjectId, subjectName, amount, description;

    public SubjectsModel(String subjectId, String subjectName, String amount, String description) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.amount = amount;
        this.description = description;
    }
}
