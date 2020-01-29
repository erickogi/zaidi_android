package com.dev.zaidi.Views.Trader.Fragments;

import android.arch.lifecycle.ViewModelProviders;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dev.zaidi.Adapters.PayoutFarmersPayAdapter;
import com.dev.zaidi.Models.Collection;
import com.dev.zaidi.Models.DayCollectionModel;
import com.dev.zaidi.Models.FamerModel;
import com.dev.zaidi.Models.LoanModel;
import com.dev.zaidi.Models.OrderModel;
import com.dev.zaidi.Models.PayoutFarmersCollectionModel;
import com.dev.zaidi.Models.Payouts;
import com.dev.zaidi.R;
import com.dev.zaidi.Utils.ApproveListener;
import com.dev.zaidi.Utils.CollectionCreateUpdateListener;
import com.dev.zaidi.Utils.DateTimeUtils;
import com.dev.zaidi.Utils.GeneralUtills;
import com.dev.zaidi.Utils.MyToast;
import com.dev.zaidi.Utils.OnclickRecyclerListener;
import com.dev.zaidi.ViewModels.Trader.BalncesViewModel;
import com.dev.zaidi.ViewModels.Trader.PayoutsVewModel;
import com.dev.zaidi.ViewModels.Trader.TraderViewModel;
import com.dev.zaidi.Views.CommonFuncs;
import com.dev.zaidi.Views.Trader.PayoutConstants;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.dev.zaidi.Views.CommonFuncs.setPayoutActionStatus;

