package com.dev.lishaboramobile.Admin.Controllers;

import android.content.Context;
import android.content.DialogInterface;
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
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Trader.Models.TraderModel;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TradersController {
    public int ISCREATE = 12;
    public int ISUPDATE = 13;
    private Context context;
    private CreateTraderCallbacks createTraderCallbacks;
    private AVLoadingIndicatorView avi;

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
//        private   int archived ;

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

    public void createTrader(CreateTraderCallbacks createTraderCallbacks) {
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
        theButton.setOnClickListener(new CustomListener(alertDialogAndroid, createTraderCallbacks));


    }

    void startAnim() {
        avi.setVisibility(View.VISIBLE);
        avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {
        avi.setVisibility(View.GONE);
        avi.hide();
        // or avi.smoothToHide();
    }

    private void snack(String msg) {
        MyToast.toast(msg, context, R.drawable.ic_launcher, Toast.LENGTH_LONG);
//        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
//                .setAction("Action", null).show();

        Log.d("SnackMessage", msg);
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

    public void editTrader(TraderModel traderModel, CreateTraderCallbacks createTraderCallbacks) {
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
        theButton.setOnClickListener(new EditCustomListener(alertDialogAndroid, traderModel, createTraderCallbacks));


    }

    private class CustomListener implements View.OnClickListener {
        AlertDialog dialog;
        boolean isDummy = false;
        //int type;
        CreateTraderCallbacks createTraderCallbacks;

        public CustomListener(AlertDialog alertDialogAndroid, CreateTraderCallbacks createTraderCallbacks) {
            dialog = alertDialogAndroid;
            this.createTraderCallbacks = createTraderCallbacks;
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
                return;
            }


            if (phone.getText().toString().isEmpty()) {
                phone.setError("Required");
                phone.requestFocus();
                return;
            }

            if (!isValidPhoneNumber(phone.getText().toString())) {
                snack("Invalid phone number ");
                phone.requestFocus();
                phone.setError("Invalid Phone number");
            }


            if (chkDummy.isChecked()) {
                isDummy = true;
            }


            AdminPrefs adminPrefs = new AdminPrefs(context);
            TraderModel traderModel = new TraderModel();
            //if(type==ISCREATE) {
            traderModel.setApikey("");
            traderModel.setArchived(0);
            traderModel.setEntityname("LishaBora");
            traderModel.setBalance("0");
            traderModel.setCode("" + new GeneralUtills(context).getRandon(9000, 1000));
            traderModel.setDummy(0);
            traderModel.setEntity("Admin");
            traderModel.setEntitycode(adminPrefs.getAdmin().getCode());
            traderModel.setDeleted(0);
            traderModel.setSynced(0);
            traderModel.setTransactiontime(DateTimeUtils.Companion.getNowslong());
            traderModel.setTransactedby(adminPrefs.getAdmin().getCode());
            traderModel.setIsdeleted(false);
            traderModel.setIsarchived(false);
            traderModel.setIsdummy(isDummy);
            traderModel.setStatus("Active");
            traderModel.setNames(name.getText().toString());
            traderModel.setMobile(phone.getText().toString());
            traderModel.setPassword("");
            traderModel.setId(0);
//            }else {
//
//            }


            startAnim();
            saveTrader(traderModel, new CreateTraderCallbacks() {
                @Override
                public void success(ResponseModel responseModel) {
                    dialog.dismiss();
                    stopAnim();
                    snack(responseModel.getResultDescription());
                    createTraderCallbacks.success(responseModel);

                }

                @Override
                public void error(String response) {

                    stopAnim();
                    snack(response);
                    createTraderCallbacks.error(response);
                    //dialog.dismiss();
                }

                @Override
                public void startProgressDialog() {

                    startAnim();


                }

                @Override
                public void stopProgressDialog() {

                    stopAnim();
                }

                @Override
                public void updateProgressDialog(String message) {

                }
            });


        }

    }

    private class EditCustomListener implements View.OnClickListener {
        AlertDialog dialog;
        boolean isDummy = false;
        TraderModel traderModel;
        //int type;
        CreateTraderCallbacks createTraderCallbacks;

        public EditCustomListener(AlertDialog alertDialogAndroid, TraderModel traderModel, CreateTraderCallbacks createTraderCallbacks) {
            dialog = alertDialogAndroid;
            this.createTraderCallbacks = createTraderCallbacks;
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
                return;
            }


            if (phone.getText().toString().isEmpty()) {
                phone.setError("Required");
                phone.requestFocus();
                return;
            }

            if (!isValidPhoneNumber(phone.getText().toString())) {
                snack("Invalid phone number ");
                phone.requestFocus();
                phone.setError("Invalid Phone number");
            }


            if (chkDummy.isChecked()) {
                isDummy = true;
            }

            traderModel.setNames(name.getText().toString());
            traderModel.setMobile(phone.getText().toString());
            traderModel.setTransactiontime(DateTimeUtils.Companion.getNowslong());


            startAnim();
            upTrader(traderModel, new TraderCallbacks() {
                @Override
                public void success(ResponseModel responseModel) {
                    dialog.dismiss();
                    stopAnim();
                    snack(responseModel.getResultDescription());
                    createTraderCallbacks.success(responseModel);

                }

                @Override
                public void error(String response) {

                    stopAnim();
                    snack(response);
                    createTraderCallbacks.error(response);
                    //dialog.dismiss();
                }

                @Override
                public void startProgressDialog() {

                    startAnim();


                }

                @Override
                public void stopProgressDialog() {

                    stopAnim();
                }

                @Override
                public void updateProgressDialog(String message) {

                }
            });


        }

    }


}
