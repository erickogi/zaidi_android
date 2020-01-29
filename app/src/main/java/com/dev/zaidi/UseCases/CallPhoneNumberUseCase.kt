package com.dev.zaidi.UseCases

import android.content.Context
import android.content.Intent
import android.net.Uri

class CallPhoneNumberUseCase {
    companion object{
        fun call(phoneNumber: String,context: Context){
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "0" + phoneNumber))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

        }
    }
}