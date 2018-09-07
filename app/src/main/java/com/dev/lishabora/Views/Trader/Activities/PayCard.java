package com.dev.lishabora.Views.Trader.Activities;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishabora.Adapters.FarmerCollectionsAdapter;
import com.dev.lishabora.Adapters.PayoutFarmersAdapter;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.DayCollectionModel;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.LoanModel;
import com.dev.lishabora.Models.OrderModel;
import com.dev.lishabora.Models.PayoutFarmersCollectionModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Utils.AdvancedOnclickRecyclerListener;
import com.dev.lishabora.Utils.CollectionCreateUpdateListener;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishabora.Views.Trader.OrderConstants;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;

import static com.dev.lishabora.Views.CommonFuncs.getBalance;

public class PayCard extends AppCompatActivity {
    public TextView status, id, name, balance, milk, loan, order;
    public RelativeLayout background;
    public View statusview;
    public View itemVew;
    Collection c = new Collection();
    PayoutFarmersCollectionModel payoutfarmermodel;
    LinearLayout layoutBottomSheet;

    private Button save;
    private FarmerCollectionsAdapter listAdapter;
    private Context context;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private List<DayCollectionModel> dayCollectionModels;
    private MaterialButton btnApprove, btnBack;
    private TextView txtApprovalStatus;

    private LinearLayout empty_layout;
    private Payouts payouts;
    private PayoutsVewModel payoutsVewModel;

    private List<Collection> collections;
    private boolean firstDone = false;
    private List<DayCollectionModel> liveModel;
    BottomSheetBehavior sheetBehavior;
    private AVLoadingIndicatorView avi;
    private SearchView searchView;
    Double milkKsh = 0.0;
    private FamerModel famerModel;




    private void loadCollections(String payoutNumber, String farmerCode) {
        payoutsVewModel.getCollectionByDateByPayoutByFarmer(payoutNumber, farmerCode).observe(this, collections -> {
            if (collections != null) {
                PayCard.this.collections = collections;
                getPayout(payoutNumber, collections);
            }
        });
    }

    private void getPayout(String payout, List<Collection> collections) {
        payoutsVewModel.getPayoutsByPayoutNumber(payout).observe(this, payouts -> {
            if (payouts != null) {
                setUpDayCollectionsModel(payouts, collections);
            }
        });

    }


    private void setUpList(List<DayCollectionModel> dayCollectionModels) {
        this.dayCollectionModels = dayCollectionModels;
        this.liveModel = dayCollectionModels;

        listAdapter.refresh(dayCollectionModels, payoutfarmermodel.getStatus() == 0);



    }

