package com.dev.lishabora.Utils.Jobs.Evernote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.dev.lishabora.Application;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import java.util.concurrent.TimeUnit;

public class UpSyncJob extends Job {

    static final String TAG = "sync_job";

    public static void schedulePeriodic() {
        Log.d("jobadd", "job shedule periodic");
        new JobRequest.Builder(UpSyncJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(5))
                .setUpdateCurrent(true)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(false)
                //.setRequirementsEnforced(true)

                //.startNow()
                //.setPersisted(true)
                .build()
                .schedule();
    }

    static void scheduleExact() {
        new JobRequest.Builder(UpSyncJob.TAG)
                .setExact(TimeUnit.MINUTES.toMillis(10))
                //.setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(5))
                .setUpdateCurrent(true)
                .startNow()
                //.setPersisted(true)
                .build()
                .schedule();
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {

        Application.sync();
        //Application.syncDown();






        return Result.SUCCESS;
    }
}
