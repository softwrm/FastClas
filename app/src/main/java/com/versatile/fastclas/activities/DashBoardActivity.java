package com.versatile.fastclas.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.versatile.fastclas.interfaces.IParseListener;
import com.versatile.fastclas.models.CourseModel;
import com.versatile.fastclas.models.UniversityModel;
import com.versatile.fastclas.utils.Constants;
import com.versatile.fastclas.utils.PopUtils;
import com.versatile.fastclas.utils.ServerResponse;
import com.versatile.fastclas.utils.Utility;
import com.versatilemobitech.fastclas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DashBoardActivity extends AppCompatActivity implements IParseListener {

    RecyclerView universityRecyclerView;
    ArrayList<UniversityModel> universityModelArrayList = new ArrayList<>();
    ArrayList<CourseModel> courseModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        universityRecyclerView = findViewById(R.id.universityRecyclerView);
    }

    private void callServiceForDeviceChecking() {
        if (PopUtils.checkInternetConnection(DashBoardActivity.this)) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("action", "dashboard");
                jsonObject.put("state_id", "" + Utility.getSharedPreference(this, Constants.STATE_ID));

                Utility.showLoadingDialog(this, "Loading...", false);

                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.SERVICE_DASHBOARD);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            PopUtils.alertDialog(DashBoardActivity.this, getString(R.string.pls_check_internet), null);
        }

    }

    @Override
    public void ErrorResponse(VolleyError volleyError, int requestCode) {
        if (requestCode == Constants.SERVICE_DASHBOARD) {
            Utility.hideLoadingDialog();
            PopUtils.alertDialog(DashBoardActivity.this, getString(R.string.some_thing_went_wrong), null);
        }
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        if (requestCode == Constants.SERVICE_DASHBOARD) {
            Utility.hideLoadingDialog();

            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");
                if (status.equals("200")) {
                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectInner = jsonArray.optJSONObject(i);
                        String university_id = jsonObjectInner.optString("university_id");
                        String university = jsonObjectInner.optString("university");
                        JSONArray courseArray = jsonObjectInner.optJSONArray("course");
                        for (int j = 0; j < courseArray.length(); j++) {
                            JSONObject jsonObjectCourse = courseArray.optJSONObject(j);
                            String course_id = jsonObjectCourse.optString("course_id");
                            String course = jsonObjectCourse.optString("course");
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
