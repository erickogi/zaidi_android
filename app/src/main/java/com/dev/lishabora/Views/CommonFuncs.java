package com.dev.lishabora.Views;


import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.lishabora.AppConstants;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.DayCollectionModel;
import com.dev.lishabora.Models.DaysDates;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.FarmerBalance;
import com.dev.lishabora.Models.FarmerHistoryByDateModel;
import com.dev.lishabora.Models.LoanModel;
import com.dev.lishabora.Models.MilkModel;
import com.dev.lishabora.Models.MonthsDates;
import com.dev.lishabora.Models.Notifications;
import com.dev.lishabora.Models.OrderModel;
import com.dev.lishabora.Models.PayoutFarmersCollectionModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Models.Reports.ReportLineChartModel;
import com.dev.lishabora.Models.Reports.ReportListModel;
import com.dev.lishabora.Models.ResponseModel;
import com.dev.lishabora.Models.Trader.FarmerLoansTable;
import com.dev.lishabora.Models.Trader.FarmerOrdersTable;
import com.dev.lishabora.Models.UnitsModel;
import com.dev.lishabora.Repos.NotificationRepo;
import com.dev.lishabora.Repos.Trader.BalanceRepo;
import com.dev.lishabora.Repos.Trader.CollectionsRepo;
import com.dev.lishabora.Repos.Trader.LoanPaymentsRepo;
import com.dev.lishabora.Repos.Trader.LoansTableRepo;
import com.dev.lishabora.Repos.Trader.OrderPaymentsRepo;
import com.dev.lishabora.Repos.Trader.OrdersTableRepo;
import com.dev.lishabora.Utils.ApproveFarmerPayCardListener;
import com.dev.lishabora.Utils.CollectListener;
import com.dev.lishabora.Utils.CollectionCreateUpdateListener;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.Utils.InputFilterMinMax;
import com.dev.lishabora.Utils.LoanEditValueListener;
import com.dev.lishabora.Utils.MilkEditValueListener;
import com.dev.lishabora.ViewModels.Trader.BalncesViewModel;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.Trader.Activities.TraderActivity;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

