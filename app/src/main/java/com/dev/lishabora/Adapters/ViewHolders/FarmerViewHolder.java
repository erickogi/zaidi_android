package com.dev.lishabora.Adapters.ViewHolders;

import android.support.design.card.MaterialCardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;

import java.lang.ref.WeakReference;

//import com.chauthai.swipereveallayout.SwipeRevealLayout;
//import com.daimajia.swipe.SimpleSwipeListener;
//import com.daimajia.swipe.SwipeLayout;

public class FarmerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    //  public RelativeLayout engineer_background;

    public View frontLayout;
    public View deleteLayout;
    public TextView deleteLayoutTxtName, deleteLayoutTxtLoan, deleteLayoutTxtOrder, deleteTxtProfile, deleteLayoutTxtDelete;


    public TextView status, id, codeLbl, name, cycle, balance, famerscount, route, txtDate;
    LinearLayout linearLayout;
    private WeakReference<OnclickRecyclerListener> listenerWeakReference;
    public RelativeLayout background;
    public View statusview;
    public View itemVew;
    public RelativeLayout lBig;
    public View dragHandle;
    // public SwipeRevealLayout swipeLayout;
    private MaterialCardView card;
    // public SwipeLayout swipeLayout;


    public FarmerViewHolder(View itemView, OnclickRecyclerListener onclickRecyclerListener) {
        super(itemView);
        this.itemVew = itemView;
        linearLayout = itemView.findViewById(R.id.linear);
        listenerWeakReference = new WeakReference<>(onclickRecyclerListener);
        //itemView.setOnClickListener(this);
        //itemView.setOnLongClickListener(this);
        lBig = itemView.findViewById(R.id.l_big);
        lBig.setOnLongClickListener(this);
        statusview = itemView.findViewById(R.id.status_view);
        // background = itemView.findViewById(R.id.background);
        // background.setOnLongClickListener(this);

        card = itemView.findViewById(R.id.card);
        txtDate = itemView.findViewById(R.id.txt_date);
        codeLbl = itemView.findViewById(R.id.txt_code_lbl);

        status = itemView.findViewById(R.id.txt_status);
        id = itemView.findViewById(R.id.txt_id);
        name = itemView.findViewById(R.id.txt_name);
        cycle = itemView.findViewById(R.id.txt_cycle);
        route = itemView.findViewById(R.id.txt_route);
        balance = itemView.findViewById(R.id.txt_balance);

        //   swipeLayout = itemView.findViewById(R.id.swipe);

//        swipeLayout = itemView.findViewById(R.id.swipe_layout);
//
//        frontLayout = itemView.findViewById(R.id.front_layout);
//        deleteLayout = itemView.findViewById(R.id.delete_layout);
//
//        deleteLayoutTxtName = itemView.findViewById(R.id.txt_farmer);
        deleteLayoutTxtLoan = itemView.findViewById(R.id.txt_loan);
        deleteLayoutTxtOrder = itemView.findViewById(R.id.txt_order);
        deleteTxtProfile = itemView.findViewById(R.id.txt_profile);
        deleteLayoutTxtDelete = itemView.findViewById(R.id.txt_delete);

//        deleteLayoutTxtLoan.setOnClickListener(this);
//        deleteLayoutTxtOrder.setOnClickListener(this);
//        deleteTxtProfile.setOnClickListener(this);
//        deleteLayoutTxtDelete.setOnClickListener(this);
////
//        card.setOnClickListener(this);
//        background.setOnClickListener(this);
//        itemView.setOnClickListener(this);
//        itemView.setOnLongClickListener(this);

//        frontLayout.setOnClickListener(this);
        //      linearLayout.setOnClickListener(this);
//        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
//        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
//            @Override
//            public void onOpen(SwipeLayout layout) {
//                // YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
//            }
//        });
//
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                listenerWeakReference.get().onClickListener(getAdapterPosition());
//
//                // Log.d(getClass().getSimpleName(), "onItemSelected: " + textViewData.getText().toString());
//              //  Toast.makeText(view.getContext(), "onItemSelected: " + textViewData.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//       swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
//            @Override
//            public void onDoubleClick(SwipeLayout layout, boolean surface) {
//                listenerWeakReference.get().onClickListener(getAdapterPosition());
//            }
//        });
//        swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listenerWeakReference.get().onClickListener(getAdapterPosition());
//            }
//        });


    }

    public FarmerViewHolder(View itemView) {
        super(itemView);
//        dragHandle = itemView.findViewById(R.id.drag_handle);
        this.itemVew = itemView;
        lBig = itemView.findViewById(R.id.l_big);
        statusview = itemView.findViewById(R.id.status_view);
        background = itemView.findViewById(R.id.background);

        txtDate = itemView.findViewById(R.id.txt_date);

        status = itemView.findViewById(R.id.txt_status);
        id = itemView.findViewById(R.id.txt_id);
        name = itemView.findViewById(R.id.txt_name);
        cycle = itemView.findViewById(R.id.txt_cycle);
        route = itemView.findViewById(R.id.txt_route);
        balance = itemView.findViewById(R.id.txt_balance);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        //lBig.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
//        if ( v.getId() == R.id.card || v.getId() == R.id.background||v==itemVew) {
//            listenerWeakReference.get().onClickListener(getAdapterPosition());
//        } else {
            switch (v.getId()) {

                case R.id.txt_loan:
                    listenerWeakReference.get().onMenuItem(getAdapterPosition(), 1);
                    break;
                case R.id.txt_order:
                    listenerWeakReference.get().onMenuItem(getAdapterPosition(), 2);
                    break;
                case R.id.txt_profile:
                    listenerWeakReference.get().onMenuItem(getAdapterPosition(), 3);
                    break;
                case R.id.txt_delete:
                    listenerWeakReference.get().onMenuItem(getAdapterPosition(), 4);
                    break;
                default:
                    listenerWeakReference.get().onClickListener(getAdapterPosition());

            }
        // }
    }

    @Override
    public boolean onLongClick(View v) {
        listenerWeakReference.get().onClickListener(getAdapterPosition(), v);

        return true;
    }
}
