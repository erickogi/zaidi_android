package com.dev.zaidi.Views.Trader.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dev.zaidi.Adapters.PayoutFarmersAdapter;
import com.dev.zaidi.Models.ApprovalRegisterModel;
import com.dev.zaidi.Models.Collection;
import com.dev.zaidi.Models.DayCollectionModel;
import com.dev.zaidi.Models.FamerModel;
import com.dev.zaidi.Models.LoanModel;
import com.dev.zaidi.Models.OrderModel;
import com.dev.zaidi.Models.PayoutFarmersCollectionModel;
import com.dev.zaidi.Models.Payouts;
import com.dev.zaidi.R;
import com.dev.zaidi.Utils.ApproveListener;
import com.dev.zaidi.Utils.CollectionCreateUpdateListener;
import com.dev.zaidi.Utils.MyToast;
import com.dev.zaidi.Utils.OnclickRecyclerListener;
import com.dev.zaidi.Utils.PrefrenceManager;
import com.dev.zaidi.Utils.RecyclerTouchListener;
import com.dev.zaidi.ViewModels.Trader.BalncesViewModel;
import com.dev.zaidi.ViewModels.Trader.PayoutsVewModel;
import com.dev.zaidi.ViewModels.Trader.TraderViewModel;
import com.dev.zaidi.Views.CommonFuncs;
import com.dev.zaidi.Views.Trader.Activities.PayCard;
import com.dev.zaidi.Views.Trader.PayoutConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.joda.time.format.PeriodFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

import static com.dev.zaidi.Models.FamerModel.farmerDateComparator;
import static com.dev.zaidi.Models.FamerModel.farmerNameComparator;
import static com.dev.zaidi.Models.FamerModel.farmerPosComparator;
import static com.dev.zaidi.Views.CommonFuncs.setPayoutActionStatus;

public class FragmentPayoutFarmersList extends Fragment {
    //    public TextView status, startDate, cycleName, endDate, milkTotal, loanTotal, orderTotal, balance, approvedCount, unApprovedCount;
//    public RelativeLayout background;
//    public View statusview;
    private View view;

    List<Integer> unclickableRows, unswipeableRows;
    private RecyclerTouchListener onTouchListener;
    private PayoutFarmersAdapter listAdapter;
    private Context context;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout empty_layout;
    private Payouts payouts;
    private PayoutsVewModel payoutsVewModel;
    private BalncesViewModel balncesViewModel;
    private TraderViewModel traderViewModel;
    private List<PayoutFarmersCollectionModel> dayCollectionModels;
    private List<PayoutFarmersCollectionModel> dayCollectionModels1;
    private List<FamerModel> famerModels;
    private List<Collection> collections;
    TextView txtApprovalStatus;
    private MaterialButton btnApprove;
    private final int CHRONOLOGICAL = 1, ALPHABETICAL = 2, AUTOMATICALLY = 0, MANUALLY = 3;
    private PrefrenceManager prefrenceManager;
    private SearchView searchView;
    private ImageButton helpView;

    private String filterText = "";
    private int SORTTYPE = 0;

    private MaterialDialog materialDialog;

