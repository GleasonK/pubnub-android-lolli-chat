package me.kevingleason.pubnubchat.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import me.kevingleason.pubnubchat.Constants;
import me.kevingleason.pubnubchat.MainActivity;
import me.kevingleason.pubnubchat.R;

/**
 * Created by GleasonK on 7/14/15.
 */
public class GcmIntentService  extends IntentService {

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        if (!extras.isEmpty() && GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            sendNotification(intent.getExtras());
        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(Bundle extras) {
        if (extras==null) return;
        Log.d("GCM-notif",extras.toString());
        String chatRoom = extras.getString(Constants.GCM_CHAT_ROOM);
        String notifBigTex  = "Poke from " + extras.getString(Constants.GCM_POKE_FROM);
        String notifContent = "In chat room " + chatRoom;

        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.CHAT_ROOM, chatRoom);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_pn_chat);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setLargeIcon(icon)
                        .setSmallIcon(android.R.drawable.star_on)
                        .setContentTitle(notifBigTex)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(notifBigTex))
                        .setContentText(notifContent)
                        .setAutoCancel(true);

        mBuilder.setContentIntent(contentIntent);
        Notification pnNotif = mBuilder.build();
        mNotificationManager.notify(0, pnNotif);  // Set notification ID
    }
}
