package com.dev.lishabora.Jobs.Evernote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import java.util.concurrent.TimeUnit;

public class SyncNotifierJob extends Job {

    static final String TAG = "sync_notifier_job";

    public static void schedulePeriodic() {
        Log.d("jobadd", "job shedule periodic");
        new JobRequest.Builder(SyncNotifierJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(720), TimeUnit.MINUTES.toMillis(5))
                .setUpdateCurrent(true)

                //.startNow()
                //.setPersisted(true)
                .build()
                .schedule();
    }

    static void scheduleExact() {
        new JobRequest.Builder(SyncNotifierJob.TAG)
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
