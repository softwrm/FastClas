package com.versatile.fastclas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.versatile.fastclas.BaseActivity;
import com.versatile.fastclas.interfaces.IParseListener;
import com.versatile.fastclas.utils.Constants;
import com.versatile.fastclas.utils.PopUtils;
import com.versatile.fastclas.utils.ServerResponse;
import com.otelpt.fastclas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TermsandconditionsActivity extends BaseActivity implements View.OnClickListener, IParseListener {
    private TextView mTitle;
    private ImageView mImgBack;
    CheckBox mCheckBoxTerms;
    private TextView txtTerms, mTxtProceed;

    private String amount = "", subjectId = "", packageId = "", from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termsandconditions);

//        HomeActivity.navigationView.getMenu().getItem(5).setChecked(false);
        initViews();
    }

    private void initViews() {
        setReferences();
        getBundleData();
        setClickListeners();
    }

    private void setReferences() {
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mTitle = (TextView) findViewById(R.id.txtToolbar);
        txtTerms = (TextView) findViewById(R.id.txtTerms);
        mCheckBoxTerms = (CheckBox) findViewById(R.id.checkBoxTerms);
        mTitle.setText("TERMS & CONDITIONS ");

        mTxtProceed = (TextView) findViewById(R.id.txtProceed);

        callWebServiceForTermsnConditions();
    }

    private void getBundleData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("amount")) {
                amount = intent.getStringExtra("amount");
            }
            if (intent.hasExtra("subjectId")) {
                subjectId = intent.getStringExtra("subjectId");
            }
            if (intent.hasExtra("packageId")) {
                packageId = intent.getStringExtra("packageId");
            }
            if (intent.hasExtra("from")) {
                from = intent.getStringExtra("from");
            }

            if (!from.equalsIgnoreCase("")) {
                mTxtProceed.setVisibility(View.VISIBLE);
                mCheckBoxTerms.setVisibility(View.VISIBLE);
            } else {
                mCheckBoxTerms.setVisibility(View.GONE);
                mTxtProceed.setVisibility(View.GONE);
            }
        }
    }

    private void callWebServiceForTermsnConditions() {
        if (PopUtils.checkInternetConnection(this)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("action", "terms");
                showLoadingDialog("Loading...", false);
                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObject, this, Constants.SERVICE_TERMSNCONDITIONS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            PopUtils.alertDialog(this, getString(R.string.pls_check_internet), null);
        }
    }

    private void setClickListeners() {
        mImgBack.setOnClickListener(this);
        mTxtProceed.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.txtProceed:
                if (mCheckBoxTerms.isChecked()) {
                    Intent intent = new Intent(this, PaymentActivity.class);
                    intent.putExtra("amount", amount + "");
                    intent.putExtra("subjectId", subjectId + "");
                    intent.putExtra("packageId", packageId + "");
                    startActivity(intent);
                } else {
                    PopUtils.alertDialog(this, "Please Select Terms & Conditions for Payment", null);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void ErrorResponse(VolleyError volleyError, int requestCode) {
        if (requestCode == Constants.SERVICE_TERMSNCONDITIONS) {
            hideLoadingDialog();
            PopUtils.alertDialog(this, "Please Check Internet Connection", null);
        }
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        if (requestCode == Constants.SERVICE_TERMSNCONDITIONS) {
            hideLoadingDialog();
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");

                JSONArray jsonArray = jsonObject.optJSONArray("data");
                if (status.equals("200")) {
                    JSONObject jsonObjectData = jsonArray.optJSONObject(0);
                    String terms_and_conditions = jsonObjectData.optString("terms_and_conditions");
                    txtTerms.setText(terms_and_conditions);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