public class FragmentPayoutSummary extends Fragment {
    public TextView status, startDate, cycleName, endDate, milkTotalKsh, milkTotalLtrs, loanTotal, orderTotal, balance, approvedCount, unApprovedCount;
    public RelativeLayout background;
    public View statusview;
    public View view;
    private Payouts payouts;
    TextView txtApprovalStatus;
    private PayoutsVewModel payoutsVewModel;
    private BalncesViewModel balncesViewModel;
    List<PayoutFarmersCollectionModel> payoutFarmersCollectionModels;
    private MaterialButton btnApprove;
    PayoutFarmersPayAdapter listAdapterAll;
    private TraderViewModel traderViewModel;
    private MaterialDialog materialDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.payout_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);
        balncesViewModel = ViewModelProviders.of(this).get(BalncesViewModel.class);
        traderViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);

        payouts = new Payouts();
        if (getArguments() != null) {
            payouts = (Payouts) getArguments().getSerializable("data");
        } else {
            payouts = PayoutConstants.getPayouts();
        }
        initCardHeader();

        btnApprove = view.findViewById(R.id.btn_approve);
        txtApprovalStatus = view.findViewById(R.id.txt_approval_status);
        btnApprove.setVisibility(View.GONE);


        if (CommonFuncs.canApprovePayout(payouts)) {
            btnApprove.setVisibility(View.VISIBLE);
        } else {
            btnApprove.setVisibility(View.GONE);
        }


        btnApprove.setOnClickListener(view1 -> {

            if (CommonFuncs.allCollectionsAreApproved(payoutsVewModel, payouts)) {
                payouts.setStatus(1);
                payouts.setStatusName("Approved");
                payoutsVewModel.updatePayout(payouts);
                setCardHeaderData(payouts);
            } else {
                showFarmers();

            }

        });
        setPayoutActionStatus(payouts, getContext(), btnApprove, txtApprovalStatus);

    }

    private void showFarmers() {
        if (payoutFarmersCollectionModels == null) {

            payoutFarmersCollectionModels = new LinkedList<>();
        }
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.approve_multiple, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput.setIcon(R.drawable.ic_done_black_24dp);
        alertDialogBuilderUserInput.setTitle("Approve Multiple");


        RecyclerView recyclerView = mView.findViewById(R.id.recyclerView);

        StaggeredGridLayoutManager mStaggeredLayoutManager;
        LinkedList<PayoutFarmersCollectionModel> selected = new LinkedList<>();
        listAdapterAll = new PayoutFarmersPayAdapter(getActivity(), payoutFarmersCollectionModels, new OnclickRecyclerListener() {
            @Override
            public void onMenuItem(int position, int menuItem) {

            }

            @Override
            public void onSwipe(int adapterPosition, int direction) {

            }

            @Override
            public void onClickListener(int position) {

                if (payoutFarmersCollectionModels.get(position).isChecked()) {
                    payoutFarmersCollectionModels.get(position).setChecked(false);
                    selected.remove(payoutFarmersCollectionModels.get(position));
                    listAdapterAll.notifyItemChanged(position, payoutFarmersCollectionModels.get(position));

                } else {
                    selected.add(payoutFarmersCollectionModels.get(position));
                    payoutFarmersCollectionModels.get(position).setChecked(true);
                    listAdapterAll.notifyItemChanged(position, payoutFarmersCollectionModels.get(position));

                }
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
        }, true);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        listAdapterAll.notifyDataSetChanged();
        recyclerView.setAdapter(listAdapterAll);


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
        btnNeutral.setText("Delete");

        btnNeutral.setBackgroundColor(this.getResources().getColor(R.color.red));
        lTitle.setVisibility(View.GONE);
        txtTitle.setVisibility(View.VISIBLE);
        imgIcon.setVisibility(View.VISIBLE);
        imgIcon.setImageResource(R.drawable.ic_add_black_24dp);
        txtTitle.setText("Route");

//        btnNeutral.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                OrderConstants.getProductOrderModels().remove(pos);
//
//                alertDialogAndroid.dismiss();
//
//                refreshList();
//            }
//        });

        btnPositive.setOnClickListener(new CustomListener(alertDialogAndroid, selected));

        btnNegative.setOnClickListener(view -> alertDialogAndroid.dismiss());


    }

    private void updateCollectionValue(FamerModel famerModel, String s, int time, int type, DayCollectionModel dayCollectionModel,
                                       AlertDialog a, @Nullable LoanModel loanModel, @Nullable OrderModel orderModel) {
        CommonFuncs.updateCollectionValue(s, time, type, dayCollectionModel, payoutsVewModel, payouts, famerModel, loanModel, orderModel, new CollectionCreateUpdateListener() {
            @Override
            public void createCollection(Collection c) {
                payoutsVewModel.createCollectionsS(c);
                CommonFuncs.addBalance(famerModel, traderViewModel, balncesViewModel, c, payouts.getCode(), type, null, null);

            }

            @Override
            public void updateCollection(Collection c) {
                payoutsVewModel.updateCollectionS(c);

            }

            @Override
            public void error(String error) {
                MyToast.toast(error, getContext());

            }
        });
    }

    public void doi(List<PayoutFarmersCollectionModel> models, int last) {
        if (last + 1 < models.size()) {
            done(models, last + 1);
        } else {
            if (materialDialog != null) {
                materialDialog.dismiss();
            }
        }

    }

    private void done(List<PayoutFarmersCollectionModel> models, int a) {

        int v = models.size();
        ApproveFuncs.approveCard(getActivity(), payouts.getCode(), models.get(a), traderViewModel, balncesViewModel, payoutsVewModel, new ApproveListener() {
            @Override
            public void onComplete() {

                MyToast.errorToast(models.get(a).getFarmername() + " Approval Complete", getContext());
                doi(models, a);

                }

            @Override
            public void onStart() {
                if (materialDialog != null) {
                    materialDialog.setContent(models.get(a).getFarmername());
                }


            }

            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onError(String error) {

                MyToast.errorToast(error, getContext());
            }
        });
    }

    private void setUpFarmerCollectionList(List<FamerModel> famerModels, List<Collection> collections) {


        Double totalBalance = 0.0;
        Double totalMilk = 0.0;
        Double totalMilkKsh = 0.0;
        Double totalMilkLtrs = 0.0;
        Double totalOrders = 0.0;
        Double totalLoans = 0.0;


        payoutFarmersCollectionModels = new LinkedList<>();
        for (FamerModel famerModel : famerModels) {
            if (famerModel.getArchived() == 0 && famerModel.getDeleted() == 0 && famerModel.getDummy() == 0) {


                PayoutFarmersCollectionModel p = CommonFuncs.getFarmersCollectionModel(famerModel, collections, payouts, balncesViewModel);

                p.setChecked(false);
                payoutFarmersCollectionModels.add(p);

                if (p != null) {

                    try {
                        totalBalance = totalBalance + Double.valueOf(p.getBalance());

                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }
                    try {
                        totalMilk = totalMilk + Double.valueOf(p.getMilktotal());
                        totalMilkKsh = totalMilkKsh + Double.valueOf(p.getMilktotalKsh());
                        totalMilkLtrs = totalMilkLtrs + Double.valueOf(p.getMilktotalLtrs());

                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }
                    try {
                        totalLoans = totalLoans + Double.valueOf(p.getLoanTotal());

                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }
                    try {
                        totalOrders = totalOrders + Double.valueOf(p.getOrderTotal());

                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }

                }
            }
        }

        setData(totalBalance, totalMilk, totalMilkKsh, totalMilkLtrs, totalLoans, totalOrders);


    }


    private void loadFarmers() {


        try {
            payoutsVewModel.getFarmersByCycle("" + payouts.getCycleCode()).observe(this, famerModels -> {
                if (famerModels != null) {


                    try {
                        loadCollectionPayouts(famerModels);
                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }

                } else {

                }
            });
        } catch (Exception nm) {
            nm.printStackTrace();
        }
    }

    private void loadCollectionPayouts(List<FamerModel> famerModels) {

        payoutsVewModel.getCollectionByDateByPayout("" + payouts.getCode()).observe(this, collections -> {
            if (collections != null) {


                try {
                    setUpFarmerCollectionList(famerModels, collections);
                } catch (Exception nm) {
                    nm.printStackTrace();
                }
            }
        });


    }

    private void setData(Double totalBalance, Double totalMilk, Double totalMilkKsh, Double totalMilkLtrs, Double totalLoans, Double totalOrders) {

        String vB = String.valueOf(totalBalance);
        String vL = String.valueOf(totalLoans);
        String vO = String.valueOf(totalOrders);
        String vM = String.valueOf(totalMilkKsh);
        String vML = String.valueOf(totalMilkLtrs);


        try {
            vB = GeneralUtills.Companion.addCommify(GeneralUtills.Companion.round(vB, 0));
            vL = GeneralUtills.Companion.addCommify(GeneralUtills.Companion.round(vL, 0));
            vO = GeneralUtills.Companion.addCommify(GeneralUtills.Companion.round(vO, 0));
            vM = GeneralUtills.Companion.addCommify(GeneralUtills.Companion.round(vM, 0));
            vML = GeneralUtills.Companion.addCommify(GeneralUtills.Companion.round(vML, 1));
        } catch (Exception nm) {
            nm.printStackTrace();
        }


        milkTotalKsh.setText(String.format("%s %s", vM, this.getString(R.string.ksh)));
        milkTotalLtrs.setText(String.format("%s %s", vML, this.getString(R.string.ltrs)));
        loanTotal.setText(String.format("%s %s", vL, this.getString(R.string.ksh)));
        orderTotal.setText(String.format("%s %s", vO, this.getString(R.string.ksh)));
        balance.setText(String.format("%s %s", vB, this.getString(R.string.ksh)));


        GeneralUtills.Companion.changeCOlor(String.valueOf(totalBalance), balance, 1);


    }

    public void setCardHeaderData(com.dev.zaidi.Models.Payouts model) {
        startDate.setText(DateTimeUtils.Companion.getDisplayDate(model.getStartDate(), DateTimeUtils.Companion.getDisplayDatePattern1()));
        endDate.setText(DateTimeUtils.Companion.getDisplayDate(model.getEndDate(), DateTimeUtils.Companion.getDisplayDatePattern1()));
        cycleName.setText(model.getCyclename());


        try {
            milkTotalLtrs.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getMilkTotalLtrs(), 1), this.getString(R.string.ltrs)));
            milkTotalKsh.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getMilkTotalKsh(), 1), this.getString(R.string.ksh)));
            loanTotal.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getLoanTotal(), 1), this.getString(R.string.ksh)));
            orderTotal.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getOrderTotal(), 1), this.getString(R.string.ksh)));
            balance.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getBalance(), 1), this.getString(R.string.ksh)));


            GeneralUtills.Companion.changeCOlor(model.getBalance(), balance, 1);

        } catch (Exception nm) {
            nm.printStackTrace();
        }

        approvedCount.setText(model.getApprovedCards());
        unApprovedCount.setText(model.getPendingCards());


        if (model.getStatus() == 1) {

            background.setBackgroundColor(this.getResources().getColor(R.color.green_color_picker));


        } else if (model.getStatus() == 0) {
            background.setBackgroundColor(this.getResources().getColor(R.color.red));

        } else {
            background.setBackgroundColor(this.getResources().getColor(R.color.blue_color_picker));

        }
        try {
            loadFarmers();
        } catch (Exception nm) {
            nm.printStackTrace();
        }

    }

    public void initCardHeader() {
        background = view.findViewById(R.id.background);
        startDate = view.findViewById(R.id.txt_date_start);
        endDate = view.findViewById(R.id.txt_date_end);


        cycleName = view.findViewById(R.id.txt_cycle);

        milkTotalKsh = view.findViewById(R.id.txt_milk_totals);
        milkTotalLtrs = view.findViewById(R.id.txt_milk_totals_litres);
        loanTotal = view.findViewById(R.id.txt_loans_total);
        orderTotal = view.findViewById(R.id.txt_orders_total);

        approvedCount = view.findViewById(R.id.txt_approved_farmers);
        unApprovedCount = view.findViewById(R.id.txt_pending_farmers);
        balance = view.findViewById(R.id.txt_Bal_out);

        if (payouts != null) {
            setCardHeaderData(payouts);
        }

    }

    private class CustomListener implements View.OnClickListener {
        AlertDialog dialog;
        boolean isActive = true;
        int isAct = 1;
        private LinkedList<PayoutFarmersCollectionModel> selectedProducts = new LinkedList<>();

        public CustomListener(AlertDialog alertDialogAndroid, LinkedList<PayoutFarmersCollectionModel> selectedProducts) {
            dialog = alertDialogAndroid;
            this.selectedProducts = selectedProducts;

        }

        @Override
        public void onClick(View v) {
            if (selectedProducts != null && selectedProducts.size() > 0) {
                materialDialog = new MaterialDialog.Builder(getActivity())
                        .title("Approving farmers cards")
                        .content("Please wait ..")
                        .cancelable(false)
                        .progress(true, selectedProducts.size()).build();


                materialDialog.show();
                for (int a = 0; a < selectedProducts.size(); a++) {
                    PayoutFarmersCollectionModel p = selectedProducts.get(a);
                    List<Collection> collections = payoutsVewModel.getCollectionByDateByPayoutByFarmerListOne(p.getPayoutCode(), p.getFarmercode());
                    if (collections.size() < 1) {
                        CommonFuncs.silentValueMilk(1, 1, 1, CommonFuncs.setUpDayCollectionsModel(payouts, collections).get(0), (value, adapterPosition, time, type, dayCollectionModel, alertDialogAndroid)
                                -> updateCollectionValue(p.getFamerModel(), value, time, type, dayCollectionModel, null, null, null));
                    }


                }

                doi(selectedProducts, -1);


            } else {

            }

        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem mSearch = menu.findItem(R.id.action_search);
        MenuItem mHelp = menu.findItem(R.id.action_help);


        MenuItem mAutomatically = menu.findItem(R.id.action_automatically);
        MenuItem mManually = menu.findItem(R.id.action_manually);
        MenuItem mChronologically = menu.findItem(R.id.action_chronologically);
        MenuItem mAlphabetically = menu.findItem(R.id.action_alphabetically);
        MenuItem mRearrangeManually = menu.findItem(R.id.action_smanually);

        mAutomatically.setVisible(false);
        mChronologically.setVisible(false);
        mManually.setVisible(false);
        mAlphabetically.setVisible(false);
        mRearrangeManually.setVisible(false);

        mSearch.setVisible(false);
        mHelp.setVisible(false);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return false;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

}
