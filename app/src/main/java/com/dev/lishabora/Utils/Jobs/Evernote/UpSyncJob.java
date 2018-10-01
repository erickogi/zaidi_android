package com.dev.lishabora.Utils.Jobs.Evernote;

import android.content.Context;
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
        Context context = getContext();

        Application.sync();

//        LMDatabase lmDatabase = LMDatabase.getDatabase(context);
//        //lmDatabase.cl
//        List<SyncModel> list = lmDatabase.syncDao().getAllByStatusRaw(0);
//        Intent intent = new Intent(getContext(), SyncWorks.class);
//        intent.putExtra("type", "notification_cart");
//
//
//        PendingIntent pi = PendingIntent.getActivity(getContext(), 0,
//                intent, 0);
//
//        if (list != null && list.size() > 0) {
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "ctx")
//                    .setSmallIcon(R.drawable.ic_dehaze_black_24dp)
//                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
//                    .setSmallIcon(R.drawable.ic_launcher_background)
//                    .setContentTitle("Sync Worker class Notification")
//                    .setContentIntent(pi)
//
//                    .setContentText("You have content  waiting for sync .... \nWe are working on implementing sync sit tight");
//
//            android.app.NotificationManager notificationManager =
//                    (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//            if (notificationManager != null) {
//                // notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
//                notificationManager.notify(0, notificationBuilder.build());
//            }
//
//        }




        return Result.SUCCESS;
    }
}
