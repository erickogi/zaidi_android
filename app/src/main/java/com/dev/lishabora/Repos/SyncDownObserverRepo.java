package com.dev.lishabora.Repos;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.dev.lishabora.Database.LMDatabase;
import com.dev.lishabora.Database.SyncObserverDao;
import com.dev.lishabora.Models.SyncDownObserver;

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
