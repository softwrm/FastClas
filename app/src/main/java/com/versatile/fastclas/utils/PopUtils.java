package com.versatile.fastclas.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.versatile.fastclas.activities.HomeActivity;
import com.versatilemobitech.fastclas.R;
import com.versatile.fastclas.activities.RegisterActivity;
import com.versatile.fastclas.interfaces.ReturnValue;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Excentd11 on 11/18/2016.
 */

public class PopUtils {
    private static Dialog dialog;

    public static void alertDialog(final Context mContext, String message, final View.OnClickListener okClick) {
        TextView mTxtOk, mTxtMessage;
        final Dialog dialog = new Dialog(mContext, R.style.AlertDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = LayoutInflater.from(mContext).inflate(R.layout.alert_dialog, null);
        mTxtOk = (TextView) v.findViewById(R.id.txtOk);
        mTxtMessage = (TextView) v.findViewById(R.id.txtMessage);

        dialog.getWindow().getAttributes().windowAnimations = R.style.AlertDialogCustom;
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        mTxtMessage.setText(message);
        mTxtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (okClick != null) {
                    okClick.onClick(v);
                }
            }
        });

        dialog.setContentView(v);
        dialog.setCancelable(false);

        int width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (mContext.getResources().getDisplayMetrics().heightPixels * 0.30);
        dialog.getWindow().setLayout(width, lp.height);

        dialog.show();
    }

    public static void forgotPasswordDialog(final Context mContext, final View.OnClickListener submitClick) {
        Button mBtnSubmit;
        final EditText mEdtEmailId;
        final ImageView mImgCross;
        final Dialog dialog = new Dialog(mContext, R.style.AlertDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = LayoutInflater.from(mContext).inflate(R.layout.alert_forgot_password, null);
        mBtnSubmit = (Button) v.findViewById(R.id.btnSubmit);
        mEdtEmailId = (EditText) v.findViewById(R.id.edtEmailid);
        mImgCross = (ImageView) v.findViewById(R.id.imgCross);
        dialog.getWindow().getAttributes().windowAnimations = R.style.AlertDialogCustom;
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEdtEmailId.getText().toString().trim().length() == 0) {
                    PopUtils.alertDialog(mContext, "Please Enter  Email", null);
                } else if (!emailValidator(mEdtEmailId.getText().toString().trim())) {
                    PopUtils.alertDialog(mContext, "Please Enter Valid Email Id", null);
                } else {
                    dialog.dismiss();
                    if (submitClick != null) {
                        v.setTag(mEdtEmailId.getText().toString().trim());
                        submitClick.onClick(v);
                    }
                }
            }
        });
        mImgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(v);
        dialog.setCancelable(false);

        int width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (mContext.getResources().getDisplayMetrics().heightPixels * 0.30);
        dialog.getWindow().setLayout(width, lp.height);

        dialog.show();
    }

    public static void otpPasswordDialog(final Context mContext, final View.OnClickListener submitClick, final View.OnClickListener sendOtpAgainClick) {
        Button mBtnSubmit;
        final EditText mEdtOtp;
        final TextView mTxtOtpagain;

        final Dialog dialog = new Dialog(mContext, R.style.AlertDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = LayoutInflater.from(mContext).inflate(R.layout.alert_otp_password, null);
        mBtnSubmit = (Button) v.findViewById(R.id.btnSubmit);
        mTxtOtpagain = (TextView) v.findViewById(R.id.txtOtpagain);
        mEdtOtp = (EditText) v.findViewById(R.id.edtOtp);

        dialog.getWindow().getAttributes().windowAnimations = R.style.AlertDialogCustom;
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEdtOtp.getText().toString().trim().length() == 0) {
                    PopUtils.alertDialog(mContext, "Please enter  OTP.", null);
                } else {
                    dialog.dismiss();
                    if (submitClick != null) {
                        v.setTag(mEdtOtp.getText().toString().trim());
                        submitClick.onClick(v);
                    }
                }
            }
        });

        mTxtOtpagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                sendOtpAgainClick.onClick(view);
            }
        });

        dialog.setContentView(v);
        dialog.setCancelable(true);

        int width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (mContext.getResources().getDisplayMetrics().heightPixels * 0.30);
        dialog.getWindow().setLayout(width, lp.height);

        dialog.show();
    }


    public static boolean emailValidator(CharSequence email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager _connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean _isConnected = false;
        NetworkInfo _activeNetwork = _connManager.getActiveNetworkInfo();
        if (_activeNetwork != null) {
            _isConnected = _activeNetwork.isConnectedOrConnecting();
        }
        return _isConnected;
    }


    public static Dialog exitDialog(final Context mContext, String message, final View.OnClickListener yesClick) {

        TextView mTxtMessage;
        TextView mBtnYes, mBtnNo;
        dialog = new Dialog(mContext, R.style.AlertDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = LayoutInflater.from(mContext).inflate(R.layout.alert_two_button, null);
        mBtnYes = (TextView) v.findViewById(R.id.btnYes);
        mBtnNo = (TextView) v.findViewById(R.id.btnNo);
        mTxtMessage = (TextView) v.findViewById(R.id.txtMessage);
        dialog.getWindow().getAttributes().windowAnimations = R.style.AlertDialogCustom;
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mTxtMessage.setText(message);
        mBtnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.navigationView.getMenu().getItem(6).setChecked(false);
                dialog.dismiss();
            }
        });
        mBtnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.navigationView.getMenu().getItem(6).setChecked(false);
                if (yesClick != null) {
                    yesClick.onClick(v);
                }
            }
        });
        dialog.setContentView(v);
        dialog.setCancelable(false);
        int width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (mContext.getResources().getDisplayMetrics().heightPixels * 0.30);
        dialog.getWindow().setLayout(width, lp.height);
        dialog.show();
        return dialog;
    }

    public static void showListItems(Context context, ArrayList<String> values, final EditText mEditText, String title, final ReturnValue returnValue) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_gender, null);
        alertDialog.setView(view);
        alertDialog.setCancelable(true);
        //alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationexit;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, values);
        TextView tv_dialogtitle = (TextView) view.findViewById(R.id.txtGender);
        tv_dialogtitle.setText(title);
        ListView listItem = (ListView) view.findViewById(R.id.lv_genderselect);
        listItem.setAdapter(adapter);
        listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str_gender = parent.getItemAtPosition(position).toString();
                mEditText.setText(str_gender);
                returnValue.returnValues(str_gender, position);
                alertDialog.dismiss();

            }
        });
        alertDialog.show();
    }
}

