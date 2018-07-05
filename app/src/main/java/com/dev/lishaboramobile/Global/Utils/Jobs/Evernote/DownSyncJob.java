package com.dev.lishaboramobile.Global.Utils.Jobs.Evernote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import java.util.concurrent.TimeUnit;

public class DownSyncJob extends Job {

    static final String TAG = "down_sync_job";

    public static void schedulePeriodic() {
        Log.d("jobadd", "job shedule periodic");
        new JobRequest.Builder(DownSyncJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(5))
                .setUpdateCurrent(true)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)

                //.startNow()
                //.setPersisted(true)
                .build()
                .schedule();
    }

    static void scheduleExact() {
        new JobRequest.Builder(DownSyncJob.TAG)
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

        return Result.SUCCESS;
    }
}
