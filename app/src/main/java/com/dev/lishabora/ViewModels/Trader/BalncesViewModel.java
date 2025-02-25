package com.dev.lishabora.ViewModels.Trader;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dev.lishabora.AppConstants;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.FarmerBalance;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Models.ProductsModel;
import com.dev.lishabora.Models.RoutesModel;
import com.dev.lishabora.Models.SyncModel;
import com.dev.lishabora.Models.Trader.FarmerLoansTable;
import com.dev.lishabora.Models.Trader.FarmerOrdersTable;
import com.dev.lishabora.Models.Trader.LoanPayments;
import com.dev.lishabora.Models.Trader.OrderPayments;
import com.dev.lishabora.Models.Trader.TraderModel;
import com.dev.lishabora.Repos.Trader.BalanceRepo;
import com.dev.lishabora.Repos.Trader.LoanPaymentsRepo;
import com.dev.lishabora.Repos.Trader.LoansTableRepo;
import com.dev.lishabora.Repos.Trader.OrderPaymentsRepo;
import com.dev.lishabora.Repos.Trader.OrdersTableRepo;
import com.dev.lishabora.Repos.Trader.SyncRepo;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class BalncesViewModel extends AndroidViewModel

{

    BalanceRepo balanceRepo;
    LoansTableRepo loansTableRepo;
    LoanPaymentsRepo loanPaymentsRepo;
    OrderPaymentsRepo orderPaymentsRepo;
    OrdersTableRepo ordersTableRepo;
    SyncRepo syncRepo;


    private Application application;
    private PrefrenceManager prefrenceManager;


    public BalncesViewModel(@NonNull Application application) {
        super(application);
        this.application = application;


        balanceRepo = new BalanceRepo(application);
        loansTableRepo = new LoansTableRepo(application);
        ordersTableRepo = new OrdersTableRepo(application);

        loanPaymentsRepo = new LoanPaymentsRepo(application);
        orderPaymentsRepo = new OrderPaymentsRepo(application);
        syncRepo = new SyncRepo(application);
        prefrenceManager = new PrefrenceManager(application);


    }

    public LiveData<List<SyncModel>> fetchAllSync() {
        return syncRepo.fetchAllData(false);
    }

    public LiveData<List<SyncModel>> fetchByStatus(int status) {
        return syncRepo.getAllByStatus(status);
    }

    public LiveData<SyncModel> fetchByCode(int id) {
        return syncRepo.getSynce(id);
    }

    public void createSync(SyncModel model) {
        syncRepo.insert(model);

    }

    public void updateSync(SyncModel syncModel) {
        syncRepo.upDateRecord(syncModel);

    }

    public void deleteSync(SyncModel syncModel) {
        syncRepo.deleteRecord(syncModel);

    }


//    public void synch(int action, int entity, Object o) {
//        SyncModel syncModel = new SyncModel();
//        syncModel.setActionType(action);
//        syncModel.setObjectData(o);
//        //syncModel.setObject(new Gson().toJson(o));
//        syncModel.setEntityType(entity);
//        syncModel.setSyncStatus(0);
//        syncModel.setTimeStamp(DateTimeUtils.Companion.getNow());
//        syncModel.setSyncTime("");
//        syncModel.setTraderCode(prefrenceManager.getTraderModel().getCode());
//        switch (action) {
//            case AppConstants.INSERT:
//                syncModel.setActionTypeName("Insert");
//                break;
//            case AppConstants.UPDATE:
//                syncModel.setActionTypeName("Update");
//                break;
//            case AppConstants.DELETE:
//                syncModel.setActionTypeName("Delete");
//                break;
//
//        }
//        switch (entity) {
//            case AppConstants.ENTITY_FARMER:
//                syncModel.setEntityTypeName("Farmer");
//                syncModel.setObject(new Gson().toJson(o, FamerModel.class));
//
//                break;
//            case AppConstants.ENTITY_PRODUCTS:
//                syncModel.setEntityTypeName("Products");
//                // syncModel.setObject(new Gson().toJson(o, ProductsModel.class));
//
//                break;
//            case AppConstants.ENTITY_PAYOUTS:
//                syncModel.setEntityTypeName("Payout");
//                syncModel.setObject(new Gson().toJson(o, Payouts.class));
//
//                break;
//            case AppConstants.ENTITY_COLLECTION:
//                syncModel.setEntityTypeName("Collection");
//                syncModel.setObject(new Gson().toJson(o, Collection.class));
//
//                break;
//            case AppConstants.ENTITY_ROUTES:
//                syncModel.setEntityTypeName("Route");
//                syncModel.setObject(new Gson().toJson(o, RoutesModel.class));
//
//                break;
//            case AppConstants.ENTITY_TRADER:
//
//                syncModel.setEntityTypeName("Trader");
//                syncModel.setObject(new Gson().toJson(o, TraderModel.class));
//
//                break;
//        }
//
//        createSync(syncModel);
//    }


    public void synch(int action, int entity, Object o, List<ProductsModel> objects, int type) {
        Gson gson = new Gson();
        SyncModel syncModel = new SyncModel();
        syncModel.setActionType(action);
        syncModel.setObjectData(o);
        //syncModel.setObject(new Gson().toJson(o));
        syncModel.setDataType(type);
        syncModel.setEntityType(entity);
        syncModel.setSyncStatus("");
        syncModel.setTimeStamp(DateTimeUtils.Companion.getNow());
        syncModel.setSyncTime("");
        syncModel.setTraderCode(prefrenceManager.getTraderModel().getCode());
        switch (action) {
            case AppConstants.INSERT:
                syncModel.setActionTypeName("Insert");

                break;
            case AppConstants.UPDATE:
                syncModel.setActionTypeName("Update");
                break;
            case AppConstants.DELETE:
                syncModel.setActionTypeName("Delete");
                break;

        }
        switch (entity) {
            case AppConstants.ENTITY_FARMER:
                syncModel.setEntityTypeName("Farmer");

                syncModel.setObject(new Gson().toJson(o, FamerModel.class));
                //syncModel.setObjectData(o);

                break;
            case AppConstants.ENTITY_PRODUCTS:
                syncModel.setEntityTypeName("Products");

                if (type == 1) {
                    syncModel.setObject(new Gson().toJson(o, ProductsModel.class));
                    // syncModel.setObjectData(o);

                } else {
                    JsonArray jsonArray = gson.toJsonTree(objects).getAsJsonArray();
                    Type listType = new TypeToken<LinkedList<ProductsModel>>() {
                    }.getType();


                    syncModel.setObjects(gson.toJson(jsonArray));


                }

                break;
            case AppConstants.ENTITY_PAYOUTS:
                syncModel.setEntityTypeName("Payout");
                syncModel.setObject(new Gson().toJson(o, Payouts.class));

                break;
            case AppConstants.ENTITY_COLLECTION:
                syncModel.setEntityTypeName("Collection");
                syncModel.setObject(new Gson().toJson(o, Collection.class));

                break;
            case AppConstants.ENTITY_ROUTES:
                syncModel.setEntityTypeName("Route");
                syncModel.setObject(new Gson().toJson(o, RoutesModel.class));

                break;
            case AppConstants.ENTITY_TRADER:

                syncModel.setEntityTypeName("Trader");
                syncModel.setObject(new Gson().toJson(o, TraderModel.class));

                break;

            case AppConstants.ENTITY_LOANS:

                syncModel.setEntityTypeName("Loans");
                syncModel.setObject(new Gson().toJson(o, FarmerLoansTable.class));

                break;


            case AppConstants.ENTITY_ORDERS:

                syncModel.setEntityTypeName("Order");
                syncModel.setObject(new Gson().toJson(o, FarmerOrdersTable.class));

                break;

            case AppConstants.ENTITY_BALANCES:

                syncModel.setEntityTypeName("Balance");
                syncModel.setObject(new Gson().toJson(o, FarmerBalance.class));

                break;

            case AppConstants.ENTITY_LOAN_PAYMNENTS:

                syncModel.setEntityTypeName("Loan Payments");
                syncModel.setObject(new Gson().toJson(o, LoanPayments.class));

                break;

            case AppConstants.ENTITY_ORDER_PAYMENTS:

                syncModel.setEntityTypeName("Order Payment");
                syncModel.setObject(new Gson().toJson(o, OrderPayments.class));

                break;
        }


        createSync(syncModel);
    }


    /****LOANS TABLE *****/

    public LiveData<List<FarmerLoansTable>> fetchAllLoans() {
        return loansTableRepo.fetchAll();
    }

    public void insertMultipleLoans(List<FarmerLoansTable> farmerLoansTables) {
        loansTableRepo.insertMultiple(farmerLoansTables);
    }

    public void insertLoan(FarmerLoansTable farmerLoansTable) {
        loansTableRepo.insert(farmerLoansTable);
        farmerLoansTable.setTraderCode(prefrenceManager.getTraderModel().getCode());
        synch(AppConstants.INSERT, AppConstants.ENTITY_LOANS, farmerLoansTable, null, 1);
    }

    public void insertLoanDirect(FarmerLoansTable farmerLoansTable) {
        loansTableRepo.insertDirect(farmerLoansTable);
        farmerLoansTable.setTraderCode(prefrenceManager.getTraderModel().getCode());
        synch(AppConstants.INSERT, AppConstants.ENTITY_LOANS, farmerLoansTable, null, 1);
    }
    public LiveData<FarmerLoansTable> getFarmerLoanByCode(String code) {
        return loansTableRepo.getFarmerLoanByCode(code);
    }

    public FarmerLoansTable getFarmerLoanByCodeOne(String code) {
        return loansTableRepo.getFarmerLoanByCodeOne(code);
    }

    public LiveData<List<FarmerLoansTable>> getFarmerLoanByDate(String date) {
        return getFarmerLoanByDate(date);
    }

    public LiveData<List<FarmerLoansTable>> getFarmerLoanByPayoutCode(String payoutcode) {
        return loansTableRepo.getFarmerLoanByPayoutCode(payoutcode);
    }

    public List<FarmerLoansTable> getFarmerLoanByPayoutCodeOne(String payoutcode) {
        return loansTableRepo.getFarmerLoanByPayoutCodeOne(payoutcode);
    }

    public FarmerLoansTable getFarmerLoanByCollectionOne(String collCode) {
        return loansTableRepo.getFarmerLoanByCollectionOne(collCode);
    }

    public LiveData<FarmerLoansTable> getFarmerLoanByCollection(String collCode) {
        return loansTableRepo.getFarmerLoanByCollection(collCode);
    }

    public LiveData<List<FarmerLoansTable>> getFarmerLoanByPayoutCodeByFarmer(String payoutCode, String farmer) {
        return loansTableRepo.getFarmerLoanByPayoutCodeByFarmer(payoutCode, farmer);
    }

    public List<FarmerLoansTable> getFarmerLoanByPayoutCodeByFarmerOne(String payoutCode, String farmer) {
        return loansTableRepo.getFarmerLoanByPayoutCodeByFarmerOne(payoutCode, farmer);
    }


    public List<FarmerLoansTable> getFarmerLoanByPayoutNumberByFarmerByStatus(String payoutNo, String farmer, int status) {
        return loansTableRepo.getFarmerLoanByPayoutCodeByFarmerByStatus(payoutNo, farmer, status);
    }

    public List<FarmerLoansTable> getFarmerLoanByPayoutNumberByFarmerByStatus(String farmer, int status) {
        return loansTableRepo.getFarmerLoanByPayoutCodeByFarmerByStatus(farmer, status);
    }

    public LiveData<List<FarmerLoansTable>> getFarmerLoanByFarmerByStatus(String farmer, int status) {
        return loansTableRepo.getFarmerLoanByFarmerByStatus(farmer, status);
    }

    public LiveData<List<FarmerLoansTable>> getFarmerLoans() {
        return loansTableRepo.fetchAll();
    }


    public LiveData<List<FarmerLoansTable>> getFarmerLoanByFarmer(String farmer) {
        return loansTableRepo.getFarmerLoanByFarmer(farmer);
    }

    public List<FarmerLoansTable> getFarmerLoanByFarmerOne(String farmer) {
        return loansTableRepo.getFarmerLoanByFarmerOne(farmer);
    }

    public List<FarmerLoansTable> getFarmerLoanByPayoutNumberListOne(String payoutNo) {
        return loansTableRepo.getFarmerLoanByPayoutCodeListOne(payoutNo);
    }

    public void approveFarmersoan(int a, String farmercode, String payoutNo) {
        loansTableRepo.approveFarmersPayoutCard(a, farmercode, payoutNo);
    }

    public void updateRecordLoan(FarmerLoansTable farmerLoansTable) {
        loansTableRepo.updateRecord(farmerLoansTable);
        farmerLoansTable.setTraderCode(prefrenceManager.getTraderModel().getCode());
        synch(AppConstants.UPDATE, AppConstants.ENTITY_LOANS, farmerLoansTable, null, 1);

    }

    public void deleteRecordLoan(FarmerLoansTable farmerLoansTable) {
        loansTableRepo.deleteRecord(farmerLoansTable);
        farmerLoansTable.setTraderCode(prefrenceManager.getTraderModel().getCode());
        synch(AppConstants.DELETE, AppConstants.ENTITY_LOANS, farmerLoansTable, null, 1);

    }

    public void updateRecordLoanDirect(FarmerLoansTable farmerLoansTable) {
        loansTableRepo.updateRecordDirect(farmerLoansTable);
        farmerLoansTable.setTraderCode(prefrenceManager.getTraderModel().getCode());
        synch(AppConstants.UPDATE, AppConstants.ENTITY_LOANS, farmerLoansTable, null, 1);

    }

    public void deleteRecordLoanDirect(FarmerLoansTable farmerLoansTable) {
        loansTableRepo.deleteRecordDirect(farmerLoansTable);
        farmerLoansTable.setTraderCode(prefrenceManager.getTraderModel().getCode());
        synch(AppConstants.DELETE, AppConstants.ENTITY_LOANS, farmerLoansTable, null, 1);

    }

    public double getBalanceLoanByFarmerCode(String farmercode) {
        return loansTableRepo.getBalanceByFarmerCode(farmercode);
    }

    public double getBalanceLoanByPayout(String payoutNo) {
        return loansTableRepo.getBalanceByPayout(payoutNo);
    }

    public double getLoanInstallmentSumByPayoutCode(String payoutNo) {
        return loansTableRepo.getInstallmentSumByPayoutCode(payoutNo);
    }

    public double getLoanInstallmentSumByFarmerCode(String code) {
        return loansTableRepo.getInstallmentSumByFarmerCode(code);
    }

    public List<FarmerLoansTable> getFarmerLoanByFarmerByDate(String code, String date) {
        return loansTableRepo.getFarmerLoanByFarmerByDate(code, date);
    }

    public FarmerLoansTable getFarmerLoanByDateByFarmerByTimeSingle(String code, String today) {
        return loansTableRepo.getFarmerLoanByDateByFarmerByTimeSingle(code, today);

    }

    public FarmerLoansTable getLastLoan() {
        return loansTableRepo.getLast();
    }

    public void updateStatusLoan(String code, int status) {
        loansTableRepo.updateStatus(code, status);
    }

    public LiveData<List<FarmerLoansTable>> getFarmerLoansBetweenDates(Long date1, Long date2) {
        return loansTableRepo.getFarmerLoansBetweenDates(date1, date2);

    }

    public LiveData<List<FarmerLoansTable>> getFarmerLoansBetweenDates(Long date1, Long date2, String code) {
        return loansTableRepo.getFarmerLoansBetweenDates(date1, date2, code);
    }


    /******ORDERS TABLES*******/

    public LiveData<List<FarmerOrdersTable>> getFarmerOrders() {
        return ordersTableRepo.fetchAll();
    }

    public void insertMultipleOrders(List<FarmerOrdersTable> farmerOrdersTables) {
        ordersTableRepo.insertMultiple(farmerOrdersTables);
    }

    public void insertOrder(FarmerOrdersTable farmerOrdersTable) {
        ordersTableRepo.insert(farmerOrdersTable);
        farmerOrdersTable.setTraderCode(prefrenceManager.getTraderModel().getCode());

        synch(AppConstants.INSERT, AppConstants.ENTITY_ORDERS, farmerOrdersTable, null, 1);

    }

    public void insertOrderDirect(FarmerOrdersTable farmerOrdersTable) {
        ordersTableRepo.insertDirect(farmerOrdersTable);
        farmerOrdersTable.setTraderCode(prefrenceManager.getTraderModel().getCode());

        synch(AppConstants.INSERT, AppConstants.ENTITY_ORDERS, farmerOrdersTable, null, 1);

    }

    public LiveData<List<FarmerOrdersTable>> fetchAllOrders() {
        return ordersTableRepo.fetchAll();
    }

    public LiveData<FarmerOrdersTable> getFarmerOrderByCode(String code) {
        return ordersTableRepo.getFarmerOrderByCode(code);
    }

    public FarmerOrdersTable getFarmerOrderByCodeOne(String code) {
        return ordersTableRepo.getFarmerOrderByCodeOne(code);
    }

    public LiveData<List<FarmerOrdersTable>> getFarmerOrderByDate(String date) {
        return getFarmerOrderByDate(date);
    }

    public LiveData<List<FarmerOrdersTable>> getFarmerOrderByPayoutCode(String payoutCode) {
        return ordersTableRepo.getFarmerOrderByPayoutCode(payoutCode);
    }

    public List<FarmerOrdersTable> getFarmerOrderByPayoutCodeOne(String payoutCode) {
        return ordersTableRepo.getFarmerOrderByPayoutCodeOne(payoutCode);
    }

    public FarmerOrdersTable getFarmerOrderByCollectionOne(String collCode) {
        return ordersTableRepo.getFarmerOrderByCollection(collCode);
    }

    public LiveData<FarmerOrdersTable> getFarmerOrderByCollection(String collCode) {
        return ordersTableRepo.getFarmerOrderByCollectionLive(collCode);
    }

    public LiveData<List<FarmerOrdersTable>> getFarmerOrderByPayoutCodeByFarmer(String payoutCode, String farmer) {
        return ordersTableRepo.getFarmerOrderByPayoutCodeByFarmer(payoutCode, farmer);
    }

    public List<FarmerOrdersTable> getFarmerOrderByPayoutCodeByFarmerOne(String payoutCode, String farmer) {
        return ordersTableRepo.getFarmerOrderByPayoutCodeByFarmerOne(payoutCode, farmer);
    }

    public List<FarmerOrdersTable> getFarmerOrderByPayoutNumberByFarmerByStatus(String payoutNo, String farmer, int status) {
        return ordersTableRepo.getFarmerOrderByPayoutCodeByFarmerByStatus(payoutNo, farmer, status);
    }

    public List<FarmerOrdersTable> getFarmerOrderByPayoutNumberByFarmerByStatus(String farmer, int status) {
        return ordersTableRepo.getFarmerOrderByPayoutCodeByFarmerByStatus(farmer, status);
    }

    public LiveData<List<FarmerOrdersTable>> getFarmerOrderByFarmerByStatus(String farmer, int status) {
        return ordersTableRepo.getFarmerOrderByFarmerByStatus(farmer, status);
    }


    public LiveData<List<FarmerOrdersTable>> getFarmerOrderByFarmer(String farmer) {
        return ordersTableRepo.getFarmerOrderByFarmer(farmer);
    }

    public List<FarmerOrdersTable> getFarmerOrderByFarmerOne(String farmer) {
        return ordersTableRepo.getFarmerOrderByFarmerOne(farmer);
    }

    public List<FarmerOrdersTable> getFarmerOrderByPayoutCodeListOne(String payoutNo) {
        return ordersTableRepo.getFarmerOrderByPayoutCodeListOne(payoutNo);
    }

    public void approveFarmersOrder(int a, String farmercode, String payoutNo) {
        ordersTableRepo.approveFarmersPayoutCard(a, farmercode, payoutNo);
    }

    public void updateRecord(FarmerOrdersTable farmerOrdersTable) {
        ordersTableRepo.updateRecord(farmerOrdersTable);
        farmerOrdersTable.setTraderCode(prefrenceManager.getTraderModel().getCode());

        synch(AppConstants.UPDATE, AppConstants.ENTITY_ORDERS, farmerOrdersTable, null, 1);

    }

    public void deleteRecord(FarmerOrdersTable farmerOrdersTable) {
        ordersTableRepo.deleteRecord(farmerOrdersTable);
        farmerOrdersTable.setTraderCode(prefrenceManager.getTraderModel().getCode());

        synch(AppConstants.DELETE, AppConstants.ENTITY_ORDERS, farmerOrdersTable, null, 1);

    }

    public void updateRecordDirect(FarmerOrdersTable farmerOrdersTable) {
        ordersTableRepo.updateRecordDirect(farmerOrdersTable);
        farmerOrdersTable.setTraderCode(prefrenceManager.getTraderModel().getCode());

        synch(AppConstants.UPDATE, AppConstants.ENTITY_ORDERS, farmerOrdersTable, null, 1);

    }

    public void deleteRecordDirect(FarmerOrdersTable farmerOrdersTable) {
        ordersTableRepo.deleteRecordDirect(farmerOrdersTable);
        farmerOrdersTable.setTraderCode(prefrenceManager.getTraderModel().getCode());

        synch(AppConstants.DELETE, AppConstants.ENTITY_ORDERS, farmerOrdersTable, null, 1);

    }

    public double getBalanceOrderByFarmerCode(String farmercode) {
        return ordersTableRepo.getBalanceByFarmerCode(farmercode);
    }

    public double getBalanceOrderByPayout(String payoutNo) {
        return ordersTableRepo.getBalanceByPayout(payoutNo);
    }

    public double getOrderInstallmentSumByPayoutCode(String payoutNo) {
        return ordersTableRepo.getInstallmentSumByPayoutCode(payoutNo);
    }

    public double getOrderInstallmentSumByFarmerCode(String code) {
        return ordersTableRepo.getInstallmentSumByFarmerCode(code);
    }

    public List<FarmerOrdersTable> getFarmerOrderByFarmerByDate(String code, String date) {
        return ordersTableRepo.getFarmerOrderByFarmerByDate(code, date);
    }

    public FarmerOrdersTable getFarmerOrderByDateByFarmerByTimeSingle(String code, String today) {
        return ordersTableRepo.getFarmerOrderByDateByFarmerByTimeSingle(code, today);

    }

    public FarmerOrdersTable getLastOrder() {
        return ordersTableRepo.getLast();
    }

    public void updateStatusOrder(String code, int status) {
        ordersTableRepo.updateStatus(code, status);
    }

    public LiveData<List<FarmerOrdersTable>> getFarmerOrdersBetweenDates(Long date1, Long date2) {
        return ordersTableRepo.getFarmerOrdersBetweenDates(date1, date2);

    }

    public LiveData<List<FarmerOrdersTable>> getFarmerOrdersBetweenDates(Long date1, Long date2, String code) {
        return ordersTableRepo.getFarmerOrdersBetweenDates(date1, date2, code);
    }


    /********LOAN PAYMENTS TABLE ****/

    public void insertMultipleLoanPayments(List<LoanPayments> loanPayments) {
        loanPaymentsRepo.insertMultiple(loanPayments);

    }

    public void insertSingleLoanPayment(LoanPayments loanPayments) {
        loanPaymentsRepo.insertSingle(loanPayments);
        loanPayments.setTraderCode(prefrenceManager.getTraderModel().getCode());

        synch(AppConstants.INSERT, AppConstants.ENTITY_LOAN_PAYMNENTS, loanPayments, null, 1);

    }

    public void insertSingleLoanPaymentDirect(LoanPayments loanPayments) {
        loanPaymentsRepo.insertSingleLoanPaymentDirect(loanPayments);
        loanPayments.setTraderCode(prefrenceManager.getTraderModel().getCode());

        synch(AppConstants.INSERT, AppConstants.ENTITY_LOAN_PAYMNENTS, loanPayments, null, 1);

    }


    public LiveData<List<LoanPayments>> fetchAllDataLoanPayments() {
        return loanPaymentsRepo.fetchAllData();
    }

    public LiveData<LoanPayments> getLoanPaymentByCode(String code) {
        return loanPaymentsRepo.getLoanPaymentByCode(code);
    }


    public LoanPayments getLoanPaymentByCodeOne(String code) {
        return loanPaymentsRepo.getLoanPaymentByCodeOne(code);
    }


    public LiveData<List<LoanPayments>> getLoanPaymentByDate(String date) {
        return loanPaymentsRepo.getLoanPaymentByDate(date);
    }

    public LiveData<List<LoanPayments>> getLoanPaymentByLoanCode(String loanCode) {
        return loanPaymentsRepo.getLoanPaymentByLoanCode(loanCode);
    }

    public List<LoanPayments> getLoanPaymentByLoanCodeOne(String loanCode) {
        return loanPaymentsRepo.getLoanPaymentByLoanCodeoOne(loanCode);
    }

    public LiveData<List<LoanPayments>> getLoanPaymentByPaout(String payoutNo) {
        return loanPaymentsRepo.getLoanPaymentByPaout(payoutNo);
    }


    public LiveData<List<LoanPayments>> getLoanPaymentByPayment(String payment) {
        return loanPaymentsRepo.getLoanPaymentByPayment(payment);
    }

    public LiveData<List<LoanPayments>> getLoanPaymentByRefNo(String refNo) {
        return loanPaymentsRepo.getLoanPaymentByRefNo(refNo);
    }


    public List<LoanPayments> getLoanPaymentByPayoutNumberListOne(String payoutnumber) {
        return loanPaymentsRepo.getLoanPaymentByPayoutNumberListOne(payoutnumber);
    }


    public void updateRecordLoanPayment(LoanPayments loanPayment) {
        loanPaymentsRepo.updateRecord(loanPayment);
        loanPayment.setTraderCode(prefrenceManager.getTraderModel().getCode());

        synch(AppConstants.UPDATE, AppConstants.ENTITY_LOAN_PAYMNENTS, loanPayment, null, 1);

    }

    public void deleteRecordLoanPayment(LoanPayments loanPayment) {
        loanPaymentsRepo.deleteRecord(loanPayment);
        loanPayment.setTraderCode(prefrenceManager.getTraderModel().getCode());

        synch(AppConstants.DELETE, AppConstants.ENTITY_LOAN_PAYMNENTS, loanPayment, null, 1);

    }

    public double getSumPaidLoanPayment(String loanCode) {
        return loanPaymentsRepo.getSumPaid(loanCode);
    }


    public double getSumPaidByPayoutLoanPayment(String payoutid) {
        return loanPaymentsRepo.getSumPaidByPayout(payoutid);
    }


    public LoanPayments getLoanPaymentByDateByFarmerByTimeSingle(String loanCode, String today) {
        return loanPaymentsRepo.getLoanPaymentByDateByFarmerByTimeSingle(loanCode, today);
    }


    public LoanPayments getLastLoanPayment() {
        return loanPaymentsRepo.getLast();
    }


    public LiveData<List<LoanPayments>> getLoanPaymentsBetweenDates(Long date1, Long date2) {
        return loanPaymentsRepo.getLoanPaymentsBetweenDates(date1, date2);
    }


    public LiveData<List<LoanPayments>> getLoanPaymentsBetweenDates(Long date1, Long date2, String loanCode) {
        return loanPaymentsRepo.getLoanPaymentsBetweenDates(date1, date2, loanCode);
    }

    public List<LoanPayments> getLoanPaymentsByLoanIdBetweenDatesorByPayoutCode(Long date1, Long date2, String loanCode, String payoutCode) {
        return loanPaymentsRepo.getLoanPaymentsByLoanIdBetweenDatesorByPayoutCode(date1, date2, loanCode, payoutCode);
    }


    /*******ORDER PAYMENTS *****/

    public void insertMultipleOrderPayments(List<OrderPayments> orderPayments) {
        orderPaymentsRepo.insertMultiple(orderPayments);
    }

    public void insertSingleOrderPayment(OrderPayments orderPayments) {
        orderPaymentsRepo.insertSingle(orderPayments);
        orderPayments.setTraderCode(prefrenceManager.getTraderModel().getCode());

        synch(AppConstants.INSERT, AppConstants.ENTITY_ORDER_PAYMENTS, orderPayments, null, 1);

    }


    public LiveData<List<OrderPayments>> fetchAllDataOrderPayment() {
        return orderPaymentsRepo.fetchAllData();
    }

    public LiveData<OrderPayments> getOrderPaymentByCode(String keyid) {
        return orderPaymentsRepo.getOrderPaymentByCode(keyid);
    }


    public OrderPayments getOrderPaymentByCodeOne(String code) {
        return orderPaymentsRepo.getOrderPaymentByCodeOne(code);
    }


    public LiveData<List<OrderPayments>> getOrderPaymentByDate(String date) {
        return orderPaymentsRepo.getOrderPaymentByDate(date);
    }

    public LiveData<List<OrderPayments>> getOrderPaymentByOrderCode(String orderCode) {
        return orderPaymentsRepo.getOrderPaymentByOrderCode(orderCode);
    }

    public List<OrderPayments> getOrderPaymentByOrderCodeOne(String orderCode) {
        return orderPaymentsRepo.getOrderPaymentByOrderCodeOne(orderCode);
    }


    public LiveData<List<OrderPayments>> getOrderPaymentByPaout(String payoutNo) {
        return orderPaymentsRepo.getOrderPaymentByPaout(payoutNo);
    }


    public LiveData<List<OrderPayments>> getOrderPaymentByPayment(String payment) {
        return orderPaymentsRepo.getOrderPaymentByPayment(payment);
    }

    public LiveData<List<OrderPayments>> getOrderPaymentByRefNo(String refNo) {
        return orderPaymentsRepo.getOrderPaymentByRefNo(refNo);
    }


    public List<OrderPayments> getOrderPaymentByPayoutNumberListOne(String payoutnumber) {
        return orderPaymentsRepo.getOrderPaymentByPayoutNumberListOne(payoutnumber);
    }


    public void updateRecordOrderPayment(OrderPayments orderPayments) {
        orderPaymentsRepo.updateRecord(orderPayments);
        orderPayments.setTraderCode(prefrenceManager.getTraderModel().getCode());

        synch(AppConstants.UPDATE, AppConstants.ENTITY_ORDER_PAYMENTS, orderPayments, null, 1);

    }

    public void deleteRecordOrderPayment(OrderPayments orderPayments) {
        orderPaymentsRepo.deleteRecord(orderPayments);
        orderPayments.setTraderCode(prefrenceManager.getTraderModel().getCode());

        synch(AppConstants.DELETE, AppConstants.ENTITY_ORDER_PAYMENTS, orderPayments, null, 1);

    }

    public double getSumPaidOrderPayment(String loanCode) {
        return orderPaymentsRepo.getSumPaid(loanCode);
    }


    public double getSumPaidByPayoutOrderPayment(String payoutCode) {
        return orderPaymentsRepo.getSumPaidByPayout(payoutCode);
    }


    public OrderPayments getOrderPaymentByDateByFarmerByTimeSingle(String loanCode, String today) {
        return orderPaymentsRepo.getOrderPaymentByDateByFarmerByTimeSingle(loanCode, today);
    }


    public OrderPayments getLastOrderPaymentt() {
        return orderPaymentsRepo.getLast();
    }


    public LiveData<List<OrderPayments>> getOrderPaymentsBetweenDates(Long date1, Long date2) {
        return orderPaymentsRepo.getOrderPaymentsBetweenDates(date1, date2);
    }


    public LiveData<List<OrderPayments>> getOrderPaymentsBetweenDates(Long date1, Long date2, String loanCode) {
        return orderPaymentsRepo.getOrderPaymentsBetweenDates(date1, date2, loanCode);
    }

    public List<OrderPayments> getOrderPaymentsByLoanIdBetweenDatesorByPayoutCode(Long date1, Long date2, String orderCODE, String payoutCode) {
        return orderPaymentsRepo.getOrderPaymentsByOrderIdBetweenDatesorByPayoutCode(date1, date2, orderCODE, payoutCode);
    }


    /****** BALANCES ******/

    public void insertMultiple(List<FarmerBalance> farmerBalance) {
        balanceRepo.insertMultiple(farmerBalance);
    }

    public void insert(FarmerBalance farmerBalance) {
        balanceRepo.insert(farmerBalance);
        farmerBalance.setTraderCode(prefrenceManager.getTraderModel().getCode());

        synch(AppConstants.INSERT, AppConstants.ENTITY_BALANCES, farmerBalance, null, 1);

    }

    public void insertDirect(FarmerBalance farmerBalance) {
        balanceRepo.insertDirect(farmerBalance);
        farmerBalance.setTraderCode(prefrenceManager.getTraderModel().getCode());

        synch(AppConstants.INSERT, AppConstants.ENTITY_BALANCES, farmerBalance, null, 1);

    }


    public LiveData<List<FarmerBalance>> fetchAll() {
        return balanceRepo.fetchAll();
    }

    public LiveData<FarmerBalance> getByKeyCode(String code) {
        return balanceRepo.getByKeyCode(code);
    }


    public LiveData<List<FarmerBalance>> getByFarmerCode(String code) {
        return balanceRepo.getByFarmerCode(code);
    }

    public LiveData<FarmerBalance> getByFarmerCodeByPayout(String code, String payoutCode) {
        return balanceRepo.getByFarmerCodeByPayout(code, payoutCode);
    }


    public List<FarmerBalance> getByFarmerCodeOne(String code) {
        return balanceRepo.getByFarmerCodeOne(code);
    }

    public List<FarmerBalance> getByFarmerCodeByStatusOne(String code) {
        return balanceRepo.getByFarmerCodeOne(code);
    }

    public FarmerBalance getByFarmerCodeByPayoutOne(String code, String payoutCode) {
        return balanceRepo.getByFarmerCodeByPayoutOne(code, payoutCode);
    }

    public void updateRecord(FarmerBalance farmerBalance) {
        balanceRepo.updateRecord(farmerBalance);
        farmerBalance.setTraderCode(prefrenceManager.getTraderModel().getCode());

        synch(AppConstants.UPDATE, AppConstants.ENTITY_BALANCES, farmerBalance, null, 1);

    }

    public void updateRecordDirect(FarmerBalance farmerBalance) {
        balanceRepo.updateRecordDirect(farmerBalance);
        farmerBalance.setTraderCode(prefrenceManager.getTraderModel().getCode());

        synch(AppConstants.UPDATE, AppConstants.ENTITY_BALANCES, farmerBalance, null, 1);

    }


    public void deleteRecord(FarmerBalance farmerBalance) {
        balanceRepo.deleteRecord(farmerBalance);
        farmerBalance.setTraderCode(prefrenceManager.getTraderModel().getCode());

        synch(AppConstants.DELETE, AppConstants.ENTITY_BALANCES, farmerBalance, null, 1);

    }


}
