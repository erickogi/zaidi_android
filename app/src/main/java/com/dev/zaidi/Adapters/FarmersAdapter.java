package com.dev.zaidi.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.zaidi.Adapters.ViewHolders.FarmerViewHolder;
import com.dev.zaidi.Models.FamerModel;
import com.dev.zaidi.R;
import com.dev.zaidi.Utils.DateTimeUtils;
import com.dev.zaidi.Utils.Draggable.helper.OnStartDragListener;
import com.dev.zaidi.Utils.GeneralUtills;
import com.dev.zaidi.Utils.OnclickRecyclerListener;
import com.dev.zaidi.Utils.TextUtils;

import java.util.LinkedList;
import java.util.List;

public class FarmersAdapter extends RecyclerView.Adapter<FarmerViewHolder>

{
    private Context context;
    private List<FamerModel> modelList;
    private OnclickRecyclerListener listener;

    public void updateFarmer(FamerModel famerModel, int position) {
        notifyItemChanged(position, famerModel);
    }
    public FarmersAdapter(Context context, List<FamerModel> modelList, OnclickRecyclerListener listener, OnStartDragListener dragStartListener) {
        this.context = context;
        // mmDragStartListener = dragStartListener;

        this.modelList = modelList;
        this.listener = listener;

    }

    public FarmersAdapter(Context context, LinkedList<FamerModel> modelList, OnclickRecyclerListener listener) {
        this.context = context;
        // mDragStartListener = dragStartListener;

        this.modelList = modelList;
        this.listener = listener;

    }

//    public void setDraggale(boolean d) {
//        if (d) {
//            mDragStartListener = mmDragStartListener;
//        } else {
//            mDragStartListener = null;
//        }
//    }


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

//        if (pre > 0) {
//
//
//            holder.id.setText("Previous Bal : " + String.valueOf(pre));
//            holder.id.setTextColor(context.getResources().getColor(R.color.red));
//            holder.codeLbl.setVisibility(View.GONE);
//
//        } else {


            holder.id.setTextColor(context.getResources().getColor(R.color.textblack));
            holder.codeLbl.setVisibility(View.VISIBLE);
        //   }

        String v = farmer.getTotalbalance();
        String mL = farmer.getMilkbalance();


        try {
            v = GeneralUtills.Companion.addCommify(GeneralUtills.Companion.round(v, 1));
            mL = GeneralUtills.Companion.addCommify(GeneralUtills.Companion.round(mL, 1));
        } catch (Exception nm) {
            nm.printStackTrace();
        }

        holder.id.setText(String.format("%s%s", mL, context.getString(R.string.ltrs)));

        holder.balance.setText(String.format("%s%s", v, context.getString(R.string.ksh)));
        GeneralUtills.Companion.changeCOlor(v, holder.balance, 1);



        holder.name.setText(GeneralUtills.Companion.capitalize(farmer.getNames()));
        holder.cycle.setText(farmer.getCyclename());
        holder.route.setText(farmer.getRoutename());
        String[] arr = DateTimeUtils.Companion.getDateAndTime(farmer.getLastCollectionTime());
        holder.txtDate.setText(TextUtils.makeSectionOfTextBold(arr[0], arr[1]));

        if (farmer.getArchived() == 1) {
            holder.deleteLayoutTxtDelete.setText("Un-Archive");
        } else {
            holder.deleteLayoutTxtDelete.setText("Archive");

        }

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

//    @Override
//    public void onItemDismiss(int position, int direction) {
//        //modelList.remove(position);
//        //notifyItemRemoved(position);
//        WeakReference<OnclickRecyclerListener> listenerWeakReference;
//        listenerWeakReference = new WeakReference<>(listener);
//
//        if (listener != null) {
//            listenerWeakReference.get().onSwipe(position, direction);
//        }
//
//
//    }
//
//    @Override
//    public boolean onItemMove(int fromPosition, int toPosition) {
//        try {
//
//
//            Collections.swap(modelList, fromPosition, toPosition);
//            FarmerConst.setSortedFamerModels(modelList);
//            notifyItemMoved(fromPosition, toPosition);
//
//
//        } catch (Exception ignored) {
//
//        }
//
//
//        return true;
//    }


}
