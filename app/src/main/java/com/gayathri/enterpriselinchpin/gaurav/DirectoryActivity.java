package com.gayathri.enterpriselinchpin.gaurav;

        import com.gayathri.enterpriselinchpin.R;
        import com.gayathri.enterpriselinchpin.gaurav.fragments.HorizontalScroll;
        import com.gayathri.enterpriselinchpin.gaurav.fragments.ShowManager;
        import android.app.FragmentManager;
        import android.app.FragmentTransaction;
        import android.app.SearchManager;
        import android.content.ComponentName;
        import android.content.Context;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.v7.app.ActionBarActivity;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.View;
        import android.widget.SearchView;
        import org.apache.http.HttpEntity;
        import org.apache.http.client.ClientProtocolException;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.json.JSONArray;
        import org.json.JSONObject;
        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.Reader;


public class DirectoryActivity extends ActionBarActivity implements HorizontalScroll.OnImageSelectedListener{

    String message;
    ShowManager showManagerFrag;
    private static String urlString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);
        setTitle("Directory");

        getSupportActionBar().setIcon(R.drawable.linchpin);
        if( savedInstanceState != null)
            return;
        FragmentManager fm = getFragmentManager();
        HorizontalScroll horizontalScroll = new HorizontalScroll();
        System.out.println("Directory Intent Extras" + getIntent().getExtras());
        Bundle args = new Bundle();
        message = getIntent().getStringExtra(Profile.DIRECTORY_MESSAGE);
       // if (message == null)
       //     message = getIntent().getStringExtra(SearchDirectoryActivity.DIRECTORY_MESSAGE);
        Log.d("Directory Message ", message);
        args.putString(HorizontalScroll.MESSAGE, message);
        horizontalScroll.setArguments(args);

        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.horscroll, horizontalScroll);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_directory, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(new ComponentName(getApplicationContext(), SearchDirectoryActivity.class)));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onImageSelectedListener(int position){
        System.out.println("Inside Directory Activity OnImageSelected Listener Position " + position);
        System.out.println("Message in Directory Image SElected listener " + message);
        try {
            //convert String into JSON array
            JSONObject obj = new JSONObject(message);
            JSONArray arr = obj.getJSONArray("directory");
            JSONObject selObj = arr.getJSONObject(position);
            System.out.println(selObj.getString("manager"));
            String linchpinip =  getString(R.string.ipaddress);
            urlString = linchpinip + "employee/?emp_id=" + selObj.getString("emp_id");
            new CallAPI().execute(urlString);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private class CallAPI extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params){

            String fullApi_url = params[0];
            HttpClient httpClientObject = new DefaultHttpClient();
            System.out.println(fullApi_url);
            HttpGet httpGetCall = new HttpGet(fullApi_url);

            String jsonResult = null;
            try {

                HttpEntity httpEntityObject = httpClientObject.execute(httpGetCall).getEntity();
                System.out.println(httpEntityObject);
                if (httpEntityObject != null) {

                    InputStream inputStreamObject = httpEntityObject.getContent();
                    Reader readerObject = new InputStreamReader(inputStreamObject);
                    BufferedReader bufferedReaderObject = new BufferedReader(readerObject);

                    StringBuilder jsonResultStringBuilder = new StringBuilder();
                    String readLine = null;

                    while ((readLine = bufferedReaderObject.readLine()) != null) {
                        jsonResultStringBuilder.append(readLine + "\n");
                    }

                    jsonResult = jsonResultStringBuilder.toString();
                }

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResult;
        }

        protected void onPostExecute(String result) {
            FragmentManager fm = getFragmentManager();
            showManagerFrag = new ShowManager();
            Bundle args = new Bundle();
            System.out.println("Manager Info " + result);
            args.putString(ShowManager.Message, result);
            showManagerFrag.setArguments(args);
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.showManager,showManagerFrag);
            ft.commit();
        }
    }

    public void advancedSearch(View v){
        Intent intent = new Intent(getApplicationContext(), SearchDirectoryActivity.class);
        startActivity(intent);
    }
}





