package com.dev.lishabora.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.lishabora.Adapters.ViewHolders.ProductsViewHolder;
import com.dev.lishabora.Models.ProductsModel;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;

import java.util.LinkedList;
import java.util.List;

//import com.dev.lishaboramobile.Trader.Models.ProductsModel;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsViewHolder> {

    private Context context;
    private List<ProductsModel> modelList;
    private OnclickRecyclerListener listener;
    private boolean isChk = false;

    public ProductsAdapter(Context context, List<ProductsModel> modelList, OnclickRecyclerListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
        this.isChk = false;

    }

    public ProductsAdapter(Context context, List<ProductsModel> modelList, OnclickRecyclerListener listener, boolean isChk) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
        this.isChk = isChk;

    }

    @Override
    public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);

        return new ProductsViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(ProductsViewHolder holder, int position) {
        ProductsModel productsModel = modelList.get(position);

        String vSP = productsModel.getSellingprice();
        String vBp = productsModel.getBuyingprice();


        try {
            vSP = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vSP, 0)));
            vBp = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vBp, 0)));
        } catch (Exception nm) {
            nm.printStackTrace();
        }


        holder.selling.setText(String.format("%s %s", vSP, context.getString(R.string.ksh)));
        holder.cost.setText(String.format("%s %s", vBp, context.getString(R.string.ksh)));




        holder.name.setText(productsModel.getNames());
        holder.status.setText("" + productsModel.getStatus());
        holder.txtDate.setText("");


        if (productsModel.isSelected()) {
            holder.chk.setChecked(true);
        } else {
            holder.chk.setChecked(false);
        }
        if (isChk) {
            holder.imgEdit.setVisibility(View.GONE);

            holder.chk.setVisibility(View.VISIBLE);
        } else {
            holder.chk.setVisibility(View.GONE);
            holder.imgEdit.setVisibility(View.VISIBLE);

        }

        String status = "";

        if (productsModel.getStatus() == 1) {
            holder.status.setText("Active");
            holder.status.setTextColor(context.getResources().getColor(R.color.green_color_picker));
            // holder.background.setBackgroundColor(context.getResources().getColor(R.color.green_color_picker));
            holder.statusview.setBackgroundColor(context.getResources().getColor(R.color.green_color_picker));
//
//
        } else if (productsModel.getStatus() == 0) {

            holder.status.setText("Deleted");
            holder.status.setTextColor(context.getResources().getColor(R.color.red));
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.red));
            holder.statusview.setBackgroundColor(context.getResources().getColor(R.color.red));
//
        } else {
            holder.status.setText("In-Active");
            holder.status.setTextColor(context.getResources().getColor(R.color.blue_color_picker));
            // holder.background.setBackgroundColor(context.getResources().getColor(R.color.blue_color_picker));
            holder.statusview.setBackgroundColor(context.getResources().getColor(R.color.blue_color_picker));
//
        }


    }


    @Override
    public int getItemCount() {
        return (null != modelList ? modelList.size() : 0);
    }


    public void refresh(LinkedList<ProductsModel> filteredProductsModel) {
        modelList = filteredProductsModel;
        notifyDataSetChanged();

    }
}
