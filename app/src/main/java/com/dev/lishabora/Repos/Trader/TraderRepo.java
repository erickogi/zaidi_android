package com.dev.lishabora.Repos.Trader;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dev.lishabora.Database.LMDatabase;
import com.dev.lishabora.Database.TradersDao;
import com.dev.lishabora.Models.Trader.TraderModel;

import java.util.List;

public class TraderRepo {
    private TradersDao tradersDao;
    private LiveData<List<TraderModel>> traders;
    private LiveData<TraderModel> trader;


    private LMDatabase db;


    public TraderRepo(Application application) {
        db = LMDatabase.getDatabase(application);
        tradersDao=db.tradersDao();
    }


    public LiveData<List<TraderModel>> fetchAllData(boolean isOnline) {
        if(isOnline){
            return null;
        }else {
            tradersDao = db.tradersDao();
            return tradersDao.fetchAllData();
        }
    }

    public void insert (TraderModel traderModel) {
        tradersDao = db.tradersDao();

        new insertTraderAsyncTask(tradersDao).execute(traderModel);
    }
    public void upDateRecord(TraderModel traderModel){
        new updateTradersAsyncTask(db.tradersDao()).execute(traderModel);
    }
    public void deleteRecord(TraderModel traderModel){
        new deleteTradersAsyncTask(db.tradersDao()).execute(traderModel);
    }
    private static class insertTraderAsyncTask extends AsyncTask<TraderModel, Void, Boolean> {

        private TradersDao mAsyncTaskDao;

        insertTraderAsyncTask(TradersDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final TraderModel... params) {
            mAsyncTaskDao.insertSingleTrader(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }
    private static class insertTradersAsyncTask extends AsyncTask<List<TraderModel>, Void, Boolean> {

        private TradersDao mAsyncTaskDao;

        insertTradersAsyncTask(TradersDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final List<TraderModel>... params) {
            mAsyncTaskDao.insertMultipleTraders(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }
    private static class updateTradersAsyncTask extends AsyncTask<TraderModel, Void, Boolean> {

        private TradersDao mAsyncTaskDao;

        updateTradersAsyncTask(TradersDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final TraderModel... params) {
            mAsyncTaskDao.updateRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }
    private static class deleteTradersAsyncTask extends AsyncTask<TraderModel, Void, Boolean> {

        private TradersDao mAsyncTaskDao;

        deleteTradersAsyncTask(TradersDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final TraderModel... params) {
            mAsyncTaskDao.deleteRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }


    public void insertMultipleTraders(List<TraderModel> traderModels){
        tradersDao = db.tradersDao();

        new insertTradersAsyncTask(tradersDao).execute(traderModels);
    }

    public void insertSingleTrader(TraderModel traderModel,boolean isOnline){
        if(isOnline){
            insertSingleTrader(traderModel);

        }else {
            db.tradersDao().insertSingleTrader(traderModel);
        }
    }

    private void insertSingleTrader(TraderModel traderModel) {

    }

    public LiveData<TraderModel> getTraderByKeyID(int key) {
        return db.tradersDao().getTraderByKeyID(key);
    }

    public LiveData<List<TraderModel>> getAllByArchivedStatus(int archived) {
        return db.tradersDao().getAllByArchivedStatus(archived);
    }

    public LiveData<List<TraderModel>> getAllByDeleteStatus(int status) {
        return db.tradersDao().getAllByDeleteStatus(status);
    }
    public LiveData<List<TraderModel>> getAllByDummyStatus(int status) {
        return db.tradersDao().getAllByDummyStatus(status);
    }
    public LiveData<List<TraderModel>> getAllByStatus(String status) {
        return db.tradersDao().getAllByStatus(status);
    }
    public LiveData<TraderModel> getTraderByCode(String code) {
        return db.tradersDao().getTraderByCode(code);
    }
    public LiveData<TraderModel> getTradersByNames(String name) {
        return db.tradersDao().getTradersByNames(name);
    }
    public LiveData<List<TraderModel>> getTradersByEntityCode(String entity) {
        return db.tradersDao().getTradersByEntityCode(entity);
    }
    public LiveData<List<TraderModel>> searchByNames(String names) {
        return db.tradersDao().searchByNames(names);
    }
    public LiveData<List<TraderModel>> searchByCode(String code) {
        return db.tradersDao().searchByCode(code);
    }
    public LiveData<List<TraderModel>> searchByMobile(String mobile) {
        return db.tradersDao().searchByMobile(mobile);
    }
    public LiveData<List<TraderModel>> search(String names,String moile,int dummy,int deleted,int archived) {
        return db.tradersDao().search( names, moile, dummy, deleted, archived);
    }


}