    private void setUpDayCollectionsModel(Payouts payouts, List<Collection> collections) {
        setUpList(CommonFuncs.setUpDayCollectionsModel(payouts, collections));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_card);
        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);
        famerModel = (FamerModel) getIntent().getSerializableExtra("farmer");


        initList();
        btnApprove = findViewById(R.id.btn_approve);
        btnApprove.setVisibility(View.GONE);
        txtApprovalStatus = findViewById(R.id.txt_approval_status);


        btnBack = findViewById(R.id.btn_back);
        btnBack.setVisibility(View.GONE);

        statusview = findViewById(R.id.status_view);
        background = findViewById(R.id.background);
        save = findViewById(R.id.save_btn);
        save.setVisibility(View.GONE);
        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        // save.setOnClickListener(view -> update(liveModel));


        status = findViewById(R.id.txt_status);
        id = findViewById(R.id.txt_id);
        name = findViewById(R.id.txt_name);
        balance = findViewById(R.id.txt_balance);
        searchView = findViewById(R.id.search);


        milk = findViewById(R.id.txt_milk);
        loan = findViewById(R.id.txt_loans);
        order = findViewById(R.id.txt_orders);
        PayoutFarmersCollectionModel model = (PayoutFarmersCollectionModel) getIntent().getSerializableExtra("data");
        payouts = (Payouts) getIntent().getSerializableExtra("payout");
        setData(model);

        String data = getIntent().getStringExtra("farmers");

        if (data != null && !data.equals("null")) {

            Gson gson = new Gson();
            Type listType = new TypeToken<LinkedList<PayoutFarmersCollectionModel>>() {
            }.getType();
            setBottom();
            initBottomList(gson.fromJson(data, listType));

        } else {
            layoutBottomSheet.setVisibility(View.GONE);
        }



    }

    private void approve(Payouts payouts, PayoutFarmersCollectionModel model) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PayCard.this);
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

    public void initBottomList(List<PayoutFarmersCollectionModel> models) {

        RecyclerView recyclerView = findViewById(R.id.recyclerViewbottom);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        PayoutFarmersAdapter listAdapter = new PayoutFarmersAdapter(this, models, new OnclickRecyclerListener() {
            @Override
            public void onSwipe(int adapterPosition, int direction) {


            }

            @Override
            public void onClickListener(int position) {
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                payoutsVewModel.getFarmerByCode(models.get(position).getFarmercode()).observe(PayCard.this, famerModel -> {
                    if (famerModel != null) {
                        PayCard.this.famerModel = famerModel;
                        setData(models.get(position));
                    } else {
                        Timber.tag("farmerCilcked").d("clicked " + position + "  eRROR Farmern Not found");

                    }

                });




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

        searchView.setVisibility(View.GONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {


                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return true;
            }
        });


    }

    private void setBottom() {
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }

    private void setBalance(Double milkKsh) {
        balance.setText(getBalance(String.valueOf(milkKsh), loan.getText().toString(), order.getText().toString()));
    }

    private void setData(PayoutFarmersCollectionModel model) {
        this.payoutfarmermodel = model;
        //  balance.setText("" + model.getBalance());

        id.setText(model.getFarmercode());
        name.setText(model.getFarmername());
        status.setText(model.getStatusName());

        setTitle("" + model.getFarmername() + "        ID " + model.getFarmercode());


        milk.setText(String.format("%s %s", model.getMilktotal(), this.getString(R.string.ltrs)));
        if (!model.getMilktotal().equals("0.0")) {
            milk.setTextColor(this.getResources().getColor(R.color.colorPrimary));
            milk.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            milk.setTypeface(Typeface.DEFAULT);

            milk.setTextColor(this.getResources().getColor(R.color.black));

        }


        loan.setText(String.format("%s %s", model.getLoanTotal(), this.getString(R.string.ksh)));
        if (!model.getLoanTotal().equals("0.0")) {
            loan.setTextColor(this.getResources().getColor(R.color.colorPrimary));
            loan.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            loan.setTypeface(Typeface.DEFAULT);

            loan.setTextColor(this.getResources().getColor(R.color.black));

        }

        order.setText(String.format("%s %s", model.getOrderTotal(), this.getString(R.string.ksh)));
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

        loadCollections("" + model.getPayoutNumber(), model.getFarmercode());


        payoutsVewModel.getSumOfMilkForPayoutLtrs(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> {
            milk.setText(String.valueOf(integer));
            setBalance(milkKsh);
        });
        payoutsVewModel.getSumOfMilkForPayoutKsh(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> {
            milkKsh = integer;
            setBalance(milkKsh);
        });
        payoutsVewModel.getSumOfLoansForPayout(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> {
            loan.setText(String.valueOf(integer));
            setBalance(milkKsh);
        });
        payoutsVewModel.getSumOfOrdersForPayout(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> {
            order.setText(String.valueOf(integer));
            setBalance(milkKsh);
        });


        btnApprove.setOnClickListener(view -> approve(payouts, model));


        if (model.getStatus() == 0 && (DateTimeUtils.Companion.getToday().equals(payouts.getEndDate())
                || DateTimeUtils.Companion.isPastLastDay(payouts.getEndDate()))) {
            btnApprove.setVisibility(View.VISIBLE);
            txtApprovalStatus.setVisibility(View.GONE);
            btnBack.setVisibility(View.GONE);


        } else if (model.getStatus() == 1) {
            txtApprovalStatus.setText("Approved");
            txtApprovalStatus.setVisibility(View.VISIBLE);
            txtApprovalStatus.setTextColor(this.getResources().getColor(R.color.colorPrimary));


            if (payouts.getStatus() == 0) {
                btnApprove.setVisibility(View.GONE);
                btnBack.setVisibility(View.VISIBLE);
                btnBack.setText("Cancel Approval");
                txtApprovalStatus.setVisibility(View.VISIBLE);

            } else {
                btnBack.setVisibility(View.GONE);
                btnApprove.setVisibility(View.GONE);
                txtApprovalStatus.setText("Approved");
                txtApprovalStatus.setTextColor(this.getResources().getColor(R.color.colorPrimary));

                txtApprovalStatus.setVisibility(View.VISIBLE);
            }
        } else if (model.getStatus() == 0 && (!DateTimeUtils.Companion.getToday().equals(payouts.getEndDate())
                || !DateTimeUtils.Companion.isPastLastDay(payouts.getEndDate()))) {
            txtApprovalStatus.setText("Pending");
            txtApprovalStatus.setTextColor(this.getResources().getColor(R.color.red));

            txtApprovalStatus.setVisibility(View.VISIBLE);
            btnBack.setVisibility(View.GONE);
            btnApprove.setVisibility(View.GONE);


        }


    }

    public void editValue(int adapterPosition, int time, int type, String value, Object o, View editable, DayCollectionModel dayCollectionModel) {


        if (type == 1) {
            CommonFuncs.editValueMilk(adapterPosition, time, type, value, o, editable, dayCollectionModel, PayCard.this, avi, famerModel, (s, adapterPosition1, time1, type1, dayCollectionModel1, a) -> updateCollectionValue(s, adapterPosition1, time1, type1, dayCollectionModel1, a, null, null));

        } else if (type == 2) {
            CommonFuncs.editValueLoan(time, type, value, o, dayCollectionModel, PayCard.this, famerModel, (value1, loanModel, time12, dayCollectionModel12, alertDialogAndroid) -> updateCollectionValue(value1, adapterPosition, time12, type, dayCollectionModel12, alertDialogAndroid, loanModel, null));

        } else {
            //CommonFuncs.editValueOrder(time, type, value, o, dayCollectionModel, PayCard.this, famerModel, (String value1, OrderModel orderModel, int time12, DayCollectionModel dayCollectionModel12, AlertDialog alertDialogAndroid) -> updateCollectionValue(value1, adapterPosition, time12, type, dayCollectionModel12, alertDialogAndroid, null, orderModel));
            OrderConstants.setFamerModel(famerModel);
            Intent intent2 = new Intent(PayCard.this, EditOrder.class);
            intent2.putExtra("farmer", famerModel);
            // intent2.putExtra("collection", dayCollectionModel);
            startActivityForResult(intent2, 10004);


        }
    }

    private void updateCollectionValue(String s, int adapterPosition, int time, int type, DayCollectionModel dayCollectionModel, AlertDialog a, @Nullable LoanModel loanModel, @Nullable OrderModel orderModel) {
        CommonFuncs.updateCollectionValue(s, time, type, dayCollectionModel, payoutsVewModel, payouts, famerModel, loanModel, orderModel, new CollectionCreateUpdateListener() {
            @Override
            public void createCollection(Collection c) {
                payoutsVewModel.createCollections(c).observe(PayCard.this, responseModel -> {
                    if (responseModel != null) {
                        a.dismiss();
                        MyToast.toast(responseModel.getResultDescription(), PayCard.this, R.drawable.ic_launcher, Toast.LENGTH_LONG);
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
                MyToast.toast(error, PayCard.this, R.drawable.ic_launcher, Toast.LENGTH_LONG);
                a.dismiss();
            }
        });
    }

    public void initList() {
        recyclerView = findViewById(R.id.recyclerView);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        if (dayCollectionModels == null) {
            dayCollectionModels = new LinkedList<>();
        }


        listAdapter = new FarmerCollectionsAdapter(this, dayCollectionModels, new AdvancedOnclickRecyclerListener() {
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

        }, false);


        recyclerView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();


    }


    @Override
    public void onBackPressed() {


        super.onBackPressed();


    }




}
