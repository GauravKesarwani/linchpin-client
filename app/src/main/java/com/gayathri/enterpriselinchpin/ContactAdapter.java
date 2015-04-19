package com.gayathri.enterpriselinchpin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<Contact> {

    private ArrayList<Contact> contacts;

    public ContactAdapter(Context context, int resource) {
        super(context, resource);
        contacts = new ArrayList<Contact>();
    }

    public int getCount() {
        return contacts.size();
    }

    public Contact getItem(int index) {
        return contacts.get(index);
    }

    public void add(Contact contact) {
        contacts.add(contact);
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.contactsview_row, parent, false);
        }
        Contact contact =  contacts.get(position);
        new FetchPictureTask((ImageView) row.findViewById(R.id.tv_picture), getContext()).execute(contact.picture);
        TextView tv = (TextView) row.findViewById(R.id.tv_email);
        tv.setText(contact.email);
        return row;
    }

}