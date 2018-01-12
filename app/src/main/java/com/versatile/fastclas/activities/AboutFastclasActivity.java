package com.versatile.fastclas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.versatile.fastclas.BaseActivity;
import com.versatilemobitech.fastclas.R;

public class AboutFastclasActivity extends BaseActivity implements View.OnClickListener {

    TextView mTitle, mTxtAbout;
    ImageView mImgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_fastclas);
        setReferences();
        HomeActivity.navigationView.getMenu().getItem(1).setChecked(false);
    }

    private void setReferences() {
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mTitle = (TextView) findViewById(R.id.txtToolbar);
        mTxtAbout = (TextView) findViewById(R.id.txtAbout);
        mTitle.setText("ABOUT FASTCLAS");


        mImgBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
