package com.versatile.fastclas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.versatile.fastclas.BaseActivity;
import com.versatilemobitech.fastclas.R;
import com.versatile.fastclas.adapters.SessionsAdapter;
import com.versatile.fastclas.interfaces.IParseListener;
import com.versatile.fastclas.models.SessionsModel;
import com.versatile.fastclas.utils.Constants;
import com.versatile.fastclas.utils.PopUtils;
import com.versatile.fastclas.utils.ServerResponse;
import com.versatile.fastclas.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SessionsActivity extends BaseActivity implements View.OnClickListener, SessionsAdapter.OnItemClickListener, IParseListener {

    private static final String TAG = SessionsActivity.class.getSimpleName();
    Toolbar toolbar;
    RecyclerView recyclerView;
    TextView txtToolbar, mTxtHeading, txtxNoDataFound;
    ImageView mImgBack;
    String label, heading, unitId;
    ArrayList<SessionsModel> sessionsModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_units);

        initViews();
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtToolbar = (TextView) findViewById(R.id.txtToolbar);
        mTxtHeading = (TextView) findViewById(R.id.txtHeading);
        txtxNoDataFound = (TextView) findViewById(R.id.txtxNoDataFound);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mImgBack = (ImageView) findViewById(R.id.imgBack);

        label = getIntent().getStringExtra("label");
        heading = getIntent().getStringExtra("heading");
        unitId = getIntent().getStringExtra("unitId");

        txtToolbar.setText(label);
        mTxtHeading.setText(heading);

        mImgBack.setOnClickListener(this);

        callWebServiceSession();
    }

    private void callWebServiceSession() {
        if (PopUtils.checkInternetConnection(this)) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("action", "session");
                jsonObject.put("unit_id", unitId);
                jsonObject.put("user_id", Utility.getSharedPreference(this, Constants.USER_ID));

                showLoadingDialog("Loading...", false);

                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.SERVICE_SESSION);
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
        }
    }

    @Override
    public void onItemClick(SessionsModel sessionPojo, int Position) {
        Intent intent = new Intent(this, SessionsInnerActivity.class);
        intent.putExtra("heading", sessionPojo.getSessionTitle());
        intent.putExtra("unit_id", unitId);
        intent.putExtra("session_id", sessionPojo.getSessionId());
        intent.putExtra("item_count", "" + sessionPojo.getItemCount());
        intent.putExtra("item_viewed",""+sessionPojo.getItemsViewed());
        navigateActivity(intent, false);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void ErrorResponse(VolleyError volleyError, int requestCode) {
        if (requestCode == Constants.SERVICE_SESSION) {
            hideLoadingDialog();
        }
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        if (requestCode == Constants.SERVICE_SESSION) {
            hideLoadingDialog();

            try {
                JSONObject jsonObject = new JSONObject(response);

                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");
                if (status.equals("200")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectData = jsonArray.getJSONObject(i);

                        String session_id = jsonObjectData.optString("session_id");
                        int items_viewed = jsonObjectData.optInt("items_viewed");
                        double item_count = jsonObjectData.optDouble("item_count");
                        String statusVal = jsonObjectData.optString("status");
                        String session_title = jsonObjectData.optString("session_title");
                        String session_number = jsonObjectData.optString("session_number");

                        double calci = item_count / 2;

                        SessionsModel sessionsModel = new SessionsModel();
                        sessionsModel.setItemCount("" + (int) (Math.ceil(calci)));
                        sessionsModel.setSessionId(session_id);
                        sessionsModel.setItemsViewed("" + items_viewed);
                        sessionsModel.setStatus(statusVal);
                        sessionsModel.setSessionTitle(session_title);
                        sessionsModel.setSessionNumber(session_number);

                        sessionsModelArrayList.add(sessionsModel);
                    }
                    SessionsAdapter sessionsAdapter = new SessionsAdapter(this, sessionsModelArrayList, this);
                    recyclerView.setAdapter(sessionsAdapter);
                } else {
                    txtxNoDataFound.setText("No Session's Found");
                    txtxNoDataFound.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