public class CommonFuncs {
    public static void timeIs(Activity activity) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(activity));
        alertDialogBuilderUserInput.setTitle("Time Error");
        alertDialogBuilderUserInput.setMessage("Your time and Date settings is set to manual time settings.. For this app to run you need to enable automatic time settings");


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Okay", (dialogBox, id) -> {
                    // ToDo get user input here
                    // startActivity(new Intent(SplashActivity.this, SyncWorks.class));
                    activity.startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);

                });


        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        alertDialogAndroid.show();


    }

    public static void syncDue(Activity activity, int days) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(activity));
        alertDialogBuilderUserInput.setTitle("Sync Due");
        alertDialogBuilderUserInput.setMessage("You  have not synched for " + String.valueOf(days) + " days .. \n For this app to run you need to enable internet and sync your data");


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Okay", (dialogBox, id) -> {
                    // ToDo get user input here
                    // startActivity(new Intent(SplashActivity.this, SyncWorks.class));
                    // activity.startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                    activity.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                });


        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        alertDialogAndroid.show();


    }


    public static OrderModel getOrder(String date, String ampm, List<Collection> collections) {
        double orderTotal = 0.0;
        OrderModel orderModel = new OrderModel();
        if (collections != null) {
            for (Collection c : collections) {

                if (c.getDayDate().contains(date) && c.getTimeOfDay().equals(ampm)) {
                    try {
                        orderTotal += Double.valueOf(c.getOrderGivenOutPrice());
                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }
                    OrderModel orderModelm = new Gson().fromJson(c.getOrderDetails(), OrderModel.class);
                    if (orderModelm != null) {
                        orderModel = orderModelm;
                    }

                }
            }

            orderModel.setOrderAmount(String.valueOf(orderTotal));


        }
        return orderModel;

    }

    public static OrderModel getOrderForDay(String date, List<Collection> collections) {
        double orderTotal = 0.0;
        OrderModel orderModel = new OrderModel();
        if (collections != null) {
            for (Collection c : collections) {

                if (c.getDayDate().contains(date)) {
                    try {
                        orderTotal = Double.valueOf(c.getOrderGivenOutPrice());
                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }
                    OrderModel orderModelm = new Gson().fromJson(c.getOrderDetails(), OrderModel.class);
                    if (orderModelm != null) {
                        orderModel = orderModelm;
                    }

                }
            }

            orderModel.setOrderAmount(String.valueOf(orderTotal));


        }
        return orderModel;

    }

    public static LoanModel getLoan(String date, String ampm, List<Collection> collections) {
        double loanTotal = 0.0;
        LoanModel loanModel = new LoanModel();

        if (collections != null) {
            for (Collection c : collections) {
                if (c.getDayDate().contains(date) && c.getTimeOfDay().equals(ampm)) {
                    try {
                        loanTotal += Double.valueOf(c.getLoanAmountGivenOutPrice());

                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }
                    LoanModel loanModelm = new Gson().fromJson(c.getLoanDetails(), LoanModel.class);
                    if (loanModelm != null) {
                        loanModel = loanModelm;
                    }


                }

            }
            loanModel.setLoanAmount(String.valueOf(loanTotal));
        }
        return loanModel;


    }

    public static LoanModel getLoanForDay(String date, List<Collection> collections) {
        double loanTotal = 0.0;
        LoanModel loanModel = new LoanModel();

        if (collections != null) {
            for (Collection c : collections) {
                // Log.d("loanTotsl",""+c.getLoanAmountGivenOutPrice()+"  "+c.getFarmerName());

                if (c.getDayDate().contains(date)) {
                    try {
                        loanTotal = Double.valueOf(c.getLoanAmountGivenOutPrice());
                        Log.d("loanTotsl", "" + c.getLoanAmountGivenOutPrice() + "  " + c.getFarmerName());

                    } catch (Exception nm) {
                        //   Log.d("loanTotsl",""+loanTotal+"  "+nm.toString());
                        nm.printStackTrace();
                    }
                    LoanModel loanModelm = new Gson().fromJson(c.getLoanDetails(), LoanModel.class);
                    if (loanModelm != null) {
                        loanModel = loanModelm;
                    }


                }

            }
            loanModel.setLoanAmount(String.valueOf(loanTotal));
        }


        return loanModel;


    }

    public static LoanModel getLoan(List<Collection> collections) {
        double loanTotal = 0.0;
        LoanModel loanModel = new LoanModel();

        if (collections != null) {
            for (Collection c : collections) {
                try {
                    loanTotal += Double.valueOf(c.getLoanAmountGivenOutPrice());

                } catch (Exception nm) {
                    nm.printStackTrace();
                }
                LoanModel loanModelm = new Gson().fromJson(c.getLoanDetails(), LoanModel.class);
                if (loanModelm != null) {
                    loanModel = loanModelm;
                }


            }
            loanModel.setLoanAmount(String.valueOf(loanTotal));
        }
        return loanModel;


    }

    public static OrderModel getOrder(List<Collection> collections) {
        double orderTotal = 0.0;
        OrderModel orderModel = new OrderModel();

        if (collections != null) {
            for (Collection c : collections) {
                try {
                    orderTotal += Double.valueOf(c.getOrderGivenOutPrice());

                } catch (Exception nm) {
                    nm.printStackTrace();
                }
                OrderModel orderModel1 = new Gson().fromJson(c.getOrderDetails(), OrderModel.class);
                if (orderModel1 != null) {
                    orderModel = orderModel1;
                }


            }
            orderModel.setOrderAmount(String.valueOf(orderTotal));
        }
        return orderModel;


    }


    public static MilkModel getMilk(String date, String ampm, List<Collection> collections) {
        double milkTotal = 0.0;
        double milkTotalLtrs = 0.0;
        double milkTotalKsh = 0.0;
        MilkModel m = new MilkModel();

        if (collections != null) {
            for (Collection c : collections) {
                if (c.getDayDate().equals(date)) {
                    if (ampm == "AM") {
                        try {
                            milkTotal += Double.valueOf(c.getMilkCollectedAm());
                            milkTotalLtrs += Double.valueOf(c.getMilkCollectedValueLtrsAm());
                            milkTotalKsh += Double.valueOf(c.getMilkCollectedValueKshAm());
                            MilkModel mm = new Gson().fromJson(c.getMilkDetailsAm(), MilkModel.class);
                            if (mm != null) {
                                m = mm;
                            }
                        } catch (Exception nm) {
                            nm.printStackTrace();
                        }

                    } else {
                        try {
                            milkTotal += Double.valueOf(c.getMilkCollectedPm());
                            milkTotalLtrs += Double.valueOf(c.getMilkCollectedValueLtrsPm());
                            milkTotalKsh += Double.valueOf(c.getMilkCollectedValueKshPm());

                            MilkModel mm = new Gson().fromJson(c.getMilkDetailsPm(), MilkModel.class);
                            if (mm != null) {
                                m = mm;
                            }
                        } catch (Exception nm) {
                            nm.printStackTrace();
                        }

                    }


                }
            }
        }

        m.setUnitQty(String.valueOf(milkTotal));
        m.setValueLtrs(String.valueOf(milkTotalLtrs));
        m.setValueKsh(String.valueOf(milkTotalKsh));
        return m;


    }

    public static String getCollectionCode(String date, List<Collection> collections) {
        if (collections != null) {
            Timber.tag("collectisdid").d("Am Called  " + date
                    + "" + collections);

            for (Collection c : collections) {

                if (c.getDayDate().contains(date)) {

                    return "" + c.getCode();
                }
            }

        }
        return null;
    }

    private static UnitsModel u = new UnitsModel();

    @NonNull
    public static String getOrder(String farmercode, List<Collection> collections) {
        double orderTotal = 0.0;

        for (Collection c : collections) {
            if (c.getFarmerCode().equals(farmercode)) {
                try {
                    orderTotal += Double.valueOf(c.getOrderGivenOutPrice());
                } catch (Exception nm) {
                    nm.printStackTrace();
                }
            }
        }

        return String.valueOf(orderTotal);

    }

    @NonNull
    public static String getLoan(String farmercode, List<Collection> collections) {
        double loanTotal = 0.0;
        for (Collection c : collections) {
            if (c.getFarmerCode().equals(farmercode)) {
                try {
                    loanTotal += Double.valueOf(c.getLoanAmountGivenOutPrice());
                } catch (Exception nm) {
                    nm.printStackTrace();
                }
            }
        }
        return String.valueOf(loanTotal);


    }

    private static PeriodFormatter mPeriodFormat;

    @NonNull
    public static String getBalance(String milkTotal, String loanTotal, String orderTotal) {
        Log.d("getBalance", " MIlk " + milkTotal + " LOan " + loanTotal + "   Order " + orderTotal);
        double balance = 0.0;
        try {
            balance = Double.valueOf(milkTotal);
        } catch (Exception nm) {
            nm.printStackTrace();
        }
        try {
            return String.valueOf(Double.valueOf(milkTotal) - (Double.valueOf(loanTotal) + Double.valueOf(orderTotal)));

        } catch (Exception nm) {
            nm.printStackTrace();
        }
        return String.valueOf(balance);
    }

    @Nullable
    public static Collection updateCollection(String s, int time, int type, DayCollectionModel dayCollectionModel, Collection collectionExisting, Payouts payouts, FamerModel famerModel, LoanModel loanModel, OrderModel orderModel) {


        UnitsModel unitsModel = new UnitsModel();
        unitsModel.setCode(famerModel.getUnitcode());
        unitsModel.setUnitprice(famerModel.getUnitprice());
        unitsModel.setUnitcapacity(famerModel.getUnitcapacity());
        unitsModel.setUnit(famerModel.getUnitname());

        Collection collection = new Collection();

        String timeOfDay = "";
        if (time == 1) {
            timeOfDay = "AM";
        } else {
            timeOfDay = "PM";
        }


        if (dayCollectionModel.getCollectionCode() != null) {
            collection = collectionExisting;


        } else {
            Collection c = new Collection();
            c.setCode(GeneralUtills.Companion.createCode(famerModel.getCode()));

            c.setCycleCode(payouts.getCycleCode());
            c.setFarmerCode(famerModel.getCode());
            c.setFarmerName(famerModel.getNames());
            c.setCycleId(payouts.getCycleCode());
            c.setDayName(dayCollectionModel.getDay());
            c.setDayDate(dayCollectionModel.getDate());
            c.setTimeOfDay(timeOfDay);

            c.setMilkCollectedAm(dayCollectionModel.getMilkAm());
            c.setMilkCollectedPm(dayCollectionModel.getMilkPm());


            c.setLoanAmountGivenOutPrice(dayCollectionModel.getLoan());
            c.setOrderGivenOutPrice(dayCollectionModel.getOrder());


            c.setMilkCollectedValueLtrsAm(dayCollectionModel.getMilkModelAm().getValueLtrs());
            c.setMilkCollectedValueKshAm(dayCollectionModel.getMilkModelAm().getValueKsh());
            //c.setMilkCollectedPriceAm(dayCollectionModel.getMilkModelAm().getValueKsh());
            c.setMilkDetailsAm(new Gson().toJson(dayCollectionModel.getMilkModelAm()));


            c.setMilkCollectedValueLtrsPm(dayCollectionModel.getMilkModelPm().getValueLtrs());
            c.setMilkCollectedValueKshPm(dayCollectionModel.getMilkModelPm().getValueKsh());
            //c.setMilkCollectedPricepm(dayCollectionModel.getMilkModelPm().getValueKsh());
            c.setMilkDetailsPm(new Gson().toJson(dayCollectionModel.getMilkModelPm()));


            c.setLoanDetails(new Gson().toJson(dayCollectionModel.getLoanModel()));
            c.setOrderDetails(new Gson().toJson(dayCollectionModel.getOrderModel()));


            c.setLoanId("");
            c.setOrderId("");
            c.setSynced(0);
            c.setSynced(false);
            c.setApproved(0);

            c.setPayoutCode(dayCollectionModel.getPayoutCode());
            c.setPayoutCode(payouts.getCode());

            c.setCycleStartedOn(payouts.getStartDate());

            if (type == 1) {

                MilkModel milkModel;
                if (time == 1) {
                    milkModel = dayCollectionModel.getMilkModelAm();
                } else {
                    milkModel = dayCollectionModel.getMilkModelPm();
                }

                milkModel.setUnitsModel(unitsModel);
                milkModel.setUnitQty(s);


                if (time == 1) {

                    c.setMilkCollectedAm(s);
                    c.setMilkCollectedValueKshAm(milkModel.getValueKsh());
                    c.setMilkCollectedValueLtrsAm(milkModel.getValueLtrs());
                    c.setMilkDetailsAm(new Gson().toJson(milkModel));

                } else {


                    c.setMilkCollectedPm(s);
                    c.setMilkCollectedValueKshPm(milkModel.getValueKsh());
                    c.setMilkCollectedValueLtrsPm(milkModel.getValueLtrs());
                    c.setMilkDetailsPm(new Gson().toJson(milkModel));

                }

                Log.d("collectionUponUpdateNew", new Gson().toJson(c));


            } else if (type == 2) {

                c.setLoanAmountGivenOutPrice(s);
                c.setLoanDetails(new Gson().toJson(loanModel));


            } else if (type == 3) {

                c.setOrderGivenOutPrice(s);
                c.setOrderDetails(new Gson().toJson(orderModel));

            }


            return c;


        }


        if (collection != null) {
            if (type == 1) {
                MilkModel milkModel;
                if (time == 1) {
                    milkModel = dayCollectionModel.getMilkModelAm();
                } else {
                    milkModel = dayCollectionModel.getMilkModelPm();
                }
                milkModel.setUnitsModel(unitsModel);
                milkModel.setUnitQty(s);


                if (time == 1) {
                    collection.setMilkCollectedAm(s);
                    collection.setMilkCollectedValueKshAm(milkModel.getValueKsh());
                    collection.setMilkCollectedValueLtrsAm(milkModel.getValueLtrs());
                    collection.setMilkDetailsAm(new Gson().toJson(milkModel));

                } else {
                    collection.setMilkCollectedPm(s);
                    collection.setMilkCollectedValueKshPm(milkModel.getValueKsh());
                    collection.setMilkCollectedValueLtrsPm(milkModel.getValueLtrs());
                    collection.setMilkDetailsPm(new Gson().toJson(milkModel));
                }
                Log.d("collectionUponUpdate", new Gson().toJson(collection));

                return collection;
            } else if (type == 2) {
                collection.setLoanAmountGivenOutPrice(s);
                collection.setLoanDetails(new Gson().toJson(loanModel));

                return collection;
            } else if (type == 3) {


                collection.setOrderGivenOutPrice(s);
                collection.setOrderDetails(new Gson().toJson(orderModel));


                return collection;

            }
        } else {

        }
        return null;
    }


    public static LinkedList<FarmerHistoryByDateModel> createHistoryList(List<Collection> collections,
                                                                         MonthsDates monthsDatesa,
                                                                         boolean isForWholeYearByMonth) {

        if (isForWholeYearByMonth) {
            List<MonthsDates> monthsDates = DateTimeUtils.Companion.getMonths(12);
            if (monthsDates.size() > 0) {

                LinkedList<FarmerHistoryByDateModel> fmh = new LinkedList<>();

                for (MonthsDates mds : monthsDates) {

                    String[] totals = getCollectionsTotalsAll(mds, collections);
                    fmh.add(new FarmerHistoryByDateModel(mds.getMonthName(), totals[0], totals[1], totals[2], totals[3], totals[4]));

                }
                return fmh;

            }
        } else if (monthsDatesa != null) {
            LinkedList<FarmerHistoryByDateModel> fmh = new LinkedList<>();

            for (Collection c : collections) {
                if (DateTimeUtils.Companion.isInMonth(c.getDayDate(), monthsDatesa.getMonthName())) {


                    fmh.add(new FarmerHistoryByDateModel(DateTimeUtils.Companion.getDisplayDate(c.getDayDate(), DateTimeUtils.Companion.getDisplayDatePattern1()),
                            getTotal(c.getMilkCollectedValueLtrsAm(), c.getMilkCollectedValueLtrsPm()),
                            c.getLoanAmountGivenOutPrice(),
                            c.getOrderGivenOutPrice(),
                            c.getMilkCollectedValueLtrsAm(),
                            c.getMilkCollectedValueLtrsPm()));


                }
            }

            return fmh;

        } else {
            LinkedList<FarmerHistoryByDateModel> fmh = new LinkedList<>();

            for (Collection c : collections) {

                fmh.add(new FarmerHistoryByDateModel(DateTimeUtils.Companion.getDisplayDate(c.getDayDate(), DateTimeUtils.Companion.getDisplayDatePattern1()),
                        getTotal(c.getMilkCollectedValueLtrsAm(), c.getMilkCollectedValueLtrsPm()),
                        c.getLoanAmountGivenOutPrice(), c.getOrderGivenOutPrice(), c.getMilkCollectedValueLtrsAm(), c.getMilkCollectedValueLtrsPm()));


            }
            return fmh;

        }
        return null;


    }

    public static String[] getCollectionsTotalsAll(MonthsDates mds, List<Collection> collections) {
        String cycleCode = "";
        double milk = 0.0;
        double loan = 0.0;
        double order = 0.0;
        double milkAm = 0.0;
        double milkPm = 0.0;


        for (Collection collection : collections) {
            if (DateTimeUtils.Companion.isInMonth(collection.getDayDate(), mds.getMonthName())) {
                Log.d("eroordebug", "Coll" + new Gson().toJson(collection));
                if (collection.getMilkCollectedValueLtrsAm() != null) {
                    milk = milk + (Double.valueOf(collection.getMilkCollectedValueLtrsAm()) + Double.valueOf(collection.getMilkCollectedValueLtrsPm()));
                }
                if (collection.getLoanAmountGivenOutPrice() != null) {
                    loan = loan + Double.valueOf(collection.getLoanAmountGivenOutPrice());
                }
                if (collection.getOrderGivenOutPrice() != null) {
                    order = order + Double.valueOf(collection.getOrderGivenOutPrice());
                }

                if (collection.getMilkCollectedValueLtrsAm() != null) {
                    milkAm = milkAm + Double.valueOf(collection.getMilkCollectedValueLtrsAm());
                }
                if (collection.getMilkCollectedValueLtrsPm() != null) {
                    milkPm = milkPm + Double.valueOf(collection.getMilkCollectedValueLtrsPm());
                }

            }

        }
        double[] totals = {milk, loan, order, milkAm, milkPm};


        return new String[]{String.valueOf(totals[0]), String.valueOf(totals[1]), String.valueOf(totals[2]), String.valueOf(totals[3]), String.valueOf(totals[4])};
    }

    public static String[] getCollectionsTotals(MonthsDates mds, List<Collection> collections) {
        String cycleCode = "";
        double milk = 0.0;
        double loan = 0.0;
        double order = 0.0;
        double milkAm = 0.0;
        double milkPm = 0.0;


        for (Collection collection : collections) {
            if (DateTimeUtils.Companion.isInMonth(collection.getDayDate(), mds.getMonthName())) {
                Log.d("eroordebug", "Coll" + new Gson().toJson(collection));
                if (collection.getMilkCollectedValueLtrsAm() != null) {
                    milk = milk + (Double.valueOf(collection.getMilkCollectedValueLtrsAm()) + Double.valueOf(collection.getMilkCollectedValueLtrsPm()));
                }
                if (collection.getLoanAmountGivenOutPrice() != null) {
                    loan = loan + Double.valueOf(collection.getLoanAmountGivenOutPrice());
                }
                if (collection.getOrderGivenOutPrice() != null) {
                    order = order + Double.valueOf(collection.getOrderGivenOutPrice());
                }

                if (collection.getMilkCollectedValueLtrsAm() != null) {
                    milkAm = milkAm + Double.valueOf(collection.getMilkCollectedValueLtrsAm());
                }
                if (collection.getMilkCollectedValueLtrsPm() != null) {
                    milkPm = milkPm + Double.valueOf(collection.getMilkCollectedValueLtrsPm());
                }

            }

        }
        double[] totals = {milk, loan, order, milkAm, milkPm};


        return new String[]{String.valueOf(totals[0]), String.valueOf(totals[1]), String.valueOf(totals[2]), cycleCode};
    }


    public static int getFarmerStatus(String code, List<Collection> collections) {
        int status = 0;
        int collectsNo = 0;
        for (Collection c : collections) {
            if (c.getFarmerCode().equals(code)) {
                collectsNo++;
                try {
                    status += c.getApproved();
                } catch (Exception nm) {
                    nm.printStackTrace();
                }
            }

        }
        if (status >= collectsNo && status != 0) {
            return 1;
        } else {
            return 0;
        }
    }

    private static int[] getApprovedCards(List<Collection> collections, String pcode, PayoutsVewModel payoutsVewModel) {

        int[] statusR = new int[3];
        int farmerStatus = 0;


        List<FamerModel> f = payoutsVewModel.getFarmersByCycleONe(pcode);


        statusR[0] = f.size();


        int approved = 0;

        for (FamerModel famerModel : f) {
            int status = 0;
            int collectionNo = 0;
            for (Collection c : collections) {


                if (c.getFarmerCode().equals(famerModel.getCode())) {


                    collectionNo = collectionNo + 1;

                    try {
                        status += c.getApproved();

                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }
                }


            }

            if (status == collectionNo && status != 0) {
                approved += 1;
            }


        }
        statusR[1] = approved;
        statusR[2] = statusR[0] - approved;


        return statusR;


    }

    private static UnitsModel comUpWithUnitModel(Object o, FamerModel famerModel) {
        UnitsModel u = new UnitsModel();
        MilkModel m = (MilkModel) o;
        if (m != null) {
            if (m.getUnitsModel() != null) {
                u = m.getUnitsModel();
            } else {
                u.setUnit(famerModel.getUnitname());
                u.setUnitcapacity(famerModel.getUnitcapacity());
                u.setUnitprice(famerModel.getUnitprice());
            }

        }
        return u;

    }

    public static List<DayCollectionModel> setUpDayCollectionsModel(Payouts payouts, List<Collection> collections) {


        List<DaysDates> daysDates = DateTimeUtils.Companion.getDaysAndDatesBtnDates(payouts.getStartDate(), payouts.getEndDate());

        List<DayCollectionModel> dayCollectionModels = new LinkedList<>();
        for (DaysDates d : daysDates) {


            MilkModel milkModelAm = CommonFuncs.getMilk(d.getDate(), "AM", collections);
            MilkModel milkModelPm = CommonFuncs.getMilk(d.getDate(), "PM", collections);


            String milkAm = milkModelAm.getUnitQty();
            String milkPm = milkModelPm.getUnitQty();

            LoanModel loanModel = CommonFuncs.getLoanForDay(d.getDate(), collections);
            Log.d("loanTotsl", "" + new Gson().toJson(loanModel));

            String loan = loanModel.getLoanAmount();


            OrderModel orderModel = CommonFuncs.getOrderForDay(d.getDate(), collections);
            String order = orderModel.getOrderAmount();


            String collectionCode = getCollectionCode(d.getDate(), collections);

            dayCollectionModels.add(new DayCollectionModel(
                            payouts.getCode(),
                            d.getDay(),
                            d.getDate(),
                            milkAm,
                            milkPm,
                            collectionCode,
                            milkModelAm,
                            milkModelPm, loan, order,
                            loanModel.getCollectionCode(),
                            loanModel, orderModel.getCollectionCode(), orderModel, payouts.getStatus(), milkModelAm.getValueLtrs(),
                            milkModelAm.getValueKsh(),
                            milkModelPm.getValueLtrs(),
                            milkModelPm.getValueKsh()

                    )

            );
        }

        return dayCollectionModels;

    }

    public static ValueObject getValueObjectToEditFromDayCollection(DayCollectionModel dayCollectionModel, int time, int type) {
        String value = "";
        Object o = null;

        if (dayCollectionModel != null) {
            switch (type) {
                case 1:
                    if (time == 1) {
                        o = dayCollectionModel.getMilkModelAm();
                        value = dayCollectionModel.getMilkModelAm().getUnitQty();
                    } else {
                        o = dayCollectionModel.getMilkModelPm();
                        value = dayCollectionModel.getMilkModelPm().getUnitQty();
                    }

                    break;
                case 2:

                    o = dayCollectionModel.getLoanModel();
                    value = dayCollectionModel.getLoan();


                    break;
                case 3:
                    o = dayCollectionModel.getOrderModel();
                    value = dayCollectionModel.getOrder();

                    break;

                default:
                    value = "";
                    o = new Object();

            }
        }

        return new ValueObject(value, o);


    }

    private static Date previousdate;

    @NonNull
    public static MilkModel getMilk(String farmercode, List<Collection> collections) {
        double milkTotalQty = 0.0;
        double milkTotalLtrs = 0.0;
        double milkTotalKsh = 0.0;
        MilkModel milkModel = new MilkModel();


        for (Collection c : collections) {

            if (c.getFarmerCode().equals(farmercode)) {
                try {
                    milkTotalQty += Double.valueOf(c.getMilkCollectedAm()) + Double.valueOf(c.getMilkCollectedPm());
                    milkTotalLtrs += Double.valueOf(c.getMilkCollectedValueLtrsAm()) + Double.valueOf(c.getMilkCollectedValueLtrsPm());
                    milkTotalKsh += Double.valueOf(c.getMilkCollectedValueKshAm()) + Double.valueOf(c.getMilkCollectedValueKshPm());


                } catch (Exception nm) {
                    nm.printStackTrace();
                }
            }


        }
        milkModel.setUnitQty(String.valueOf(milkTotalQty));
        milkModel.setValueKsh(String.valueOf(milkTotalKsh));
        milkModel.setValueLtrs(String.valueOf(milkTotalLtrs));


        return milkModel;


    }

    private static void log(String msg) {

        mPeriodFormat = new PeriodFormatterBuilder().appendYears()
                .appendMinutes().appendSuffix(" Mins  ")
                .appendSeconds().appendSuffix(" Secs  ")
                .appendMillis().appendSuffix("Mil   ")
                .toFormatter();
        if (previousdate == null) {
            previousdate = DateTimeUtils.Companion.getDateNow();
        }

        Period length = DateTimeUtils.Companion.calcDiff(previousdate, new Date());

        previousdate = new Date();
        Timber.tag("debugComonfun").d("  Length " + mPeriodFormat.print(length) + "" + msg);

    }

    public static PayoutFarmersCollectionModel getFarmersCollectionModel(FamerModel famerModel, List<Collection> collections, Payouts payouts) {

        MilkModel m;

        m = CommonFuncs.getMilk(famerModel.getCode(), collections);
        String milkTotal = m.getUnitQty();
        String milkTotalKsh = m.getValueKsh();
        String milkTotalLtrs = m.getValueLtrs();


        String loanTotal = CommonFuncs.getLoan(famerModel.getCode(), collections);


        String orderTotal = CommonFuncs.getOrder(famerModel.getCode(), collections);


        int cardstatus = getFarmerStatus(famerModel.getCode(), collections);
        String statusText;


        statusText = cardstatus == 0 ? "Pending" : "Approved";


        String balance = getBalance(milkTotalKsh, loanTotal, orderTotal);
        return new PayoutFarmersCollectionModel(
                famerModel.getCode(),
                famerModel.getNames(),
                milkTotal,
                loanTotal,
                orderTotal,
                cardstatus,
                payouts.getStatus(),
                statusText,
                balance,
                payouts.getCode(),
                famerModel.getCyclecode(),
                milkTotalKsh, milkTotalLtrs, payouts.getStartDate(), payouts.getEndDate()
        );

    }

    public static PayoutFarmersCollectionModel getFarmersCollectionModel(FamerModel famerModel, Payouts p) {
        return new PayoutFarmersCollectionModel(
                famerModel.getCode(),
                famerModel.getNames(),
                p.getMilkTotal(),
                p.getLoanTotal(),
                p.getOrderTotal(),
                p.getStatus(),
                p.getStatus(),
                p.getStatusName(),
                p.getBalance(), p.getCode(), famerModel.getCyclecode(),
                p.getMilkTotalKsh(), p.getMilkTotalLtrs(), p.getStartDate(), p.getEndDate()
        );
    }

    final static double MAX_ALLOWED_AMOUNT = 9999.99;
    final static int MAX_ALLOWED_DECIMALS = 1;

    private static int getNumDecimals(String num) {
        if (!hasComma(num)) {
            return 0;
        }
        return num.substring(num.indexOf('.') + 1, num.length()).length();
    }

    private static boolean hasComma(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '.') {
                return true;
            }
        }
        return false;
    }

    private static void updateAmount(String newAmountText) {
//        double newAmount = newAmountText.isEmpty() ? 0.0 : Double.parseDouble(newAmountText.replaceAll(",", "."));
//        if (newAmount >= 0.0 && newAmount <= MAX_ALLOWED_AMOUNT
//                && getNumDecimals(newAmountText) <= MAX_ALLOWED_DECIMALS) {
//            amountText = newAmountText;
//            amount = newAmount;
//            showAmount(amountText);
//        }
    }
    public static void editValueMilk(boolean isEditable, int adapterPosition, int time, int type, String value, Object o, DayCollectionModel dayCollectionModel, Context context, AVLoadingIndicatorView avi, FamerModel famerModel, MilkEditValueListener listener) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_edit_collection, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(context));
        alertDialogBuilderUserInput.setView(mView);

        avi = mView.findViewById(R.id.avi);
        LinearLayout milkUnits = mView.findViewById(R.id.milk_units);
        TextInputEditText edtVL = mView.findViewById(R.id.edt_value);
        TextView txt = mView.findViewById(R.id.txt_desc);
        TextView unitName, unitPrice, unitTotal;


        unitName = mView.findViewById(R.id.txtUnitName);
        unitPrice = mView.findViewById(R.id.txtUnitPrice);
        unitTotal = mView.findViewById(R.id.txtCost);


        String ti = "";
        if (time == 1) {
            ti = " AM";
        } else if (time == 2) {
            ti = " PM";
        } else {
            ti = "Unknown";
        }


        String tp = "";
        u = comUpWithUnitModel(o, famerModel);
        milkUnits.setVisibility(View.VISIBLE);
        unitName.setText(u.getUnit());
        unitPrice.setText(u.getUnitprice());
        edtVL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && editable.length() > 0) {
                    if (u.getUnitprice() != null) {

                        try {
                            Double price = Double.valueOf(u.getUnitprice());
                            Double unitCapacity = Double.valueOf(u.getUnitcapacity()) / 1000;
                            Double total = (Double.valueOf(edtVL.getText().toString())) * price;
                            unitTotal.setText(String.valueOf(GeneralUtills.Companion.round(total, 2)));

                        } catch (Exception nm) {
                            nm.printStackTrace();
                        }
                    }
                } else {
                    unitTotal.setText("");
                }
            }
        });
        tp = " Milk collection";


        txt.setText(" Editing " + tp + "  For  " + DateTimeUtils.Companion.getDisplayDate(dayCollectionModel.getDate(), DateTimeUtils.Companion.getDisplayDatePattern1()) + "  " + ti);


        try {
            if (!value.equals("0.0")) {
                edtVL.setText(value);
                edtVL.setSelection(value.length());
            }
        } catch (Exception nm) {
            nm.printStackTrace();
        }


        //   edtVL.setFilters(new InputFilter[]{new InputFilterMinMax(1, 1000)});

        edtVL.setFilters(new InputFilter[]{
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 5, afterDecimal = 2;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = edtVL.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.";
                        } else if (temp.indexOf(".") == -1) {
                            // no decimal point placed yet
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        } else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if (temp.length() > afterDecimal) {
                                return "";
                            }
                        }

                        return super.filter(source, start, end, dest, dstart, dend);
                    }
                }
        });
        alertDialogBuilderUserInput
                .setCancelable(false);
        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        Objects.requireNonNull(alertDialogAndroid.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialogAndroid.show();


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

//        btnPositive.setBackgroundColor(Application.context.getResources().getColor(R.color.colorPrimary));
//        btnNegative.setBackgroundColor(Application.context.getResources().getColor(R.color.colorPrimary));
//
//        btnNegative.setTextColor(Application.context.getResources().getColor(R.color.white));
//        btnPositive.setTextColor(Application.context.getResources().getColor(R.color.white));
//

        btnNeutral.setVisibility(View.GONE);
        lTitle.setVisibility(View.GONE);
        txtTitle.setVisibility(View.VISIBLE);
        imgIcon.setVisibility(View.VISIBLE);
        imgIcon.setImageResource(R.drawable.ic_add_black_24dp);
        txtTitle.setText("Route");

        if (!isEditable) {
            edtVL.setEnabled(false);
            btnPositive.setVisibility(View.GONE);
            // ed.setEnabled(false);
        }


        btnPositive.setOnClickListener(view -> {
            if (TextUtils.isEmpty(Objects.requireNonNull(edtVL.getText()).toString())) {
                listener.updateCollection("0", adapterPosition, time, type, dayCollectionModel, alertDialogAndroid);
            }

            listener.updateCollection(edtVL.getText().toString(), adapterPosition, time, type, dayCollectionModel, alertDialogAndroid);

        });
        btnNeutral.setOnClickListener(view -> {

        });
        btnNegative.setOnClickListener(view -> alertDialogAndroid.dismiss());

    }

    public static void editValueLoan(boolean isEditable, DayCollectionModel dayCollectionModel, Context context, FamerModel famerModel, LoanEditValueListener listener) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_edit_loan, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(context));
        alertDialogBuilderUserInput.setView(mView);

        TextView status, id, name, balance, milk, loan, order;
        ImageView imgAdd, imgRemove, imgDelete;
        TextView txtQty, txtPrice;
        TextInputEditText edtAmount;


        id = mView.findViewById(R.id.txt_id);
        name = mView.findViewById(R.id.txt_name);

        edtAmount = mView.findViewById(R.id.edt_value);
        txtQty = mView.findViewById(R.id.txt_qty);


        txtPrice = mView.findViewById(R.id.txt_installment);

        imgAdd = mView.findViewById(R.id.img_add);
        imgRemove = mView.findViewById(R.id.img_remove);

        imgAdd.setOnClickListener(view -> calc(imgAdd, txtQty, edtAmount, txtPrice));
        imgRemove.setOnClickListener(view -> calc(imgRemove, txtQty, edtAmount, txtPrice));


        edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                double installmentValue = 0.0;

                if (editable != null) {

                    if (edtAmount.getText().toString() != null && !TextUtils.isEmpty(edtAmount.getText().toString())) {
                        double value = Double.valueOf(edtAmount.getText().toString());
                        int insNo = Integer.valueOf(txtQty.getText().toString());
                        if (value > 0.0) {
                            installmentValue = (value / insNo);
                        }
                    }
                }
                txtPrice.setText(String.valueOf(GeneralUtills.Companion.round(installmentValue, 2)));
            }
        });

        if (dayCollectionModel != null) {

            if (dayCollectionModel.getLoanModel() != null) {
                if (dayCollectionModel.getLoanModel().getInstallmentsNo() != null) {
                    txtQty.setText(dayCollectionModel.getLoanModel().getInstallmentsNo());
                }
            }
        }


        if (dayCollectionModel.getLoan() != null && !dayCollectionModel.getLoan().equals("0.0")) {
            edtAmount.setText(dayCollectionModel.getLoan());
        } else {
            edtAmount.setText("");
        }
        edtAmount.setFilters(new InputFilter[]{new InputFilterMinMax(1, 10000)});


        id.setText(famerModel.getCode());
        name.setText(famerModel.getNames());


        alertDialogBuilderUserInput.setCancelable(false);
        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        Objects.requireNonNull(alertDialogAndroid.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialogAndroid.show();


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
        lTitle.setVisibility(View.GONE);
        txtTitle.setVisibility(View.VISIBLE);
        imgIcon.setVisibility(View.VISIBLE);
        imgIcon.setImageResource(R.drawable.ic_add_black_24dp);
        txtTitle.setText("Loan");

        if (!isEditable) {
            edtAmount.setEnabled(false);
            imgAdd.setClickable(false);
            imgRemove.setClickable(false);
            imgAdd.setEnabled(false);
            imgRemove.setEnabled(false);
            btnPositive.setVisibility(View.GONE);
        }

        btnPositive.setOnClickListener(view -> {
            String value = "0";
            if (!TextUtils.isEmpty(edtAmount.getText().toString()) && !edtAmount.getText().toString().equals("0.0")) {
                value = edtAmount.getText().toString();

            }

            LoanModel loanModel = new LoanModel();
            loanModel.setLoanAmount(value);
            loanModel.setInstallmentAmount(txtPrice.getText().toString());
            loanModel.setInstallmentsNo(txtQty.getText().toString());//giveLoan(edtAmount.getText().toString(), new Gson().toJson(loanModel));


            listener.updateCollection(edtAmount.getText().toString(), loanModel, 0, dayCollectionModel, alertDialogAndroid);

        });
        btnNeutral.setOnClickListener(view -> {

        });
        btnNegative.setOnClickListener(view -> alertDialogAndroid.dismiss());


    }


    public static void updateCollectionValue(String s, int time, int type,
                                             DayCollectionModel dayCollectionModel,
                                             PayoutsVewModel payoutsVewModel,
                                             Payouts payouts,
                                             FamerModel famerModel,
                                             LoanModel loanModel,
                                             OrderModel orderModel,
                                             CollectionCreateUpdateListener listener) {


        Collection collection;
        Collection ctoUpdate;


        if (dayCollectionModel.getCollectionCode() != null) {
            collection = payoutsVewModel.getCollectionByCodeOne(dayCollectionModel.getCollectionCode());
            ctoUpdate = CommonFuncs.updateCollection(s, time, type, dayCollectionModel, collection, payouts, famerModel, loanModel, orderModel);
        } else {
            Collection c;
            c = CommonFuncs.updateCollection(s, time, type, dayCollectionModel, null, payouts, famerModel, loanModel, orderModel);
            listener.createCollection(c);
            return;
        }


        if (collection != null) {
            listener.updateCollection(ctoUpdate);

        } else {
            listener.error("Our coll is null");
        }
    }

    private static void calc(View imgAction, View txtQty, TextInputEditText edtAmount, TextView txtPrice) {
        String gty = ((TextView) txtQty).getText().toString();

        if (imgAction.getId() == R.id.img_add) {
            int vq = Integer.valueOf(gty) + 1;
            if (vq <= 10) {
                ((TextView) txtQty).setText(String.valueOf(vq));
            }
            // ((TextView) txtQty).setText(String.valueOf(vq));

        } else {
            int vq = Integer.valueOf(gty);
            if (vq != 1) {
                ((TextView) txtQty).setText(String.valueOf(vq - 1));
            }
        }


        double installmentValue = 0.0;

        if (edtAmount.getText() != null && !TextUtils.isEmpty(edtAmount.getText())) {

            double value = Double.valueOf(edtAmount.getText().toString());
            int insNo = Integer.valueOf(((TextView) txtQty).getText().toString());
            if (value > 0.0) {
                installmentValue = (value / insNo);
            }
        }

        txtPrice.setText(String.valueOf(GeneralUtills.Companion.round(installmentValue, 2)));


    }

    public static void setCardActionStatus(PayoutFarmersCollectionModel model, Context context,
                                           MaterialButton btnApprove, MaterialButton btnBack, TextView txtApprovalStatus) {
        if (model.getCardstatus() == 0 // Card not approved
                && (DateTimeUtils.Companion.getToday().equals(model.getPayoutEnd())  //TODAY IS  THIS PAYOUT END DATE
                || DateTimeUtils.Companion.isPastLastDay(model.getPayoutEnd())   // TODAY IS PAST THIS PAYOUT END DATE
        )) {

            /****** HERE THE PAYOUT IS PENDING , THIS CARD IS PENDING APPROVAL  ******/
            btnApprove.setVisibility(View.VISIBLE);
            txtApprovalStatus.setVisibility(View.GONE);
            btnBack.setVisibility(View.GONE);


        } else if (model.getCardstatus() == 1) {

            /****THIS CARD HAS BEEN APPROVED  ****/


            txtApprovalStatus.setText("Approved");
            txtApprovalStatus.setVisibility(View.VISIBLE);
            txtApprovalStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));


            if (model.getPayoutStatus() == 0) {
                /*******THIS CARD IS APPROVED BUT WHOLE PAYOUT IS PENDING SO ONE CAN STILL CANCEL CARDS APPROVAL ******/

                btnApprove.setVisibility(View.GONE);
                btnBack.setVisibility(View.VISIBLE);
                btnBack.setText("Cancel Approval");
                txtApprovalStatus.setVisibility(View.VISIBLE);

            } else {

                /*******THIS CARD IS APPROVED AND WHOLE PAYOUT IS APPROVED SO ONE CANNOT  CANCEL CARDS APPROVAL ******/

                btnBack.setVisibility(View.GONE);
                btnApprove.setVisibility(View.GONE);
                txtApprovalStatus.setText("Approved");
                txtApprovalStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));

                txtApprovalStatus.setVisibility(View.VISIBLE);
            }
        } else if (model.getCardstatus() == 0 && (!DateTimeUtils.Companion.getToday().equals(model.getPayoutEnd())
                || !DateTimeUtils.Companion.isPastLastDay(model.getPayoutEnd()))) {


            txtApprovalStatus.setText("Pending");
            txtApprovalStatus.setTextColor(context.getResources().getColor(R.color.red));
            txtApprovalStatus.setVisibility(View.VISIBLE);
            btnBack.setVisibility(View.GONE);
            btnApprove.setVisibility(View.GONE);


        }

    }

    public static void setPayoutActionStatus(Payouts payouts, Context context, MaterialButton btnApprove, TextView txtApprovalStatus) {

        if (payouts.getStatus() == 1) {
            btnApprove.setVisibility(View.GONE);
            txtApprovalStatus.setText("Approved");
            txtApprovalStatus.setVisibility(View.VISIBLE);
            txtApprovalStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));


        } else {
            if (DateTimeUtils.Companion.getToday().equals(payouts.getEndDate()) ||
                    DateTimeUtils.Companion.isPastLastDay(payouts.getEndDate())) {

                btnApprove.setVisibility(View.VISIBLE);
                btnApprove.setText("Approve Payout");
                txtApprovalStatus.setVisibility(View.GONE);


            } else {
                txtApprovalStatus.setText("Pending");
                txtApprovalStatus.setTextColor(context.getResources().getColor(R.color.red));
                txtApprovalStatus.setVisibility(View.VISIBLE);
                btnApprove.setVisibility(View.GONE);
            }


        }


    }


    public static List<ReportListModel> generateReportListModel(List<Collection> collections, int type) {
        List<ReportListModel> reportListModels = new LinkedList<>();
        for (Collection c : collections) {

            if (type == 1) {
                reportListModels.add(new ReportListModel(c.getCode(), c.getDayName(), c.getDayDate(), c.getMilkCollectedValueLtrsAm(), c.getMilkCollectedValueLtrsPm()));
            } else if (type == 2) {
                reportListModels.add(new ReportListModel(c.getCode(), c.getDayName(), c.getDayDate(), null, c.getLoanAmountGivenOutPrice()));

            } else if (type == 3) {
                reportListModels.add(new ReportListModel(c.getCode(), c.getDayName(), c.getDayDate(), null, c.getOrderGivenOutPrice()));

            } else if (type == 4) {
                reportListModels.add(new ReportListModel(c.getCode(), c.getDayName(), c.getDayDate(), null, getTotal(c.getMilkCollectedValueLtrsAm(), c.getMilkCollectedValueLtrsPm(), c.getLoanAmountGivenOutPrice(), c.getOrderGivenOutPrice())));

            }


        }
        return reportListModels;
    }

    public static List<ReportListModel> generateReportListModel(MonthsDates mds, List<Collection> collections, int type) {


        List<ReportListModel> reportListModels = new LinkedList<>();
        for (Collection c : collections) {
            if (DateTimeUtils.Companion.isInMonth(c.getDayDate(), mds.getMonthName())) {

                if (type == 1) {
                    reportListModels.add(new ReportListModel(c.getCode(), c.getDayName(), c.getDayDate(), c.getMilkCollectedValueLtrsAm(), c.getMilkCollectedValueLtrsPm()));
                } else if (type == 2) {
                    reportListModels.add(new ReportListModel(c.getCode(), c.getDayName(), c.getDayDate(), null, c.getLoanAmountGivenOutPrice()));

                } else if (type == 3) {
                    reportListModels.add(new ReportListModel(c.getCode(), c.getDayName(), c.getDayDate(), null, c.getOrderGivenOutPrice()));

                } else if (type == 4) {
                    //  reportListModels.add(new ReportListModel(c.getId(), c.getDayName(), c.getDayDate(), null, "Milk "+getTotal(c.getMilkCollectedValueLtrsAm(),c.getMilkCollectedValueLtrsPm())+" Loan "+c.getLoanAmountGivenOutPrice()+" Order "+c.getOrderGivenOutPrice()));

                }
            }


        }
        return reportListModels;


    }

    public static List<ReportListModel> generateListModel(MonthsDates mds, List<Collection> collections, int type) {


        List<ReportListModel> reportListModels = new LinkedList<>();
        for (Collection c : collections) {
            if (DateTimeUtils.Companion.isInMonth(c.getDayDate(), mds.getMonthName())) {

                if (type == 1) {
                    reportListModels.add(new ReportListModel(c.getCode(), c.getDayName(), c.getDayDate(), null, getTotal(c.getMilkCollectedValueLtrsAm(), c.getMilkCollectedValueLtrsPm())));
                } else if (type == 2) {
                    reportListModels.add(new ReportListModel(c.getCode(), c.getDayName(), c.getDayDate(), null, c.getLoanAmountGivenOutPrice()));

                } else if (type == 3) {
                    reportListModels.add(new ReportListModel(c.getCode(), c.getDayName(), c.getDayDate(), null, c.getOrderGivenOutPrice()));

                } else if (type == 4) {
                    //  reportListModels.add(new ReportListModel(c.getId(), c.getDayName(), c.getDayDate(), null, "Milk "+getTotal(c.getMilkCollectedValueLtrsAm(),c.getMilkCollectedValueLtrsPm())+" Loan "+c.getLoanAmountGivenOutPrice()+" Order "+c.getOrderGivenOutPrice()));

                }
            }


        }
        return reportListModels;


    }


    public static List<ReportLineChartModel> getCollectionsMonthlyReport(List<Collection> collections, List<MonthsDates> monthsDates, int type) {
        List<ReportLineChartModel> reportLineChartModels = new LinkedList<>();

        for (MonthsDates m : monthsDates) {

            List<ReportListModel> listModels = generateListModel(m, collections, type);
            double total = 0.0;
            for (ReportListModel r : listModels) {
                total = total + Double.valueOf(r.getValue2());
            }
            reportLineChartModels.add(new ReportLineChartModel(m.getMonthName(), String.valueOf(total)));

        }
        return reportLineChartModels;

    }


    public static List<ReportLineChartModel> getCollectionsCustomReport(List<Collection> collections, List<DaysDates> daysDates, int type) {
        List<ReportLineChartModel> reportLineChartModels = new LinkedList<>();

        for (DaysDates d : daysDates) {

            Double total = 0.0;
            if (type == 1) {
                total = getDayMilkTotal(d, collections);
            } else if (type == 2) {
                total = getDayLoanTotal(d, collections);

            } else if (type == 3) {


                total = getDayOrderTotal(d, collections);


            } else if (type == 3) {


                total = getDayOrderTotal(d, collections);


            }


            reportLineChartModels.add(new ReportLineChartModel(d.getDate(), String.valueOf(total)));

        }
        return reportLineChartModels;

    }

    static double getDayMilkTotal(DaysDates d, List<Collection> collections) {
        MilkModel milkModelAm = CommonFuncs.getMilk(d.getDate(), "AM", collections);
        MilkModel milkModelPm = CommonFuncs.getMilk(d.getDate(), "PM", collections);


        String milkAm = milkModelAm.getUnitQty();
        String milkPm = milkModelPm.getUnitQty();

        double totalmilkAm = 0.0;
        double totalmilkpm = 0.0;
        try {
            totalmilkAm = Double.valueOf(milkModelAm.getValueLtrs());

        } catch (Exception nm) {
            nm.printStackTrace();
        }
        try {
            totalmilkpm = Double.valueOf(milkModelPm.getValueLtrs());

        } catch (Exception nm) {
            nm.printStackTrace();
        }
        return totalmilkAm + totalmilkpm;

    }

    static double getDayLoanTotal(DaysDates d, List<Collection> collections) {
        LoanModel loanModel = CommonFuncs.getLoanForDay(d.getDate(), collections);
        return Double.valueOf(loanModel.getLoanAmount());

    }

    static double getDayOrderTotal(DaysDates d, List<Collection> collections) {
        OrderModel orderModel = CommonFuncs.getOrderForDay(d.getDate(), collections);
        return Double.valueOf(orderModel.getOrderAmount());

    }

    private static String getTotal(String a1, String b1, String c1, String d1) {
        double a = 0.0;
        double b = 0.0;
        double c = 0.0;
        double d = 0.0;

        if (a1 != null) {
            a = Double.valueOf(a1);
        }
        if (b1 != null) {
            b = Double.valueOf(b1);
        }
        if (c1 != null) {
            c = Double.valueOf(c1);
        }
        if (d1 != null) {
            d = Double.valueOf(d1);
        }


        return String.valueOf(a + b + c + d);
    }

    private static String getTotal(String aa, String ab) {
        double a = 0.0;
        double b = 0.0;

        if (aa != null) {
            a = Double.valueOf(aa);
        }
        if (ab != null) {
            b = Double.valueOf(ab);
        }

        return String.valueOf(a + b);
    }


    public static Payouts createPayoutsByCollection(List<Collection> collections, Payouts p,
                                                    PayoutsVewModel payoutsVewModel,
                                                    BalncesViewModel balncesViewModel
            , String farmerId, boolean isFarmer,
                                                    List<FamerModel> famerModels

    ) {


        double total = 0.0;
        double milk = 0.0;
        double loans = 0.0;
        double orders = 0.0;


        double milkLtrs = 0.0;

        double milkKsh = 0.0;


        double loansInstallments = 0.0;
        double orderInstallments = 0.0;


        double loansPaid = 0.0;
        double orderPaid = 0.0;


        List<FarmerLoansTable> farmerLoansTables;
        List<FarmerOrdersTable> farmerOrdersTables;
        if (!isFarmer) {
            farmerLoansTables = balncesViewModel.getFarmerLoanByPayoutCodeOne(p.getCode());
            farmerOrdersTables = balncesViewModel.getFarmerOrderByPayoutCodeOne(p.getCode());
        } else {
            farmerLoansTables = balncesViewModel.getFarmerLoanByPayoutCodeByFarmerOne(p.getCode(), farmerId);
            farmerOrdersTables = balncesViewModel.getFarmerOrderByPayoutCodeByFarmerOne(p.getCode(), farmerId);
        }


        if (farmerLoansTables != null) {
            for (FarmerLoansTable farmerLoansTable : farmerLoansTables) {
                try {
                    if (farmerLoansTable != null) {
                        loans = +Double.valueOf(farmerLoansTable.getLoanAmount());
                        loansInstallments = +Double.valueOf(farmerLoansTable.getInstallmentAmount());
                        loansPaid = +balncesViewModel.getSumPaidLoanPayment(farmerLoansTable.getCode());
                    }

                } catch (Exception nm) {
                    Timber.tag("CreatePayout").e(nm.toString());
                }
            }
        }


        if (farmerOrdersTables != null) {
            for (FarmerOrdersTable farmerOrdersTable : farmerOrdersTables) {
                try {
                    if (farmerOrdersTable != null) {
                        orders = +Double.valueOf(farmerOrdersTable.getOrderAmount());
                        orderInstallments = +Double.valueOf(farmerOrdersTable.getInstallmentAmount());
                        orderPaid = +balncesViewModel.getSumPaidOrderPayment(farmerOrdersTable.getCode());

                    }

                } catch (Exception nm) {
                    Timber.tag("CreatePayout").e(nm.toString());

                }
            }
        }


        if (collections != null) {
            for (Collection coll : collections) {
                Timber.tag("CreatePayout").e(coll.getFarmerName());


                try {
                    milk = milk + (Double.valueOf(coll.getMilkCollectedAm()) + Double.valueOf(coll.getMilkCollectedPm()));
                    milkLtrs = milkLtrs + (Double.valueOf(coll.getMilkCollectedValueLtrsAm()) + Double.valueOf(coll.getMilkCollectedValueLtrsPm()));
                    milkKsh = milkKsh + (Double.valueOf(coll.getMilkCollectedValueKshAm()) + Double.valueOf(coll.getMilkCollectedValueKshPm()));

                    if (coll.getLoanAmountGivenOutPrice() != null) {
                        loans = loans + Double.valueOf(coll.getLoanAmountGivenOutPrice());
                    }
                    if (coll.getOrderGivenOutPrice() != null) {
                        orders = orders + Double.valueOf(coll.getOrderGivenOutPrice());
                    }
                } catch (Exception nm) {
                    nm.printStackTrace();
                }


            }
        }


        if (isFarmer) {

            p.setBalance(String.valueOf(balncesViewModel.getByFarmerCodeByPayoutOne(farmerId, p.getCode()).getBalanceToPay()));
            p.setBalanceTotal(String.valueOf(balncesViewModel.getByFarmerCodeByPayoutOne(farmerId, p.getCode()).getBalanceOwed()));

        } else {


            p.setBalanceTotal(String.valueOf(milkKsh - ((loans - loansPaid) + (orders - orderPaid))));
            p.setBalance(String.valueOf(milkKsh - (loansInstallments + orderInstallments)));

        }


        int status[] = getApprovedCards(collections, p.getCycleCode(), payoutsVewModel);
        p.setMilkTotal(String.valueOf(milk));

        p.setMilkTotalKsh(String.valueOf(milkKsh));
        p.setMilkTotalLtrs(String.valueOf(milkLtrs));

        p.setLoanTotal(String.valueOf(loansInstallments));
        p.setOrderTotal(String.valueOf(orderInstallments));

        p.setFarmersCount("" + payoutsVewModel.getFarmersCountByCycle("" + p.getCycleCode()));
        p.setApprovedCards("" + status[1]);
        p.setPendingCards("" + status[2]);


        return p;

    }

    public static Payouts createPayouts(List<Collection> collections,
                                        Payouts p,
                                        PayoutsVewModel payoutsVewModel,
                                        BalncesViewModel balncesViewModel


    ) {


        double total = 0.0;
        double milk = 0.0;
        double loans = 0.0;
        double orders = 0.0;


        double milkLtrs = 0.0;

        double milkKsh = 0.0;


        double loansInstallments = 0.0;
        double orderInstallments = 0.0;


        double loansPaid = 0.0;
        double orderPaid = 0.0;


        List<FarmerLoansTable> farmerLoansTables;
        List<FarmerOrdersTable> farmerOrdersTables;

        farmerLoansTables = balncesViewModel.getFarmerLoanByPayoutCodeOne(p.getCode());
        farmerOrdersTables = balncesViewModel.getFarmerOrderByPayoutCodeOne(p.getCode());


        if (farmerLoansTables != null) {
            for (FarmerLoansTable farmerLoansTable : farmerLoansTables) {
                try {
                    if (farmerLoansTable != null) {
                        loans = +Double.valueOf(farmerLoansTable.getLoanAmount());
                        loansInstallments = +Double.valueOf(farmerLoansTable.getInstallmentAmount());
                        loansPaid = +balncesViewModel.getSumPaidLoanPayment(farmerLoansTable.getCode());
                    }

                } catch (Exception nm) {
                    Timber.tag("CreatePayout").e(nm.toString());
                }
            }
        }


        if (farmerOrdersTables != null) {
            for (FarmerOrdersTable farmerOrdersTable : farmerOrdersTables) {
                try {
                    if (farmerOrdersTable != null) {
                        orders = +Double.valueOf(farmerOrdersTable.getOrderAmount());
                        orderInstallments = +Double.valueOf(farmerOrdersTable.getInstallmentAmount());
                        orderPaid = +balncesViewModel.getSumPaidOrderPayment(farmerOrdersTable.getCode());

                    }

                } catch (Exception nm) {
                    Timber.tag("CreatePayout").e(nm.toString());

                }
            }
        }


        if (collections != null) {
            for (Collection coll : collections) {
                Timber.tag("CreatePayout").e(coll.getFarmerName());


                try {
                    milk = milk + (Double.valueOf(coll.getMilkCollectedAm()) + Double.valueOf(coll.getMilkCollectedPm()));
                    milkLtrs = milkLtrs + (Double.valueOf(coll.getMilkCollectedValueLtrsAm()) + Double.valueOf(coll.getMilkCollectedValueLtrsPm()));
                    milkKsh = milkKsh + (Double.valueOf(coll.getMilkCollectedValueKshAm()) + Double.valueOf(coll.getMilkCollectedValueKshPm()));

                    if (coll.getLoanAmountGivenOutPrice() != null) {
                        loans = loans + Double.valueOf(coll.getLoanAmountGivenOutPrice());
                    }
                    if (coll.getOrderGivenOutPrice() != null) {
                        orders = orders + Double.valueOf(coll.getOrderGivenOutPrice());
                    }
                } catch (Exception nm) {
                    nm.printStackTrace();
                }


            }
        }


        p.setBalanceTotal(String.valueOf(milkKsh - ((loans - loansPaid) + (orders - orderPaid))));
        p.setBalance(String.valueOf(milkKsh - (loansInstallments + orderInstallments)));


        int status[] = getApprovedCards(collections, p.getCycleCode(), payoutsVewModel);
        p.setMilkTotal(String.valueOf(milk));

        p.setMilkTotalKsh(String.valueOf(milkKsh));
        p.setMilkTotalLtrs(String.valueOf(milkLtrs));

        p.setLoanTotal(String.valueOf(loansInstallments));
        p.setOrderTotal(String.valueOf(orderInstallments));

        p.setFarmersCount("" + payoutsVewModel.getFarmersCountByCycle("" + p.getCycleCode()));
        p.setApprovedCards("" + status[1]);
        p.setPendingCards("" + status[2]);


        return p;

    }


    public static FamerModel addBalance(FamerModel famerModel, TraderViewModel traderViewModel, BalncesViewModel balncesViewModel, Collection c, String payoutCode, int type, FarmerLoansTable farmerLoansTable, FarmerOrdersTable farmerOrdersTable) {


        return updateBalance(famerModel, traderViewModel, balncesViewModel, c, payoutCode, type, farmerLoansTable, farmerOrdersTable);


    }

    public static FamerModel updateBalance(FamerModel famerModel,
                                           TraderViewModel traderViewModel,
                                           BalncesViewModel balncesViewModel,
                                           Collection c, String payoutCode, int type, FarmerLoansTable farmerLoan, FarmerOrdersTable farmerOrder) {


        String timed = DateTimeUtils.Companion.getNow();


        if (type == AppConstants.MILK) {


            return refreshTotalBalances(0, null, null, balncesViewModel, traderViewModel, c, famerModel);


        } else if (type == AppConstants.LOAN) {

            LoanModel loanModelm = new Gson().fromJson(c.getLoanDetails(), LoanModel.class);


            if (farmerLoan == null) {

                farmerLoan = new FarmerLoansTable(
                        GeneralUtills.Companion.createCode(c.getFarmerCode()),
                        c.getCode(),
                        payoutCode,
                        c.getFarmerCode(),
                        c.getLoanAmountGivenOutPrice(),
                        "0",
                        loanModelm.getInstallmentAmount(),
                        loanModelm.getInstallmentsNo(),
                        0,
                        timed);


                return refreshLoanStatus(balncesViewModel, c.getCode(), 1, farmerLoan, traderViewModel, c, famerModel);


            } else {

                farmerLoan.setInstallmentAmount(loanModelm.getInstallmentAmount());
                farmerLoan.setLoanAmount(c.getLoanAmountGivenOutPrice());
                farmerLoan.setInstallmentNo(loanModelm.getInstallmentsNo());
                farmerLoan.setTimestamp(timed);


                return refreshLoanStatus(balncesViewModel, c.getCode(), 2, farmerLoan, traderViewModel, c, famerModel);


            }


        } else if (type == AppConstants.ORDER) {

            OrderModel orderModel = new Gson().fromJson(c.getOrderDetails(), OrderModel.class);


            if (farmerOrder == null) {
                farmerOrder = new FarmerOrdersTable(
                        c.getCode(),
                        payoutCode,
                        c.getFarmerCode(),
                        c.getOrderGivenOutPrice(),
                        "0",
                        orderModel.getInstallmentAmount(),
                        orderModel.getInstallmentNo(),
                        0,
                        timed,
                        GeneralUtills.Companion.createCode(c.getFarmerCode()));
                balncesViewModel.insertOrder(farmerOrder);

                return refreshOrderStatus(balncesViewModel, c.getCode(), 2, farmerOrder, traderViewModel, c, famerModel);


            } else {


                farmerOrder.setInstallmentAmount(orderModel.getInstallmentAmount());
                farmerOrder.setOrderAmount(c.getOrderGivenOutPrice());
                farmerOrder.setInstallmentNo(orderModel.getInstallmentNo());
                farmerOrder.setTimestamp(timed);


                return refreshOrderStatus(balncesViewModel, c.getCode(), 1, farmerOrder, traderViewModel, c, famerModel);


            }


        } else {
            return null;
        }


    }

    static Double orderTotalD = 0.0;

    private static FamerModel refreshTotalBalances(int type,
                                                   FarmerLoansTable lastLoan,
                                                   FarmerOrdersTable lastOrder,
                                                   BalncesViewModel balncesViewModel,
                                                   TraderViewModel traderViewModel,
                                                   Collection c,
                                                   FamerModel famerModel) {

        Double totalMilkForCurrentPayout = 0.0;
        try {
            totalMilkForCurrentPayout = traderViewModel.getSumOfMilkForPayoutKshD(c.getFarmerCode(), c.getPayoutCode());

        } catch (Exception nm) {
            nm.printStackTrace();

        }
        try {
            FarmerBalance farmerBalance = balncesViewModel.getByFarmerCodeByPayoutOne(c.getFarmerCode(), c.getPayoutCode());
            List<FarmerLoansTable> loansTables = balncesViewModel.getFarmerLoanByPayoutNumberByFarmerByStatus(c.getFarmerCode(), 0);
            List<FarmerOrdersTable> ordersTables = balncesViewModel.getFarmerOrderByPayoutNumberByFarmerByStatus(c.getFarmerCode(), 0);


            double loanTotalAmount = 0.0;
            double loanInstalmentAmount = 0.0;
            double loanPaid = 0.0;

            double orderTotalAmount = 0.0;
            double orderInstalmentAmount = 0.0;
            double orderPaid = 0.0;


            if (type == 1) {
                boolean isFound = false;
                for (FarmerLoansTable fl : loansTables) {
                    if (fl.getTimestamp().equals(lastLoan.getTimestamp())) {
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) {
                    loansTables.add(lastLoan);
                }
            } else if (type == 2) {
                boolean isFound = false;
                for (FarmerOrdersTable fo : ordersTables) {
                    if (fo.getTimestamp().equals(lastOrder.getTimestamp())) {
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) {
                    ordersTables.add(lastOrder);
                }
            }


            if (loansTables != null) {
                for (FarmerLoansTable fl : loansTables) {

                    loanTotalAmount = +(Double.valueOf(fl.getLoanAmount()));
                    loanInstalmentAmount = +(Double.valueOf(fl.getInstallmentAmount()));
                    loanPaid = +balncesViewModel.getSumPaidLoanPayment(fl.getCode());
                }
            }

            if (ordersTables != null) {
                for (FarmerOrdersTable fo : ordersTables) {
                    orderTotalAmount = +(Double.valueOf(fo.getOrderAmount()));
                    orderInstalmentAmount = +(Double.valueOf(fo.getInstallmentAmount()));
                    orderPaid = +balncesViewModel.getSumPaidOrderPayment(fo.getCode());
                }
            }


            if (farmerBalance == null) {


                farmerBalance = new FarmerBalance(GeneralUtills.Companion.createCode(c.getFarmerCode()),
                        c.getFarmerCode(), c.getPayoutCode(), "", "", "", String.valueOf(loanTotalAmount), String.valueOf(orderTotalAmount), String.valueOf(totalMilkForCurrentPayout));

                farmerBalance.setBalanceOwed(String.valueOf((totalMilkForCurrentPayout - ((loanTotalAmount - loanPaid) + (orderTotalAmount - orderPaid)))));
                farmerBalance.setBalanceToPay(String.valueOf((totalMilkForCurrentPayout - ((loanInstalmentAmount) + (orderInstalmentAmount)))));


                famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());


                balncesViewModel.insertDirect(farmerBalance);
                famerModel.setTotalbalance(farmerBalance.getBalanceToPay());
                // traderViewModel.updateFarmer(famerModel, false, false);


            } else {


                farmerBalance.setBalanceOwed(String.valueOf((totalMilkForCurrentPayout - ((loanTotalAmount - loanPaid) + (orderTotalAmount - orderPaid)))));
                farmerBalance.setBalanceToPay(String.valueOf((totalMilkForCurrentPayout - ((loanInstalmentAmount) + (orderInstalmentAmount)))));


                famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());


                balncesViewModel.updateRecordDirect(farmerBalance);
                famerModel.setTotalbalance(farmerBalance.getBalanceToPay());
                //traderViewModel.updateFarmer(famerModel, false, false);

                // handler(traderViewModel, famerModel);
            }
            return famerModel;

        } catch (Exception nm) {
            nm.printStackTrace();

            return null;
        }

    }

    private static void handler(TraderViewModel traderViewModel, FamerModel famerModel) {
        int spalsh_time_out = 500;
        new Handler().postDelayed(() -> traderViewModel.updateFarmer(famerModel, false, false), spalsh_time_out);

    }


    private static FamerModel refreshLoanStatus(BalncesViewModel balncesViewModel, String id, int type, FarmerLoansTable farmerLoansTable, TraderViewModel traderViewModel, Collection c, FamerModel famerModel) {


        if (type == 2) {//UPDATE LOAN

            double paid = 0.0;
            try {
                paid = balncesViewModel.getSumPaidLoanPayment(farmerLoansTable.getCode());
            } catch (Exception nm) {
                nm.printStackTrace();
            }

            try {
                if (paid == Double.valueOf(farmerLoansTable.getLoanAmount()) || paid > Double.valueOf(farmerLoansTable.getLoanAmount())) {
                    farmerLoansTable.setStatus(1);
                    farmerLoansTable.setLoanAmountPaid(String.valueOf(paid));
                    // balncesViewModel.updateRecordLoan(farmerLoansTable);
                } else {
                    farmerLoansTable.setStatus(0);
                    farmerLoansTable.setLoanAmountPaid(String.valueOf(paid));
                    //  balncesViewModel.updateRecordLoan(farmerLoansTable);
                }
            } catch (Exception nm) {
                nm.printStackTrace();
            }


            if (Double.valueOf(farmerLoansTable.getLoanAmount()) < 1) {
                balncesViewModel.deleteRecordLoanDirect(farmerLoansTable);

            } else {
                balncesViewModel.updateRecordLoanDirect(farmerLoansTable);
            }

        } else {
            balncesViewModel.insertLoanDirect(farmerLoansTable);


        }

        return refreshTotalBalances(1, farmerLoansTable, null, balncesViewModel, traderViewModel, c, famerModel);


    }

    private static FamerModel refreshOrderStatus(BalncesViewModel balncesViewModel, String id, int type, FarmerOrdersTable farmerOrdersTable, TraderViewModel traderViewModel, Collection c, FamerModel famerModel) {


        if (type == 2) {//UPDATE

            double paid = 0.0;
            try {
                paid = balncesViewModel.getSumPaidOrderPayment(farmerOrdersTable.getCode());
            } catch (Exception nm) {
                nm.printStackTrace();
            }


            try {
                if (paid == Double.valueOf(farmerOrdersTable.getOrderAmount()) || paid > Double.valueOf(farmerOrdersTable.getOrderAmount())) {
                    farmerOrdersTable.setStatus(1);
                    farmerOrdersTable.setOrderAmountPaid(String.valueOf(paid));
                    //balncesViewModel.updateRecord(farmerOrdersTable);
                } else {
                    farmerOrdersTable.setStatus(0);
                    farmerOrdersTable.setOrderAmountPaid(String.valueOf(paid));

                    //balncesViewModel.updateRecord(farmerOrdersTable);
                }
            } catch (Exception nm) {
                nm.printStackTrace();
            }

            if (Double.valueOf(farmerOrdersTable.getOrderAmount()) < 1) {
                balncesViewModel.deleteRecordDirect(farmerOrdersTable);

            } else {
                balncesViewModel.updateRecordDirect(farmerOrdersTable);
            }


        } else {  //INSERT

            balncesViewModel.insertOrderDirect(farmerOrdersTable);
        }

        return refreshTotalBalances(2, null, farmerOrdersTable, balncesViewModel, traderViewModel, c, famerModel);


    }

    public static String getCardLoan(List<FarmerLoansTable> farmerLoansTables) {

        List<FarmerLoansTable> farmerLoansTablesNotPaid = new LinkedList<>();
        for (FarmerLoansTable f : farmerLoansTables) {
            Log.d("loanCardTotals", "1   " + f.getLoanAmount());

            if (f.getStatus() == 0) {
                farmerLoansTablesNotPaid.add(f);
            }
        }

        double toPay = 0.0;

        for (FarmerLoansTable fg : farmerLoansTablesNotPaid) {
            Double remaining = (Double.valueOf(fg.getLoanAmount()) - Double.valueOf(fg.getLoanAmountPaid()));
            Double installmentAmount = Double.valueOf(fg.getInstallmentAmount());

            Log.d("loanCardTotals", "2  " + remaining + "    \n  " + installmentAmount);

            if (installmentAmount > remaining) {
                toPay = toPay + remaining;
            } else {
                toPay = toPay + installmentAmount;
            }

        }
        return String.valueOf(toPay);


    }

    public static String getCardOrder(List<FarmerOrdersTable> farmerOrderTables) {

        List<FarmerOrdersTable> farmerOrderTablesNotPaid = new LinkedList<>();
        for (FarmerOrdersTable f : farmerOrderTables) {
            if (f.getStatus() == 0) {
                farmerOrderTablesNotPaid.add(f);
            }
        }

        double toPay = 0.0;

        for (FarmerOrdersTable fg : farmerOrderTablesNotPaid) {
            Log.d("comfdeb", "" + fg.getOrderAmount() + "" + fg.getOrderAmountPaid());
            Double oAmount = 0.0;
            Double oAmountPaid = 0.0;

            try {
                oAmount = Double.valueOf(fg.getOrderAmount());
            } catch (Exception nm) {
                nm.printStackTrace();
            }


            try {
                oAmountPaid = Double.valueOf(fg.getOrderAmountPaid());
            } catch (Exception nm) {
                nm.printStackTrace();
            }


            Double remaining = oAmount - oAmountPaid;
            Double installmentAmount = Double.valueOf(fg.getInstallmentAmount());


            if (installmentAmount > remaining) {
                toPay = toPay + remaining;
            } else {
                toPay = toPay + installmentAmount;
            }

        }
        return String.valueOf(toPay);


    }

    static Double loanInstallmentsd = 0.0;
    static Double orderInstallmentsD = 0.0;
    static Double milkCollectionD = 0.0;
    static Double loanTotalD = 0.0;
    static Double totalToPay = 0.0;
    static boolean isLoanEditable = false;
    static boolean isOrderEditable = false;


    public static void doAprove(Context context, BalncesViewModel balncesViewModel,
                                TraderViewModel traderViewModel,
                                PayoutFarmersCollectionModel model,
                                FamerModel c, Payouts p,
                                ApproveFarmerPayCardListener listener) {

        double loanTotalAmount = 0.0;
        double loanInstalmentAmount = 0.0;
        double loanPaid = 0.0;

        double orderTotalAmount = 0.0;
        double orderInstalmentAmount = 0.0;
        double orderPaid = 0.0;


        List<FarmerLoansTable> loansTables = balncesViewModel.getFarmerLoanByPayoutNumberByFarmerByStatus(c.getCode(), 0);
        List<FarmerOrdersTable> ordersTables = balncesViewModel.getFarmerOrderByPayoutNumberByFarmerByStatus(c.getCode(), 0);

        for (FarmerLoansTable fl : loansTables) {
            try {
                loanTotalAmount = +(Double.valueOf(fl.getLoanAmount()));
                loanInstalmentAmount = +(Double.valueOf(fl.getInstallmentAmount()));
                loanPaid = +balncesViewModel.getSumPaidLoanPayment(fl.getCode());
            } catch (Exception nm) {
                nm.printStackTrace();
            }
        }

        for (FarmerOrdersTable fo : ordersTables) {
            try {
                orderTotalAmount = +(Double.valueOf(fo.getOrderAmount()));
                orderInstalmentAmount = +(Double.valueOf(fo.getInstallmentAmount()));
                orderPaid = +balncesViewModel.getSumPaidOrderPayment(fo.getCode());
            } catch (Exception nm) {
                nm.printStackTrace();
            }
        }
        Log.d("RecordAsd", "Alll Order " + loanInstalmentAmount + "\n" + ordersTables.size());


        double totalMilkForCurrentPayout = 0.0;
        if (traderViewModel.getSumOfMilkForPayoutKshD(c.getCode(), p.getCode()) != null) {
            totalMilkForCurrentPayout += traderViewModel.getSumOfMilkForPayoutKshD(c.getCode(), p.getCode());
        }


        doPayout(context, model, "" + totalMilkForCurrentPayout, "" + (loanTotalAmount - loanPaid), "" + (orderTotalAmount - orderPaid), "" + loanInstalmentAmount, "" + orderInstalmentAmount, listener);
    }

    public static void setInputLimiter(TextInputEditText edt, double limit) {

        edt.setFilters(new InputFilter[]{new InputFilterMinMax(0, (int) limit)});
    }

    private static void doPayout(Context context, PayoutFarmersCollectionModel model, String milkCollection, String loanTotal, String orderTotal,
                                 String loanInstallments, String orderInstallments, ApproveFarmerPayCardListener listener) {

        try {
            milkCollectionD = Double.valueOf(milkCollection);
        } catch (Exception nm) {
            nm.printStackTrace();
        }
        try {
            loanTotalD = Double.valueOf(loanTotal);
        } catch (Exception nm) {
            nm.printStackTrace();
        }
        try {
            orderTotalD = Double.valueOf(orderTotal);
        } catch (Exception nm) {
            nm.printStackTrace();
        }
        try {
            orderInstallmentsD = Double.valueOf(orderInstallments);
        } catch (Exception nm) {
            nm.printStackTrace();
        }
        try {
            loanInstallmentsd = Double.valueOf(loanInstallments);
        } catch (Exception nm) {
            nm.printStackTrace();
        }


        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_approve_farmer_card, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(context));
        alertDialogBuilderUserInput.setView(mView);


        TextView txtMilkCollections, txtTotalLoans, txtTotalOrders, txtPayout;
        TextInputEditText edtLoanInstallment, edtOrderInstallment;

        txtMilkCollections = mView.findViewById(R.id.txt_approve_milk);
        txtTotalLoans = mView.findViewById(R.id.txt_approve_loans);
        txtTotalOrders = mView.findViewById(R.id.txt_approve_orders);
        txtPayout = mView.findViewById(R.id.txt_approve_payout);

        edtLoanInstallment = mView.findViewById(R.id.edt_approve_loan_installment);
        edtOrderInstallment = mView.findViewById(R.id.edt_approve_order_installments);


        txtMilkCollections.setText(GeneralUtills.Companion.round(milkCollection, 0));
        txtTotalLoans.setText(GeneralUtills.Companion.round(loanTotal, 0));
        txtTotalOrders.setText(GeneralUtills.Companion.round(orderTotal, 0));

        // edtLoanInstallment.setFilters(new InputFilter[]{new InputFilterMinMax("0","100")});

        edtLoanInstallment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                double installmentValue = 0.0;

                if (editable != null) {

                    edtLoanInstallment.getText().toString();
                    if (!TextUtils.isEmpty(edtLoanInstallment.getText().toString())) {
                        try {
                            double value = Double.valueOf(edtLoanInstallment.getText().toString());
                            loanInstallmentsd = value;

                        } catch (Exception nm) {
                            nm.printStackTrace();
                        }

                    } else {
                        loanInstallmentsd = 0.0;
                    }
                } else {
                    loanInstallmentsd = 0.0;
                }


                if (isLoanEditable) {
                    totalToPay = (milkCollectionD - (loanInstallmentsd + orderInstallmentsD));
                    setInputLimiter(edtLoanInstallment, totalToPay);
                } else {
                    totalToPay = (milkCollectionD - (orderInstallmentsD));

                }
                if (totalToPay > 0) {
                    txtPayout.setText(GeneralUtills.Companion.round(String.valueOf(totalToPay), 0));
                } else {
                    txtPayout.setText("0");

                }


            }
        });
        edtOrderInstallment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                double installmentValue = 0.0;

                if (editable != null) {

                    edtOrderInstallment.getText().toString();
                    if (!TextUtils.isEmpty(edtOrderInstallment.getText().toString())) {

                        try {
                            double value = Double.valueOf(edtOrderInstallment.getText().toString());
                            orderInstallmentsD = value;

                        } catch (Exception nm) {
                            nm.printStackTrace();
                        }

                    } else {
                        orderInstallmentsD = 0.0;
                    }
                } else {
                    orderInstallmentsD = 0.0;
                }


                if (isLoanEditable) {
                    totalToPay = (milkCollectionD - (loanInstallmentsd + orderInstallmentsD));
                    setInputLimiter(edtLoanInstallment, totalToPay);
                } else {
                    totalToPay = (milkCollectionD - (orderInstallmentsD));

                }
                if (totalToPay > 0) {
                    txtPayout.setText(GeneralUtills.Companion.round(String.valueOf(totalToPay), 0));
                } else {
                    txtPayout.setText("0");

                }
            }
        });


        double maxplayBalnce = milkCollectionD;


        edtLoanInstallment.setText(GeneralUtills.Companion.round(loanInstallments, 0));

        edtOrderInstallment.setText(GeneralUtills.Companion.round(orderInstallments, 0));


        if (milkCollectionD > (loanInstallmentsd + orderInstallmentsD)) {


            edtLoanInstallment.setText(GeneralUtills.Companion.roundD(loanInstallmentsd, 0));
            edtOrderInstallment.setText(GeneralUtills.Companion.roundD(orderInstallmentsD, 0));
            edtLoanInstallment.setVisibility(View.VISIBLE);
            edtOrderInstallment.setVisibility(View.VISIBLE);


            isLoanEditable = true;
            isOrderEditable = true;


            setInputLimiter(edtLoanInstallment, maxplayBalnce);
            setInputLimiter(edtOrderInstallment, maxplayBalnce);


        } else if (milkCollectionD > orderInstallmentsD && milkCollectionD < (loanInstallmentsd + orderInstallmentsD)) {
            edtOrderInstallment.setText(GeneralUtills.Companion.roundD(orderInstallmentsD, 0));
            edtLoanInstallment.setVisibility(View.GONE);
            edtOrderInstallment.setVisibility(View.VISIBLE);

            isLoanEditable = false;
            isOrderEditable = true;

            setInputLimiter(edtOrderInstallment, maxplayBalnce);


        } else if (milkCollectionD > 0) {
            edtOrderInstallment.setText(GeneralUtills.Companion.roundD(milkCollectionD, 0));
            edtLoanInstallment.setVisibility(View.GONE);
            edtOrderInstallment.setVisibility(View.VISIBLE);

            isLoanEditable = false;
            isOrderEditable = true;


            setInputLimiter(edtOrderInstallment, milkCollectionD);


        } else {
            isLoanEditable = false;
            isOrderEditable = false;

            edtLoanInstallment.setVisibility(View.GONE);
            edtOrderInstallment.setVisibility(View.GONE);
        }


        if (orderInstallmentsD < 1) {
            edtOrderInstallment.setVisibility(View.GONE);
        }
        if (loanInstallmentsd < 1) {
            edtLoanInstallment.setVisibility(View.GONE);
        }

        totalToPay = (milkCollectionD - (loanInstallmentsd + orderInstallmentsD));


        if (totalToPay > 0) {
            txtPayout.setText(GeneralUtills.Companion.round(String.valueOf(totalToPay), 0));
        } else {
            txtPayout.setText("0");

        }

        try {
            edtLoanInstallment.setSelection(Objects.requireNonNull(edtLoanInstallment.getText()).toString().length());
            edtOrderInstallment.setSelection(Objects.requireNonNull(edtOrderInstallment.getText()).toString().length());
        } catch (Exception nm) {
            nm.printStackTrace();
        }


        alertDialogBuilderUserInput.setCancelable(false);
        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        Objects.requireNonNull(alertDialogAndroid.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialogAndroid.show();


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
        lTitle.setVisibility(View.GONE);
        txtTitle.setVisibility(View.VISIBLE);
        imgIcon.setVisibility(View.VISIBLE);
        imgIcon.setImageResource(R.drawable.ic_add_black_24dp);
        txtTitle.setText("Approve");


        btnPositive.setOnClickListener(view -> {


            if (totalToPay > 0 && loanInstallmentsd > 0 && orderInstallmentsD > 0) {
                listener.onApprove(model, totalToPay, loanInstallmentsd, orderInstallmentsD);
                alertDialogAndroid.dismiss();
            } else if (totalToPay > 1 && loanInstallmentsd > 1 && orderInstallmentsD < 1) {
                listener.onApprovePayLoan(model, totalToPay, loanInstallmentsd);
                alertDialogAndroid.dismiss();

            } else if (totalToPay > 1 && loanInstallmentsd < 1 && orderInstallmentsD > 1) {
                listener.onApprovePayOrder(model, totalToPay, orderInstallmentsD);
                alertDialogAndroid.dismiss();

            } else if (totalToPay > 1 && loanInstallmentsd < 1 && orderInstallmentsD < 1) {
                listener.onApprove(model, totalToPay);
                alertDialogAndroid.dismiss();

            } else {
                listener.onApprove(model);
                alertDialogAndroid.dismiss();
            }
        });
        btnNeutral.setOnClickListener(view -> {

            listener.onApproveDismiss();
        });
        btnNegative.setOnClickListener(view -> {
            listener.onApproveDismiss();

            alertDialogAndroid.dismiss();
        });


    }

    public static String getPreviousPayoutBalance(FamerModel famerModel, BalncesViewModel balncesViewModel) {
        List<FarmerBalance> farmerBalances = balncesViewModel.getByFarmerCodeOne(famerModel.getCode());

        Double total = 0.0;
        for (FarmerBalance f : farmerBalances) {
            try {
                boolean isPast = DateTimeUtils.Companion.isPastLastDay(f.getLastUpdated());
                if (f.getPayoutStatus() == 0 && !f.getPayoutCode().equals(famerModel.getCurrentPayoutCode()) && !isPast) {
                    total = total + (Double.valueOf(f.getBalanceToPay()));
                }
            } catch (Exception nm) {
                nm.printStackTrace();
            }
        }
        return String.valueOf(total);
    }


    public static class ValueObject {
        private String value;
        private Object o;

        public ValueObject(String value, Object o) {
            this.value = value;
            this.o = o;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Object getO() {
            return o;
        }

        public void setO(Object o) {
            this.o = o;
        }


    }

    public static void updateBalance(Application mInstance, BalncesViewModel balncesViewModel) {
        BalanceRepo balanceRepo = new BalanceRepo(mInstance);
        LoansTableRepo loansTableRepo = new LoansTableRepo(mInstance);
        OrdersTableRepo ordersTableRepo = new OrdersTableRepo(mInstance);
        LoanPaymentsRepo loanPaymentsRepo = new LoanPaymentsRepo(mInstance);
        OrderPaymentsRepo orderPaymentsRepo = new OrderPaymentsRepo(mInstance);
        CollectionsRepo collectionsRepo = new CollectionsRepo(mInstance);
        //PayoutsRepo payoutsRepo=new PayoutsRepo(mInstance);


        List<FarmerBalance> farmerBalances = balanceRepo.fetchAllOne();

        if (farmerBalances != null) {
            String farmerCode = "";
            String payoutCode = "";


            for (FarmerBalance f : farmerBalances) {

                if (f.getPayoutCode() != null) {
                    farmerCode = f.getFarmerCode();
                    payoutCode = f.getPayoutCode();
                    updateBalances(0, null, null, balanceRepo,
                            loansTableRepo, ordersTableRepo, loanPaymentsRepo, orderPaymentsRepo,
                            collectionsRepo, farmerCode, payoutCode, balncesViewModel);

                }
            }
        }

    }

    private static void updateBalances(int type,
                                       FarmerLoansTable lastLoan,
                                       FarmerOrdersTable lastOrder,
                                       BalanceRepo balanceRepo, LoansTableRepo loansTableRepo, OrdersTableRepo ordersTableRepo,
                                       LoanPaymentsRepo loanPaymentsRepo, OrderPaymentsRepo orderPaymentsRepo,
                                       CollectionsRepo collectionsRepo, String farmerCode, String payoutCode, BalncesViewModel balncesViewModel
    ) {


        try {
            FarmerBalance farmerBalance = null;
            if (payoutCode == null) {
                farmerBalance = balanceRepo.getByFarmerCodeByPayoutOne(farmerCode, payoutCode);
            } else {
                farmerBalance = balanceRepo.getByFarmerCodeByPayoutOne(farmerCode, payoutCode);
            }


            double loanTotalAmount = 0.0;
            double loanInstalmentAmount = 0.0;
            double loanPaid = 0.0;

            double orderTotalAmount = 0.0;
            double orderInstalmentAmount = 0.0;
            double orderPaid = 0.0;


            List<FarmerLoansTable> loansTables = loansTableRepo.getFarmerLoanByPayoutCodeByFarmerByStatus(farmerCode, 0);
            List<FarmerOrdersTable> ordersTables = ordersTableRepo.getFarmerOrderByPayoutCodeByFarmerByStatus(farmerCode, 0);

            if (type == 1) {
                boolean isFound = false;
                for (FarmerLoansTable fl : loansTables) {
                    if (fl.getTimestamp().equals(lastLoan.getTimestamp())) {
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) {
                    loansTables.add(lastLoan);
                }
            } else if (type == 2) {
                boolean isFound = false;
                for (FarmerOrdersTable fo : ordersTables) {
                    if (fo.getTimestamp().equals(lastOrder.getTimestamp())) {
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) {
                    ordersTables.add(lastOrder);
                }
            }


            for (FarmerLoansTable fl : loansTables) {
                Log.d("DebugUpdateERROR", new Gson().toJson(fl));

                loanTotalAmount = +(Double.valueOf(fl.getLoanAmount()));
                loanInstalmentAmount = +(Double.valueOf(fl.getInstallmentAmount()));
                loanPaid = +loanPaymentsRepo.getSumPaid(fl.getCode());
            }

            for (FarmerOrdersTable fo : ordersTables) {
                orderTotalAmount = +(Double.valueOf(fo.getOrderAmount()));
                orderInstalmentAmount = +(Double.valueOf(fo.getInstallmentAmount()));
                orderPaid = +orderPaymentsRepo.getSumPaid(fo.getCode());
            }


            double totalMilkForCurrentPayout = 0.0;
            try {
                // if (traderViewModel.getSumOfMilkForPayoutKshD(c.getFarmerCode(), c.getPayoutCode()) != null) {
                totalMilkForCurrentPayout += collectionsRepo.getSumOfMilkFarmerPayoutKshD(farmerCode, payoutCode);
                // }


            } catch (Exception nm) {
                nm.printStackTrace();

            }
            if (farmerBalance == null) {


                farmerBalance = new FarmerBalance(GeneralUtills.Companion.createCode(farmerCode),
                        farmerCode, payoutCode, "", "", "", String.valueOf(loanTotalAmount), String.valueOf(orderTotalAmount), String.valueOf(totalMilkForCurrentPayout));

                farmerBalance.setBalanceOwed(String.valueOf((totalMilkForCurrentPayout - ((loanTotalAmount - loanPaid) + (orderTotalAmount - orderPaid)))));
                farmerBalance.setBalanceToPay(String.valueOf((totalMilkForCurrentPayout - ((loanInstalmentAmount) + (orderInstalmentAmount)))));

                balncesViewModel.insert(farmerBalance);

                //  traderViewModel.updateFarmer(famerModel, false, false);


            } else {

                String balanceOwed = String.valueOf((totalMilkForCurrentPayout - ((loanTotalAmount - loanPaid) + (orderTotalAmount - orderPaid))));
                String balanceToPay = String.valueOf((totalMilkForCurrentPayout - ((loanInstalmentAmount) + (orderInstalmentAmount))));

                if (!farmerBalance.getBalanceOwed().equals(balanceOwed) && !farmerBalance.getBalanceToPay().equals(balanceToPay)) {


                    farmerBalance.setBalanceOwed(balanceOwed);
                    farmerBalance.setBalanceToPay(balanceToPay);


                    balncesViewModel.updateRecord(farmerBalance);

                }

                //  traderViewModel.updateFarmer(famerModel, false, false);

            }
        } catch (Exception nm) {
            nm.printStackTrace();

        }


    }

    //
//    public static List<Notifications> getPendingPayouts(List<Payouts> payoutss){
//        List<Notifications> notifications=new LinkedList<>();
//        for(Payouts payouts:payoutss)
//            if (payouts.getStatus() == 0 &&(payouts.getEndDate().equals(DateTimeUtils.Companion.getToday()) ||
//                        DateTimeUtils.Companion.isPastLastDay(payouts.getEndDate()))) {
//                        notifications.add(new Notifications(0,DateTimeUtils.Companion.getToday(),payouts.getCyclename()+"  Payout Due ",
//                    "This payout was due on  "+payouts.getEndDate(),payouts.getCode(),0,11,11));
//        }
//
//
//        return notifications;
//    }
    public static List<Notifications> getPendingPayouts(List<Payouts> payoutss) {
        List<Notifications> notifications = new LinkedList<>();
        for (Payouts payouts : payoutss)
            if (payouts.getStatus() == 0 && (payouts.getEndDate().equals(DateTimeUtils.Companion.getToday()) ||
                    DateTimeUtils.Companion.isPastLastDay(payouts.getEndDate()))) {
                notifications.add(new Notifications(0, DateTimeUtils.Companion.getNow(), payouts.getCyclename() + "  Payout Due ",
                        "This payout was due on  " + payouts.getEndDate(), "PGHTSE", 0, AppConstants.NOTIFICATION_TYPE_INDIVIDUAL_PAYOUT_PENDING, 11, payouts.getCode()));
            }


        try {
            if (notifications.size() == 1) {

                new NotificationRepo(com.dev.lishabora.Application.application).insert(notifications.get(0));

                sendNotification(notifications.get(0).getTitle(), notifications.get(0).getMessage(), true);

            } else if (notifications.size() > 1) {
                String txtTitle = "Un-Approved Payouts ";
                String mes = "You have " + notifications.size() + " Pending payouts that require approval";

                Notifications n = notifications.get(0);
                n.setTitle(txtTitle);
                n.setMessage(mes);
                n.setType(AppConstants.NOTIFICATION_TYPE_MULTIPLE_PAYOUT_PENDING);

                sendNotification(txtTitle, mes, true);

                new NotificationRepo(com.dev.lishabora.Application.application).insert(n);

                sendNotification(txtTitle, mes, true);

            }
        } catch (Exception nm) {
            nm.printStackTrace();
        }

        return notifications;
    }

    public static boolean canApprovePayout(Payouts payouts) {
        if (payouts.getStatus() == 1) {
            return false;

        } else {
            return payouts.getEndDate().equals(DateTimeUtils.Companion.getToday()) ||
                    DateTimeUtils.Companion.isPastLastDay(payouts.getEndDate());
        }

    }

    public static void setUpView(Fragment fragment, FragmentManager manager) {
        if (fragment != null) {
            manager.beginTransaction().replace(R.id.frame_layout, fragment)
                    .addToBackStack(null).commit();
        }

    }

    public static void popOutFragments(FragmentManager manager) {
        for (int i = 0; i < manager.getBackStackEntryCount(); i++) {
            manager.popBackStack();
        }
    }

    public static boolean allCollectionsAreApproved(PayoutsVewModel payoutsVewModel, Payouts payouts) {
        List<Collection> collections = payoutsVewModel.getCollectionByDateByPayoutListOne(payouts.getCode());
        for (Collection c : collections) {
            if (c.getApproved() == 0) {
                return false;
            }
        }
        return true;
    }


    public static void giveLoan(String l, String loanDetails, CollectListener listener, TraderViewModel traderViewModel, FamerModel famerModel) {
        String StringValue = null;
        //String PmStringValue = null;
        Double DoubleValue = 0.0;
        // Double PmDoubleValue = 0.0;
        Collection collModel = null;
        Collection previousColl = null;
        // Collection PmCollModel = null;

        String ampm = "";
        if (DateTimeUtils.Companion.isAM(DateTimeUtils.Companion.getTodayDate())) {

            ampm = "AM";

        } else {

            ampm = "PM";
        }


        StringValue = null;
        //PmStringValue = null;
        DoubleValue = 0.0;
        //PmDoubleValue = 0.0;


        collModel = null;


        collModel = traderViewModel.getCollectionByDateByFarmerByTimeSngle(famerModel.getCode(), DateTimeUtils.Companion.getToday());

        previousColl = collModel;

        if (collModel != null) {
            DoubleValue = DoubleValue + Double.valueOf(collModel.getLoanAmountGivenOutPrice());
            StringValue = String.valueOf(DoubleValue);
        }


        if (collModel == null) {

            Collection c = new Collection();
            c.setCycleCode(famerModel.getCyclecode());
            c.setFarmerCode(famerModel.getCode());
            c.setFarmerName(famerModel.getNames());
            c.setCycleId(famerModel.getCode());
            c.setDayName(DateTimeUtils.Companion.getDayOfWeek(DateTimeUtils.Companion.getTodayDate(), "E"));
            c.setLoanAmountGivenOutPrice("0");
            c.setDayDate(DateTimeUtils.Companion.getToday());
            c.setDayDateLog(DateTimeUtils.Companion.getLongDate(c.getDayDate()));
            c.setCode(GeneralUtills.Companion.createCode(famerModel.getCode()));

            c.setTimeOfDay(ampm);
            c.setMilkCollectedAm("0");
            c.setMilkCollectedPm("0");
            c.setLoanAmountGivenOutPrice(l);
            c.setLoanDetails(loanDetails);
            c.setOrderGivenOutPrice("0");


            c.setLoanId("");
            c.setOrderId("");
            c.setSynced(0);
            c.setSynced(false);
            c.setApproved(0);


            listener.createCollection(c, famerModel, 0.0);


        } else {


            collModel.setLoanAmountGivenOutPrice(l);
            collModel.setLoanDetails(loanDetails);
            listener.updateCollection(collModel, famerModel, 0.0);


        }


    }

    public static void giveOrder(String o, String orderDetails, CollectListener listener, TraderViewModel traderViewModel, FamerModel famerModel) {
        String StringValue = null;
        //String PmStringValue = null;
        Double DoubleValue = 0.0;
        // Double PmDoubleValue = 0.0;
        Collection collModel = null;
        Collection previousColl = null;
        // Collection PmcollModel = null;

        String ampm = "";
        if (DateTimeUtils.Companion.isAM(DateTimeUtils.Companion.getTodayDate())) {

            ampm = "AM";

        } else {

            ampm = "PM";
        }


        StringValue = null;
        DoubleValue = 0.0;


        collModel = null;


        collModel = traderViewModel.getCollectionByDateByFarmerByTimeSngle(famerModel.getCode(), DateTimeUtils.Companion.getToday());

        previousColl = collModel;

        if (collModel != null) {
            DoubleValue = DoubleValue + Double.valueOf(collModel.getOrderGivenOutPrice());
            StringValue = String.valueOf(DoubleValue);
        }


        if (collModel == null) {
            Collection c = new Collection();
            c.setCycleCode(famerModel.getCyclecode());
            c.setFarmerCode(famerModel.getCode());
            c.setFarmerName(famerModel.getNames());
            c.setCycleId(famerModel.getCyclecode());
            c.setDayName(DateTimeUtils.Companion.getDayOfWeek(DateTimeUtils.Companion.getTodayDate(), "E"));
            c.setLoanAmountGivenOutPrice("0");
            c.setCode(GeneralUtills.Companion.createCode(famerModel.getCode()));
            c.setDayDate(DateTimeUtils.Companion.getToday());
            c.setDayDateLog(DateTimeUtils.Companion.getLongDate(c.getDayDate()));

            c.setTimeOfDay(ampm);
            c.setMilkCollectedAm("0");
            c.setLoanAmountGivenOutPrice("0");
            c.setLoanDetails("");
            c.setOrderGivenOutPrice(o);
            c.setOrderDetails(orderDetails);

            c.setLoanId("");
            c.setOrderId("");
            c.setSynced(0);
            c.setSynced(false);
            c.setApproved(0);


            listener.createCollection(c, famerModel, 0.0);


        } else {


            if (collModel != null) {
                collModel.setOrderGivenOutPrice(o);
                collModel.setOrderDetails(orderDetails);
            }
            listener.updateCollection(collModel, famerModel, 0.0);


        }


    }

    private static void sendNotification(String title, String message, boolean isloggedIn) {

        Intent intent = new Intent(com.dev.lishabora.Application.context, TraderActivity.class);
        intent.putExtra("type", "notification_fragment");


        PendingIntent pi = PendingIntent.getActivity(com.dev.lishabora.Application.context, 0,
                intent, 0);

        Notification notification = new NotificationCompat.Builder(com.dev.lishabora.Application.context)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setShowWhen(true)
                .setColor(Color.RED)
                .setLocalOnly(true)
                .build();


        NotificationManagerCompat.from(com.dev.lishabora.Application.context)
                .notify(5, notification);

        // }
    }


    public static Double xChange(Collection nowCollection, Collection previousCollection, int type) {
        Double previusAm = 0.0;
        Double previousPm = 0.0;

        Double nowAm = 0.0;
        Double nowPm = 0.0;

        Double xChange;


        if (previousCollection != null) {

            switch (type) {
                case AppConstants.MILK:
                    if (previousCollection.getMilkCollectedValueKshAm() != null) {
                        try {
                            previusAm = Double.valueOf(previousCollection.getMilkCollectedValueKshAm());
                        } catch (Exception nm) {
                            nm.printStackTrace();
                        }
                    }
                    if (previousCollection.getMilkCollectedValueKshPm() != null) {

                        try {
                            previousPm = Double.valueOf(previousCollection.getMilkCollectedValueKshPm());
                        } catch (Exception nm) {
                            nm.printStackTrace();
                        }
                    }
                    break;
                case AppConstants.LOAN:

                    break;
                case AppConstants.ORDER:

                    break;
            }

        }


        if (nowCollection != null) {

            switch (type) {
                case AppConstants.MILK:
                    if (nowCollection.getMilkCollectedValueKshAm() != null) {
                        try {
                            nowAm = Double.valueOf(nowCollection.getMilkCollectedValueKshAm());
                        } catch (Exception nm) {
                            nm.printStackTrace();
                        }
                    }


                    if (nowCollection.getMilkCollectedValueKshPm() != null) {

                        try {
                            nowPm = Double.valueOf(nowCollection.getMilkCollectedValueKshPm());
                        } catch (Exception nm) {
                            nm.printStackTrace();
                        }
                    }

                    break;
                case AppConstants.LOAN:

                    break;
                case AppConstants.ORDER:

                    break;
            }

        }


        xChange = (nowAm + nowPm) - (previusAm + previousPm);


        return xChange;
    }


    public static class createCollection {
        private Collection collection;
        private FamerModel famerModel;
        private ResponseModel responseModel;

        public createCollection(Collection collection, FamerModel famerModel, ResponseModel responseModel) {
            this.collection = collection;
            this.famerModel = famerModel;
            this.responseModel = responseModel;
        }

        public ResponseModel getResponseModel() {
            return responseModel;
        }

        public Collection getCollection() {
            return collection;
        }

        public void setCollection(Collection collection) {
            this.collection = collection;
        }

        public FamerModel getFamerModel() {
            return famerModel;
        }

        public void setFamerModel(FamerModel famerModel) {
            this.famerModel = famerModel;
        }
    }

    public static class asyn {
        private FamerModel famerModel;
        private Collection collection;
        private Boolean hasToSync;

        public asyn(FamerModel famerModel, Collection collection, boolean hasToSync) {
            this.famerModel = famerModel;
            this.collection = collection;
            this.hasToSync = hasToSync;
        }

        public Boolean getHasToSync() {
            return hasToSync;
        }

        public void setHasToSync(Boolean hasToSync) {
            this.hasToSync = hasToSync;
        }

        public FamerModel getFamerModel() {
            return famerModel;
        }

        public void setFamerModel(FamerModel famerModel) {
            this.famerModel = famerModel;
        }

        public Collection getCollection() {
            return collection;
        }

        public void setCollection(Collection collection) {
            this.collection = collection;
        }
    }


}


