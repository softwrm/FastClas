package com.versatile.fastclas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.versatile.fastclas.BaseActivity;
import com.versatile.fastclas.adapters.PackagesAdapter;
import com.versatile.fastclas.interfaces.IParseListener;
import com.versatile.fastclas.models.PackagesModel;
import com.versatile.fastclas.models.SubjectsModel;
import com.versatile.fastclas.utils.Constants;
import com.versatile.fastclas.utils.PopUtils;
import com.versatile.fastclas.utils.ServerResponse;
import com.versatile.fastclas.utils.Utility;
import com.versatilemobitech.fastclas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Excentd11 on 4/16/2018.
 */

public class PackagesActivity extends BaseActivity implements View.OnClickListener, IParseListener {
    private RecyclerView mRecyclerView;
    private TextView mTxtTitle, mTxtProceed, mTxtContent;
    private ImageView mImgBack;
    private PackagesAdapter mPackagesAdapter;

    private String subjectAmount = "", subjectId = "";

    ArrayList<PackagesModel> mPackagesModel = new ArrayList<>();
    ArrayList<SubjectsModel> mSubjectsModel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages);

        initComponents();
    }

    private void initComponents() {
        setReferences();
        getBundleData();
        setClickListeners();

        requestForPackagesWS();
    }

    private void setReferences() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mTxtTitle = findViewById(R.id.txtToolbar);
        mTxtTitle.setText("Offers");
        mTxtProceed = findViewById(R.id.txtProceed);
        mTxtContent = findViewById(R.id.txtContent);
        mImgBack = findViewById(R.id.imgBack);
    }

    private void getBundleData() {
        Intent intent = getIntent();
        if (intent != null) {
            subjectAmount = intent.getStringExtra("amount");
            subjectId = intent.getStringExtra("subjectId");
        }
    }

    private void setClickListeners() {
        mImgBack.setOnClickListener(this);
        mTxtProceed.setOnClickListener(this);
    }

    private void requestForPackagesWS() {
        if (PopUtils.checkInternetConnection(this)) {
            showLoadingDialog("Loading...", false);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("action", "packages");
                jsonObject.put("university_id", Utility.getSharedPreference(this, Constants.UNIVERSITY_ID));
                jsonObject.put("cource_id", Utility.getSharedPreference(this, Constants.COURSE_ID));
                jsonObject.put("semister_id", Utility.getSharedPreference(this, Constants.SEMESTER_ID));
                jsonObject.put("user_id", Utility.getSharedPreference(this, Constants.USER_ID));

                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.SERVICE_PACKAGES);
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
                finish();
                break;
            case R.id.txtProceed:
                if (mPackagesAdapter != null && mPackagesAdapter.selectedPackagePosition() != -1) {
                    navigateToPaymentActivity(true, mPackagesAdapter.selectedPackagePosition());
                } else /*if (mPackagesAdapter != null && mPackagesAdapter.selectedPackagePosition() == -1)*/ {
                    navigateToPaymentActivity(false, -1);
                }
                break;
            default:
                break;
        }
    }

    private void navigateToPaymentActivity(boolean packageSelected, int selectedPosition) {
        if (packageSelected) {
            if (PopUtils.checkInternetConnection(this)) {
                Intent intent = new Intent(this, TermsandconditionsActivity.class);
                intent.putExtra("amount", mPackagesModel.get(selectedPosition).amount + "");
                intent.putExtra("subjectId", subjectId + "");
                intent.putExtra("packageId", mPackagesModel.get(selectedPosition).packageId + "");
                intent.putExtra("from", "package");
                startActivity(intent);
            } else {
                PopUtils.alertDialog(this, "Please Check Internet Connection", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(PackagesActivity.this, HomeActivity.class));
                        finish();
                    }
                });
            }
        } else {
            if (PopUtils.checkInternetConnection(this)) {
                Intent intent = new Intent(this, TermsandconditionsActivity.class);
                intent.putExtra("amount", subjectAmount + "");
                intent.putExtra("subjectId", subjectId + "");
                intent.putExtra("packageId", "");
                intent.putExtra("from", "package");
                startActivity(intent);
            } else {
                PopUtils.alertDialog(this, "Please Check Internet Connection", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(PackagesActivity.this, HomeActivity.class));
                        finish();
                    }
                });
            }
        }
    }

    @Override
    public void ErrorResponse(VolleyError volleyError, int requestCode) {
        hideLoadingDialog();
        Utility.showSettingDialog(this,
                this.getResources().getString(R.string.some_thing_went_wrong),
                this.getResources().getString(R.string.error), Constants.SERVER_ERROR).show();
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        hideLoadingDialog();
        switch (requestCode) {
            case Constants.SERVICE_PACKAGES:
                responseForPackages(response);
                break;
            default:
                break;
        }
    }

    private void responseForPackages(String response) {
        if (response != null) {
            try {
                JSONObject mJsonObject = new JSONObject(response);
                if (mJsonObject.optString("status").equalsIgnoreCase("200")) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mTxtContent.setVisibility(View.GONE);

                    mPackagesModel.clear();

                    JSONArray mDataArray = mJsonObject.getJSONArray("data");
                    for (int i = 0; i < mDataArray.length(); i++) {
                        JSONObject mDataObject = mDataArray.getJSONObject(i);

                        JSONArray mSubjectsArray = mDataObject.getJSONArray("subjects");
                        mSubjectsModel.clear();
                        for (int j = 0; j < mSubjectsArray.length(); j++) {
                            JSONObject mSubjectsObject = mSubjectsArray.getJSONObject(j);

                            mSubjectsModel.add(new SubjectsModel(
                                    mSubjectsObject.optString("subject_id"),
                                    mSubjectsObject.optString("subject"),
                                    mSubjectsObject.optString("amount"),
                                    mSubjectsObject.optString("description")));
                        }

                        mPackagesModel.add(new PackagesModel(
                                mDataObject.optString("pkg_id"),
                                mDataObject.optString("pkg_name"),
                                mDataObject.optString("description"),
                                mDataObject.optString("amount"),
                                mDataObject.optString("created_on"),
                                mDataObject.optString("updated_on"),
                                new ArrayList(mSubjectsModel)));
                    }

                    setAdapter(mPackagesModel);
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    mTxtContent.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setAdapter(ArrayList<PackagesModel> mPackagesModel) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mPackagesAdapter = new PackagesAdapter(this, mPackagesModel);
        mRecyclerView.setAdapter(mPackagesAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnItemTouchListener(new PackagesAdapter(this, new PackagesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        }));
    }
}
