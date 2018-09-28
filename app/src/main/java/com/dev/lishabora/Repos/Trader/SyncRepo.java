package com.dev.lishabora.Repos.Trader;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dev.lishabora.Database.LMDatabase;
import com.dev.lishabora.Database.SyncDao;
import com.dev.lishabora.Models.SyncModel;

import java.util.List;

public class SyncRepo {
    private SyncDao syncDao;


    private LMDatabase db;


    public SyncRepo(Application application) {
        db = LMDatabase.getDatabase(application);
        syncDao = db.syncDao();
    }


    public LiveData<List<SyncModel>> fetchAllData(boolean isOnline) {
        if (isOnline) {
            return null;
        } else {
            syncDao = db.syncDao();
            return syncDao.fetchAllData();
        }
    }

    public void insert(SyncModel syncModel) {
        syncDao = db.syncDao();

        new insertSyncAsyncTask(syncDao).execute(syncModel);
    }

    public void insert(List<SyncModel> syncModel) {
        syncDao = db.syncDao();

        new insertSyncsAsyncTask(syncDao).execute(syncModel);
    }

    public void upDateRecord(SyncModel syncModel) {
        new updateSyncAsyncTask(db.syncDao()).execute(syncModel);
    }

    public void deleteRecord(SyncModel syncModel) {
        new deleteSyncAsyncTask(db.syncDao()).execute(syncModel);
    }

    public void insertMultipleSyncs(List<SyncModel> traderModels) {
        syncDao = db.syncDao();

        new insertSyncsAsyncTask(syncDao).execute(traderModels);
    }

    private void insertSingleSync(SyncModel syncModel) {

    }

    public LiveData<List<SyncModel>> getAllByStatus(int status) {
        return db.syncDao().getAllByStatus(status);
    }

    public LiveData<SyncModel> getSynce(int code) {
        return db.syncDao().getSyncById(code);
    }

    private static class insertSyncAsyncTask extends AsyncTask<SyncModel, Void, Boolean> {

        private SyncDao mAsyncTaskDao;

        insertSyncAsyncTask(SyncDao syncDao) {
            mAsyncTaskDao = syncDao;
        }

        @Override
        protected Boolean doInBackground(final SyncModel... params) {
            mAsyncTaskDao.insertSingleSync(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            com.dev.lishabora.Application.sync();


        }
    }

    private static class insertSyncsAsyncTask extends AsyncTask<List<SyncModel>, Void, Boolean> {

        private SyncDao mAsyncTaskDao;

        insertSyncsAsyncTask(SyncDao syncDao) {
            mAsyncTaskDao = syncDao;
        }

        @Override
        protected Boolean doInBackground(final List<SyncModel>... params) {
            mAsyncTaskDao.insertMultipleSyncs(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class updateSyncAsyncTask extends AsyncTask<SyncModel, Void, Boolean> {

        private SyncDao mAsyncTaskDao;

        updateSyncAsyncTask(SyncDao syncDao) {
            mAsyncTaskDao = syncDao;
        }

        @Override
        protected Boolean doInBackground(final SyncModel... params) {
            mAsyncTaskDao.updateRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class deleteSyncAsyncTask extends AsyncTask<SyncModel, Void, Boolean> {

        private SyncDao mAsyncTaskDao;

        deleteSyncAsyncTask(SyncDao syncDao) {
            mAsyncTaskDao = syncDao;
        }

        @Override
        protected Boolean doInBackground(final SyncModel... params) {
            mAsyncTaskDao.deleteRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }


}
