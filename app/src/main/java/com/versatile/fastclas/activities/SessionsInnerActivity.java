package com.versatile.fastclas.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.versatile.fastclas.BaseActivity;
import com.versatile.fastclas.R;
import com.versatile.fastclas.adapters.SessionsInnerAdapter;
import com.versatile.fastclas.interfaces.IParseListener;
import com.versatile.fastclas.models.SessionsInnerModel;
import com.versatile.fastclas.utils.Constants;
import com.versatile.fastclas.utils.PopUtils;
import com.versatile.fastclas.utils.ServerResponse;
import com.versatile.fastclas.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SessionsInnerActivity extends BaseActivity implements View.OnClickListener,
        SessionsInnerAdapter.OnItemClickListener, IParseListener {

    private static final String TAG = SessionsActivity.class.getSimpleName();
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView txtToolbar, mTxtHeading;
    private ImageView mImgBack;
    private ImageButton mImgOpenDialog;
    private String heading, unitId, sessionId;
    ArrayList<SessionsInnerModel> sessionsInnerModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessions_inner);

        initViews();
    }

    private void initViews() {
        setReferences();
        setClickListeners();
    }

    private void setReferences() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtToolbar = (TextView) findViewById(R.id.txtToolbar);
        mTxtHeading = (TextView) findViewById(R.id.txtHeading);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mImgOpenDialog = (ImageButton) findViewById(R.id.imgOpenDialog);

        heading = getIntent().getStringExtra("heading");
        unitId = getIntent().getStringExtra("unit_id");
        sessionId = getIntent().getStringExtra("session_id");
        txtToolbar.setText(heading);

        callWebServiceForItems();
    }


    private void setClickListeners() {
        mImgBack.setOnClickListener(this);
        mImgOpenDialog.setOnClickListener(this);
    }

    private void callWebServiceForItems() {

        if (PopUtils.checkInternetConnection(this)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("action", "items");
                jsonObject.put("user_id", Utility.getSharedPreference(this, Constants.USER_ID));
                jsonObject.put("unit_id", unitId);
                jsonObject.put("session_id", sessionId);

                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.SERVICE_ITEM);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            PopUtils.alertDialog(this, getString(R.string.pls_check_internet), null);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.imgOpenDialog:
                openDialog();
                break;
            default:
                break;
        }
    }

    private void openDialog() {
        final Dialog dialogShare = new Dialog(this);
        dialogShare.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialogShare.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogShare.setContentView(R.layout.dialog_sessions);
        dialogShare.getWindow().setGravity(Gravity.BOTTOM);
        dialogShare.setCanceledOnTouchOutside(true);
        dialogShare.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialogShare.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogShare.show();

        TextView textDiscussionForum = (TextView) dialogShare.findViewById(R.id.textDiscussionForum);
//        TextView textAskQuestion = (TextView) dialogShare.findViewById(R.id.textAskQuestion);
        TextView textCancel = (TextView) dialogShare.findViewById(R.id.textCancel);

        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogShare.dismiss();
            }
        });

        textDiscussionForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SessionsInnerActivity.this, DiscussionForumActivity.class);
                navigateActivity(intent, false);
                dialogShare.dismiss();
            }
        });

        /*textAskQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SessionsInnerActivity.this, AskQuestionActivity.class);
                navigateActivity(intent, false);
                dialogShare.dismiss();
            }
        });*/
    }

    @Override
    public void onItemClick(SessionsInnerModel sessionPojo, int Position) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_readmore, null);
        alertDialog.setView(view);
        alertDialog.setCancelable(true);
        //alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationexit;

        TextView txtDescription = (TextView) view.findViewById(R.id.txtDescription);
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void ErrorResponse(VolleyError volleyError, int requestCode) {
        if (requestCode == Constants.SERVICE_ITEM) {
            hideLoadingDialog();
        }
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        if (requestCode == Constants.SERVICE_ITEM) {
            hideLoadingDialog();

            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");
                if (status.equals("200")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("Data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectData = jsonArray.getJSONObject(i);
                        String item_id = jsonObjectData.optString("item_id");
                        String item_title = jsonObjectData.optString("item_title");
                        String video = jsonObjectData.optString("video");
                        String description = jsonObjectData.optString("description");
                        String statusVal = jsonObjectData.optString("status");
                        int video_status = jsonObjectData.optInt("video_status");

                        SessionsInnerModel sessionsInnerModel = new SessionsInnerModel();
                        sessionsInnerModel.setItemId(item_id);
                        sessionsInnerModel.setItemTitle(item_title);
                        sessionsInnerModel.setVideo(video);
                        sessionsInnerModel.setDescription(description);
                        sessionsInnerModel.setStatus(statusVal);
                        sessionsInnerModel.setVideoStatus("" + video_status);
                        sessionsInnerModelArrayList.add(sessionsInnerModel);
                    }
                    SessionsInnerAdapter sessionsInnerAdapter = new SessionsInnerAdapter(this, sessionsInnerModelArrayList, this);
                    recyclerView.setAdapter(sessionsInnerAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
