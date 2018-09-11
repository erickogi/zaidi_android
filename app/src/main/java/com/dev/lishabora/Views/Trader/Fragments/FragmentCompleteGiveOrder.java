package com.dev.lishabora.Views.Trader.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.lishabora.Models.OrderModel;
import com.dev.lishabora.Models.ProductOrderModel;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.Views.Trader.OrderConstants;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.Objects;

public class FragmentCompleteGiveOrder extends Fragment implements BlockingStep {
    public TextView status, id, name, balance, milk, loan, order;
    ImageView imgAdd, imgRemove, imgDelete;
    TextView txtQty, txtPrice;
    TextInputEditText edtAmount, edtDeliveryFee;
    private View view;

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


        double delivery = 0.0;
        double installmentValue = 0.0;
        if (edtDeliveryFee.getText() != null && !TextUtils.isEmpty(edtDeliveryFee.getText())) {
            delivery = Double.valueOf(edtDeliveryFee.getText().toString());

        }
        if (edtAmount.getText() != null && !TextUtils.isEmpty(edtAmount.getText())) {

            double value = Double.valueOf(edtAmount.getText().toString());
            int insNo = Integer.valueOf(((TextView) txtQty).getText().toString());
            if (value > 0.0) {
                installmentValue = ((value + delivery) / insNo);
            }
        }


        txtPrice.setText(String.valueOf(GeneralUtills.Companion.round(installmentValue, 2)));


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_give_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        OrderModel orderModel = new OrderModel();
        orderModel.setInstallmentAmount(txtPrice.getText().toString());
        orderModel.setInstallmentNo(txtQty.getText().toString());
        orderModel.setOrderAmount(edtAmount.getText().toString());

        String delivery = "0";

        if (!TextUtils.isEmpty(edtDeliveryFee.getText())) {
            delivery = edtDeliveryFee.getText().toString();
        }
        orderModel.setOrderDeliveryFee(delivery);

        orderModel.setProductOrderModels(OrderConstants.getProductOrderModels());

        Gson gson = new Gson();

        String data = gson.toJson(orderModel);
        OrderConstants.setOrderData(data);
        OrderConstants.setOrderModel(orderModel);

        callback.complete();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

        OrderModel orderModel = new OrderModel();
        orderModel.setInstallmentAmount(txtPrice.getText().toString());
        orderModel.setInstallmentNo(txtQty.getText().toString());
        orderModel.setOrderAmount(edtAmount.getText().toString());
        String delivery = "0";

        if (!TextUtils.isEmpty(edtDeliveryFee.getText())) {
            delivery = edtDeliveryFee.getText().toString();
        }
        orderModel.setOrderDeliveryFee(delivery);

        orderModel.setProductOrderModels(OrderConstants.getProductOrderModels());

        Gson gson = new Gson();

        String data = gson.toJson(orderModel);
        OrderConstants.setOrderData(data);
        OrderConstants.setOrderModel(orderModel);


        callback.goToPrevStep();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {
        initView();
        initActions();
        initData();

    }


    @Override
    public void onError(@NonNull VerificationError error) {

    }

    private void initData() {

        Double dt = 0.0;
        Double delivery = 0.0;
        if (OrderConstants.getProductOrderModels() != null) {

            for (ProductOrderModel p : OrderConstants.getProductOrderModels()) {
                if (p.getTotalprice() != null) {
                    dt = dt + (Double.valueOf(p.getTotalprice()));
                }
            }
        }
        if (OrderConstants.getOrderModel() != null && OrderConstants.getOrderModel().getOrderDeliveryFee() != null) {
            delivery = Double.valueOf(OrderConstants.getOrderModel().getOrderDeliveryFee());
        }

        edtAmount.setText(String.valueOf(dt));
        edtDeliveryFee.setText(String.valueOf(delivery));


        double installmentValue = 0.0;
        if (!TextUtils.isEmpty(Objects.requireNonNull(edtAmount.getText()).toString())) {
            double value = Double.valueOf(edtAmount.getText().toString());
            int insNo = Integer.valueOf(txtQty.getText().toString());
            if (value > 0.0) {
                installmentValue = ((value + delivery) / insNo);
            }
        }


        txtPrice.setText(String.valueOf(GeneralUtills.Companion.round((installmentValue), 2)));

        if (OrderConstants.getOrderModel() != null) {
            OrderModel l = OrderConstants.getOrderModel();

            if (l.getInstallmentNo() != null) {
                txtQty.setText(l.getInstallmentNo());

                try {
                    if (!TextUtils.isEmpty(Objects.requireNonNull(edtAmount.getText()).toString())) {
                        double value = Double.valueOf(edtAmount.getText().toString());
                        int insNo = Integer.valueOf(txtQty.getText().toString());
                        if (value > 0.0) {
                            installmentValue = ((value + delivery) / insNo);
                        }
                    }
                    txtPrice.setText(String.valueOf(GeneralUtills.Companion.round((installmentValue), 2)));

                } catch (Exception nm) {
                    nm.printStackTrace();
                }

            }


        }
    }


    void initView() {
        id = view.findViewById(R.id.txt_id);
        name = view.findViewById(R.id.txt_name);

        edtAmount = view.findViewById(R.id.edt_value);
        edtDeliveryFee = view.findViewById(R.id.edt_delivery);

        txtQty = view.findViewById(R.id.txt_qty);
        txtPrice = view.findViewById(R.id.txt_installment);

        imgAdd = view.findViewById(R.id.img_add);
        imgRemove = view.findViewById(R.id.img_remove);


    }

    void initActions() {
        imgAdd.setOnClickListener(view -> calc(imgAdd, txtQty));
        imgRemove.setOnClickListener(view -> calc(imgRemove, txtQty));
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
                double delivery = 0.0;
                if (!TextUtils.isEmpty(edtDeliveryFee.getText())) {
                    delivery = Double.valueOf(edtDeliveryFee.getText().toString());
                }
                if (editable != null) {


                    if (edtAmount.getText().toString() != null && !TextUtils.isEmpty(edtAmount.getText().toString())) {
                        double value = Double.valueOf(edtAmount.getText().toString());
                        int insNo = Integer.valueOf(txtQty.getText().toString());
                        if (value > 0.0) {
                            installmentValue = ((value + delivery) / insNo);
                        }
                    }

                }
                txtPrice.setText(String.valueOf(GeneralUtills.Companion.round((installmentValue), 2)));
            }
        });
        edtDeliveryFee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                double installmentValue = 0.0;
                double delivery = 0.0;
                if (!TextUtils.isEmpty(edtDeliveryFee.getText())) {
                    delivery = Double.valueOf(edtDeliveryFee.getText().toString());
                }
                if (editable != null) {


                    if (edtAmount.getText().toString() != null && !TextUtils.isEmpty(edtAmount.getText().toString())) {
                        double value = Double.valueOf(edtAmount.getText().toString());
                        int insNo = Integer.valueOf(txtQty.getText().toString());
                        if (value > 0.0) {
                            installmentValue = ((value + delivery) / insNo);
                        }
                    }

                }
                txtPrice.setText(String.valueOf(GeneralUtills.Companion.round((installmentValue), 2)));
            }
        });


    }
}

