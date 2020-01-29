package com.dev.zaidi.Repos.Trader;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dev.zaidi.Database.LMDatabase;
import com.dev.zaidi.Database.UnitsDao;
import com.dev.zaidi.Models.UnitsModel;

import java.util.List;

public class UnitsRepo {
    private UnitsDao unitsDao;
    private LiveData<List<UnitsModel>> units;
    private LiveData<UnitsModel> unit;


    private LMDatabase db;


    public UnitsRepo(Application application) {
        db = LMDatabase.getDatabase(application);
        unitsDao=db.unitsDao();
    }


    public LiveData<List<UnitsModel>> fetchAllData(boolean isOnline) {
        if(isOnline){
            return null;
        }else {
            unitsDao = db.unitsDao();
            return unitsDao.fetchAllData();
        }
    }

    public void insert (UnitsModel unitsModel) {
        unitsDao = db.unitsDao();

        new insertUnitAsyncTask(unitsDao).execute(unitsModel);
    }
    public void insert (List<UnitsModel> unitsModel) {
        unitsDao = db.unitsDao();
        new insertUnitsAsyncTask(unitsDao).execute(unitsModel);
    }
    public void upDateRecord(UnitsModel unitsModel){
        new updateUnitAsyncTask(db.unitsDao()).execute(unitsModel);
    }
    public void deleteRecord(UnitsModel unitsModel){
        new deleteUnitAsyncTask(db.unitsDao()).execute(unitsModel);
    }
    private static class insertUnitAsyncTask extends AsyncTask<UnitsModel, Void, Boolean> {

        private UnitsDao mAsyncTaskDao;

        insertUnitAsyncTask(UnitsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final UnitsModel... params) {
            mAsyncTaskDao.insertSingleUnit(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }
    private static class insertUnitsAsyncTask extends AsyncTask<List<UnitsModel>, Void, Boolean> {

        private UnitsDao mAsyncTaskDao;

        insertUnitsAsyncTask(UnitsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final List<UnitsModel>... params) {
            mAsyncTaskDao.insertMultipleUnits(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }
    private static class updateUnitAsyncTask extends AsyncTask<UnitsModel, Void, Boolean> {

        private UnitsDao mAsyncTaskDao;

        updateUnitAsyncTask(UnitsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final UnitsModel... params) {
            mAsyncTaskDao.updateRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }
    private static class deleteUnitAsyncTask extends AsyncTask<UnitsModel, Void, Boolean> {

        private UnitsDao mAsyncTaskDao;

        deleteUnitAsyncTask(UnitsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final UnitsModel... params) {
            mAsyncTaskDao.deleteRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }


    public void insertMultipleUnits(List<UnitsModel> unitsModels) {
        unitsDao = db.unitsDao();

        new insertUnitsAsyncTask(unitsDao).execute(unitsModels);
    }

    public void insertSingleTrader(UnitsModel unitsModel,boolean isOnline){
        if(isOnline){
            insertSingleUnit(unitsModel);

        }else {
            db.unitsDao().insertSingleUnit(unitsModel);
        }
    }

    private void insertSingleUnit(UnitsModel unitsModel) {

    }

    public List<UnitsModel> getUnits() {
        return db.unitsDao().getUnits();
    }

    public LiveData<UnitsModel> getUnitByKeyCode(String code) {
        return db.unitsDao().getUnitByKeyCode(code);
    }

    
    public LiveData<List<UnitsModel>> getAllByStatus(String status) {
        return db.unitsDao().getAllByStatus(status);
    }
    public LiveData<UnitsModel> getUnitByCode(String code) {
        return db.unitsDao().getUnitByCode(code);
    }

    public LiveData<List<UnitsModel>> getUnitsByEntityCode(String entity) {
        return db.unitsDao().getUnitsByEntityCode(entity);
    }
    public LiveData<List<UnitsModel>> searchByUnit(String unit) {
        return db.unitsDao().searchByUnit(unit);
    }
 


}
