package com.gayathri.enterpriselinchpin.gaurav.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gayathri.enterpriselinchpin.R;
import com.gayathri.enterpriselinchpin.gaurav.SearchDirectoryActivity;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;


public class ShowManager extends Fragment {
    public static final String Message = "Manager_Message";
    private String fbImageUrl = "https://s3-us-west-1.amazonaws.com/cmpe295b/Profile/";
    ImageView v;
    JSONObject obj;
    JSONArray arr;

    private OnFragmentInteractionListener mListener;

    public ShowManager() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        try {
            obj = new JSONObject(args.getString(Message));
            arr = obj.getJSONArray("emp_info");
            System.out.println("Inside Show Manager " + arr );
            String fbusername  = arr.getJSONObject(0).getString("fbusername");
            new CallAPI().execute(fbImageUrl + fbusername + ".jpg");

        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        System.out.println("Show Managers onCreateView method called");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_manager, container, false);
        try {
            TextView empID = (TextView) view.findViewById(R.id.empID);
            TextView name = (TextView) view.findViewById(R.id.name);
            TextView empDesg = (TextView) view.findViewById(R.id.empDesg);
            TextView empBand = (TextView) view.findViewById(R.id.empBand);
            empID.setText("ID: " + arr.getJSONObject(0).getString(("emp_id")));
            empDesg.setText(arr.getJSONObject(0).getString("emp_designation"));
            name.setText(arr.getJSONObject(0).getString("fname") + " " + arr.getJSONObject(0).getString("lname"));
            empBand.setText("Band: " + arr.getJSONObject(0).getString("grade"));

        }catch (JSONException e){
            e.printStackTrace();
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
     /*   if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        } */
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
     /*   try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    private class CallAPI extends AsyncTask<String, String, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params){

            String fullApi_url = params[0];


         Bitmap img_icon = null;
            try {
                URL url = new URL(fullApi_url);
             //   System.out.println(url);
                 img_icon = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return img_icon;
        }

        protected void onPostExecute(Bitmap result) {
            v = (ImageView) getActivity().findViewById(R.id.managerImg);
            v.setImageBitmap(result);
        }
    }

    public void advancedSearch(){
        Intent intent = new Intent(getActivity(), SearchDirectoryActivity.class);
        startActivity(intent);
    }

}
