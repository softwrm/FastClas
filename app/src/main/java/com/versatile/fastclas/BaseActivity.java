package com.versatile.fastclas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    public String[] inspectionFormArray = new String[139];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void navigateActivity(Intent intent, boolean finish) {
        startActivity(intent);
        if (finish) {
            finish();
        }
    }

    public void navigateActivityBack(Intent intent, boolean finish) {
        startActivity(intent);
//        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
        if (finish) {
            finish();
        }
    }

    /**
     * Method to show dialog with given message
     *
     * @param title        dialog heading
     * @param isCancelable whether dialog is cancellable or not
     */
    public void showLoadingDialog(final String title, final boolean isCancelable) {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                return;
            }
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(title);
            progressDialog.setCancelable(isCancelable);
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hides loading dialog if shown
     */
    public void hideLoadingDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
            progressDialog = null;
        } catch (Exception e) {
            progressDialog = null;
        }
    }
}
