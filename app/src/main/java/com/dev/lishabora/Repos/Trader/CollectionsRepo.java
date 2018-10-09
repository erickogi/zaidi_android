package com.dev.lishabora.Repos.Trader;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.dev.lishabora.Database.CollectionsDao;
import com.dev.lishabora.Database.LMDatabase;
import com.dev.lishabora.Models.Collection;

import java.util.List;

public class CollectionsRepo {
    private CollectionsDao collectionsDao;


    private LMDatabase db;


    public CollectionsRepo(Application application) {
        db = LMDatabase.getDatabase(application);
        collectionsDao = db.collectionsDao();
    }


    public LiveData<List<Collection>> fetchAllData(boolean isOnline) {
        if (isOnline) {
            return null;
        } else {


            return collectionsDao.fetchAllData();
        }
    }

    public List<Collection> getCollectionByFarmerByDa(String code, String date) {
        return collectionsDao.getCollectionByFarmerByDate(code, date);
    }

    public double getFarmerBalance(String code) {
        return collectionsDao.getBalanceByFarmerCode(code);
    }


    public List<Collection> getCollectionByDateByFarmerByTime(String code, String today, String ampm) {
        return collectionsDao.getCollectionByDateByFarmerByTime(code, today, ampm);

    }

    public Collection getCollectionByDateByFarmerByTimeSingle(String code, String today, String ampm) {
        return collectionsDao.getCollectionByDateByFarmerByTimeSingle(code, today, ampm);

    }

    public Collection getCollectionByDateByFarmerByTimeSingle(String code, String today) {
        return collectionsDao.getCollectionByDateByFarmerByTimeSingle(code, today);

    }

    public LiveData<List<Collection>> getCollectionByPayout(String payoutCode) {
        return collectionsDao.getCollectionByPayoutCode(payoutCode);
    }

    public LiveData<List<Collection>> getCollectionByPayoutByFarmer(String payoutCode, String farmer) {
        return collectionsDao.getCollectionByPayoutCodeByFarmer(payoutCode, farmer);
    }

    public LiveData<List<Collection>> getCollectionByFarmer(String farmer) {
        return collectionsDao.getCollectionByFarmer(farmer);
    }

    public LiveData<Double> getSumOfMilkFarmerPayout(String farmercode, String payoutCode) {
        return collectionsDao.getSumOfMilkFarmerPayout(farmercode, payoutCode);

    }

    public LiveData<Double> getSumOfMilkFarmerPayoutLtrs(String farmercode, String payoutCode) {
        return collectionsDao.getSumOfMilkFarmerPayoutLtrs(farmercode, payoutCode);

    }

    public LiveData<Double> getSumOfMilkFarmerPayoutKsh(String farmercode, String payoutCode) {
        return collectionsDao.getSumOfMilkFarmerPayoutKsh(farmercode, payoutCode);

    }

    public Double getSumOfMilkFarmerPayoutKshD(String farmercode, String payoutCode) {
        return collectionsDao.getSumOfMilkFarmerPayoutKshD(farmercode, payoutCode);

    }

    public LiveData<Double> getSumOfLoanFarmerPayout(String farmercode, String payoutCode) {
        return collectionsDao.getSumOfLoanFarmerPayout(farmercode, payoutCode);

    }

    public LiveData<Double> getSumOfOrderFarmerPayout(String farmercode, String payoutCode) {
        return collectionsDao.getSumOfOrderFarmerPayout(farmercode, payoutCode);

    }

    public List<Collection> getCollectionByPayoutListOne(String payoutCode) {
        return collectionsDao.getCollectionByPayoutCodeListOne(payoutCode);
    }

    public Collection getLast(String cycleCode) {
        return collectionsDao.getLast(cycleCode);
    }

    public void insert(Collection collection) {

        new insertAsyncTask(collectionsDao).execute(collection);
    }

    public boolean insert(List<Collection> collections) {

        Log.d("insertmaneneo", "inserting repo" + collections.size());
        new insertsAsyncTask(collectionsDao).execute(collections);

        return true;
    }

    public void upDateRecord(Collection collection) {
        new updateAsyncTask(collectionsDao).execute(collection);
    }

    public void deleteRecord(Collection collection) {
        new deleteAsyncTask(collectionsDao).execute(collection);
    }


    private void insertSingleProduct(Collection productsModel) {


    }

    public LiveData<Collection> getCollectionByCode(String collectionCode) {
        return collectionsDao.getCollectionByCode(collectionCode);
    }

    public Collection getCollectionByCodeOne(String collectionCode) {
        return collectionsDao.getCollectionByCodeOne(collectionCode);
    }

    public void approveFarmersPayoutCard(String farmercode, String payoutCode) {
        collectionsDao.approveFarmersPayoutCard(1, farmercode, payoutCode);

    }

    public void cancelFarmersPayoutCard(String farmercode, String payoutCode) {
        collectionsDao.approveFarmersPayoutCard(0, farmercode, payoutCode);

    }

    public void updateCollectionsByPayout(String payoutCode, int status) {
        collectionsDao.approvePayout(payoutCode, status);

    }

    public LiveData<List<Collection>> getCollectionsBetweenDates(Long date1, Long date2) {
        return collectionsDao.getCollectionsBetweenDates(date1, date2);

    }

    public LiveData<List<Collection>> getCollectionsBetweenDates(Long date1, Long date2, String code) {
        return collectionsDao.getCollectionsBetweenDates(date1, date2, code);

    }

    public List<Collection> getCollectionsBetweenDatesOne(Long date1, Long date2, String code) {
        return collectionsDao.getCollectionsBetweenDatesOne(date1, date2, code);

    }


    private static class insertAsyncTask extends AsyncTask<Collection, Void, Boolean> {

        private CollectionsDao mAsyncTaskDao;

        insertAsyncTask(CollectionsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final Collection... params) {
            try {
                mAsyncTaskDao.insertSingleCollection(params[0]);
            } catch (Exception nm) {

            }
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class insertsAsyncTask extends AsyncTask<List<Collection>, Void, Boolean> {

        private CollectionsDao mAsyncTaskDao;

        insertsAsyncTask(CollectionsDao dao) {
            mAsyncTaskDao = dao;
        }

        @SafeVarargs
        @Override
        protected final Boolean doInBackground(final List<Collection>... params) {
            mAsyncTaskDao.insertMultioleCollections(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Log.d("insertmaneneo", "inserting repo" + aBoolean);


        }
    }

    private static class updateAsyncTask extends AsyncTask<Collection, Void, Boolean> {

        private CollectionsDao mAsyncTaskDao;

        updateAsyncTask(CollectionsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final Collection... params) {
            mAsyncTaskDao.updateRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class deleteAsyncTask extends AsyncTask<Collection, Void, Boolean> {

        private CollectionsDao mAsyncTaskDao;

        deleteAsyncTask(CollectionsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final Collection... params) {
            mAsyncTaskDao.deleteRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }


}
