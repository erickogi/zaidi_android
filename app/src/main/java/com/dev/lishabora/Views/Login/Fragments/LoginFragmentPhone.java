package com.dev.lishabora.Views.Login.Fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.card.MaterialCardView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.lishabora.COntrollers.LoginController;
import com.dev.lishabora.Models.Admin.AdminModel;
import com.dev.lishabora.Models.Login.AuthModel;
import com.dev.lishabora.Models.ResponseObject;
import com.dev.lishabora.Models.Trader.TraderModel;
import com.dev.lishabora.Utils.NetworkUtils;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.ViewModels.Login.LoginViewModel;
import com.dev.lishabora.Views.Admin.Activities.AdminsActivity;
import com.dev.lishabora.Views.Login.LoginConsts;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import timber.log.Timber;


public class LoginFragmentPhone extends Fragment implements View.OnClickListener {
    private String TAG = "lsbLoginTag";
    private LoginViewModel mViewModel;
    private Gson gson = new Gson();

    //CARDS
    TextView txtKe;
    int ISVISIBLE = 0;

    //WIDGETS
    int ISGONE = 1;
    private MaterialCardView headerCard, phoneCard;
    private AVLoadingIndicatorView aviPhone;

    private Context context;
    private View view;

    private String phoneNumber = "";
    private TextInputEditText edtPhone;
    private CountryCodePicker ccp;
    private Button btnNextPhoneView;
    private int headerCardState = 0;
    private Fragment fragment;
    int v = 0;


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

    private ImageView imageCow;

