package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishabora.Adapters.FarmerCollectionsAdapter;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.Cycles;
import com.dev.lishabora.Models.DayCollectionModel;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.LoanModel;
import com.dev.lishabora.Models.OrderModel;
import com.dev.lishabora.Models.PayoutFarmersCollectionModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Models.UnitsModel;
import com.dev.lishabora.Utils.AdvancedOnclickRecyclerListener;
import com.dev.lishabora.Utils.CollectionCreateUpdateListener;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishabora.Views.Trader.Activities.EditOrder;
import com.dev.lishabora.Views.Trader.OrderConstants;
import com.dev.lishaboramobile.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.dev.lishabora.Views.CommonFuncs.getBalance;

public class FragmentCurrentFarmerPayout extends Fragment {

    public TextView status, id, name, balance, milk, loan, order;
    public RelativeLayout background;
    public View statusview;
    Double milkKsh = 0.0;
    Double loanKsh = 0.0;
    Double orderKsh = 0.0;


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
    UnitsModel u = new UnitsModel();


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

    DayCollectionModel dayCollectionModel;

    private void setUpList(List<DayCollectionModel> dayCollectionModels) {
        this.dayCollectionModels = dayCollectionModels;
        this.liveModel = dayCollectionModels;
        listAdapter.refresh(dayCollectionModels, payouts.getStatus() == 0);


    }

    private int time, type, adp;

    private void setUpDayCollectionsModel(Payouts payouts, List<Collection> collections) {

        setUpList(CommonFuncs.setUpDayCollectionsModel(payouts, collections));

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
                setData(getFarmersCollectionModel());


            });
        }



    }

    public PayoutFarmersCollectionModel getFarmersCollectionModel() {

        return CommonFuncs.getFarmersCollectionModel(famerModel, collections, payouts);

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

    private void setBalance(Double milkKsh) {
        balance.setText(String.format("%s %s", getBalance(String.valueOf(milkKsh), String.valueOf(loanKsh), String.valueOf(orderKsh)), getActivity().getString(R.string.ksh)));
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
            loanKsh = integer;
        });
        payoutsVewModel.getSumOfOrdersForPayout(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> {
            order.setText(String.format("%s %s", String.valueOf(integer), getActivity().getString(R.string.ksh)));
            // setBalance(milkKsh);
            orderKsh = integer;
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

    public void editValue(int adapterPosition, int time, int type, String value, Object o, View editable, DayCollectionModel dayCollectionModel) {

        if (type == 1) {
            CommonFuncs.editValueMilk(adapterPosition, time, type, value, o, editable, dayCollectionModel, getContext(), avi, famerModel, (s, adapterPosition1, time1, type1, dayCollectionModel1, a) -> updateCollectionValue(s, adapterPosition1, time1, type1, dayCollectionModel1, a, null, null));

        } else if (type == 2) {
            CommonFuncs.editValueLoan(time, type, value, o, dayCollectionModel, getContext(), famerModel, (value1, loanModel, time12, dayCollectionModel12, alertDialogAndroid) -> {
                updateCollectionValue(value1, adapterPosition, time12, type, dayCollectionModel12, alertDialogAndroid, loanModel, null);
            });

        } else {


            OrderConstants.setFamerModel(famerModel);
            Intent intent2 = new Intent(getActivity(), EditOrder.class);
            intent2.putExtra("farmer", famerModel);
            // intent2.putExtra("collection", dayCollectionModel);
            startActivityForResult(intent2, 10004);


            //CommonFuncs.editValueOrder(time, type, value, o, dayCollectionModel, getContext(), famerModel, (String value1, OrderModel orderModel, int time12, DayCollectionModel dayCollectionModel12, AlertDialog alertDialogAndroid) -> updateCollectionValue(value1, adapterPosition, time12, type, dayCollectionModel12, alertDialogAndroid, null, orderModel));

        }
    }

    private void updateCollectionValue(String s, int adapterPosition, int time, int type, DayCollectionModel dayCollectionModel, AlertDialog a, @Nullable LoanModel loanModel, @Nullable OrderModel orderModel) {
        CommonFuncs.updateCollectionValue(s, time, type, dayCollectionModel, payoutsVewModel, payouts, famerModel, loanModel, orderModel, new CollectionCreateUpdateListener() {
            @Override
            public void createCollection(Collection c) {
                payoutsVewModel.createCollections(c).observe(FragmentCurrentFarmerPayout.this, responseModel -> {
                    if (responseModel != null) {
                        a.dismiss();
                        MyToast.toast(responseModel.getResultDescription(), getContext(), R.drawable.ic_launcher, Toast.LENGTH_LONG);
                    }
                });
                a.dismiss();
            }

            @Override
            public void updateCollection(Collection c) {
                payoutsVewModel.updateCollection(c);
                a.dismiss();
            }

            @Override
            public void error(String error) {
                MyToast.toast(error, getContext(), R.drawable.ic_launcher, Toast.LENGTH_LONG);
                a.dismiss();
            }
        });
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

                CommonFuncs.ValueObject v = CommonFuncs.getValueObjectToEditFromDayCollection(dayCollectionModels.get(adapterPosition), time, type);
                editValue(adapterPosition, time, type, v.getValue(), v.getO(), editable, dayCollectionModels.get(adapterPosition));



            }

        });
        recyclerView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();


    }


    @Override
    public void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 1004:
                if (resultCode == RESULT_OK && data != null) {


                }
                break;

            default:
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
