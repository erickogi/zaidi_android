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
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.Utils.TextUtils;
import com.dev.lishabora.Views.Trader.FarmerConst;
import com.dev.lishaboramobile.R;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import github.nisrulz.recyclerviewhelper.RVHAdapter;

//import github.nisrulz.recyclerviewhelper.RVHAdapter;

//import com.chauthai.swipereveallayout.ViewBinderHelper;
//import com.daimajia.swipe.SimpleSwipeListener;
//import com.daimajia.swipe.SwipeLayout;
//import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

// RecyclerView.Adapter<FarmerViewHolder> implements RVHAdapter {
public class FarmersAdapter extends RecyclerView.Adapter<FarmerViewHolder> implements RVHAdapter

{
    private Context context;
    private List<FamerModel> modelList;
    private OnclickRecyclerListener listener;
    private OnStartDragListener mDragStartListener;
    private OnStartDragListener mmDragStartListener;
    // private final ViewBinderHelper binderHelper = new ViewBinderHelper();

    public void updateFarmer(FamerModel famerModel, int position) {
        notifyItemChanged(position, famerModel);
    }
    public FarmersAdapter(Context context, List<FamerModel> modelList, OnclickRecyclerListener listener, OnStartDragListener dragStartListener) {
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

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.farmer_card_v2, parent, false);
        //itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.farmer_card_v1, parent, false);

        return new FarmerViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(FarmerViewHolder holder, int position) {
        FamerModel farmer = modelList.get(position);


        Double pre = 0.0;
        try {
            Double b = Double.valueOf(farmer.getPreviousBalance());
            Double c = Double.valueOf(farmer.getTotalbalance());

            pre = (b);


        } catch (Exception nm) {

        }

        if (pre > 0) {


            holder.id.setText("Previous Bal : " + String.valueOf(pre));
            holder.id.setTextColor(context.getResources().getColor(R.color.red));
            holder.codeLbl.setVisibility(View.GONE);

        } else {


            holder.id.setTextColor(context.getResources().getColor(R.color.textblack));
            holder.id.setText(farmer.getCode());
            holder.codeLbl.setVisibility(View.VISIBLE);
        }

        String v = farmer.getTotalbalance();


        try {
            v = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(v, 0)));
        } catch (Exception nm) {
            nm.printStackTrace();
        }


        holder.balance.setText(String.format("%s%s", v, context.getString(R.string.ksh)));
        GeneralUtills.Companion.changeCOlor(v, holder.balance, 1);



        holder.name.setText(GeneralUtills.Companion.capitalize(farmer.getNames()));
        holder.cycle.setText(farmer.getCyclename());
        holder.route.setText(farmer.getRoutename());
        String[] arr = DateTimeUtils.Companion.getDateAndTime(farmer.getLastCollectionTime());
        holder.txtDate.setText(TextUtils.makeSectionOfTextBold(arr[0], arr[1]));
//

    }


    @Override
    public int getItemCount() {
        return (null != modelList ? modelList.size() : 0);
    }

//
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

//    @Override
//    public int getSwipeLayoutResourceId(int position) {
//        return R.id.swipe;
//    }
}
