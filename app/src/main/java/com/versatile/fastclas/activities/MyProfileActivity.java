package com.versatile.fastclas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.versatile.fastclas.BaseActivity;
import com.versatilemobitech.fastclas.R;
import com.versatile.fastclas.utils.Constants;
import com.versatile.fastclas.utils.Utility;

public class MyProfileActivity extends BaseActivity implements View.OnClickListener {

    TextView mTitle, mTxtProfilename,
            mTxtProfileno, mTxtProfileemail, mTxtUsername, mTxtUsergender,
            mTxtUseremail, mTxtUserphoneno, mTxtAddress;
    ImageView mImgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        HomeActivity.navigationView.getMenu().getItem(0).setChecked(false);

        initViews();
    }

    private void initViews() {
        setReferences();
        setClickListeners();
    }

    private void setReferences() {

        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mTitle = (TextView) findViewById(R.id.txtToolbar);
        mTitle.setText("MY PROFILE");
        mTxtProfilename = (TextView) findViewById(R.id.txtProfilename);
        mTxtProfileno = (TextView) findViewById(R.id.txtProfileno);
        mTxtProfileemail = (TextView) findViewById(R.id.txtProfileemail);
        mTxtUsername = (TextView) findViewById(R.id.txtUsername);
        mTxtUsergender = (TextView) findViewById(R.id.txtUsergender);
        mTxtUseremail = (TextView) findViewById(R.id.txtUseremail);
        mTxtUserphoneno = (TextView) findViewById(R.id.txtUserphoneno);
        mTxtAddress = (TextView) findViewById(R.id.txtAddress);

        mTxtProfilename.setText(Utility.getSharedPreference(this, Constants.FNAME) + " " + Utility.getSharedPreference(this, Constants.LNAME));
        mTxtProfileno.setText(Utility.getSharedPreference(this, Constants.MOBILE));
        mTxtProfileemail.setText(Utility.getSharedPreference(this, Constants.EMAIL));
        mTxtUseremail.setText(Utility.getSharedPreference(this, Constants.EMAIL));
        mTxtUserphoneno.setText(Utility.getSharedPreference(this, Constants.MOBILE));

        mTxtUsername.setText(Utility.getSharedPreference(this,Constants.FNAME) + " " + Utility.getSharedPreference(this, Constants.LNAME));
        mTxtUsergender.setText(Utility.getSharedPreference(this,Constants.GENDER));

        mTxtAddress.setText(Utility.getSharedPreference(this, Constants.SEMESTER) + "\n" +
                Utility.getSharedPreference(this, Constants.COURSE) + "\n" +
                Utility.getSharedPreference(this, Constants.UNIVERSITY) + "\n" +
                Utility.getSharedPreference(this, Constants.STATE));
    }

    private void setClickListeners() {
        mImgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
//        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
//        navigateActivityBack(new Intent(this, HomeActivity.class), false);
    }
}

