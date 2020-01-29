package com.dev.zaidi.Repos;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.dev.zaidi.Database.LMDatabase;
import com.dev.zaidi.Database.SyncObserverDao;
import com.dev.zaidi.Models.SyncDownObserver;

public class SyncDownObserverRepo {
    private SyncObserverDao syncDao;


    private LMDatabase db;


    public SyncDownObserverRepo(Application application) {
        db = LMDatabase.getDatabase(application);
        syncDao = db.syncObserverDao();
    }


    public LiveData<SyncDownObserver> getObserver() {
        return syncDao.getRecordObserve();
    }


}
