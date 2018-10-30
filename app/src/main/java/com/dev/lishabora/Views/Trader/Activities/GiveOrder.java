package com.dev.lishabora.Views.Trader.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishabora.Adapters.GiveOrderAdapter;
import com.dev.lishabora.AppConstants;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.Cycles;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.OrderModel;
import com.dev.lishabora.Models.ResponseModel;
import com.dev.lishabora.Models.Trader.FarmerOrdersTable;
import com.dev.lishabora.Utils.CollectListener;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.ViewModels.Trader.BalncesViewModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishabora.Views.Trader.OrderConstants;
import com.dev.lishaboramobile.R;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.Objects;

public class GiveOrder extends AppCompatActivity implements StepperLayout.StepperListener, CollectListener {
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
    private BalncesViewModel balncesViewModel;

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
        balncesViewModel = ViewModelProviders.of(this).get(BalncesViewModel.class);


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

        CommonFuncs.giveOrder(o, orderDetails, this, traderViewModel, famerModel);


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

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(this.getSupportActionBar()).hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(this.getSupportActionBar()).show();
    }

    @Override
    public void createCollection(Collection c, FamerModel famerModel, Double aDouble) {
        traderViewModel.createCollections(c).observe(this, responseModel -> {
            if (Objects.requireNonNull(responseModel).getResultCode() == 1) {
                FarmerOrdersTable f = balncesViewModel.getFarmerOrderByCollectionOne(c.getCode());

                famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());

                boolean hasToSyncFarmer = false;
                if (!famerModel.getCurrentPayoutCode().equals(responseModel.getPayoutCode())) {
                    hasToSyncFarmer = true;
                }
                famerModel.setCurrentPayoutCode(responseModel.getPayoutCode());

                FamerModel fm = CommonFuncs.updateBalance(famerModel, traderViewModel, balncesViewModel, c, responseModel.getPayoutCode(), AppConstants.ORDER, null, f);

                traderViewModel.updateFarmer(fm, false, true);


                finish();
            } else {
                snack(responseModel.getResultDescription());

            }


        });
    }

    @Override
    public void updateCollection(Collection c, FamerModel famerModel, Double aDouble) {
        ResponseModel responseModel = traderViewModel.updateCollection(c);//.observe(this, responseModel -> {
            if (Objects.requireNonNull(responseModel).getResultCode() == 1) {

                FarmerOrdersTable f = balncesViewModel.getFarmerOrderByCollectionOne(c.getCode());

                famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());
                boolean hasToSyncFarmer = false;
                if (!famerModel.getCurrentPayoutCode().equals(responseModel.getPayoutCode())) {
                    hasToSyncFarmer = true;
                }
                famerModel.setCurrentPayoutCode(responseModel.getPayoutCode());

                FamerModel fm = CommonFuncs.updateBalance(famerModel, traderViewModel, balncesViewModel, c, responseModel.getPayoutCode(), AppConstants.ORDER, null, f);
                traderViewModel.updateFarmer(fm, false, true);
                finish();
            } else {


                snack(responseModel.getResultDescription());
            }
        // });
    }

    @Override
    public void error(String error) {
        snack(error);

    }


    private void snack(String msg) {
        if (mStepperLayout != null) {

            AlertDialog.Builder d = new AlertDialog.Builder(this);
            d.setMessage(msg);
            d.setCancelable(true);
            d.setPositiveButton("Okay", (dialogInterface, i) -> dialogInterface.dismiss());
            d.show();

        }
    }
}
