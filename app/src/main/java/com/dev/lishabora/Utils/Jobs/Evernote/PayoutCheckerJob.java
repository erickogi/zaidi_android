package com.dev.lishabora.Utils.Jobs.Evernote;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.dev.lishabora.Application;
import com.dev.lishabora.Models.Notifications;
import com.dev.lishabora.Repos.Trader.PayoutsRepo;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishabora.Views.Trader.Activities.TraderActivity;
import com.dev.lishaboramobile.R;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PayoutCheckerJob extends Job {

    static final String TAG = "sync_job_pay_checker";

    public static void schedulePeriodic() {
        Log.d("jobadd", "job shedule periodic" + TAG);
        new JobRequest.Builder(PayoutCheckerJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(300), TimeUnit.MINUTES.toMillis(5))
                .setUpdateCurrent(true)
                //.setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(false)
                //.setRequirementsEnforced(true)

                //.startNow()
                //.setPersisted(true)
                .build()
                .schedule();
    }

    private void sendNotification(String title, String message, boolean isloggedIn) {

        // if (isloggedIn) {
            Intent intent = new Intent(Application.context, TraderActivity.class);
            intent.putExtra("type", "notification_fragment");


            PendingIntent pi = PendingIntent.getActivity(Application.context, 0,
                    intent, 0);

            Notification notification = new NotificationCompat.Builder(Application.context)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pi)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setShowWhen(true)
                    .setColor(Color.RED)
                    .setLocalOnly(true)
                    .build();


        NotificationManagerCompat.from(Application.context)
                .notify(5, notification);

        // }
    }

    @Override
    protected Result onRunJob(Params params) {
        List<Notifications> notifications = CommonFuncs.getPendingPayouts(new PayoutsRepo(Application.application).getPayoutsByStatusD("0"));


        //new NotificationRepo(Application.application).insertMultipleNotificationss(notifications);
        if (notifications != null) {
            String title = "";
            String message = "";

            if (notifications.size() > 1) {
                title = "Hi . " + new PrefrenceManager(Application.context).getTraderModel().getNames();
                message = "You have " + notifications.size() + " Pending payouts that require approval";

                //if (new PrefrenceManager(Application.context).isLoggedIn()) {
                    sendNotification(title, message, true);
                //}

            } else if (notifications.size() == 1) {
                title = "Hi . " + new PrefrenceManager(Application.context).getTraderModel().getNames() + " You have 1 " + notifications.get(0).getTitle();
                message = notifications.get(0).getMessage();

                //if (new PrefrenceManager(Application.context).isLoggedIn()) {
                sendNotification(title, message, true);
                //}

            }
        }


        return Result.SUCCESS;
    }


}
