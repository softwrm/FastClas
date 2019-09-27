package com.versatile.fastclas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.versatile.fastclas.BaseActivity;
import com.versatile.fastclas.interfaces.IParseListener;
import com.versatile.fastclas.utils.Constants;
import com.versatile.fastclas.utils.PopUtils;
import com.versatile.fastclas.utils.ServerResponse;
import com.versatile.fastclas.utils.Utility;
import com.otelpt.fastclas.R;

import org.json.JSONException;
import org.json.JSONObject;

public class HelpActivity extends BaseActivity implements View.OnClickListener, IParseListener {

    TextView mTitle;
    ImageView mImgBack;
    EditText edtName, edtPhoneNumber, edtDescription;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        HomeActivity.navigationView.getMenu().getItem(6).setChecked(false);
        initViews();
    }

    private void initViews() {
        setReferences();
        setClickListeners();
    }

    private void setReferences() {

        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mTitle = (TextView) findViewById(R.id.txtToolbar);

        edtName = (EditText) findViewById(R.id.edtName);
        edtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
        edtDescription = (EditText) findViewById(R.id.edtDescription);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        mTitle.setText("HELP");
    }

    private void setClickListeners() {
        btnSubmit.setOnClickListener(this);
        mImgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit: {
                callServiceForHelp();
                break;
            }
            case R.id.imgBack:
                onBackPressed();
                break;
        }
    }

    private void callServiceForHelp() {
        if (validated()) {
            if (PopUtils.checkInternetConnection(this)) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("action", "help");
                    jsonObject.put("user_name", edtName.getText().toString().trim());
                    jsonObject.put("mobile_number", edtPhoneNumber.getText().toString().trim());
                    jsonObject.put("message", edtDescription.getText().toString().trim());

                    showLoadingDialog("Loading...", false);

                    ServerResponse serverResponse = new ServerResponse();
                    serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.SERVICE_MESSAGE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                PopUtils.alertDialog(this, getString(R.string.pls_check_internet), null);
            }
        }
    }

    private boolean validated() {
        boolean validation = false;
        if (Utility.isValueNullOrEmpty(edtName.getText().toString().trim())) {
            Utility.setSnackBarEnglish(HelpActivity.this, edtName, "Enter Name");
            edtName.requestFocus();
        } else if (Utility.isValueNullOrEmpty(edtPhoneNumber.getText().toString().trim())) {
            Utility.setSnackBarEnglish(HelpActivity.this, edtPhoneNumber, "Enter Phone Number");
            edtPhoneNumber.requestFocus();
        } else if (edtPhoneNumber.getText().toString().trim().length() != 10) {
            Utility.setSnackBarEnglish(HelpActivity.this, edtPhoneNumber, "Enter Valid Phone Number");
            edtPhoneNumber.requestFocus();
        } else if (Utility.isValueNullOrEmpty(edtDescription.getText().toString().trim())) {
            Utility.setSnackBarEnglish(HelpActivity.this, edtDescription, "Enter Message");
            edtDescription.requestFocus();
        } else {
            validation = true;
        }
        return validation;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void ErrorResponse(VolleyError volleyError, int requestCode) {
        if (requestCode == Constants.SERVICE_MESSAGE) {
            hideLoadingDialog();
            PopUtils.alertDialog(this, "Please Check Internet Connection", null);
        }
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        if (requestCode == Constants.SERVICE_MESSAGE) {
            hideLoadingDialog();
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");
                PopUtils.alertDialog(HelpActivity.this, message, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(HelpActivity.this, HomeActivity.class));
                        finish();
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
