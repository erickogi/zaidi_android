package com.dev.lishabora.Views.Trader.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishabora.Adapters.GiveOrderAdapter;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.Cycles;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.OrderModel;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.Trader.OrderConstants;
import com.dev.lishaboramobile.R;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.Objects;

public class GiveOrder extends AppCompatActivity implements StepperLayout.StepperListener {
    private StepperLayout mStepperLayout;
    private GiveOrderAdapter mStepperAdapter;
    private PrefrenceManager prefrenceManager;
    private TraderViewModel mViewModel;
    private int pos;
    public TextView status, id, name, balance, milk, loan, order;
    ImageView imgAdd, imgRemove, imgDelete;
    TextView txtQty, txtPrice;
    TextInputEditText edtAmount;
    Button btnGiveLoan;
    String ampm = "";
    Cycles c;
    String StringValue = null;
    // String PmStringValue = null;
    Double DoubleValue = 0.0;
    // Double PmDoubleValue = 0.0;
    Collection CollModel = null;
    //Collection PmCollModel = null;
    boolean hasAmChanged = false;
    boolean hasPmChanged = false;
    private FamerModel famerModel;
    private TraderViewModel traderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_launch);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        traderViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);
        famerModel = (FamerModel) getIntent().getSerializableExtra("farmer");


        OrderConstants.setProductOrderModels(null);
        OrderConstants.setOrderModel(null);
        OrderConstants.setOrderData(null);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        prefrenceManager = new PrefrenceManager(GiveOrder.this);
        mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);


        mStepperLayout = findViewById(R.id.stepperLayout);
        mStepperAdapter = new GiveOrderAdapter(getSupportFragmentManager(), this);
        mStepperLayout.setAdapter(mStepperAdapter);
        mStepperLayout.setListener(this);
        mStepperLayout.setOffscreenPageLimit(4);

    }

    @Override
    public void onCompleted(View completeButton) {
        OrderModel orderModel = OrderConstants.getOrderModel();
        String orderData = OrderConstants.getOrderData();

        if (orderData != null && orderModel != null && famerModel != null) {

            makeOrder(orderModel, orderData);
        } else {
            Toast.makeText(GiveOrder.this, "No orders Found recoded", Toast.LENGTH_LONG).show();
        }
        //finish();

    }

    private void makeOrder(OrderModel orderModel, String orderData) {
        giveOrder(orderModel.getTotalOrderAmount(), orderData);
    }


    private void giveOrder(String o, String orderDetails) {

        if (DateTimeUtils.Companion.isAM(DateTimeUtils.Companion.getTodayDate())) {

            ampm = "AM";

        } else {

            ampm = "PM";
        }


        StringValue = null;
        DoubleValue = 0.0;


        CollModel = null;


        CollModel = traderViewModel.getCollectionByDateByFarmerByTimeSngle(famerModel.getCode(), DateTimeUtils.Companion.getToday());


        if (CollModel != null) {
            DoubleValue = DoubleValue + Double.valueOf(CollModel.getOrderGivenOutPrice());
            StringValue = String.valueOf(DoubleValue);
        }


        if (CollModel == null) {
                Collection c = new Collection();
                c.setCycleCode(famerModel.getCyclecode());
                c.setFarmerCode(famerModel.getCode());
                c.setFarmerName(famerModel.getNames());
                c.setCycleId(famerModel.getCode());
                c.setDayName(DateTimeUtils.Companion.getDayOfWeek(DateTimeUtils.Companion.getTodayDate(), "E"));
                c.setLoanAmountGivenOutPrice("0");
                c.setDayDate(DateTimeUtils.Companion.getToday());
            c.setTimeOfDay(ampm);
            c.setMilkCollectedAm("0");
                c.setLoanAmountGivenOutPrice("0");
                c.setLoanDetails("");
                c.setOrderGivenOutPrice(o);
                c.setOrderDetails(orderDetails);

                c.setLoanId("");
                c.setOrderId("");
                c.setSynced(0);
                c.setSynced(false);
                c.setApproved(0);


                traderViewModel.createCollections(c, false).observe(this, responseModel -> {
                    if (Objects.requireNonNull(responseModel).getResultCode() == 1) {
                    } else {

                    }


                });
                famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());
                traderViewModel.updateFarmer(famerModel, false);
                finish();

            } else {


            if (CollModel != null) {
                CollModel.setOrderGivenOutPrice(o);
                CollModel.setOrderDetails(orderDetails);
            }
            traderViewModel.updateCollection(CollModel).observe(this, responseModel -> {
                    if (Objects.requireNonNull(responseModel).getResultCode() == 1) {
                    } else {


                    }
                });
                famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());
                traderViewModel.updateFarmer(famerModel, false);
                finish();

            }


    }


    @Override
    public void onError(VerificationError verificationError) {

    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {

    }
}