    public void adminLogin() {
        // MaterialDialog.Builder builder=new MaterialDialog.Builder(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Admin Login");

// Set up the input
        final EditText username = new EditText(getContext());
        final EditText password = new EditText(getContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        username.setInputType(InputType.TYPE_CLASS_TEXT);
        password.setInputType(InputType.TYPE_CLASS_TEXT);

// Set up the buttons


        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

// Add a TextView here for the "Title" label, as noted in the comments
        username.setHint("Username");
        layout.addView(username); // Notice this is an add method

// Add another TextView here for the "Description" label
        password.setHint("Password");
        layout.addView(password); // Another add method

        builder.setView(layout);


        builder.setPositiveButton("OK", (dialog, which) -> {
            if (!TextUtils.isEmpty(username.getText())&&!TextUtils.isEmpty(password.getText())) {
                adminLoginHack(username.getText().toString(),password.getText().toString());
               // new PrefrenceManager(getContext()).setDev_folder(input.getText().toString());
                dialog.cancel();
            }else {
                snack("Invalid credentials");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    void initCards() {
        //Find views
        headerCard = view.findViewById(R.id.card_header);
        phoneCard = view.findViewById(R.id.card_phone_view);
        txtKe = view.findViewById(R.id.txt_ke);
               imageCow = view.findViewById(R.id.logo);
//               imageCow.setOnClickListener(new View.OnClickListener() {
//                   @Override
//                   public void onClick(View v) {
//                       adminLoginHack();
//                   }
//               });
        imageCow.setOnClickListener(view -> {

            if (v >= 5) {
                v = 0;
                adminLogin();
                //adminLoginHack();

            }
            v++;

        });


        initWidgets();


    }

    void initWidgets() {
        edtPhone = view.findViewById(R.id.edt_phone);
        ccp = view.findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(edtPhone);

        btnNextPhoneView = view.findViewById(R.id.btn_next_phone_view);
        aviPhone = view.findViewById(R.id.avi_phone);
//        TextView txtPrivacyPolicy = view.findViewById(R.id.privacy_policy);
//        txtPrivacyPolicy.setOnClickListener(view -> openPrivacyPolicy());



        edtPhone.setOnClickListener(this);
        btnNextPhoneView.setOnClickListener(this);
        final char space = ' ';

//        edtPhone.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // if(s.length()>0&&editTextCarrierNumber.getText().toString().charAt(0)!='0') {
//                try {
//                    if (s.charAt(0) == '0') {
//                        s.delete(s.length() - 1, s.length());
//                        txtKe.setText("+254-0");
//                    } else if (s != null && s.length() < 1) {
//                        txtKe.setText("+254");
//
//                    }
//
//
//                } catch (Exception nm) {
//                    nm.printStackTrace();
//                }
//                if (s.length() > 0 && (s.length() % 4) == 0) {
//                    final char c = s.charAt(s.length() - 1);
//                    if (space == c) {
//                        s.delete(s.length() - 1, s.length());
//                    }
//
//                }
//                // Insert char where needed.
//                if (s.length() > 0 && (s.length() % 4) == 0) {
//                    char c = s.charAt(s.length() - 1);
//                    // Only if its a digit where there should be a space we insert a space
//                    if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
//                        s.insert(s.length() - 1, String.valueOf(space));
//                    }
//
//                }
//            }
//        });


    }

    private void openPrivacyPolicy() {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://calista.co.ke/sokoni/lishaboraprivacy_policy.html"));
            startActivity(browserIntent);
        } catch (Exception nm) {
            nm.printStackTrace();
        }
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
                Timber.d(" No action on click");
        }
    }

    private void handler() {

        aviPhone.setVisibility(View.GONE);
        aviPhone.smoothToHide();

    }

    //private String phoneNumber = "";
    private void nextOnPhoneInputClicked() {

        if (!TextUtils.isEmpty(edtPhone.getText().toString())) {
            String phoneNumber1 = ccp.getFullNumber();
            if (phoneNumber1.startsWith("25407")) {
                phoneNumber = phoneNumber1.replace("2540", "");
            }
            if (phoneNumber1.startsWith("2547")) {
                phoneNumber = phoneNumber1.replace("254", "");
            }
            if (LoginController.isValidPhoneNumber(phoneNumber)
                // && GeneralUtills.Companion.isValidPhoneNumber(edtPhone.getText().toString())

            ) {

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
                                } else {
                                    passWordFragment(responseModel);
                                }


                                break;
                            default:
                        }

                    } else {
                        //snack(responseModel.getResultDescription());
                        aviPhone.smoothToHide();
                        accountNotFoundFragment(responseModel);
                    }
                });

            } else {
                snack(getString(R.string.invalid_phone));
                edtPhone.requestFocus();
                edtPhone.setError(getString(R.string.invalid_phone));

            }

        } else {
            snack(getString(R.string.please_enter_valid_phone_number));
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
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
        if (context != null) {
            // MyToast.toast(msg, context, Toast.LENGTH_SHORT);
        }
    }

    public void passWordFragment(ResponseObject responseModel) {
//        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.right_enter, R.anim.left_out)
//
//                .replace(R.id.container, LoginFragmentPassword.newInstance(responseModel))
//                .commitNow();

        fragment = LoginFragmentPassword.newInstance(responseModel);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()//.setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.container, fragment, "fragmentWelcome").commit();

    }

    public void newPassword(ResponseObject responseModel) {

        fragment = ForgotPassConfirmFragment.newInstance(responseModel);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()//.setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.container, fragment, "fragmentWelcome").addToBackStack(null).commit();


    }

    public void accountNotFoundFragment(ResponseObject responseModel) {

        fragment = AccountNotFoundFragment.newInstance(responseModel.getResultDescription(), phoneNumber);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()//.setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.container, fragment, "accountNotound").addToBackStack(null).commit();


    }


    /////// ADMIN HACK

    private void adminLoginHack(String username,String  password) {

            AuthModel authModel = new AuthModel();
            authModel.setMobile(username);
            authModel.setPassword(password);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(gson.toJson(authModel));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            mViewModel.passwordAuth(jsonObject).observe(this, responseModel -> {
                snack(responseModel.getResultDescription());
                LoginConsts.setResponseObject(responseModel);

                if (responseModel.getResultCode() == 1) {
                    snack(responseModel.getResultDescription());

                    Gson gson = new Gson();
                    TraderModel traderModel;
                    AdminModel adminModel;

                    switch (responseModel.getType()) {
                        case LoginController.ADMIN:

                            adminModel = gson.fromJson(gson.toJson(responseModel.getData()), AdminModel.class);

                            loginAdmin(adminModel);

                            break;
                        default:
                    }
                }
            });




    }
    private void loginAdmin(AdminModel adminModel) {
        PrefrenceManager prefrenceManager = new PrefrenceManager(context);
        prefrenceManager.setIsLoggedIn(true, LoginController.ADMIN);
        prefrenceManager.setLoggedUser(adminModel);

        startActivity(new Intent(getActivity(), AdminsActivity.class));
        Objects.requireNonNull(getActivity()).finish();
    }



}
