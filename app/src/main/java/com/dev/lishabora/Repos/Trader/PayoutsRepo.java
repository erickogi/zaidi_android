package com.dev.lishabora.Repos.Trader;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.dev.lishabora.Database.LMDatabase;
import com.dev.lishabora.Database.PayoutsDao;
import com.dev.lishabora.Models.Payouts;

import java.util.List;

public class PayoutsRepo {
    private PayoutsDao dao;


    private LMDatabase db;


    public PayoutsRepo(Application application) {
        db = LMDatabase.getDatabase(application);
        dao = db.payoutsDao();
    }


    public LiveData<List<Payouts>> fetchAllData(boolean isOnline) {
        if (isOnline) {
            return null;
        } else {


            return dao.fetchAllData();
        }
    }

    public LiveData<Payouts> getPayoutByCode(String code) {
        return dao.getPayoutsByCode(code);
    }

    public LiveData<List<Payouts>> getPayoutsByCycleCode(String code) {
        return dao.getPayoutsByCycleCode(code);
    }

    public LiveData<List<Payouts>> getPayoutsByStatus(String status) {

        return dao.getPayoutsByStatus(status);
    }

    public List<Payouts> getPayoutsByStatusD(String status) {

        return dao.getPayoutsByStatusD(status);
    }



    public LiveData<List<Payouts>> getPayoutsByPayoutsByDate(String start, String end) {
        return dao.getPayoutsByByDate(start, end);
    }

    public LiveData<Payouts> getPayoutsByPayout(String payout) {
        return dao.getPayoutsByPayoutCode(payout);
    }

    public Payouts getLast(String cycleCode) {
        return dao.getLast(cycleCode);
    }

    public Payouts getLast() {
        return dao.getLast();
    }

    public void insert(Payouts collection) {

        new insertAsyncTask(dao).execute(collection);
    }

    public boolean insert(List<Payouts> payouts) {

        new insertsAsyncTask(dao).execute(payouts);

        return true;
    }

    public void upDateRecord(Payouts collection) {
        new updateAsyncTask(dao).execute(collection);
    }

    public void deleteRecord(Payouts collection) {
        new deleteAsyncTask(dao).execute(collection);
    }


    private void insertSingleProduct(Payouts productsModel) {


    }


    private static class insertAsyncTask extends AsyncTask<Payouts, Void, Boolean> {

        private PayoutsDao mAsyncTaskDao;

        insertAsyncTask(PayoutsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final Payouts... params) {
            mAsyncTaskDao.insertSinglePayouts(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class insertsAsyncTask extends AsyncTask<List<Payouts>, Void, Boolean> {

        private PayoutsDao mAsyncTaskDao;

        insertsAsyncTask(PayoutsDao dao) {
            mAsyncTaskDao = dao;
        }

        @SafeVarargs
        @Override
        protected final Boolean doInBackground(final List<Payouts>... params) {
            mAsyncTaskDao.insertMultiolePayoutss(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Log.d("insertmaneneo", "inserting repo" + aBoolean);


        }
    }

    private static class updateAsyncTask extends AsyncTask<Payouts, Void, Boolean> {

        private PayoutsDao mAsyncTaskDao;

        updateAsyncTask(PayoutsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final Payouts... params) {
            mAsyncTaskDao.updateRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class deleteAsyncTask extends AsyncTask<Payouts, Void, Boolean> {

        private PayoutsDao mAsyncTaskDao;

        deleteAsyncTask(PayoutsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final Payouts... params) {
            mAsyncTaskDao.deleteRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }


}
