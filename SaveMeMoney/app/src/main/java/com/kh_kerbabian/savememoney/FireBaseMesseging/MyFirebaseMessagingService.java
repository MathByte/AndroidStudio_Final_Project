package com.kh_kerbabian.savememoney.FireBaseMesseging;

import static com.kh_kerbabian.savememoney.Notifications.Notifications.CHANNEL_1_ID;

import android.app.Notification;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kh_kerbabian.savememoney.IFirebaseMesseginig;
import com.kh_kerbabian.savememoney.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private IFirebaseMesseginig interfase;
    private SharedPreferences sharedPref;
    private NotificationManagerCompat notificationManager;
    private String TAG = "aaaaaaaaaaaaaaaaaa";
    String settingsNotification = "xmlNotification";



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob(remoteMessage);
            } else {
                // Handle message within 10 seconds
                handleNow(remoteMessage);
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            interfase.RunNotification();
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }



    private void handleNow(RemoteMessage er) {
        Log.d(TAG, "handle: " + er.getNotification().getBody());
    }

    private void scheduleJob(RemoteMessage er) {
        Log.d(TAG, "schedule: " + er.getNotification().getBody());
    }

}
