package com.gayathri.enterpriselinchpin.gaurav;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.gayathri.enterpriselinchpin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CompensationActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compensation);

        Intent intent = getIntent();
        String message = intent.getStringExtra(Profile.COMP_MESSAGE);
        System.out.println("Message in Compensation Activity " + message);
        try {
            JSONObject obj = new JSONObject(message);
            JSONArray arr = obj.getJSONArray("compensation");
            JSONObject compensationInfo = arr.getJSONObject(0);

            TextView empBand = (TextView) findViewById(R.id.textView12);
            empBand.setText(compensationInfo.getString("band"));

            TextView compFreq = (TextView) findViewById(R.id.textView22);
            compFreq.setText(compensationInfo.getString("frequency"));



            TextView empLocation = (TextView) findViewById(R.id.textView42);
            empLocation.setText(compensationInfo.getString("location"));

            TextView empBasePay = (TextView) findViewById(R.id.textView52);
            empBasePay.setText(compensationInfo.getString("totalBasePay"));

            TextView totalCtc = (TextView) findViewById(R.id.textView62);
            totalCtc.setText(compensationInfo.getString("totalCtc"));

            TextView compCurrency = (TextView) findViewById(R.id.textView32);
            compCurrency.setText(compensationInfo.getString("currency"));
        }catch(JSONException e) {
            e.printStackTrace();

        }

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compensation, menu);
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
