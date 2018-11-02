package com.dev.lishabora.Jobs.Evernote;

import android.support.annotation.NonNull;

import com.dev.lishabora.Application;
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

    @NonNull
    @Override
    protected Result onRunJob(Params params) {

        Application.sync();






        return Result.SUCCESS;
    }
}
