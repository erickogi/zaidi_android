package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishabora.Adapters.FarmersAdapter;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.Cycles;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.MilkModel;
import com.dev.lishabora.Models.RPFSearchModel;
import com.dev.lishabora.Models.ResponseModel;
import com.dev.lishabora.Models.RoutesModel;
import com.dev.lishabora.Models.UnitsModel;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.Draggable.helper.OnStartDragListener;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.Utils.RequestDataCallback;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.Trader.Activities.CreateFarmerActivity;
import com.dev.lishabora.Views.Trader.Activities.FarmerProfile;
import com.dev.lishabora.Views.Trader.Activities.FirstTimeLaunch;
import com.dev.lishabora.Views.Trader.Activities.GiveOrder;
import com.dev.lishabora.Views.Trader.FarmerConst;
import com.dev.lishabora.Views.Trader.OrderConstants;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import github.nisrulz.recyclerviewhelper.RVHItemClickListener;
import github.nisrulz.recyclerviewhelper.RVHItemDividerDecoration;
import github.nisrulz.recyclerviewhelper.RVHItemTouchHelperCallback;
import timber.log.Timber;

import static com.dev.lishabora.Models.FamerModel.farmerDateComparator;
import static com.dev.lishabora.Models.FamerModel.farmerNameComparator;
import static com.dev.lishabora.Models.FamerModel.farmerPosComparator;

public class FragementFarmersList extends Fragment implements OnStartDragListener {
    FarmersAdapter listAdapter;
    private final int CHRONOLOGICAL = 1, ALPHABETICAL = 2, AUTOMATICALLY = 0, MANUALLY = 3;
    public static Fragment fragment = null;

    Gson gson = new Gson();
    boolean isArchived = false;
    boolean isDummy = false;
    private View view;
    private Context context;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout empty_layout;
    private TextView emptyTxt, txt_network_state;
    private AVLoadingIndicatorView avi;
    private String filterText = "";
    FloatingActionButton fab;
    MaterialSpinner spinner1, spinner2;
    LinearLayout lspinner1, lspinner2;
    List<UnitsModel> getUnits = new LinkedList<>();
    private SearchView searchView;
    List<RoutesModel> getRoutess = new LinkedList<>();
    List<Cycles> getCycles = new LinkedList<>();
    private TraderViewModel mViewModel;
    private PrefrenceManager prefrenceManager;
    private List<RoutesModel> routesModels;
    private ItemTouchHelper mItemTouchHelper;
    private Button btnDrag;
    private boolean isDraggable = true;
    private int SORTTYPE = 0;


    boolean isAm = false;
    String ampm = "";
    Cycles c;


    String AmStringValue = null;
    String PmStringValue = null;


    Double AmDoubleValue = 0.0;
    Double PmDoubleValue = 0.0;


    Collection AmCollModel = null;
    Collection PmCollModel = null;


    boolean hasAmChanged = false;
    boolean hasPmChanged = false;

