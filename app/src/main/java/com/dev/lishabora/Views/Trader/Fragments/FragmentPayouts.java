package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.dev.lishabora.Adapters.PayoutesAdapter;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Utils.MaterialIntro;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.ViewModels.Trader.BalncesViewModel;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishabora.Views.Trader.PayoutConstants;
import com.dev.lishaboramobile.R;
import com.getkeepsafe.taptargetview.TapTarget;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class FragmentPayouts extends Fragment {
    double total, milk, loans, orders;
    private PayoutesAdapter listAdapter;
    private View view;
    private Context context;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout empty_layout;
    private List<com.dev.lishabora.Models.Payouts> payouts;
    private PayoutsVewModel payoutsVewModel;
    private BalncesViewModel balncesViewModel;
    private Fragment fragment;
    boolean type1 = false;
    private Button btnSwitch;


    public void initList() {
        recyclerView = view.findViewById(R.id.recyclerView);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        if (payouts == null) {
            payouts = new LinkedList<>();
        }
        listAdapter = new PayoutesAdapter(getActivity(), payouts, new OnclickRecyclerListener() {
            @Override
            public void onMenuItem(int position, int menuItem) {

            }

            @Override
            public void onSwipe(int adapterPosition, int direction) {


            }

            @Override
            public void onClickListener(int position) {

                Intent intent = new Intent(getActivity(), com.dev.lishabora.Views.Trader.Activities.Payouts.class);
                intent.putExtra("data", payouts.get(position));
                PayoutConstants.setPayouts(payouts.get(position));
                startActivity(intent);








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
            public void onClickListener(int adapterPosition, @NotNull View view) {

//                fragment = new FragmentPayoutFarmersList();
//                Bundle args = new Bundle();
//                args.putSerializable("data", payouts.get(adapterPosition));
//                PayoutConstants.setPayouts(payouts.get(adapterPosition));
//                fragment.setArguments(args);
//                // popOutFragments();
//                setUpView();

            }
        });
        recyclerView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();
        emptyState(listAdapter.getItemCount() > 0);


    }

    void setUpView() {
        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment)
                    .addToBackStack(null).commit();
        }

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trader_payouts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;
        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);
        balncesViewModel = ViewModelProviders.of(this).get(BalncesViewModel.class);

        btnSwitch = view.findViewById(R.id.btn_switch);
        btnSwitch.setOnClickListener(v -> {
            type1 = !type1;
            fetch();

        });
        try {
            Objects.requireNonNull(getActivity()).setTitle("Payouts List");

        } catch (Exception nm) {
            nm.printStackTrace();
        }


    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        initList();
        fetch();
    }

    private void fetch() {
        emptyState(true);

        try {
            // new Thread(() ->
            payoutsVewModel.fetchAll(false).observe(FragmentPayouts.this, FragmentPayouts.this::setData);
            //)

            //.start();
        } catch (Exception xc) {
            xc.printStackTrace();
        }
    }

    private void emptyState(boolean listHasData) {
        LinearLayout empty_layout;
        empty_layout = view.findViewById(R.id.empty_layout);

        if (listHasData) {
            empty_layout.setVisibility(View.GONE);
        } else {
            empty_layout.setVisibility(View.VISIBLE);

        }
    }

    private void setData(List<com.dev.lishabora.Models.Payouts> payouts) {
        if (payouts != null) {


            if (type1) {
                LinkedList<Payouts> payouts1 = new LinkedList<>();
                for (int a = 0; a < payouts.size(); a++) {
                    List<Collection> c = payoutsVewModel.getCollectionByDateByPayoutListOne(payouts.get(a).getCode());
                    payouts1.add(CommonFuncs.createPayouts(c, payouts.get(a), payoutsVewModel, balncesViewModel));


                }
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                    if (this.payouts == null) {
                        this.payouts = new LinkedList<>();
                        this.payouts.addAll(payouts1);
                        listAdapter.notifyDataSetChanged();
                        emptyState(listAdapter.getItemCount() > 0);

                    } else {
                        this.payouts.clear();
                        this.payouts.addAll(payouts1);
                        listAdapter.notifyDataSetChanged();
                        emptyState(listAdapter.getItemCount() > 0);

                    }
                });
            } else {
                LinkedList<Payouts> payouts1 = new LinkedList<>();

                for (Payouts p : payouts) {
                    payouts1.add(CommonFuncs.loadFarmers(p, payoutsVewModel, balncesViewModel));
                }
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> {


                    if (this.payouts == null) {
                        this.payouts = new LinkedList<>();
                        this.payouts.addAll(payouts1);
                        listAdapter.notifyDataSetChanged();
                        emptyState(listAdapter.getItemCount() > 0);

                    } else {
                        this.payouts.clear();
                        this.payouts.addAll(payouts1);
                        listAdapter.notifyDataSetChanged();
                        emptyState(listAdapter.getItemCount() > 0);

                    }
                });
            }
        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

    }
    private SearchView searchView;
    private ImageButton helpView;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);

        //inflater.inflate(R.menu.menu_main, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        mSearch.setVisible(false);
        MenuItem mHelp = menu.findItem(R.id.action_help);
        searchView = (SearchView) mSearch.getActionView();
        searchView.setVisibility(View.GONE);


        helpView = (ImageButton) mHelp.getActionView();
        helpView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_help));
        helpView.setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
        if (!new PrefrenceManager(getContext()).isPayoutsFragmentIntroShown()) {
            showIntro();
        }
        helpView.setOnClickListener(v -> showIntro());

    }
    void showIntro() {
        final Display display = getActivity().getWindowManager().getDefaultDisplay();
        int canvasW = display.getWidth();
        int canvasH = display.getHeight();
        Point centerOfCanvas = new Point(canvasW / 2, canvasH / 2);
        int rectW = 10;
        int rectH = 10;
        int left = centerOfCanvas.x - (rectW / 2);
        int top = centerOfCanvas.y - (rectH / 2);
        int right = centerOfCanvas.x + (rectW / 2);
        int bottom = centerOfCanvas.y + (rectH / 2);
        Rect rect = new Rect(left, top, right, bottom);

        List<TapTarget> targets = new ArrayList<>();
        targets.add(TapTarget.forBounds(rect, "Click on a payout to approve , view (farmers ,collections and payout summary) ", getContext().getResources().getString(R.string.dismiss_intro)).cancelable(false).id(13).transparentTarget(true));
        targets.add(TapTarget.forView(helpView, "Click here to see this introduction again", getContext().getResources().getString(R.string.dismiss_intro)).cancelable(false).id(14).transparentTarget(true));

        MaterialIntro.Companion.showIntroSequence(getActivity(), targets);
        new PrefrenceManager(getContext()).setPayoutsFragmentIntroShown(true);
    }


}
