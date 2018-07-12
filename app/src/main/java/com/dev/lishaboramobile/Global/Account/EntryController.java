package com.dev.lishaboramobile.Global.Account;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.error.ANError;
import com.dev.lishaboramobile.Global.Network.ApiConstants;
import com.dev.lishaboramobile.Global.Network.Request;
import com.dev.lishaboramobile.Global.Network.RequestListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EntryController {

    private Context context;

    EntryController(Context context) {
        this.context = context;
    }

    boolean isValidPhoneNumber(String mobile) {
        Log.d("enteredPhone", mobile);
        String regEx = "^[0-9]{9}$";
        return mobile.matches(regEx);
    }

    boolean isValidPassword(String mobile) {
        Log.d("enteredPhone", mobile);
        String regEx = "^[0-9]{9}$";
        return mobile.matches(regEx);
    }

    void authPhone(String phone, EntryCallbacks entryCallbacks) {
        HashMap<String, String> params = new HashMap<>();
        params.put("Phone", phone);
        Request.Companion.postRequest(ApiConstants.Companion.getPhoneAuth(), params, "", new RequestListener() {
            @Override
            public void onError(@NotNull ANError error) {
                entryCallbacks.updateProgressDialog("");
                entryCallbacks.stopProgressDialog();
                entryCallbacks.error(error.getErrorBody());
            }

            @Override
            public void onError(@NotNull String error) {

            }

            @Override
            public void onSuccess(@NotNull String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.optInt(ApiConstants.Companion.getResultCode()) > 0) {


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
