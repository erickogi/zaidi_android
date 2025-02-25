package com.dev.lishabora.Repos.Trader;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dev.lishabora.Database.BalancesDao;
import com.dev.lishabora.Database.LMDatabase;
import com.dev.lishabora.Models.FarmerBalance;

import java.util.List;

public class BalanceRepo {
    private BalancesDao dao;


    private LMDatabase db;


    public BalanceRepo(Application application) {
        db = LMDatabase.getDatabase(application);
        dao = db.balancesDao();
    }


    public void insertMultiple(List<FarmerBalance> farmerBalance) {
        new insertAsyncsTasks(dao).execute(farmerBalance);
    }

    public void insert(FarmerBalance farmerBalance) {
        new insertAsyncTask(dao).execute(farmerBalance);
    }

    public void insertDirect(FarmerBalance farmerBalance) {
        // new insertAsyncTask(dao).execute(farmerBalance);
        try {
            dao.insertSingle(farmerBalance);
        } catch (Exception nm) {
            nm.printStackTrace();
        }
    }


    public LiveData<List<FarmerBalance>> fetchAll() {
        return dao.fetchAllData();
    }

    public List<FarmerBalance> fetchAllOne() {
        return dao.fetchAllDataOne();
    }

    public LiveData<FarmerBalance> getByKeyCode(String code) {
        return dao.getByKeyCode(code);
    }


    public LiveData<List<FarmerBalance>> getByFarmerCode(String code) {
        return dao.getByFarmerCode(code);
    }


    public List<FarmerBalance> getByFarmerCodeOne(String code) {
        return dao.getByFarmerCodeOne(code);
    }


    public LiveData<FarmerBalance> getByFarmerCodeByPayout(String code, String payoutCode) {
        return dao.getByFarmerCodeByPayout(code, payoutCode);
    }


    public FarmerBalance getByFarmerCodeByPayoutOne(String code, String payoutCode) {
        return dao.getByFarmerCodeByPayoutOne(code, payoutCode);
    }



    public void updateRecord(FarmerBalance farmerBalance) {
        new updateAsyncTask(dao).execute(farmerBalance);
    }

    public void updateRecordDirect(FarmerBalance farmerBalance) {
        // new updateAsyncTask(dao).execute(farmerBalance);
        try {
            dao.updateRecord(farmerBalance);
        } catch (Exception nm) {
            nm.printStackTrace();
        }
    }


    public void deleteRecord(FarmerBalance farmerBalance) {
        new deleteAsyncTask(dao).execute(farmerBalance);
    }


    private static class insertAsyncTask extends AsyncTask<FarmerBalance, Void, Boolean> {

        private BalancesDao mAsyncTaskDao;

        insertAsyncTask(BalancesDao farmersLoansDao) {
            mAsyncTaskDao = farmersLoansDao;
        }

        @Override
        protected Boolean doInBackground(final FarmerBalance... params) {
            try {
                mAsyncTaskDao.insertSingle(params[0]);
            } catch (Exception nm) {

            }
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class insertAsyncsTasks extends AsyncTask<List<FarmerBalance>, Void, Boolean> {

        private BalancesDao mAsyncTaskDao;

        insertAsyncsTasks(BalancesDao syncDao) {
            mAsyncTaskDao = syncDao;
        }

        @Override
        protected Boolean doInBackground(final List<FarmerBalance>... params) {
            try {
                mAsyncTaskDao.insertMultiple(params[0]);
            } catch (Exception nm) {

            }
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class updateAsyncTask extends AsyncTask<FarmerBalance, Void, Boolean> {

        private BalancesDao mAsyncTaskDao;

        updateAsyncTask(BalancesDao syncDao) {
            mAsyncTaskDao = syncDao;
        }

        @Override
        protected Boolean doInBackground(final FarmerBalance... params) {
            mAsyncTaskDao.updateRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class deleteAsyncTask extends AsyncTask<FarmerBalance, Void, Boolean> {

        private BalancesDao mAsyncTaskDao;

        deleteAsyncTask(BalancesDao syncDao) {
            mAsyncTaskDao = syncDao;
        }

        @Override
        protected Boolean doInBackground(final FarmerBalance... params) {
            mAsyncTaskDao.deleteRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }


}
