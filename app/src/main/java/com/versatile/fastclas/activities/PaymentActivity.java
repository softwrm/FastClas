package com.versatile.fastclas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;
import com.versatile.fastclas.BaseActivity;
import com.versatile.fastclas.interfaces.IParseListener;
import com.versatile.fastclas.payment.AppEnvironment;
import com.versatile.fastclas.utils.Constants;
import com.versatile.fastclas.utils.PopUtils;
import com.versatile.fastclas.utils.ServerResponse;
import com.versatile.fastclas.utils.Utility;
import com.versatilemobitech.fastclas.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class PaymentActivity extends BaseActivity implements IParseListener {

    PayUmoneySdkInitializer.PaymentParam mPaymentParams;
    public static int selectedTheme = R.style.AppTheme_Green;
    String amountVal, subjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        amountVal = intent.getStringExtra("amount");
        subjectId = intent.getStringExtra("subjectId");
        launchPayUMoneyFlow();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction
                    PayUmoneyFlowManager.logoutUser(getApplicationContext());


                } else {
                    //Failure Transaction
                    PayUmoneyFlowManager.logoutUser(getApplicationContext());
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();
                Utility.showLog("payuResponse", "" + payuResponse);

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();
                Utility.showLog("merchantResponse", "" + merchantResponse);

                callServiceForPaymentStatus(payuResponse);
//                new AlertDialog.Builder(this)
//                        .setCancelable(false)
//                        .setMessage("Payu's Data : " + payuResponse + "\n\n\n Merchant's Data: " + merchantResponse)
//                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                dialog.dismiss();
//                            }
//                        }).show();

            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d("", "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d("", "Both objects are null!");
            }
        }
    }

    private void callServiceForPaymentStatus(String payuResponse) {

        try {
            JSONObject jsonObject = new JSONObject(payuResponse);
            JSONObject jsonObjectResult = jsonObject.optJSONObject("result");
            String status = jsonObjectResult.optString("status");
            long paymentId = jsonObjectResult.optLong("paymentId");
            String txnid = jsonObjectResult.optString("txnid");
            String amount = jsonObjectResult.optString("amount");

            if (PopUtils.checkInternetConnection(this)) {
                JSONObject jsonObjectInsert = new JSONObject();
                jsonObjectInsert.put("action", "payment");
                jsonObjectInsert.put("user_id", Utility.getSharedPreference(this, Constants.USER_ID));
                jsonObjectInsert.put("subject_id", "" + subjectId);
                jsonObjectInsert.put("paymentid", "" + paymentId);
                jsonObjectInsert.put("txnid", "" + txnid);
                jsonObjectInsert.put("amount", "" + amount);
                if (status.equals("success")) {
                    jsonObjectInsert.put("status", "1");
                } else {
                    jsonObjectInsert.put("status", "0");
                }
                showLoadingDialog("Loading...", false);
                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequest(this, Constants.BASE_URL, jsonObjectInsert, this, Constants.SERVICE_PAYMENT);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
//        payNowButton.setEnabled(true);
    }

    private void launchPayUMoneyFlow() {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

        //Use this to set your custom text on result screen button
        payUmoneyConfig.setDoneButtonText("Payment Status");

        //Use this to set your custom title for the activity
        payUmoneyConfig.setPayUmoneyActivityTitle("Pay U Money");

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        double amount = 1;
        try {
            amount = Double.parseDouble(amountVal);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String txnId = System.currentTimeMillis() + "";
        String phone = Utility.getSharedPreference(this, Constants.MOBILE);
        String productName = "EEE";
        String firstName = Utility.getSharedPreference(this, Constants.FNAME);
        String email = Utility.getSharedPreference(this, Constants.EMAIL);
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";

        AppEnvironment appEnvironment = AppEnvironment.PRODUCTION;
        builder.setAmount(amount)
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(appEnvironment.surl())
                .setfUrl(appEnvironment.furl())
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(false)
                .setKey(appEnvironment.merchant_Key())
                .setMerchantId(appEnvironment.merchant_ID());

        try {
            mPaymentParams = builder.build();

            /*
            * Hash should always be generated from your server side.
            * */
//            generateHashFromServer(mPaymentParams);
            calculateServerSideHashAndInitiatePayment1(mPaymentParams);

           /* if (AppPreference.selectedTheme != -1) {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, MainActivity.this, selectedTheme, mAppPreference.isOverrideResultScreen());
            } else {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, MainActivity.this, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
            }*/

            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PaymentActivity.this, selectedTheme, false);


/*            *//**
             * Do not use below code when going live
             * Below code is provided to generate hash from sdk.
             * It is recommended to generate hash from server side only.
             * *//*
            mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);

           if (AppPreference.selectedTheme != -1) {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,MainActivity.this, AppPreference.selectedTheme,mAppPreference.isOverrideResultScreen());
            } else {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,MainActivity.this, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
            }*/

        } catch (Exception e) {
            // some exception occurred
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
//            payNowButton.setEnabled(true);
        }
    }

    /**
     * Thus function calculates the hash for transaction
     *
     * @param paymentParam payment params of transaction
     * @return payment params along with calculated merchant hash
     */
    private PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment1(final PayUmoneySdkInitializer.PaymentParam paymentParam) {

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");

        AppEnvironment appEnvironment = AppEnvironment.PRODUCTION;
        stringBuilder.append(appEnvironment.salt());

        String hash = hashCal(stringBuilder.toString());
        paymentParam.setMerchantHash(hash);

        return paymentParam;
    }

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }

    @Override
    public void ErrorResponse(VolleyError volleyError, int requestCode) {
        if (requestCode == Constants.SERVICE_PAYMENT) {
            hideLoadingDialog();
            Utility.showLog("Error", "" + volleyError);
            this.finish();
        }
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        if (requestCode == Constants.SERVICE_PAYMENT) {
            hideLoadingDialog();
            JSONObject jsonObject = new JSONObject();
            String message = jsonObject.optString("message");
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
