package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishabora.Adapters.FarmerCollectionsAdapter;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.Cycles;
import com.dev.lishabora.Models.DayCollectionModel;
import com.dev.lishabora.Models.DaysDates;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.LoanModel;
import com.dev.lishabora.Models.MilkModel;
import com.dev.lishabora.Models.OrderModel;
import com.dev.lishabora.Models.PayoutFarmersCollectionModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Utils.AdvancedOnclickRecyclerListener;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishaboramobile.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

import static com.dev.lishabora.Views.CommonFuncs.getBalance;
import static com.dev.lishabora.Views.CommonFuncs.getCollectionIdAm;
import static com.dev.lishabora.Views.CommonFuncs.getCollectionIdPm;

public class FragmentCurrentFarmerPayout extends Fragment {

    public TextView status, id, name, balance, milk, loan, order;
    public RelativeLayout background;
    public View statusview;
    Double milkKsh = 0.0;
    private FamerModel famerModel;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private PayoutsVewModel payoutsVewModel;
    private TraderViewModel traderViewModel;
    private Payouts payouts;
    private FarmerCollectionsAdapter listAdapter;
    private RecyclerView recyclerView;
    private List<DayCollectionModel> dayCollectionModels;
    private MaterialButton btnApprove, btnBack;
    private TextView txtApprovalStatus;
    private List<DayCollectionModel> liveModel;
    private List<Collection> collections;
    private AVLoadingIndicatorView avi;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);
        traderViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_farmer_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        famerModel = (FamerModel) getArguments().getSerializable("farmer");
        btnApprove = view.findViewById(R.id.btn_approve);
        btnApprove.setVisibility(View.GONE);

        btnBack = view.findViewById(R.id.btn_back);
        txtApprovalStatus = view.findViewById(R.id.txt_approval_status);

        btnBack.setVisibility(View.GONE);

        statusview = view.findViewById(R.id.status_view);
        background = view.findViewById(R.id.background);


        status = view.findViewById(R.id.txt_status);
        id = view.findViewById(R.id.txt_id);
        name = view.findViewById(R.id.txt_name);
        balance = view.findViewById(R.id.txt_balance);


        milk = view.findViewById(R.id.txt_milk);
        loan = view.findViewById(R.id.txt_loans);
        order = view.findViewById(R.id.txt_orders);


    }

    @Override
    public void onStart() {
        super.onStart();

        initList();
        initData();
    }

    private void setUpDayCollectionsModel(Payouts payouts, List<Collection> collections) {


        List<DaysDates> daysDates = DateTimeUtils.Companion.getDaysAndDatesBtnDates(payouts.getStartDate(), payouts.getEndDate());

        List<DayCollectionModel> dayCollectionModels = new LinkedList<>();
        for (DaysDates d : daysDates) {

            MilkModel milkModelAm = CommonFuncs.getMilk(d.getDate(), "AM", collections);
            MilkModel milkModelPm = CommonFuncs.getMilk(d.getDate(), "PM", collections);


            String milkAm = milkModelAm.getUnitQty();
            String milkPm = milkModelPm.getUnitQty();

            LoanModel loanModelAm = CommonFuncs.getLoan(d.getDate(), "AM", collections);
            LoanModel loanModelPm = CommonFuncs.getLoan(d.getDate(), "PM", collections);


            String loanAm = loanModelAm.getLoanAmount();
            String laonPm = loanModelPm.getLoanAmount();

            OrderModel orderModelAm = CommonFuncs.getOrder(d.getDate(), "AM", collections);
            OrderModel orderModelPm = CommonFuncs.getOrder(d.getDate(), "PM", collections);


            String orderAm = orderModelAm.getOrderAmount();
            String orderPm = orderModelPm.getOrderAmount();


            int collectionIdAm = getCollectionIdAm(d.getDate(), collections);
            int collectionIdPm = getCollectionIdPm(d.getDate(), collections);

            dayCollectionModels.add(new DayCollectionModel(
                    payouts.getPayoutnumber(),
                    d.getDay(),
                    d.getDate(),
                    milkAm,
                    milkPm,
                    loanAm,
                    laonPm,
                    orderAm,
                            orderPm,
                            collectionIdAm,
                            collectionIdPm,
                            milkModelAm, loanModelAm, orderModelAm,
                            milkModelPm, loanModelPm, orderModelPm

                    )

            );
        }

        setUpList(dayCollectionModels);

    }

    private void setUpList(List<DayCollectionModel> dayCollectionModels) {
        this.dayCollectionModels = dayCollectionModels;
        this.liveModel = dayCollectionModels;
        listAdapter.refresh(dayCollectionModels, payouts.getStatus() == 0);


    }


    private void initData() {

        Cycles c = new Cycles();
        c.setCycle(famerModel.getCyclename());
        c.setCode(Integer.valueOf(famerModel.getCyclecode()));


        this.payouts = traderViewModel.createPayout(c);
        if (payouts != null) {
            payoutsVewModel.getCollectionByDateByPayoutByFarmer("" + payouts.getPayoutnumber(), famerModel.getCode()).observe(this, (List<Collection> collections) -> {


                FragmentCurrentFarmerPayout.this.collections = collections;
                setUpDayCollectionsModel(payouts, collections);
                setData(det());


            });
        }



    }


    private int getFarmerStatus(String code) {
        int status = 0;
        int collectsNo = 0;
        for (Collection c : collections) {
            if (c.getFarmerCode().equals(code)) {
                collectsNo++;
                try {
                    status += c.getApproved();
                } catch (Exception nm) {
                    nm.printStackTrace();
                }
            }

        }
        if (status >= collectsNo && status != 0) {
            return 1;
        } else {
            return 0;
        }
    }





    public PayoutFarmersCollectionModel det() {
        String milkTotal = CommonFuncs.getMilk(famerModel.getCode(), collections).getUnitQty();
        String milkTotalKsh = CommonFuncs.getMilk(famerModel.getCode(), collections).getValueKsh();
        String milkTotalLtrs = CommonFuncs.getMilk(famerModel.getCode(), collections).getValueLtrs();
        String loanTotal = CommonFuncs.getLoan(famerModel.getCode(), collections);
        String orderTotal = CommonFuncs.getOrder(famerModel.getCode(), collections);


        int status = getFarmerStatus(famerModel.getCode());
        String statusText;


        statusText = status == 0 ? "Pending" : "Approved";
        String balance = getBalance(famerModel.getCode(), collections);
        return new PayoutFarmersCollectionModel(
                famerModel.getCode(),
                famerModel.getNames(),
                milkTotal,
                loanTotal,
                orderTotal,
                status,
                statusText,
                balance,
                payouts.getPayoutnumber(),
                famerModel.getCyclecode(),
                milkTotalKsh, milkTotalLtrs
        );

    }


    private void setBalance(Double milkKsh) {
        balance.setText(String.format("%s %s", getBalance(String.valueOf(milkKsh), loan.getText().toString(), order.getText().toString()), getActivity().getString(R.string.ksh)));
    }

    private void setData(PayoutFarmersCollectionModel model) {
        //balance.setText(model.getBalance());
        id.setText(model.getFarmercode());
        name.setText(model.getFarmername());
        status.setText(model.getStatusName());

        Objects.requireNonNull(getActivity()).setTitle("" + model.getFarmername() + "        ID " + model.getFarmercode());


        milk.setText(String.format("%s %s", model.getMilktotalLtrs(), getActivity().getString(R.string.ltrs)));
        if (!model.getMilktotal().equals("0.0")) {
            milk.setTextColor(this.getResources().getColor(R.color.colorPrimary));
            milk.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            milk.setTypeface(Typeface.DEFAULT);

            milk.setTextColor(this.getResources().getColor(R.color.black));

        }


        loan.setText(String.format("%s %s", model.getLoanTotal(), getActivity().getString(R.string.ksh)));
        if (!model.getLoanTotal().equals("0.0")) {
            loan.setTextColor(this.getResources().getColor(R.color.colorPrimary));
            loan.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            loan.setTypeface(Typeface.DEFAULT);

            loan.setTextColor(this.getResources().getColor(R.color.black));

        }

        order.setText(String.format("%s %s", model.getOrderTotal(), getActivity().getString(R.string.ksh)));
        if (!model.getOrderTotal().equals("0.0")) {
            order.setTextColor(this.getResources().getColor(R.color.colorPrimary));
            order.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            order.setTypeface(Typeface.DEFAULT);

            order.setTextColor(this.getResources().getColor(R.color.black));

        }

        if (model.getStatus() == 1) {
            //  status.setText("Active");
            status.setTextColor(this.getResources().getColor(R.color.green_color_picker));
            background.setBackgroundColor(this.getResources().getColor(R.color.green_color_picker));
            statusview.setBackgroundColor(this.getResources().getColor(R.color.green_color_picker));


        } else if (model.getStatus() == 0) {

            //  status.setText("Deleted");
            status.setTextColor(this.getResources().getColor(R.color.red));
            background.setBackgroundColor(this.getResources().getColor(R.color.red));
            statusview.setBackgroundColor(this.getResources().getColor(R.color.red));

        } else {
            // status.setText("In-Active");
            status.setTextColor(this.getResources().getColor(R.color.blue_color_picker));
            background.setBackgroundColor(this.getResources().getColor(R.color.blue_color_picker));
            statusview.setBackgroundColor(this.getResources().getColor(R.color.blue_color_picker));

        }


        payoutsVewModel.getSumOfMilkForPayoutLtrs(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> {
            milk.setText(String.format("%s %s", String.valueOf(integer), getActivity().getString(R.string.ltrs)));
            //setBalance(milkKsh);
        });

        payoutsVewModel.getSumOfLoansForPayout(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> {
            loan.setText(String.format("%s %s", String.valueOf(integer), getActivity().getString(R.string.ksh)));
            //setBalance(milkKsh);
        });
        payoutsVewModel.getSumOfOrdersForPayout(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> {
            order.setText(String.format("%s %s", String.valueOf(integer), getActivity().getString(R.string.ksh)));
            // setBalance(milkKsh);
        });

        payoutsVewModel.getSumOfMilkForPayoutKsh(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> {
            milkKsh = integer;
            setBalance(milkKsh);
        });

        btnApprove.setOnClickListener(view -> approve(payouts, model));
        btnBack.setOnClickListener(view1 -> cancelApprove(payouts, model));


        if (model.getStatus() == 0 && (DateTimeUtils.Companion.getToday().equals(payouts.getEndDate())
                || DateTimeUtils.Companion.isPastLastDay(payouts.getEndDate()))) {
            btnApprove.setVisibility(View.VISIBLE);
            txtApprovalStatus.setVisibility(View.GONE);
            btnBack.setVisibility(View.GONE);


        } else if (model.getStatus() == 1) {
            txtApprovalStatus.setText("Approved");
            txtApprovalStatus.setVisibility(View.VISIBLE);
            txtApprovalStatus.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));

            if (payouts.getStatus() == 0) {
                btnApprove.setVisibility(View.GONE);
                btnBack.setVisibility(View.VISIBLE);
                btnBack.setText("Cancel Approval");

                txtApprovalStatus.setVisibility(View.VISIBLE);
            } else {
                btnBack.setVisibility(View.GONE);
                btnApprove.setVisibility(View.GONE);
                txtApprovalStatus.setText("Approved");
                txtApprovalStatus.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));

                txtApprovalStatus.setVisibility(View.VISIBLE);
            }
        } else if (model.getStatus() == 0 && (!DateTimeUtils.Companion.getToday().equals(payouts.getEndDate())
                || !DateTimeUtils.Companion.isPastLastDay(payouts.getEndDate()))) {
            txtApprovalStatus.setText("Pending");
            txtApprovalStatus.setTextColor(getContext().getResources().getColor(R.color.red));

            txtApprovalStatus.setVisibility(View.VISIBLE);
            btnBack.setVisibility(View.GONE);
            btnApprove.setVisibility(View.GONE);


        }


    }

    private void cancelApprove(Payouts payouts, PayoutFarmersCollectionModel model) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        alertDialog.setMessage("Confirm that you wish to cancel " + model.getFarmername() + "'s " + payouts.getCyclename() + " Collection card").setCancelable(false).setTitle("Cancel " + model.getFarmername() + " Card");


        alertDialog.setPositiveButton("Yes", (dialogInterface, i) -> {

            payoutsVewModel.cancelFarmersPayoutCard(model.getFarmercode(), model.getPayoutNumber());
            model.setStatus(0);
            model.setStatusName("Canceled");
            setData(model);


            dialogInterface.dismiss();

        }).setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());

        AlertDialog alertDialogAndroid = alertDialog.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.show();
    }

    private void approve(Payouts payouts, PayoutFarmersCollectionModel model) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        alertDialog.setMessage("Confirm that you wish to approve " + model.getFarmername() + "'s " + payouts.getCyclename() + " Collection card").setCancelable(false).setTitle("Approve " + model.getFarmername() + " Card");


        alertDialog.setPositiveButton("Yes", (dialogInterface, i) -> {

            payoutsVewModel.approveFarmersPayoutCard(model.getFarmercode(), model.getPayoutNumber());
            model.setStatus(1);
            model.setStatusName("Approved");
            setData(model);


            dialogInterface.dismiss();

        }).setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());

        AlertDialog alertDialogAndroid = alertDialog.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.show();

    }

    public void editValue(int adapterPosition, int time, int type, View editable, DayCollectionModel dayCollectionModel) {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_edit_collection, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        alertDialogBuilderUserInput.setView(mView);

        avi = mView.findViewById(R.id.avi);
        LinearLayout milkUnits = mView.findViewById(R.id.milk_units);
        TextInputEditText edtVL = mView.findViewById(R.id.edt_value);
        TextView txt = mView.findViewById(R.id.txt_desc);


        String ti = "";
        if (time == 0) {
            ti = " AM";
        } else {
            ti = " PM";
        }
        String tp = "";
        if (type == 1) {
            milkUnits.setVisibility(View.VISIBLE);
            tp = " Milk collection";
        } else if (type == 2) {
            tp = " Loan";
        } else {
            tp = " Order ";
        }
        txt.setText(" Editing " + tp + "  For  " + dayCollectionModel.getDate() + "  " + ti);



        try {
            if (!((TextView) editable).getText().toString().equals("0.0")) {
                edtVL.setText(((TextView) editable).getText().toString());
                edtVL.setSelection(edtVL.getText().length());
            }
        } catch (Exception nm) {
            nm.printStackTrace();
        }


        alertDialogBuilderUserInput
                .setCancelable(false);

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        alertDialogAndroid.show();



        MaterialButton btnPositive, btnNegative, btnNeutral;
        TextView txtTitle;
        LinearLayout lTitle;
        ImageView imgIcon;
        btnPositive = mView.findViewById(R.id.btn_positive);
        btnNegative = mView.findViewById(R.id.btn_negative);
        btnNeutral = mView.findViewById(R.id.btn_neutral);
        txtTitle = mView.findViewById(R.id.txt_title);
        lTitle = mView.findViewById(R.id.linear_title);
        imgIcon = mView.findViewById(R.id.img_icon);


        btnNeutral.setVisibility(View.GONE);
        lTitle.setVisibility(View.GONE);
        txtTitle.setVisibility(View.VISIBLE);
        imgIcon.setVisibility(View.VISIBLE);
        imgIcon.setImageResource(R.drawable.ic_add_black_24dp);
        txtTitle.setText("Route");

        btnPositive.setOnClickListener(view -> {
            if (TextUtils.isEmpty(edtVL.getText().toString())) {
                updateCollection("0.0", adapterPosition, time, type, dayCollectionModel, alertDialogAndroid);
            }

            updateCollection(edtVL.getText().toString(), adapterPosition, time, type, dayCollectionModel, alertDialogAndroid);

        });
        btnNeutral.setOnClickListener(view -> {

        });
        btnNegative.setOnClickListener(view -> alertDialogAndroid.dismiss());

    }

    private void updateCollection(
            String s, int adapterPosition,
            int time, int type,
            DayCollectionModel dayCollectionModel,
            AlertDialog a) {


        Collection collection;
        Collection ctoUpdate;

        if (time == 1) {


            if (dayCollectionModel.getCollectionIdAm() != 0) {
                collection = payoutsVewModel.getCollectionByIdOne(dayCollectionModel.getCollectionIdAm());
                ctoUpdate = CommonFuncs.updateCollection(s, time, type, dayCollectionModel, collection, payouts, famerModel);


            } else {
                Collection c = new Collection();
                c = CommonFuncs.updateCollection(s, time, type, dayCollectionModel, null, payouts, famerModel);
                //                c.setCycleCode(payouts.getCycleCode());
//                c.setFarmerCode(famerModel.getCode());
//                c.setFarmerName(famerModel.getNames());
//                c.setCycleId(payouts.getCycleCode());
//                c.setDayName(dayCollectionModel.getDay());
//                c.setDayDate(dayCollectionModel.getDate());
//                c.setTimeOfDay("AM");
//
//                c.setLoanAmountGivenOutPrice(dayCollectionModel.getLoanAm());
//                c.setMilkCollected(dayCollectionModel.getMilkAm());
//                c.setOrderGivenOutPrice(dayCollectionModel.getOrderAm());
//
//
//
//                c.setLoanId("");
//                c.setOrderId("");
//                c.setSynced(0);
//                c.setSynced(false);
//                c.setApproved(0);
//
//                c.setPayoutnumber(dayCollectionModel.getPayoutNumber());
//                c.setCycleStartedOn(payouts.getStartDate());
//
//                if (type == 1) {
//                    MilkModel milkModel=dayCollectionModel.getMilkModelAm();
//                    milkModel.setUnitQty(s);
//
//
//                    c.setMilkCollected(s);
//                    c.setMilkCollectedValueKsh(milkModel.getValueKsh());
//                    c.setMilkCollectedValueLtrs(milkModel.getValueLtrs());
//                    c.setMilkDetails(new Gson().toJson(milkModel));
//
//
//                } else if (type == 2) {
//                    LoanModel loanModel=dayCollectionModel.getLoanModelAm();
//                    loanModel.setLoanAmount(s);
//
//
//
//                    c.setLoanAmountGivenOutPrice(s);
//                    c.setLoanDetails(new Gson().toJson(loanModel));
//
//
//
//
//                } else if (type == 3) {
//                    OrderModel orderModel=dayCollectionModel.getOrderModelAm();
//                    orderModel.setOrderAmount(s);
//
//                    c.setOrderGivenOutPrice(s);
//                    c.setOrderDetails(new Gson().toJson(orderModel));
//
//                }
                payoutsVewModel.createCollections(c).observe(this, responseModel -> {
                    if (responseModel != null) {
                        a.dismiss();
                        MyToast.toast(responseModel.getResultDescription(), getContext(), R.drawable.ic_launcher, Toast.LENGTH_LONG);
                    }
                });
                a.dismiss();
                return;


            }

        } else {
            if (dayCollectionModel.getCollectionIdPm() != 0) {
                collection = payoutsVewModel.getCollectionByIdOne(dayCollectionModel.getCollectionIdPm());
                ctoUpdate = CommonFuncs.updateCollection(s, time, type, dayCollectionModel, collection, payouts, famerModel);

            } else {


                Collection c = new Collection();
                c = CommonFuncs.updateCollection(s, time, type, dayCollectionModel, null, payouts, famerModel);
                //
//                c.setCycleCode(famerModel.getCyclecode());
//                c.setFarmerCode(famerModel.getCode());
//                c.setFarmerName(famerModel.getNames());
//                c.setCycleId(famerModel.getCode());
//                c.setDayName(dayCollectionModel.getDay());
//                c.setLoanAmountGivenOutPrice(dayCollectionModel.getLoanPm());
//                c.setDayDate(dayCollectionModel.getDate());
//                c.setTimeOfDay("PM");
//                c.setMilkCollected(dayCollectionModel.getMilkPm());
//                c.setOrderGivenOutPrice(dayCollectionModel.getOrderPm());
//
//                c.setLoanId("");
//                c.setOrderId("");
//                c.setSynced(0);
//                c.setSynced(false);
//                c.setApproved(0);
//
//                c.setPayoutnumber(dayCollectionModel.getPayoutNumber());
//                c.setCycleStartedOn(payouts.getStartDate());
//
//
//                if (type == 1) {
//                    MilkModel milkModel=dayCollectionModel.getMilkModelPm();
//                    milkModel.setUnitQty(s);
//
//
//                    c.setMilkCollected(s);
//                    c.setMilkCollectedValueKsh(milkModel.getValueKsh());
//                    c.setMilkCollectedValueLtrs(milkModel.getValueLtrs());
//                    c.setMilkDetails(new Gson().toJson(milkModel));
//
//
//                } else if (type == 2) {
//                    LoanModel loanModel=dayCollectionModel.getLoanModelPm();
//                    loanModel.setLoanAmount(s);
//
//
//
//                    c.setLoanAmountGivenOutPrice(s);
//                    c.setLoanDetails(new Gson().toJson(loanModel));
//
//
//
//
//                } else if (type == 3) {
//                    OrderModel orderModel=dayCollectionModel.getOrderModelPm();
//                    orderModel.setOrderAmount(s);
//
//                    c.setOrderGivenOutPrice(s);
//                    c.setOrderDetails(new Gson().toJson(orderModel));
//
//                }
//
//
//
//
//
//
//
                payoutsVewModel.createCollections(c).observe(this, responseModel -> {
                    if (responseModel != null) {
                        a.dismiss();
                        MyToast.toast(responseModel.getResultDescription(), getContext(), R.drawable.ic_launcher, Toast.LENGTH_LONG);
                    }
                });
                a.dismiss();
                return;
            }

        }
        if (collection != null) {
            //            if (type == 1) {
//                MilkModel milkModel;
//                milkModel = time == 1 ? dayCollectionModel.getMilkModelAm() : dayCollectionModel.getMilkModelPm();
//                milkModel.setUnitQty(s);
//                collection.setMilkCollected(s);
//                collection.setMilkCollectedValueKsh(milkModel.getValueKsh());
//                collection.setMilkCollectedValueLtrs(milkModel.getValueLtrs());
//                collection.setMilkDetails(new Gson().toJson(milkModel));
//                payoutsVewModel.updateCollection(collection);
//                a.dismiss();
//            } else if (type == 2) {
//                LoanModel loanModel;
//                loanModel = time == 1 ? dayCollectionModel.getLoanModelAm() : dayCollectionModel.getLoanModelPm();
//                loanModel.setLoanAmount(s);
//                collection.setLoanAmountGivenOutPrice(s);
//                collection.setLoanDetails(new Gson().toJson(loanModel));
//                payoutsVewModel.updateCollection(collection);
//                a.dismiss();
//            } else if (type == 3) {
//                OrderModel orderModel;
//                orderModel = time == 1 ? dayCollectionModel.getOrderModelAm() : dayCollectionModel.getOrderModelPm();
//                orderModel.setOrderAmount(s);
//
//
//
//                collection.setOrderGivenOutPrice(s);
//                collection.setLoanDetails(new Gson().toJson(orderModel));
//
            payoutsVewModel.updateCollection(ctoUpdate);
                a.dismiss();
            //}
        } else {
            Timber.d("Our coll is null" + dayCollectionModel.getCollectionIdAm() + "  " + dayCollectionModel.getCollectionIdPm());
        }
    }

    public void initList() {
        recyclerView = view.findViewById(R.id.recyclerView);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        if (dayCollectionModels == null) {
            dayCollectionModels = new LinkedList<>();
        }
        listAdapter = new FarmerCollectionsAdapter(getContext(), dayCollectionModels, new AdvancedOnclickRecyclerListener() {
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

            @Override
            public void onEditTextChanged(int adapterPosition, int time, int type, View editable) {
                editValue(adapterPosition, time, type, editable, dayCollectionModels.get(adapterPosition));


            }

        });
        recyclerView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();


    }

}
