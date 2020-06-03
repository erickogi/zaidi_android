package com.dev.zaidi;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import com.dev.zaidi.Database.LMDatabase;
import com.dev.zaidi.Models.NetworkAnalytics;
import com.dev.zaidi.Models.SyncHolderModel;
import com.dev.zaidi.Models.SyncModel;
import com.dev.zaidi.Models.SyncResponseModel;
import com.dev.zaidi.Models.Trader.TraderModel;
import com.dev.zaidi.Network.ApiConstants;
import com.dev.zaidi.Network.Request;
import com.dev.zaidi.Utils.DateTimeUtils;
import com.dev.zaidi.Utils.FetchDeviceData;
import com.dev.zaidi.Utils.Logs;
import com.dev.zaidi.Utils.NetworkUtils;
import com.dev.zaidi.Utils.PrefrenceManager;
import com.dev.zaidi.Utils.SyncResponseCallback;
import com.dev.zaidi.Utils.SystemInfo;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SyncJob extends JobIntentService {
    private static final int JOB_ID = 1;
    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, SyncJob.class, JOB_ID, intent);
    }
    LMDatabase lmDatabase ;
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        lmDatabase = LMDatabase.getDatabase(this);
        sync();
    }
    public  void sync() {


        try {
            List<SyncModel> list = lmDatabase.syncDao().getAllByStatusRaw(new PrefrenceManager(this).syncNumber());
            if (list != null && list.size() > 0) {

                List<SyncModel> syncWorks1 = new LinkedList<>();
                syncWorks1.addAll(list);


                PrefrenceManager p = new PrefrenceManager(this);
                SyncHolderModel s = new SyncHolderModel();


                s.setEntityCode(p.getTraderModel().getCode());
                s.setEntityType(AppConstants.ENTITY_TRADER);
                s.setSyncModels(syncWorks1);
                s.setSyncType(1);
                s.setTime(DateTimeUtils.Companion.getNow());


                //IF SENDING DATA
                if (NetworkUtils.Companion.isConnectionFast(this)) {
                    s.setSystemInfo(new FetchDeviceData().fetchDetails());
                } else {
                    s.setSystemInfo(new SystemInfo());
                }


                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(s));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sync(jsonObject, syncWorks1);

            }


        } catch (Exception nm) {
            nm.printStackTrace();

        }

    }

    //    public static prepareSync(){
//
//    }
    public  <T> List<List<T>> getBatches(List<T> collection,int batchSize){
        int i = 0;
        List<List<T>> batches = new ArrayList<List<T>>();
        while(i<collection.size()){
            int nextInc = Math.min(collection.size()-i,batchSize);
            List<T> batch = collection.subList(i,i+nextInc);
            batches.add(batch);
            i = i + nextInc;
        }

        return batches;
    }

    int attemptts = 0;
    public  void sync(JSONObject jsonObject, List<SyncModel> syncWorks) {

        TraderModel traderModel = lmDatabase.tradersDao().getTraderByCodeOne(new PrefrenceManager(this).getCode());
        if (traderModel != null) {
            traderModel.setSynchingStatus(1);

            lmDatabase.tradersDao().updateRecord(traderModel);

        }
        try {
            Request.Companion.getResponseSync(this, ApiConstants.Companion.getSync(), jsonObject, "", new SyncResponseCallback() {
                @Override
                public void response(SyncResponseModel responseModel, NetworkAnalytics analytics) {


                    if (traderModel != null) {
                        traderModel.setSynchingStatus(0);
                        lmDatabase.tradersDao().updateRecord(traderModel);

                    }

                    if (responseModel.getResultCode() == 2) {
                        int failureId = Integer.valueOf(responseModel.getFailureId());
                        for (int a = failureId; a > 0; a--) {

                            for (SyncModel d : syncWorks) {
                                if (d.getId() == a) {
                                    deleteSync(d);
                                }
                            }
                        }
                        sync();
                    } else if (responseModel.getResultCode() == 1) {
                        for (SyncModel s : syncWorks) {
                            deleteSync(s);
                        }
                        sync();
                    }


                }

                @Override
                public void response(String error, NetworkAnalytics analytics) {
                    Logs.Companion.d("syncresponse", error);

                    if (traderModel != null) {
                        traderModel.setSynchingStatus(2);
                        traderModel.setLastsynchingMessage(error);
                        lmDatabase.tradersDao().updateRecord(traderModel);

                    }
                    attemptts++;
                    if(attemptts<=10){
                        sync();
                    }


                }


            });
        } catch (Exception nm) {
            nm.printStackTrace();
            if (traderModel != null) {
                traderModel.setSynchingStatus(0);

                lmDatabase.tradersDao().updateRecord(traderModel);

            }

        }

    }
    public  void deleteSync(SyncModel s) {
        LMDatabase lmDatabase = LMDatabase.getDatabase(this);
        lmDatabase.syncDao().deleteRecord(s);

        new PrefrenceManager(this).setLastTransaction(DateTimeUtils.Companion.getNow());

    }

}
