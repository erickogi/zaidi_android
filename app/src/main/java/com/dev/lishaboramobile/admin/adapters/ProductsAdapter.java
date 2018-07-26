package com.dev.lishaboramobile.admin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.lishaboramobile.Global.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.admin.adapters.viewHolders.ProductsViewHolder;
import com.dev.lishaboramobile.admin.models.ProductsModel;

import java.util.List;

//import com.dev.lishaboramobile.Trader.Models.ProductsModel;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsViewHolder> {

    private Context context;
    private List<ProductsModel> modelList;
    private OnclickRecyclerListener listener;

    public ProductsAdapter(Context context, List<ProductsModel> modelList, OnclickRecyclerListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;

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

        holder.selling.setText(productsModel.getSellingprice());
//        holder.id.setText(productsModel.getId());
        holder.name.setText(productsModel.getNames());
        holder.cost.setText(productsModel.getCostprice());
        holder.status.setText("" + productsModel.getStatus());

        String status = "";

        if (productsModel.getStatus() == 1) {
            holder.status.setText("Active");
            holder.status.setTextColor(context.getResources().getColor(R.color.green_color_picker));
//
        } else if (productsModel.getStatus() == 0) {

            holder.status.setText("Deleted");
            holder.status.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            holder.status.setText("In-Active");
            holder.status.setTextColor(context.getResources().getColor(R.color.blue_color_picker));
        }


    }


    @Override
    public int getItemCount() {
        return (null != modelList ? modelList.size() : 0);
    }


}
