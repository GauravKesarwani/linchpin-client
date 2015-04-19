package com.gayathri.enterpriselinchpin;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<Message> {

    private ArrayList<Message> messages;

    public MessageAdapter(Context context, int resource) {
        super(context, resource);
        messages = new ArrayList<Message>();
    }

    public int getCount() {
        return messages.size();
    }

    public Message getItem(int index) {
        return messages.get(index);
    }

    public void add(Message message) {
        messages.add(message);
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.messageview_row, parent, false);
        }
        Message message = messages.get(position);
        TextView tv = (TextView) row.findViewById(R.id.tv_message);
        tv.setBackgroundResource(message.received ? R.drawable.bubble_yellow : R.drawable.bubble_green);
        tv.setText(message.message);
        ((LinearLayout) row.findViewById(R.id.wrapper)).setGravity(message.received ? Gravity.LEFT : Gravity.RIGHT);
        return row;
    }
}