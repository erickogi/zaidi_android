package com.dev.zaidi.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.zaidi.Adapters.ViewHolders.LoanOrderPaymentViewHolder;
import com.dev.zaidi.Models.Trader.LoanPayments;
import com.dev.zaidi.Models.Trader.OrderPayments;
import com.dev.zaidi.R;
import com.dev.zaidi.Utils.DateTimeUtils;
import com.dev.zaidi.Utils.Draggable.helper.OnStartDragListener;
import com.dev.zaidi.Utils.GeneralUtills;
import com.dev.zaidi.Utils.OnclickRecyclerListener;

import java.util.List;


public class LoansOrdersPaymnetsAdapter extends RecyclerView.Adapter<LoanOrderPaymentViewHolder>

{
    private Context context;
    private List<LoanPayments> modelListLoans;
    private List<OrderPayments> modelListOrders;
    private OnclickRecyclerListener listener;
    private OnStartDragListener mDragStartListener;
    private OnStartDragListener mmDragStartListener;
    // private final ViewBinderHelper binderHelper = new ViewBinderHelper();


    public LoansOrdersPaymnetsAdapter(Context context, List<LoanPayments> modelListLoans, List<OrderPayments> modelListOrders, OnclickRecyclerListener listener) {
        this.context = context;
        // mDragStartListener = dragStartListener;

        this.modelListLoans = modelListLoans;
        this.modelListOrders = modelListOrders;
        this.listener = listener;

    }

    public void setDraggale(boolean d) {
        if (d) {
            mDragStartListener = mmDragStartListener;
        } else {
            mDragStartListener = null;
        }
    }


    @Override
    public LoanOrderPaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trader_loanorderpayment_card, parent, false);
        //itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.farmer_card_v1, parent, false);

        return new LoanOrderPaymentViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(LoanOrderPaymentViewHolder holder, int position) {

        if (modelListLoans != null) {

            LoanPayments model = modelListLoans.get(position);
            holder.txtDate.setText(DateTimeUtils.Companion.getDisplayDate(model.getTimeStamp()));
            holder.balance.setText(model.getAmountRemaining());

            holder.txtMode.setText(model.getPaymentMethod());


            String vA = model.getAmountPaid();

            try {
                vA = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vA, 0)));

            } catch (Exception nm) {
                nm.printStackTrace();
            }
            holder.amount.setText(String.format("%s %s", vA, context.getString(R.string.ksh)));




        } else {
            OrderPayments model = modelListOrders.get(position);
            holder.txtDate.setText(DateTimeUtils.Companion.getDisplayDate(model.getTimestamp()));
            //   holder.farmer.setText("");
            holder.balance.setText(model.getAmountRemaining());

            holder.txtMode.setText(model.getPaymentMethod());

            String vA = model.getAmountPaid();

            try {
                vA = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vA, 0)));

            } catch (Exception nm) {
                nm.printStackTrace();
            }
            holder.amount.setText(String.format("%s %s", vA, context.getString(R.string.ksh)));




        }


    }


    @Override
    public int getItemCount() {

        if (modelListOrders == null && modelListLoans == null) {
            return 0;
        } else if (modelListLoans != null) {
            return modelListLoans.size();
        } else {
            return modelListOrders.size();
        }

    }

}
