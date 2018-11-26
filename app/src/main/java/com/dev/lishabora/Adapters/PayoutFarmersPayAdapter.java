package com.dev.lishabora.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.lishabora.Adapters.ViewHolders.PayoutFarmersPayViewHolder;
import com.dev.lishabora.Models.PayoutFarmersCollectionModel;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishaboramobile.R;

import java.util.List;


public class PayoutFarmersPayAdapter extends RecyclerView.Adapter<PayoutFarmersPayViewHolder> {

    private Context context;
    private List<PayoutFarmersCollectionModel> modelList;
    private OnclickRecyclerListener listener;

    public PayoutFarmersPayAdapter(Context context, List<PayoutFarmersCollectionModel> modelList, OnclickRecyclerListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
    }

    public PayoutFarmersPayAdapter(Context context, List<PayoutFarmersCollectionModel> modelList, OnclickRecyclerListener listener, boolean isChk) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;

    }

    @Override
    public PayoutFarmersPayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.farmer_approve_card, parent, false);

        return new PayoutFarmersPayViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(PayoutFarmersPayViewHolder holder, int position) {
        PayoutFarmersCollectionModel model = modelList.get(position);


        if (model.isChecked()) {
            holder.chk.setChecked(true);
        } else {
            holder.chk.setChecked(false);
        }
        holder.name.setText(model.getFarmername());


        String vB = model.getBalance();
        String vL = model.getLoanTotal();
        String vO = model.getOrderTotal();
        String vD = String.valueOf(Double.valueOf(model.getOrderTotal()) + Double.valueOf(model.getLoanTotal()));
        String vM = model.getMilktotalKsh();
        String vML = model.getMilktotalLtrs();


        try {
            vB = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vB, 0)));


            vL = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vL, 0)));
            vO = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vO, 0)));
            vM = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vM, 0)));
            vD = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vD, 0)));


            vML = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vML, 0)));
        } catch (Exception nm) {
            nm.printStackTrace();
        }


        holder.balance.setText(String.format("%s %s", vB, context.getString(R.string.ksh)));
        GeneralUtills.Companion.changeCOlor(model.getBalance(), holder.balance, 1);

        holder.milk.setText(String.format("%s %s", vML, context.getString(R.string.ltrs)));
        if (!model.getMilktotal().equals("0.0")) {
            holder.milk.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.milk.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.milk.setTypeface(Typeface.DEFAULT);
            holder.milk.setTextColor(context.getResources().getColor(R.color.black));

        }


        holder.deductions.setText(String.format("%s %s", vD, context.getString(R.string.ksh)));
        if (!model.getLoanTotal().equals("0.0")) {

            holder.deductions.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.deductions.setTypeface(Typeface.DEFAULT_BOLD);

        } else {

            holder.deductions.setTypeface(Typeface.DEFAULT);
            holder.deductions.setTextColor(context.getResources().getColor(R.color.black));

        }


        CommonFuncs.setCardActionStatus(model, holder.chk, model.getLoanTotal(), model.getOrderTotal());


    }


    @Override
    public int getItemCount() {
        return (null != modelList ? modelList.size() : 0);
    }


    public void refresh(List<PayoutFarmersCollectionModel> dayCollectionModels) {
        modelList = dayCollectionModels;
        notifyDataSetChanged();
    }
}
