package com.versatile.fastclas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.versatile.fastclas.BaseActivity;
import com.versatile.fastclas.interfaces.IParseListener;
import com.versatile.fastclas.utils.Constants;
import com.versatile.fastclas.utils.PopUtils;
import com.versatile.fastclas.utils.ServerResponse;
import com.versatile.fastclas.utils.Utility;
import com.versatilemobitech.fastclas.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AskQuestionActivity extends BaseActivity implements View.OnClickListener, IParseListener {

    TextView mTxtTitle;
    Button mBtnSubmit;
    ImageView mImgBack;
    String sessionId;
    EditText edtQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

        Intent intent = getIntent();
        sessionId = intent.getStringExtra("sessionId");

        mTxtTitle = findViewById(R.id.txtToolbar);
        edtQuestion = findViewById(R.id.edtQuestion);

        mTxtTitle.setText("ASK A QUESTION");
        setReferences();

    }

    private void callServiceForAskingQuestion() {
        if (PopUtils.checkInternetConnection(this)) {
            if (!Utility.isValueNullOrEmpty(edtQuestion.getText().toString().trim())) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("action", "questionpost");
                    jsonObject.put("user_id", Utility.getSharedPreference(this, Constants.USER_ID));
                    jsonObject.put("session_id", sessionId);
                    jsonObject.put("question", edtQuestion.getText().toString().trim());
                    showLoadingDialog("Loading...", false);

                    ServerResponse serverResponse = new ServerResponse();
                    serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.SERVICE_QUESTION);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utility.setSnackBarEnglish(this, edtQuestion, "Enter Question");
                edtQuestion.requestFocus();
            }
        } else {
            PopUtils.alertDialog(this, getString(R.string.pls_check_internet), null);
        }

    }


    private void setReferences() {
        mBtnSubmit = findViewById(R.id.btnSubmit);
        mImgBack = findViewById(R.id.imgBack);

        mBtnSubmit.setOnClickListener(this);
        mImgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.btnSubmit:
                callServiceForAskingQuestion();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, DiscussionForumActivity.class);
        intent.putExtra("SessionId", sessionId);
        startActivity(intent);
        finish();
    }

    @Override
    public void ErrorResponse(VolleyError volleyError, int requestCode) {
        if (requestCode == Constants.SERVICE_QUESTION) {
            hideLoadingDialog();
            Utility.showSettingDialog(this,
                    this.getResources().getString(R.string.some_thing_went_wrong),
                    this.getResources().getString(R.string.error), Constants.SERVER_ERROR).show();
        }
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        if (requestCode == Constants.SERVICE_QUESTION) {
            hideLoadingDialog();
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");
                if (status.equals("200")) {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, DiscussionForumActivity.class);
                    intent.putExtra("SessionId", sessionId);
                    startActivity(intent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
