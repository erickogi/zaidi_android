package com.dev.zaidi.Views.Login.Fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.card.MaterialCardView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dev.zaidi.Models.Trader.TraderModel;
import com.dev.zaidi.R;
import com.dev.zaidi.ViewModels.Login.LoginViewModel;
import com.dev.zaidi.Views.Admin.CreateTraderConstants;
import com.dev.zaidi.Views.Trader.Activities.Register;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.LinkedList;
import java.util.Objects;

import timber.log.Timber;


public class AccountNotFoundFragment extends Fragment implements View.OnClickListener {
    //CARDS
    TextView txtKe;
    int ISVISIBLE = 0;
    //WIDGETS
    int ISGONE = 1;
    int v = 0;
    private String TAG = "lsbLoginTag";
    private LoginViewModel mViewModel;
    private Gson gson = new Gson();
    private MaterialCardView headerCard, phoneCard;
    private AVLoadingIndicatorView aviPhone;
    private Context context;
    private View view;
    private String phoneNumber = "";
    private TextInputEditText edtPhone;
    private Button btnRegister;
    private Button btnLogin;
    private int headerCardState = 0;
    private Fragment fragment;

    public static AccountNotFoundFragment newInstance(String s, String phone) {

        AccountNotFoundFragment fragment = new AccountNotFoundFragment();
        Bundle args = new Bundle();
        args.putSerializable("message", s);
        args.putSerializable("phone", phone);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.account_not_found_fragment, container, false);
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
        txtKe = view.findViewById(R.id.txt_ke);

        try {
            TextView txtTitle = view.findViewById(R.id.txt_title);
            txtTitle.setText(getArguments().getString("message"));
        } catch (Exception nm) {
            nm.printStackTrace();
        }


        headerCard.setVisibility(View.GONE);


        initWidgets();


    }

    void initWidgets() {
        btnLogin = view.findViewById(R.id.btn_login);
        btnRegister = view.findViewById(R.id.btn_register);
        aviPhone = view.findViewById(R.id.avi_phone);


        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);


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
            case R.id.btn_login:


                Objects.requireNonNull(getActivity()).onBackPressed();
                break;


            case R.id.btn_register:

                TraderModel tr = new TraderModel();
                tr.setMobile(getArguments().getString("phone"));
                CreateTraderConstants.setTraderModel(tr);
                CreateTraderConstants.setProductModels(new LinkedList<>());

                startActivity(new Intent(getActivity(), Register.class));
                //Objects.requireNonNull(getActivity()).onBackPressed();
                getActivity().finish();

                break;


            default:
                Timber.d(" No action on click");
        }
    }


}
