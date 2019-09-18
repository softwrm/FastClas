package com.versatile.fastclas.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.versatile.fastclas.interfaces.SmsListener;

/**
 * Created by USER on 04-10-2017.
 */

public class IncomingSms extends BroadcastReceiver {
    private static SmsListener mListener;
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObject = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObject.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[])
                            pdusObject[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String message = currentMessage.getDisplayMessageBody();

                    Log.e("Message ", "onReceive broadcast " + phoneNumber);
                    Log.e("Message ", "onReceive broadcat message :  " + message);

                    if (phoneNumber.contains("DM-VMTECH")) {

                        Log.e("Message ", "onReceive inside: " + message);

                        String digits = message.replaceAll("[^0-9.]", "");

                        Log.e("Message ", "digits: " + digits);

                        mListener.messageReceived(digits);
                    }else{
                        Log.e("Message ", "onReceive inside else: " + message);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
