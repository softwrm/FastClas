package com.versatile.fastclas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.versatile.fastclas.BaseActivity;
import com.versatile.fastclas.interfaces.IParseListener;
import com.versatile.fastclas.utils.Constants;
import com.versatile.fastclas.utils.PopUtils;
import com.versatile.fastclas.utils.ServerResponse;
import com.versatile.fastclas.utils.Utility;
import com.versatilemobitech.fastclas.R;
import com.versatile.fastclas.adapters.DiscussionForumAdapter;
import com.versatile.fastclas.models.DiscussionForumModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DiscussionForumActivity extends BaseActivity implements View.OnClickListener, IParseListener {

    private static final String TAG = DiscussionForumActivity.class.getSimpleName();
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView txtToolbar;
    private ImageView mImgBack;
    private String heading, sessionId;
    private Button btnAskQuestion;
    TextView txtNoDataFound;
    ArrayList<DiscussionForumModel> discussionForumModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_forum);

        Intent intent = getIntent();
        sessionId = intent.getStringExtra("SessionId");

        setReferences();
        callWebServiceForDiscussion();
    }

    private void callWebServiceForDiscussion() {
        if (PopUtils.checkInternetConnection(this)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("action", "discussion");
                jsonObject.put("user_id", Utility.getSharedPreference(this, Constants.USER_ID));
                jsonObject.put("session_id", sessionId);

                showLoadingDialog("Loading...", false);

                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.SERVICE_DISCUSSION);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            PopUtils.alertDialog(this, getString(R.string.pls_check_internet), null);
        }
    }


    private void setReferences() {
        toolbar = findViewById(R.id.toolbar);
        txtToolbar = findViewById(R.id.txtToolbar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        txtNoDataFound = findViewById(R.id.txtNoDataFound);
        mImgBack = findViewById(R.id.imgBack);
        btnAskQuestion = findViewById(R.id.btnAskQuestion);

        txtToolbar.setText("DISCUSSION FORUM");

        mImgBack.setOnClickListener(this);
        btnAskQuestion.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.btnAskQuestion:
                Intent intent = new Intent(this, AskQuestionActivity.class);
                intent.putExtra("sessionId", sessionId);
                startActivity(intent);
                finish();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void ErrorResponse(VolleyError volleyError, int requestCode) {
        if (requestCode == Constants.SERVICE_DISCUSSION) {
            hideLoadingDialog();
            Utility.showSettingDialog(this,
                    this.getResources().getString(R.string.some_thing_went_wrong),
                    this.getResources().getString(R.string.error), Constants.SERVER_ERROR).show();
        }
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        if (requestCode == Constants.SERVICE_DISCUSSION) {
            hideLoadingDialog();
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");
                if (status.equals("200")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectData = jsonArray.getJSONObject(i);
                        String question_id = jsonObjectData.optString("question_id");
                        String user_id = jsonObjectData.optString("user_id");
                        String user_name = jsonObjectData.optString("user_name");
                        String session_id = jsonObjectData.optString("session_id");
                        String question = jsonObjectData.optString("question");
                        String post_on = jsonObjectData.optString("post_on");
                        int answer_count = jsonObjectData.optInt("answer_count");

                        DiscussionForumModel discussionForumModel = new DiscussionForumModel();
                        if (answer_count == 1) {
                            discussionForumModel.setNumber_of_answers(answer_count + " Answer");
                        } else if (answer_count == 0) {
                            discussionForumModel.setNumber_of_answers("Answer");
                        } else {
                            discussionForumModel.setNumber_of_answers(answer_count + " Answers");
                        }
                        discussionForumModel.setQuestion_id(question_id);
                        discussionForumModel.setQuestion(question);
                        discussionForumModel.setUser_name(user_name);
                        discussionForumModel.setSession_id(session_id);
                        discussionForumModel.setPost_on(post_on);

                        discussionForumModelArrayList.add(discussionForumModel);
                    }
                    DiscussionForumAdapter discussionForumAdapter = new DiscussionForumAdapter(this, discussionForumModelArrayList);
                    recyclerView.setAdapter(discussionForumAdapter);
                } else {
                    txtNoDataFound.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
