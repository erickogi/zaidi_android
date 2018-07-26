package com.dev.lishaboramobile.Global.Account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.lishaboramobile.Global.AppConstants;
import com.dev.lishaboramobile.Global.Utils.NetworkUtils;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Trader.Models.TraderModel;
import com.dev.lishaboramobile.Views.Admin.AdminActivity;
import com.dev.lishaboramobile.Views.Trader.TraderActivity;
import com.dev.lishaboramobile.admin.models.AdminModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hbb20.CountryCodePicker;
import com.transitionseverywhere.ArcMotion;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.TransitionManager;

import java.lang.reflect.Type;
import java.util.Objects;

public class EntryActivity extends AppCompatActivity {
    CountryCodePicker ccp;
    private final int PHONE_VIEW = 1;
    private final int PASSWORD_VIEW = 2;
    ImageView imageView;
    TextInputEditText editTextCarrierNumber, edtPassword;
    private TextView txtNames;
    GridLayout gridView;
    private boolean isImageVisible=true;
    LinearLayout linearLayout2, linearLayout1;
    private RelativeLayout relativeLayoutParent;
    LinearLayout linearLayoutPassword, linearLayoutPhoneNumber;
    EntryController entryController;
    private boolean isPhone = true;
    private ViewGroup transitionsContainer;
    private ProgressDialog progressDialog;

    @Override
    protected void onStart() {
        super.onStart();
        entryController = new EntryController(EntryActivity.this);

    }

    private void bindViews() {
        transitionsContainer = findViewById(R.id.transitions_container);
        linearLayout2=findViewById(R.id.linear2);
        linearLayoutPassword = findViewById(R.id.linear_password);
        linearLayoutPhoneNumber = findViewById(R.id.linear_phone);
        imageView=findViewById(R.id.logo);
        txtNames = findViewById(R.id.txt_names);
        gridView=findViewById(R.id.grid);
        gridView.setVisibility(View.GONE);
        ccp = findViewById(R.id.ccp);
        relativeLayoutParent=findViewById(R.id.relative_parent);

        editTextCarrierNumber =  findViewById(R.id.editText_carrierNumber);
        edtPassword = findViewById(R.id.editText_password);


    }

    private void viewTransitions(boolean isHeaderVisible) {
        if (isHeaderVisible) {
            imageView.setVisibility(View.VISIBLE);
            isImageVisible = true;
        } else {
            TransitionManager.beginDelayedTransition(transitionsContainer,
                    new ChangeBounds().setPathMotion(new ArcMotion()).setDuration(500));

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout2.getLayoutParams();
            params.gravity = (Gravity.LEFT | Gravity.TOP);
            linearLayout2.setLayoutParams(params);
            imageView.setVisibility(View.GONE);
            isImageVisible = false;
        }
    }

    private void viewSwitchTo(int whichView) {
        switch (whichView) {
            case PHONE_VIEW:
                linearLayoutPhoneNumber.setVisibility(View.VISIBLE);
                linearLayoutPassword.setVisibility(View.GONE);
                isPhone = true;
                break;

            case PASSWORD_VIEW:
                viewTransitions(true);
                linearLayoutPassword.setVisibility(View.VISIBLE);
                linearLayoutPhoneNumber.setVisibility(View.GONE);
                isPhone = false;

            default:
        }

    }

