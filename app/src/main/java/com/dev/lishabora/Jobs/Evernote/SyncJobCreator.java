package com.dev.lishabora.Jobs.Evernote;

import com.dev.lishabora.Utils.Logs;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by Eric on 2/20/2018.
 */

public class SyncJobCreator implements JobCreator {

    @Override
    public Job create(String tag) {

        Logs.Companion.d("jobadd", "job added");
        switch (tag) {

            case UpSyncJob.TAG:
                Logs.Companion.d("jobadd", "job added upSync");
                return new UpSyncJob();
            case DownSyncJob.TAG:
                Logs.Companion.d("jobadd", "job added downSync");
                return new DownSyncJob();
            case SyncNotifierJob.TAG:
                Logs.Companion.d("jobadd", "job added syncnotifire");
                return new SyncNotifierJob();

            case PayoutCheckerJob.TAG:
                Logs.Companion.d("jobadd", "job added paychecker");
                return new PayoutCheckerJob();
            default:
                return null;
        }
    }
}