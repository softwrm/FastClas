package com.versatile.fastclas.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.versatile.fastclas.BaseActivity;
import com.versatilemobitech.fastclas.R;
import com.versatile.fastclas.adapters.HomeAdapter;
import com.versatile.fastclas.interfaces.IParseListener;
import com.versatile.fastclas.models.SubjectModel;
import com.versatile.fastclas.utils.Constants;
import com.versatile.fastclas.utils.PopUtils;
import com.versatile.fastclas.utils.ServerResponse;
import com.versatile.fastclas.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener,
        HomeAdapter.OnItemClickListener, IParseListener {

    //    private static final String TAG = HomeActivity.class.getSimpleName();
    private Toolbar toolbar;
    private DrawerLayout drawer;
    @SuppressLint("StaticFieldLeak")
    public static NavigationView navigationView;
    private RecyclerView recyclerView;
    TextView tvUserWelcomeHeader, tvUserEmailHeader, txtToolbar;
    TextView txtSemester, txtCourse, txtxNoDataFound;
    private ImageView imgNotification;
    Dialog dialog;
    ArrayList<SubjectModel> subjectModelArrayList = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    static View viewNotificationIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
    }

    private void initViews() {
        setReferences();
        setNavigationDrawer();
        setClickListeners();
        callWedServiceForSubjects();
        callWebServiceForNotificationCount();

        if (PopUtils.checkInternetConnection(this)) {

            final Handler handler = new Handler();
            final int delay = 15000; //milliseconds


            handler.postDelayed(new Runnable() {
                public void run() {
                    //do something
                    if (PopUtils.checkInternetConnection(HomeActivity.this)) {
                        callWebServiceForNotificationCount();
                    }
                    handler.postDelayed(this, delay);
                }
            }, delay);
        }

    }

    private void setReferences() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        txtToolbar = findViewById(R.id.txtToolbar);
        txtxNoDataFound = findViewById(R.id.txtxNoDataFound);
        txtSemester = findViewById(R.id.txtSemester);
        txtCourse = findViewById(R.id.txtCourse);

        txtSemester.setText(Utility.getSharedPreference(this, Constants.SEMESTER));
        txtCourse.setText(Utility.getSharedPreference(this, Constants.COURSE));

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        imgNotification = findViewById(R.id.imgNotification);
        viewNotificationIcon = findViewById(R.id.viewNotificationIcon);

    }

    private void setNavigationDrawer() {
        navigationView.setItemIconTintList(null);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        View headerLayout = navigationView.getHeaderView(0);
        tvUserWelcomeHeader = headerLayout.findViewById(R.id.tvUserWelcome);
        tvUserEmailHeader = headerLayout.findViewById(R.id.tvUserEmailHeader);
        tvUserEmailHeader.setText(Utility.getSharedPreference(this, Constants.EMAIL));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menubaricon1);
        // txtToolbar3.setText(R.string.home);
    }


    private void setClickListeners() {
        navigationView.setNavigationItemSelectedListener(this);
        imgNotification.setOnClickListener(this);
    }

    private void callWedServiceForSubjects() {
        if (PopUtils.checkInternetConnection(this)) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("action", "subjects");
                jsonObject.put("user_id", Utility.getSharedPreference(this, Constants.USER_ID));

                showLoadingDialog("Loaing...", false);

                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.SERVICE_SUBJECT);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            PopUtils.alertDialog(this, getString(R.string.pls_check_internet), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finishAffinity();
                }
            });
        }
    }

    private void callWebServiceForNotificationCount() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "notification");
            jsonObject.put("user_id", "" + Utility.getSharedPreference(this, Constants.USER_ID));

            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.SERVICE_NOTIFICATION);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgNotification:
                startActivity(new Intent(this, NotificationActivity.class));
                break;
        }

    }

    //From RecyclerView
    @Override
    public void onItemClick(SubjectModel subjectModel, int Position) {
        Intent intent = new Intent(this, AllUnitsActivity.class);
        intent.putExtra("id", subjectModel.getId());
        intent.putExtra("subject_name", subjectModel.getSubjectName());
        intent.putExtra("amount", "" + subjectModel.getAmount());
        intent.putExtra("payment_status", "" + subjectModel.getPaymentStatus());
        navigateActivity(intent, false);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_myprofile) {
            navigateActivity(new Intent(this, MyProfileActivity.class), false);

        } else if (id == R.id.nav_aboutus) {
            navigateActivity(new Intent(this, AboutFastclasActivity.class), false);
        } else if (id == R.id.nav_editProfile) {
            navigateActivity(new Intent(this, EditProfileActivity.class), false);
        } else if (id == R.id.nav_changepassword) {
            navigateActivity(new Intent(this, ChangePasswordActivity.class), false);
        } else if (id == R.id.nav_termsandconditions) {
            navigateActivity(new Intent(this, TermsandconditionsActivity.class), false);
        } else if (id == R.id.nav_help) {
            navigateActivity(new Intent(this, HelpActivity.class), false);
        } else if (id == R.id.nav_logout) {
            PopUtils.exitDialog(HomeActivity.this, "Are you sure.....you want to logout?", logoutClick);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    View.OnClickListener logoutClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Utility.setSharedPrefStringData(HomeActivity.this, Constants.FNAME, "");
            Utility.setSharedPrefStringData(HomeActivity.this, Constants.LNAME, "");
            Utility.setSharedPrefStringData(HomeActivity.this, Constants.GENDER, "");
            Utility.setSharedPrefStringData(HomeActivity.this, Constants.MOBILE, "");
            Utility.setSharedPrefStringData(HomeActivity.this, Constants.EMAIL, "");
            Utility.setSharedPrefStringData(HomeActivity.this, Constants.PASSWORD, "");
            Utility.setSharedPrefStringData(HomeActivity.this, Constants.STATE_ID, "");
            Utility.setSharedPrefStringData(HomeActivity.this, Constants.STATE, "");
            Utility.setSharedPrefStringData(HomeActivity.this, Constants.UNIVERSITY_ID, "");
            Utility.setSharedPrefStringData(HomeActivity.this, Constants.UNIVERSITY, "");
            Utility.setSharedPrefStringData(HomeActivity.this, Constants.COURSE_ID, "");
            Utility.setSharedPrefStringData(HomeActivity.this, Constants.COURSE, "");
            Utility.setSharedPrefStringData(HomeActivity.this, Constants.SEMESTER, "");
            Utility.setSharedPrefStringData(HomeActivity.this, Constants.SEMESTER_ID, "");
            Utility.setSharedPrefStringData(HomeActivity.this, Constants.USER_ID, "");

            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finishAffinity();
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            dialog = PopUtils.exitDialog(this, "Do you want to Exit...?", YesExitClick);
        }
    }

    View.OnClickListener YesExitClick = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View v) {
            dialog.dismiss();
            finishAffinity();
        }
    };

    @Override
    public void ErrorResponse(VolleyError volleyError, int requestCode) {
        if (requestCode == Constants.SERVICE_SUBJECT) {
            hideLoadingDialog();
            PopUtils.alertDialog(this, "Please Check Internet Connection", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finishAffinity();
                }
            });
        } else if (requestCode == Constants.SERVICE_NOTIFICATION) {
            Utility.showLog("Error", "" + volleyError);
        }
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        if (requestCode == Constants.SERVICE_SUBJECT) {
            hideLoadingDialog();
            subjectModelArrayList.clear();
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status");
//                String message = jsonObject.optString("message");
                if (status.equals("200")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectdata = jsonArray.getJSONObject(i);
                        String subject_id = jsonObjectdata.optString("subject_id");
                        String subject = jsonObjectdata.optString("subject");
                        String amount = jsonObjectdata.optString("amount");
                        String description = jsonObjectdata.optString("description");
                        String payment_status = jsonObjectdata.optString("payment_status");

                        SubjectModel subjectModel = new SubjectModel();
                        subjectModel.setAmount(amount);
                        subjectModel.setId(subject_id);
                        subjectModel.setPaymentStatus(payment_status);
                        subjectModel.setDescription(description);
                        subjectModel.setSubjectName(subject);

                        subjectModelArrayList.add(subjectModel);
                    }
                    HomeAdapter homeAdapter = new HomeAdapter(this, subjectModelArrayList, this);
                    recyclerView.setAdapter(homeAdapter);
                } else {
                    txtxNoDataFound.setText("No Subjects Found");
                    txtxNoDataFound.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (requestCode == Constants.SERVICE_NOTIFICATION) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("status").equals("200")) {
                    int count = jsonObject.optInt("count");

                    if (Integer.parseInt(Utility.getSharedPreference(this, Constants.NOTIFICATION_COUNT)) < count) {
                        viewNotificationIcon.setVisibility(View.VISIBLE);
                    } else {
                        viewNotificationIcon.setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {
                Utility.showLog("Error", "" + e);
            }
        }
    }
}
