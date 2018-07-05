package com.dev.lishaboramobile.Global.Data.Operations.Repo;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dev.lishaboramobile.Farmer.Models.FamerModel;
import com.dev.lishaboramobile.Global.Data.LMDatabase;
import com.dev.lishaboramobile.Global.Data.Operations.Dao.FarmersDao;

import java.util.List;

public class FarmerRepo {
    private FarmersDao farmersDao;
    private LiveData<List<FamerModel>> farmers;
    private LiveData<FamerModel> farmer;


    private LMDatabase db;


    public FarmerRepo(Application application) {
        db = LMDatabase.getDatabase(application);
        farmersDao=db.farmersDao();
    }


    public LiveData<List<FamerModel>> fetchAllData(boolean isOnline) {
        if(isOnline){
            return null;
        }else {
            farmersDao = db.farmersDao();
            return farmersDao.fetchAllData();
        }
    }

    public void insert (FamerModel farmerModel) {
        farmersDao = db.farmersDao();

        new insertTraderAsyncTask(farmersDao).execute(farmerModel);
    }
    public void upDateRecord(FamerModel farmerModel){
        new updateTradersAsyncTask(db.farmersDao()).execute(farmerModel);
    }
    public void deleteRecord(FamerModel farmerModel){
        new deleteTradersAsyncTask(db.farmersDao()).execute(farmerModel);
    }
    private static class insertTraderAsyncTask extends AsyncTask<FamerModel, Void, Boolean> {

        private FarmersDao mAsyncTaskDao;

        insertTraderAsyncTask(FarmersDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final FamerModel... params) {
            mAsyncTaskDao.insertSingleFramer(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }
    private static class insertTradersAsyncTask extends AsyncTask<List<FamerModel>, Void, Boolean> {

        private FarmersDao mAsyncTaskDao;

        insertTradersAsyncTask(FarmersDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final List<FamerModel>... params) {
            mAsyncTaskDao.insertMultipleFramers(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }
    private static class updateTradersAsyncTask extends AsyncTask<FamerModel, Void, Boolean> {

        private FarmersDao mAsyncTaskDao;

        updateTradersAsyncTask(FarmersDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final FamerModel... params) {
            mAsyncTaskDao.updateRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }
    private static class deleteTradersAsyncTask extends AsyncTask<FamerModel, Void, Boolean> {

        private FarmersDao mAsyncTaskDao;

        deleteTradersAsyncTask(FarmersDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final FamerModel... params) {
            mAsyncTaskDao.deleteRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }


    public void insertMultipleTraders(List<FamerModel> traderModels){
        farmersDao = db.farmersDao();

        new insertTradersAsyncTask(farmersDao).execute(traderModels);
    }

    public void insertSingleTrader(FamerModel farmerModel,boolean isOnline){
        if(isOnline){
            insertSingleTrader(farmerModel);

        }else {
            db.farmersDao().insertSingleFramer(farmerModel);
        }
    }

    private void insertSingleTrader(FamerModel farmerModel) {
        
    }

    public LiveData<FamerModel> getFramerByKeyID(int key) {
        return db.farmersDao().getFramerByKeyID(key);
    }

    public LiveData<List<FamerModel>> getAllByArchivedStatus(int archived) {
        return db.farmersDao().getAllByArchivedStatus(archived);
    }

    public LiveData<List<FamerModel>> getAllByDeleteStatus(int status) {
        return db.farmersDao().getAllByDeleteStatus(status);
    }
    public LiveData<List<FamerModel>> getAllByDummyStatus(int status) {
        return db.farmersDao().getAllByDummyStatus(status);
    }
    public LiveData<List<FamerModel>> getAllByStatus(String status) {
        return db.farmersDao().getAllByStatus(status);
    }
    public LiveData<FamerModel> getFramerByCode(String code) {
        return db.farmersDao().getFramerByCode(code);
    }
    public LiveData<FamerModel> getFramersByNames(String name) {
        return db.farmersDao().getFramersByNames(name);
    }
    public LiveData<FamerModel> getFramersByRoute(String route) {
        return db.farmersDao().getFramersByRoute(route);
    }
    public LiveData<List<FamerModel>> getFramerByEntityCode(String entity) {
        return db.farmersDao().getFramerByEntityCode(entity);
    }
    public LiveData<List<FamerModel>> searchByNames(String names) {
        return db.farmersDao().searchByNames(names);
    }
    public LiveData<List<FamerModel>> searchByCode(String code) {
        return db.farmersDao().searchByCode(code);
    }
    public LiveData<List<FamerModel>> searchByMobile(String mobile) {
        return db.farmersDao().searchByMobile(mobile);
    }
    public LiveData<List<FamerModel>> search(String names,String moile,int dummy,int deleted,int archived) {
        return db.farmersDao().search( names, moile, dummy, deleted, archived);
    }


}
