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
import com.otelpt.fastclas.R;
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

    Toolbar toolbar;
    RecyclerView recyclerView;
    TextView txtToolbar, mTxtHeading, txtxNoDataFound;
    ImageView mImgBack;
    String id, subject_name, amount, payment_status, from;
    ArrayList<AllUnitsModel> allUnitsModelArrayList = new ArrayList<>();
    TextView txtPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_units);

        setReferences();
    }


    private void setReferences() {
        toolbar = findViewById(R.id.toolbar);
        txtToolbar = findViewById(R.id.txtToolbar);
        mTxtHeading = findViewById(R.id.txtHeading);
        txtxNoDataFound = findViewById(R.id.txtxNoDataFound);
        mImgBack = findViewById(R.id.imgBack);
        recyclerView = findViewById(R.id.recyclerView);
        txtPayment = findViewById(R.id.txtPayment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mImgBack.setOnClickListener(this);
        txtPayment.setOnClickListener(this);

        id = getIntent().getStringExtra("id");
        subject_name = getIntent().getStringExtra("subject_name");
        amount = getIntent().getStringExtra("amount");
        payment_status = getIntent().getStringExtra("payment_status");
        from = getIntent().getStringExtra("from");

        if (payment_status.equals("1")) {
            txtPayment.setVisibility(View.GONE);
        } else {
            //txtPayment.setVisibility(View.VISIBLE);
        }

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
            PopUtils.alertDialog(this, getString(R.string.pls_check_internet), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(AllUnitsActivity.this, HomeActivity.class));
                    finish();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.txtPayment:
                Intent intent = new Intent(this, PackagesActivity.class);
                intent.putExtra("amount", amount);
                intent.putExtra("subjectId", id);
                startActivity(intent);
               /* if (PopUtils.checkInternetConnection(this)) {
                    Intent intent = new Intent(this, PaymentActivity.class);
                    intent.putExtra("amount", amount);
                    intent.putExtra("subjectId", id);
                    startActivity(intent);
                } else {
                    PopUtils.alertDialog(this, "Please Check Internet Connection", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(AllUnitsActivity.this, HomeActivity.class));
                            finish();
                        }
                    });
                }*/
                break;
        }
    }

    @Override
    public void onItemClick(AllUnitsModel allUnitsModel, int Position) {
       /* if (Position != 0) {
            if (!payment_status.equals("0")) {
                Intent intent = new Intent(this, SessionsActivity.class);
                intent.putExtra("label", allUnitsModel.getUnitNumber());
                intent.putExtra("heading", allUnitsModel.getUnitTitle());
                intent.putExtra("unitId", allUnitsModel.getUnitId());
                intent.putExtra("payment_status", payment_status);

                intent.putExtra("subject_id", id);
                intent.putExtra("subject_name", subject_name);
                intent.putExtra("amount", amount);

                navigateActivity(intent, false);
            } else {
                PopUtils.alertDialog(this, "Please complete the payment procedure", null);
            }
        } else {
            Intent intent = new Intent(this, SessionsActivity.class);
            intent.putExtra("label", allUnitsModel.getUnitNumber());
            intent.putExtra("heading", allUnitsModel.getUnitTitle());
            intent.putExtra("unitId", allUnitsModel.getUnitId());
            intent.putExtra("payment_status", payment_status);
            navigateActivity(intent, false);
        }*/

        Intent intent = new Intent(this, SessionsActivity.class);
        intent.putExtra("label", allUnitsModel.getUnitNumber());
        intent.putExtra("heading", allUnitsModel.getUnitTitle());
        intent.putExtra("unitId", allUnitsModel.getUnitId());
        intent.putExtra("payment_status", payment_status);
        navigateActivity(intent, false);

    }

    @Override
    public void onBackPressed() {
        if (from.equals("MyOrdersActivity")) {
            startActivity(new Intent(this, MyOrdersActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    @Override
    public void ErrorResponse(VolleyError volleyError, int requestCode) {
        if (requestCode == Constants.SERVICE_UNITS) {
            hideLoadingDialog();
            Utility.showSettingDialog(this,
                    this.getResources().getString(R.string.some_thing_went_wrong),
                    this.getResources().getString(R.string.error), Constants.SERVER_ERROR).show();
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
//                String message = jsonObject.optString("message");

                if (status.equals("200")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectData = jsonArray.getJSONObject(i);

                        String unit_id = jsonObjectData.optString("unit_id");
                        String unit_number = jsonObjectData.optString("unit_number");
                        String unit_title = jsonObjectData.optString("unit_title");
                        String description = jsonObjectData.optString("description");
                        String due_date = jsonObjectData.optString("due_date");

                        AllUnitsModel allUnitsModel = new AllUnitsModel();
                        allUnitsModel.setDescription(description);
                        allUnitsModel.setUnitId(unit_id);
                        allUnitsModel.setUnitNumber("Unit - " + unit_number);
                        allUnitsModel.setUnitTitle(unit_title);

                        //2018-02-22

                        if (!Utility.isValueNullOrEmpty(due_date)) {
                            String dateVal[] = due_date.split("-");
                            String month = "Jan";
                            switch (dateVal[1]) {
                                case "01": {
                                    month = "Jan";
                                    break;
                                }
                                case "02": {
                                    month = "Feb";
                                    break;
                                }
                                case "03": {
                                    month = "Mar";
                                    break;
                                }
                                case "04": {
                                    month = "Aprl";
                                    break;
                                }
                                case "05": {
                                    month = "May";
                                    break;
                                }
                                case "06": {
                                    month = "June";
                                    break;
                                }

                                case "07": {
                                    month = "July";
                                    break;
                                }
                                case "08": {
                                    month = "Aug";
                                    break;
                                }
                                case "09": {
                                    month = "Sept";
                                    break;
                                }
                                case "10": {
                                    month = "Oct";
                                    break;
                                }
                                case "11": {
                                    month = "Nov";
                                    break;
                                }
                                case "12": {
                                    month = "Dec";
                                    break;
                                }
                            }
                            allUnitsModel.setDueDate(dateVal[2] + "-" + month + "-" + dateVal[0]);
                        } else {
                            allUnitsModel.setDueDate("");
                        }

//                        allUnitsModel.setDueDate(due_date);

                        allUnitsModelArrayList.add(allUnitsModel);

                    }

                    AllUnitsAdapter allUnitsAdapter = new AllUnitsAdapter(this, allUnitsModelArrayList, this);
                    recyclerView.setAdapter(allUnitsAdapter);
                } else {
                    txtxNoDataFound.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
