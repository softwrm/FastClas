package com.versatile.fastclas.models;

import java.util.ArrayList;

/**
 * Created by USER on 14-12-2017.
 */

public class PackagesModel {
    public String packageId, packageName, description, amount, createdOn, updatedOn;
    public ArrayList<SubjectsModel> mSubjectsModel;

    public PackagesModel(String packageId, String packageName, String description, String amount, String createdOn,
                         String updatedOn, ArrayList<SubjectsModel> mSubjectsModel) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.description = description;
        this.amount = amount;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.mSubjectsModel = mSubjectsModel;
    }
}
