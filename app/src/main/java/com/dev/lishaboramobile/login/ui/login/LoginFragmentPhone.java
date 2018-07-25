package com.dev.lishaboramobile.login.ui.login;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.card.MaterialCardView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishaboramobile.Admin.Models.AdminModel;
import com.dev.lishaboramobile.Global.Account.ResponseObject;
import com.dev.lishaboramobile.Global.Utils.MyToast;
import com.dev.lishaboramobile.Global.Utils.NetworkUtils;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Trader.Models.TraderModel;
import com.dev.lishaboramobile.Views.Admin.AdminActivity;
import com.dev.lishaboramobile.Views.Trader.TraderActivity;
import com.dev.lishaboramobile.login.Models.AuthModel;
import com.dev.lishaboramobile.login.PrefrenceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;


public class LoginFragmentPhone extends Fragment implements View.OnClickListener {
    private static int spalsh_time_out = 1000;
    private String TAG = "lsbLoginTag";
    private LoginViewModel mViewModel;
    private Gson gson = new Gson();

    //CARDS

    private MaterialCardView headerCard, phoneCard, passwordCard;

    //WIDGETS

    private AVLoadingIndicatorView aviPass, aviPhone;
    private TextInputEditText edtPhone, edtPassword;
    private TextView txtTagline, txtPasswordViewTitle;
    private Button btnNextPhoneView, btnNextPasswordView;

    //CARD STATES

    private int headerCardState, phoneCardState, passwordcardState;

    //STATES

    private int ISVISIBLE = 0;
    private int ISGONE = 1;
    private int INVISIBLE = 2;


    private Context context;
    private View view;

    private String phoneNumber = "";


    public static LoginFragmentPhone newInstance() {
        return new LoginFragmentPhone();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
    }

    void initCards() {
        //Find views
        headerCard = view.findViewById(R.id.card_header);
        phoneCard = view.findViewById(R.id.card_phone_view);
        passwordCard = view.findViewById(R.id.card_password_view);

        //Set password view gone
        passwordCard.setVisibility(View.GONE);

        //initialize states
        headerCardState = ISVISIBLE;
        phoneCardState = ISVISIBLE;
        passwordcardState = ISGONE;

        initWidgets();


    }

