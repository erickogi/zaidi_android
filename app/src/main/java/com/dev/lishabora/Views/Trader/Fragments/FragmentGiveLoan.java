package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.lishabora.AppConstants;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.Cycles;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.FarmerBalance;
import com.dev.lishabora.Models.FarmerHistoryByDateModel;
import com.dev.lishabora.Models.LoanModel;
import com.dev.lishabora.Models.ResponseModel;
import com.dev.lishabora.Models.Trader.FarmerLoansTable;
import com.dev.lishabora.Utils.CollectListener;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.Utils.InputFilterMinMax;
import com.dev.lishabora.Utils.Logs;
import com.dev.lishabora.ViewModels.Trader.BalncesViewModel;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class FragmentGiveLoan extends Fragment implements CollectListener {
    public TextView status, id, name, balance, milk, loan, order;
    ImageView imgAdd, imgRemove, imgDelete;
    TextView txtQty, txtPrice;
    TextInputEditText edtAmount;
    Button btnGiveLoan;
    String ampm = "";
    Cycles c;
    String StringValue = null;

    Double DoubleValue = 0.0;

    Collection collModel = null;
    // Collection PmCollModel = null;
    private FamerModel famerModel;



    boolean hasAmChanged = false;
    boolean hasPmChanged = false;
    private MaterialSpinner spinnerMonths;

    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private PayoutsVewModel payoutsVewModel;
    private TraderViewModel traderViewModel;
    private BalncesViewModel balncesViewModel;
    private View view;
    private List<FarmerHistoryByDateModel> modelsDA = new LinkedList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);
        traderViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);

        balncesViewModel = ViewModelProviders.of(this).get(BalncesViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_give_loan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        famerModel = (FamerModel) getArguments().getSerializable("farmer");


    }

    private void getCollection(String code, String date, TextInputEditText txt, TextView txtPrice, TextView txtQty) {

        List<Collection> collections = traderViewModel.getCollectionByDateByFarmer(code, date);//.observe(FragementFarmersList.this, collections -> {


        if (collections != null) {

            LoanModel l = CommonFuncs.getLoan(collections);
            if (l != null) {
                if (l.getLoanAmount() != null && !l.getLoanAmount().equals("0.0"))
                    txt.setText(l.getLoanAmount());

                txtPrice.setText(l.getInstallmentAmount());
                txtQty.setText(l.getInstallmentsNo());


            }
        }

    }

    public void calc(View imgAction, View txtQty) {
        String gty = ((TextView) txtQty).getText().toString();

        if (!TextUtils.isEmpty(((TextView) txtQty).getText().toString())) {


            if (imgAction.getId() == R.id.img_add) {
                int vq = Integer.valueOf(gty) + 1;
                if (vq <= 10) {
                    ((TextView) txtQty).setText(String.valueOf(vq));
                }


            } else {
                int vq = Integer.valueOf(gty);
                if (vq != 1) {
                    ((TextView) txtQty).setText(String.valueOf(vq - 1));
                }
            }


            double installmentValue = 0.0;

            if (edtAmount.getText() != null && !TextUtils.isEmpty(edtAmount.getText())) {

                double value = Double.valueOf(edtAmount.getText().toString());
                int insNo = Integer.valueOf(((TextView) txtQty).getText().toString());
                if (value > 0.0) {
                    installmentValue = (value / insNo);
                }
            }

            txtPrice.setText(String.valueOf(GeneralUtills.Companion.round(installmentValue, 2)));

//            } else {
//                MyToast.toast("You reached maximum value",getContext(),R.drawable.ic_error_outline_black_24dp,Toast.LENGTH_LONG);
//            }

        } else {
            ((TextView) txtQty).setText("1");
        }

    }

    private void giveLoan(String l, String loanDetails) {

        CommonFuncs.giveLoan(l, loanDetails, this, traderViewModel, famerModel);

    }

    void popOutFragments() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {
        btnGiveLoan = view.findViewById(R.id.btn_give_loan);

        spinnerMonths = view.findViewById(R.id.spinner_months);

        id = view.findViewById(R.id.txt_id);
        name = view.findViewById(R.id.txt_name);

        edtAmount = view.findViewById(R.id.edt_value);
        txtQty = view.findViewById(R.id.txt_qty);
        txtPrice = view.findViewById(R.id.txt_installment);

        imgAdd = view.findViewById(R.id.img_add);
        imgRemove = view.findViewById(R.id.img_remove);

        imgAdd.setOnClickListener(view -> calc(imgAdd, txtQty));


        imgRemove.setOnClickListener(view -> calc(imgRemove, txtQty));


        milk = view.findViewById(R.id.txt_milk);
        loan = view.findViewById(R.id.txt_loans);
        order = view.findViewById(R.id.txt_orders);

        payoutsVewModel.getCollectionByFarmer(famerModel.getCode()).observe(this, collections -> {
            if (collections != null && collections.size() > 0) {
                //  createMonthlyList(collections);
            } else {
                // initMonthlyList(null);
            }
        });


        edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                double installmentValue = 0.0;

                if (editable != null) {

                    if (edtAmount.getText().toString() != null && !TextUtils.isEmpty(edtAmount.getText().toString())) {
                        double value = Double.valueOf(edtAmount.getText().toString());
                        if (TextUtils.isEmpty(txtQty.getText().toString())) {
                            txtQty.setText("1");
                        }
                        int insNo = Integer.valueOf(txtQty.getText().toString());
                        if (value > 0.0) {
                            installmentValue = (value / insNo);
                        }
                    }
                }
                txtPrice.setText(String.valueOf(GeneralUtills.Companion.round(installmentValue, 2)));
            }
        });


        getCollection(famerModel.getCode(), DateTimeUtils.Companion.getToday(), edtAmount, txtPrice, txtQty);
        edtAmount.setFilters(new InputFilter[]{new InputFilterMinMax(1, 10000)});


        btnGiveLoan.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(edtAmount.getText().toString()) && edtAmount.getText() != null && !edtAmount.getText().toString().equals("0.0")) {


                LoanModel loanModel = new LoanModel();
                loanModel.setLoanAmount(edtAmount.getText().toString());
                loanModel.setInstallmentAmount(txtPrice.getText().toString());
                loanModel.setInstallmentsNo(txtQty.getText().toString());
                giveLoan(edtAmount.getText().toString(), new Gson().toJson(loanModel));
            }
        });

        fetchBalance();
    }

    private void createMonthlyList(List<Collection> collections) {

//        List<MonthsDates> monthsDates = DateTimeUtils.Companion.getMonths(12);
//        if (monthsDates != null && monthsDates.size() > 0) {
//
//            LinkedList<FarmerHistoryByDateModel> fmh = new LinkedList<>();
//
//            for (MonthsDates mds : monthsDates) {
//
//                String[] totals = CommonFuncs.getCollectionsTotals(mds, collections);
//                fmh.add(new FarmerHistoryByDateModel(mds, famerModel, totals[0], totals[1], totals[2], totals[3]));
//
//            }
//            initMonthlyList(fmh);
//
//        }


        //PayoutFarmersCollectionModel collectionModels=CommonFuncs.getFarmersCollectionModel(famerModel, collections, payouts, balncesViewModel);


    }

    private void fetchBalance() {
        balncesViewModel.getByFarmerCode(famerModel.getCode()).observe(this, new Observer<List<FarmerBalance>>() {
            @Override
            public void onChanged(@Nullable List<FarmerBalance> farmerBalances) {
                if (farmerBalances != null) {
                    calculate(farmerBalances);
                }
            }
        });
    }

    private void calculate(List<FarmerBalance> farmerBalances) {
        Double balance = 0.0;
        for (FarmerBalance f : farmerBalances) {
            balance = balance + Double.valueOf(f.getBalanceToPay());

        }
        try {
            Double ave = balance / farmerBalances.size();
            setAveareage(String.valueOf(ave));


        } catch (Exception nm) {
            nm.printStackTrace();
        }

    }

    private void setAveareage(String s) {
        milk.setText(GeneralUtills.Companion.round(s, 0));

    }


