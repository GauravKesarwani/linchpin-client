package com.gayathri.enterpriselinchpin;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;

public class SendMessageService extends IntentService {

    public SendMessageService() {
        super("SendMessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String wearInput = getWearInput(intent);
        if (wearInput != null) {
            String email = intent.getStringExtra("email");
            // kick off the task for sending.
            new SendMessageTask(this).execute(email, wearInput);
            // store it locally.
            Db.putMessage(this, email, wearInput, false);
            // remove notifications in case it exists.
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.cancel(1);
        }
    }

    private String getWearInput(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            String input = remoteInput.getCharSequence(MessageReceiverService.WEAR_VOICE_REPLY).toString();
            return input.isEmpty() ? null : input;
        }
        return null;
    }

}
