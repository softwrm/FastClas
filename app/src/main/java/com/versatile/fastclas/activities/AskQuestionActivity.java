package com.versatile.fastclas.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.versatilemobitech.fastclas.R;

public class AskQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mTxtTitle;
    Button mBtnSubmit;
    ImageView mImgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

        mTxtTitle = (TextView) findViewById(R.id.txtToolbar);
        mTxtTitle.setText("ASK A QUESTION");
        initComponents();
    }

    private void initComponents() {
        setReferences();
        setClickListeners();
    }


    private void setReferences() {
        mBtnSubmit = (Button) findViewById(R.id.btnSubmit);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
    }

    private void setClickListeners() {
        mBtnSubmit.setOnClickListener(this);
        mImgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.btnSubmit:
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
    }
}
