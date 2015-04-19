package com.gayathri.enterpriselinchpin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SocialFragment extends ListFragment {

    private ContactAdapter adapter;

    public static SocialFragment newInstance() {
        return new SocialFragment();
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SocialFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!C.getBooleanSetting(C.CONTACTS_SYNCED, false, getActivity())) {
            // contacts not yet synced. kick off a task to sync them.
            syncContacts();
        } else {
            populateListAdapter();
        }
    }

    public void syncContacts() {
        new SyncContacts().execute((Void[]) null);
    }

    private void populateListAdapter() {
        try {
            JSONArray contacts_list = new JSONArray(C.getStringSetting(C.CONTACTS, getActivity()));
            adapter = new ContactAdapter(getActivity(), R.layout.contactsview_row);
            for (int i = 0; i < contacts_list.length(); i++) {
                adapter.add(
                        new Contact(contacts_list.getJSONObject(i).getString("email"), contacts_list.getJSONObject(i).getString("picture")));
            }
            setListAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onListItemClick (ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), MessageActivity.class);
        intent.putExtra("email", adapter.getItem(position).email);
        intent.putExtra("picture", adapter.getItem(position).picture);
        startActivity(intent);
    }

    private class SyncContacts extends AsyncTask<Void, Void, Integer> {

        private ProgressDialog progressDialog;
        private Context context;

        @Override
        protected void onPreExecute() {
            context = getActivity();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Syncing contacts, Please wait!");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setTitle("");
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            String contactsUrl = C.DOMAIN + "/contacts?username=" + C.getStringSetting(C.USERNAME, context);
            JSONObject response = C.httpGet(contactsUrl);
            try {
                if (!response.getString("status_code").equalsIgnoreCase("200")) {
                    return -1;
                }
                // store the contacts list.
                JSONArray contacts = response.getJSONArray("contacts");
                C.putSetting(C.CONTACTS, contacts.toString(), context);
            } catch (JSONException e) {
                return -1;
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer returnCode) {
            progressDialog.dismiss();
            String message = "Sync Failed";
            if (returnCode == 0) {
                message = "Sync Successful";
                C.putSetting(C.CONTACTS_SYNCED, true, context);
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            populateListAdapter();
        }
    }

}
