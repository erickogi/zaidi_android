package com.dev.zaidi.FireBase;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.dev.zaidi.Application;
import com.dev.zaidi.R;
import com.dev.zaidi.Utils.Logs;
import com.dev.zaidi.Views.Trader.Activities.TraderActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Random;

import static com.dev.zaidi.Application.syncChanges;

//import com.erickogi14gmail.photozuri.Payments.Payments;

/**
 *
 * @author Eric
 * @date 2/20/2018
 */

public class FirebaseService extends FirebaseMessagingService {
    private String TAG = "taggs";
    public FirebaseService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //Log.d(TAG, "From: " + remoteMessage.getFrom());
        //  handleNow(remoteMessage.getNotification().getBody());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Logs.Companion.d(TAG, "Message data payload: " , remoteMessage.getData());

            handleNow(remoteMessage.getData().toString());

        } else {
            Logs.Companion.d(TAG, "Message data payload: is less");

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Logs.Companion.d(TAG, "Message Notification Body " , remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow(String result) {
        try {

            JSONObject json = new JSONObject(result);
            // JSONObject data = json.getJSONObject("data");
            String title = json.getString("title");
            String message = json.getString("message");


            //int code = data.getInt("code");// MPESA 1, NOTIFICATION
            //int status = data.getInt("status");
            syncChanges();

            //  sendNotification(title, message, true, 0);
        } catch (Exception e) {
        Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void sendNotification(String title, String message, boolean isloggedIn) {

        if (isloggedIn) {
            Intent intent = new Intent(Application.context, TraderActivity.class);
            intent.putExtra("type", "notification_cart");


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
                    .notify(new Random().nextInt(), notification);

        }
    }


}
