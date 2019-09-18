package com.versatile.fastclas.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.versatile.fastclas.adapters.DashboardAdapter;
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

public class DashBoardActivity extends AppCompatActivity implements IParseListener, View.OnClickListener {

    TextView mTitle;
    ImageView mImgBack;
    RecyclerView universityRecyclerView;
    public static ArrayList<UniversityModel> universityModelArrayList;
    ArrayList<CourseModel> courseModelArrayList;
    TextView txtNoDataFound;

    public static ArrayList<UniversityModel> getUniversityModelArrayList() {
        return universityModelArrayList;
    }

    public static void setUniversityModelArrayList(ArrayList<UniversityModel> universityModelArrayList) {
        DashBoardActivity.universityModelArrayList = universityModelArrayList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        HomeActivity.navigationView.getMenu().getItem(0).setChecked(false);

        universityRecyclerView = findViewById(R.id.universityRecyclerView);
        universityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        universityRecyclerView.setItemAnimator(new DefaultItemAnimator());
        universityRecyclerView.setHasFixedSize(true);
        txtNoDataFound = findViewById(R.id.txtNoDataFound);

        mImgBack = findViewById(R.id.imgBack);
        mTitle = findViewById(R.id.txtToolbar);
        mTitle.setText(getResources().getString(R.string.university));

        mImgBack.setOnClickListener(this);

        callServiceForDeviceChecking();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
        }
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
            Utility.showSettingDialog(this,
                    this.getResources().getString(R.string.some_thing_went_wrong),
                    this.getResources().getString(R.string.error), Constants.SERVER_ERROR).show();
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
                    universityModelArrayList = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.optJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        courseModelArrayList = new ArrayList<>();

                        JSONObject jsonObjectInner = jsonArray.optJSONObject(i);
                        String university_id = jsonObjectInner.optString("university_id");
                        String university = jsonObjectInner.optString("university");
                        JSONArray courseArray = jsonObjectInner.optJSONArray("course");

                        if (jsonObjectInner.has("course")) {
                            for (int j = 0; j < courseArray.length(); j++) {

                                JSONObject jsonObjectCourse = courseArray.optJSONObject(j);
                                String course_id = jsonObjectCourse.optString("course_id");
                                String course = jsonObjectCourse.optString("course");

                                CourseModel courseModel = new CourseModel();
                                courseModel.setCourseName(course);
                                courseModel.setCourseId(course_id);
                                courseModelArrayList.add(courseModel);

                            }
                        }
                        UniversityModel universityModel = new UniversityModel();

                        universityModel.setUniversity_id(university_id);
                        universityModel.setUniversity_name(university);

                        universityModel.setCourseModelArrayList(courseModelArrayList);

                        universityModelArrayList.add(universityModel);
                    }
                    DashBoardActivity.setUniversityModelArrayList(universityModelArrayList);

                    DashboardAdapter dashboardAdapter = new DashboardAdapter(this, universityModelArrayList);
                    universityRecyclerView.setAdapter(dashboardAdapter);

                } else {
                    txtNoDataFound.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                Utility.showLog("Error", "" + e);
            }
        }
    }
}
