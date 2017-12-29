package com.versatile.fastclas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.versatile.fastclas.BaseActivity;
import com.versatilemobitech.fastclas.R;
import com.versatile.fastclas.interfaces.IParseListener;
import com.versatile.fastclas.utils.Constants;
import com.versatile.fastclas.utils.PopUtils;
import com.versatile.fastclas.utils.ServerResponse;
import com.versatile.fastclas.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity implements View.OnClickListener, IParseListener {

    EditText mEdtUseremail, mEdtPassword;
    TextView mTxtForgotpassword;
    Button mBtnLogin, mBtnRegister;
    String device_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponents();
    }

    private void initComponents() {
        setReferences();
        setClickListeners();

        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

    }

    private void setReferences() {
        mEdtUseremail = (EditText) findViewById(R.id.edtUseremail);
        mEdtPassword = (EditText) findViewById(R.id.edtUserpassword);
        mTxtForgotpassword = (TextView) findViewById(R.id.txtForgotpassword);
        mBtnLogin = (Button) findViewById(R.id.btnLogin);
        mBtnRegister = (Button) findViewById(R.id.btnRegister);

    }

    private void setClickListeners() {
        mTxtForgotpassword.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtForgotpassword:
                PopUtils.forgotPasswordDialog(this, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (PopUtils.checkInternetConnection(LoginActivity.this)) {
                            requestForForgotPasswordWS(view.getTag() + "");
                        } else {
                            PopUtils.alertDialog(LoginActivity.this, getString(R.string.pls_check_internet), null);
                        }
                    }
                });
                break;
            case R.id.btnLogin: {
                if (PopUtils.checkInternetConnection(LoginActivity.this)) {
                    if (isValidated()) {
                        callServiceForLogin();
                    }
                } else {
                    PopUtils.alertDialog(LoginActivity.this, getString(R.string.pls_check_internet), null);
                }
                break;
            }
            case R.id.btnRegister:
                Intent register = new Intent(this, RegisterActivity.class);
                navigateActivity(register, false);
                break;
        }
    }

    private boolean isValidated() {
        boolean validated = false;
        if (Utility.isValueNullOrEmpty(mEdtUseremail.getText().toString().trim())) {
            Utility.setSnackBarEnglish(LoginActivity.this, mEdtUseremail, "Enter Email ID");
            mEdtUseremail.requestFocus();
        } else if (!mEdtUseremail.getText().toString().trim().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z]+)*(\\.[A-Za-z]{2,})$")) {
            Utility.setSnackBarEnglish(LoginActivity.this, mEdtUseremail, "Enter Valid Email ID");
            mEdtUseremail.requestFocus();
        } else if (Utility.isValueNullOrEmpty(mEdtPassword.getText().toString().trim())) {
            Utility.setSnackBarEnglish(LoginActivity.this, mEdtPassword, "Enter Password");
            mEdtPassword.requestFocus();
        } else {
            validated = true;
        }
        return validated;
    }

    private void callServiceForLogin() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "login");
            jsonObject.put("email", mEdtUseremail.getText().toString().trim());
            jsonObject.put("password", mEdtPassword.getText().toString().trim());
            jsonObject.put("device_id", device_id);

            showLoadingDialog("Loading...", false);

            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.SERVICE_LOGIN);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestForForgotPasswordWS(String email) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "forgotpassword");
            jsonObject.put("email", email);

            showLoadingDialog("Loading...", false);

            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.SERVICE_FORGOTPASSWORD);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ErrorResponse(VolleyError volleyError, int requestCode) {
        if (requestCode == Constants.SERVICE_FORGOTPASSWORD) {
            hideLoadingDialog();
        } else if (requestCode == Constants.SERVICE_LOGIN) {
            hideLoadingDialog();
        }
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        if (requestCode == Constants.SERVICE_FORGOTPASSWORD) {
            hideLoadingDialog();
            try {
                JSONObject jsonObject = new JSONObject(response);

                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");
                PopUtils.alertDialog(this, message, null);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (requestCode == Constants.SERVICE_LOGIN) {
            hideLoadingDialog();

            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");

                if (status.equals("200")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONObject jsonObjectData = jsonArray.getJSONObject(0);
                    String fname = jsonObjectData.optString("fname");
                    String lname = jsonObjectData.optString("lname");
                    String gender = jsonObjectData.optString("gender");
                    String mobile = jsonObjectData.optString("mobile");
                    String email = jsonObjectData.optString("email");
                    String state_id = jsonObjectData.optString("state_id");
                    String state = jsonObjectData.optString("state");
                    String university_id = jsonObjectData.optString("university_id");
                    String university = jsonObjectData.optString("university");
                    String course_id = jsonObjectData.optString("course_id");
                    String course = jsonObjectData.optString("course");
                    String semester_id = jsonObjectData.optString("semester_id");
                    String semester = jsonObjectData.optString("semester");
                    String user_id = jsonObjectData.optString("user_id");


                    Utility.setSharedPrefStringData(this, Constants.FNAME, fname);
                    Utility.setSharedPrefStringData(this, Constants.LNAME, lname);
                    Utility.setSharedPrefStringData(this, Constants.GENDER, gender);
                    Utility.setSharedPrefStringData(this, Constants.MOBILE, mobile);
                    Utility.setSharedPrefStringData(this, Constants.EMAIL, email);
                    Utility.setSharedPrefStringData(this, Constants.STATE_ID, state_id);
                    Utility.setSharedPrefStringData(this, Constants.STATE, state);
                    Utility.setSharedPrefStringData(this, Constants.UNIVERSITY_ID, university_id);
                    Utility.setSharedPrefStringData(this, Constants.UNIVERSITY, university);
                    Utility.setSharedPrefStringData(this, Constants.COURSE_ID, course_id);
                    Utility.setSharedPrefStringData(this, Constants.COURSE, course);
                    Utility.setSharedPrefStringData(this, Constants.SEMESTER, semester);
                    Utility.setSharedPrefStringData(this, Constants.SEMESTER_ID, semester_id);
                    Utility.setSharedPrefStringData(this, Constants.USER_ID, "" + user_id);

                    startActivity(new Intent(this, HomeActivity.class));
                    finishAffinity();
                } else {
                    PopUtils.alertDialog(this, message, null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
