package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.dev.lishabora.Adapters.NotificationsAdapter;
import com.dev.lishabora.AppConstants;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.Notifications;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.ViewModels.Trader.BalncesViewModel;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishabora.Views.Trader.PayoutConstants;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class FragmentNotifications extends Fragment {
    public static Fragment fragment = null;
    private TraderViewModel traderViewModel;
    private PayoutsVewModel payoutsVewModel;
    private BalncesViewModel balncesViewModel;
    private View view;
    private List<Notifications> notifications;
    private NotificationsAdapter notificationsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        traderViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);
        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);
        balncesViewModel = ViewModelProviders.of(this).get(BalncesViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initList();
        loadNotifications();
    }

    private void initList() {
        if (notifications == null) {
            notifications = new LinkedList<>();
        }
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager mStaggeredLayoutManager;
        notificationsAdapter = new NotificationsAdapter(getContext(), notifications, new OnclickRecyclerListener() {
            @Override
            public void onClickListener(int position) {
                Notifications n = notifications.get(position);
                switch (n.getType()) {
                    case AppConstants
                            .NOTIFICATION_TYPE_INDIVIDUAL_PAYOUT_PENDING:
                        traderViewModel.getPayoutByCode(notifications.get(0).getPayoutCode()).observe(FragmentNotifications.this, new Observer<Payouts>() {
                            @Override
                            public void onChanged(@Nullable Payouts payouts) {
                                if (payouts != null) {
                                    Log.d("padfs", new Gson().toJson(payouts));
                                    List<Collection> c = payoutsVewModel.getCollectionByDateByPayoutListOne(payouts.getCode());
                                    Payouts p = CommonFuncs.createPayoutsByCollection(c, payouts, payoutsVewModel, balncesViewModel, null, false, payoutsVewModel.getFarmersByCycleONe(payouts.getCycleCode()));
                                    Intent intent = new Intent(getActivity(), com.dev.lishabora.Views.Trader.Activities.Payouts.class);
                                    intent.putExtra("data", p);
                                    PayoutConstants.setPayouts(p);
                                    startActivity(intent);
                                }

                            }
                        });

                        break;
                    case AppConstants
                            .NOTIFICATION_TYPE_MULTIPLE_PAYOUT_PENDING:

                        CommonFuncs.popOutFragments(getActivity().getSupportFragmentManager());
                        CommonFuncs.setUpView(new FragmentPayouts(), getActivity().getSupportFragmentManager());
                        break;

                    case AppConstants
                            .NOTIFICATION_TYPE_SYNC_OVERSTAYED:
                        alertSync();


                        break;

                    case AppConstants
                            .NOTIFICATION_TYPE_SERVER_MESSAGE:

                        alertMessage(n.getMessage());
                        break;

                }
            }

            @Override
            public void onLongClickListener(int position) {

            }

            @Override
            public void onCheckedClickListener(int position) {

            }

            @Override
            public void onMoreClickListener(int position) {

            }

            @Override
            public void onMenuItem(int position, int menuItem) {

            }

            @Override
            public void onClickListener(int adapterPosition, @NotNull View view) {

            }

            @Override
            public void onSwipe(int adapterPosition, int direction) {

            }
        });
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        notificationsAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(notificationsAdapter);


    }

    private void alertMessage(String message) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setTitle("Message");
        alertDialogBuilderUserInput.setCancelable(true);
        alertDialogBuilderUserInput.setMessage(message);

        alertDialogBuilderUserInput
                .setCancelable(true)
                .setPositiveButton("Dismiss", (dialogBox, id) -> {


                })

        ;

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        alertDialogAndroid.show();


    }

    private void alertSync() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setTitle("Sync Your Data ");
        alertDialogBuilderUserInput.setCancelable(true);
        alertDialogBuilderUserInput.setMessage(" Hello ... \nIt seems you have not synced your data for the last 7 days.\nTo continue using the app without worries of loosing your valuable data , please re-charge your data balance and put on your internet connection .");


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Okay", (dialogBox, id) -> {


                })

        ;

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        alertDialogAndroid.show();


    }

    private void loadNotifications() {

        traderViewModel.getNotifications().observe(this, new Observer<List<Notifications>>() {
            @Override
            public void onChanged(@Nullable List<Notifications> notifications) {
                if (notifications != null) {
                    showNotifications(notifications);
                }
            }
        });
    }

    private void showNotifications(List<Notifications> notifications) {
        this.notifications = notifications;
        notificationsAdapter.refresh(notifications);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
