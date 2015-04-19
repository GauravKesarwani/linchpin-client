package com.gayathri.enterpriselinchpin.gaurav;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gayathri.enterpriselinchpin.R;


/**
 * Created by gauravkesarwani on 2/26/15.
 */
public class CustomAdapter extends ArrayAdapter<NavigationItem> {
    Context mContext;
    int layoutResourceId;
    NavigationItem data[] = null;


    public CustomAdapter(Context mContext, int layoutResourceId, NavigationItem[] data) {
        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        //ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.imageViewIcon);
        TextView textViewName = (TextView) listItem.findViewById(R.id.textViewName);

        NavigationItem folder = data[position];

        //imageViewIcon.setImageResource(folder.icon);
        textViewName.setText(folder.name);

        return listItem;
    }
}

