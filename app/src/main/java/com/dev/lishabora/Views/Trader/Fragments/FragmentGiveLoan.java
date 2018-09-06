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

import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.Cycles;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.FarmerHistoryByDateModel;
import com.dev.lishabora.Models.LoanModel;
import com.dev.lishabora.Models.MonthsDates;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
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
    String AmStringValue = null;
    String PmStringValue = null;
    Double AmDoubleValue = 0.0;
    Double PmDoubleValue = 0.0;
    Collection AmCollModel = null;
    Collection PmCollModel = null;
    boolean hasAmChanged = false;
    boolean hasPmChanged = false;
    private MaterialSpinner spinnerMonths;
    private FamerModel famerModel;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private PayoutsVewModel payoutsVewModel;
    private TraderViewModel traderViewModel;
    private View view;
    private List<FarmerHistoryByDateModel> modelsDA = new LinkedList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);
        traderViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);


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

    private String getCollection(String code, String date, TextInputEditText txt) {

        List<Collection> collections = traderViewModel.getCollectionByDateByFarmer(code, date);//.observe(FragementFarmersList.this, collections -> {

        Double tt = 0.0;

        if (txt != null) {
            if (collections != null) {

                for (Collection c : collections) {


                    tt = tt + (Double.valueOf(c.getLoanAmountGivenOutPrice()));


                }

            }
            txt.setText(String.valueOf(tt));
        }


        return String.valueOf(tt);

    }

    public void calc(View imgAction, View txtQty) {
        String gty = ((TextView) txtQty).getText().toString();

        if (imgAction.getId() == R.id.img_add) {
            int vq = Integer.valueOf(gty) + 1;
            ((TextView) txtQty).setText(String.valueOf(vq));

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


    }

    private void giveLoan(String l, String loanDetails) {

        if (DateTimeUtils.Companion.isAM(DateTimeUtils.Companion.getTodayDate())) {

            ampm = "AM";

        } else {

            ampm = "PM";
        }


        AmStringValue = null;
        PmStringValue = null;
        AmDoubleValue = 0.0;
        PmDoubleValue = 0.0;


        AmCollModel = null;
        PmCollModel = null;


        AmCollModel = traderViewModel.getCollectionByDateByFarmerByTimeSngle(famerModel.getCode(), DateTimeUtils.Companion.getToday(), "AM");
        PmCollModel = traderViewModel.getCollectionByDateByFarmerByTimeSngle(famerModel.getCode(), DateTimeUtils.Companion.getToday(), "PM");


        if (AmCollModel != null) {
            AmDoubleValue = AmDoubleValue + Double.valueOf(AmCollModel.getLoanAmountGivenOutPrice());
            AmStringValue = String.valueOf(AmDoubleValue);
        }

        if (PmCollModel != null) {

            PmDoubleValue = PmDoubleValue + Double.valueOf(PmCollModel.getLoanAmountGivenOutPrice());
            PmStringValue = String.valueOf(PmDoubleValue);
        }


        if (ampm.equals("AM")) {

            if (AmStringValue == null && AmDoubleValue == 0.0 && AmCollModel == null) {
                Collection c = new Collection();
                c.setCycleCode(famerModel.getCyclecode());
                c.setFarmerCode(famerModel.getCode());
                c.setFarmerName(famerModel.getNames());
                c.setCycleId(famerModel.getCode());
                c.setDayName(DateTimeUtils.Companion.getDayOfWeek(DateTimeUtils.Companion.getTodayDate(), "E"));
                c.setLoanAmountGivenOutPrice("0");
                c.setDayDate(DateTimeUtils.Companion.getToday());
                c.setTimeOfDay("AM");
                c.setMilkCollected("0");
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
                    } else {

                    }


                });
                famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());
                traderViewModel.updateFarmer(famerModel, false);
                popOutFragments();

            } else {


                if (AmCollModel != null) {
                    AmCollModel.setLoanAmountGivenOutPrice(l);
                    AmCollModel.setLoanDetails(loanDetails);
                }
                traderViewModel.updateCollection(AmCollModel).observe(FragmentGiveLoan.this, responseModel -> {
                    if (Objects.requireNonNull(responseModel).getResultCode() == 1) {
                    } else {


                    }
                });
                famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());
                traderViewModel.updateFarmer(famerModel, false);
                popOutFragments();

            }


        } else {
            if (PmStringValue == null && PmDoubleValue == 0.0 && PmCollModel == null) {
                Collection c = new Collection();
                c.setCycleCode(famerModel.getCyclecode());
                c.setFarmerCode(famerModel.getCode());
                c.setFarmerName(famerModel.getNames());
                c.setCycleId(famerModel.getCode());
                c.setDayName(DateTimeUtils.Companion.getDayOfWeek(DateTimeUtils.Companion.getTodayDate(), "E"));
                c.setLoanAmountGivenOutPrice("0");
                c.setDayDate(DateTimeUtils.Companion.getToday());
                c.setTimeOfDay("PM");
                c.setMilkCollected("0");
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
                    } else {

                    }


                });
                famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());
                traderViewModel.updateFarmer(famerModel, false);
                popOutFragments();


            } else {

                PmCollModel.setLoanAmountGivenOutPrice(l);
                PmCollModel.setLoanDetails(loanDetails);
                traderViewModel.updateCollection(PmCollModel).observe(FragmentGiveLoan.this, responseModel -> {
                    if (Objects.requireNonNull(responseModel).getResultCode() == 1) {
                    } else {


                    }
                });
                famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());
                traderViewModel.updateFarmer(famerModel, false);
                popOutFragments();

            }
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
                        int insNo = Integer.valueOf(txtQty.getText().toString());
                        if (value > 0.0) {
                            installmentValue = (value / insNo);
                        }
                    }
                }
                txtPrice.setText(String.valueOf(GeneralUtills.Companion.round(installmentValue, 2)));
            }
        });


        getCollection(famerModel.getCode(), DateTimeUtils.Companion.getToday(), edtAmount);


        btnGiveLoan.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(edtAmount.getText().toString())) {


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

                String[] totals = getCollectionsTotals(mds, collections);
                fmh.add(new FarmerHistoryByDateModel(mds, famerModel, totals[0], totals[1], totals[2], totals[3]));

            }
            initMonthlyList(fmh);

        }


    }

    private String[] getCollectionsTotals(MonthsDates mds, List<Collection> collections) {
        String cycleCode = "";
        double milk = 0.0;
        double loan = 0.0;
        double order = 0.0;

        for (Collection collection : collections) {
            if (DateTimeUtils.Companion.isInMonth(collection.getDayDate(), mds.getMonthName())) {
                milk = milk + Double.valueOf(collection.getMilkCollected());
                loan = loan + Double.valueOf(collection.getLoanAmountGivenOutPrice());
                order = order + Double.valueOf(collection.getOrderGivenOutPrice());
            }

        }
        double[] totals = {milk, loan, order};


        return new String[]{String.valueOf(totals[0]), String.valueOf(totals[1]), String.valueOf(totals[2]), cycleCode};
    }

    public void initMonthlyList(List<FarmerHistoryByDateModel> models) {

        this.modelsDA = models;


        if (models == null) {
            models = new LinkedList<>();
        }
        String[] months = new String[models.size()];
        for (int a = 0; a < models.size(); a++) {
            months[a] = models.get(a).getMonthsDates().getMonthName() + " ";

        }

        String m = DateTimeUtils.Companion.getMonth(DateTimeUtils.Companion.getToday());

        spinnerMonths.setItems(months);
        int curr = 0;
        for (int a = 0; a < months.length; a++) {
            if (months[a].contains(m)) {
                curr = a;
            }
        }
        spinnerMonths.setSelectedIndex(curr);
        setData(modelsDA.get(curr));
        spinnerMonths.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                setData(modelsDA.get(position));
            }
        });


    }

    private void setData(FarmerHistoryByDateModel farmerHistoryByDateModel) {
        id.setText(famerModel.getCode());
        name.setText(famerModel.getNames());
        milk.setText(farmerHistoryByDateModel.getMilktotal());
        loan.setText(farmerHistoryByDateModel.getLoanTotal());
        order.setText(farmerHistoryByDateModel.getOrderTotal());

    }

    public int[] getApprovedCards(List<Collection> collections, String pcode) {

        int[] statusR = new int[3];
        int farmerStatus = 0;


        List<FamerModel> f = payoutsVewModel.getFarmersByCycleONe(pcode);


        statusR[0] = f.size();


        int approved = 0;

        for (FamerModel famerModel : f) {
            int status = 0;
            int collectionNo = 0;
            for (Collection c : collections) {


                if (c.getFarmerCode().equals(famerModel.getCode())) {


                    collectionNo = collectionNo + 1;

                    try {
                        status += c.getApproved();

                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }
                }


            }

            if (status == collectionNo) {
                approved += 1;
            }


        }
        statusR[1] = approved;
        statusR[2] = statusR[0] - approved;


        return statusR;


    }

}
