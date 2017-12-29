package com.versatile.fastclas.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.versatile.fastclas.BaseActivity;
import com.versatile.fastclas.adapters.SessionsInnerAdapter;
import com.versatile.fastclas.interfaces.IParseListener;
import com.versatile.fastclas.models.SessionsInnerModel;
import com.versatile.fastclas.utils.Constants;
import com.versatile.fastclas.utils.PopUtils;
import com.versatile.fastclas.utils.ServerResponse;
import com.versatile.fastclas.utils.Utility;
import com.versatilemobitech.fastclas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SessionsInnerActivity extends BaseActivity implements View.OnClickListener,
        SessionsInnerAdapter.OnItemClickListener, IParseListener {
    private static final String TAG = SessionsActivity.class.getSimpleName();
    private Toolbar toolbar;
    TextView textTitle1, textTitle2, textTitle3, textTitle4, textTitle5, textTitle6, textTitle7, textTitle8, textTitle9, textTitle10;
    TextView textStatus, textStatus2;
    private TextView txtToolbar;
    private String heading, unitId, sessionId;
    ArrayList<SessionsInnerModel> sessionsInnerModelArrayList = new ArrayList<>();
    ImageView imgYoutube, imgYoutube2;
    ImageView imgButton, imgButton2;
    ScrollView scrollView;
    TextView txtxNoDataFound;
    RelativeLayout rl;
    ImageView imgOpenDialog;
    TextView txtNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessions_inner);

        setReferences();

    }

    private void setReferences() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtToolbar = (TextView) findViewById(R.id.txtToolbar);
        imgYoutube = (ImageView) findViewById(R.id.imgYoutube);
        imgYoutube2 = (ImageView) findViewById(R.id.imgYoutube2);
        imgButton = (ImageView) findViewById(R.id.imgButton);
        imgButton2 = (ImageView) findViewById(R.id.imgButton2);
        imgOpenDialog = (ImageView) findViewById(R.id.imgOpenDialog);
        textTitle1 = (TextView) findViewById(R.id.textTitle1);
        textTitle2 = (TextView) findViewById(R.id.textTitle2);
        textTitle3 = (TextView) findViewById(R.id.textTitle3);
        textTitle4 = (TextView) findViewById(R.id.textTitle4);
        textTitle5 = (TextView) findViewById(R.id.textTitle5);
        textTitle6 = (TextView) findViewById(R.id.textTitle6);
        textTitle7 = (TextView) findViewById(R.id.textTitle7);
        textTitle8 = (TextView) findViewById(R.id.textTitle8);
        textTitle9 = (TextView) findViewById(R.id.textTitle9);
        textTitle10 = (TextView) findViewById(R.id.textTitle10);

        textStatus = (TextView) findViewById(R.id.textStatus);
        textStatus2 = (TextView) findViewById(R.id.textStatus2);

        txtxNoDataFound = (TextView) findViewById(R.id.txtxNoDataFound);

        txtNext = (TextView) findViewById(R.id.txtNext);

        rl = (RelativeLayout) findViewById(R.id.rl);

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        heading = getIntent().getStringExtra("heading");
        unitId = getIntent().getStringExtra("unit_id");
        sessionId = getIntent().getStringExtra("session_id");
        txtToolbar.setText(heading);

        imgOpenDialog.setOnClickListener(this);
        imgButton.setOnClickListener(this);
        imgButton2.setOnClickListener(this);

        callWebServiceForItems();
    }


    private void callWebServiceForItems() {

        if (PopUtils.checkInternetConnection(this)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("action", "items");
                jsonObject.put("user_id", Utility.getSharedPreference(this, Constants.USER_ID));
                jsonObject.put("unit_id", unitId);
                jsonObject.put("session_id", sessionId);
                jsonObject.put("page", "1");
                jsonObject.put("no_of_records", "2");

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
            case R.id.imgButton: {
                Intent intent = new Intent(this, YoutubeVideoPlayer.class);
                intent.putExtra("video_id_1", sessionsInnerModelArrayList.get(0).youtubeId);
                startActivityForResult(intent, 2);

                break;
            }
            case R.id.imgButton2: {
                Intent intent = new Intent(this, YoutubeVideoPlayer.class);
                intent.putExtra("video_id_2", sessionsInnerModelArrayList.get(1).youtubeId);
                startActivityForResult(intent, 4);
                break;
            }
        }
    }

    private void callWebServiceVideo1(String item_id) {
        if (PopUtils.checkInternetConnection(this)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("action","video");
                jsonObject.put("user_id", "" + Utility.getSharedPreference(this, Constants.USER_ID));
                jsonObject.put("session_id", "" + sessionId);
                jsonObject.put("item_id", "" +item_id);

                Utility.showLoadingDialog(this, "Loading...", false);
                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.SERVICE_VIDEO_WATECHED_1);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2) {
            textStatus.setVisibility(View.VISIBLE);


            callWebServiceVideo1(sessionsInnerModelArrayList.get(0).getItemId());

            if ((textStatus.getVisibility() == View.VISIBLE) && (textStatus2.getVisibility() == View.VISIBLE)) {
                txtNext.setTextColor(getResources().getColor(R.color.color_blue));
                txtNext.setEnabled(true);
                txtNext.setDrawingCacheBackgroundColor(getResources().getColor(R.color.color_blue));

            }
        } else if (requestCode == 4) {
            textStatus2.setVisibility(View.VISIBLE);

            callWebServiceVideo1(sessionsInnerModelArrayList.get(1).getItemId());

            if ((textStatus.getVisibility() == View.VISIBLE) && (textStatus2.getVisibility() == View.VISIBLE)) {
                txtNext.setTextColor(getResources().getColor(R.color.color_blue));
                txtNext.setEnabled(true);
                txtNext.setDrawingCacheBackgroundColor(getResources().getColor(R.color.color_blue));

            }

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
                startActivity(intent);
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
            Utility.hideLoadingDialog();
        } else if (requestCode == Constants.SERVICE_VIDEO_WATECHED_1) {
            Utility.hideLoadingDialog();
        }
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        if (requestCode == Constants.SERVICE_ITEM) {
            Utility.hideLoadingDialog();

            try {

                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");
                if (status.equals("200")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectData = jsonArray.getJSONObject(i);
                        String item_id = jsonObjectData.optString("item_id");
                        String item_title1 = jsonObjectData.optString("item_title1");
                        String item_title2 = jsonObjectData.optString("item_title2");
                        String item_title3 = jsonObjectData.optString("item_title3");
                        String item_title4 = jsonObjectData.optString("item_title4");
                        String item_title5 = jsonObjectData.optString("item_title5");
                        String video = jsonObjectData.optString("video");
                        String youtube_id = jsonObjectData.optString("youtube_id");
                        String thumbnail_image = jsonObjectData.optString("thumbnail_image");
                        String description = jsonObjectData.optString("description");
                        String statusVal = jsonObjectData.optString("status");
                        int video_status = jsonObjectData.optInt("video_status");

                        SessionsInnerModel sessionsInnerModel = new SessionsInnerModel();
                        sessionsInnerModel.setItemId(item_id);
                        sessionsInnerModel.setItemTitle1(item_title1);
                        sessionsInnerModel.setItemTitle2(item_title2);
                        sessionsInnerModel.setItemTitle3(item_title3);
                        sessionsInnerModel.setItemTitle4(item_title4);
                        sessionsInnerModel.setItemTitle5(item_title5);
                        sessionsInnerModel.setVideo(video);
                        sessionsInnerModel.setDescription(description);
                        sessionsInnerModel.setStatus(statusVal);
                        sessionsInnerModel.setVideoStatus("" + video_status);
                        sessionsInnerModel.setThumbnailImage(thumbnail_image);
                        sessionsInnerModel.setYoutubeId(youtube_id);
                        sessionsInnerModelArrayList.add(sessionsInnerModel);
                    }
/*
                    textTitle1.setText(sessionsInnerModelArrayList.get(0).getItemTitle1());
                    textTitle2.setText(sessionsInnerModelArrayList.get(0).getItemTitle2());
                    textTitle3.setText(sessionsInnerModelArrayList.get(0).getItemTitle3());
                    textTitle4.setText(sessionsInnerModelArrayList.get(0).getItemTitle4());
                    textTitle5.setText(sessionsInnerModelArrayList.get(0).getItemTitle5());
                    Picasso.with(this)
                            .load(sessionsInnerModelArrayList.get(0).getThumbnailImage())
                            .placeholder(R.drawable.video_image1)
                            .into(imgYoutube);

                    textTitle6.setText(sessionsInnerModelArrayList.get(1).getItemTitle1());
                    textTitle7.setText(sessionsInnerModelArrayList.get(1).getItemTitle2());
                    textTitle8.setText(sessionsInnerModelArrayList.get(1).getItemTitle3());
                    textTitle9.setText(sessionsInnerModelArrayList.get(1).getItemTitle4());
                    textTitle10.setText(sessionsInnerModelArrayList.get(1).getItemTitle5());

                    Picasso.with(this)
                            .load(sessionsInnerModelArrayList.get(1).getThumbnailImage())
                            .placeholder(R.drawable.video_image1)
                            .into(imgYoutube2);

                    if (sessionsInnerModelArrayList.get(0).getVideoStatus().equals("0")) {
                        textStatus.setVisibility(View.GONE);
                    } else {
                        textStatus.setVisibility(View.VISIBLE);
                    }
                    if (sessionsInnerModelArrayList.get(1).getVideoStatus().equals("0")) {
                        textStatus2.setVisibility(View.GONE);
                    } else {
                        textStatus2.setVisibility(View.VISIBLE);
                    }*/
                    setData();
                } else {
                    scrollView.setVisibility(View.GONE);
                    txtxNoDataFound.setVisibility(View.VISIBLE);
                    rl.setVisibility(View.GONE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (requestCode == Constants.SERVICE_VIDEO_WATECHED_1) {
            Utility.hideLoadingDialog();

            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");
                if (status.equals("200")) {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setData() {

        textTitle1.setText(sessionsInnerModelArrayList.get(0).getItemTitle1());
        textTitle2.setText(sessionsInnerModelArrayList.get(0).getItemTitle2());
        textTitle3.setText(sessionsInnerModelArrayList.get(0).getItemTitle3());
        textTitle4.setText(sessionsInnerModelArrayList.get(0).getItemTitle4());
        textTitle5.setText(sessionsInnerModelArrayList.get(0).getItemTitle5());
        Picasso.with(this)
                .load(sessionsInnerModelArrayList.get(0).getThumbnailImage())
                .placeholder(R.drawable.video_image1)
                .into(imgYoutube);

        textTitle6.setText(sessionsInnerModelArrayList.get(1).getItemTitle1());
        textTitle7.setText(sessionsInnerModelArrayList.get(1).getItemTitle2());
        textTitle8.setText(sessionsInnerModelArrayList.get(1).getItemTitle3());
        textTitle9.setText(sessionsInnerModelArrayList.get(1).getItemTitle4());
        textTitle10.setText(sessionsInnerModelArrayList.get(1).getItemTitle5());

        Picasso.with(this)
                .load(sessionsInnerModelArrayList.get(1).getThumbnailImage())
                .placeholder(R.drawable.video_image1)
                .into(imgYoutube2);

        if (sessionsInnerModelArrayList.get(0).getVideoStatus().equals("0")) {
            textStatus.setVisibility(View.GONE);
        } else {
            textStatus.setVisibility(View.VISIBLE);
        }
        if (sessionsInnerModelArrayList.get(1).getVideoStatus().equals("0")) {
            textStatus2.setVisibility(View.GONE);
        } else {
            textStatus2.setVisibility(View.VISIBLE);
        }
    }
}
