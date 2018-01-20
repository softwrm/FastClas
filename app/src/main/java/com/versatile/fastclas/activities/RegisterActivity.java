package com.versatile.fastclas.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.versatile.fastclas.BaseActivity;
import com.versatilemobitech.fastclas.R;
import com.versatile.fastclas.interfaces.IParseListener;
import com.versatile.fastclas.interfaces.ReturnValue;
import com.versatile.fastclas.models.CourseModel;
import com.versatile.fastclas.models.SemesterModel;
import com.versatile.fastclas.models.StateModel;
import com.versatile.fastclas.models.UniversityModel;
import com.versatile.fastclas.utils.Constants;
import com.versatile.fastclas.utils.PopUtils;
import com.versatile.fastclas.utils.ServerResponse;
import com.versatile.fastclas.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, IParseListener {

    TextView mTxtTitle, mTxtAlreadymember;
    EditText mEdtFirstname, mEdtLastname,
            mEdtGender, mEdtPhoneno,
            mEdtEmail, mEdtPassword,
            mEdtConfirmpassword,
            mEdtState, mEdtUniversity,
            mEdtCourse, mEdtSemester;
    ImageView mImgBack;
    CheckBox mSelect;
    Button mBtnRegister;

    String fname, lname, gender, mobile, email, password, stateId, universityId, courseId, semesterId, otp;
    String state_id, university_id, course_id, semester_id;

    ArrayList<StateModel> stateModelArrayList = new ArrayList<>();
    ArrayList<UniversityModel> universityModelArrayList = new ArrayList<>();
    ArrayList<CourseModel> courseModelArrayList = new ArrayList<>();
    ArrayList<SemesterModel> semesterModelArrayList = new ArrayList<>();
    ArrayList<String> statesArrayList = new ArrayList<>();
    ArrayList<String> universityArrayList = new ArrayList<>();
    ArrayList<String> courseArrayList = new ArrayList<>();
    ArrayList<String> semesterArrayList = new ArrayList<>();

    String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mTxtTitle = (TextView) findViewById(R.id.txtToolbar);
        mTxtTitle.setText(R.string.app_name_caps);
        initComponents();
    }

    private void initComponents() {
        setReferences();
        setClickListeners();

        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private void setReferences() {

        mTxtAlreadymember = (TextView) findViewById(R.id.txtAlready);
        mEdtFirstname = (EditText) findViewById(R.id.edtFirstname);
        mEdtLastname = (EditText) findViewById(R.id.edtLastname);
        mEdtGender = (EditText) findViewById(R.id.edtGender);
        mEdtPhoneno = (EditText) findViewById(R.id.edtPhoneno);
        mEdtEmail = (EditText) findViewById(R.id.edtEmail);
        mEdtPassword = (EditText) findViewById(R.id.edtPassword);
        mEdtConfirmpassword = (EditText) findViewById(R.id.edtConfirmpassword);
        mEdtState = (EditText) findViewById(R.id.edtState);
        mEdtUniversity = (EditText) findViewById(R.id.edtUniversity);
        mEdtCourse = (EditText) findViewById(R.id.edtCourse);
        mEdtSemester = (EditText) findViewById(R.id.edtSemester);
        mSelect = (CheckBox) findViewById(R.id.chTerms);
        mBtnRegister = (Button) findViewById(R.id.btnRegister);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mImgBack.setVisibility(View.GONE);

        SpannableString styledString = new SpannableString(mTxtAlreadymember.getText().toString());
        styledString.setSpan(new RelativeSizeSpan(1.5f), 25, 31, 0);
        styledString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                finish();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(Color.BLACK);
            }
        }, 25, 31, 0);
        mTxtAlreadymember.setMovementMethod(LinkMovementMethod.getInstance());
        mTxtAlreadymember.setText(styledString);
    }

    private void setClickListeners() {
        mBtnRegister.setOnClickListener(this);
        mEdtGender.setOnClickListener(this);
        mEdtState.setOnClickListener(this);
        mEdtUniversity.setOnClickListener(this);
        mEdtCourse.setOnClickListener(this);
        mEdtSemester.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                callServiceForRegistration();
                break;
            case R.id.edtGender:
                selectGender();
                break;
            case R.id.edtState: {
                callStateWebService();
                break;
            }
            case R.id.edtUniversity: {
                if (!Utility.isValueNullOrEmpty(mEdtState.getText().toString().trim())) {
                    if (universityArrayList.size() <= 0) {
                        PopUtils.alertDialog(RegisterActivity.this, "No Universities Found", null);
                    } else {
                        selectUniversity();
                    }
                } else {
                    PopUtils.alertDialog(RegisterActivity.this, "Please Select State", null);
                }
                break;
            }
            case R.id.edtCourse: {
                if (!Utility.isValueNullOrEmpty(mEdtUniversity.getText().toString().trim())) {
                    if (courseArrayList.size() <= 0) {
                        PopUtils.alertDialog(RegisterActivity.this, "No Courses Found", null);
                    } else {
                        selectCourse();
                    }
                } else {
                    PopUtils.alertDialog(RegisterActivity.this, "Please Select University", null);
                }

                break;
            }
            case R.id.edtSemester: {
                if (!Utility.isValueNullOrEmpty(mEdtCourse.getText().toString().trim())) {
                    if (semesterArrayList.size() <= 0) {
                        PopUtils.alertDialog(RegisterActivity.this, "No Semester's Found", null);
                    } else {
                        selectSemester();
                    }
                } else {
                    PopUtils.alertDialog(RegisterActivity.this, "Please Select Course", null);
                }
                break;
            }
        }

    }


    private void selectGender() {
        ArrayList<String> genderArrayList = new ArrayList<>();
        genderArrayList.add("Male");
        genderArrayList.add("Female");

        PopUtils.showListItems(this, genderArrayList, mEdtGender, "Select Gender", new ReturnValue() {
            @Override
            public void returnValues(String value, int positionValue) {
                Utility.showLog("Finished", "" + positionValue);
            }
        });
    }


    private void selectUniversity() {
        PopUtils.showListItems(this, universityArrayList, mEdtUniversity, "Select University", new ReturnValue() {
            @Override
            public void returnValues(String value, int positionValue) {

                mEdtCourse.setText("");
                mEdtSemester.setText("");

                for (int i = 0; i < stateModelArrayList.size(); i++) {
                    if (mEdtState.getText().toString().trim().contains(stateModelArrayList.get(i).getState_name().trim())) {
                        state_id = stateModelArrayList.get(i).getState_id();
                    }
                }

                for (int i = 0; i < universityModelArrayList.size(); i++) {
                    if (value.equals(universityModelArrayList.get(i).getUniversity_name().trim())) {
                        university_id = universityModelArrayList.get(i).getUniversity_id();
                    }
                }

                callServiceForCourse(state_id, university_id);
            }
        });
    }

    private void selectCourse() {

        PopUtils.showListItems(this, courseArrayList, mEdtCourse, "Select Course", new ReturnValue() {
            @Override
            public void returnValues(String value, int positionValue) {

                mEdtSemester.setText("");

                for (int i = 0; i < stateModelArrayList.size(); i++) {
                    if (mEdtState.getText().toString().trim().equals(stateModelArrayList.get(i).getState_name().trim())) {
                        state_id = stateModelArrayList.get(i).getState_id();
                    }
                }

                for (int i = 0; i < universityModelArrayList.size(); i++) {
                    if (mEdtUniversity.getText().toString().trim().equals(universityModelArrayList.get(i).getUniversity_name().trim())) {
                        university_id = universityModelArrayList.get(i).getUniversity_id();
                    }
                }

                for (int i = 0; i < courseModelArrayList.size(); i++) {
                    if (value.equals(courseModelArrayList.get(i).getCourseName().trim())) {
                        course_id = courseModelArrayList.get(i).getCourseId();
                    }
                }
                callServiceForSemester(state_id, university_id, course_id);
            }
        });
    }

    private void selectSemester() {
        PopUtils.showListItems(this, semesterArrayList, mEdtSemester, "Select Semester", new ReturnValue() {
            @Override
            public void returnValues(String value, int positionValue) {
                for (int i = 0; i < semesterModelArrayList.size(); i++) {
                    if (value.equals(semesterModelArrayList.get(i).getSemester_name().trim())) {
                        semester_id = semesterModelArrayList.get(i).getSemester_id();
                    }
                }
            }
        });
    }

    private void callServiceForRegistration() {
        if (validated()) {
            if (PopUtils.checkInternetConnection(this)) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("action", "register");
                    jsonObject.put("fname", mEdtFirstname.getText().toString().trim());
                    jsonObject.put("lname", mEdtLastname.getText().toString().trim());
                    jsonObject.put("gender", mEdtGender.getText().toString().trim());
                    jsonObject.put("mobile", mEdtPhoneno.getText().toString().trim());
                    jsonObject.put("email", mEdtEmail.getText().toString().trim());
                    jsonObject.put("password", mEdtPassword.getText().toString().trim());
                    jsonObject.put("state_id", state_id);
                    jsonObject.put("university_id", university_id);
                    jsonObject.put("course_id", course_id);
                    jsonObject.put("semester_id", semester_id);

                    showLoadingDialog("Loading...", false);

                    ServerResponse serverResponse = new ServerResponse();
                    serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.SERVICE_REGISTER);

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
        if (Utility.isValueNullOrEmpty(mEdtFirstname.getText().toString().trim())) {
            Utility.setSnackBarEnglish(RegisterActivity.this, mEdtFirstname, "Enter First Name");
            mEdtFirstname.requestFocus();
        } else if (Utility.isValueNullOrEmpty(mEdtLastname.getText().toString().trim())) {
            Utility.setSnackBarEnglish(RegisterActivity.this, mEdtLastname, "Enter Last Name");
            mEdtLastname.requestFocus();
        } else if (Utility.isValueNullOrEmpty(mEdtGender.getText().toString().trim())) {
            Utility.setSnackBarEnglish(RegisterActivity.this, mEdtGender, "Select Gender");
            mEdtGender.requestFocus();
        } else if (Utility.isValueNullOrEmpty(mEdtPhoneno.getText().toString().trim())) {
            Utility.setSnackBarEnglish(RegisterActivity.this, mEdtPhoneno, "Enter Phone Number");
            mEdtPhoneno.requestFocus();
        } else if (mEdtPhoneno.getText().toString().trim().length() != 10) {
            Utility.setSnackBarEnglish(RegisterActivity.this, mEdtPhoneno, "Enter Valid Phone Number");
            mEdtPhoneno.requestFocus();
        } else if (Utility.isValueNullOrEmpty(mEdtEmail.getText().toString().trim())) {
            Utility.setSnackBarEnglish(RegisterActivity.this, mEdtEmail, "Enter Email ID");
            mEdtEmail.requestFocus();
        } else if (!mEdtEmail.getText().toString().trim().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z]+)*(\\.[A-Za-z]{2,})$")) {
            Utility.setSnackBarEnglish(RegisterActivity.this, mEdtEmail, "Enter Valid Email ID");
            mEdtEmail.requestFocus();
        } else if (Utility.isValueNullOrEmpty(mEdtPassword.getText().toString().trim())) {
            Utility.setSnackBarEnglish(RegisterActivity.this, mEdtPassword, "Enter Password");
            mEdtPassword.requestFocus();
        } else if (Utility.isValueNullOrEmpty(mEdtConfirmpassword.getText().toString().trim())) {
            Utility.setSnackBarEnglish(RegisterActivity.this, mEdtConfirmpassword, "Enter Confirm Password");
            mEdtConfirmpassword.requestFocus();
        } else if (mEdtPassword.getText().toString().trim().length() < 5) {
            Utility.setSnackBarEnglish(RegisterActivity.this, mEdtPassword, "Enter Valid Password(More than 6 letters)");
            mEdtPassword.requestFocus();
        } else if (!mEdtPassword.getText().toString().trim().equals(mEdtConfirmpassword.getText().toString().trim())) {
            Utility.setSnackBarEnglish(RegisterActivity.this, mEdtPassword, "Passwords Mismatch");
            mEdtPassword.requestFocus();
        } else if (Utility.isValueNullOrEmpty(mEdtState.getText().toString().trim())) {
            Utility.setSnackBarEnglish(RegisterActivity.this, mEdtState, "Select State");
            mEdtState.requestFocus();
        } else if (Utility.isValueNullOrEmpty(mEdtUniversity.getText().toString().trim())) {
            Utility.setSnackBarEnglish(RegisterActivity.this, mEdtUniversity, "Select University");
            mEdtUniversity.requestFocus();
        } else if (Utility.isValueNullOrEmpty(mEdtCourse.getText().toString().trim())) {
            Utility.setSnackBarEnglish(RegisterActivity.this, mEdtCourse, "Select Course");
            mEdtCourse.requestFocus();
        } else if (Utility.isValueNullOrEmpty(mEdtSemester.getText().toString().trim())) {
            Utility.setSnackBarEnglish(RegisterActivity.this, mEdtSemester, "Select Semester");
            mEdtSemester.requestFocus();
        } else if (!mSelect.isChecked()) {
            Utility.setSnackBarEnglish(RegisterActivity.this, mEdtPassword, "Please accept the Terms and Conditions");
        } else {
            validation = true;
        }
        return validation;
    }

    private void callStateWebService() {
        if (PopUtils.checkInternetConnection(this)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("action", "state");

                showLoadingDialog("Loading...", false);
                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.Service_State);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            PopUtils.alertDialog(this, getString(R.string.pls_check_internet), null);
        }
    }

    private void callServiceForUniversity(String positionValue) {
        if (PopUtils.checkInternetConnection(this)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("action", "university");
                Utility.showLog("statesid", "" + positionValue);
                jsonObject.put("state_id", positionValue);

                showLoadingDialog("Loading...", false);

                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.Service_University);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            PopUtils.alertDialog(this, getString(R.string.pls_check_internet), null);
        }

    }

    private void callServiceForCourse(String state_id, String university_id) {
        if (PopUtils.checkInternetConnection(this)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("action", "course");
                jsonObject.put("state_id", state_id);
                jsonObject.put("university_id", university_id);

                showLoadingDialog("Loading...", false);

                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.Service_Course);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            PopUtils.alertDialog(this, getString(R.string.pls_check_internet), null);
        }
    }

    private void callServiceForSemester(String state_id, String university_id, String course_id) {
        if (PopUtils.checkInternetConnection(this)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("action", "semester");
                jsonObject.put("state_id", state_id);
                jsonObject.put("university_id", university_id);
                jsonObject.put("course_id", course_id);

                showLoadingDialog("Loading...", false);

                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.Service_Semester);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            PopUtils.alertDialog(this, getString(R.string.pls_check_internet), null);
        }
    }

    private void callServiceForOTP() {
        if (PopUtils.checkInternetConnection(this)) {
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("action", "otpexits");
                jsonObject.put("fname", mEdtFirstname.getText().toString().trim());
                jsonObject.put("lname", mEdtLastname.getText().toString().trim());
                jsonObject.put("gender", mEdtGender.getText().toString().trim());
                jsonObject.put("mobile", mEdtPhoneno.getText().toString().trim());
                jsonObject.put("email", mEdtEmail.getText().toString().trim());
                jsonObject.put("password", mEdtPassword.getText().toString().trim());
                jsonObject.put("state_id", state_id);
                jsonObject.put("university_id", university_id);
                jsonObject.put("course_id", course_id);
                jsonObject.put("semester_id", semester_id);
                jsonObject.put("user_otp", otp);
                jsonObject.put("device_id", deviceId);

                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.SERVICE_OTP);

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
        if (requestCode == Constants.Service_State) {
            hideLoadingDialog();
            PopUtils.alertDialog(this, "Please Check Internet Connection", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                }
            });
        } else if (requestCode == Constants.Service_University) {
            hideLoadingDialog();
            PopUtils.alertDialog(this, "Please Check Internet Connection", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                }
            });
        } else if (requestCode == Constants.Service_Course) {
            hideLoadingDialog();
            PopUtils.alertDialog(this, "Please Check Internet Connection", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                }
            });
        } else if (requestCode == Constants.Service_Semester) {
            hideLoadingDialog();
            PopUtils.alertDialog(this, "Please Check Internet Connection", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                }
            });
        } else if (requestCode == Constants.SERVICE_REGISTER) {
            hideLoadingDialog();
            PopUtils.alertDialog(this, "Please Check Internet Connection", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                }
            });
        } else if (requestCode == Constants.SERVICE_OTP) {
            hideLoadingDialog();
            PopUtils.alertDialog(this, "Please Check Internet Connection", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                }
            });
        }
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        if (requestCode == Constants.Service_State) {
            hideLoadingDialog();
            try {
                statesArrayList.clear();
                stateModelArrayList.clear();

                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");
                if (status.equals("200")) {
                    JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArrayData.length(); i++) {
                        JSONObject jsonObjectData = jsonArrayData.getJSONObject(i);

                        StateModel stateModel = new StateModel();
                        stateModel.setState_id(jsonObjectData.optString("state_id"));
                        stateModel.setState_name(jsonObjectData.optString("state"));

                        stateModelArrayList.add(stateModel);
                        statesArrayList.add(jsonObjectData.optString("state"));
                    }
                    PopUtils.showListItems(this, statesArrayList, mEdtState, "Select State", new ReturnValue() {
                        @Override
                        public void returnValues(String value, int positionValue) {

                            mEdtUniversity.setText("");
                            mEdtCourse.setText("");
                            mEdtSemester.setText("");
                            for (int i = 0; i < stateModelArrayList.size(); i++) {
                                String stateValue = stateModelArrayList.get(i).getState_name().trim();
                                if (stateValue.equals(value)) {
                                    callServiceForUniversity(stateModelArrayList.get(i).getState_id());
                                }
                            }
                        }
                    });

                } else {
                    PopUtils.alertDialog(this, "No States Found", null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (requestCode == Constants.Service_University) {
            hideLoadingDialog();

            try {
                universityModelArrayList.clear();
                universityArrayList.clear();

                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");

                if (status.equals("200")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectData = jsonArray.getJSONObject(i);

                        String university_id = jsonObjectData.optString("university_id");
                        String university = jsonObjectData.optString("university");

                        UniversityModel universityModel = new UniversityModel();
                        universityModel.setUniversity_id(university_id);
                        universityModel.setUniversity_name(university);

                        universityModelArrayList.add(universityModel);
                        universityArrayList.add(university);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (requestCode == Constants.Service_Course) {
            hideLoadingDialog();

            try {

                courseArrayList.clear();
                courseModelArrayList.clear();

                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");
                if (status.equals("200")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectData = jsonArray.getJSONObject(i);

                        String course_id = jsonObjectData.optString("course_id");
                        String course = jsonObjectData.optString("course");

                        CourseModel courseModel = new CourseModel();
                        courseModel.setCourseId(course_id);
                        courseModel.setCourseName(course);

                        courseModelArrayList.add(courseModel);
                        courseArrayList.add(course);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (requestCode == Constants.Service_Semester) {
            hideLoadingDialog();

            try {

                semesterArrayList.clear();
                semesterModelArrayList.clear();

                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");
                if (status.equals("200")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectData = jsonArray.getJSONObject(i);

                        String semester_id = jsonObjectData.optString("semester_id");
                        String semester = jsonObjectData.optString("semester");

                        SemesterModel semesterModel = new SemesterModel();
                        semesterModel.setSemester_id(semester_id);
                        semesterModel.setSemester_name(semester);

                        semesterArrayList.add(semester);
                        semesterModelArrayList.add(semesterModel);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (requestCode == Constants.SERVICE_REGISTER) {
            hideLoadingDialog();

            try {
                JSONObject jsonObject = new JSONObject(response);

                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");

                if (status.equals("200")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    JSONObject jsonObjectData = jsonArray.getJSONObject(0);
                    fname = jsonObjectData.optString("fname");
                    lname = jsonObjectData.optString("lname");
                    gender = jsonObjectData.optString("gender");
                    mobile = jsonObjectData.optString("mobile");
                    email = jsonObjectData.optString("email");
                    password = jsonObjectData.optString("password");
                    stateId = jsonObjectData.optString("state_id");
                    universityId = jsonObjectData.optString("university_id");
                    courseId = jsonObjectData.optString("course_id");
                    semesterId = jsonObjectData.optString("semester_id");
                    otp = jsonObjectData.optString("otp");


                    PopUtils.otpPasswordDialog(this, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Utility.showLog("value", view.getTag().toString());
                            if (otp.equals(view.getTag().toString().trim())) {
                                callServiceForOTP();
                            } else {
                                PopUtils.alertDialog(RegisterActivity.this, "Otp Mismatch. Try Again", null);
                            }
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            callServiceForRegistration();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (requestCode == Constants.SERVICE_OTP) {
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
                    String password = jsonObjectData.optString("password");
                    String state_id = jsonObjectData.optString("state_id");
                    String state = jsonObjectData.optString("state");
                    String university_id = jsonObjectData.optString("university_id");
                    String university = jsonObjectData.optString("university");
                    String course_id = jsonObjectData.optString("course_id");
                    String course = jsonObjectData.optString("course");
                    String semester_id = jsonObjectData.optString("semester_id");
                    String semester = jsonObjectData.optString("semester");
                    int user_id = jsonObjectData.optInt("user_id");

                    Utility.setSharedPrefStringData(this, Constants.FNAME, fname);
                    Utility.setSharedPrefStringData(this, Constants.LNAME, lname);
                    Utility.setSharedPrefStringData(this, Constants.GENDER, gender);
                    Utility.setSharedPrefStringData(this, Constants.MOBILE, mobile);
                    Utility.setSharedPrefStringData(this, Constants.EMAIL, email);
                    Utility.setSharedPrefStringData(this, Constants.PASSWORD, password);
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

                } else {
                    PopUtils.alertDialog(this, message, null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
