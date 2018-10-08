package com.dev.lishabora.Repos.Trader;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dev.lishabora.Database.FarmersLoansDao;
import com.dev.lishabora.Database.LMDatabase;
import com.dev.lishabora.Models.Trader.FarmerLoansTable;

import java.util.List;

public class LoansTableRepo {

    private FarmersLoansDao dao;


    private LMDatabase db;


    public LoansTableRepo(Application application) {
        db = LMDatabase.getDatabase(application);
        dao = db.farmersLoansDao();
    }

    public void insertMultiple(List<FarmerLoansTable> farmerLoansTables) {
        new insertAsyncsTasks(dao).execute(farmerLoansTables);
    }

    public void insert(FarmerLoansTable farmerLoansTable) {
        new insertAsyncTask(dao).execute(farmerLoansTable);
    }


    public LiveData<List<FarmerLoansTable>> fetchAll() {
        return dao.fetchAllData();
    }

    public LiveData<FarmerLoansTable> getFarmerLoanByCode(String code) {
        return dao.getFarmerLoanByCode(code);
    }

    public FarmerLoansTable getFarmerLoanByCodeOne(String code) {
        return dao.getFarmerLoanByCodeOne(code);
    }

    public LiveData<List<FarmerLoansTable>> getFarmerLoanByDate(String date) {
        return getFarmerLoanByDate(date);
    }

    public LiveData<List<FarmerLoansTable>> getFarmerLoanByPayoutCode(String payouCode) {
        return dao.getFarmerLoanByPayoutCode(payouCode);
    }


    public FarmerLoansTable getFarmerLoanByCollectionOne(String collCode) {
        return dao.getFarmerLoanByCollectionOne(collCode);
    }

    public LiveData<FarmerLoansTable> getFarmerLoanByCollection(String collCode) {
        return dao.getFarmerLoanByCollection(collCode);
    }

    public LiveData<List<FarmerLoansTable>> getFarmerLoanByPayoutCodeByFarmer(String payoutCode, String farmer) {
        return dao.getFarmerLoanByPayoutCodeByFarmer(payoutCode, farmer);
    }

    public List<FarmerLoansTable> getFarmerLoanByPayoutCodeByFarmerByStatus(String payoutCode, String farmer, int status) {
        return dao.getFarmerLoanByPayoutCodeByFarmerByStatus(payoutCode, farmer, status);
    }

    public List<FarmerLoansTable> getFarmerLoanByPayoutCodeByFarmerByStatus(String farmer, int status) {
        return dao.getFarmerLoanByPayoutCodeByFarmerByStatus(farmer, status);
    }

    public LiveData<List<FarmerLoansTable>> getFarmerLoanByFarmerByStatus(String farmer, int status) {
        return dao.getFarmerLoanByFarmerByStatus(farmer, status);
    }


    public LiveData<List<FarmerLoansTable>> getFarmerLoanByFarmer(String farmer) {
        return dao.getFarmerLoanByFarmer(farmer);
    }

    public List<FarmerLoansTable> getFarmerLoanByPayoutCodeListOne(String payoutCode) {
        return dao.getFarmerLoanByPayoutCodeListOne(payoutCode);
    }


    public void approveFarmersPayoutCard(int a, String farmercode, String payoutCode) {
        dao.approveFarmersPayoutCard(a, farmercode, payoutCode);
    }


    public void updateRecord(FarmerLoansTable farmerLoansTable) {
        new updateAsyncTask(dao).execute(farmerLoansTable);
    }

    public void deleteRecord(FarmerLoansTable farmerLoansTable) {
        new deleteAsyncTask(dao).execute(farmerLoansTable);
    }


    public double getBalanceByFarmerCode(String farmercode) {
        return dao.getBalanceByFarmerCode(farmercode);
    }


    public double getBalanceByPayout(String payoutCode) {
        return dao.getBalanceBySumCode(payoutCode);
    }


    public double getInstallmentSumByPayoutCode(String payoutCode) {
        return dao.getInstallmentSumByPayoutCode(payoutCode);
    }

    public double getInstallmentSumByFarmerCode(String code) {
        return dao.getInstallmentSumByFarmerCode(code);
    }


    public List<FarmerLoansTable> getFarmerLoanByFarmerByDate(String code, String date) {
        return dao.getFarmerLoanByFarmerByDate(code, date);
    }


    public FarmerLoansTable getFarmerLoanByDateByFarmerByTimeSingle(String code, String today) {
        return dao.getFarmerLoanByDateByFarmerByTimeSingle(code, today);

    }


    public FarmerLoansTable getLast() {
        return dao.getLast();
    }


    public void updateStatus(String code, int status) {
        dao.updateStatus(code, status);
    }

    public LiveData<List<FarmerLoansTable>> getFarmerLoansBetweenDates(Long date1, Long date2) {
        return dao.getFarmerLoansBetweenDates(date1, date2);

    }


    public LiveData<List<FarmerLoansTable>> getFarmerLoansBetweenDates(Long date1, Long date2, String code) {
        return dao.getFarmerLoansBetweenDates(date1, date2, code);
    }


    private static class insertAsyncTask extends AsyncTask<FarmerLoansTable, Void, Boolean> {

        private FarmersLoansDao mAsyncTaskDao;

        insertAsyncTask(FarmersLoansDao farmersLoansDao) {
            mAsyncTaskDao = farmersLoansDao;
        }

        @Override
        protected Boolean doInBackground(final FarmerLoansTable... params) {
            mAsyncTaskDao.insertSingleFarmerLoan(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class insertAsyncsTasks extends AsyncTask<List<FarmerLoansTable>, Void, Boolean> {

        private FarmersLoansDao mAsyncTaskDao;

        insertAsyncsTasks(FarmersLoansDao syncDao) {
            mAsyncTaskDao = syncDao;
        }

        @Override
        protected Boolean doInBackground(final List<FarmerLoansTable>... params) {
            mAsyncTaskDao.insertMultioleFarmerLoans(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class updateAsyncTask extends AsyncTask<FarmerLoansTable, Void, Boolean> {

        private FarmersLoansDao mAsyncTaskDao;

        updateAsyncTask(FarmersLoansDao syncDao) {
            mAsyncTaskDao = syncDao;
        }

        @Override
        protected Boolean doInBackground(final FarmerLoansTable... params) {
            mAsyncTaskDao.updateRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class deleteAsyncTask extends AsyncTask<FarmerLoansTable, Void, Boolean> {

        private FarmersLoansDao mAsyncTaskDao;

        deleteAsyncTask(FarmersLoansDao syncDao) {
            mAsyncTaskDao = syncDao;
        }

        @Override
        protected Boolean doInBackground(final FarmerLoansTable... params) {
            mAsyncTaskDao.deleteRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

}
