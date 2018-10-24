package com.dev.lishabora.Repos.Trader;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dev.lishabora.Database.FarmersOrdersDao;
import com.dev.lishabora.Database.LMDatabase;
import com.dev.lishabora.Models.Trader.FarmerOrdersTable;

import java.util.List;

public class OrdersTableRepo {

    private FarmersOrdersDao dao;


    private LMDatabase db;


    public OrdersTableRepo(Application application) {
        db = LMDatabase.getDatabase(application);
        dao = db.farmersOrdersDao();
    }

    public void insertMultiple(List<FarmerOrdersTable> farmerOrdersTables) {
        new insertAsyncsTasks(dao).execute(farmerOrdersTables);
    }

    public void insert(FarmerOrdersTable farmerOrdersTable) {
        new insertAsyncTask(dao).execute(farmerOrdersTable);
    }

    public void insertDirect(FarmerOrdersTable farmerOrdersTable) {
        try {
            dao.insertSingleFarmerOrder(farmerOrdersTable);
        } catch (Exception nm) {
            nm.printStackTrace();
            new insertAsyncTask(dao).execute(farmerOrdersTable);

        }
    }

    public LiveData<List<FarmerOrdersTable>> fetchAll() {
        return dao.fetchAllData();
    }

    public LiveData<FarmerOrdersTable> getFarmerOrderByCode(String code) {
        return dao.getFarmerOrderByCode(code);
    }

    public FarmerOrdersTable getFarmerOrderByCodeOne(String code) {
        return dao.getFarmerOrderByCodeOne(code);
    }

    public LiveData<List<FarmerOrdersTable>> getFarmerOrderByDate(String date) {
        return getFarmerOrderByDate(date);
    }

    public LiveData<List<FarmerOrdersTable>> getFarmerOrderByPayoutCode(String payoutCode) {
        return dao.getFarmerOrderByPayoutCode(payoutCode);
    }

    public List<FarmerOrdersTable> getFarmerOrderByPayoutCodeOne(String payoutCode) {
        return dao.getFarmerOrderByPayoutCodeOne(payoutCode);
    }

    public FarmerOrdersTable getFarmerOrderByCollection(String collId) {
        return dao.getFarmerOrderByCollection(collId);
    }

    public LiveData<FarmerOrdersTable> getFarmerOrderByCollectionLive(String collId) {
        return dao.getFarmerOrderByCollectionLive(collId);
    }

    public LiveData<List<FarmerOrdersTable>> getFarmerOrderByPayoutCodeByFarmer(String payoutCode, String farmer) {
        return dao.getFarmerOrderByPayoutCodeByFarmer(payoutCode, farmer);
    }

    public List<FarmerOrdersTable> getFarmerOrderByPayoutCodeByFarmerOne(String payoutCode, String farmer) {
        return dao.getFarmerOrderByPayoutCodeByFarmerOne(payoutCode, farmer);
    }

    public List<FarmerOrdersTable> getFarmerOrderByPayoutCodeByFarmerByStatus(String payoutCode, String farmer, int status) {
        return dao.getFarmerOrderByPayoutCodeByFarmerByStatus(payoutCode, farmer, status);
    }

    public List<FarmerOrdersTable> getFarmerOrderByPayoutCodeByFarmerByStatus(String farmer, int status) {
        return dao.getFarmerOrderByPayoutCodeByFarmerByStatus(farmer, status);

    }

    public LiveData<List<FarmerOrdersTable>> getFarmerOrderByFarmerByStatus(String farmer, int status) {
        return dao.getFarmerOrderByFarmerByStatus(farmer, status);

    }


    public LiveData<List<FarmerOrdersTable>> getFarmerOrderByFarmer(String farmer) {
        return dao.getFarmerOrderByFarmer(farmer);
    }

    public List<FarmerOrdersTable> getFarmerOrderByPayoutCodeListOne(String payoutCode) {
        return dao.getFarmerOrderByPayoutCodeListOne(payoutCode);
    }

