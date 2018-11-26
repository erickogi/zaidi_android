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

    public void insertSingleLoanPaymentDirect(LoanPayments loanPayments) {
        dao.insertSingle(loanPayments);
    }


    public LiveData<List<LoanPayments>> fetchAllData() {
        return dao.fetchAllData();
    }

    public LiveData<LoanPayments> getLoanPaymentByCode(String code) {
        return dao.getLoanPaymentByCode(code);
    }


    public LoanPayments getLoanPaymentByCodeOne(String code) {
        return dao.getLoanPaymentByCodeOne(code);
    }


    public LiveData<List<LoanPayments>> getLoanPaymentByDate(String date) {
        return dao.getLoanPaymentByDate(date);
    }

    public LiveData<List<LoanPayments>> getLoanPaymentByLoanCode(String loanCode) {
        return dao.getLoanPaymentByLoanCode(loanCode);
    }

    public List<LoanPayments> getLoanPaymentByLoanCodeoOne(String loanCode) {
        return dao.getLoanPaymentByLoanCodeOne(loanCode);
    }

    public LiveData<List<LoanPayments>> getLoanPaymentByPaout(String payoutCode) {
        return dao.getLoanPaymentByPaout(payoutCode);
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

    public double getSumPaid(String loanCode) {
        return dao.getSumPaid(loanCode);
    }


    public double getSumPaidByPayout(String payoutCode) {
        return dao.getSumPaidByPayout(payoutCode);
    }


    public LoanPayments getLoanPaymentByDateByFarmerByTimeSingle(String loanCode, String today) {
        return dao.getLoanPaymentByDateByFarmerByTimeSingle(loanCode, today);
    }


    public LoanPayments getLast() {
        return dao.getLast();
    }


    public LiveData<List<LoanPayments>> getLoanPaymentsBetweenDates(Long date1, Long date2) {
        return dao.getLoanPaymentsBetweenDates(date1, date2);
    }


    public LiveData<List<LoanPayments>> getLoanPaymentsBetweenDates(Long date1, Long date2, String loanCode) {
        return dao.getLoanPaymentsBetweenDates(date1, date2, loanCode);
    }

    public List<LoanPayments> getLoanPaymentsByLoanIdBetweenDatesorByPayoutCode(Long date1, Long date2, String loanCode, String payoutCode) {
        return dao.getLoanPaymentsByLoanIdBetweenDatesorByPayoutCode(date1, date2, loanCode, payoutCode);
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
