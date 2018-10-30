package com.dev.lishabora.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.ImageView;

import com.dev.lishabora.Application;
import com.dev.lishabora.COntrollers.LoginController;
import com.dev.lishabora.Database.LMDatabase;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.Views.Admin.Activities.AdminsActivity;
import com.dev.lishabora.Views.Login.Activities.LoginActivity;
import com.dev.lishabora.Views.Trader.Activities.SyncWorks;
import com.dev.lishabora.Views.Trader.Activities.TraderActivity;
import com.dev.lishaboramobile.R;

import java.util.Objects;

import static com.dev.lishabora.Application.isTimeAutomatic;

public class SplashActivity extends AppCompatActivity {

    private ImageView imageView;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        imageView = findViewById(R.id.image);

        imageView.setImageResource(R.drawable.ic_launcher);

        //setContentView(R.layout.activity_trader);

        if (isTimeAutomatic()) {
            start();
        } else {
            timeIs();
        }

    }

    private void timeIs() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(this));
        alertDialogBuilderUserInput.setTitle("Time Error");
        alertDialogBuilderUserInput.setMessage("Your time and Date settings is set to manual time settings.. For this app to run you need to enable automatic time settings");


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Okay", (dialogBox, id) -> {
                    // ToDo get user input here
                    // startActivity(new Intent(SplashActivity.this, SyncWorks.class));
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);

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

    private void sync(int days) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(this));
        alertDialogBuilderUserInput.setTitle("Sync OverLoad ");
        alertDialogBuilderUserInput.setMessage("It seems that you have not synced your data for  " + String.valueOf(days) + "  \nTo continue using the app fully, you will need to sync you data now .\n Press SYNC to do so  thank you.");


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("SYNC", (dialogBox, id) -> {
                    startActivity(new Intent(SplashActivity.this, SyncWorks.class));


                })

                .setNegativeButton("Skip for now",
                        (dialogBox, id) -> {

                            Intent intent = new Intent(this, TraderActivity.class);
                            startActivity(intent);
                            finish();
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        alertDialogAndroid.show();


    }

    public void start() {

        new Thread(() -> {
            PrefrenceManager globalPrefs = new PrefrenceManager(SplashActivity.this);
            if (globalPrefs.isLoggedIn()) {
                LMDatabase lmDatabase = LMDatabase.getDatabase(Application.context);

                switch (globalPrefs.getTypeLoggedIn()) {
                    case LoginController.ADMIN:
                        intent = new Intent(SplashActivity.this, AdminsActivity.class);

                        break;
                    case LoginController.TRADER:
                        String xTime = globalPrefs.getLastTransactionTIme();
                        if (xTime.equals("")) {
                            intent = new Intent(SplashActivity.this, TraderActivity.class);

                        } else {
                            int daysBtwen = DateTimeUtils.Companion.calcDiff(DateTimeUtils.Companion.conver2Date(xTime), DateTimeUtils.Companion.getTodayDate()).getDays();

                            try {
                                if (daysBtwen > 7 && lmDatabase.syncDao().getCount() > 0) {
                                    runOnUiThread(() -> sync(daysBtwen));
                                } else {
                                    intent = new Intent(SplashActivity.this, TraderActivity.class);

                                }
                            } catch (Exception nm) {
                                nm.printStackTrace();
                                intent = new Intent(SplashActivity.this, TraderActivity.class);

                            }
                        }
                        break;


                    default:
                        intent = new Intent(SplashActivity.this, LoginActivity.class);


                }
            } else {
                intent = new Intent(SplashActivity.this, LoginActivity.class);


            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                    finish();
                }
            });
        }).start();

//

    }
}