    void initWidgets() {

        //edtPassword=view.findViewById(R.id.edt_password);
        edtPhone = view.findViewById(R.id.edt_phone);

        txtTagline = view.findViewById(R.id.txt_tagline);
        //txtPasswordViewTitle=view.findViewById(R.id.txt_password_view_title);

        //btnNextPasswordView=view.findViewById(R.id.btn_next_password_view);
        btnNextPhoneView = view.findViewById(R.id.btn_next_phone_view);

        //aviPass=view.findViewById(R.id.avi_pass);
        aviPhone = view.findViewById(R.id.avi_phone);


        edtPhone.setOnClickListener(this);
        //edtPassword.setOnClickListener(this);
        btnNextPhoneView.setOnClickListener(this);
        //btnNextPasswordView.setOnClickListener(this);
        //txtPasswordViewTitle.setOnClickListener(this);
        final char space = ' ';

        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

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
            }
        });


    }


    @Override
    public void onStart() {
        super.onStart();
        context = getContext();
        initCards();

    }

    @Override
    public void onResume() {
        super.onResume();
        context = getContext();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next_phone_view:

                if (NetworkUtils.Companion.isConnected(context)) {
                    nextOnPhoneInputClicked();
                } else {
                    snack("You not connected");
                }
                break;

            case R.id.btn_next_password_view:

                nextOnPasswordInputClicked();

                break;

            case R.id.edt_phone:

                phoneInputClicked();

                break;

            case R.id.edt_password:

                passwordInputClicked();

                break;

            default:
                Log.d(TAG, " No action on click");
        }
    }

    private void handler() {

        aviPhone.setVisibility(View.GONE);
        aviPhone.smoothToHide();

        //snack("Phone Auth simulation success");
//
//                    phoneCard.setVisibility(View.GONE);
//                    //passwordCard.setVisibility(View.VISIBLE);
//
//                    passwordcardState=ISVISIBLE;
//                    phoneCardState=ISGONE;


    }

    private void nextOnPhoneInputClicked() {

        if (!TextUtils.isEmpty(edtPhone.getText().toString())) {
            String phoneNumber = edtPhone.getText().toString().replaceAll(" ", "").trim();
            if (LoginController.isValidPhoneNumber(phoneNumber)) {

                aviPhone.setVisibility(View.VISIBLE);
                aviPhone.smoothToShow();
                //TODO AUTHPHONE

                // handler();

                AuthModel authModel = new AuthModel();
                authModel.setMobile(phoneNumber);
                this.phoneNumber = phoneNumber;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(gson.toJson(authModel));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.d("authl", jsonObject.toString());

                mViewModel.phoneAuth(jsonObject).observe(this, (ResponseObject responseModel) -> {
                    snack(responseModel.getResultDescription());

                    if (responseModel.getResultCode() == 1) {
                        snack(responseModel.getResultDescription());
                        handler();
                        Type type;
                        Gson gson = new Gson();
                        TraderModel traderModel = new TraderModel();
                        AdminModel adminModel = new AdminModel();

                        switch (responseModel.getType()) {
                            case LoginController.ADMIN:

                                passWordFragment(responseModel);

                                break;
                            case LoginController.TRADER:

                                traderModel = gson.fromJson(gson.toJson(responseModel.getData()), TraderModel.class);
                                //txtPasswordViewTitle.setText("Welcome | Karibu Trader - "+traderModel.getNames());
                                if (traderModel.getPasswordstatus() == 0) {
                                    newPassword(responseModel);
                                    snack("Create Pass");
                                } else {
                                    passWordFragment(responseModel);
                                }


                                break;
                            default:
                        }

                    } else {
                        snack(responseModel.getResultDescription());
                        aviPhone.smoothToHide();
                    }
                });

            } else {
                snack("Invalid phone");
                edtPhone.requestFocus();
                edtPhone.setError("Invalid phone");

            }

        } else {
            snack("Please enter  phone");
            edtPhone.requestFocus();
            edtPhone.setError("Required");

        }
    }

    private void nextOnPasswordInputClicked() {
        if (!TextUtils.isEmpty(edtPassword.getText().toString())) {

            aviPass.setVisibility(View.VISIBLE);
            aviPass.smoothToShow();
            //TODO AUTHPHONE

            handlerPass();
            AuthModel authModel = new AuthModel();
            authModel.setMobile(phoneNumber);
            authModel.setPassword(edtPassword.getText().toString());
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(gson.toJson(authModel));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            mViewModel.phoneAuth(jsonObject).observe(this, responseModel -> {
                snack(responseModel.getResultDescription());

                if (responseModel != null && responseModel.getResultCode() == 1) {
                    snack(responseModel.getResultDescription());
                    aviPhone.setVisibility(View.GONE);
                    aviPhone.smoothToHide();
                    Type type;
                    Gson gson = new Gson();
                    TraderModel traderModel = new TraderModel();
                    AdminModel adminModel = new AdminModel();

                    switch (responseModel.getType()) {
                        case LoginController.ADMIN:
                            type = new TypeToken<TraderModel>() {
                            }.getType();
                            adminModel = gson.fromJson(gson.toJson(responseModel.getData()), AdminModel.class);

                            loginAdmin(adminModel);

                            break;
                        case LoginController.TRADER:
                            type = new TypeToken<TraderModel>() {
                            }.getType();
                            traderModel = gson.fromJson(gson.toJson(responseModel.getData()), TraderModel.class);

                            loginTrader(traderModel);
                            break;
                        default:
                    }
                }
            });


        } else {
            snack("Please enter  password");
            edtPassword.requestFocus();
            edtPassword.setError("Required");

        }

    }

    private void loginTrader(TraderModel traderModel) {
        PrefrenceManager prefrenceManager = new PrefrenceManager(context);

        prefrenceManager.setIsLoggedIn(true, LoginController.TRADER);
        prefrenceManager.setLoggedUser(traderModel);

        startActivity(new Intent(getActivity(), TraderActivity.class));
    }

    private void loginAdmin(AdminModel adminModel) {
        PrefrenceManager prefrenceManager = new PrefrenceManager(context);
        prefrenceManager.setIsLoggedIn(true, LoginController.ADMIN);
        prefrenceManager.setLoggedUser(adminModel);

        startActivity(new Intent(getActivity(), AdminActivity.class));
    }

    private void handlerPass() {
        new Handler().postDelayed(() -> {

                    aviPass.setVisibility(View.GONE);
                    aviPass.smoothToHide();

                    snack("Pass Auth simulation success");

                    startActivity(new Intent(getActivity(), AdminActivity.class));
                    getActivity().finish();

                }


                , spalsh_time_out);

    }

    private void passwordInputClicked() {
        //TODO
    }

    private void phoneInputClicked() {
        if (headerCardState == ISVISIBLE) {
            headerCard.setVisibility(View.GONE);
            headerCardState = ISGONE;
        }
    }

    private void snack(String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
        if (context != null) {
            MyToast.toast(msg, context, R.drawable.ic_launcher, Toast.LENGTH_SHORT);
        }
    }

    public void passWordFragment(ResponseObject responseModel) {
        // getChildFragmentManager()
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, LoginFragmentPassword.newInstance(responseModel))
                // .addToBackStack("null")
                .commitNow();

    }

    public void newPassword(ResponseObject responseModel) {
        // getChildFragmentManager()
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ForgotPassPhoneFragment.newInstance(responseModel))
                // .addToBackStack("null")
                .commitNow();

    }


}
