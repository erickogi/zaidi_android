package com.dev.zaidi.Jobs.WorkManager;


import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class UpWorkCreator {

    public  void createUpSyncWork(){
         PeriodicWorkRequest.Builder work = new PeriodicWorkRequest.Builder(UpWork.class, 15,
                        TimeUnit.MINUTES);
//        Constraints constraints=new Constraints();
//        constraints.setRequiredNetworkType(NetworkType.CONNECTED);
//        constraints.setRequiresBatteryNotLow(true);
//
//         work.setConstraints(constraints);

// ...if you want, you can apply constraints to the builder here...
// Create the actual work object:
        PeriodicWorkRequest myWork = work.build();
// Then enqueue the recurring task:
        WorkManager.getInstance().enqueue(myWork);
    }
}
