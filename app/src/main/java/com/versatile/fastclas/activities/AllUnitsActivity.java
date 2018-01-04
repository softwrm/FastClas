package com.versatile.fastclas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.versatile.fastclas.BaseActivity;
import com.versatilemobitech.fastclas.R;
import com.versatile.fastclas.adapters.AllUnitsAdapter;
import com.versatile.fastclas.interfaces.IParseListener;
import com.versatile.fastclas.models.AllUnitsModel;
import com.versatile.fastclas.utils.Constants;
import com.versatile.fastclas.utils.PopUtils;
import com.versatile.fastclas.utils.ServerResponse;
import com.versatile.fastclas.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllUnitsActivity extends BaseActivity implements View.OnClickListener, AllUnitsAdapter.OnItemClickListener, IParseListener {

    private static final String TAG = AllUnitsActivity.class.getSimpleName();
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView txtToolbar, mTxtHeading, txtxNoDataFound;
    private ImageView mImgBack;
    private String id, subject_name;
    ArrayList<AllUnitsModel> allUnitsModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_units);

        initViews();
    }

    private void initViews() {
        setReferences();
        setClickListeners();

    }

    private void setReferences() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtToolbar = (TextView) findViewById(R.id.txtToolbar);
        mTxtHeading = (TextView) findViewById(R.id.txtHeading);
        txtxNoDataFound = (TextView) findViewById(R.id.txtxNoDataFound);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mImgBack = (ImageView) findViewById(R.id.imgBack);

        id = getIntent().getStringExtra("id");
        subject_name = getIntent().getStringExtra("subject_name");

        mTxtHeading.setText(subject_name);

        callServiceForUnits();
    }

    private void callServiceForUnits() {
        if (PopUtils.checkInternetConnection(this)) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("action", "units");
                jsonObject.put("user_id", Utility.getSharedPreference(this, Constants.USER_ID));
                jsonObject.put("subject_id", id);

                showLoadingDialog("Loading...", false);

                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.SERVICE_UNITS);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            PopUtils.alertDialog(this, getString(R.string.pls_check_internet), null);
        }
    }


    private void setClickListeners() {
        mImgBack.setOnClickListener(this);
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
    public void onItemClick(AllUnitsModel allUnitsModel, int Position) {
        Intent intent = new Intent(this, SessionsActivity.class);
        intent.putExtra("label", allUnitsModel.getUnitNumber());
        intent.putExtra("heading", allUnitsModel.getUnitTitle());
        intent.putExtra("unitId", allUnitsModel.getUnitId());
//        intent.putExtra("noOfPages",allUnitsModel.getUnitId())
        navigateActivity(intent, false);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void ErrorResponse(VolleyError volleyError, int requestCode) {
        if (requestCode == Constants.SERVICE_UNITS) {
            hideLoadingDialog();
        }
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        if (requestCode == Constants.SERVICE_UNITS) {
            hideLoadingDialog();
            try {
                allUnitsModelArrayList.clear();
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");

                if (status.equals("200")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectData = jsonArray.getJSONObject(i);

                        String unit_id = jsonObjectData.optString("unit_id");
                        String unit_number = jsonObjectData.optString("unit_number");
                        String unit_title = jsonObjectData.optString("unit_title");
                        String description = jsonObjectData.optString("description");

                        AllUnitsModel allUnitsModel = new AllUnitsModel();
                        allUnitsModel.setDescription(description);
                        allUnitsModel.setUnitId(unit_id);
                        allUnitsModel.setUnitNumber("Unit - " + unit_number);
                        allUnitsModel.setUnitTitle(unit_title);

                        allUnitsModelArrayList.add(allUnitsModel);

                    }

                    AllUnitsAdapter allUnitsAdapter = new AllUnitsAdapter(this, allUnitsModelArrayList, this);
                    recyclerView.setAdapter(allUnitsAdapter);
                } else {
                    txtxNoDataFound.setText("No Unit's Found");
                    txtxNoDataFound.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
