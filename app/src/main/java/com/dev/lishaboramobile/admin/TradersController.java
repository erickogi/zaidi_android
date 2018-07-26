package com.dev.lishaboramobile.admin;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.dev.lishaboramobile.Global.Models.ResponseModel;
import com.dev.lishaboramobile.Global.Network.ApiConstants;
import com.dev.lishaboramobile.Global.Network.Request;
import com.dev.lishaboramobile.Global.Network.RequestListener;
import com.dev.lishaboramobile.Global.Utils.MyToast;
import com.dev.lishaboramobile.Global.Utils.ResponseCallback;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;

public class TradersController {
    public int ISCREATE = 12;
    public int ISUPDATE = 13;

    private static int spalsh_time_out = 1000;
    ResponseModel responseModel = new ResponseModel();
    Gson gson = new Gson();

    private Context context;
    private AVLoadingIndicatorView avi;
    private AlertDialog dialog;

    private void handler() {
        new Handler().postDelayed(() -> {

                }


                , spalsh_time_out);

    }

    public TradersController(Context context) {
        this.context = context;
    }

    boolean isValidPhoneNumber(String mobile) {
        StringBuilder sb = new StringBuilder(mobile);

        if (sb.toString().startsWith("0")) {
            sb.deleteCharAt(0);
        }

        Log.d("enteredPhone", mobile);
        String regEx = "^[0-9]{9}$";
        return mobile.matches(regEx);
    }




    public void startAnim() {
        if (avi != null) {
            avi.setVisibility(View.VISIBLE);
            avi.show();
        }

    }

    public void dismissDialog() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }


    //UpdateList


    //View Actions

    public void stopAnim() {
        if (avi != null) {
            avi.setVisibility(View.GONE);
            avi.hide();
        }
        // or avi.smoothToHide();
    }

    public void snack(String msg) {
        if (context != null) {
            MyToast.toast(msg, context, R.drawable.ic_launcher, Toast.LENGTH_LONG);

            Log.d("SnackMessage", msg);
        }
    }

    public void getResponse(String url, JSONObject jsonObject, String token, ResponseCallback responseCallback) {
        Request.Companion.postRequest(url, jsonObject, token, new RequestListener() {
            @Override
            public void onError(@NotNull ANError error) {
                responseModel.setData(null);
                responseModel.setResultCode(0);
                responseModel.setResultDescription(error.getErrorBody());

                responseCallback.response(responseModel);

            }

            @Override
            public void onError(@NotNull String error) {
                responseModel.setData(null);
                responseModel.setResultCode(0);
                responseModel.setResultDescription(error);

                responseCallback.response(responseModel);

            }

            @Override
            public void onSuccess(@NotNull String response) {
                try {


                    Gson gson = new Gson();
                    responseModel = gson.fromJson(response, ResponseModel.class);
                    Log.d("2ReTrRe", gson.toJson(responseModel));

                    responseCallback.response(responseModel);


                } catch (Exception e) {
                    responseModel.setData(null);
                    responseModel.setResultCode(0);
                    responseModel.setResultDescription(e.getMessage());
                    responseCallback.response(responseModel);

                    e.printStackTrace();
                }
            }
        });

    }

    public ResponseModel getTraders(int del, int archive, int dummy, int synched, ResponseCallback responseCallback) {

        HashMap<String, String> params = new HashMap<>();
        params.put("deleted", "" + del);
        params.put("archived", "" + archive);
        params.put("synced", "" + synched);
        params.put("dummy", "" + dummy);
        Request.Companion.postRequest(ApiConstants.Companion.getTraders(), params, "", new RequestListener() {
            @Override
            public void onError(@NotNull ANError error) {

                responseModel.setData(null);
                responseModel.setResultCode(0);
                responseModel.setResultDescription(error.getErrorBody());

                responseCallback.response(responseModel);


            }

            @Override
            public void onError(@NotNull String error) {

            }

            @Override
            public void onSuccess(@NotNull String response) {

                try {

                    Gson gson = new Gson();
                    responseModel = gson.fromJson(response, ResponseModel.class);
                    responseCallback.response(responseModel);


                } catch (Exception e) {
                    responseModel.setData(null);
                    responseModel.setResultCode(0);
                    responseModel.setResultDescription(e.getMessage());
                    responseCallback.response(responseModel);

                    e.printStackTrace();
                }
            }
        });


        return responseModel;
    }

    //Live



}
