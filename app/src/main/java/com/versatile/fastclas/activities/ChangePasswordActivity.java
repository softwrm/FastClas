package com.versatile.fastclas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.versatile.fastclas.BaseActivity;
import com.otelpt.fastclas.R;
import com.versatile.fastclas.interfaces.IParseListener;
import com.versatile.fastclas.utils.Constants;
import com.versatile.fastclas.utils.PopUtils;
import com.versatile.fastclas.utils.ServerResponse;
import com.versatile.fastclas.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener, IParseListener {

    TextView mTitle;
    ImageView mImgBack;
    EditText mEdtOldpassword, mEdtedtNewpassword, mEdtConfirmpassword;
    Button mBtnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        HomeActivity.navigationView.getMenu().getItem(5).setChecked(false);

        initViews();
    }

    private void initViews() {
        setReferences();
        setClickListeners();
    }

    private void setReferences() {
        mEdtOldpassword =  findViewById(R.id.edtOldpassword);
        mEdtedtNewpassword =  findViewById(R.id.edtNewpassword);
        mEdtConfirmpassword =  findViewById(R.id.edtRenterpassword);
        mBtnSubmit =  findViewById(R.id.btnSubmit);
        mImgBack =  findViewById(R.id.imgBack);
        mTitle =  findViewById(R.id.txtToolbar);
        mTitle.setText("CHANGE PASSWORD");
    }

    private void setClickListeners() {
        mImgBack.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.btnSubmit:
                if (isValidated()) {
                    callServiceForChnagePassword();
                }
                break;
        }
    }

    private boolean isValidated() {
        boolean validated = false;
        if (Utility.isValueNullOrEmpty(mEdtOldpassword.getText().toString().trim())) {
            Utility.setSnackBarEnglish(ChangePasswordActivity.this, mEdtOldpassword, "Enter Current Password");
            mEdtOldpassword.requestFocus();
        } else if (Utility.isValueNullOrEmpty(mEdtedtNewpassword.getText().toString().trim())) {
            Utility.setSnackBarEnglish(ChangePasswordActivity.this, mEdtedtNewpassword, "Enter New Password");
            mEdtedtNewpassword.requestFocus();
        } else if (Utility.isValueNullOrEmpty(mEdtConfirmpassword.getText().toString().trim())) {
            Utility.setSnackBarEnglish(ChangePasswordActivity.this, mEdtConfirmpassword, "Enter New Confirm Password");
            mEdtConfirmpassword.requestFocus();
        } else if (!mEdtedtNewpassword.getText().toString().trim().equals(mEdtConfirmpassword.getText().toString().trim())) {
            Utility.setSnackBarEnglish(ChangePasswordActivity.this, mEdtConfirmpassword, "Passwords Mismatch");
            mEdtConfirmpassword.requestFocus();
        } else {
            validated = true;
        }
        return validated;
    }

    private void callServiceForChnagePassword() {
        if (PopUtils.checkInternetConnection(this)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("action", "changepassword");
                jsonObject.put("oldpassword", mEdtOldpassword.getText().toString().trim());
                jsonObject.put("newpassword", mEdtedtNewpassword.getText().toString().trim());
                jsonObject.put("userid", Utility.getSharedPreference(this, Constants.USER_ID));

                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.SERVICE_CHANGEPASSWORD);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            PopUtils.alertDialog(this, getString(R.string.pls_check_internet), null);
        }


    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void ErrorResponse(VolleyError volleyError, int requestCode) {
        if (requestCode == Constants.SERVICE_CHANGEPASSWORD) {
            hideLoadingDialog();
            Utility.showSettingDialog(this,
                    this.getResources().getString(R.string.some_thing_went_wrong),
                    this.getResources().getString(R.string.error), Constants.SERVER_ERROR).show();
        }
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        if (requestCode == Constants.SERVICE_CHANGEPASSWORD) {
            hideLoadingDialog();
            try {
                JSONObject jsonObject = new JSONObject(response);

                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");
                if (status.equals("200")) {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, HomeActivity.class));
                } else {
                    PopUtils.alertDialog(this, message, null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