    public void initList() {
        unclickableRows = new ArrayList<>();
        unswipeableRows = new ArrayList<>();




        recyclerView = view.findViewById(R.id.recyclerView);
        listAdapter = new PayoutFarmersAdapter(getActivity(), dayCollectionModels, new OnclickRecyclerListener() {
            @Override
            public void onMenuItem(int position, int menuItem) {

            }

            @Override
            public void onSwipe(int adapterPosition, int direction) {


            }

            @Override
            public void onClickListener(int position) {

                List<Collection> collections = payoutsVewModel.getCollectionByDateByPayoutByFarmerListOne(dayCollectionModels.get(position).getPayoutCode(), dayCollectionModels.get(position).getFarmercode());
                if (collections.size() < 1) {
                    CommonFuncs.silentValueMilk(1, 1, 1, CommonFuncs.setUpDayCollectionsModel(payouts, collections).get(0),
                            (value, adapterPosition, time, type, dayCollectionModel, alertDialogAndroid) -> {
                                updateCollectionValue(dayCollectionModels.get(position).getFamerModel(), value, time, type, dayCollectionModel, null, null, null);
                            });
                }





                Gson gson = new Gson();
                String element = gson.toJson(dayCollectionModels, new TypeToken<ArrayList<PayoutFarmersCollectionModel>>() {
                }.getType());
                Gson g = new Gson();
                            Timber.tag("farmerCilcked").d("clicked " + position);
                            Intent intent = new Intent(getActivity(), PayCard.class);
                            intent.putExtra("data", dayCollectionModels.get(position));
                            intent.putExtra("payout", payouts);
                            intent.putExtra("farmers", element);
                intent.putExtra("farmer", dayCollectionModels.get(position).getFamerModel());

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


            }
        });

        onTouchListener = new RecyclerTouchListener(getActivity(), recyclerView);
        onTouchListener
                .setIndependentViews(R.id.btn_approve)
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        Timber.tag("farmerCilcked").d("clicked " + position);
                        List<Collection> collections = payoutsVewModel.getCollectionByDateByPayoutByFarmerListOne(dayCollectionModels.get(position).getPayoutCode(), dayCollectionModels.get(position).getFarmercode());
                        if (collections.size() < 1) {
                            CommonFuncs.silentValueMilk(1, 1, 1, CommonFuncs.setUpDayCollectionsModel(payouts, collections).get(0), (value, adapterPosition, time, type, dayCollectionModel, alertDialogAndroid)
                                    -> updateCollectionValue(dayCollectionModels.get(position).getFamerModel(), value, time, type, dayCollectionModel, null, null, null));
                        }


                        Gson gson = new Gson();
                        String element = gson.toJson(dayCollectionModels, new TypeToken<ArrayList<PayoutFarmersCollectionModel>>() {
                        }.getType());

                        Timber.tag("farmerCilcked").d("clicked " + position);
                        Intent intent = new Intent(getActivity(), PayCard.class);
                        intent.putExtra("data", dayCollectionModels.get(position));
                        intent.putExtra("payout", payouts);
                        intent.putExtra("farmers", element);
                        intent.putExtra("farmer", dayCollectionModels.get(position).getFamerModel());

                        startActivity(intent);

                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {
                        List<Collection> collections = payoutsVewModel.getCollectionByDateByPayoutByFarmerListOne(dayCollectionModels.get(position).getPayoutCode(), dayCollectionModels.get(position).getFarmercode());
                        if (collections.size() < 1) {
                            CommonFuncs.silentValueMilk(1, 1, 1, CommonFuncs.setUpDayCollectionsModel(payouts, collections).get(0), (value, adapterPosition, time, type, dayCollectionModel, alertDialogAndroid)
                                    -> updateCollectionValue(dayCollectionModels.get(position).getFamerModel(), value, time, type, dayCollectionModel, null, null, null));
                        }
                        if (independentViewID == R.id.btn_approve) {
                            if (dayCollectionModels.get(position).getCardstatus() == 1) {

                                ApprovalRegisterModel approvalRegisterModel = payoutsVewModel.getByFarmerPayoutCodeOne(dayCollectionModels.get(position).getFarmercode(), payouts.getCode());//.observe(FragmentPayoutFarmersList.this, new Observer<ApprovalRegisterModel>() {

                                cancelCard(dayCollectionModels.get(position), position, approvalRegisterModel);


                            } else {
                                approveCard(dayCollectionModels.get(position), position);
                            }
                        }

                    }
                })

                .setLongClickable(true, position -> {

                });


        if (dayCollectionModels == null) {
            dayCollectionModels = new LinkedList<>();
        }


        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();


        if (CommonFuncs.canApprovePayout(payouts)) {
            btnApprove.setVisibility(View.VISIBLE);
        } else {
            btnApprove.setVisibility(View.GONE);
        }



        btnApprove.setOnClickListener(view -> {

            if (CommonFuncs.allCollectionsAreApproved(payoutsVewModel, payouts)) {
                payouts.setStatus(1);
                payouts.setStatusName("Approved");
                payoutsVewModel.updatePayout(payouts);
                starterPack();
            } else {
                MyToast.errorToast("Some farmer cards in this payout are not approved yet", getContext());
            }

        });
        setPayoutActionStatus(payouts, getContext(), btnApprove, txtApprovalStatus);





    }


    private void cancelCard(PayoutFarmersCollectionModel model, int position, ApprovalRegisterModel approvalRegisterModel) {
        materialDialog = new MaterialDialog.Builder(getActivity())
                .title("Canceling farmer's card")
                .content("Please wait ..")
                .cancelable(false)
                .progress(true, 0).build();


        CancelFuncs.cancelCard(getActivity(), payouts.getCode(), model, approvalRegisterModel, traderViewModel, balncesViewModel, payoutsVewModel, new ApproveListener() {
            @Override
            public void onComplete() {
                if (materialDialog != null) {
                    materialDialog.dismiss();
                }
                dayCollectionModels.get(position).setCardstatus(0);
                model.setCardstatus(0);
                dayCollectionModels.get(position).setStatusName("Approval Canceled");
            }

            @Override
            public void onStart() {

                if (materialDialog != null) {
                    materialDialog.show();
                }
            }

            @Override
            public void onProgress(int progress) {

                if (materialDialog != null) {
                    materialDialog.setProgress(progress);
                }
            }

            @Override
            public void onError(String error) {
                if (materialDialog != null) {
                    materialDialog.dismiss();
                }
                MyToast.errorToast(error, getContext());
            }
        });

    }

    private void approveCard(PayoutFarmersCollectionModel model, int position) {
        materialDialog = new MaterialDialog.Builder(getActivity())
                .title("Approving farmer's card")
                .content("Please wait ..")
                .cancelable(false)
                .progress(true, 0).build();


        ApproveFuncs.approveCard(getActivity(), payouts.getCode(), model, traderViewModel, balncesViewModel, payoutsVewModel, new ApproveListener() {
            @Override
            public void onComplete() {
                if (materialDialog != null) {
                    materialDialog.dismiss();
                }
                dayCollectionModels.get(position).setCardstatus(0);

            }

            @Override
            public void onStart() {

                if (materialDialog != null) {
                    materialDialog.show();
                }
            }

            @Override
            public void onProgress(int progress) {

                if (materialDialog != null) {
                    materialDialog.setProgress(progress);
                }
            }

            @Override
            public void onError(String error) {
                if (materialDialog != null) {
                    materialDialog.dismiss();
                }
                MyToast.errorToast(error, getContext());
            }
        });

    }

    private void action(int position, int i) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Not implemented here
                return false;
            case R.id.action_search:
                // Do Fragment menu item stuff here

                return true;
            case R.id.action_automatically:
                // Do Fragment menu item stuff here
                SORTTYPE = AUTOMATICALLY;
                prefrenceManager.setSortType(SORTTYPE);
                setUpFarmerCollectionList();

                return true;

            case R.id.action_chronologically:
                SORTTYPE = CHRONOLOGICAL;
                prefrenceManager.setSortType(SORTTYPE);

                setUpFarmerCollectionList();

                return true;
            case R.id.action_alphabetically:
                SORTTYPE = ALPHABETICAL;
                prefrenceManager.setSortType(SORTTYPE);

                setUpFarmerCollectionList();

                // Do Fragment menu item stuff here
                return true;
            case R.id.action_smanually:
                SORTTYPE = MANUALLY;
                prefrenceManager.setSortType(SORTTYPE);

                setUpFarmerCollectionList();

                // Do Fragment menu item stuff here
                return true;


            default:
                break;
        }

        return false;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payout_farmers, container, false);
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        prefrenceManager = new PrefrenceManager(getContext());

        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);
        balncesViewModel = ViewModelProviders.of(this).get(BalncesViewModel.class);
        traderViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);


        if (getArguments() != null) {
            payouts = (Payouts) getArguments().getSerializable("data");
        } else {
            payouts = PayoutConstants.getPayouts();
        }


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        btnApprove = view.findViewById(R.id.btn_approve);
        txtApprovalStatus = view.findViewById(R.id.txt_approval_status);
        btnApprove.setVisibility(View.GONE);
        initList();




    }


    private void loadFarmers() {


        try {
            payoutsVewModel.getFarmersByCycle("" + payouts.getCycleCode()).observe(this, famerModels -> {
                if (famerModels != null) {

                    FragmentPayoutFarmersList.this.famerModels = famerModels;

                    loadCollectionPayouts();

                } else {

                }
            });
        } catch (Exception nm) {
            nm.printStackTrace();
        }
    }

    private void loadCollectionPayouts() {

        payoutsVewModel.getCollectionByDateByPayout("" + payouts.getCode()).observe(this, collections -> {
            if (collections != null) {



                FragmentPayoutFarmersList.this.collections = collections;
                setUpFarmerCollectionList();
            }
        });


    }

    private void updateCollectionValue(FamerModel famerModel, String s, int time, int type, DayCollectionModel dayCollectionModel,
                                       AlertDialog a, @Nullable LoanModel loanModel, @Nullable OrderModel orderModel) {
        CommonFuncs.updateCollectionValue(s, time, type, dayCollectionModel, payoutsVewModel, payouts, famerModel, loanModel, orderModel, new CollectionCreateUpdateListener() {
            @Override
            public void createCollection(Collection c) {
                payoutsVewModel.createCollectionsS(c);
                CommonFuncs.addBalance(famerModel, traderViewModel, balncesViewModel, c, payouts.getCode(), type, null, null);

            }

            @Override
            public void updateCollection(Collection c) {
                payoutsVewModel.updateCollectionS(c);

            }

            @Override
            public void error(String error) {
                MyToast.toast(error, getContext());

            }
        });
    }

    private void setUpFarmerCollectionList() {

        List<PayoutFarmersCollectionModel> collectionModels = new LinkedList<>();

        int sort = new PrefrenceManager(getContext()).getSortType();
        switch (sort) {
            case AUTOMATICALLY:
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> listAdapter.notifyDataSetChanged());
                break;
            case ALPHABETICAL:
                filterFarmersAlpahbetically();
                break;
            case CHRONOLOGICAL:
                filterFarmersChronologically();
                break;
            case MANUALLY:
                filterFarmersManually();
                break;
            default:
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> listAdapter.notifyDataSetChanged());
        }




        for (FamerModel famerModel : famerModels) {

            if (famerModel.getArchived() == 0 && famerModel.getDeleted() == 0 && famerModel.getDummy() == 0) {


                collectionModels.add(CommonFuncs.getFarmersCollectionModel(famerModel, collections, payouts, balncesViewModel));

            }

        }


        setUpList(collectionModels);


    }


    private PeriodFormatter mPeriodFormat;
    private Date previousdate;

    private void setUpList(List<PayoutFarmersCollectionModel> dayCollectionModels) {
        this.dayCollectionModels = dayCollectionModels;
        this.dayCollectionModels1 = dayCollectionModels;

        Objects.requireNonNull(getActivity()).runOnUiThread(() -> listAdapter.refresh(dayCollectionModels));

        // initList();
    }


    private void filterFarmersAlpahbetically() {


        Collections.sort(famerModels, farmerNameComparator);
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> listAdapter.notifyDataSetChanged());


    }

    private void filterFarmersChronologically() {


        Collections.sort(famerModels, farmerDateComparator);
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> listAdapter.notifyDataSetChanged());


    }

    private void filterFarmersManually() {

        Collections.sort(famerModels, farmerPosComparator);
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> listAdapter.notifyDataSetChanged());
    }

    @Override
    public void onStart() {
        super.onStart();
        starterPack();
    }

    private void starterPack() {
        // initCardHeader();
        //initList();
        new Thread(this::loadFarmers).start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);

        //inflater.inflate(R.menu.menu_main, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        MenuItem mHelp = menu.findItem(R.id.action_help);


        MenuItem mAutomatically = menu.findItem(R.id.action_automatically);
        MenuItem mManually = menu.findItem(R.id.action_manually);
        MenuItem mChronologically = menu.findItem(R.id.action_chronologically);
        MenuItem mAlphabetically = menu.findItem(R.id.action_alphabetically);
        MenuItem mRearrangeManually = menu.findItem(R.id.action_smanually);

        mAutomatically.setVisible(false);
        mChronologically.setVisible(true);
        mManually.setVisible(true);
        mAlphabetically.setVisible(true);
        mRearrangeManually.setVisible(false);
        mHelp.setVisible(false);


        searchView = (SearchView) mSearch.getActionView();
        searchView.setVisibility(View.GONE);

        searchView = (SearchView) mSearch.getActionView();
        searchView.setVisibility(View.GONE);


        helpView = (ImageButton) mHelp.getActionView();
        helpView.setVisibility(View.GONE);
    }



    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filterText = s;
                filterFarmers();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterText = s;
                filterFarmers();

                return true;
            }
        });

    }

    private void filterFarmers() {
        if (dayCollectionModels1 == null) { dayCollectionModels1 = new LinkedList<>(); }
        dayCollectionModels.clear();
        if (dayCollectionModels1 != null && dayCollectionModels1.size() > 0) {
            for (PayoutFarmersCollectionModel famerModel : dayCollectionModels1) {


                if (famerModel.getFarmername().toLowerCase().contains(filterText.toLowerCase()) || famerModel.getFarmercode().toLowerCase().contains(filterText.toLowerCase())) {
                    dayCollectionModels.add(famerModel);
                }

            }
        }
        listAdapter.refresh(dayCollectionModels);
    }

    @Override
    public void onResume() {
        super.onResume();


        recyclerView.addOnItemTouchListener(onTouchListener);


    }

    @Override
    public void onPause() {
        super.onPause();
        filterText = "";
        recyclerView.removeOnItemTouchListener(onTouchListener);

    }


}
