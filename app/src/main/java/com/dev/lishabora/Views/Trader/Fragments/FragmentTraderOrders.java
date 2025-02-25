package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioGroup;

import com.dev.lishabora.Adapters.LoansOrdersAdapter;
import com.dev.lishabora.Adapters.LoansOrdersPaymnetsAdapter;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.FarmerBalance;
import com.dev.lishabora.Models.Trader.FarmerLoansTable;
import com.dev.lishabora.Models.Trader.FarmerOrdersTable;
import com.dev.lishabora.Models.Trader.OrderPayments;
import com.dev.lishabora.Utils.InputFilterMinMax;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.Utils.OnActivityTouchListener;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.Utils.RecyclerTouchListener;
import com.dev.lishabora.ViewModels.Trader.BalncesViewModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishaboramobile.R;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class FragmentTraderOrders extends Fragment {
    LoansOrdersAdapter listAdapter;
    List<Integer> unclickableRows, unswipeableRows;
    List<OrderPayments> orderPayments;
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


    private EasyFlipView easyFlipView;
    private EasyFlipView.FlipState currentSide;
    private TraderViewModel traderViewModel;

    String paymentMethod = "";
    int paymentMethodId = 0;

    private void listPayments(FarmerOrdersTable code) {
        String vB = String.valueOf((Double.valueOf(code.getOrderAmount())) - Double.valueOf(code.getOrderAmountPaid()));

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_loan_order_payments, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput.setCancelable(true);
        alertDialogBuilderUserInput.setCancelable(true);
        //  alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
        alertDialogBuilderUserInput.setTitle("Order Payments  Balance " + vB);

        RecyclerView recyclerView = mView.findViewById(R.id.recyclerView);


        MaterialButton positive = mView.findViewById(R.id.btn_positive);
        MaterialButton negative = mView.findViewById(R.id.btn_negative);

        MaterialButton positive1 = mView.findViewById(R.id.btn_positive1);
        MaterialButton negative1 = mView.findViewById(R.id.btn_negative1);

        RadioGroup radioGroup = mView.findViewById(R.id.radiogroup);
        TextInputEditText value = mView.findViewById(R.id.edt_value);
        TextInputEditText edt_ref = mView.findViewById(R.id.edt_ref);
        TextInputLayout edtL = mView.findViewById(R.id.edtl);


        double bal = (Double.valueOf(code.getOrderAmount())) - Double.valueOf(code.getOrderAmountPaid());
        value.setFilters(new InputFilter[]{new InputFilterMinMax(1, (int) bal)});

        if (code.getStatus() == 1) {
            positive.setVisibility(View.GONE);
        }

        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        easyFlipView = mView.findViewById(R.id.easyFlipView);
        currentSide = EasyFlipView.FlipState.FRONT_SIDE;

        easyFlipView.setOnFlipListener((easyFlipView, newCurrentSide) -> {
            currentSide = newCurrentSide;

            if (currentSide == EasyFlipView.FlipState.FRONT_SIDE) {
            } else {
            }


        });

        listAdapterP = new LoansOrdersPaymnetsAdapter(getActivity(), null, code.getOrderPayments(), new OnclickRecyclerListener() {
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
                .setCancelable(false);

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(true);
        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Objects.requireNonNull(alertDialogAndroid.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        alertDialogAndroid.show();


        positive.setOnClickListener(v -> {

            if (currentSide == EasyFlipView.FlipState.FRONT_SIDE) {

                easyFlipView.flipTheView();
            }
        });


        negative.setOnClickListener(v -> alertDialogAndroid.dismiss());

        positive1.setOnClickListener(v -> {

            if (TextUtils.isEmpty(value.getText()) || value.getText().toString() == null) {
                value.requestFocus();
                value.setError("Required");
            } else {
                String valuea = value.getText().toString();
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    MyToast.errorToast("Select Payment method", getContext());
                } else {

                    String ref = "";
                    String paymentCode = "";

                    if (paymentMethodId == 1 || paymentMethodId == 3) {

                        if (!TextUtils.isEmpty(edt_ref.getText())) {

                            ref = edt_ref.getText().toString();
                        }

                            FamerModel famerModel = traderViewModel.getFarmersByCodeOne(code.getFarmerCode());
                        CommonFuncs.insertOrderPayment(Double.valueOf(valuea), balncesViewModel, famerModel, paymentMethod, ref, famerModel.getCurrentPayoutCode());
                            refreshFarmerBalance(famerModel, code.getPayoutCode());
                            CommonFuncs.updateOrder(code, balncesViewModel);

                            alertDialogAndroid.dismiss();
                            getData();

                    } else {

                        FamerModel famerModel = traderViewModel.getFarmersByCodeOne(code.getFarmerCode());
                        CommonFuncs.insertOrderPayment(Double.valueOf(valuea), balncesViewModel, famerModel, paymentMethod, ref, famerModel.getCurrentPayoutCode());
                        refreshFarmerBalance(famerModel, code.getPayoutCode());
                        CommonFuncs.updateOrder(code, balncesViewModel);

                        alertDialogAndroid.dismiss();
                        getData();
                    }



                }

            }

        });


        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            switch (checkedId) {
                case R.id.radio_mpesa:
                    paymentMethod = "Mpesa";
                    paymentMethodId = 1;
                    edt_ref.setVisibility(View.VISIBLE);
                    edtL.setVisibility(View.VISIBLE);

                    break;
                case R.id.radio_cash:
                    paymentMethod = "Cash";
                    paymentMethodId = 2;

                    edt_ref.setVisibility(View.GONE);
                    edtL.setVisibility(View.GONE);

                    break;

                case R.id.radio_bank:
                    paymentMethod = "Bank";
                    paymentMethodId = 3;

                    edt_ref.setVisibility(View.VISIBLE);
                    edtL.setVisibility(View.VISIBLE);


                    break;
                default:
                    paymentMethodId = 0;

                    paymentMethod = "";
                    edt_ref.setVisibility(View.GONE);
                    edtL.setVisibility(View.GONE);


            }
        });

        negative1.setOnClickListener(v -> alertDialogAndroid.dismiss());

        if (listAdapterP.getItemCount() < 1) {
            MyToast.errorToast("No payment found", getContext());
        }
    }

    private void refreshFarmerBalance(FamerModel f, String payoutCode) {
        FarmerBalance bal;//= CommonFuncs.getFarmerBalanceAfterPayoutCardApproval(famerModel, balncesViewModel, traderViewModel,payouts);

        bal = balncesViewModel.getByFarmerCodeByPayoutOne(f.getCode(), payoutCode);
        if (bal != null) {
            f.setTotalbalance(bal.getBalanceToPay());
            traderViewModel.updateFarmer(f, false, true);
        }


    }
    public void initList(List<FarmerOrdersTable> farmerOrdersTables) {
        unclickableRows = new ArrayList<>();
        unswipeableRows = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerView);

        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        listAdapter = new LoansOrdersAdapter(getActivity(), null, farmerOrdersTables, new OnclickRecyclerListener() {
            @Override
            public void onMenuItem(int position, int menuItem) {

                // action();
            }

            @Override
            public void onSwipe(int adapterPosition, int direction) {


            }

            @Override
            public void onClickListener(int position) {


                listPayments(farmerOrdersTables.get(position));


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
        traderViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);


        getData();
    }

    public void getData() {

        balncesViewModel.getFarmerOrders().observe(this, farmerOrdersTables -> {
            List<FarmerOrdersTable> farmerOrdersTables1 = new LinkedList<>();


            if (farmerOrdersTables != null) {
                for (FarmerOrdersTable f : farmerOrdersTables) {
                    if (f.getOrderAmount() != null && Double.valueOf(f.getOrderAmount()) > 0) {
                        farmerOrdersTables1.add(f);
                    } else {
                        //balncesViewModel.deleteRecordLoanDirect(f);
                    }
                }
                getPayments(farmerOrdersTables1);

            } else {

            }
        });
    }

    private void getPayments(List<FarmerOrdersTable> farmerOrdersTables1) {
        if (farmerOrdersTables1 != null) {
            for (int a = 0; a < farmerOrdersTables1.size(); a++) {
                List<OrderPayments> lm = balncesViewModel.getOrderPaymentByOrderCodeOne(farmerOrdersTables1.get(a).getCode());
                Double paid = 0.0;
                if (lm != null) {
                    for (OrderPayments lkm : lm) {
                        try {
                            paid = paid + Double.valueOf(lkm.getAmountPaid());

                        } catch (Exception nm) {

                        }
                    }
                    farmerOrdersTables1.get(a).setOrderAmountPaid("" + paid);
                    farmerOrdersTables1.get(a).setOrderPayments(lm);
                    Double amount = Double.valueOf(farmerOrdersTables1.get(a).getOrderAmount());

                    try {
                        if (paid.equals(amount) || paid > amount) {
                            farmerOrdersTables1.get(a).setStatus(1);
                        } else {
                            farmerOrdersTables1.get(a).setStatus(0);
                        }
                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }
                }
            }

        }
        initList(farmerOrdersTables1);
    }

    private void filter(List<FarmerOrdersTable> farmerOrdersTables) {


        setUplist(farmerOrdersTables);
    }

    private void setUplist(List<FarmerOrdersTable> farmerOrdersTables) {

        this.farmerOrdersTables = farmerOrdersTables;
        this.farmerLoansTables = null;
    }
}
