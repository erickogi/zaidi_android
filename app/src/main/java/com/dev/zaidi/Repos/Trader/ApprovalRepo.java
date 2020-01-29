package com.dev.zaidi.Repos.Trader;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dev.zaidi.Database.ApprovalRegisterDao;
import com.dev.zaidi.Database.LMDatabase;
import com.dev.zaidi.Models.ApprovalRegisterModel;

import java.util.List;

public class ApprovalRepo {
    private ApprovalRegisterDao dao;


    private LMDatabase db;


    public ApprovalRepo(Application application) {
        db = LMDatabase.getDatabase(application);
        dao = db.approvalRegisterDao();
    }


    public void insertMultiple(List<ApprovalRegisterModel> approvalRegisterModel) {
        new insertAsyncsTasks(dao).execute(approvalRegisterModel);
    }

    public void insert(ApprovalRegisterModel approvalRegisterModel) {
        new insertAsyncTask(dao).execute(approvalRegisterModel);
    }

    public void insertDirect(ApprovalRegisterModel approvalRegisterModel) {
        // new insertAsyncTask(dao).execute(approvalRegisterModel);
        try {
            dao.insertSingle(approvalRegisterModel);
        } catch (Exception nm) {
            nm.printStackTrace();
        }
    }


    public LiveData<List<ApprovalRegisterModel>> fetchAll() {
        return dao.fetchAllData();
    }

    public List<ApprovalRegisterModel> fetchAllOne() {
        return dao.fetchAllDataOne();
    }

    public LiveData<ApprovalRegisterModel> getByFarmerPayoutCode(String farmerCode, String payoutCode) {
        return dao.getByFarmerPayoutCode(farmerCode, payoutCode);
    }

    public ApprovalRegisterModel getByFarmerPayoutCodeOne(String farmerCode, String payoutCode) {
        return dao.getByFarmerPayoutCodeOne(farmerCode, payoutCode);
    }


    public void updateRecord(ApprovalRegisterModel approvalRegisterModel) {
        new updateAsyncTask(dao).execute(approvalRegisterModel);
    }

    public void updateRecordDirect(ApprovalRegisterModel approvalRegisterModel) {
        // new updateAsyncTask(dao).execute(approvalRegisterModel);
        try {
            dao.updateRecord(approvalRegisterModel);
        } catch (Exception nm) {
            nm.printStackTrace();
        }
    }


    public void deleteRecord(ApprovalRegisterModel approvalRegisterModel) {
        new deleteAsyncTask(dao).execute(approvalRegisterModel);
    }


    private static class insertAsyncTask extends AsyncTask<ApprovalRegisterModel, Void, Boolean> {

        private ApprovalRegisterDao mAsyncTaskDao;

        insertAsyncTask(ApprovalRegisterDao approvalRegisterDao) {
            mAsyncTaskDao = approvalRegisterDao;
        }

        @Override
        protected Boolean doInBackground(final ApprovalRegisterModel... params) {
            try {
                mAsyncTaskDao.insertSingle(params[0]);
            } catch (Exception nm) {

            }
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class insertAsyncsTasks extends AsyncTask<List<ApprovalRegisterModel>, Void, Boolean> {

        private ApprovalRegisterDao mAsyncTaskDao;

        insertAsyncsTasks(ApprovalRegisterDao syncDao) {
            mAsyncTaskDao = syncDao;
        }

        @Override
        protected Boolean doInBackground(final List<ApprovalRegisterModel>... params) {
            try {
                mAsyncTaskDao.insertMultiple(params[0]);
            } catch (Exception nm) {

            }
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class updateAsyncTask extends AsyncTask<ApprovalRegisterModel, Void, Boolean> {

        private ApprovalRegisterDao mAsyncTaskDao;

        updateAsyncTask(ApprovalRegisterDao syncDao) {
            mAsyncTaskDao = syncDao;
        }

        @Override
        protected Boolean doInBackground(final ApprovalRegisterModel... params) {
            mAsyncTaskDao.updateRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class deleteAsyncTask extends AsyncTask<ApprovalRegisterModel, Void, Boolean> {

        private ApprovalRegisterDao mAsyncTaskDao;

        deleteAsyncTask(ApprovalRegisterDao syncDao) {
            mAsyncTaskDao = syncDao;
        }

        @Override
        protected Boolean doInBackground(final ApprovalRegisterModel... params) {
            mAsyncTaskDao.deleteRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }


}
