package com.dev.lishabora.Adapters.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.lishabora.Utils.AdvancedOnclickRecyclerListener;
import com.dev.lishaboramobile.R;

import java.lang.ref.WeakReference;

public class FarmerCollectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, TextWatcher {

    public TextView date, day;
    public TextView milkTotalAm, milkTotalPm, loanTotalAm, loanTotalPm, orderTotalAm, orderTotalPm;
    public RelativeLayout background;
    public View statusview;
    LinearLayout background_linear;
    private WeakReference<AdvancedOnclickRecyclerListener> listenerWeakReference;


    public FarmerCollectionViewHolder(View itemView, AdvancedOnclickRecyclerListener onclickRecyclerListener, boolean isEditable) {
        super(itemView);
        listenerWeakReference = new WeakReference<>(onclickRecyclerListener);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        background_linear = itemView.findViewById(R.id.background_linear);
        background_linear.setOnClickListener(this);
        statusview = itemView.findViewById(R.id.status_view);
        background = itemView.findViewById(R.id.background);
        background.setOnClickListener(this);
        date = itemView.findViewById(R.id.txt_date);
        day = itemView.findViewById(R.id.txt_day);
        milkTotalAm = itemView.findViewById(R.id.txt_am_milk);
        milkTotalPm = itemView.findViewById(R.id.txt_pm_milk);
        loanTotalAm = itemView.findViewById(R.id.txt_am_loans);
        loanTotalPm = itemView.findViewById(R.id.txt_pm_loans);
        orderTotalAm = itemView.findViewById(R.id.txt_am_orders);
        orderTotalPm = itemView.findViewById(R.id.txt_pm_orders);


//        if(isEditable) {
        milkTotalAm.setOnClickListener(this);
        milkTotalPm.setOnClickListener(this);
        orderTotalAm.setOnClickListener(this);
        orderTotalPm.setOnClickListener(this);
        loanTotalAm.setOnClickListener(this);
        loanTotalPm.setOnClickListener(this);
//        }else {
//            milkTotalPm.setEnabled(false);
//            milkTotalAm.setEnabled(false);
//            loanTotalAm.setEnabled(false);
//            loanTotalPm.setEnabled(false);
//            orderTotalAm.setEnabled(false);
//            orderTotalPm.setEnabled(false);
//
//
//        }


//        milkTotalAm.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                listenerWeakReference.get().onEditTextChanged(getAdapterPosition(), 1, 1, editable);
//            }
//        });
//        milkTotalPm.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                listenerWeakReference.get().onEditTextChanged(getAdapterPosition(), 2, 1, editable);
//            }
//        });
//        loanTotalAm.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                listenerWeakReference.get().onEditTextChanged(getAdapterPosition(), 1, 2, editable);
//            }
//        });
//        loanTotalPm.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                listenerWeakReference.get().onEditTextChanged(getAdapterPosition(), 2, 2, editable);
//            }
//        });
//        orderTotalAm.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                listenerWeakReference.get().onEditTextChanged(getAdapterPosition(), 1, 3, editable);
//            }
//        });
//        orderTotalPm.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                listenerWeakReference.get().onEditTextChanged(getAdapterPosition(), 2, 3, editable);
//            }
//        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_am_milk:
                listenerWeakReference.get().onEditTextChanged(getAdapterPosition(), 1, 1, v);

                break;
            case R.id.txt_pm_milk:
                listenerWeakReference.get().onEditTextChanged(getAdapterPosition(), 2, 1, v);

                break;
            case R.id.txt_am_loans:
                listenerWeakReference.get().onEditTextChanged(getAdapterPosition(), 1, 2, v);

                break;
            case R.id.txt_pm_loans:
                listenerWeakReference.get().onEditTextChanged(getAdapterPosition(), 2, 2, v);

                break;
            case R.id.txt_am_orders:
                listenerWeakReference.get().onEditTextChanged(getAdapterPosition(), 1, 3, v);

                break;
            case R.id.txt_pm_orders:
                listenerWeakReference.get().onEditTextChanged(getAdapterPosition(), 2, 3, v);

                break;


        }
        listenerWeakReference.get().onClickListener(getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View v) {
        listenerWeakReference.get().onClickListener(getAdapterPosition(), v);

        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        //editable.
        //listenerWeakReference.get().onEditTextChanged();
    }
}
