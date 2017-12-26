package com.versatile.fastclas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.versatile.fastclas.BaseActivity;
import com.versatile.fastclas.R;

public class AboutFastclasActivity extends BaseActivity implements View.OnClickListener {

    TextView mTitle, mTxtAbout;
    ImageView mImgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_fastclas);
        initViews();
    }

    private void initViews() {
        setReferences();
        setClickListeners();
    }

    private void setReferences() {
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mTitle = (TextView) findViewById(R.id.txtToolbar);
        mTxtAbout = (TextView) findViewById(R.id.txtAbout);
        mTitle.setText("ABOUT FASTCLAS");
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
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
//        navigateActivityBack(new Intent(this, HomeActivity.class), false);
    }
}
