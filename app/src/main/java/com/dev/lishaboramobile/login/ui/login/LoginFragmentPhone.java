package com.dev.lishaboramobile.login.ui.login;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.card.MaterialCardView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.dev.lishaboramobile.Global.Account.ResponseObject;
import com.dev.lishaboramobile.Global.Utils.MyToast;
import com.dev.lishaboramobile.Global.Utils.NetworkUtils;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Trader.Models.TraderModel;
import com.dev.lishaboramobile.login.LoginConsts;
import com.dev.lishaboramobile.login.Models.AuthModel;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginFragmentPhone extends Fragment implements View.OnClickListener {
    private String TAG = "lsbLoginTag";
    private LoginViewModel mViewModel;
    private Gson gson = new Gson();

    //CARDS

    int ISVISIBLE = 0;

    //WIDGETS
    int ISGONE = 1;
    private MaterialCardView headerCard, phoneCard;
    private AVLoadingIndicatorView aviPhone;

    private Context context;
    private View view;

    private String phoneNumber = "";
    private TextInputEditText edtPhone;
    private Button btnNextPhoneView;
    private int headerCardState = 0;
    private Fragment fragment;


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

        initWidgets();


    }

    void initWidgets() {
        edtPhone = view.findViewById(R.id.edt_phone);
        btnNextPhoneView = view.findViewById(R.id.btn_next_phone_view);
        aviPhone = view.findViewById(R.id.avi_phone);


        edtPhone.setOnClickListener(this);
        btnNextPhoneView.setOnClickListener(this);
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


            case R.id.edt_phone:

                phoneInputClicked();

                break;


            default:
                Log.d(TAG, " No action on click");
        }
    }

    private void handler() {

        aviPhone.setVisibility(View.GONE);
        aviPhone.smoothToHide();

    }

    private void nextOnPhoneInputClicked() {

        if (!TextUtils.isEmpty(edtPhone.getText().toString())) {
            String phoneNumber = edtPhone.getText().toString().replaceAll(" ", "").trim();
            if (LoginController.isValidPhoneNumber(phoneNumber)) {

                aviPhone.setVisibility(View.VISIBLE);
                aviPhone.smoothToShow();

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

                    if (responseModel.getResultCode() == 1) {
                        handler();
                        Gson gson = new Gson();
                        TraderModel traderModel;
                        LoginConsts.setResponseObject(responseModel);
                        LoginConsts.setPhone(phoneNumber);
                        switch (responseModel.getType()) {
                            case LoginController.ADMIN:

                                passWordFragment(responseModel);

                                break;
                            case LoginController.TRADER:

                                traderModel = gson.fromJson(gson.toJson(responseModel.getData()), TraderModel.class);
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


    private void phoneInputClicked() {
        if (headerCardState == ISVISIBLE) {
            headerCard.setVisibility(View.GONE);
            headerCardState = ISGONE;
        }
    }

    private void snack(String msg) {
        // Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
        if (context != null) {
            MyToast.toast(msg, context, R.drawable.ic_launcher, Toast.LENGTH_SHORT);
        }
    }

    public void passWordFragment(ResponseObject responseModel) {
//        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.right_enter, R.anim.left_out)
//
//                .replace(R.id.container, LoginFragmentPassword.newInstance(responseModel))
//                .commitNow();

        fragment = LoginFragmentPassword.newInstance(responseModel);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.container, fragment, "fragmentWelcome").commit();

    }

    public void newPassword(ResponseObject responseModel) {

        fragment = ForgotPassConfirmFragment.newInstance(responseModel);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.container, fragment, "fragmentWelcome").commit();

//        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.right_enter, R.anim.left_out)
//
//                .replace(R.id.container, ForgotPassConfirmFragment.newInstance(responseModel))
//                .commitNow();

    }


}
