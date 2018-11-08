package com.dev.lishabora.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.lishabora.Adapters.ViewHolders.ProductOrderViewHolder;
import com.dev.lishabora.Models.ProductOrderModel;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;

import java.util.List;

//import com.dev.lishaboramobile.Trader.Models.ProductOrderModel;

public class ProductOrderAdapter extends RecyclerView.Adapter<ProductOrderViewHolder> {

    private Context context;
    private List<ProductOrderModel> modelList;
    private OnclickRecyclerListener listener;
    private boolean isChk = false;

    public ProductOrderAdapter(Context context, List<ProductOrderModel> modelList, OnclickRecyclerListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
        this.isChk = false;

    }

    public ProductOrderAdapter(Context context, List<ProductOrderModel> modelList, OnclickRecyclerListener listener, boolean isChk) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
        this.isChk = isChk;

    }

    @Override
    public ProductOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_order_card, parent, false);

        return new ProductOrderViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(ProductOrderViewHolder holder, int position) {
        ProductOrderModel productsModel = modelList.get(position);

        String vTP = productsModel.getTotalprice();
        String vBp = productsModel.getBuyingprice();


        try {
            vTP = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vTP, 0)));
            vBp = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vBp, 0)));
        } catch (Exception nm) {
            nm.printStackTrace();
        }


        holder.selling.setText(String.format("%s %s", vTP, context.getString(R.string.ksh)));
        holder.name.setText(productsModel.getNames());


        holder.cost.setText(String.format("%s %s", vBp, context.getString(R.string.ksh)));
        holder.txtQty.setText(productsModel.getQuantity());

    }


    @Override
    public int getItemCount() {
        return (null != modelList ? modelList.size() : 0);
    }


    public void refresh(List<ProductOrderModel> filteredProductOrderModel) {
        modelList = filteredProductOrderModel;
        notifyDataSetChanged();

    }
}
