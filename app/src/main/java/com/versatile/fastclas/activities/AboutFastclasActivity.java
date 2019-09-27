package com.versatile.fastclas.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.versatile.fastclas.BaseActivity;
import com.versatile.fastclas.interfaces.IParseListener;
import com.versatile.fastclas.utils.Constants;
import com.versatile.fastclas.utils.PopUtils;
import com.versatile.fastclas.utils.ServerResponse;
import com.otelpt.fastclas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AboutFastclasActivity extends BaseActivity implements View.OnClickListener, IParseListener {
    private TextView mTitle, mTxtAbout;
    private ImageView mImgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_fastclas);

        setReferences();
        HomeActivity.navigationView.getMenu().getItem(4).setChecked(false);

        if (PopUtils.checkInternetConnection(this)) {
            requestingForAboutUsWS();
        } else {
            PopUtils.alertDialog(this, getString(R.string.internet_connection), null);
        }
    }

    private void setReferences() {
        mImgBack = findViewById(R.id.imgBack);
        mTitle = findViewById(R.id.txtToolbar);
        mTxtAbout = findViewById(R.id.txtAbout);
        mTitle.setText("ABOUT FASTCLAS");
        mImgBack.setOnClickListener(this);
    }

    private void requestingForAboutUsWS() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "about_us");
            showLoadingDialog("Loading...", false);
            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.SERVICE_ABOUTUS);
        } catch (JSONException e) {
            e.printStackTrace();
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
    public void onBackPressed() {
        finish();
    }

    @Override
    public void ErrorResponse(VolleyError volleyError, int requestCode) {
        hideLoadingDialog();
        PopUtils.alertDialog(this, "Please Check Internet Connection", null);
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        hideLoadingDialog();
        switch (requestCode) {
            case Constants.SERVICE_ABOUTUS:
                responseForAboutUs(response);
                break;
            default:
                break;
        }
    }

    private void responseForAboutUs(String response) {
        if (response != null) {
            try {
                JSONObject mJsonObject = new JSONObject(response);
                String message = mJsonObject.optString("message");
                if (mJsonObject.optString("status").equalsIgnoreCase("200")) {
                    JSONArray mDataArray = mJsonObject.getJSONArray("data");

                    JSONObject mDataObject = mDataArray.getJSONObject(0);
                    mTxtAbout.setText(mDataObject.optString("about_us"));
                } else {
                    mTxtAbout.setText("About us content is updating...");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
