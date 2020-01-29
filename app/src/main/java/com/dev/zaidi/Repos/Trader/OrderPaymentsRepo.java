package com.dev.zaidi.Repos.Trader;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dev.zaidi.Database.FarmersLoansDao;
import com.dev.zaidi.Database.LMDatabase;
import com.dev.zaidi.Database.OrderPaymentsDao;
import com.dev.zaidi.Models.Trader.OrderPayments;

import java.util.List;

public class OrderPaymentsRepo {

    private OrderPaymentsDao dao;
    private FarmersLoansDao farmersLoansDao;



    private LMDatabase db;


    public OrderPaymentsRepo(Application application) {
        db = LMDatabase.getDatabase(application);
        farmersLoansDao = db.farmersLoansDao();
        dao = db.orderPaymentsDao();
    }

    public void insertMultiple(List<OrderPayments> orderPayments) {
        new insertAsyncsTasks(dao).execute(orderPayments);
    }

    public void insertSingle(OrderPayments orderPayments) {
        //new insertAsyncTask(dao).execute(orderPayments);
        dao.insertSingle(orderPayments);

    }


    public LiveData<List<OrderPayments>> fetchAllData() {
        return dao.fetchAllData();
    }

    public LiveData<OrderPayments> getOrderPaymentByCode(String code) {
        return dao.getOrderPaymentByCode(code);
    }


    public OrderPayments getOrderPaymentByCodeOne(String Code) {
        return dao.getOrderPaymentByCodeOne(Code);
    }


    public LiveData<List<OrderPayments>> getOrderPaymentByDate(String date) {
        return dao.getOrderPaymentByDate(date);
    }

    public LiveData<List<OrderPayments>> getOrderPaymentByOrderCode(String orderCode) {
        return dao.getOrderPaymentByOrderCode(orderCode);
    }

    public List<OrderPayments> getOrderPaymentByOrderCodeOne(String orderCode) {
        return dao.getOrderPaymentByOrderCodeOne(orderCode);
    }

    public LiveData<List<OrderPayments>> getOrderPaymentByPaout(String payoutCode) {
        return dao.getOrderPaymentByPaout(payoutCode);
    }


    public LiveData<List<OrderPayments>> getOrderPaymentByPayment(String payment) {
        return dao.getOrderPaymentByPayment(payment);
    }

    public LiveData<List<OrderPayments>> getOrderPaymentByRefNo(String refNo) {
        return dao.getOrderPaymentByRefNo(refNo);
    }


    public List<OrderPayments> getOrderPaymentByPayoutNumberListOne(String payoutnumber) {
        return dao.getOrderPaymentByPayoutNumberListOne(payoutnumber);
    }


    public void updateRecord(OrderPayments orderPayments) {
        new updateAsyncTask(dao).execute(orderPayments);
    }

    public void deleteRecord(OrderPayments orderPayments) {
        new deleteAsyncTask(dao).execute(orderPayments);
    }

    public double getSumPaid(String loanCode) {
        return dao.getSumPaid(loanCode);
    }


    public double getSumPaidByPayout(String payoutCode) {
        return dao.getSumPaidByPayout(payoutCode);
    }


    public OrderPayments getOrderPaymentByDateByFarmerByTimeSingle(String loanCode, String today) {
        return dao.getOrderPaymentByDateByFarmerByTimeSingle(loanCode, today);
    }


    public OrderPayments getLast() {
        return dao.getLast();
    }


    public LiveData<List<OrderPayments>> getOrderPaymentsBetweenDates(Long date1, Long date2) {
        return dao.getOrderPaymentsBetweenDates(date1, date2);
    }


    public LiveData<List<OrderPayments>> getOrderPaymentsBetweenDates(Long date1, Long date2, String loanCode) {
        return dao.getOrderPaymentsBetweenDates(date1, date2, loanCode);
    }

    public List<OrderPayments> getOrderPaymentsByOrderIdBetweenDatesorByPayoutCode(Long date1, Long date2, String orderCODE, String payoutCode) {
        return dao.getOrderPaymentsByOrderIdBetweenDatesorByPayoutCode(date1, date2, orderCODE, payoutCode);
    }


    private static class insertAsyncTask extends AsyncTask<OrderPayments, Void, Boolean> {

        private OrderPaymentsDao mAsyncTaskDao;

        insertAsyncTask(OrderPaymentsDao syncdao) {
            mAsyncTaskDao = syncdao;
        }

        @Override
        protected Boolean doInBackground(final OrderPayments... params) {
            mAsyncTaskDao.insertSingle(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class insertAsyncsTasks extends AsyncTask<List<OrderPayments>, Void, Boolean> {

        private OrderPaymentsDao mAsyncTaskDao;

        insertAsyncsTasks(OrderPaymentsDao syncDao) {
            mAsyncTaskDao = syncDao;
        }

        @Override
        protected Boolean doInBackground(final List<OrderPayments>... params) {
            mAsyncTaskDao.insertMultiple(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class updateAsyncTask extends AsyncTask<OrderPayments, Void, Boolean> {

        private OrderPaymentsDao mAsyncTaskDao;

        updateAsyncTask(OrderPaymentsDao syncDao) {
            mAsyncTaskDao = syncDao;
        }

        @Override
        protected Boolean doInBackground(final OrderPayments... params) {
            mAsyncTaskDao.updateRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class deleteAsyncTask extends AsyncTask<OrderPayments, Void, Boolean> {

        private OrderPaymentsDao mAsyncTaskDao;

        deleteAsyncTask(OrderPaymentsDao syncDao) {
            mAsyncTaskDao = syncDao;
        }

        @Override
        protected Boolean doInBackground(final OrderPayments... params) {
            mAsyncTaskDao.deleteRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

}
