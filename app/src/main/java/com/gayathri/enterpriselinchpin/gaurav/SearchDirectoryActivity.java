package com.gayathri.enterpriselinchpin.gaurav;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import com.gayathri.enterpriselinchpin.R;


public class SearchDirectoryActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
        setContentView(R.layout.activity_search_directory);
        System.out.println("Inside Oncreate");

    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //Intent intent = getIntent();
            System.out.println("Query " + query);

            //use the query to search your data somehow
        }
    }
}