package com.gayathri.enterpriselinchpin.gaurav;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.gayathri.enterpriselinchpin.R;
import com.gayathri.enterpriselinchpin.gaurav.adapters.TeamInfoAdapter;
import com.gayathri.enterpriselinchpin.gaurav.beans.EmployeeRowItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TeamInfoActivity extends ActionBarActivity {
    ListView listView;
    EmployeeRowItem[] rowItems;
    private String fbImageUrl = "https://graph.facebook.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);
        Intent intent = getIntent();
        String message = intent.getStringExtra(Profile.TEAMINFO_MESSAGE);
        try {
            JSONObject obj = new JSONObject(message);
            JSONArray arr = obj.getJSONArray("teamInfo");
            int teamSize = Integer.parseInt(obj.getString("teamSize"));
            String[] teamMembers = new String[teamSize];
            TextView teamSizeL = (TextView) findViewById(R.id.label_teamSize);
            teamSizeL.setText("Team Size ");
            TextView teamSizeCount = (TextView) findViewById(R.id.teamSizeCount);
            teamSizeCount.setText(""+teamSize);
            rowItems = new EmployeeRowItem[teamSize];
            for (int i = 0; i < teamSize; i++) {
                JSONObject o = arr.getJSONObject(i);
                EmployeeRowItem item = new EmployeeRowItem(fbImageUrl + o.getString("fbusername") + "/picture?width=400",o.getString("fname"),o.getString("emp_designation"),null);
                rowItems[i] = item;
            }
            listView = (ListView) findViewById(R.id.list_team);
            TeamInfoAdapter adapter = new TeamInfoAdapter(this, rowItems);
            listView.setAdapter(adapter);

        }catch(JSONException e){
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_team_info, menu);
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
