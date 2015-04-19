package com.gayathri.enterpriselinchpin;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.app.RemoteInput;
import android.util.Log;

public class MessageReceiverService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    public static final String WEAR_VOICE_REPLY = "wear_voice_reply";
    public static final String NOTIFIER_INTENT = "com.gayathri.enterpriselinchpin.intent.action.NOTIFY";

    public MessageReceiverService() {
        super("MessageReceiverService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String message = intent.getStringExtra("message");
        String sender = intent.getStringExtra("sender");
        Db.putMessage(this, sender, message, true);
        sendNotification(sender, message);
        MessageReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String sender, String message) {
        // build the intent for notification tap.
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("email", sender);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // build the intent for wearable reply action.
        Intent replyIntent = new Intent(this, SendMessageService.class);
        replyIntent.putExtra("email", sender);
        PendingIntent replyPendingIntent =
                PendingIntent.getService(this, 0, replyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // create the reply action for wearable.
        RemoteInput remoteInput = new RemoteInput.Builder(WEAR_VOICE_REPLY)
                .setLabel("Reply")
                .build();
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon,
                        "Reply", replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(sender)
                        .setContentText(message)
                        .setContentIntent(contentIntent)
                        .extend(new WearableExtender().addAction(action));

        // get the sender's picture if it exists in the cache.
        Bitmap profilePicture = C.getProfilePicture(sender, this);
        if (profilePicture != null) {
            notificationBuilder.setLargeIcon(profilePicture);
        }


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

        // notify the activity if it is active.
        Intent activityNotifier = new Intent(NOTIFIER_INTENT);
        activityNotifier.putExtra("message", message);
        activityNotifier.putExtra("sender", sender);
        sendBroadcast(activityNotifier);
    }

}