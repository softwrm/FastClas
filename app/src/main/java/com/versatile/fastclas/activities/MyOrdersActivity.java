package com.versatile.fastclas.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.versatile.fastclas.adapters.HomeAdapter;
import com.versatile.fastclas.interfaces.IParseListener;
import com.versatile.fastclas.models.SubjectModel;
import com.versatile.fastclas.utils.Constants;
import com.versatile.fastclas.utils.ServerResponse;
import com.versatile.fastclas.utils.Utility;
import com.otelpt.fastclas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyOrdersActivity extends AppCompatActivity implements IParseListener, View.OnClickListener, HomeAdapter.OnItemClickListener {

    RecyclerView myOrdersRecyclerView;
    TextView txtNoDataFound;
    ArrayList<SubjectModel> subjectModelArrayList = new ArrayList<>();
    TextView mTitle;
    ImageView mImgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        HomeActivity.navigationView.getMenu().getItem(3).setChecked(false);

        myOrdersRecyclerView = findViewById(R.id.myOrdersRecyclerView);
        myOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myOrdersRecyclerView.setHasFixedSize(true);
        txtNoDataFound = findViewById(R.id.txtNoDataFound);

        mImgBack = findViewById(R.id.imgBack);
        mTitle = findViewById(R.id.txtToolbar);
        mTitle.setText(getResources().getString(R.string.order));

        mImgBack.setOnClickListener(this);

        callWebServiceforMyOrders();
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
        startActivity(new Intent(this, HomeActivity.class));
    }

    private void callWebServiceforMyOrders() {

        JSONObject jsonObject = new JSONObject();
        try {
            Utility.showLoadingDialog(this, "Loading...", false);
            jsonObject.put("action", "orders");
            jsonObject.put("user_id", "" + Utility.getSharedPreference(this, Constants.USER_ID));

            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.SERVICE_MYORDERS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ErrorResponse(VolleyError volleyError, int requestCode) {
        if (requestCode == Constants.SERVICE_MYORDERS) {
            Utility.hideLoadingDialog();
            Utility.showSettingDialog(this,
                    this.getResources().getString(R.string.some_thing_went_wrong),
                    this.getResources().getString(R.string.error), Constants.SERVER_ERROR).show();
        }
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        if (requestCode == Constants.SERVICE_MYORDERS) {
            Utility.hideLoadingDialog();
            subjectModelArrayList.clear();
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status");
                int count = jsonObject.optInt("count");


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
                    myOrdersRecyclerView.setAdapter(homeAdapter);
                } else {
                    txtNoDataFound.setText("No Orders Found");
                    txtNoDataFound.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                Utility.showLog("Error", "" + e);
            }
        }
    }

    @Override
    public void onItemClick(SubjectModel subjectModel, int position) {
        Intent intent = new Intent(this, AllUnitsActivity.class);
        intent.putExtra("id", subjectModel.getId());
        intent.putExtra("subject_name", subjectModel.getSubjectName());
        intent.putExtra("amount", "" + subjectModel.getAmount());
        intent.putExtra("payment_status", "" + subjectModel.getPaymentStatus());
        intent.putExtra("from", "MyOrdersActivity");
        startActivity(intent);
    }
}
