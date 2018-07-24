package com.dev.lishaboramobile.Admin.Controllers;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.dev.lishaboramobile.Admin.Callbacks.CreateTraderCallbacks;
import com.dev.lishaboramobile.Admin.Callbacks.TraderCallbacks;
import com.dev.lishaboramobile.Admin.Data.AdminPrefs;
import com.dev.lishaboramobile.Global.Models.ResponseModel;
import com.dev.lishaboramobile.Global.Network.ApiConstants;
import com.dev.lishaboramobile.Global.Network.Request;
import com.dev.lishaboramobile.Global.Network.RequestListener;
import com.dev.lishaboramobile.Global.Utils.DateTimeUtils;
import com.dev.lishaboramobile.Global.Utils.GeneralUtills;
import com.dev.lishaboramobile.Global.Utils.MyToast;
import com.dev.lishaboramobile.Global.Utils.RequestDataCallback;
import com.dev.lishaboramobile.Global.Utils.ResponseCallback;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Trader.Models.TraderModel;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TradersController {
    public int ISCREATE = 12;
    public int ISUPDATE = 13;

    private static int spalsh_time_out = 1000;
    ResponseModel responseModel = new ResponseModel();
    Gson gson = new Gson();

    private Context context;
    private CreateTraderCallbacks createTraderCallbacks;
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

    public List<TraderModel> getTraderModels(int del, int archive, int dummy, int synched, TraderCallbacks traderCallbacks) {

        handler();

        HashMap<String, String> params = new HashMap<>();
        params.put("deleted", "" + del);
        params.put("archived", "" + archive);
        params.put("synced", "" + synched);
        params.put("dummy", "" + dummy);
        traderCallbacks.startProgressDialog();
        Request.Companion.postRequest(ApiConstants.Companion.getTraders(), params, "", new RequestListener() {
            @Override
            public void onError(@NotNull ANError error) {
                traderCallbacks.updateProgressDialog("");
                traderCallbacks.stopProgressDialog();
                traderCallbacks.error(error.getErrorBody());
            }

            @Override
            public void onError(@NotNull String error) {

            }

            @Override
            public void onSuccess(@NotNull String response) {

                traderCallbacks.updateProgressDialog(response);
                try {

                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response, ResponseModel.class);
                    traderCallbacks.stopProgressDialog();
                    traderCallbacks.success(responseModel);


                } catch (Exception e) {
                    traderCallbacks.error(e.getMessage());
                    traderCallbacks.stopProgressDialog();
                    e.printStackTrace();
                }
            }
        });


        return new ArrayList<>();
    }

    private boolean saveTrader(TraderModel traderModel, CreateTraderCallbacks createTraderCallbacks) {
        HashMap<String, String> params = new HashMap<>();

        params.put("id", "");
        params.put("code", traderModel.getCode());
        params.put("entity", "");
        params.put("entitycode", "");
        params.put("entityname", traderModel.getEntityname());
        params.put("transactioncode", "");
        params.put("names", traderModel.getNames());
        params.put("mobile", traderModel.getMobile());
        params.put("password", "");
        params.put("balance", "");
        params.put("apikey", "");
        params.put("firebasetoken", "");
        params.put("status", "");
        params.put("transactiontime", "");
        params.put("synctime", "");
        params.put("transactedby", "Admin");
        params.put("dummy", "" + traderModel.getDummy());

        createTraderCallbacks.startProgressDialog();
        Request.Companion.postRequest(ApiConstants.Companion.getCreateTrader(), params, "", new RequestListener() {
            @Override
            public void onError(@NotNull ANError error) {
                createTraderCallbacks.error(error.getMessage());
                // TradersViewModel viewModel=new TradersViewModel(context);
            }

            @Override
            public void onError(@NotNull String error) {
                createTraderCallbacks.error(error);
            }

            @Override
            public void onSuccess(@NotNull String response) {

                createTraderCallbacks.updateProgressDialog(response);

                try {

                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response, ResponseModel.class);
                    createTraderCallbacks.stopProgressDialog();
                    createTraderCallbacks.success(responseModel);


                } catch (Exception e) {
                    createTraderCallbacks.error(e.getMessage());
                    createTraderCallbacks.stopProgressDialog();
                    e.printStackTrace();
                }
            }
        });


        return true;
    }



    public void upTrader(TraderModel traderModel, TraderCallbacks traderCallbacks) {
        HashMap<String, String> params = new HashMap<>();
        //params.put("")
        params.put("id", "" + traderModel.getId());
        params.put("code", traderModel.getCode());
        params.put("entity", traderModel.getEntity());
        params.put("entitycode", traderModel.getEntitycode());
        params.put("transactioncode", traderModel.getTransactioncode());
        params.put("names", traderModel.getNames());
        params.put("mobile", traderModel.getMobile());
        params.put("password", traderModel.getPassword());
        params.put("balance", traderModel.getBalance());
        params.put("apikey", traderModel.getApikey());
        params.put("firebasetoken", traderModel.getFirebasetoken());
        params.put("status", traderModel.getStatus());
        params.put("transactiontime", traderModel.getTransactiontime());
        params.put("synctime", traderModel.getSynctime());
        params.put("transactedby", traderModel.getTransactedby());
        params.put("entityname", traderModel.getEntityname());
        params.put("dummy", "" + traderModel.getDummy());
        params.put("archived", "" + traderModel.getArchived());
        params.put("deleted", "" + traderModel.getDeleted());


        Request.Companion.postRequest(ApiConstants.Companion.getUpdateTrader(), params, "", new RequestListener() {
            @Override
            public void onError(@NotNull ANError error) {
                traderCallbacks.error(error.getMessage());
            }

            @Override
            public void onError(@NotNull String error) {

                traderCallbacks.error(error);
            }

            @Override
            public void onSuccess(@NotNull String response) {
                traderCallbacks.updateProgressDialog(response);
                try {

                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response, ResponseModel.class);
                    traderCallbacks.stopProgressDialog();
                    traderCallbacks.success(responseModel);


                } catch (Exception e) {
                    traderCallbacks.error(e.getMessage());
                    traderCallbacks.stopProgressDialog();
                    e.printStackTrace();
                }

            }
        });


    }

    public void createTrader(RequestDataCallback requestDataCallback) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_add_trader, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(context));
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
        alertDialogBuilderUserInput.setTitle("Trader");


        avi = mView.findViewById(R.id.avi);


        TextInputEditText edtNames, edtMobile;
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Save", (dialogBox, id) -> {
                    // ToDo get user input here


                })

                .setNegativeButton("Dismiss",
                        (dialogBox, id) -> dialogBox.cancel());

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.show();

        Button theButton = alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CustomListener(alertDialogAndroid, requestDataCallback));


    }

    public void editTrader(TraderModel traderModel, RequestDataCallback requestDataCallback) {
        if (context != null) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
            View mView = layoutInflaterAndroid.inflate(R.layout.dialog_add_trader, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(context));
            alertDialogBuilderUserInput.setView(mView);
            alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
            alertDialogBuilderUserInput.setTitle("Trader");


            avi = mView.findViewById(R.id.avi);


            TextInputEditText edtNames, edtMobile;
            edtMobile = mView.findViewById(R.id.edt_traders_phone);
            edtNames = mView.findViewById(R.id.edt_traders_names);

            edtMobile.setText(traderModel.getMobile());
            edtNames.setText(traderModel.getNames());

            CheckBox chk = mView.findViewById(R.id.chk_dummy);
            chk.setVisibility(View.GONE);


            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("Update", (dialogBox, id) -> {
                        // ToDo get user input here


                    })

                    .setNegativeButton("Dismiss",
                            (dialogBox, id) -> dialogBox.cancel());

            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.setCancelable(false);
            alertDialogAndroid.show();

            Button theButton = alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE);
            theButton.setOnClickListener(new EditCustomListener(alertDialogAndroid, traderModel, requestDataCallback));

        }

    }

    private class CustomListener implements View.OnClickListener {
        // AlertDialog dialog;
        boolean isDummy = false;
        //int type;
        RequestDataCallback requestDataCallback;

        public CustomListener(AlertDialog alertDialogAndroid, RequestDataCallback requestDataCallback) {
            dialog = alertDialogAndroid;
            this.requestDataCallback = requestDataCallback;
            // this.type=type;
        }

        @Override
        public void onClick(View v) {
            TextInputEditText name, phone;
            CheckBox chkDummy;

            name = dialog.findViewById(R.id.edt_traders_names);
            phone = dialog.findViewById(R.id.edt_traders_phone);
            chkDummy = dialog.findViewById(R.id.chk_dummy);


            if (name.getText().toString().isEmpty()) {
                name.setError("Required");
                name.requestFocus();
                stopAnim();
                return;
            }


            if (phone.getText().toString().isEmpty()) {
                phone.setError("Required");
                phone.requestFocus();
                stopAnim();
                return;
            }

            if (!isValidPhoneNumber(phone.getText().toString())) {
                stopAnim();
                snack("Invalid phone number ");
                phone.requestFocus();
                phone.setError("Invalid Phone number");
                return;
            }


            if (chkDummy.isChecked()) {
                isDummy = true;
            }


            AdminPrefs adminPrefs = new AdminPrefs(context);
            TraderModel traderModel = new TraderModel();
            //if(type==ISCREATE) {
            traderModel.setId(0);
            traderModel.setCode("" + new GeneralUtills(context).getRandon(9000, 1000));
            traderModel.setEntity("Admin");
            traderModel.setEntityname("LishaBora");
            traderModel.setEntitycode(adminPrefs.getAdmin().getCode());

            traderModel.setTransactioncode(DateTimeUtils.Companion.getNow());
            traderModel.setNames(name.getText().toString());
            traderModel.setMobile(phone.getText().toString());
            traderModel.setPassword("password");
            traderModel.setBalance("0");
            traderModel.setApikey("");
            traderModel.setFirebasetoken("");
            traderModel.setStatus("Active");
            traderModel.setTransactiontime(DateTimeUtils.Companion.getNow());
            traderModel.setSynctime(DateTimeUtils.Companion.getNow());
            traderModel.setTransactedby(traderModel.getEntityname() + ", " + adminPrefs.getAdmin().getCode());



            traderModel.setArchived(0);
            traderModel.setDeleted(0);
            traderModel.setSynced(0);
            traderModel.setDummy(0);


            traderModel.setIsdeleted(false);
            traderModel.setIsarchived(false);
            traderModel.setIsdummy(isDummy);



            startAnim();

            try {
                requestDataCallback.data(new JSONObject(gson.toJson(traderModel)));
            } catch (JSONException e) {
                e.printStackTrace();
            }


//            saveTrader(traderModel, new CreateTraderCallbacks() {
//                @Override
//                public void success(ResponseModel responseModel) {
//                    dialog.dismiss();
//                    stopAnim();
//                    snack(responseModel.getResultDescription());
//                    createTraderCallbacks.success(responseModel);
//
//                }
//
//                @Override
//                public void error(String response) {
//
//                    stopAnim();
//                    snack(response);
//                    createTraderCallbacks.error(response);
//                    //dialog.dismiss();
//                }
//
//                @Override
//                public void startProgressDialog() {
//
//                    startAnim();
//
//
//                }
//
//                @Override
//                public void stopProgressDialog() {
//
//                    stopAnim();
//                }
//
//                @Override
//                public void updateProgressDialog(String message) {
//
//                }
//            });


        }

    }

    private class EditCustomListener implements View.OnClickListener {
        // AlertDialog dialog;
        boolean isDummy = false;
        TraderModel traderModel;
        //int type;
        RequestDataCallback requestDataCallback;

        public EditCustomListener(AlertDialog alertDialogAndroid, TraderModel traderModel, RequestDataCallback requestDataCallback) {
            dialog = alertDialogAndroid;
            this.requestDataCallback = requestDataCallback;
            this.traderModel = traderModel;

            // this.type=type;
        }

        @Override
        public void onClick(View v) {
            TextInputEditText name, phone;
            CheckBox chkDummy;

            name = dialog.findViewById(R.id.edt_traders_names);
            phone = dialog.findViewById(R.id.edt_traders_phone);
            chkDummy = dialog.findViewById(R.id.chk_dummy);


            if (name.getText().toString().isEmpty()) {
                name.setError("Required");
                name.requestFocus();
                stopAnim();
                return;
            }


            if (phone.getText().toString().isEmpty()) {
                phone.setError("Required");
                phone.requestFocus();
                stopAnim();
                return;
            }

            if (!isValidPhoneNumber(phone.getText().toString())) {
                snack("Invalid phone number ");
                phone.requestFocus();
                stopAnim();
                phone.setError("Invalid Phone number");
            }


            if (chkDummy.isChecked()) {
                isDummy = true;
            }

            traderModel.setNames(name.getText().toString());
            traderModel.setMobile(phone.getText().toString());
            traderModel.setTransactiontime(DateTimeUtils.Companion.getNowslong());


            startAnim();
            try {
                requestDataCallback.data(new JSONObject(gson.toJson(traderModel)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            upTrader(traderModel, new TraderCallbacks() {
//                @Override
//                public void success(ResponseModel responseModel) {
//                    dialog.dismiss();
//                    stopAnim();
//                    snack(responseModel.getResultDescription());
//                    createTraderCallbacks.success(responseModel);
//
//                }
//
//                @Override
//                public void error(String response) {
//
//                    stopAnim();
//                    snack(response);
//                    createTraderCallbacks.error(response);
//                    //dialog.dismiss();
//                }
//
//                @Override
//                public void startProgressDialog() {
//
//                    startAnim();
//
//
//                }
//
//                @Override
//                public void stopProgressDialog() {
//
//                    stopAnim();
//                }
//
//                @Override
//                public void updateProgressDialog(String message) {
//
//                }
//            });


        }

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
