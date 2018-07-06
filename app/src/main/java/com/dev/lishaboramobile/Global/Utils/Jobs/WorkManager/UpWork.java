package com.dev.lishaboramobile.Global.Utils.Jobs.WorkManager;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.dev.lishaboramobile.Global.Data.LMDatabase;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Trader.Data.TradersViewModel;

import androidx.work.Worker;

public class UpWork extends Worker {
    @NonNull
    @Override
    public WorkerResult doWork() {

       // doUpSync();

        return WorkerResult.SUCCESS;
    }

    private void doUpSync() {

        Context context=getApplicationContext();

//        LMDatabase lmDatabase=LMDatabase.getDatabase(context);
//        lmDatabase.tradersDao().getAllByArchivedStatus(1);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,"ctx")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Title")
                .setContentText("Desc");

        android.app.NotificationManager notificationManager =
                (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 , notificationBuilder.build());


        //TradersViewModel viewModel=ViewModelProviders.of(UpWork.class).get(TradersViewModel.class);
    }
}