    public void approveFarmersPayoutCard(int a, String farmercode, String payoutCode) {
        dao.approveFarmersPayoutCard(a, farmercode, payoutCode);
    }

    public void updateRecord(FarmerOrdersTable farmerOrdersTable) {
        new updateAsyncTask(dao).execute(farmerOrdersTable);
    }

    public void deleteRecord(FarmerOrdersTable farmerOrdersTable) {
        new deleteAsyncTask(dao).execute(farmerOrdersTable);
    }

    public void updateRecordDirect(FarmerOrdersTable farmerOrdersTable) {
        //new updateAsyncTask(dao).execute(farmerOrdersTable);
        try {
            dao.updateRecord(farmerOrdersTable);
        } catch (Exception nm) {
            new updateAsyncTask(dao).execute(farmerOrdersTable);
        }
    }

    public void deleteRecordDirect(FarmerOrdersTable farmerOrdersTable) {
        try {
            dao.deleteRecord(farmerOrdersTable);
        } catch (Exception nm) {
            new deleteAsyncTask(dao).execute(farmerOrdersTable);
        }

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

    public List<FarmerOrdersTable> getFarmerOrderByFarmerByDate(String code, String date) {
        return dao.getFarmerOrderByFarmerByDate(code, date);
    }

    public FarmerOrdersTable getFarmerOrderByDateByFarmerByTimeSingle(String code, String today) {
        return dao.getFarmerOrderByDateByFarmerByTimeSingle(code, today);

    }

    public FarmerOrdersTable getLast() {
        return dao.getLast();
    }

    public void updateStatus(String id, int status) {
        dao.updateStatus(id, status);
    }

    public LiveData<List<FarmerOrdersTable>> getFarmerOrdersBetweenDates(Long date1, Long date2) {
        return dao.getFarmerOrdersBetweenDates(date1, date2);

    }

    public LiveData<List<FarmerOrdersTable>> getFarmerOrdersBetweenDates(Long date1, Long date2, String code) {
        return dao.getFarmerOrdersBetweenDates(date1, date2, code);
    }


    private static class insertAsyncTask extends AsyncTask<FarmerOrdersTable, Void, Boolean> {

        private FarmersOrdersDao mAsyncTaskDao;

        insertAsyncTask(FarmersOrdersDao farmersOrdersDao) {
            mAsyncTaskDao = farmersOrdersDao;
        }

        @Override
        protected Boolean doInBackground(final FarmerOrdersTable... params) {
            mAsyncTaskDao.insertSingleFarmerOrder(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class insertAsyncsTasks extends AsyncTask<List<FarmerOrdersTable>, Void, Boolean> {

        private FarmersOrdersDao mAsyncTaskDao;

        insertAsyncsTasks(FarmersOrdersDao syncDao) {
            mAsyncTaskDao = syncDao;
        }

        @Override
        protected Boolean doInBackground(final List<FarmerOrdersTable>... params) {
            mAsyncTaskDao.insertMultioleFarmerOrders(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class updateAsyncTask extends AsyncTask<FarmerOrdersTable, Void, Boolean> {

        private FarmersOrdersDao mAsyncTaskDao;

        updateAsyncTask(FarmersOrdersDao syncDao) {
            mAsyncTaskDao = syncDao;
        }

        @Override
        protected Boolean doInBackground(final FarmerOrdersTable... params) {
            mAsyncTaskDao.updateRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class deleteAsyncTask extends AsyncTask<FarmerOrdersTable, Void, Boolean> {

        private FarmersOrdersDao mAsyncTaskDao;

        deleteAsyncTask(FarmersOrdersDao syncDao) {
            mAsyncTaskDao = syncDao;
        }

        @Override
        protected Boolean doInBackground(final FarmerOrdersTable... params) {
            mAsyncTaskDao.deleteRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

}
