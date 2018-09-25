package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
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
import com.dev.lishabora.Models.FarmerHistoryByDateModel;
import com.dev.lishabora.Models.LoanModel;
import com.dev.lishabora.Models.MonthsDates;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.GeneralUtills;
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

public class FragmentGiveLoan extends Fragment {
    public TextView status, id, name, balance, milk, loan, order;
    ImageView imgAdd, imgRemove, imgDelete;
    TextView txtQty, txtPrice;
    TextInputEditText edtAmount;
    Button btnGiveLoan;
    String ampm = "";
    Cycles c;
    String StringValue = null;
    //String PmStringValue = null;
    Double DoubleValue = 0.0;
    // Double PmDoubleValue = 0.0;
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

//            double mazx=0;
//
//            try{
//                mazx=Double.valueOf(gty);
//            }catch (Exception nm){
//                nm.printStackTrace();
//            }
//
//            if(mazx > 10.0) {

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

        if (DateTimeUtils.Companion.isAM(DateTimeUtils.Companion.getTodayDate())) {

            ampm = "AM";

        } else {

            ampm = "PM";
        }


        StringValue = null;
        //PmStringValue = null;
        DoubleValue = 0.0;
        //PmDoubleValue = 0.0;


        collModel = null;
        //PmCollModel = null;


        collModel = traderViewModel.getCollectionByDateByFarmerByTimeSngle(famerModel.getCode(), DateTimeUtils.Companion.getToday());
        //PmCollModel = traderViewModel.getCollectionByDateByFarmerByTimeSngle(famerModel.getCode(), DateTimeUtils.Companion.getToday(), "PM");


        if (collModel != null) {
            DoubleValue = DoubleValue + Double.valueOf(collModel.getLoanAmountGivenOutPrice());
            StringValue = String.valueOf(DoubleValue);
        }


        if (collModel == null) {

                Collection c = new Collection();
                c.setCycleCode(famerModel.getCyclecode());
                c.setFarmerCode(famerModel.getCode());
                c.setFarmerName(famerModel.getNames());
                c.setCycleId(famerModel.getCode());
                c.setDayName(DateTimeUtils.Companion.getDayOfWeek(DateTimeUtils.Companion.getTodayDate(), "E"));
                c.setLoanAmountGivenOutPrice("0");
                c.setDayDate(DateTimeUtils.Companion.getToday());
            c.setDayDateLog(DateTimeUtils.Companion.getLongDate(c.getDayDate()));

            c.setTimeOfDay(ampm);
            c.setMilkCollectedAm("0");
            c.setMilkCollectedPm("0");
                c.setLoanAmountGivenOutPrice(l);
                c.setLoanDetails(loanDetails);
                c.setOrderGivenOutPrice("0");


                c.setLoanId("");
                c.setOrderId("");
                c.setSynced(0);
                c.setSynced(false);
                c.setApproved(0);


                traderViewModel.createCollections(c, false).observe(FragmentGiveLoan.this, responseModel -> {
                    if (Objects.requireNonNull(responseModel).getResultCode() == 1) {
                        CommonFuncs.addBalance(traderViewModel, balncesViewModel, c, responseModel.getPayoutkey(), AppConstants.LOAN);

                    } else {


                    }


                });
                famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());
            traderViewModel.updateFarmer(famerModel, false, false);
                popOutFragments();

            } else {


            if (collModel != null) {
                collModel.setLoanAmountGivenOutPrice(l);
                collModel.setLoanDetails(loanDetails);
                }


            traderViewModel.updateCollection(collModel).observe(FragmentGiveLoan.this, responseModel -> {
                    if (Objects.requireNonNull(responseModel).getResultCode() == 1) {
                        CommonFuncs.updateBalance(traderViewModel, balncesViewModel, collModel, responseModel.getPayoutkey(), AppConstants.LOAN);

                    } else {


                    }
                });
                famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());
            traderViewModel.updateFarmer(famerModel, false, false);
                popOutFragments();

            }





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
                createMonthlyList(collections);
            } else {
                initMonthlyList(null);
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


        btnGiveLoan.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(edtAmount.getText().toString()) && edtAmount.getText() != null && !edtAmount.getText().toString().equals("0.0")) {


                LoanModel loanModel = new LoanModel();
                loanModel.setLoanAmount(edtAmount.getText().toString());
                loanModel.setInstallmentAmount(txtPrice.getText().toString());
                loanModel.setInstallmentsNo(txtQty.getText().toString());
                giveLoan(edtAmount.getText().toString(), new Gson().toJson(loanModel));
            }
        });
    }

    private void createMonthlyList(List<Collection> collections) {

        List<MonthsDates> monthsDates = DateTimeUtils.Companion.getMonths(12);
        if (monthsDates != null && monthsDates.size() > 0) {

            LinkedList<FarmerHistoryByDateModel> fmh = new LinkedList<>();

            for (MonthsDates mds : monthsDates) {

                String[] totals = CommonFuncs.getCollectionsTotals(mds, collections);
                fmh.add(new FarmerHistoryByDateModel(mds, famerModel, totals[0], totals[1], totals[2], totals[3]));

            }
            initMonthlyList(fmh);

        }


    }


    public void initMonthlyList(List<FarmerHistoryByDateModel> models) {

        this.modelsDA = models;

        double milkTotal = 0.0, loanTotal = 0, OrderTotal = 0;
        for (FarmerHistoryByDateModel f : models) {
            milkTotal = milkTotal + Double.valueOf(f.getMilktotal());

            loanTotal = loanTotal + Double.valueOf(f.getLoanTotal());

            OrderTotal = OrderTotal + Double.valueOf(f.getOrderTotal());
        }


        if (models == null) {
            models = new LinkedList<>();
        }
//        String[] months = new String[models.size()];
//        for (int a = 0; a < models.size(); a++) {
//            months[a] = models.get(a).getMonthsDates().getMonthName() + " ";
//
//        }

//        String m = DateTimeUtils.Companion.getMonth(DateTimeUtils.Companion.getToday());
//
//        spinnerMonths.setItems(months);
//        int curr = 0;
//        for (int a = 0; a < months.length; a++) {
//            if (months[a].contains(m)) {
//                curr = a;
//            }
//        }
//        try {
//            spinnerMonths.setSelectedIndex(curr);
//            setData(modelsDA.get(curr));
//        }catch (Exception nm){
//            nm.printStackTrace();
//        }
//        spinnerMonths.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
//                setData(modelsDA.get(position));
//            }

//        });
        int x = 1;
        if (models.size() > 0) {
            x = models.size();
        }

        setData(String.valueOf(milkTotal / x), String.valueOf(loanTotal / x), String.valueOf(OrderTotal / x));

    }

    private void setData(String s, String s1, String s2) {
        id.setText(famerModel.getCode());
        name.setText(famerModel.getNames());
        milk.setText(GeneralUtills.Companion.round(s, 0));
        loan.setText(GeneralUtills.Companion.round(s1, 0));
        order.setText(GeneralUtills.Companion.round(s2, 0));


    }

    private void setData(FarmerHistoryByDateModel farmerHistoryByDateModel) {
        id.setText(famerModel.getCode());
        name.setText(famerModel.getNames());
        milk.setText(farmerHistoryByDateModel.getMilktotal());
        loan.setText(farmerHistoryByDateModel.getLoanTotal());
        order.setText(farmerHistoryByDateModel.getOrderTotal());

    }


}
