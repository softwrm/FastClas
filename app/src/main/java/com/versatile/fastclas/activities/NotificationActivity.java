package com.versatile.fastclas.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.versatile.fastclas.BaseActivity;
import com.versatile.fastclas.adapters.NotificationAdapter;
import com.versatile.fastclas.interfaces.IParseListener;
import com.versatile.fastclas.utils.Constants;
import com.versatile.fastclas.utils.PopUtils;
import com.versatile.fastclas.utils.ServerResponse;
import com.versatile.fastclas.utils.Utility;
import com.otelpt.fastclas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationActivity extends BaseActivity implements IParseListener, View.OnClickListener {
    RecyclerView recyclerViewNotification;
    ArrayList<String> messageArrayList = new ArrayList<>();
    TextView txtNoDataFound;
    ImageView imgBack;
    TextView txtToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initUI();
        callServiceForNotification();
    }

    private void initUI() {
        recyclerViewNotification = findViewById(R.id.recyclerViewNotification);
        recyclerViewNotification.setLayoutManager(new LinearLayoutManager(this));
        txtNoDataFound = findViewById(R.id.txtNoDataFound);
        imgBack = findViewById(R.id.imgBack);
        txtToolbar = findViewById(R.id.txtToolbar);

        txtToolbar.setText("Notifications");


        imgBack.setOnClickListener(this);
    }

    private void callServiceForNotification() {
        if (PopUtils.checkInternetConnection(this)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("action", "newnotification");
                jsonObject.put("user_id", "" + Utility.getSharedPreference(this, Constants.USER_ID));

                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.SERVICE_NOTIFICATION);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            PopUtils.alertDialog(this, getString(R.string.pls_check_internet), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    @Override
    public void ErrorResponse(VolleyError volleyError, int requestCode) {
        if (requestCode == Constants.SERVICE_NOTIFICATION) {
            Utility.showSettingDialog(this,
                    this.getResources().getString(R.string.some_thing_went_wrong),
                    this.getResources().getString(R.string.error), Constants.SERVER_ERROR).show();
        }
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        if (requestCode == Constants.SERVICE_NOTIFICATION) {
            try {
                messageArrayList.clear();
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("status").equals("200")) {

                    Utility.setSharedPrefStringData(this, Constants.NOTIFICATION_COUNT, "" + jsonObject.optInt("count"));

                    HomeActivity.viewNotificationIcon.setVisibility(View.GONE);

                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectData = jsonArray.getJSONObject(i);

                        String message = jsonObjectData.optString("message");
                        messageArrayList.add(message);
                    }
                    NotificationAdapter notificationAdapter = new NotificationAdapter(this, messageArrayList);
                    recyclerViewNotification.setAdapter(notificationAdapter);
                } else {
                    txtNoDataFound.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack: {
                finish();
                break;
            }
        }
    }
}
