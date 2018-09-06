package com.dev.lishabora.Views;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.DayCollectionModel;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.FarmerHistoryByDateModel;
import com.dev.lishabora.Models.LoanModel;
import com.dev.lishabora.Models.MilkModel;
import com.dev.lishabora.Models.MonthsDates;
import com.dev.lishabora.Models.OrderModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Models.UnitsModel;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

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

    public static MilkModel getMilk(String date, String ampm, List<Collection> collections) {
        double milkTotal = 0.0;
        MilkModel m = new MilkModel();

        if (collections != null) {
            for (Collection c : collections) {
                if (c.getDayDate().equals(date) && c.getTimeOfDay().equals(ampm)) {
                    try {
                        milkTotal += Double.valueOf(c.getMilkCollected());
                        Timber.tag("CollectionsVsDate").d(" Date : " + date + "  Time " + ampm + "\nColDate : " + c.getDayDate() + "  ColTime " + c.getTimeOfDay() + "\n Milk " + c.getMilkCollected());
                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }


                    MilkModel mm = new Gson().fromJson(c.getMilkDetails(), MilkModel.class);
                    if (mm != null) {
                        m = mm;
                    }
                }
            }
        }

        m.setUnitQty(String.valueOf(milkTotal));
        return m;


    }


    public static int getCollectionIdAm(String date, List<Collection> collections) {
        if (collections != null) {
            Timber.tag("collectisdid").d("Am Called  " + date
                    + "" + collections);

            for (Collection c : collections) {

                if (c.getDayDate().contains(date) && c.getTimeOfDay().equals("AM")) {
                    Timber.tag("collectisdid").d("AM " + c.getId());
                    return c.getId();
                }
            }

        }
        return 0;
    }

    public static int getCollectionIdPm(String date, List<Collection> collections) {
        if (collections != null) {
            Timber.tag("collectisdid").d("Pm Called ");

            for (Collection c : collections) {

                if (c.getDayDate().contains(date) && c.getTimeOfDay().equals("PM")) {
                    Timber.tag("collectisdid").d("PM " + c.getId());

                    return c.getId();
                }
            }

        }
        return 0;
    }


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
                    milkTotalQty += Double.valueOf(c.getMilkCollected());
                    milkTotalLtrs += Double.valueOf(c.getMilkCollectedValueLtrs());
                    milkTotalKsh += Double.valueOf(c.getMilkCollectedValueKsh());
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


    @NonNull
    public static String getBalance(String farmercode, List<Collection> collections) {
        double milkTotal = 0.0;
        double loanTotal = 0.0;
        double orderTotal = 0.0;

        for (Collection c : collections) {
            if (c.getFarmerCode().equals(farmercode)) {
                try {
                    milkTotal += Double.valueOf(c.getMilkCollectedValueKsh());
                    orderTotal += Double.valueOf(c.getOrderGivenOutPrice());
                    loanTotal += Double.valueOf(c.getLoanAmountGivenOutPrice());
                } catch (Exception nm) {
                    nm.printStackTrace();
                }
            }
        }

        return String.valueOf(milkTotal - (loanTotal + orderTotal));


    }

    @NonNull
    public static String getBalance(String milkTotal, String loanTotal, String orderTotal) {
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


    /**
     * @param s
     * @param time
     * @param type
     * @param dayCollectionModel
     * @param collectionExisting
     * @param payouts
     * @param famerModel
     * @return
     */
    @Nullable
    public static Collection updateCollection(String s, int time, int type, DayCollectionModel dayCollectionModel, Collection collectionExisting, Payouts payouts, FamerModel famerModel) {


        UnitsModel unitsModel = new UnitsModel();
        unitsModel.setCode(famerModel.getUnitcode());
        unitsModel.setUnitprice(famerModel.getUnitprice());
        unitsModel.setUnitcapacity(famerModel.getUnitcapacity());
        unitsModel.setUnit(famerModel.getUnitname());

        Collection collection = new Collection();

        if (time == 1) {


            if (dayCollectionModel.getCollectionIdAm() != 0) {
                collection = collectionExisting;


            } else {
                Collection c = new Collection();
                c.setCycleCode(payouts.getCycleCode());
                c.setFarmerCode(famerModel.getCode());
                c.setFarmerName(famerModel.getNames());
                c.setCycleId(payouts.getCycleCode());
                c.setDayName(dayCollectionModel.getDay());
                c.setDayDate(dayCollectionModel.getDate());
                c.setTimeOfDay("AM");

                c.setLoanAmountGivenOutPrice(dayCollectionModel.getLoanAm());
                c.setMilkCollected(dayCollectionModel.getMilkAm());
                c.setOrderGivenOutPrice(dayCollectionModel.getOrderAm());


                c.setLoanId("");
                c.setOrderId("");
                c.setSynced(0);
                c.setSynced(false);
                c.setApproved(0);

                c.setPayoutnumber(dayCollectionModel.getPayoutNumber());
                c.setCycleStartedOn(payouts.getStartDate());

                if (type == 1) {
                    MilkModel milkModel = dayCollectionModel.getMilkModelAm();
                    milkModel.setUnitsModel(unitsModel);
                    milkModel.setUnitQty(s);


                    c.setMilkCollected(s);
                    c.setMilkCollectedValueKsh(milkModel.getValueKsh());
                    c.setMilkCollectedValueLtrs(milkModel.getValueLtrs());
                    c.setMilkDetails(new Gson().toJson(milkModel));


                } else if (type == 2) {
                    LoanModel loanModel = dayCollectionModel.getLoanModelAm();
                    loanModel.setLoanAmount(s);


                    c.setLoanAmountGivenOutPrice(s);
                    c.setLoanDetails(new Gson().toJson(loanModel));


                } else if (type == 3) {
                    OrderModel orderModel = dayCollectionModel.getOrderModelAm();
                    orderModel.setOrderAmount(s);

                    c.setOrderGivenOutPrice(s);
                    c.setOrderDetails(new Gson().toJson(orderModel));

                }

//                payoutsVewModel.createCollections(c).observe(this, responseModel -> {
//                    if (responseModel != null) {
//                        a.dismiss();
//                        MyToast.toast(responseModel.getResultDescription(), getContext(), R.drawable.ic_launcher, Toast.LENGTH_LONG);
//                    }
//                });
//                a.dismiss();
                return c;


            }

        } else {
            if (dayCollectionModel.getCollectionIdPm() != 0) {
                collection = collectionExisting;
            } else {


                Collection c = new Collection();
                c.setCycleCode(famerModel.getCyclecode());
                c.setFarmerCode(famerModel.getCode());
                c.setFarmerName(famerModel.getNames());
                c.setCycleId(famerModel.getCode());
                c.setDayName(dayCollectionModel.getDay());
                c.setLoanAmountGivenOutPrice(dayCollectionModel.getLoanPm());
                c.setDayDate(dayCollectionModel.getDate());
                c.setTimeOfDay("PM");
                c.setMilkCollected(dayCollectionModel.getMilkPm());
                c.setOrderGivenOutPrice(dayCollectionModel.getOrderPm());

                c.setLoanId("");
                c.setOrderId("");
                c.setSynced(0);
                c.setSynced(false);
                c.setApproved(0);

                c.setPayoutnumber(dayCollectionModel.getPayoutNumber());
                c.setCycleStartedOn(payouts.getStartDate());


                if (type == 1) {
                    MilkModel milkModel = dayCollectionModel.getMilkModelPm();
                    milkModel.setUnitsModel(unitsModel);
                    milkModel.setUnitQty(s);


                    c.setMilkCollected(s);
                    c.setMilkCollectedValueKsh(milkModel.getValueKsh());
                    c.setMilkCollectedValueLtrs(milkModel.getValueLtrs());
                    c.setMilkDetails(new Gson().toJson(milkModel));


                } else if (type == 2) {
                    LoanModel loanModel = dayCollectionModel.getLoanModelPm();
                    loanModel.setLoanAmount(s);


                    c.setLoanAmountGivenOutPrice(s);
                    c.setLoanDetails(new Gson().toJson(loanModel));


                } else if (type == 3) {
                    OrderModel orderModel = dayCollectionModel.getOrderModelPm();
                    orderModel.setOrderAmount(s);

                    c.setOrderGivenOutPrice(s);
                    c.setOrderDetails(new Gson().toJson(orderModel));

                }


//                payoutsVewModel.createCollections(c).observe(this, responseModel -> {
//                    if (responseModel != null) {
//                        a.dismiss();
//                        MyToast.toast(responseModel.getResultDescription(), getContext(), R.drawable.ic_launcher, Toast.LENGTH_LONG);
//                    }
//                });
//                a.dismiss();
                return c;
            }

        }


        if (collection != null) {
            if (type == 1) {
                MilkModel milkModel;
                milkModel = time == 1 ? dayCollectionModel.getMilkModelAm() : dayCollectionModel.getMilkModelPm();
                milkModel.setUnitQty(s);
                collection.setMilkCollected(s);
                collection.setMilkCollectedValueKsh(milkModel.getValueKsh());
                collection.setMilkCollectedValueLtrs(milkModel.getValueLtrs());
                collection.setMilkDetails(new Gson().toJson(milkModel));
                //payoutsVewModel.updateCollection(collection);
                // a.dismiss();
                return collection;
            } else if (type == 2) {
                LoanModel loanModel;
                loanModel = time == 1 ? dayCollectionModel.getLoanModelAm() : dayCollectionModel.getLoanModelPm();
                loanModel.setLoanAmount(s);
                collection.setLoanAmountGivenOutPrice(s);
                collection.setLoanDetails(new Gson().toJson(loanModel));
                // payoutsVewModel.updateCollection(collection);
                //a.dismiss();
                return collection;
            } else if (type == 3) {
                OrderModel orderModel;
                orderModel = time == 1 ? dayCollectionModel.getOrderModelAm() : dayCollectionModel.getOrderModelPm();
                orderModel.setOrderAmount(s);


                collection.setOrderGivenOutPrice(s);
                collection.setLoanDetails(new Gson().toJson(orderModel));


                return collection;
                //payoutsVewModel.updateCollection(collection);
                //a.dismiss();
            }
        } else {
            Timber.tag("collectionche0").d("Our coll is null" + dayCollectionModel.getCollectionIdAm() + "  " + dayCollectionModel.getCollectionIdPm());
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
                milk = milk + Double.valueOf(collection.getMilkCollectedValueLtrs());
                loan = loan + Double.valueOf(collection.getLoanAmountGivenOutPrice());
                order = order + Double.valueOf(collection.getOrderGivenOutPrice());
            }

        }
        double[] totals = {milk, loan, order};


        return new String[]{String.valueOf(totals[0]), String.valueOf(totals[1]), String.valueOf(totals[2]), cycleCode};
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

    public static Payouts createPayoutsByCollection(List<Collection> collections, Payouts p, PayoutsVewModel payoutsVewModel) {

        double total = 0.0;
        double milk = 0.0;
        double loans = 0.0;
        double orders = 0.0;

        double milkLtrs = 0.0;
        double milkKsh = 0.0;
        for (Collection coll : collections) {

            milk = milk + Double.valueOf(coll.getMilkCollected());
            milkLtrs = milkLtrs + Double.valueOf(coll.getMilkCollectedValueLtrs());
            milkKsh = milkKsh + Double.valueOf(coll.getMilkCollectedValueKsh());
            loans = loans + Double.valueOf(coll.getLoanAmountGivenOutPrice());
            orders = orders + Double.valueOf(coll.getOrderGivenOutPrice());


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

    public static int[] getApprovedCards(List<Collection> collections, String pcode, PayoutsVewModel payoutsVewModel) {

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


}