    private void viewActions() {
        ccp.setCcpClickable(false);
        editTextCarrierNumber.setOnClickListener(v -> viewTransitions(false));
        edtPassword.setOnClickListener(v -> viewTransitions(false));
        editTextCarrierNumber.addTextChangedListener(new TextWatcher() {
            private static final char space = ' ';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // if(s.length()>0&&editTextCarrierNumber.getText().toString().charAt(0)!='0') {
                if (s.length() > 0 && (s.length() % 4) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (space == c) {
                        s.delete(s.length() - 1, s.length());
                    }

                }
                // Insert char where needed.
                if (s.length() > 0 && (s.length() % 4) == 0) {
                    char c = s.charAt(s.length() - 1);
                    // Only if its a digit where there should be a space we insert a space
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                        s.insert(s.length() - 1, String.valueOf(space));
                    }

                }
//                }else {
//                    if(s.length()==4){
//                        final char c = s.charAt(s.length() - 1);
//                        if (space == c) {
//                            s.delete(s.length() - 1, s.length());
//                        }
//                        //s.insert(s.length() , String.valueOf(space));
//                        String no=editTextCarrierNumber.getText().toString()+space;
//                        editTextCarrierNumber.setText(no);
//                        editTextCarrierNumber.setSelection(editTextCarrierNumber.length());
//
//                    }
//                    if(s.length()==8){
//                        //s.insert(s.length() , String.valueOf(space));
//                        String no=editTextCarrierNumber.getText().toString()+space;
//                        editTextCarrierNumber.setText(no);
//                        editTextCarrierNumber.setSelection(editTextCarrierNumber.length());
//                    }
//
//
//
//                }
            }
        });


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_entry);
        bindViews();
        viewActions();


    }


    @Override
    public void onBackPressed() {

        if (isPhone) {
            if (isImageVisible) {
                super.onBackPressed();

            } else {
                viewTransitions(true);
            }
        } else {
            if (isImageVisible) {
                viewSwitchTo(PHONE_VIEW);
            } else {
                viewTransitions(true);
            }
        }


    }

    public void numberClicked(int a){
        String n=editTextCarrierNumber.getText().toString();
        editTextCarrierNumber.setText(n+String.valueOf(a));
        editTextCarrierNumber.setSelection(editTextCarrierNumber.length());

    }
    public void clearClicked(){
        if(editTextCarrierNumber.getText()!=null&&editTextCarrierNumber.getText().toString().length()>0) {
            String n = editTextCarrierNumber.getText().toString();
            StringBuilder sb = new StringBuilder(n);
            editTextCarrierNumber.setText(sb.deleteCharAt(n.length() - 1).toString());
            editTextCarrierNumber.setSelection(editTextCarrierNumber.length());

        }
    }


    public void next(View view) {

        hideKeyboard();
        if (isPhone) {
            if (!TextUtils.isEmpty(editTextCarrierNumber.getText().toString())) {
                String phoneNumber = editTextCarrierNumber.getText().toString().replaceAll(" ", "").trim();

                String phone = "";

                StringBuilder sb = new StringBuilder(phoneNumber);

                if (entryController.isValidPhoneNumber(sb.toString())) {

                    if (NetworkUtils.Companion.isConnected(this)) {
                        checkPhone(sb.toString());
                    } else {
                        snack(getString(R.string.no_internet));
                    }
                } else {
                    editTextCarrierNumber.setError(getString(R.string.invalid_phone));
                }
            }else {
                snack(getString(R.string.enter_phone));
                editTextCarrierNumber.requestFocus();
                editTextCarrierNumber.setError(getString(R.string.reguired));
            }
        } else {


            if (!TextUtils.isEmpty(edtPassword.getText().toString())) {
                checkPassword(edtPassword.getText().toString());
            } else {
                edtPassword.requestFocus();
                edtPassword.setError("Required");
                snack("Enter your password");
            }

        }
    }

    private void checkPassword(String password) {
        entryController.authPassword(password, new EntryCallbacks() {
            @Override
            public void success(ResponseObject responseObject) {
                if (responseObject.getResultCode() > 0) {
                    String names = "";
                    Type type;
                    Gson gson = new Gson();
                    switch (responseObject.getType()) {
                        case AppConstants.ADMIN:
                            type = new TypeToken<AdminModel>() {
                            }.getType();
                            AdminModel adminModel = gson.fromJson(gson.toJson(responseObject.getData()), type);
                            names = adminModel.getNames();
                            startActivity(new Intent(EntryActivity.this, AdminActivity.class));

                            break;
                        case AppConstants.COLLECTOR:

                            break;

                        case AppConstants.DISTRIBUTER:
                            break;

                        case AppConstants.FARMER:

                            break;
                        case AppConstants.MASTER_TRADER:

                            break;

                        case AppConstants.TRADER:
                            type = new TypeToken<TraderModel>() {
                            }.getType();
                            TraderModel traderModel = gson.fromJson(gson.toJson(responseObject.getData()), type);
                            names = traderModel.getNames();

                            startActivity(new Intent(EntryActivity.this, TraderActivity.class));

                            break;

                        default:
                    }

                    //setNames(names);
                    //viewSwitchTo(PASSWORD_VIEW);
                } else {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    snack(responseObject.getResultDescription());
                }

            }

            @Override
            public void error(String response) {

                snack(response);

            }

            @Override
            public void startProgressDialog() {

                if (progressDialog != null) {
                    progressDialog.show();
                } else {
                    createDialog();
                    progressDialog.show();
                }
            }

            @Override
            public void stopProgressDialog() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void updateProgressDialog(String message) {

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.setMessage(message);
                }
            }
        });


    }

    private void checkPhone(String phone) {


        entryController.authPhone(phone, new EntryCallbacks() {
            @Override
            public void success(ResponseObject responseObject) {
                if (responseObject.getResultCode() > 0) {
                    String names = "";
                    Type type;
                    Gson gson = new Gson();
                    switch (responseObject.getType()) {
                        case AppConstants.ADMIN:
                            type = new TypeToken<AdminModel>() {
                            }.getType();
                            AdminModel adminModel = gson.fromJson(gson.toJson(responseObject.getData()), type);
                            names = adminModel.getNames();

                            break;
                        case AppConstants.COLLECTOR:

                            break;

                        case AppConstants.DISTRIBUTER:
                            break;

                        case AppConstants.FARMER:

                            break;
                        case AppConstants.MASTER_TRADER:

                            break;

                        case AppConstants.TRADER:
                            type = new TypeToken<TraderModel>() {
                            }.getType();
                            TraderModel traderModel = gson.fromJson(gson.toJson(responseObject.getData()), type);
                            names = traderModel.getNames();
                            break;

                        default:
                    }

                    setNames(names);
                    viewSwitchTo(PASSWORD_VIEW);
                } else {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    snack(responseObject.getResultDescription());
                }

            }

            @Override
            public void error(String response) {

                snack(response);

            }

            @Override
            public void startProgressDialog() {

                if (progressDialog != null) {
                    progressDialog.show();
                } else {
                    createDialog();
                    progressDialog.show();
                }
            }

            @Override
            public void stopProgressDialog() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void updateProgressDialog(String message) {

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.setMessage(message);
                }
            }
        });


    }

    private void setNames(String names) {
        txtNames.setText("You are login -in as " + names);
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void clear(View view) {
        clearClicked();
    }

    public void zero(View view) {
        numberClicked(0);
    }

    public void nine(View view) {
        numberClicked(9);

    }

    public void eight(View view) {
        numberClicked(8);

    }

    public void six(View view) {
        numberClicked(6);

    }

    public void seven(View view) {
        numberClicked(7);

    }

    public void five(View view) {
        numberClicked(5);

    }

    public void four(View view) {
        numberClicked(4);

    }

    public void three(View view) {
        numberClicked(3);

    }

    public void two(View view) {
        numberClicked(2);

    }

    public void one(View view) {
        numberClicked(1);

    }

    private void snack(String msg) {
        Snackbar.make(relativeLayoutParent, msg, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    private void createDialog() {
        progressDialog = new ProgressDialog(EntryActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(true);
    }

}
