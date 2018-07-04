package com.dev.lishaboramobile.Global.Data.Operations.Repo;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.dev.lishaboramobile.Global.Data.LMDatabase;
import com.dev.lishaboramobile.Global.Data.Operations.TradersDao;
import com.dev.lishaboramobile.Trader.Models.TraderModel;

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

    public LiveData<List<TraderModel>> getTraders() {
        tradersDao = db.tradersDao();
        return tradersDao.fetchAllData();
    }



}
