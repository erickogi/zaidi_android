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
