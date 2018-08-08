package com.dev.lishabora.Utils.Jobs.Evernote;

import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by Eric on 2/20/2018.
 */

public class SyncJobCreator implements JobCreator {

    @Override
    public Job create(String tag) {

        Log.d("jobadd", "job added");
        switch (tag) {

            case UpSyncJob.TAG:
                Log.d("jobadd", "job added upSync");
                return new UpSyncJob();
            case DownSyncJob.TAG:
                Log.d("jobadd", "job added downSync");
                return new DownSyncJob();
            case SyncNotifierJob.TAG:
                Log.d("jobadd", "job added downSync");
                return new SyncNotifierJob();
            default:
                return null;
        }
    }
}