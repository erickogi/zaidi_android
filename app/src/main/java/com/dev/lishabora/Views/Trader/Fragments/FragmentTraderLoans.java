package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.dev.lishabora.Adapters.LoansOrdersAdapter;
import com.dev.lishabora.Adapters.LoansOrdersPaymnetsAdapter;
import com.dev.lishabora.Models.Trader.FarmerLoansTable;
import com.dev.lishabora.Models.Trader.FarmerOrdersTable;
import com.dev.lishabora.Models.Trader.LoanPayments;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.Utils.OnActivityTouchListener;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.Utils.RecyclerTouchListener;
import com.dev.lishabora.ViewModels.Trader.BalncesViewModel;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class FragmentTraderLoans extends Fragment {
    LoansOrdersAdapter listAdapter;
    List<Integer> unclickableRows, unswipeableRows;
    List<LoanPayments> payments;
    LoansOrdersPaymnetsAdapter listAdapterP;
    private BalncesViewModel balncesViewModel;
    private RecyclerView recyclerView;
    private RecyclerTouchListener onTouchListener;
    private int openOptionsPosition;
    private OnActivityTouchListener touchListener;
    private View view;
    private List<FarmerLoansTable> farmerLoansTables;
    private List<FarmerOrdersTable> farmerOrdersTables;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;

    private void listPayments(String code) {

        payments = new LinkedList<>();
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.fragment_recycler_view, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
        alertDialogBuilderUserInput.setTitle("Loan Payments");

        RecyclerView recyclerView = mView.findViewById(R.id.recyclerView);


        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        listAdapterP = new LoansOrdersPaymnetsAdapter(getActivity(), payments, null, new OnclickRecyclerListener() {
            @Override
            public void onMenuItem(int position, int menuItem) {

                // action();
            }

            @Override
            public void onSwipe(int adapterPosition, int direction) {


            }

            @Override
            public void onClickListener(int position) {


            }

            @Override
            public void onLongClickListener(int position) {


            }

            @Override
            public void onCheckedClickListener(int position) {

            }

            @Override
            public void onMoreClickListener(int position) {


            }

            @Override
            public void onClickListener(int adapterPosition, @NotNull View view) {


            }
        });


        recyclerView.setAdapter(listAdapterP);

        listAdapterP.notifyDataSetChanged();

        alertDialogBuilderUserInput
                .setCancelable(true);

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(true);
        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        try {
            alertDialogAndroid.show();
        } catch (Exception NM) {
            NM.printStackTrace();
        }


//        MaterialButton btnPositive, btnNegative, btnNeutral;
//        TextView txtTitle;
//        LinearLayout lTitle;
//        ImageView imgIcon;
//        btnPositive = mView.findViewById(R.id.btn_positive);
//        btnNegative = mView.findViewById(R.id.btn_negative);
//        btnNeutral = mView.findViewById(R.id.btn_neutral);
//        txtTitle = mView.findViewById(R.id.txt_title);
//        lTitle = mView.findViewById(R.id.linear_title);
//        imgIcon = mView.findViewById(R.id.img_icon);
//
//
//        btnNeutral.setVisibility(View.GONE);
//        btnNeutral.setText("Delete");
//
//        btnNeutral.setBackgroundColor(this.getResources().getColor(R.color.red));
//        lTitle.setVisibility(View.GONE);
//        txtTitle.setVisibility(View.VISIBLE);
//        imgIcon.setVisibility(View.VISIBLE);
//        imgIcon.setImageResource(R.drawable.ic_add_black_24dp);
//        txtTitle.setText("Route");
//        btnPositive.setVisibility(View.GONE);
//
////        btnNeutral.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                OrderConstants.getProductOrderModels().remove(pos);
////
////                alertDialogAndroid.dismiss();
////
////                refreshList();
////            }
////        });
//
////        btnPositive.setOnClickListener(new FragmentProductList.CustomListener(alertDialogAndroid, selected));
//
//        btnNegative.setOnClickListener(view -> alertDialogAndroid.dismiss());
//

        balncesViewModel.getLoanPaymentByLoanCode(code).observe(FragmentTraderLoans.this, loanPayments -> {
            if (loanPayments != null && loanPayments.size() > 0) {
                payments = loanPayments;
                listAdapterP.notifyDataSetChanged();
            } else {
                alertDialogAndroid.dismiss();

                MyToast.toast("There are no payments to this loan", getContext(), R.drawable.ic_error_outline_black_24dp, Toast.LENGTH_LONG);
            }

        });


    }

    public void initList(List<FarmerLoansTable> farmerLoansTables) {
        unclickableRows = new ArrayList<>();
        unswipeableRows = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerView);

        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        listAdapter = new LoansOrdersAdapter(getActivity(), farmerLoansTables, null, new OnclickRecyclerListener() {
            @Override
            public void onMenuItem(int position, int menuItem) {

                // action();
            }

            @Override
            public void onSwipe(int adapterPosition, int direction) {


            }

            @Override
            public void onClickListener(int position) {
                listPayments(farmerLoansTables.get(position).getCode());


            }

            @Override
            public void onLongClickListener(int position) {


            }

            @Override
            public void onCheckedClickListener(int position) {

            }

            @Override
            public void onMoreClickListener(int position) {


            }

            @Override
            public void onClickListener(int adapterPosition, @NotNull View view) {


            }
        });


        recyclerView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_recycler_view, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        balncesViewModel = ViewModelProviders.of(this).get(BalncesViewModel.class);

        balncesViewModel.getFarmerLoans().observe(this, farmerLoansTables -> {
            if (farmerLoansTables != null) {
                Log.d("LoansDubug", " Is null" + new Gson().toJson(farmerLoansTables.get(0)));

                initList(farmerLoansTables);
                // filter(farmerLoansTables);
            } else {
            }
        });

    }

    private void filter(List<FarmerLoansTable> farmerLoansTables) {


        setUplist(farmerLoansTables);
    }

    private void setUplist(List<FarmerLoansTable> farmerLoansTables) {

        this.farmerLoansTables = farmerLoansTables;
        this.farmerOrdersTables = null;
    }
}
