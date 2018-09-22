package com.dev.lishabora.Views.Trader.Fragments;

import android.content.Context;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.Cycles;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.MilkModel;
import com.dev.lishabora.Models.UnitsModel;
import com.dev.lishabora.NumKey.NumberKeyboard;
import com.dev.lishabora.NumKey.NumberKeyboardListener;
import com.dev.lishabora.Utils.CollectListener;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;

import java.util.Objects;

//import com.fxn769.Numpad;

class CollectMilk implements NumberKeyboardListener {
    private static final double MAX_ALLOWED_AMOUNT = 9999.99;
    private static final int MAX_ALLOWED_DECIMALS = 1;
    private int EDIT_CLICKED = 0;
    private int EDTAM = 1;
    private int EDTPM = 2;
    private boolean isKeypadVisible;
    private CollectListener listener;
    private boolean isAm = false;
    private Cycles c;
    private TraderViewModel mViewModel;
    private Context context;
    private Collection collModel = null;
    private boolean hasAmChanged = false;
    private boolean hasPmChanged = false;
    private AlertDialog alertDialogAndroid;
    private MaterialButton btnPositive, btnNegative, btnNeutral;
    private TextView names, balance, day1, day2, day3, day1am, day1pm, day2am, day2pm, day3am, day3pm, today, unitName, unitPrice, unitTotal;
    private TextInputEditText edtTodayAm, edtTodayPm;
    //private Numpad numpad;
    private NumberKeyboard numberKeyboard;
    //private TextView amountEditText;
    private String amountText;
    private double amount;


    CollectMilk(Context context, TraderViewModel traderViewModel, CollectListener listener) {
        this.mViewModel = traderViewModel;
        this.context = context;
        this.listener = listener;
        this.amountText = "";
        this.amount = 0.0;
        setUpCollDialog();
    }

    private void clearDialog() {
        day1.setText("");
        day1am.setText("");
        day1pm.setText("");

        day2.setText("");
        day2am.setText("");
        day2pm.setText("");

        day3.setText("");
        day3am.setText("");
        day3pm.setText("");

        today.setText("");

        unitName.setText("");
        unitPrice.setText("");
        unitTotal.setText("");

        edtTodayAm.setText("");
        edtTodayPm.setText("");

        //numberKeyboard.setVisibility(View.GONE);


    }

