package com.dev.lishaboramobile.Global.Data.Operations.Repo;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dev.lishaboramobile.Global.Data.LMDatabase;
import com.dev.lishaboramobile.Global.Data.Operations.Dao.CyclesDao;
import com.dev.lishaboramobile.Trader.Models.Cycles;

import java.util.List;

public class CyclesRepo {
    private CyclesDao cyclesDao;
    private LiveData<List<Cycles>> cycles;
    private LiveData<Cycles> unit;


    private LMDatabase db;


    public CyclesRepo(Application application) {
        db = LMDatabase.getDatabase(application);
        cyclesDao = db.cyclesDao();
    }


    public LiveData<List<Cycles>> fetchAllData(boolean isOnline) {
        if (isOnline) {
            return null;
        } else {
            cyclesDao = db.cyclesDao();
            return cyclesDao.fetchAllData();
        }
    }


    public void insert(List<Cycles> cyclesModel) {
        cyclesDao = db.cyclesDao();

        new insertUnitsAsyncTask(cyclesDao).execute(cyclesModel);
    }

    public LiveData<Cycles> getCycleByKeyID(int key) {
        return db.cyclesDao().getCycleByKeyID(key);
    }

    private static class insertUnitsAsyncTask extends AsyncTask<List<Cycles>, Void, Boolean> {

        private CyclesDao mAsyncTaskDao;

        insertUnitsAsyncTask(CyclesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final List<Cycles>... params) {
            mAsyncTaskDao.insertMultipleCycles(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }


}
