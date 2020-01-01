package com.dev.lishabora.UseCases

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.Telephony

class SendSmsUseCase {
    companion object{
         fun sendSMS(phone: String,activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            // At least KitKat
            {
                val defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(activity.applicationContext) // Need to change the build to API 19

                val sendIntent = Intent(Intent.ACTION_SEND)
                sendIntent.type = "text/plain"
                sendIntent.putExtra(Intent.EXTRA_TEXT, "text")

                if (defaultSmsPackageName != null) {
                    sendIntent.setPackage(defaultSmsPackageName)
                }
                activity.startActivity(sendIntent)

            } else {
                val smsIntent = Intent(Intent.ACTION_VIEW)
                smsIntent.type = "vnd.android-dir/mms-sms"
                smsIntent.putExtra("address", "0" +phone)
                smsIntent.putExtra("sms_body", "message")
                activity.startActivity(smsIntent)
            }
        }

    }
}