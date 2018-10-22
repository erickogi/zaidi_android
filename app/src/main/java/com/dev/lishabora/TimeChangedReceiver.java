package com.dev.lishabora;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.WindowManager;

import java.util.Objects;

import static com.dev.lishabora.Application.isTimeAutomatic;

public class TimeChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Do whatever you need to

        if (isTimeAutomatic()) {
        } else {
            // new PrefrenceManager(Application.context).setTimeI
            // timeIs();
        }

        // UpSyncJob.schedulePeriodic();

    }

    private void timeIs() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(Application.context);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(Application.context));
        alertDialogBuilderUserInput.setTitle("Time Error");
        alertDialogBuilderUserInput.setMessage("Your time and Date settings is set to manual time settings.. For this app to run you need to enable automatic time settings");


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Okay", (dialogBox, id) -> {
                    // ToDo get user input here
                    // startActivity(new Intent(SplashActivity.this, SyncWorks.class));
                    //  startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);

                });

//                .setNegativeButton("Skip for now",
//                        (dialogBox, id) -> {
//                            //  dialogBox.cancel();
//                            Intent intent = new Intent(this, TraderActivity.class);
//                            startActivity(intent);
//                            finish();
//                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        alertDialogAndroid.show();


    }


}
