package com.dev.lishaboramobile.Global.Data.Operations.Repo;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dev.lishaboramobile.Global.Data.LMDatabase;
import com.dev.lishaboramobile.Global.Data.Operations.Dao.RoutesDao;
import com.dev.lishaboramobile.Trader.Models.RoutesModel;

import java.util.List;

public class RoutesRepo {
    private RoutesDao routesDao;
    private LiveData<List<RoutesModel>> routes;
    private LiveData<RoutesModel> route;


    private LMDatabase db;


    public RoutesRepo(Application application) {
        db = LMDatabase.getDatabase(application);
        routesDao=db.routesDao();
    }


    public LiveData<List<RoutesModel>> fetchAllData(boolean isOnline) {
        if(isOnline){
            return null;
        }else {
            routesDao = db.routesDao();
            return routesDao.fetchAllData();
        }
    }

    public void insert (RoutesModel routesModel) {
        routesDao = db.routesDao();

        new insertTraderAsyncTask(routesDao).execute(routesModel);
    }
    public void upDateRecord(RoutesModel routesModel){
        new updateRoutesAsyncTask(db.routesDao()).execute(routesModel);
    }
    public void deleteRecord(RoutesModel routesModel){
        new deleteRoutesAsyncTask(db.routesDao()).execute(routesModel);
    }
    private static class insertTraderAsyncTask extends AsyncTask<RoutesModel, Void, Boolean> {

        private RoutesDao mAsyncTaskDao;

        insertTraderAsyncTask(RoutesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final RoutesModel... params) {
            mAsyncTaskDao.insertSingleRoute(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }
    private static class insertRoutesAsyncTask extends AsyncTask<List<RoutesModel>, Void, Boolean> {

        private RoutesDao mAsyncTaskDao;

        insertRoutesAsyncTask(RoutesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final List<RoutesModel>... params) {
            mAsyncTaskDao.insertMultipleRoutes(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }
    private static class updateRoutesAsyncTask extends AsyncTask<RoutesModel, Void, Boolean> {

        private RoutesDao mAsyncTaskDao;

        updateRoutesAsyncTask(RoutesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final RoutesModel... params) {
            mAsyncTaskDao.updateRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }
    private static class deleteRoutesAsyncTask extends AsyncTask<RoutesModel, Void, Boolean> {

        private RoutesDao mAsyncTaskDao;

        deleteRoutesAsyncTask(RoutesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final RoutesModel... params) {
            mAsyncTaskDao.deleteRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }


    public void insertMultipleRoutes(List<RoutesModel> traderModels){
        routesDao = db.routesDao();

        new insertRoutesAsyncTask(routesDao).execute(traderModels);
    }

    public void insertSingleTrader(RoutesModel routesModel,boolean isOnline){
        if(isOnline){
            insertSingleRoute(routesModel);

        }else {
            db.routesDao().insertSingleRoute(routesModel);
        }
    }

    private void insertSingleRoute(RoutesModel routesModel) {

    }

    public LiveData<RoutesModel> getRouteByKeyID(int key) {
        return db.routesDao().getRouteByKeyID(key);
    }


    public LiveData<List<RoutesModel>> getAllByStatus(String status) {
        return db.routesDao().getAllByStatus(status);
    }
    public LiveData<RoutesModel> getRouteByCode(String code) {
        return db.routesDao().getRouteByCode(code);
    }
    public LiveData<RoutesModel> getRouteByRoute(String route) {
        return db.routesDao().getRouteByRoute(route);
    }
    public LiveData<List<RoutesModel>> getRoutesByEntityCode(String entity) {
        return db.routesDao().getRoutesByEntityCode(entity);
    }
    public LiveData<List<RoutesModel>> searchByRoute(String route) {
        return db.routesDao().searchByRoute(route);
    }



}
