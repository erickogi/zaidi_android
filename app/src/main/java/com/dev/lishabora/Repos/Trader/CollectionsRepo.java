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

    public List<Collection> getCollectionByDateByFarmerByTime(String code, String today, String ampm) {
        return collectionsDao.getCollectionByDateByFarmerByTime(code, today, ampm);

    }
    public LiveData<List<Collection>> getCollectionByPayout(String payout) {
        return collectionsDao.getCollectionByPayoutNumber(payout);
    }

    public LiveData<List<Collection>> getCollectionByPayoutByFarmer(String payout, String farmer) {
        return collectionsDao.getCollectionByPayoutNumberByFarmer(payout, farmer);
    }

    public LiveData<List<Collection>> getCollectionByFarmer(String farmer) {
        return collectionsDao.getCollectionByFarmer(farmer);
    }

    public LiveData<Double> getSumOfMilkFarmerPayout(String farmercode, int payoutNumber) {
        return collectionsDao.getSumOfMilkFarmerPayout(farmercode, payoutNumber);

    }

    public LiveData<Double> getSumOfLoanFarmerPayout(String farmercode, int payoutNumber) {
        return collectionsDao.getSumOfLoanFarmerPayout(farmercode, payoutNumber);

    }

    public LiveData<Double> getSumOfOrderFarmerPayout(String farmercode, int payoutNumber) {
        return collectionsDao.getSumOfOrderFarmerPayout(farmercode, payoutNumber);

    }

    public List<Collection> getCollectionByPayoutListOne(String payout) {
        return collectionsDao.getCollectionByPayoutNumberListOne(payout);
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

    public LiveData<Collection> getCollectionById(int collectionId) {
        return collectionsDao.getCollectionById(collectionId);
    }

    public Collection getCollectionByIdOne(int collectionId) {
        return collectionsDao.getCollectionByIdOne(collectionId);
    }

    public void approveFarmersPayoutCard(String farmercode, int payoutNumber) {
        collectionsDao.approveFarmersPayoutCard(1, farmercode, payoutNumber);

    }

    public void cancelFarmersPayoutCard(String farmercode, int payoutNumber) {
        collectionsDao.approveFarmersPayoutCard(0, farmercode, payoutNumber);

    }

    public void updateCollectionsByPayout(int payoutnumber, int status) {
        collectionsDao.approvePayout(payoutnumber, status);

    }




    private static class insertAsyncTask extends AsyncTask<Collection, Void, Boolean> {

        private CollectionsDao mAsyncTaskDao;

        insertAsyncTask(CollectionsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final Collection... params) {
            mAsyncTaskDao.insertSingleCollection(params[0]);
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
