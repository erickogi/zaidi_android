package com.dev.lishabora.Repos.Trader;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dev.lishabora.Database.LMDatabase;
import com.dev.lishabora.Database.OrderPaymentsDao;
import com.dev.lishabora.Models.Trader.OrderPayments;

import java.util.List;

public class OrderPaymentsRepo {

    private OrderPaymentsDao dao;


    private LMDatabase db;


    public OrderPaymentsRepo(Application application) {
        db = LMDatabase.getDatabase(application);
        dao = db.orderPaymentsDao();
    }

    public void insertMultiple(List<OrderPayments> orderPayments) {
        new insertAsyncsTasks(dao).execute(orderPayments);
    }

    public void insertSingle(OrderPayments orderPayments) {
        new insertAsyncTask(dao).execute(orderPayments);
    }


    public LiveData<List<OrderPayments>> fetchAllData() {
        return dao.fetchAllData();
    }

    public LiveData<OrderPayments> getOrderPaymentById(int keyid) {
        return dao.getOrderPaymentById(keyid);
    }


    public OrderPayments getOrderPaymentByIdOne(int keyid) {
        return dao.getOrderPaymentByIdOne(keyid);
    }


    public LiveData<List<OrderPayments>> getOrderPaymentByDate(String date) {
        return dao.getOrderPaymentByDate(date);
    }

    public LiveData<List<OrderPayments>> getOrderPaymentByOrderId(String orderId) {
        return dao.getOrderPaymentByOrderId(orderId);
    }

    public LiveData<List<OrderPayments>> getOrderPaymentByPaout(int payoutNo) {
        return dao.getOrderPaymentByPaout(payoutNo);
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

    public double getSumPaid(int loanId) {
        return dao.getSumPaid(loanId);
    }


    public double getSumPaidByPayout(int payoutid) {
        return dao.getSumPaidByPayout(payoutid);
    }


    public OrderPayments getOrderPaymentByDateByFarmerByTimeSingle(int loanId, String today) {
        return dao.getOrderPaymentByDateByFarmerByTimeSingle(loanId, today);
    }


    public OrderPayments getLast() {
        return dao.getLast();
    }


    public LiveData<List<OrderPayments>> getOrderPaymentsBetweenDates(Long date1, Long date2) {
        return dao.getOrderPaymentsBetweenDates(date1, date2);
    }


    public LiveData<List<OrderPayments>> getOrderPaymentsBetweenDates(Long date1, Long date2, int loanId) {
        return dao.getOrderPaymentsBetweenDates(date1, date2, loanId);
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
