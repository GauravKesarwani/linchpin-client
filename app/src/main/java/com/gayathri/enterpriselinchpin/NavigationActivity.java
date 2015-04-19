package com.gayathri.enterpriselinchpin;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import com.gayathri.enterpriselinchpin.gaurav.Profile;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


public class NavigationActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static String profileUrl;
    private static String compensationUrl;
    private static String teamInfoUrl;
    private static String experienceUrl;
    private static String performanceUrl;
    private static String directoryUrl;
    public final static String EXTRA_MESSAGE = "com.example.MESSAGE";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private Fragment currentFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        String appUrl = getString(R.string.ipaddress);
        //String appUrl1 = getString(R.string.ipaddress1);
        compensationUrl = appUrl +  "compensation/?emp_id=1";
        teamInfoUrl = appUrl + "teamInfo/?emp_id=1";
        experienceUrl =  appUrl + "experience/?emp_id=1";
        performanceUrl = appUrl + "performance/?emp_id=1";
        directoryUrl = appUrl + "directory/?emp_id=1";
        profileUrl = appUrl + "employee/?emp_id=1";
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        if (position == 0) {
            currentFragment = SocialFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, currentFragment)
                    .commit();
        } else if (position == 1) {
            new CallProfileAPI().execute(profileUrl);
        } else if (position == 2) {
            new Profile.CallTeamInfoAPI(this).execute(teamInfoUrl);
        } else if (position == 3) {
            new Profile.CallExperienceAPI(this).execute(experienceUrl);
        } else if (position == 4) {
            new Profile.CallCompensationAPI(this).execute(compensationUrl);
        } else if (position == 5) {
            new Profile.CallPerformanceAPI(this).execute(performanceUrl);
        } else if (position == 6) {
            new Profile.CallDirectoryAPI(this).execute(directoryUrl);
        } else {
            currentFragment = PlaceholderFragment.newInstance(position + 1);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, currentFragment)
                    .commit();
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.social, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sync_contacts:
                if (currentFragment != null && currentFragment instanceof SocialFragment) {
                    ((SocialFragment) currentFragment).syncContacts();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_social, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((NavigationActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    private class CallProfileAPI extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params){
            String fullApi_url = params[0];
            HttpClient httpClientObject = new DefaultHttpClient();
            HttpGet httpGetCall = new HttpGet(fullApi_url);
            String jsonResult = null;
            try {
                HttpEntity httpEntityObject = httpClientObject.execute(httpGetCall).getEntity();
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
            Intent intent = new Intent(getApplicationContext(), Profile.class);
            intent.putExtra(EXTRA_MESSAGE, result);
            startActivity(intent);
        }
    }

}
