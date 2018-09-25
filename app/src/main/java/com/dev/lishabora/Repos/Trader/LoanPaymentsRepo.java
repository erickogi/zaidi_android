package com.dev.lishabora.Repos.Trader;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dev.lishabora.Database.LMDatabase;
import com.dev.lishabora.Database.LoanPaymentsDao;
import com.dev.lishabora.Models.Trader.LoanPayments;

import java.util.List;

public class LoanPaymentsRepo {

    private LoanPaymentsDao dao;


    private LMDatabase db;


    public LoanPaymentsRepo(Application application) {
        db = LMDatabase.getDatabase(application);
        dao = db.loanPaymentsDao();
    }

    public void insertMultiple(List<LoanPayments> loanPayments) {
        dao.insertMultiple(loanPayments);
    }

    public void insertSingle(LoanPayments loanPayments) {
        dao.insertSingle(loanPayments);
    }


    public LiveData<List<LoanPayments>> fetchAllData() {
        return dao.fetchAllData();
    }

    public LiveData<LoanPayments> getLoanPaymentById(int keyid) {
        return dao.getLoanPaymentById(keyid);
    }


    public LoanPayments getLoanPaymentByIdOne(int keyid) {
        return dao.getLoanPaymentByIdOne(keyid);
    }


    public LiveData<List<LoanPayments>> getLoanPaymentByDate(String date) {
        return dao.getLoanPaymentByDate(date);
    }

    public LiveData<List<LoanPayments>> getLoanPaymentByLoanId(String loanId) {
        return dao.getLoanPaymentByLoanId(loanId);
    }

    public LiveData<List<LoanPayments>> getLoanPaymentByPaout(int payoutNo) {
        return dao.getLoanPaymentByPaout(payoutNo);
    }


    public LiveData<List<LoanPayments>> getLoanPaymentByPayment(String payment) {
        return dao.getLoanPaymentByPayment(payment);
    }

    public LiveData<List<LoanPayments>> getLoanPaymentByRefNo(String refNo) {
        return dao.getLoanPaymentByRefNo(refNo);
    }


    public List<LoanPayments> getLoanPaymentByPayoutNumberListOne(String payoutnumber) {
        return dao.getLoanPaymentByPayoutNumberListOne(payoutnumber);
    }


    public void updateRecord(LoanPayments loanPayment) {
        dao.updateRecord(loanPayment);
    }

    public void deleteRecord(LoanPayments loanPayment) {
        dao.deleteRecord(loanPayment);
    }

    public double getSumPaid(int loanId) {
        return dao.getSumPaid(loanId);
    }


    public double getSumPaidByPayout(int payoutid) {
        return dao.getSumPaidByPayout(payoutid);
    }


    public LoanPayments getLoanPaymentByDateByFarmerByTimeSingle(int loanId, String today) {
        return dao.getLoanPaymentByDateByFarmerByTimeSingle(loanId, today);
    }


    public LoanPayments getLast() {
        return dao.getLast();
    }


    public LiveData<List<LoanPayments>> getLoanPaymentsBetweenDates(Long date1, Long date2) {
        return dao.getLoanPaymentsBetweenDates(date1, date2);
    }


    public LiveData<List<LoanPayments>> getLoanPaymentsBetweenDates(Long date1, Long date2, int loanId) {
        return dao.getLoanPaymentsBetweenDates(date1, date2, loanId);
    }


    private static class insertAsyncTask extends AsyncTask<LoanPayments, Void, Boolean> {

        private LoanPaymentsDao mAsyncTaskDao;

        insertAsyncTask(LoanPaymentsDao syncdao) {
            mAsyncTaskDao = syncdao;
        }

        @Override
        protected Boolean doInBackground(final LoanPayments... params) {
            mAsyncTaskDao.insertSingle(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class insertAsyncsTasks extends AsyncTask<List<LoanPayments>, Void, Boolean> {

        private LoanPaymentsDao mAsyncTaskDao;

        insertAsyncsTasks(LoanPaymentsDao syncDao) {
            mAsyncTaskDao = syncDao;
        }

        @Override
        protected Boolean doInBackground(final List<LoanPayments>... params) {
            mAsyncTaskDao.insertMultiple(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class updateAsyncTask extends AsyncTask<LoanPayments, Void, Boolean> {

        private LoanPaymentsDao mAsyncTaskDao;

        updateAsyncTask(LoanPaymentsDao syncDao) {
            mAsyncTaskDao = syncDao;
        }

        @Override
        protected Boolean doInBackground(final LoanPayments... params) {
            mAsyncTaskDao.updateRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class deleteAsyncTask extends AsyncTask<LoanPayments, Void, Boolean> {

        private LoanPaymentsDao mAsyncTaskDao;

        deleteAsyncTask(LoanPaymentsDao syncDao) {
            mAsyncTaskDao = syncDao;
        }

        @Override
        protected Boolean doInBackground(final LoanPayments... params) {
            mAsyncTaskDao.deleteRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

}
