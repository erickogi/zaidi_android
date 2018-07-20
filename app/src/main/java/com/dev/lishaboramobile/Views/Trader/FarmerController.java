package com.dev.lishaboramobile.Views.Trader;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.dev.lishaboramobile.Admin.Callbacks.CreateTraderCallbacks;
import com.dev.lishaboramobile.Global.Models.ResponseModel;
import com.dev.lishaboramobile.Global.Network.Request;
import com.dev.lishaboramobile.Global.Network.RequestListener;
import com.dev.lishaboramobile.Global.Utils.MyToast;
import com.dev.lishaboramobile.Global.Utils.ResponseCallback;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class FarmerController {

    private static int spalsh_time_out = 1000;
    ResponseModel responseModel = new ResponseModel();
    Gson gson = new Gson();

    private Context context;
    private CreateTraderCallbacks createTraderCallbacks;
    private AVLoadingIndicatorView avi;
    private AlertDialog dialog;

    public FarmerController(Context context) {
        this.context = context;
    }

    private void handler() {
        new Handler().postDelayed(() -> {

                }


                , spalsh_time_out);

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


}
