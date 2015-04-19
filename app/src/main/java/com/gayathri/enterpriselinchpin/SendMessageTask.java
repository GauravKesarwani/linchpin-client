package com.gayathri.enterpriselinchpin;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

public class SendMessageTask extends AsyncTask<String, Void, Void> {

    private Context context;

    public SendMessageTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            String messageUrl = C.DOMAIN + "/message";
            JSONObject message = new JSONObject();
            message.put("sender", C.getStringSetting(C.USERNAME, context));
            message.put("receiver", params[0]);
            message.put("message", params[1]);
            C.httpPost(messageUrl, message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}