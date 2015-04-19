package com.gayathri.enterpriselinchpin.gaurav;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.gayathri.enterpriselinchpin.gaurav.adapters.EmployerAdapter;
import com.gayathri.enterpriselinchpin.R;
import com.gayathri.enterpriselinchpin.gaurav.beans.EmployerRowItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ExperienceActivity extends ActionBarActivity {
    ListView listView;
    EmployerRowItem[] rowItems;
    int totalExpInMonths = 0;
    double totalExpInYears = 0;
    JSONArray empJoining;
    JSONObject obj,obj1;
    String joiningDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience);
        Intent intent = getIntent();
        String message = intent.getStringExtra(Profile.EXPERIENCE_MESSAGE);
        try {
            obj = new JSONObject(message);
            JSONArray arr = obj.getJSONArray("jobhist");
            empJoining = obj.getJSONArray("empJoining");
            obj1 = empJoining.getJSONObject(0);
            joiningDate = obj1.getString("joiningDate");
            rowItems = new EmployerRowItem[arr.length()];

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            Date d1 = null;
            Date d2 = null;
            Calendar cal = Calendar.getInstance();
            Calendar startCalendar = new GregorianCalendar();
            Calendar endCalendar = new GregorianCalendar();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject jobHist = arr.getJSONObject(0);
                d1 = dateFormat.parse(jobHist.getString("startDate"));
                d2 = dateFormat.parse(jobHist.getString("endDate"));
                startCalendar.setTime(d1);
                endCalendar.setTime(d2);

                int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
                int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
                totalExpInMonths+=diffMonth;

                EmployerRowItem erow = new EmployerRowItem(jobHist.getString("companyName"), jobHist.getString("designation"), jobHist.getString("startDate"),jobHist.getString("endDate"));
                rowItems[i] = erow;
            }

            if (totalExpInMonths > 12) {
                totalExpInYears+=totalExpInMonths/12;
            }
        }catch(JSONException e) {
            e.printStackTrace();
        }
        catch(ParseException e){
            e.printStackTrace();
        }

        listView = (ListView) findViewById(R.id.list_experience);
        EmployerAdapter adapter = new EmployerAdapter(this, rowItems);
        listView.setAdapter(adapter);

        TextView v = (TextView) findViewById(R.id.totalExp);
        if (totalExpInYears > 0)
            v.setText("Total Experience " + totalExpInYears + " years");
        else
            v.setText("Total Experience " + totalExpInMonths + " months");

        TextView joiningDateView = (TextView) findViewById(R.id.joiningDateLabel);
        joiningDateView.setText("Start Date    " + joiningDate);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_experience, menu);
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
}