//    public void initMonthlyList(List<FarmerHistoryByDateModel> models) {
//
//        this.modelsDA = models;
//
//        double milkTotal = 0.0, loanTotal = 0, OrderTotal = 0;
//
//        if (models != null) {
//            for (FarmerHistoryByDateModel f : models) {
//                milkTotal = milkTotal + Double.valueOf(f.getMilktotal());
//
//                loanTotal = loanTotal + Double.valueOf(f.getLoanTotal());
//
//                OrderTotal = OrderTotal + Double.valueOf(f.getOrderTotal());
//            }
//
//        }
//
//        if (models == null) {
//            models = new LinkedList<>();
//        }
//
//        int x = 1;
//        if (models.size() > 0) {
//            x = models.size();
//        }
//
//        List<Collection> collections = payoutsVewModel.getCollectionByDateByPayoutByFarmerListOne(famerModel.getCurrentPayoutCode(), famerModel.getCode());//.observe(this);
//
//        if (collections != null) {
//
//        }
//        setData(String.valueOf(milkTotal / x), String.valueOf(loanTotal / x), String.valueOf(OrderTotal / x));
//
//    }
//
//    private void setData(String s, String s1, String s2) {
//        id.setText(famerModel.getCode());
//        name.setText(famerModel.getNames());
//        milk.setText(GeneralUtills.Companion.round(s, 0));
//        loan.setText(GeneralUtills.Companion.round(s1, 0));
//        order.setText(GeneralUtills.Companion.round(s2, 0));
//
//
//    }

    private void setData(FarmerHistoryByDateModel farmerHistoryByDateModel) {
        id.setText(famerModel.getCode());
        name.setText(famerModel.getNames());
        milk.setText(farmerHistoryByDateModel.getMilktotal());
        loan.setText(farmerHistoryByDateModel.getLoanTotal());
        order.setText(farmerHistoryByDateModel.getOrderTotal());

    }

    private void snack(String msg) {
        if (view != null) {
            Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null);//.show();

            AlertDialog.Builder d = new AlertDialog.Builder(getContext());
            d.setMessage(msg);
            d.setCancelable(true);
            d.setPositiveButton("Okay", (dialogInterface, i) -> dialogInterface.dismiss());
            d.show();

        }
    }

    @Override
    public void createCollection(Collection c, FamerModel famerModel, Double aDouble, Double milk) {
        Logs.Companion.d("Colllectionn",c);


        traderViewModel.createCollections(c).observe(FragmentGiveLoan.this, responseModel -> {
            if (Objects.requireNonNull(responseModel).getResultCode() == 1) {

                FarmerLoansTable f = balncesViewModel.getFarmerLoanByCollectionOne(c.getCode());

                famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());
                boolean hasToSyncFarmer = false;
                if (!famerModel.getCurrentPayoutCode().equals(responseModel.getPayoutCode())) {
                    hasToSyncFarmer = true;
                }
                famerModel.setCurrentPayoutCode(responseModel.getPayoutCode());
                FamerModel fm = CommonFuncs.updateBalance(famerModel, traderViewModel, balncesViewModel, c, responseModel.getPayoutCode(), AppConstants.LOAN, f, null);
                traderViewModel.updateFarmer(fm, false, hasToSyncFarmer);
                popOutFragments();

            } else {

                snack(responseModel.getResultDescription());

            }


        });
    }


    @Override
    public void updateCollection(Collection c, FamerModel famerModel, Double aDouble, Double milk) {


        ResponseModel responseModel = traderViewModel.updateCollection(c);//.observe(FragmentGiveLoan.this, responseModel -> {
            if (Objects.requireNonNull(responseModel).getResultCode() == 1) {
                FarmerLoansTable f = balncesViewModel.getFarmerLoanByCollectionOne(c.getCode());

                famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());
                boolean hasToSyncFarmer = false;

                if (!famerModel.getCurrentPayoutCode().equals(responseModel.getPayoutCode())) {
                    hasToSyncFarmer = true;
                }
                famerModel.setCurrentPayoutCode(responseModel.getPayoutCode());


                FamerModel fm = CommonFuncs.updateBalance(famerModel, traderViewModel, balncesViewModel, c, responseModel.getPayoutCode(), AppConstants.LOAN, f, null);
                traderViewModel.updateFarmer(fm, false, true);

                popOutFragments();

            } else {
                snack(responseModel.getResultDescription());


            }
        // });
    }

    @Override
    public void error(String error) {

        snack(error);
    }
}
