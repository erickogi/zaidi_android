package com.dev.lishabora.ViewModels.Trader;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dev.lishabora.Models.Trader.FarmerBalance;
import com.dev.lishabora.Models.Trader.FarmerLoansTable;
import com.dev.lishabora.Models.Trader.FarmerOrdersTable;
import com.dev.lishabora.Models.Trader.LoanPayments;
import com.dev.lishabora.Models.Trader.OrderPayments;
import com.dev.lishabora.Repos.Trader.BalanceRepo;
import com.dev.lishabora.Repos.Trader.LoanPaymentsRepo;
import com.dev.lishabora.Repos.Trader.LoansTableRepo;
import com.dev.lishabora.Repos.Trader.OrderPaymentsRepo;
import com.dev.lishabora.Repos.Trader.OrdersTableRepo;
import com.dev.lishabora.Utils.PrefrenceManager;

import java.util.List;

public class BalncesViewModel extends AndroidViewModel

{

    BalanceRepo balanceRepo;
    LoansTableRepo loansTableRepo;
    LoanPaymentsRepo loanPaymentsRepo;
    OrderPaymentsRepo orderPaymentsRepo;
    OrdersTableRepo ordersTableRepo;


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
    }

    public LiveData<FarmerLoansTable> getFarmerLoanById(int keyid) {
        return loansTableRepo.getFarmerLoanById(keyid);
    }

    public FarmerLoansTable getFarmerLoanByIdOne(int id) {
        return loansTableRepo.getFarmerLoanByIdOne(id);
    }

    public LiveData<List<FarmerLoansTable>> getFarmerLoanByDate(String date) {
        return getFarmerLoanByDate(date);
    }

    public LiveData<List<FarmerLoansTable>> getFarmerLoanByPayoutNumber(String payoutNo) {
        return loansTableRepo.getFarmerLoanByPayoutNumber(payoutNo);
    }

    public FarmerLoansTable getFarmerLoanByCollectionOne(int collId) {
        return loansTableRepo.getFarmerLoanByCollectionOne(collId);
    }

    public LiveData<FarmerLoansTable> getFarmerLoanByCollection(int collId) {
        return loansTableRepo.getFarmerLoanByCollection(collId);
    }

    public LiveData<List<FarmerLoansTable>> getFarmerLoanByPayoutNumberByFarmer(String payoutnumber, String farmer) {
        return loansTableRepo.getFarmerLoanByPayoutNumberByFarmer(payoutnumber, farmer);
    }

    public List<FarmerLoansTable> getFarmerLoanByPayoutNumberByFarmerByStatus(String payoutnumber, String farmer, int status) {
        return loansTableRepo.getFarmerLoanByPayoutNumberByFarmerByStatus(payoutnumber, farmer, status);
    }

    public List<FarmerLoansTable> getFarmerLoanByPayoutNumberByFarmerByStatus(String farmer, int status) {
        return loansTableRepo.getFarmerLoanByPayoutNumberByFarmerByStatus(farmer, status);
    }


    public LiveData<List<FarmerLoansTable>> getFarmerLoanByFarmer(String farmer) {
        return loansTableRepo.getFarmerLoanByFarmer(farmer);
    }

    public List<FarmerLoansTable> getFarmerLoanByPayoutNumberListOne(String payoutnumber) {
        return loansTableRepo.getFarmerLoanByPayoutNumberListOne(payoutnumber);
    }

    public void approveFarmersoan(int a, String farmercode, int payoutNumber) {
        loansTableRepo.approveFarmersPayoutCard(a, farmercode, payoutNumber);
    }

    public void updateRecordLoan(FarmerLoansTable farmerLoansTable) {
        loansTableRepo.updateRecord(farmerLoansTable);
    }

    public void deleteRecordLoan(FarmerLoansTable farmerLoansTable) {
        loansTableRepo.deleteRecord(farmerLoansTable);
    }

    public double getBalanceLoanByFarmerCode(String farmercode) {
        return loansTableRepo.getBalanceByFarmerCode(farmercode);
    }

    public double getBalanceLoanByPayout(int payoutid) {
        return loansTableRepo.getBalanceByPayout(payoutid);
    }

    public double getLoanInstallmentSumByPayoutCode(int payoutid) {
        return loansTableRepo.getInstallmentSumByPayoutCode(payoutid);
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

    public void updateStatusLoan(int id, int status) {
        loansTableRepo.updateStatus(id, status);
    }

    public LiveData<List<FarmerLoansTable>> getFarmerLoansBetweenDates(Long date1, Long date2) {
        return loansTableRepo.getFarmerLoansBetweenDates(date1, date2);

    }

    public LiveData<List<FarmerLoansTable>> getFarmerLoansBetweenDates(Long date1, Long date2, String code) {
        return loansTableRepo.getFarmerLoansBetweenDates(date1, date2, code);
    }


    /******ORDERS TABLES*******/


    public void insertMultipleOrders(List<FarmerOrdersTable> farmerOrdersTables) {
        ordersTableRepo.insertMultiple(farmerOrdersTables);
    }

    public void insertOrder(FarmerOrdersTable farmerOrdersTable) {
        ordersTableRepo.insert(farmerOrdersTable);
    }

    public LiveData<List<FarmerOrdersTable>> fetchAllOrders() {
        return ordersTableRepo.fetchAll();
    }

    public LiveData<FarmerOrdersTable> getFarmerOrderById(int keyid) {
        return ordersTableRepo.getFarmerOrderById(keyid);
    }

    public FarmerOrdersTable getFarmerOrderByIdOne(int id) {
        return ordersTableRepo.getFarmerOrderByIdOne(id);
    }

    public LiveData<List<FarmerOrdersTable>> getFarmerOrderByDate(String date) {
        return getFarmerOrderByDate(date);
    }

    public LiveData<List<FarmerOrdersTable>> getFarmerOrderByPayoutNumber(String payoutNo) {
        return ordersTableRepo.getFarmerOrderByPayoutNumber(payoutNo);
    }

    public FarmerOrdersTable getFarmerOrderByCollection(int collId) {
        return ordersTableRepo.getFarmerOrderByCollection(collId);
    }

    public LiveData<List<FarmerOrdersTable>> getFarmerOrderByPayoutNumberByFarmer(String payoutnumber, String farmer) {
        return ordersTableRepo.getFarmerOrderByPayoutNumberByFarmer(payoutnumber, farmer);
    }

    public List<FarmerOrdersTable> getFarmerOrderByPayoutNumberByFarmerByStatus(String payoutnumber, String farmer, int status) {
        return ordersTableRepo.getFarmerOrderByPayoutNumberByFarmerByStatus(payoutnumber, farmer, status);
    }

    public List<FarmerOrdersTable> getFarmerOrderByPayoutNumberByFarmerByStatus(String farmer, int status) {
        return ordersTableRepo.getFarmerOrderByPayoutNumberByFarmerByStatus(farmer, status);
    }


    public LiveData<List<FarmerOrdersTable>> getFarmerOrderByFarmer(String farmer) {
        return ordersTableRepo.getFarmerOrderByFarmer(farmer);
    }

    public List<FarmerOrdersTable> getFarmerOrderByPayoutNumberListOne(String payoutnumber) {
        return ordersTableRepo.getFarmerOrderByPayoutNumberListOne(payoutnumber);
    }

    public void approveFarmersOrder(int a, String farmercode, int payoutNumber) {
        ordersTableRepo.approveFarmersPayoutCard(a, farmercode, payoutNumber);
    }

    public void updateRecord(FarmerOrdersTable farmerOrdersTable) {
        ordersTableRepo.updateRecord(farmerOrdersTable);
    }

    public void deleteRecord(FarmerOrdersTable farmerOrdersTable) {
        ordersTableRepo.deleteRecord(farmerOrdersTable);
    }

    public double getBalanceOrderByFarmerCode(String farmercode) {
        return ordersTableRepo.getBalanceByFarmerCode(farmercode);
    }

    public double getBalanceOrderByPayout(int payoutid) {
        return ordersTableRepo.getBalanceByPayout(payoutid);
    }

    public double getOrderInstallmentSumByPayoutCode(int payoutid) {
        return ordersTableRepo.getInstallmentSumByPayoutCode(payoutid);
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

    public void updateStatusOrder(int id, int status) {
        ordersTableRepo.updateStatus(id, status);
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
    }


    public LiveData<List<LoanPayments>> fetchAllDataLoanPayments() {
        return loanPaymentsRepo.fetchAllData();
    }

    public LiveData<LoanPayments> getLoanPaymentById(int keyid) {
        return loanPaymentsRepo.getLoanPaymentById(keyid);
    }


    public LoanPayments getLoanPaymentByIdOne(int keyid) {
        return loanPaymentsRepo.getLoanPaymentByIdOne(keyid);
    }


    public LiveData<List<LoanPayments>> getLoanPaymentByDate(String date) {
        return loanPaymentsRepo.getLoanPaymentByDate(date);
    }

    public LiveData<List<LoanPayments>> getLoanPaymentByLoanId(String loanId) {
        return loanPaymentsRepo.getLoanPaymentByLoanId(loanId);
    }

    public LiveData<List<LoanPayments>> getLoanPaymentByPaout(int payoutNo) {
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
    }

    public void deleteRecordLoanPayment(LoanPayments loanPayment) {
        loanPaymentsRepo.deleteRecord(loanPayment);
    }

    public double getSumPaidLoanPayment(int loanId) {
        return loanPaymentsRepo.getSumPaid(loanId);
    }


    public double getSumPaidByPayoutLoanPayment(int payoutid) {
        return loanPaymentsRepo.getSumPaidByPayout(payoutid);
    }


    public LoanPayments getLoanPaymentByDateByFarmerByTimeSingle(int loanId, String today) {
        return loanPaymentsRepo.getLoanPaymentByDateByFarmerByTimeSingle(loanId, today);
    }


    public LoanPayments getLastLoanPayment() {
        return loanPaymentsRepo.getLast();
    }


    public LiveData<List<LoanPayments>> getLoanPaymentsBetweenDates(Long date1, Long date2) {
        return loanPaymentsRepo.getLoanPaymentsBetweenDates(date1, date2);
    }


    public LiveData<List<LoanPayments>> getLoanPaymentsBetweenDates(Long date1, Long date2, int loanId) {
        return loanPaymentsRepo.getLoanPaymentsBetweenDates(date1, date2, loanId);
    }


    /*******ORDER PAYMENTS *****/

    public void insertMultipleOrderPayments(List<OrderPayments> orderPayments) {
        orderPaymentsRepo.insertMultiple(orderPayments);
    }

    public void insertSingleOrderPayment(OrderPayments orderPayments) {
        orderPaymentsRepo.insertSingle(orderPayments);
    }


    public LiveData<List<OrderPayments>> fetchAllDataOrderPayment() {
        return orderPaymentsRepo.fetchAllData();
    }

    public LiveData<OrderPayments> getOrderPaymentById(int keyid) {
        return orderPaymentsRepo.getOrderPaymentById(keyid);
    }


    public OrderPayments getOrderPaymentByIdOne(int keyid) {
        return orderPaymentsRepo.getOrderPaymentByIdOne(keyid);
    }


    public LiveData<List<OrderPayments>> getOrderPaymentByDate(String date) {
        return orderPaymentsRepo.getOrderPaymentByDate(date);
    }

    public LiveData<List<OrderPayments>> getOrderPaymentByOrderId(String orderId) {
        return orderPaymentsRepo.getOrderPaymentByOrderId(orderId);
    }

    public LiveData<List<OrderPayments>> getOrderPaymentByPaout(int payoutNo) {
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


    public void updateRecordOrderPayment(OrderPayments loanPayment) {
        orderPaymentsRepo.updateRecord(loanPayment);
    }

    public void deleteRecordOrderPayment(OrderPayments loanPayment) {
        orderPaymentsRepo.deleteRecord(loanPayment);
    }

    public double getSumPaidOrderPayment(int loanId) {
        return orderPaymentsRepo.getSumPaid(loanId);
    }


    public double getSumPaidByPayoutOrderPayment(int payoutid) {
        return orderPaymentsRepo.getSumPaidByPayout(payoutid);
    }


    public OrderPayments getOrderPaymentByDateByFarmerByTimeSingle(int loanId, String today) {
        return orderPaymentsRepo.getOrderPaymentByDateByFarmerByTimeSingle(loanId, today);
    }


    public OrderPayments getLastOrderPaymentt() {
        return orderPaymentsRepo.getLast();
    }


    public LiveData<List<OrderPayments>> getOrderPaymentsBetweenDates(Long date1, Long date2) {
        return orderPaymentsRepo.getOrderPaymentsBetweenDates(date1, date2);
    }


    public LiveData<List<OrderPayments>> getOrderPaymentsBetweenDates(Long date1, Long date2, int loanId) {
        return orderPaymentsRepo.getOrderPaymentsBetweenDates(date1, date2, loanId);
    }


    /****** BALANCES ******/

    public void insertMultiple(List<FarmerBalance> farmerBalance) {
        balanceRepo.insertMultiple(farmerBalance);
    }

    public void insert(FarmerBalance farmerBalance) {
        balanceRepo.insert(farmerBalance);
    }


    public LiveData<List<FarmerBalance>> fetchAll() {
        return balanceRepo.fetchAll();
    }

    public LiveData<FarmerBalance> getByKeyID(int keyid) {
        return balanceRepo.getByKeyID(keyid);
    }


    public LiveData<FarmerBalance> getByFarmerCode(String code) {
        return balanceRepo.getByFarmerCode(code);
    }


    public FarmerBalance getByFarmerCodeOne(String code) {
        return balanceRepo.getByFarmerCodeOne(code);
    }

    public void updateRecord(FarmerBalance farmerBalance) {
        balanceRepo.updateRecord(farmerBalance);
    }


    public void deleteRecord(FarmerBalance farmerBalance) {
        balanceRepo.deleteRecord(farmerBalance);
    }


}
