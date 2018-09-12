package com.dev.lishabora.Views;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.DayCollectionModel;
import com.dev.lishabora.Models.DaysDates;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.FarmerHistoryByDateModel;
import com.dev.lishabora.Models.LoanModel;
import com.dev.lishabora.Models.MilkModel;
import com.dev.lishabora.Models.MonthsDates;
import com.dev.lishabora.Models.OrderModel;
import com.dev.lishabora.Models.PayoutFarmersCollectionModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Models.Reports.ReportListModel;
import com.dev.lishabora.Models.UnitsModel;
import com.dev.lishabora.Utils.CollectionCreateUpdateListener;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.Utils.LoanEditValueListener;
import com.dev.lishabora.Utils.MilkEditValueListener;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

public class CommonFuncs {


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
                if (c.getDayDate().contains(date)) {
                    try {
                        loanTotal = Double.valueOf(c.getLoanAmountGivenOutPrice());

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
        MilkModel m = new MilkModel();

        if (collections != null) {
            for (Collection c : collections) {
                if (c.getDayDate().equals(date)) {
                    if (ampm == "AM") {
                        try {
                            milkTotal += Double.valueOf(c.getMilkCollectedAm());
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
        return m;


    }

    public static String getCollectionId(String date, List<Collection> collections) {
        if (collections != null) {
            Timber.tag("collectisdid").d("Am Called  " + date
                    + "" + collections);

            for (Collection c : collections) {

                if (c.getDayDate().contains(date)) {

                    return "" + c.getId();
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

    @NonNull
    public static MilkModel getMilk(String farmercode, List<Collection> collections) {
        double milkTotalQty = 0.0;
        double milkTotalLtrs = 0.0;
        double milkTotalKsh = 0.0;
        MilkModel milkModel = new MilkModel();


        for (Collection c : collections) {
            if (c.getFarmerCode().equals(farmercode)) {
                try {
                    milkTotalQty += (Double.valueOf(c.getMilkCollectedAm()) + Double.valueOf(c.getMilkCollectedPm()));
                    milkTotalLtrs += (Double.valueOf(c.getMilkCollectedValueLtrsPm()) + Double.valueOf(c.getMilkCollectedValueLtrsPm()));
                    milkTotalKsh += (Double.valueOf(c.getMilkCollectedValueKshAm()) + Double.valueOf(c.getMilkCollectedValueKshPm()));
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

    public static String getCollectionIdPm(String date, List<Collection> collections) {
        if (collections != null) {
            Timber.tag("collectisdid").d("Pm Called ");

            for (Collection c : collections) {

                if (c.getDayDate().contains(date) && c.getTimeOfDay().equals("PM")) {
                    Timber.tag("collectisdid").d("PM %s", c.getId());

                    return "" + c.getId();
                }
            }

        }
        return null;
    }

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


        if (dayCollectionModel.getCollectionId() != null) {
                collection = collectionExisting;


            } else {
                Collection c = new Collection();
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
            c.setMilkCollectedPriceAm(dayCollectionModel.getMilkModelAm().getValueKsh());
            c.setMilkDetailsAm(new Gson().toJson(dayCollectionModel.getMilkModelAm()));


            c.setMilkCollectedValueLtrsPm(dayCollectionModel.getMilkModelPm().getValueLtrs());
            c.setMilkCollectedValueKshPm(dayCollectionModel.getMilkModelPm().getValueKsh());
            c.setMilkCollectedPricepm(dayCollectionModel.getMilkModelPm().getValueKsh());
            c.setMilkDetailsPm(new Gson().toJson(dayCollectionModel.getMilkModelPm()));


            c.setLoanDetails(new Gson().toJson(dayCollectionModel.getLoanModel()));
            c.setOrderDetails(new Gson().toJson(dayCollectionModel.getOrderModel()));




                c.setLoanId("");
                c.setOrderId("");
                c.setSynced(0);
                c.setSynced(false);
                c.setApproved(0);

                c.setPayoutnumber(dayCollectionModel.getPayoutNumber());
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

    public static LinkedList<FarmerHistoryByDateModel> createMonthlyList(List<Collection> collections, FamerModel famerModel) {

        List<MonthsDates> monthsDates = DateTimeUtils.Companion.getMonths(12);
        if (monthsDates.size() > 0) {

            LinkedList<FarmerHistoryByDateModel> fmh = new LinkedList<>();

            for (MonthsDates mds : monthsDates) {

                String[] totals = getCollectionsTotals(mds, collections);
                fmh.add(new FarmerHistoryByDateModel(mds, famerModel, totals[0], totals[1], totals[2], totals[3]));

            }
            return fmh;

        }
        return null;


    }


    public static String[] getCollectionsTotals(MonthsDates mds, List<Collection> collections) {
        String cycleCode = "";
        double milk = 0.0;
        double loan = 0.0;
        double order = 0.0;

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
            }

        }
        double[] totals = {milk, loan, order};


        return new String[]{String.valueOf(totals[0]), String.valueOf(totals[1]), String.valueOf(totals[2]), cycleCode};
    }

    public static Payouts createPayoutsByCollection(List<Collection> collections, Payouts p, PayoutsVewModel payoutsVewModel) {

        double total = 0.0;
        double milk = 0.0;
        double loans = 0.0;
        double orders = 0.0;

        double milkLtrs = 0.0;
        double milkKsh = 0.0;
        for (Collection coll : collections) {

            milk = milk + (Double.valueOf(coll.getMilkCollectedAm()) + Double.valueOf(coll.getMilkCollectedPm()));


            Log.d("xcdc", "" + coll.getMilkCollectedValueLtrsAm() + "    " + coll.getMilkCollectedValueLtrsPm());
            milkLtrs = milkLtrs + (Double.valueOf(coll.getMilkCollectedValueLtrsAm()) + Double.valueOf(coll.getMilkCollectedValueLtrsPm()));

            milkKsh = milkKsh + (Double.valueOf(coll.getMilkCollectedValueKshAm()) + Double.valueOf(coll.getMilkCollectedValueKshPm()));

            if (coll.getLoanAmountGivenOutPrice() != null) {
                loans = loans + Double.valueOf(coll.getLoanAmountGivenOutPrice());
            }
            if (coll.getOrderGivenOutPrice() != null) {
                orders = orders + Double.valueOf(coll.getOrderGivenOutPrice());
            }


        }


        int status[] = getApprovedCards(collections, "" + p.getPayoutnumber(), payoutsVewModel);
        p.setMilkTotal(String.valueOf(milk));

        p.setMilkTotalKsh(String.valueOf(milkKsh));
        p.setMilkTotalLtrs(String.valueOf(milkLtrs));

        p.setLoanTotal(String.valueOf(loans));
        p.setOrderTotal(String.valueOf(orders));
        p.setBalance(String.valueOf(milkKsh - (orders + loans)));
        p.setFarmersCount("" + payoutsVewModel.getFarmersCountByCycle("" + p.getCycleCode()));
        p.setApprovedCards("" + status[1]);
        p.setPendingCards("" + status[2]);


        return p;

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
        if (status >= collectsNo) {
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

            if (status == collectionNo) {
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
            String loan = loanModel.getLoanAmount();

            OrderModel orderModel = CommonFuncs.getOrderForDay(d.getDate(), collections);
            String order = orderModel.getOrderAmount();


            String collectionId = getCollectionId(d.getDate(), collections);

            dayCollectionModels.add(new DayCollectionModel(
                            payouts.getPayoutnumber(),
                            d.getDay(),
                            d.getDate(),
                            milkAm,
                            milkPm,
                    collectionId,
                    milkModelAm,
                    milkModelPm, loan, order,
                    loanModel.getCollectionId(),
                    loanModel, orderModel.getCollectionId(), orderModel, payouts.getStatus()

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

    public static PayoutFarmersCollectionModel getFarmersCollectionModel(FamerModel famerModel, List<Collection> collections, Payouts payouts) {
        String milkTotal = CommonFuncs.getMilk(famerModel.getCode(), collections).getUnitQty();
        String milkTotalKsh = CommonFuncs.getMilk(famerModel.getCode(), collections).getValueKsh();
        String milkTotalLtrs = CommonFuncs.getMilk(famerModel.getCode(), collections).getValueLtrs();
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
                payouts.getPayoutnumber(),
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
                p.getBalance(), p.getPayoutnumber(), famerModel.getCyclecode(),
                p.getMilkTotalKsh(), p.getMilkTotalLtrs(), p.getStartDate(), p.getEndDate()
        );
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

                            Double price = Double.valueOf(u.getUnitprice());
                            Double unitCapacity = Double.valueOf(u.getUnitcapacity()) / 1000;
                            Double total = (Double.valueOf(edtVL.getText().toString()) * unitCapacity) * price;
                            unitTotal.setText(String.valueOf(GeneralUtills.Companion.round(total, 2)));

                        }
                    } else {
                        unitTotal.setText("");
                    }
                }
            });
            tp = " Milk collection";


        txt.setText(" Editing " + tp + "  For  " + dayCollectionModel.getDate() + "  " + ti);


        try {
            if (!value.equals("0.0")) {
                edtVL.setText(value);
                edtVL.setSelection(value.length());
            }
        } catch (Exception nm) {
            nm.printStackTrace();
        }



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
                listener.updateCollection("0.0", adapterPosition, time, type, dayCollectionModel, alertDialogAndroid);
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


        edtAmount.setText(dayCollectionModel.getLoan());



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
            if (!TextUtils.isEmpty(edtAmount.getText().toString())) {

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


    public static void updateCollectionValue(String s, int time, int type, DayCollectionModel dayCollectionModel, PayoutsVewModel payoutsVewModel, Payouts payouts, FamerModel famerModel, LoanModel loanModel, OrderModel orderModel, CollectionCreateUpdateListener listener) {


        Collection collection;
        Collection ctoUpdate;


        if (dayCollectionModel.getCollectionId() != null) {
            collection = payoutsVewModel.getCollectionByIdOne(Integer.valueOf(dayCollectionModel.getCollectionId()));
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
            ((TextView) txtQty).setText(String.valueOf(vq));

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

    public static Payouts createPayout(Payouts payouts, PayoutsVewModel payoutsVewModel) {
        if (payouts != null) {


            List<Collection> c = payoutsVewModel.getCollectionByDateByPayoutListOne("" + payouts.getPayoutnumber());
            return CommonFuncs.createPayoutsByCollection(c, payouts, payoutsVewModel);


        }
        return null;
    }

    public static List<ReportListModel> generateReportListModel(List<Collection> collections, int type) {
        List<ReportListModel> reportListModels = new LinkedList<>();
        for (Collection c : collections) {

            if (type == 1) {
                reportListModels.add(new ReportListModel(c.getId(), c.getDayName(), c.getDayDate(), c.getMilkCollectedValueLtrsAm(), c.getMilkCollectedValueLtrsPm()));
            } else if (type == 2) {
                reportListModels.add(new ReportListModel(c.getId(), c.getDayName(), c.getDayDate(), null, c.getLoanAmountGivenOutPrice()));

            } else if (type == 3) {
                reportListModels.add(new ReportListModel(c.getId(), c.getDayName(), c.getDayDate(), null, c.getOrderGivenOutPrice()));

            }


        }
        return reportListModels;
    }

    public static List<ReportListModel> generateReportListModel(MonthsDates mds, List<Collection> collections, int type) {


        List<ReportListModel> reportListModels = new LinkedList<>();
        for (Collection c : collections) {
            if (DateTimeUtils.Companion.isInMonth(c.getDayDate(), mds.getMonthName())) {

                if (type == 1) {
                    reportListModels.add(new ReportListModel(c.getId(), c.getDayName(), c.getDayDate(), c.getMilkCollectedValueLtrsAm(), c.getMilkCollectedValueLtrsPm()));
                } else if (type == 2) {
                    reportListModels.add(new ReportListModel(c.getId(), c.getDayName(), c.getDayDate(), null, c.getLoanAmountGivenOutPrice()));

                } else if (type == 3) {
                    reportListModels.add(new ReportListModel(c.getId(), c.getDayName(), c.getDayDate(), null, c.getOrderGivenOutPrice()));

                }
            }


        }
        return reportListModels;


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




}
