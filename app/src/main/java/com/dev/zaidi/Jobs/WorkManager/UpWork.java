package com.dev.zaidi.Jobs.WorkManager;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.dev.zaidi.SyncJob;
public class UpWork extends Worker {
    public UpWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        doUpSync();
        return Result.success();
    }

//    @NonNull
//    @Override
//    public WorkerResult doWork() {
//
//        doUpSync();
//
//        return WorkerResult.SUCCESS;
//    }

    private void doUpSync() {

      //  getApplicationContext().startService(new Intent(getApplicationContext(), SyncService.class));

        Intent mIntent = new Intent(getApplicationContext(), SyncJob.class);
        SyncJob.enqueueWork(getApplicationContext(), mIntent);

      // new Handler().post(() -> getApplicationContext().startService(new Intent(getApplicationContext(), SyncService.class)));
        Log.d("upworkwork","mehereeree");
       // new Thread(Application::sync).start();
        //Application.sync();


    }
}
