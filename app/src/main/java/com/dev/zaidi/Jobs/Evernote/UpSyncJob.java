package com.dev.zaidi.Jobs.Evernote;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.dev.zaidi.SyncService;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import java.util.concurrent.TimeUnit;

public class UpSyncJob extends Job {

    static final String TAG = "sync_job_up";

    public static void schedulePeriodic() {
        new JobRequest.Builder(UpSyncJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(5))
                .setUpdateCurrent(true)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(false)

                .build()
                .schedule();
    }

    public static void scheduleExact() {
        new JobRequest.Builder(UpSyncJob.TAG)
                .setExact(TimeUnit.MINUTES.toMillis(10))
                .setUpdateCurrent(true)
                .startNow()
                .build()
                .schedule();
    }
    public static void sync() {
      //  this.startService(new Intent(getContext(), SyncService.class));
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {

       // new Thread(Application::sync).start();
        getContext().startService(new Intent(getContext(), SyncService.class));

//        Intent mIntent = new Intent(getContext(), SyncJob.class);
//        SyncJob.enqueueWork(getContext(), mIntent);
       // new Handler().post(()-> getContext().startService(new Intent(getContext(), SyncService.class)));


        return Result.SUCCESS;
    }
}
