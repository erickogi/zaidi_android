package com.dev.lishabora.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import timber.log.Timber;


/**
 * Created by Eric on 10/8/2017.
 */

public class SmsReceiver extends BroadcastReceiver {
    public static final String SMS_ORIGIN = "AFRICASTKNG";
    public static final String OTP_DELIMITER = "#";
    private static final String TAG = "NKJJK";

    @Override
    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    String senderAddress = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    Timber.e("Received SMS: " + message + ", Sender: " + senderAddress);

                    // if the SMS is not from our gateway, ignore the message
                    if (!senderAddress.toLowerCase().contains(SMS_ORIGIN.toLowerCase())) {
                        return;
                    }

                    // verification code from sms
                    String verificationCode = getVerificationCode(message);

                    Timber.e("OTP received: " + verificationCode);
                    Intent intent2 = new Intent("com.lisha.codereceived");
                    intent2.putExtra("code", verificationCode);
                    context.sendBroadcast(intent2);

                    //ssendBroadcast(intent);
                }
            }
        } catch (Exception e) {
            Timber.e("Exception: " + e.getMessage());
        }
    }

    /**
     * Getting the OTP from sms message body
     * ':' is the separator of OTP from the message
     *
     * @param message
     * @return
     */
    private String getVerificationCode(String message) {
        String code = null;
        int index = message.indexOf(OTP_DELIMITER);

        if (index != -1) {
            int start = index + 1;
            int length = 4;
            code = message.substring(start, start + length);
            return code;
        }

        return code;
    }
}
