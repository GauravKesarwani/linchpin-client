package com.gayathri.enterpriselinchpin.gaurav.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gayathri.enterpriselinchpin.R;
import com.gayathri.enterpriselinchpin.gaurav.beans.EmployeeRowItem;
import com.gayathri.enterpriselinchpin.gaurav.utils.ImageLoader;

/**
 * Created by gaurav on 3/29/15.
 */
public class TeamInfoAdapter extends ArrayAdapter<EmployeeRowItem> {
    ImageLoader imageLoader;
    Context context;
    EmployeeRowItem[] rowItems;

    public TeamInfoAdapter(Context context, EmployeeRowItem[] values) {
        super(context,R.layout.teamlist_item,values);
        this.context = context;
        this.rowItems = values;
        imageLoader = new ImageLoader(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = null;
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        listItem = inflater.inflate(R.layout.teamlist_item, parent, false);

        ImageView empimageView = (ImageView) listItem.findViewById(R.id.EmpImage);
        TextView empName = (TextView) listItem.findViewById(R.id.EmpName);
        TextView empDesg = (TextView) listItem.findViewById(R.id.EmpDesg);
        EmployeeRowItem rowItem = (EmployeeRowItem) getItem(position);

        empName.setText(rowItem.getEmpName());
        empDesg.setText(rowItem.getEmpDesg());
        imageLoader.DisplayImage(rowItem.getImageId(), empimageView);
        return listItem;
    }
}


