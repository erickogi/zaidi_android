package com.dev.lishabora.Repos;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dev.lishabora.Database.LMDatabase;
import com.dev.lishabora.Database.NotificationsDao;
import com.dev.lishabora.Models.Notifications;

import java.util.List;

public class NotificationRepo {
    private NotificationsDao dao;
    private LiveData<List<Notifications>> routes;
    private LiveData<Notifications> route;


    private LMDatabase db;


    public NotificationRepo(Application application) {
        db = LMDatabase.getDatabase(application);
        dao = db.notificationsDao();
    }


    public LiveData<List<Notifications>> fetchAllData() {

        return dao.fetchAllData();

    }

    public LiveData<List<Notifications>> fetchAllData(int viewd) {

        return dao.fetchAllData(viewd);

    }

    public void insert(Notifications routesModel) {

        new insertTraderAsyncTask(dao).execute(routesModel);
    }

    public void upDateRecord(Notifications routesModel) {
        new updateNotificationssAsyncTask(dao).execute(routesModel);
    }

    public void deleteRecord(Notifications routesModel) {
        new deleteNotificationssAsyncTask(dao).execute(routesModel);
    }

    public void insertMultipleNotificationss(List<Notifications> traderModels) {

        new insertNotificationssAsyncTask(dao).execute(traderModels);
    }

    public void insertSingleTrader(Notifications routesModel, boolean isOnline) {
        if (isOnline) {
            insertSingleNotifications(routesModel);

        } else {
            dao.insertSingleNotification(routesModel);
        }
    }

    private void insertSingleNotifications(Notifications routesModel) {

    }

    public LiveData<Integer> getCount() {
        return dao.getNumberOfRows();
    }

    private static class insertTraderAsyncTask extends AsyncTask<Notifications, Void, Boolean> {

        private NotificationsDao mAsyncTaskDao;

        insertTraderAsyncTask(NotificationsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final Notifications... params) {
            mAsyncTaskDao.insertSingleNotification(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class insertNotificationssAsyncTask extends AsyncTask<List<Notifications>, Void, Boolean> {

        private NotificationsDao mAsyncTaskDao;

        insertNotificationssAsyncTask(NotificationsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final List<Notifications>... params) {
            mAsyncTaskDao.insertMultipleNotifications(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class updateNotificationssAsyncTask extends AsyncTask<Notifications, Void, Boolean> {

        private NotificationsDao mAsyncTaskDao;

        updateNotificationssAsyncTask(NotificationsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final Notifications... params) {
            mAsyncTaskDao.updateRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class deleteNotificationssAsyncTask extends AsyncTask<Notifications, Void, Boolean> {

        private NotificationsDao mAsyncTaskDao;

        deleteNotificationssAsyncTask(NotificationsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final Notifications... params) {
            mAsyncTaskDao.deleteRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }


}
