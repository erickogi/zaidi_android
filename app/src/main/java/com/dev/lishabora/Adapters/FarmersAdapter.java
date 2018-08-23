package com.dev.lishabora.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.lishabora.Adapters.ViewHolders.FarmerViewHolder;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.Draggable.helper.OnStartDragListener;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.Views.Trader.FarmerConst;
import com.dev.lishaboramobile.R;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.LinkedList;

import github.nisrulz.recyclerviewhelper.RVHAdapter;

public class FarmersAdapter extends RecyclerView.Adapter<FarmerViewHolder> implements RVHAdapter {

    private Context context;
    private LinkedList<FamerModel> modelList;
    private OnclickRecyclerListener listener;
    private OnStartDragListener mDragStartListener;
    private OnStartDragListener mmDragStartListener;
    public FarmersAdapter(Context context, LinkedList<FamerModel> modelList, OnclickRecyclerListener listener, OnStartDragListener dragStartListener) {
        this.context = context;
        mmDragStartListener = dragStartListener;

        this.modelList = modelList;
        this.listener = listener;

    }

    public FarmersAdapter(Context context, LinkedList<FamerModel> modelList, OnclickRecyclerListener listener) {
        this.context = context;
        // mDragStartListener = dragStartListener;

        this.modelList = modelList;
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
    public FarmerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.farmer_card, parent, false);
        //itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.farmer_card_v1, parent, false);

        return new FarmerViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(FarmerViewHolder holder, int position) {
        FamerModel farmer = modelList.get(position);
        holder.balance.setText(farmer.getTotalbalance());
        holder.id.setText(farmer.getCode());
        holder.name.setText(farmer.getNames());
        holder.cycle.setText(farmer.getCyclename());
        holder.route.setText("" + farmer.getRoutename());


        holder.txtDate.setText(DateTimeUtils.Companion.getDisplayDate(farmer.getTransactiontime()));
        String status = "";
        if (farmer.getArchived() == 0 && farmer.getDeleted() == 0 && farmer.getDummy() == 0) {
            status = "Active";
            holder.status.setText(status);
            holder.status.setTextColor(context.getResources().getColor(R.color.green_color_picker));
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.green_color_picker));
            holder.statusview.setBackgroundColor(context.getResources().getColor(R.color.green_color_picker));
        } else {
            StringBuilder stringBuilder = new StringBuilder(status);
            if (farmer.getDeleted() == 1) {
                //stringBuilder.setLength(0);
                stringBuilder.append("|Deleted");
            }
            if (farmer.getArchived() == 1) {
                stringBuilder.append("|Archived");
            }
            if (farmer.getDummy() == 1) {
                stringBuilder.append("|Dummy");

            }
            holder.status.setText(stringBuilder.toString());
            holder.status.setTextColor(context.getResources().getColor(R.color.red));
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.red));
            holder.statusview.setBackgroundColor(context.getResources().getColor(R.color.red));

        }


    }


    @Override
    public int getItemCount() {
        return (null != modelList ? modelList.size() : 0);
    }


//    @Override
//    public void onItemDismiss(int position) {
//        modelList.remove(position);
//        notifyItemRemoved(position);
//    }

    @Override
    public void onItemDismiss(int position, int direction) {
        //modelList.remove(position);
        //notifyItemRemoved(position);
        WeakReference<OnclickRecyclerListener> listenerWeakReference;
        listenerWeakReference = new WeakReference<>(listener);

        if (listener != null) {
            listenerWeakReference.get().onSwipe(position, direction);
        }


    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        try {


            Collections.swap(modelList, fromPosition, toPosition);
            FarmerConst.setSortedFamerModels(modelList);
            notifyItemMoved(fromPosition, toPosition);


        } catch (Exception ignored) {

        }


        return true;
    }

}