    public void collectMilk(FamerModel famerModel) {


        if (DateTimeUtils.Companion.isAM(DateTimeUtils.Companion.getTodayDate())) {

            ampm = "AM";

        } else {

            ampm = "PM";
        }


        AmStringValue = null;
        PmStringValue = null;
        AmDoubleValue = 0.0;
        PmDoubleValue = 0.0;


        AmCollModel = null;
        PmCollModel = null;


        AmCollModel = mViewModel.getCollectionByDateByFarmerByTimeSngle(famerModel.getCode(), DateTimeUtils.Companion.getToday(), "AM");
        PmCollModel = mViewModel.getCollectionByDateByFarmerByTimeSngle(famerModel.getCode(), DateTimeUtils.Companion.getToday(), "PM");


        if (AmCollModel != null) {
            AmDoubleValue = AmDoubleValue + Double.valueOf(AmCollModel.getMilkCollected());
            AmStringValue = String.valueOf(AmDoubleValue);
        }

        if (PmCollModel != null) {

            PmDoubleValue = PmDoubleValue + Double.valueOf(PmCollModel.getMilkCollected());
            PmStringValue = String.valueOf(PmDoubleValue);
        }


        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_collect_milk, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        alertDialogBuilderUserInput.setView(mView);


        alertDialogBuilderUserInput
                .setCancelable(false);
        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialogAndroid.show();

        MaterialButton btnPositive, btnNegative, btnNeutral;
        TextView txtTitle;
        LinearLayout lTitle;
        ImageView imgIcon;

        UnitsModel unitsModel = new UnitsModel();
        unitsModel.setUnitcapacity(famerModel.getUnitcapacity());
        unitsModel.setUnitprice(famerModel.getUnitprice());
        unitsModel.setUnit(famerModel.getUnitname());


        TextView names, balance, day1, day2, day3, day1am, day1pm, day2am, day2pm, day3am, day3pm, today, unitName, unitPrice, unitTotal;
        TextInputEditText edtTodayAm, edtTodayPm;


        names = mView.findViewById(R.id.txt_name);
        balance = mView.findViewById(R.id.txt_balance);

        day1 = mView.findViewById(R.id.txt_day_1);
        day1am = mView.findViewById(R.id.txt_day_1_am);
        day1pm = mView.findViewById(R.id.txt_day_1_pm);

        day2 = mView.findViewById(R.id.txt_day_2);
        day2am = mView.findViewById(R.id.txt_day_2_am);
        day2pm = mView.findViewById(R.id.txt_day_2_pm);

        day3 = mView.findViewById(R.id.txt_day_3);
        day3am = mView.findViewById(R.id.txt_day_3_am);
        day3pm = mView.findViewById(R.id.txt_day_3_pm);

        today = mView.findViewById(R.id.txt_today);

        unitName = mView.findViewById(R.id.txtUnitName);
        unitPrice = mView.findViewById(R.id.txtUnitPrice);
        unitTotal = mView.findViewById(R.id.txtCost);

        edtTodayAm = mView.findViewById(R.id.edt_am);
        edtTodayPm = mView.findViewById(R.id.edt_pm);


        names.setText(famerModel.getNames());
        balance.setText(famerModel.getTotalbalance());

        today.setText(DateTimeUtils.Companion.getDayOfWeek(DateTimeUtils.Companion.getTodayDate(), "E"));
        day3.setText(DateTimeUtils.Companion.getDayPrevious(1, "E"));
        day2.setText(DateTimeUtils.Companion.getDayPrevious(2, "E"));
        day1.setText(DateTimeUtils.Companion.getDayPrevious(3, "E"));

        unitName.setText(unitsModel.getUnit());
        unitPrice.setText(unitsModel.getUnitprice());


        getCollection(famerModel.getCode(), DateTimeUtils.Companion.getDatePrevious(3), day1am, day1pm);
        getCollection(famerModel.getCode(), DateTimeUtils.Companion.getDatePrevious(2), day2am, day2pm);
        getCollection(famerModel.getCode(), DateTimeUtils.Companion.getDatePrevious(1), day3am, day3pm);


        getCollection(famerModel.getCode(), DateTimeUtils.Companion.getToday(), edtTodayAm, edtTodayPm);


//        if (AmStringValue != null) {
//            edtTodayAm.setText(AmStringValue);
//
//            try {
//                edtTodayAm.setSelection(AmStringValue.length());
//            } catch (Exception nm) {
//                nm.printStackTrace();
//            }
//
//        }
//        if (PmStringValue != null) {
//            edtTodayPm.setText(PmStringValue);
//
//            try {
//                edtTodayPm.setSelection(PmStringValue.length());
//            } catch (Exception nm) {
//                nm.printStackTrace();
//            }
//
//        }


        edtTodayAm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                hasAmChanged = true;
                if (editable != null && editable.length() > 0) {
                    if (unitsModel.getUnitprice() != null) {

                        Double price = Double.valueOf(unitsModel.getUnitprice());
                        Double unitCapacity = Double.valueOf(unitsModel.getUnitcapacity()) / 1000;
                        Double total = (Double.valueOf(edtTodayAm.getText().toString()) * unitCapacity) * price;


                        unitTotal.setText(String.valueOf(total));

                    }
                } else {
                    unitTotal.setText("");
                }
            }
        });
        edtTodayPm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                hasPmChanged = true;
                if (editable != null && editable.length() > 0) {
                    if (unitsModel.getUnitprice() != null) {

                        Double price = Double.valueOf(unitsModel.getUnitprice());
                        Double unitCapacity = Double.valueOf(unitsModel.getUnitcapacity()) / 1000;
                        Double total = (Double.valueOf(edtTodayPm.getText().toString()) * unitCapacity) * price;


                        unitTotal.setText(String.valueOf(total));

                    }
                } else {
                    unitTotal.setText("");
                }
            }
        });

        if (DateTimeUtils.Companion.isAM(DateTimeUtils.Companion.getTodayDate())) {

            edtTodayAm.requestFocus();


        } else {
            edtTodayPm.requestFocus();


        }






        btnPositive = mView.findViewById(R.id.btn_positive);
        btnNegative = mView.findViewById(R.id.btn_negative);
        btnNeutral = mView.findViewById(R.id.btn_neutral);
        txtTitle = mView.findViewById(R.id.txt_title);
        lTitle = mView.findViewById(R.id.linear_title);
        imgIcon = mView.findViewById(R.id.img_icon);


        btnNeutral.setVisibility(View.GONE);
        lTitle.setVisibility(View.GONE);
        txtTitle.setVisibility(View.VISIBLE);
        imgIcon.setVisibility(View.GONE);
        imgIcon.setImageResource(R.drawable.ic_add_black_24dp);
        txtTitle.setText("Route");

        btnPositive.setOnClickListener(view -> {
            String milkAm = null;
            String milkPm = null;
            if (!TextUtils.isEmpty(edtTodayAm.getText().toString())) {
                milkAm = edtTodayAm.getText().toString();
                // alertDialogAndroid.dismiss();
                Timber.tag("milkCollDebug").d("Milk Am As On Button Click " + milkAm);

            }
            if (!TextUtils.isEmpty(edtTodayPm.getText().toString())) {
                milkPm = edtTodayPm.getText().toString();
                Timber.tag("milkCollDebug").d("Milk Am As On Button Click " + milkPm);


            }



            if (hasAmChanged) {
                Timber.tag("milkCollDebug").d("HAS AM CHANGED TRUE ");

                MilkModel milkModel = new MilkModel();
                milkModel.setUnitQty(milkAm);
                milkModel.setUnitsModel(unitsModel);




                if (AmStringValue == null && AmDoubleValue == 0.0 && AmCollModel == null) {

                    Timber.tag("milkCollDebug").d("AM STRING - NULL, AM DOUBLE - NULL AM COLL MODEL - NULL DOING A NEW AM COLLECTION MILK " + milkAm);


                    Collection c = new Collection();
                    c.setCycleCode(famerModel.getCyclecode());
                    c.setFarmerCode(famerModel.getCode());
                    c.setFarmerName(famerModel.getNames());
                    c.setCycleId(famerModel.getCode());
                    c.setDayName(today.getText().toString());
                    c.setLoanAmountGivenOutPrice("0");
                    c.setDayDate(DateTimeUtils.Companion.getToday());
                    c.setTimeOfDay("AM");


                    c.setMilkCollected(milkAm);
                    c.setMilkCollectedValueKsh(milkModel.getValueKsh());
                    c.setMilkCollectedValueLtrs(milkModel.getValueLtrs());
                    c.setMilkDetails(new Gson().toJson(milkModel));






                    c.setLoanAmountGivenOutPrice("0");
                    c.setOrderGivenOutPrice("0");

                    c.setLoanId("");
                    c.setOrderId("");
                    c.setSynced(0);
                    c.setSynced(false);
                    c.setApproved(0);


                    mViewModel.createCollections(c, false).observe(FragementFarmersList.this, responseModel -> {
                        if (Objects.requireNonNull(responseModel).getResultCode() == 1) {
                        } else {
                            snack(responseModel.getResultDescription());

                        }


                    });
                    famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());
                    mViewModel.updateFarmer(famerModel, false);
                } else {

                    Timber.tag("milkCollDebug").d("AM STRING -! NULL, AM DOUBLE - !NULL AM COLL MODEL - !NULL DOING AN UPDATE AM COLLECTION MILK " + milkAm);

                    //UPDATE COLLECTION AS THERE WAS A PREVIOUS AM COLLECTION FOR THIS FARMER ON THIS DAY AND TIME

                    AmCollModel.setMilkCollected(milkAm);
                    AmCollModel.setMilkCollectedValueKsh(milkModel.getValueKsh());
                    AmCollModel.setMilkCollectedValueLtrs(milkModel.getValueLtrs());
                    AmCollModel.setMilkDetails(new Gson().toJson(milkModel));




                    mViewModel.updateCollection(AmCollModel).observe(FragementFarmersList.this, responseModel -> {
                        if (Objects.requireNonNull(responseModel).getResultCode() == 1) {
                        } else {
                            snack(responseModel.getResultDescription());

                        }
                    });
                    famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());
                    mViewModel.updateFarmer(famerModel, false);

                }


            }

            if (hasPmChanged) {
                Timber.tag("milkCollDebug").d("HAS PM CHANGED -TRUE ");
                MilkModel milkModel = new MilkModel();
                milkModel.setUnitQty(milkPm);
                milkModel.setUnitsModel(unitsModel);


                if (PmStringValue == null && PmDoubleValue == 0.0 && PmCollModel == null) {

                    Timber.tag("milkCollDebug").d("PM STRING - NULL, PM DOUBLE - NULL PM COLL MODEL - NULL DOING A NEW PM COLLECTION MILK " + milkPm);

                    //NEW COLLECTION AS THERE WERE NO PREVIOUS PM COLLECTION FOR THIS FARMER ON THIS DAY AND TIME
                    Collection c = new Collection();
                    c.setCycleCode(famerModel.getCyclecode());
                    c.setFarmerCode(famerModel.getCode());
                    c.setFarmerName(famerModel.getNames());
                    c.setCycleId(famerModel.getCode());
                    c.setDayName(today.getText().toString());
                    c.setLoanAmountGivenOutPrice("0");
                    c.setDayDate(DateTimeUtils.Companion.getToday());
                    c.setTimeOfDay("PM");


                    c.setMilkCollected(milkPm);
                    c.setMilkCollectedValueKsh(milkModel.getValueKsh());
                    c.setMilkCollectedValueLtrs(milkModel.getValueLtrs());
                    c.setMilkDetails(new Gson().toJson(milkModel));





                    c.setLoanAmountGivenOutPrice("0");
                    c.setOrderGivenOutPrice("0");

                    c.setLoanId("");
                    c.setOrderId("");
                    c.setSynced(0);
                    c.setSynced(false);
                    c.setApproved(0);


                    mViewModel.createCollections(c, false).observe(FragementFarmersList.this, responseModel -> {
                        if (Objects.requireNonNull(responseModel).getResultCode() == 1) {
                        } else {
                            snack(responseModel.getResultDescription());

                        }


                    });
                    famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());
                    mViewModel.updateFarmer(famerModel, false);

                } else {
                    //UPDATE COLLECTION AS THERE WAS A PREVIOUS PM COLLECTION FOR THIS FARMER ON THIS DAY AND TIME
                    Timber.tag("milkCollDebug").d("PM STRING -! NULL, PM DOUBLE - !NULL AM COLL MODEL - !NULL DOING AN UPDATE PM COLLECTION  MILK " + milkPm);


                    PmCollModel.setMilkCollected(milkPm);
                    PmCollModel.setMilkCollectedValueKsh(milkModel.getValueKsh());
                    PmCollModel.setMilkCollectedValueLtrs(milkModel.getValueLtrs());
                    PmCollModel.setMilkDetails(new Gson().toJson(milkModel));

                    mViewModel.updateCollection(PmCollModel).observe(FragementFarmersList.this, responseModel -> {
                        if (Objects.requireNonNull(responseModel).getResultCode() == 1) {
                        } else {
                            snack(responseModel.getResultDescription());

                        }
                    });
                    famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());
                    mViewModel.updateFarmer(famerModel, false);


                }


            }


            alertDialogAndroid.dismiss();
            
            
            
            
            
            
            
            
            
            
            
            
            



        });
        btnNeutral.setOnClickListener(view -> {

        });
        btnNegative.setOnClickListener(view -> alertDialogAndroid.dismiss());

    }

    private String getCollection(String code, String date, TextView txtAm, TextView txtPm) {

        Timber.tag("tagssearch").d(code + "  Date " + date);
        List<Collection> collections = mViewModel.getCollectionByDateByFarmer(code, date);//.observe(FragementFarmersList.this, collections -> {

        if (txtAm != null && txtPm != null) {
            if (collections != null) {

                for (Collection c : collections) {
                    Timber.tag("tagssearch").d(code + "  Response " + c.getMilkCollected());

                    if (c.getTimeOfDay() != null) {
                        if (c.getTimeOfDay().equals("AM")) {
                            txtAm.setText(c.getMilkCollected());
                        } else {
                            txtPm.setText(c.getMilkCollected());
                        }
                    }
                }

            }
        }


        return "";

    }

    void collectMilkShow(AlertDialog alertDialog) {
        alertDialog.show();

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

    public void initList() {
        recyclerView = view.findViewById(R.id.recyclerView);
        listAdapter = new FarmersAdapter(getActivity(), FarmerConst.getSearchFamerModels(), new OnclickRecyclerListener() {
            @Override
            public void onSwipe(int adapterPosition, int direction) {

                FamerModel fm = FarmerConst.getSearchFamerModels().get(adapterPosition);
                fm.setLastCollectionTime(DateTimeUtils.Companion.getNow());
                mViewModel.updateFarmer(fm, false);


            }

            @Override
            public void onClickListener(int position) {

                if (FarmerConst.getSearchFamerModels().get(position).getDeleted() == 0 && FarmerConst.getSearchFamerModels().get(position).getArchived() == 0) {
                    collectMilk(FarmerConst.getSearchFamerModels().get(position));
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
            public void onClickListener(int adapterPosition, @NotNull View view) {
                if (FarmerConst.getSearchFamerModels().get(adapterPosition).getDeleted() == 1) {

                } else {
                    try {
                        popupMenu(adapterPosition, view, FarmerConst.getSearchFamerModels().get(adapterPosition));
                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }
                }

            }
        }, this);


        ItemTouchHelper.Callback callback = new RVHItemTouchHelperCallback(listAdapter, false, true,
                true);
        ItemTouchHelper helper = new ItemTouchHelper(callback);


        helper.attachToRecyclerView(recyclerView);

        // Set the divider in the recyclerview
        recyclerView.addItemDecoration(new RVHItemDividerDecoration(getContext(), LinearLayoutManager.VERTICAL));


        recyclerView.addOnItemTouchListener(new RVHItemClickListener(context, (view, position) -> {
            if (FarmerConst.getSearchFamerModels().get(position).getDeleted() == 0 && FarmerConst.getSearchFamerModels().get(position).getArchived() == 0) {

                collectMilk(FarmerConst.getSearchFamerModels().get(position));
            }

        }));



    }


    public void update(List<FamerModel> famerModels) {

        Timber.d("update started");

        if (FarmerConst.getFamerModels() != null && listAdapter != null) {
            Timber.d("update started");

            FarmerConst.getFamerModels().clear();
            FarmerConst.getFamerModels().addAll(famerModels);
            filterFarmers();


        } else {

            FarmerConst.setFamerModels(new LinkedList<>());

            filterFarmers();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context = getContext();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);

        //inflater.inflate(R.menu.menu_main, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);


        MenuItem mAutomatically = menu.findItem(R.id.action_automatically);
        MenuItem mManually = menu.findItem(R.id.action_manually);
        MenuItem mChronologically = menu.findItem(R.id.action_chronologically);
        MenuItem mAlphabetically = menu.findItem(R.id.action_alphabetically);
        MenuItem mRearrangeManually = menu.findItem(R.id.action_smanually);

        mAutomatically.setVisible(true);
        mChronologically.setVisible(true);
        mManually.setVisible(true);
        mAlphabetically.setVisible(true);
        mRearrangeManually.setVisible(true);


        searchView = (SearchView) mSearch.getActionView();
        searchView.setVisibility(View.GONE);

    }

    void setDraggale(boolean draggale) {
        if (listAdapter != null) {
            listAdapter.setDraggale(draggale);
        }
        if (draggale) {
            isDraggable = true;
            btnDrag.setVisibility(View.VISIBLE);
        } else {
            //listAdapter.setDraggale(false);
            isDraggable = false;
            btnDrag.setVisibility(View.GONE);
        }
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
                filterFarmers();
                setDraggale(false);
                return true;
            case R.id.action_manually:
                //SORTTYPE = MANUALLY;
                //startDrag();
                fragment = new FragementFarmersDragList();
                setUpView();

                // Do Fragment menu item stuff here
                return true;
            case R.id.action_chronologically:
                SORTTYPE = CHRONOLOGICAL;
                filterFarmers();
                setDraggale(false);
                // Do Fragment menu item stuff here
                return true;
            case R.id.action_alphabetically:
                SORTTYPE = ALPHABETICAL;
                filterFarmers();
                setDraggale(false);
                // Do Fragment menu item stuff here
                return true;
            case R.id.action_smanually:
                SORTTYPE = MANUALLY;
                filterFarmers();
                setDraggale(false);
                // Do Fragment menu item stuff here
                return true;


            default:
                break;
        }

        return false;
    }

    void setUpView() {
        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment)
                    .addToBackStack(null).commit();
        }

    }

    void popOutFragments() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }
    private void startDrag() {

        setDraggale(true);
        filterFarmers();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_farmers_list, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);

        prefrenceManager = new PrefrenceManager(getContext());

        recyclerView = view.findViewById(R.id.recyclerView);
        empty_layout = view.findViewById(R.id.empty_layout);
        emptyTxt = view.findViewById(R.id.empty_text);
        avi = view.findViewById(R.id.avi);
        txt_network_state = view.findViewById(R.id.txt_network_state);
        btnDrag = view.findViewById(R.id.draggaing);
        btnDrag.setVisibility(View.GONE);
        btnDrag.setOnClickListener(view13 -> {
            isDraggable = false;
            btnDrag.setVisibility(View.GONE);
            setDraggale(false);
            updateItems();
        });
        setDraggale(false);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(viehw -> {

            if (prefrenceManager.isTraderFirstTime()) {
                alertDialogFirstTime();
            } else {
                if (isSetUp()) {
                    FragementFarmersList.this.createFarmers();
                } else {
                    initDataOffline(true, true, true);
                    MyToast.toast("Routes, Units and Cycles Not set Up", getContext(), R.drawable.ic_launcher, Toast.LENGTH_LONG);
                }
            }
        });


        spinner1 = view.findViewById(R.id.spinner1);
        lspinner1 = view.findViewById(R.id.lspinner1);
        spinner2 = view.findViewById(R.id.spinner2);
        lspinner2 = view.findViewById(R.id.lspinner2);
        spinner2.setVisibility(View.VISIBLE);
        lspinner2.setVisibility(View.VISIBLE);
        //spinner1.setItems("Active","Archived","Deleted","Dummy","All");


        spinner1.setItems("Active", "Archived", "Deleted", "Dummy", "All");
        getRoutes();

        spinner1.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view1, position, id, item) -> {

            fetchFarmers(getSelectedAccountStatus(), getSelectedRoute());

        });

        spinner2.setOnItemSelectedListener((view12, position, id, item) -> {

            fetchFarmers(getSelectedAccountStatus(), getSelectedRoute());



        });
        if (prefrenceManager.isRoutesListFirstTime() || prefrenceManager.isCycleListFirstTime() || prefrenceManager.isUnitListFirstTime()) {
            initDataOffline(false, false, false);
        }


        try {
            Objects.requireNonNull(getActivity()).setTitle("Farmers List");
        } catch (Exception nm) {
            nm.printStackTrace();
        }
    }

    private void alertDialogFirstTime() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setTitle("Initial Account Setup ");
        alertDialogBuilderUserInput.setCancelable(true);
        alertDialogBuilderUserInput.setMessage("It seems to be the first time you are logging into the app.. \nTo continue using the app fully, you will need to setup you basic details ,routes , cycles and products information .\n Press SET UP to do so  thank you.");


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Set Up", (dialogBox, id) -> {
                    // ToDo get user input here
                    startActivity(new Intent(getActivity(), FirstTimeLaunch.class));


                })

        ;

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        alertDialogAndroid.show();


    }

    private void updateItems() {
        for (int a = 1; a < FarmerConst.getSearchFamerModels().size() + 1; a++) {
            FamerModel f = FarmerConst.getSearchFamerModels().get(a - 1);
            f.setPosition(a);

            mViewModel.updateFarmer(f, false);
        }
    }

    private int getSelectedAccountStatus() {
        Timber.d("Status selected" + spinner1.getSelectedIndex());
        return spinner1.getSelectedIndex();
    }

    private String getSelectedRoute() {

        if (spinner2.getSelectedIndex() == 0) {
            return "";
        } else return spinner2.getItems().get(spinner2.getSelectedIndex()).toString();
    }


    private void fetchFarmers(int staus, String route) {

        avi.smoothToShow();
        avi.setVisibility(View.VISIBLE);

        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);


        }

        mViewModel.getFarmerByStatusRoute(staus, route).observe(FragementFarmersList.this, famerModels -> {
            avi.smoothToHide();
            prefrenceManager.setIsFarmerListFirst(false);

            if (famerModels != null) {
                for (int a = 0; a < famerModels.size(); a++) {
                    famerModels.get(a).setTotalbalance(String.valueOf(mViewModel.getBalance(famerModels.get(a).getCode())));
                }
            }
            update(famerModels);
        });
    }

    private boolean isSetUp() {
        if (getCycles == null && getCycles.size() < 1) {
            return false;
        }
        if (getUnits == null && getUnits.size() < 1) {
            return false;
        }
        return getRoutess != null || getRoutess.size() >= 1;
    }
    //spinner1.setItems("Filter Automatically", "By Route", "Chronologically", "Manually", "Alphabetically", "By Account Status");

    private void setUpSpinner2(int pos) {
        switch (pos) {
            case 0:

                filterFarmers();
                break;
            case 2:
                filterFarmers();
                break;
            case 3:
                filterFarmers();
                break;

            case 1:
                getRoutes();
                //spinner2.setItems(getRoutes());
                setUpSpinner2Listner();
                break;
            case 5:
                setUpSpinner2Listner();
                break;

            case 4:
                filterFarmersAlpahbetically();
            default:


        }
    }

    private void setUpSpinner2Listner() {
        spinner2.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view1, position, id, item) -> {

            filterFarmers();
        });
    }

    private void getRoutes() {
        if (mViewModel != null) {
            mViewModel.getRoutes(false).observe(this, routesModels -> {
                prefrenceManager.setIsRoutesListFirst(false);
                if (routesModels != null && routesModels.size() > 0) {
                    FragementFarmersList.this.routesModels = routesModels;
                    String routes[] = new String[routesModels.size() + 1];
                    routes[0] = "All";

                    for (int a = 1; a < routesModels.size() + 1; a++) {
                        routes[a] = routesModels.get(a - 1).getRoute();

                    }

                    spinner2.setItems(routes);
                    //filterFarmers();
                } else {
                    // getRoutesOnline();
                }
                //spinner2.setItems(routesModels);
            });
        }
    }

    private void getRoutesOnline() {
        if (mViewModel != null) {
            mViewModel.getRoutes(true).observe(this, routesModels -> {
                prefrenceManager.setIsRoutesListFirst(false);
                if (routesModels != null && routesModels.size() > 0) {
                    FragementFarmersList.this.routesModels = routesModels;
                    String routes[] = new String[routesModels.size() + 1];
                    routes[0] = "All";

                    for (int a = 1; a < routesModels.size() + 1; a++) {
                        routes[a] = routesModels.get(a - 1).getRoute();

                    }

                    spinner2.setItems(routes);
                    //filterFarmers();
                }
                //spinner2.setItems(routesModels);
            });
        }
    }

    private void createFarmers() {
        startActivity(new Intent(getActivity(), CreateFarmerActivity.class));
    }



    private JSONObject getTraderFarmeroductsObject() {
        RPFSearchModel rpfSearchModel = new RPFSearchModel();
        rpfSearchModel.setEntitycode(new PrefrenceManager(getActivity()).getTraderModel().getCode());
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(rpfSearchModel));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void getFarmers() {
        avi.smoothToShow();
        avi.setVisibility(View.VISIBLE);

        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);

        }

        mViewModel.getFarmers(getTraderFarmeroductsObject(), prefrenceManager.isFarmerListFirstTime()).observe(FragementFarmersList.this, new Observer<List<FamerModel>>() {
            @Override
            public void onChanged(@Nullable List<FamerModel> famerModels) {
                avi.smoothToHide();
                prefrenceManager.setIsFarmerListFirst(false);
                update(famerModels);
            }
        });
    }




    private void populateTraders() {
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        listAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(listAdapter);


        emptyState(listAdapter.getItemCount() > 0, "We couldn't find any farmers records", empty_layout, null, emptyTxt);

    }

    private void emptyState(boolean listHasData, String text, LinearLayout empty_layout, ImageView empty_image, TextView emptyTxt) {
        if (listHasData) {
            //  empty_layout.setVisibility(View.GONE);
        } else {
            //  empty_layout.setVisibility(View.VISIBLE);
            if (text != null) {
                //     emptyTxt.setText(text);
            }
        }
    }


    private void filterFarmers() {


        if (FarmerConst.getSearchFamerModels() == null) {

            FarmerConst.setSearchFamerModels(new LinkedList<>());
        }


        FarmerConst.getSearchFamerModels().clear();
        if (FarmerConst.getFamerModels() != null && FarmerConst.getFamerModels().size() > 0) {
            for (FamerModel famerModel : FarmerConst.getFamerModels()) {
                if (famerModel.getCode().toLowerCase().contains(filterText) ||
                        famerModel.getMobile().toLowerCase().contains(filterText) ||
                        famerModel.getNames().toLowerCase().contains(filterText)) {
                    FarmerConst.getSearchFamerModels().add(famerModel);


                }

            }
        }


        switch (SORTTYPE) {
            case AUTOMATICALLY:
                listAdapter.notifyDataSetChanged();
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
                listAdapter.notifyDataSetChanged();
        }









        }



    private void filterFarmersAlpahbetically() {


        Collections.sort(FarmerConst.getSearchFamerModels(), farmerNameComparator);
        listAdapter.notifyDataSetChanged();


    }

    private void filterFarmersChronologically() {


        Collections.sort(FarmerConst.getSearchFamerModels(), farmerDateComparator);
        listAdapter.notifyDataSetChanged();


    }

    private void filterFarmersManually() {


        Collections.sort(FarmerConst.getSearchFamerModels(), farmerPosComparator);
        listAdapter.notifyDataSetChanged();


    }
    @Override
    public void onStart() {
        super.onStart();
        FarmerConst.setFamerModels(new LinkedList<>());
        FarmerConst.setFilteredFamerModels(new LinkedList<>());
        FarmerConst.setSortedFamerModels(new LinkedList<>());
        FarmerConst.setSearchFamerModels(new LinkedList<>());

        try {
            Objects.requireNonNull(getActivity()).setTitle("Farmers");
        } catch (Exception nm) {
            nm.printStackTrace();
        }


        initList();
        populateTraders();
        fetchFarmers(0, "");



    }

    @Override
    public void onResume() {
        super.onResume();


        fetchFarmers(0, "");
        filterFarmers();

    }

    @Override
    public void onPause() {
        super.onPause();
        filterText = "";

    }

    @Override
    public void onStop() {
        super.onStop();
        filterText = "";

    }

    private void snack(String msg) {
        if (view != null) {
            Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null);//.show();

            AlertDialog.Builder d = new AlertDialog.Builder(getContext());
            d.setMessage(msg);
            d.setCancelable(true);
            d.setPositiveButton("Okay", (dialogInterface, i) -> dialogInterface.dismiss());
            d.show();

        }
    }


    void startAnim() {
        avi.show();

    }

    void stopAnim() {
        avi.hide();

    }


    private void popupMenu(int pos, View view, FamerModel famerModel) {
        PopupMenu popupMenu = new PopupMenu(Objects.requireNonNull(getContext()), view);
        popupMenu.inflate(R.menu.farmer_list_menu);

        isArchived = false;
        isDummy = false;
        if (famerModel.getDeleted() == 1) {

            popupMenu.getMenu().getItem(4).setTitle("Un-Delete");
        }

        if (famerModel.getArchived() == 1) {
            isArchived = true;
            popupMenu.getMenu().getItem(5).setTitle("Un-Archive");
        }


        if (famerModel.getDummy() == 1) {
            isDummy = true;
            popupMenu.getMenu().getItem(6).setTitle("Remove from dummy");


        }


        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.delete:


                    famerModel.setStatus("Deleted");
                    famerModel.setDeleted(1);
                    avi.smoothToShow();
                    mViewModel.updateFarmer(famerModel, false).observe(FragementFarmersList.this, responseModel -> avi.smoothToHide());
                    FragementFarmersList.this.fetchFarmers(FragementFarmersList.this.getSelectedAccountStatus(), FragementFarmersList.this.getSelectedRoute());//update(famerModel);


                    break;

                case R.id.archive:

                    if (isArchived) {
                        famerModel.setStatus("Active");
                        famerModel.setArchived(0);
                    } else {
                        famerModel.setStatus("Archived");
                        famerModel.setArchived(1);
                    }
                    avi.smoothToShow();
                    mViewModel.updateFarmer(famerModel, false).observe(FragementFarmersList.this, responseModel -> avi.smoothToHide());
                    FragementFarmersList.this.fetchFarmers(FragementFarmersList.this.getSelectedAccountStatus(), FragementFarmersList.this.getSelectedRoute());


                    break;

                case R.id.dummy:

                    if (isDummy) {
                        famerModel.setStatus("Active");
                        famerModel.setDummy(0);
                    } else {
                        famerModel.setStatus("Dummy");
                        famerModel.setDummy(1);
                    }
                    avi.smoothToShow();
                    mViewModel.updateFarmer(famerModel, false).observe(FragementFarmersList.this, new Observer<ResponseModel>() {
                        @Override
                        public void onChanged(@Nullable ResponseModel responseModel) {
                            avi.smoothToHide();
                            fetchFarmers(getSelectedAccountStatus(), getSelectedRoute());

                        }
                    });

                    break;
                case R.id.edit:
                    Intent intent = new Intent(getActivity(), FarmerProfile.class);
                    intent.putExtra("farmer", famerModel);
                    startActivity(intent);

                    break;

                case R.id.view:

                    Intent intent1 = new Intent(getActivity(), FarmerProfile.class);
                    intent1.putExtra("farmer", famerModel);
                    startActivity(intent1);
                    // FragementFarmersList.this.editTrader(famerModel, FragementFarmersList.this.getUnits(), FragementFarmersList.this.getCycles(), FragementFarmersList.this.getRoutess(), false);

                    break;

                case R.id.Loans:
                    fragment = new FragmentGiveLoan();
                    Bundle args = new Bundle();
                    args.putSerializable("farmer", famerModel);
                    fragment.setArguments(args);
                    popOutFragments();
                    setUpView();
                    break;

                case R.id.Orders:

                    OrderConstants.setFamerModel(famerModel);
                    Intent intent2 = new Intent(getActivity(), GiveOrder.class);
                    intent2.putExtra("farmer", famerModel);
                    startActivity(intent2);

                    break;
                default:
            }
            return false;
        });
        popupMenu.show();
    }

    private List<RoutesModel> getRoutess() {
        return getRoutess;
    }

    private List<Cycles> getCycles() {
        return getCycles;
    }

    private List<UnitsModel> getUnits() {
        return getUnits;
    }

    public void editTrader(FamerModel famerModel, List<UnitsModel> unitsModels,
                           List<Cycles> cycles, List<RoutesModel> routesModels, boolean isEditale) {

        if (context != null) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
            View mView = layoutInflaterAndroid.inflate(R.layout.dialog_edit_farmer, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(context));
            alertDialogBuilderUserInput.setView(mView);

            UnitsModel unitsModel = new UnitsModel();
            RoutesModel routesModel = new RoutesModel();
            Cycles cyclesM = new Cycles();
            avi = mView.findViewById(R.id.avi);

            MaterialSpinner spinnerRoute, spinnerUnit;

            TextInputEditText edtNames, edtMobile;
            MaterialSpinner defaultPayment;
            TraderViewModel mViewModel;
            MaterialSpinner spinner;
            TextInputEditText edtRouteName, edtRouteCode, edtUnitName, edtUnitPrice, edtUnitMeasurement;
            LinearLayout lcycle;
            TextView txtStartDay, txtEndDay, txtCycle;

            txtStartDay = mView.findViewById(R.id.starts);
            txtEndDay = mView.findViewById(R.id.ends);
            txtCycle = mView.findViewById(R.id.txt_cycle);


            try {
                txtStartDay.setText(famerModel.getCycleStartDay());
                txtEndDay.setText(famerModel.getCycleStartEndDay());
                txtCycle.setText(famerModel.getCyclename());
            } catch (Exception nm) {
                nm.printStackTrace();
            }


            edtNames = mView.findViewById(R.id.edt_farmer_names);
            edtMobile = mView.findViewById(R.id.edt_farmer_phone);
            defaultPayment = mView.findViewById(R.id.spinnerPayments);

            spinnerUnit = mView.findViewById(R.id.spinnerUnit);
            spinnerRoute = mView.findViewById(R.id.spinnerRoute);
            edtUnitName = mView.findViewById(R.id.edt_unit_names);
            edtUnitPrice = mView.findViewById(R.id.edt_unit_price);
            edtUnitMeasurement = mView.findViewById(R.id.edt_unit_size);
            edtRouteName = mView.findViewById(R.id.edt_route_names);
            edtRouteCode = mView.findViewById(R.id.edt_route_code);
            spinner = mView.findViewById(R.id.spinnerCycle);

            if (!isEditale) {
                edtNames.setEnabled(false);
                edtMobile.setEnabled(false);
                //defaultPayment.setEnabled(false);

                spinnerUnit.setEnabled(false);
                spinnerUnit.setVisibility(View.GONE);
                spinnerRoute.setEnabled(false);
                spinnerRoute.setVisibility(View.GONE);
                edtUnitName.setEnabled(false);
                edtUnitPrice.setEnabled(false);
                edtUnitMeasurement.setEnabled(false);
                edtRouteName.setEnabled(false);
                edtRouteCode.setEnabled(false);
                spinner.setSelected(false);
                spinner.setVisibility(View.GONE);
                spinner.setEnabled(false);

            }


            edtNames.setText(famerModel.getNames());
            edtMobile.setText(famerModel.getMobile());
            edtUnitPrice.setText("" + famerModel.getUnitprice());
            edtUnitMeasurement.setText("" + famerModel.getUnitcapacity());
            edtRouteName.setText("" + famerModel.getRoutename());
            edtUnitName.setText("" + famerModel.getUnitname());
            edtRouteCode.setText("" + famerModel.getRoutecode());

//Units
            if (unitsModels != null && unitsModels.size() > 0) {
                prefrenceManager.setIsRoutesListFirst(false);
                String units[] = new String[unitsModels.size() + 1];

                units[0] = "Choose Unit ";
                for (int a = 0; a < unitsModels.size(); a++) {
                    units[a + 1] = unitsModels.get(a).getUnit();

                }
                spinnerUnit.setItems(units);

            }

//Routes
            if (routesModels != null && routesModels.size() > 0) {
                prefrenceManager.setIsRoutesListFirst(false);

                String routes[] = new String[routesModels.size() + 1];

                routes[0] = "Choose Route";
                for (int a = 0; a < routesModels.size(); a++) {
                    routes[a + 1] = routesModels.get(a).getRoute();

                }
                spinnerRoute.setItems(routes);
            }

//Cycles
            if (cycles != null && cycles.size() > 0) {
                new PrefrenceManager(getContext()).setIsCyclesListFirst(false);
                String units[] = new String[cycles.size()];

                // units[0]="Choose Unit ";
                for (int a = 0; a < cycles.size(); a++) {
                    units[a] = cycles.get(a).getCycle();

                }
                spinner.setItems(units);

            }


            alertDialogBuilderUserInput
                    .setCancelable(false);
//                    .setPositiveButton("Update", (dialogBox, id) -> {
//                        // ToDo get user input here
//
//
//                    })
//
//                    .setNegativeButton("Dismiss",
//                            (dialogBox, id) -> dialogBox.cancel());

            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.setCancelable(false);
            alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            alertDialogAndroid.show();
//
//            Button theButton = alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE);
//            theButton.setOnClickListener(new EditCustomListener(alertDialogAndroid, famerModel, unitsModel, routesModel, cyclesM));

            MaterialButton btnPositive, btnNegative, btnNeutral;
            TextView txtTitle;
            LinearLayout lTitle;
            ImageView imgIcon;
            btnPositive = mView.findViewById(R.id.btn_positive);
            btnNegative = mView.findViewById(R.id.btn_negative);
            btnNeutral = mView.findViewById(R.id.btn_neutral);
            txtTitle = mView.findViewById(R.id.txt_title);
            lTitle = mView.findViewById(R.id.linear_title);
            imgIcon = mView.findViewById(R.id.img_icon);


            btnNeutral.setVisibility(View.GONE);
            btnPositive.setVisibility(View.GONE);
            lTitle.setVisibility(View.GONE);
            txtTitle.setVisibility(View.VISIBLE);
            imgIcon.setVisibility(View.VISIBLE);
            imgIcon.setImageResource(R.drawable.ic_add_black_24dp);
            txtTitle.setText(famerModel.getNames() + " " + famerModel.getCode());

            btnPositive.setOnClickListener(new EditCustomListener(alertDialogAndroid, famerModel, unitsModel, routesModel, cyclesM));
            btnNeutral.setOnClickListener(view -> {

            });
            btnNegative.setOnClickListener(view -> alertDialogAndroid.dismiss());

        } else {
            Timber.d("context nulll edit clicked");

        }

    }

    private void initDataOffline(boolean isRoutesFirst, boolean isUnitFirst, boolean isCycles) {

        if (isRoutesFirst && isUnitFirst && isCycles) {
            avi.smoothToShow();
        }
        TraderViewModel mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);
        mViewModel.getRoutes(false).observe(this, routesModels -> {
            if (routesModels != null && routesModels.size() > 0) {
                prefrenceManager.setIsRoutesListFirst(false);
                this.getRoutess = routesModels;

                avi.smoothToHide();

            }

        });

        mViewModel.getUnits(isUnitFirst).observe(this, unitsModels -> {
            if (unitsModels != null && unitsModels.size() > 0) {
                prefrenceManager.setIsRoutesListFirst(false);
                FragementFarmersList.this.getUnits = unitsModels;


            }
        });
        mViewModel.getCycles(true).observe(this, cycles -> {
            if (cycles != null && cycles.size() > 0) {
                prefrenceManager.setIsCyclesListFirst(false);
                FragementFarmersList.this.getCycles = cycles;


            }
        });
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);


    }


    private class EditCustomListener implements View.OnClickListener {
        AlertDialog dialog;
        boolean isDummy = false;
        FamerModel farmer;
        UnitsModel unitsModel;
        RoutesModel routesModel;
        Cycles cycles;

        //int type;
        RequestDataCallback requestDataCallback;

        public EditCustomListener(AlertDialog alertDialogAndroid, FamerModel famerModel, UnitsModel u, RoutesModel r, Cycles c) {
            dialog = alertDialogAndroid;
            this.farmer = famerModel;
            this.unitsModel = u;
            this.routesModel = r;
            this.cycles = c;

        }

        @Override
        public void onClick(View v) {


        }
    }


}
