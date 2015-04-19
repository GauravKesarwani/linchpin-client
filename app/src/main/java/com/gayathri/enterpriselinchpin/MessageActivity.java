package com.gayathri.enterpriselinchpin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class MessageActivity extends Activity implements View.OnClickListener {

    private String email;
    private String picture;
    private MessageAdapter adapter;
    private ListView listView;
    private BroadcastReceiver notificationReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_message);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.contactsview_row);
        email = getIntent().getStringExtra("email");
        /*
        picture = getIntent().getStringExtra("picture");

        // set the title text and picture.
        ((TextView) findViewById(R.id.tv_email)).setText(email);
        if (picture != null) {
            // guaranteed to be in the cache in most cases.
            new FetchPictureTask((ImageView) findViewById(R.id.tv_picture), this).execute(picture);
        }
        */
        listView = (ListView) findViewById(R.id.messages);
        listView.setDividerHeight(0);
        listView.setDivider(null);
        clearNotifications();
        ((Button) findViewById(R.id.send)).setOnClickListener(this);
        populateMessages();
    }

    @Override
    public void onStart() {
        super.onStart();
        // BroadcastReceiver to receive messages when the view is active.
        notificationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String sender = intent.getStringExtra("sender");
                String message = intent.getStringExtra("message");
                if (!sender.equalsIgnoreCase(email))
                    return;
                clearNotifications();
                adapter.add(new Message(sender, message, true));
                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount() - 1);
            }
        };
        registerReceiver(notificationReceiver, new IntentFilter(MessageReceiverService.NOTIFIER_INTENT));
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(notificationReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateMessages() {
        Db.init(this);
        Cursor cursor = Db.getMessages(email);

        adapter = new MessageAdapter(this, R.layout.messageview_row);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String message = cursor.getString(0);
                boolean received = cursor.getInt(1) == 1;
                adapter.add(new Message(email, message, received));
            } while (cursor.moveToNext());
        }
        listView.setAdapter(adapter);
        listView.setSelection(adapter.getCount() - 1);
        Db.deactivate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                // make sure message is not empty.
                EditText messageBox = (EditText) findViewById(R.id.message);
                String message = messageBox.getText().toString().trim();
                if (message.isEmpty()) {
                    return;
                }
                sendMessage(message);
                messageBox.setText("");
                break;
        }
    }

    private void sendMessage(String message) {
        // kick off the task for sending.
        new SendMessageTask(this).execute(email, message);
        // store it locally.
        Db.putMessage(this, email, message, false);
        // update the list view.
        adapter.add(new Message(email, message, false));
        adapter.notifyDataSetChanged();
        listView.setSelection(adapter.getCount() - 1);
    }

    private void clearNotifications() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancelAll();
    }

}
