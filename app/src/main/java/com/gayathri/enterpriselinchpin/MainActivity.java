package com.gayathri.enterpriselinchpin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gayathri.enterpriselinchpin.gaurav.Profile;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private final String GCM_PROJECT_NUMBER = "834822784318";
    private final int PHOTO_PICK_REQUEST = 1;

    private Bitmap profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hide the sign up layout.
        ((LinearLayout) findViewById(R.id.layout_signup)).setVisibility(View.GONE);

        // register button onclick listeners.
        setClickListener(R.id.signup);
        setClickListener(R.id.login);
        setClickListener(R.id.showsignup);
        setClickListener(R.id.showlogin);
        setClickListener(R.id.picture);

        // set default profile picture bitmap
        profilePicture = BitmapFactory.decodeResource(getResources(), R.drawable.default_profile_picture);

        // if already logged in, then proceed.
        if (C.getStringSetting(C.USERNAME, this) != null) {
            postLoginActivity();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void setClickListener(int resId) {
        findViewById(resId).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String username, password, password2;
        switch(v.getId()) {
            case R.id.showsignup:
                ((LinearLayout) findViewById(R.id.layout_login)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.layout_signup)).setVisibility(View.VISIBLE);
                break;
            case R.id.showlogin:
                ((LinearLayout) findViewById(R.id.layout_signup)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.layout_login)).setVisibility(View.VISIBLE);
                break;
            case R.id.login:
                username = ((EditText) findViewById(R.id.username)).getText().toString().trim();
                password = ((EditText) findViewById(R.id.password)).getText().toString().trim();
                if (username.isEmpty() || password.isEmpty()) {
                    showToast("Please fill in the username and password!");
                    return;
                }
                new LoginTask().execute(username, password);
                break;
            case R.id.signup:
                username = ((EditText) findViewById(R.id.username)).getText().toString().trim();
                password = ((EditText) findViewById(R.id.password)).getText().toString().trim();
                password2 = ((EditText) findViewById(R.id.password2)).getText().toString().trim();
                if (username.isEmpty() || password.isEmpty() || password2.isEmpty()) {
                    showToast("Please fill in the username and password!");
                    return;
                }
                if (!password.equals(password2)) {
                    showToast("Passwords do not match!");
                    return;
                }
                signUp(username, password);
                break;
            case R.id.picture:
                Intent photoPickIntent = new Intent(Intent.ACTION_GET_CONTENT,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                photoPickIntent.setType("image/*");
                photoPickIntent.putExtra("crop", "true");
                photoPickIntent.putExtra("crop", true);
                photoPickIntent.putExtra("scale", true);
                photoPickIntent.putExtra("outputX", 500);
                photoPickIntent.putExtra("outputY", 500);
                photoPickIntent.putExtra("aspectX", 1);
                photoPickIntent.putExtra("aspectY", 1);
                photoPickIntent.putExtra("return-data", true);
                startActivityForResult(photoPickIntent, PHOTO_PICK_REQUEST);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == PHOTO_PICK_REQUEST) {
            Bundle extras = data.getExtras();
            Uri uri = data.getData();
            if (extras != null) {
                profilePicture = extras.getParcelable("data");
            } else if (uri != null) {
                try {
                    profilePicture = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ((ImageView) findViewById(R.id.picture)).setImageBitmap(profilePicture);
        }
    }

    private void signUp(final String username, final String password) {
        new AsyncTask<Void, Void, Integer>() {

            private ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Setting up your account, please wait!");
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.setTitle("");
                progressDialog.show();
            }

            @Override
            protected Integer doInBackground(Void... params) {
                String registerUrl = C.DOMAIN + "/register?username=" + username + "&password=" + password;
                try {
                    JSONObject jsonResponse = C.httpPost(registerUrl, "image/png", C.getPng(profilePicture));
                    //JSONObject jsonResponse = C.httpGet(registerUrl);
                    if (jsonResponse == null) {
                        return -1;
                    }
                    if (!jsonResponse.getString("status_code").equalsIgnoreCase("200")) {
                        return -2;
                    }
                } catch (JSONException e) {
                    return -3;
                }
                return 0;
            }

            @Override
            protected void onPostExecute(Integer returnCode) {
                progressDialog.dismiss();
                // returnCode possible values:
                // 0 - Success
                // -1 - Server communication error
                // -2 - Server returned something other than 200
                // -3 - Server returned invalid response
                if (returnCode == 0) {
                    new LoginTask().execute(username, password);
                } else {
                    showToast("Sign up failed. Please try again. Error code: " + returnCode + ".");
                }
            }
        }.execute((Void[]) null);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void postLoginActivity() {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivityForResult(intent, 0);
        setResult(RESULT_OK);
        finish();
    }

    private class LoginTask extends AsyncTask<String, Void, Integer> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Logging you in, please wait!");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setTitle("");
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String username = params[0];
                String password = params[1];
                String loginUrl = C.DOMAIN + "/login?username=" + username + "&password=" + password;
                JSONObject jsonResponse = C.httpGet(loginUrl);
                if (jsonResponse == null) {
                    return -2;
                }
                if (jsonResponse.getString("status_code").equalsIgnoreCase("-1")) {
                    return -1;
                }

                // login was successful - register for GCM.
                GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                String registrationId = gcm.register(GCM_PROJECT_NUMBER);

                // transmit the registrationId to our server.
                String gcmRegistrationUrl = C.DOMAIN + "/gcm?action=register";
                JSONObject postData = new JSONObject();
                postData.put("username", username);
                postData.put("gcmId", registrationId);
                jsonResponse = C.httpPost(gcmRegistrationUrl, postData);
                if (jsonResponse == null) {
                    return -2;
                }
                if (!jsonResponse.getString("status_code").equalsIgnoreCase("200")) {
                    return -2;
                }

                // store the username and registrationId.
                C.putSetting(C.USERNAME, username, getApplicationContext());
                C.putSetting(C.GCM_REGISTRATION_ID, registrationId, getApplicationContext());

                // set CONTACTS_SYNCED to false so that contacts will be synced when logged in for
                // the first time.
                C.putSetting(C.CONTACTS_SYNCED, false, getApplicationContext());
            } catch (IOException e) {
                return -2;
            } catch (JSONException e) {
                return -2;
            }
            // everything was fine. proceed with login.
            return 0;
        }

        @Override
        protected void onPostExecute(Integer returnCode) {
            progressDialog.dismiss();
            // returnCode possible values:
            // 0 - Success
            // -1 - Username/Password does not match
            // -2 - Network IO Error.
            if (returnCode == 0) {
                postLoginActivity();
            } else {
                String message;
                if (returnCode == -1) {
                    message = "Login failed. Username and password does not match. Please try again.";
                } else {
                    message = "Login failed. Unknown error. Please try again. Error code: " + returnCode;
                }
                showToast(message);
            }
        }
    }

}