    private void setUpCollDialog() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_collect_milk, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(context));
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput.setCancelable(false);
        alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        Objects.requireNonNull(alertDialogAndroid.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        names = mView.findViewById(R.id.txt_name);
        balance = mView.findViewById(R.id.txt_balance);

        day1 = mView.findViewById(R.id.txt_day_1);
        day1am = mView.findViewById(R.id.txt_day_1_am);
        day1pm = mView.findViewById(R.id.txt_day_1_pm);

        day2 = mView.findViewById(R.id.txt_day_2);
        day2am = mView.findViewById(R.id.txt_day_2_am);
        day2pm = mView.findViewById(R.id.txt_day_2_pm);

        day3 = mView.findViewById(R.id.txt_day_3);
        day3am = mView.findViewById(R.id.txt_day_3_am);
        day3pm = mView.findViewById(R.id.txt_day_3_pm);

        today = mView.findViewById(R.id.txt_today);

        unitName = mView.findViewById(R.id.txtUnitName);
        unitPrice = mView.findViewById(R.id.txtUnitPrice);
        unitTotal = mView.findViewById(R.id.txtCost);

        edtTodayAm = mView.findViewById(R.id.edt_am);
        edtTodayPm = mView.findViewById(R.id.edt_pm);


        btnPositive = mView.findViewById(R.id.btn_positive);
        btnNegative = mView.findViewById(R.id.btn_negative);
        btnNeutral = mView.findViewById(R.id.btn_neutral);
        TextView txtTitle = mView.findViewById(R.id.txt_title);
        LinearLayout lTitle = mView.findViewById(R.id.linear_title);
        ImageView imgIcon = mView.findViewById(R.id.img_icon);


        btnNeutral.setVisibility(View.GONE);
        lTitle.setVisibility(View.GONE);
        txtTitle.setVisibility(View.VISIBLE);
        imgIcon.setVisibility(View.GONE);
        imgIcon.setImageResource(R.drawable.ic_add_black_24dp);
        txtTitle.setText("Milk Collection");

        numberKeyboard = mView.findViewById(R.id.numberKeyboard);


//         numpad = mView.findViewById(R.id.num);
//         numpad.setTextSize(16);
//         numpad.setGridBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
//         numpad.setGridThickness(2);
//         numpad.setGridVisible(true);
//         numpad.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));


    }


    void collectMilk(FamerModel famerModel) {


        clearDialog();

        Double amDoubleValue = 0.0;
        Double pmDoubleValue = 0.0;
        collModel = null;
        collModel = mViewModel.getCollectionByDateByFarmerByTimeSngle(famerModel.getCode(), DateTimeUtils.Companion.getToday());

        if (collModel != null) {
            if (collModel.getMilkCollectedAm() != null) {
                amDoubleValue = amDoubleValue + Double.valueOf(collModel.getMilkCollectedAm());
                // amStringValue = String.valueOf(amDoubleValue);
            }
            if (collModel.getMilkCollectedPm() != null) {
                pmDoubleValue = pmDoubleValue + Double.valueOf(collModel.getMilkCollectedIdPm());
                // pmStringValue = String.valueOf(pmDoubleValue);
            }
        }


        alertDialogAndroid.show();


        UnitsModel unitsModel = new UnitsModel();
        unitsModel.setUnitcapacity(famerModel.getUnitcapacity());
        unitsModel.setUnitprice(famerModel.getUnitprice());
        unitsModel.setUnit(famerModel.getUnitname());


        names.setText(famerModel.getNames());
        balance.setText(GeneralUtills.Companion.round(famerModel.getTotalbalance(), 1));

        today.setText(DateTimeUtils.Companion.getDayOfWeek(DateTimeUtils.Companion.getTodayDate(), "E"));
        day3.setText(DateTimeUtils.Companion.getDayPrevious(1, "E"));
        day2.setText(DateTimeUtils.Companion.getDayPrevious(2, "E"));
        day1.setText(DateTimeUtils.Companion.getDayPrevious(3, "E"));

        unitName.setText(unitsModel.getUnit());
        unitPrice.setText(unitsModel.getUnitprice());


        getCollection(famerModel.getCode(), DateTimeUtils.Companion.getDatePrevious(3), day1am, day1pm);
        getCollection(famerModel.getCode(), DateTimeUtils.Companion.getDatePrevious(2), day2am, day2pm);
        getCollection(famerModel.getCode(), DateTimeUtils.Companion.getDatePrevious(1), day3am, day3pm);
        getCollection(famerModel.getCode(), DateTimeUtils.Companion.getToday(), edtTodayAm, edtTodayPm);


        try {
            if (!TextUtils.isEmpty(edtTodayAm.getText())) {
                edtTodayAm.setSelection(edtTodayAm.getText().length());
            }
            if (!TextUtils.isEmpty(edtTodayPm.getText())) {
                edtTodayPm.setSelection(edtTodayPm.getText().length());
            }
        } catch (Exception nm) {
            nm.printStackTrace();
        }

//        numpad.setOnTextChangeListner((String text, int digits_remaining) -> {
//            Log.d("input",text+"  "+digits_remaining);
//        });


        edtTodayAm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                hasAmChanged = true;
                if (editable != null && editable.length() > 0) {
                    if (unitsModel.getUnitprice() != null) {
                        try {

                            Double price = Double.valueOf(unitsModel.getUnitprice());
                            Double unitCapacity = Double.valueOf(unitsModel.getUnitcapacity()) / 1000;
                            Double total = (Double.valueOf(edtTodayAm.getText().toString()) * unitCapacity) * price;


                            unitTotal.setText(String.valueOf(GeneralUtills.Companion.round(total, 2)));

                        } catch (Exception nm) {
                            nm.printStackTrace();
                        }
                    }
                } else {
                    unitTotal.setText("");
                }
            }
        });
        edtTodayPm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                hasPmChanged = true;
                if (editable != null && editable.length() > 0) {
                    if (unitsModel.getUnitprice() != null) {

                        Double price = Double.valueOf(unitsModel.getUnitprice());
                        Double unitCapacity = Double.valueOf(unitsModel.getUnitcapacity()) / 1000;
                        Double total = (Double.valueOf(edtTodayPm.getText().toString()) * unitCapacity) * price;


                        unitTotal.setText(String.valueOf(GeneralUtills.Companion.round(total, 2)));

                    }
                } else {
                    unitTotal.setText("");
                }
            }
        });

        edtTodayAm.setOnClickListener(view -> {
            edtSet(EDTAM);
        });
        edtTodayPm.setOnClickListener(view -> {
            edtSet(EDTPM);
        });


        numberKeyboard.setListener(this);


        if (DateTimeUtils.Companion.isAM(DateTimeUtils.Companion.getTodayDate())) {
            edtSet(EDTAM);


        } else {
            edtSet(EDTPM);


        }


        btnPositive.setOnClickListener(view -> {
            String milkAm = "0";
            String milkPm = "0";
            if (!TextUtils.isEmpty(edtTodayAm.getText().toString())) {
                milkAm = edtTodayAm.getText().toString();
                //     Timber.tag("milkCollDebug").d("Milk Am As On Button Click " + milkAm);

            }
            if (!TextUtils.isEmpty(edtTodayPm.getText().toString())) {
                milkPm = edtTodayPm.getText().toString();
                //Timber.tag("milkCollDebug").d("Milk Am As On Button Click " + milkPm);


            }


            if (hasAmChanged && !TextUtils.isEmpty(edtTodayAm.getText())) {
                //Timber.tag("milkCollDebug").d("HAS AM CHANGED TRUE ");
                MilkModel milkModel = new MilkModel();
                milkModel.setUnitQty(milkAm);
                milkModel.setUnitsModel(unitsModel);


                if (collModel == null) {

                    //Timber.tag("milkCollDebug").d("AM STRING - NULL, AM DOUBLE - NULL AM COLL MODEL - NULL DOING A NEW AM COLLECTION MILK " + milkAm);


                    Collection c = new Collection();
                    c.setCycleCode(famerModel.getCyclecode());
                    c.setFarmerCode(famerModel.getCode());
                    c.setFarmerName(famerModel.getNames());
                    c.setCycleId(famerModel.getCode());
                    c.setDayName(today.getText().toString());
                    c.setLoanAmountGivenOutPrice("0");
                    c.setDayDate(DateTimeUtils.Companion.getToday());
                    c.setDayDateLog(DateTimeUtils.Companion.getLongDate(c.getDayDate()));
                    c.setTimeOfDay("AM");


                    c.setMilkCollectedAm(milkAm);
                    c.setMilkCollectedValueKshAm(milkModel.getValueKsh());
                    c.setMilkCollectedValueLtrsAm(milkModel.getValueLtrs());
                    c.setMilkDetailsAm(new Gson().toJson(milkModel));


                    c.setLoanAmountGivenOutPrice("0");
                    c.setOrderGivenOutPrice("0");

                    c.setLoanId("");
                    c.setOrderId("");
                    c.setSynced(0);
                    c.setSynced(false);
                    c.setApproved(0);


                    listener.createCollection(c);
//                    mViewModel.createCollections(c, false).observe(FragementFarmersList.this, responseModel -> {
//                        if (Objects.requireNonNull(responseModel).getResultCode() == 1) {
//                        } else {
//                            snack(responseModel.getResultDescription());
//
//                        }
//
//
//                    });


                    famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());
                    mViewModel.updateFarmer(famerModel, false, false);
                } else {

                    //Timber.tag("milkCollDebug").d("AM STRING -! NULL, AM DOUBLE - !NULL AM COLL MODEL - !NULL DOING AN UPDATE AM COLLECTION MILK " + milkAm);

                    //UPDATE COLLECTION AS THERE WAS A PREVIOUS AM COLLECTION FOR THIS FARMER ON THIS DAY AND TIME

                    collModel.setMilkCollectedAm(milkAm);
                    collModel.setMilkCollectedValueKshAm(milkModel.getValueKsh());
                    collModel.setMilkCollectedValueLtrsAm(milkModel.getValueLtrs());
                    collModel.setMilkDetailsAm(new Gson().toJson(milkModel));


                    listener.updateCollection(collModel);

//                    mViewModel.updateCollection(collModel).observe(FragementFarmersList.this, responseModel -> {
//                        if (Objects.requireNonNull(responseModel).getResultCode() == 1) {
//                        } else {
//                            snack(responseModel.getResultDescription());
//
//                        }
//                    });
                    famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());
                    mViewModel.updateFarmer(famerModel, false, false);

                }


            }

            if (hasPmChanged && !TextUtils.isEmpty(edtTodayPm.getText())) {
                //Timber.tag("milkCollDebug").d("HAS PM CHANGED -TRUE ");
                MilkModel milkModel = new MilkModel();
                milkModel.setUnitQty(milkPm);
                milkModel.setUnitsModel(unitsModel);


                if (collModel == null) {

                    //Timber.tag("milkCollDebug").d("PM STRING - NULL, PM DOUBLE - NULL PM COLL MODEL - NULL DOING A NEW PM COLLECTION MILK " + milkPm);

                    Collection c = new Collection();
                    c.setCycleCode(famerModel.getCyclecode());
                    c.setFarmerCode(famerModel.getCode());
                    c.setFarmerName(famerModel.getNames());
                    c.setCycleId(famerModel.getCode());
                    c.setDayName(today.getText().toString());
                    c.setLoanAmountGivenOutPrice("0");
                    c.setDayDate(DateTimeUtils.Companion.getToday());
                    c.setDayDateLog(DateTimeUtils.Companion.getLongDate(c.getDayDate()));

                    c.setTimeOfDay("PM");


                    c.setMilkCollectedPm(milkPm);
                    c.setMilkCollectedValueKshPm(milkModel.getValueKsh());
                    c.setMilkCollectedValueLtrsPm(milkModel.getValueLtrs());
                    c.setMilkDetailsAm(new Gson().toJson(milkModel));


                    c.setLoanAmountGivenOutPrice("0");
                    c.setOrderGivenOutPrice("0");

                    c.setLoanId("");
                    c.setOrderId("");
                    c.setSynced(0);
                    c.setSynced(false);
                    c.setApproved(0);


                    listener.createCollection(c);
//                    mViewModel.createCollections(c, false).observe(FragementFarmersList.this, responseModel -> {
//                        if (Objects.requireNonNull(responseModel).getResultCode() == 1) {
//
//                        } else {
//                            snack(responseModel.getResultDescription());
//
//                        }
//
//
//                    });
                    famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());
                    mViewModel.updateFarmer(famerModel, false, false);

                } else {
                    //UPDATE COLLECTION AS THERE WAS A PREVIOUS PM COLLECTION FOR THIS FARMER ON THIS DAY AND TIME
                    // Timber.tag("milkCollDebug").d("PM STRING -! NULL, PM DOUBLE - !NULL AM COLL MODEL - !NULL DOING AN UPDATE PM COLLECTION  MILK " + milkPm);


                    collModel.setMilkCollectedPm(milkPm);
                    collModel.setMilkCollectedValueKshPm(milkModel.getValueKsh());
                    collModel.setMilkCollectedValueLtrsPm(milkModel.getValueLtrs());
                    collModel.setMilkDetailsPm(new Gson().toJson(milkModel));

                    listener.updateCollection(collModel);
//                    mViewModel.updateCollection(collModel).observe(FragementFarmersList.this, responseModel -> {
//                        if (Objects.requireNonNull(responseModel).getResultCode() == 1) {
//                        } else {
//                            snack(responseModel.getResultDescription());
//
//                        }
//                    });
                    famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());
                    mViewModel.updateFarmer(famerModel, false, false);


                }


            }


            alertDialogAndroid.dismiss();


        });
        btnNeutral.setOnClickListener(view -> {

        });
        btnNegative.setOnClickListener(view -> alertDialogAndroid.dismiss());

    }

    private void getCollection(String code, String date, TextView txtAm, TextView txtPm) {

        // Timber.tag("tagssearch").d(code + "  Date " + date);
        Collection c = mViewModel.getCollectionByDateByFarmerByTimeSngle(code, date);//.observe(FragementFarmersList.this, collections -> {

        if (txtAm != null && txtPm != null) {
            if (c != null) {
                if (c.getMilkCollectedAm() != null && !c.getMilkCollectedAm().equals("0.0") && !c.getMilkCollectedAm().equals("0")) {
                    txtAm.setText(c.getMilkCollectedAm());

                }
                if (c.getMilkCollectedPm() != null && !c.getMilkCollectedPm().equals("0.0") && !c.getMilkCollectedPm().equals("0")) {
                    txtPm.setText(c.getMilkCollectedPm());

                }


            }
        }


    }

    @Override
    public void onNumberClicked(int number) {
        if (amountText.isEmpty() && number == 0) {
            return;
        }
        updateAmount(amountText + number);
    }

    @Override
    public void onLeftAuxButtonClicked() {
        if (!hasComma(amountText)) {
            amountText = amountText.isEmpty() ? "0." : amountText + ".";
            showAmount(amountText);
        }
    }

    @Override
    public void onRightAuxButtonClicked() {
        if (amountText.isEmpty()) {
            return;
        }
        String newAmountText;
        if (amountText.length() <= 1) {
            newAmountText = "";
        } else {
            newAmountText = amountText.substring(0, amountText.length() - 1);
            if (newAmountText.charAt(newAmountText.length() - 1) == '.') {
                newAmountText = newAmountText.substring(0, newAmountText.length() - 1);
            }
            if ("0".equals(newAmountText)) {
                newAmountText = "";
            }
        }
        updateAmount(newAmountText);
    }

    private void updateAmount(String newAmountText) {
        double newAmount = newAmountText.isEmpty() ? 0.0 : Double.parseDouble(newAmountText.replaceAll(",", "."));
        if (newAmount >= 0.0 && newAmount <= MAX_ALLOWED_AMOUNT
                && getNumDecimals(newAmountText) <= MAX_ALLOWED_DECIMALS) {
            amountText = newAmountText;
            amount = newAmount;
            showAmount(amountText);
        }
    }

    private int getNumDecimals(String num) {
        if (!hasComma(num)) {
            return 0;
        }
        return num.substring(num.indexOf('.') + 1, num.length()).length();
    }

    private boolean hasComma(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '.') {
                return true;
            }
        }
        return false;
    }

    private void showAmount(String amount) {
        switch (EDIT_CLICKED) {
            case 1:
                edtTodayAm.setText(amount);
                break;
            case 2:
                edtTodayPm.setText(amount);
                break;
        }
        // amountEditText.setText("€" + (amount.isEmpty() ? "0" : addThousandSeparator(amount)));
    }

    void edtSet(int edtClicked) {
        switch (edtClicked) {
            case 1:
                if (edtTodayAm.getText().toString() != null && !TextUtils.isEmpty(edtTodayAm.getText().toString())) {
                    this.amountText = edtTodayAm.getText().toString();
                    this.amount = Double.valueOf(amountText);
                } else {
                    this.amountText = "";
                    this.amount = 0.0;
                }

                edtTodayAm.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                edtTodayPm.setBackgroundColor(context.getResources().getColor(R.color.transparent));

                edtTodayAm.setTextColor(context.getResources().getColor(R.color.white));
                edtTodayPm.setTextColor(context.getResources().getColor(R.color.black));
                EDIT_CLICKED = EDTAM;
                break;
            case 2:
                if (edtTodayPm.getText().toString() != null && !TextUtils.isEmpty(edtTodayPm.getText().toString())) {

                    this.amountText = edtTodayPm.getText().toString();
                    this.amount = Double.valueOf(amountText);
                } else {
                    this.amountText = "";
                    this.amount = 0.0;
                }
                EDIT_CLICKED = EDTPM;
                edtTodayPm.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                edtTodayAm.setBackgroundColor(context.getResources().getColor(R.color.transparent));
                edtTodayPm.setTextColor(context.getResources().getColor(R.color.white));
                edtTodayAm.setTextColor(context.getResources().getColor(R.color.black));

                break;
        }
    }

}
