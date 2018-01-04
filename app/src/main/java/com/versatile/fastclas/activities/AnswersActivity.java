package com.versatile.fastclas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.versatile.fastclas.BaseActivity;
import com.versatile.fastclas.adapters.AnswersAdapter;
import com.versatile.fastclas.interfaces.IParseListener;
import com.versatile.fastclas.models.AnswersModel;
import com.versatile.fastclas.utils.Constants;
import com.versatile.fastclas.utils.PopUtils;
import com.versatile.fastclas.utils.ServerResponse;
import com.versatile.fastclas.utils.Utility;
import com.versatilemobitech.fastclas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnswersActivity extends BaseActivity implements View.OnClickListener, IParseListener {

    RecyclerView recyclerViewAnswer;
    String session_id, question_id, user_name, post_on, question;
    TextView txtPost;
    EditText edtComments;
    ArrayList<AnswersModel> answersModelArrayList = new ArrayList<>();
    TextView textUserLetter, textName, textPostedTime, textQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        Intent intent = getIntent();
        question_id = intent.getStringExtra("question_id");
        session_id = intent.getStringExtra("session_id");
        user_name = intent.getStringExtra("user_name");
        post_on = intent.getStringExtra("post_on");
        question = intent.getStringExtra("question");


        initUI();
        callWebServiceForAnswers();

        final Handler handler = new Handler();
        final int delay = 15000; //milliseconds

        handler.postDelayed(new Runnable() {
            public void run() {
                //do something
                callWebServiceForAnswers();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }


    private void initUI() {

        recyclerViewAnswer = findViewById(R.id.recyclerViewAnswer);
        recyclerViewAnswer.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAnswer.setHasFixedSize(true);
        recyclerViewAnswer.setItemAnimator(new DefaultItemAnimator());

        textQuestion = findViewById(R.id.textQuestion);
        textUserLetter = findViewById(R.id.textUserLetter);
        textName = findViewById(R.id.textName);
        textPostedTime = findViewById(R.id.textPostedTime);

        txtPost = findViewById(R.id.txtPost);
        edtComments = findViewById(R.id.edtComments);

        txtPost.setOnClickListener(this);

        textQuestion.setText(question);
        textName.setText(user_name);
        textPostedTime.setText(post_on);
        Character character = user_name.charAt(0);
        textUserLetter.setText(character.toString());



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtPost: {
                callWebServiceToPostAnswer();
                break;
            }
        }
    }

    private void callWebServiceForAnswers() {
        if (PopUtils.checkInternetConnection(this)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("action", "getanswers");
                jsonObject.put("user_id", Utility.getSharedPreference(this, Constants.USER_ID));
                jsonObject.put("session_id", session_id);
                jsonObject.put("question_id", question_id);


                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.SERVICE_ANSWERS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            PopUtils.alertDialog(this, getString(R.string.pls_check_internet), null);
        }
    }

    private void callWebServiceToPostAnswer() {
        if (PopUtils.checkInternetConnection(this)) {
            if (!Utility.isValueNullOrEmpty(edtComments.getText().toString().trim())) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("action", "answer");
                    jsonObject.put("user_id", Utility.getSharedPreference(this, Constants.USER_ID));
                    jsonObject.put("session_id", session_id);
                    jsonObject.put("question_id", question_id);
                    jsonObject.put("answer", edtComments.getText().toString().trim());

                    showLoadingDialog("Loading...", false);

                    ServerResponse serverResponse = new ServerResponse();
                    serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.SERVICE_POSTANSWERS);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utility.setSnackBarEnglish(this, edtComments, "Enter Answer");
                edtComments.requestFocus();
            }
        } else {
            PopUtils.alertDialog(this, getString(R.string.pls_check_internet), null);
        }
    }

    @Override
    public void ErrorResponse(VolleyError volleyError, int requestCode) {
        if (requestCode == Constants.SERVICE_ANSWERS) {
            Utility.showLog("Error", "" + volleyError);
        } else if (requestCode == Constants.SERVICE_POSTANSWERS) {
            hideLoadingDialog();
        }

    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        if (requestCode == Constants.SERVICE_ANSWERS) {

            try {

                answersModelArrayList.clear();

                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");
                if (status.equals("200")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectData = jsonArray.getJSONObject(i);
                        String question_id = jsonObjectData.optString("question_id");
                        String user_name = jsonObjectData.optString("user_name");
                        String session_id = jsonObjectData.optString("session_id");
                        String answer = jsonObjectData.optString("answer");

                        AnswersModel answersModel = new AnswersModel();
                        answersModel.setAnswer(answer);
                        answersModel.setQuestion_id(question_id);
                        answersModel.setUser_name(user_name);
                        answersModel.setSession_id(session_id);

                        answersModelArrayList.add(answersModel);
                    }
                    setAdapterToRecyclerView();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (requestCode == Constants.SERVICE_POSTANSWERS) {
            hideLoadingDialog();
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");
                if (status.equals("200")) {

                    AnswersModel answersModel = new AnswersModel();
                    answersModel.setAnswer(edtComments.getText().toString().trim());
                    answersModel.setQuestion_id(question_id);
                    Utility.showLog("Name", "" + Utility.getSharedPreference(this, Constants.FNAME + " " + Constants.LNAME));
                    answersModel.setUser_name("Name");
                    answersModel.setSession_id(session_id);

                    answersModelArrayList.add(answersModel);

                    edtComments.setText("");
                    setAdapterToRecyclerView();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void setAdapterToRecyclerView() {
        AnswersAdapter answersAdapter = new AnswersAdapter(this, answersModelArrayList);
        recyclerViewAnswer.setAdapter(answersAdapter);
    }
}
