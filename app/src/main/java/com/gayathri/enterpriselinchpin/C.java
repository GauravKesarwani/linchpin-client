package com.gayathri.enterpriselinchpin;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class C {

    public static final String DOMAIN = "http://linode.foamsnet.com:8000/linchpin";

    public static final String GCM_REGISTRATION_ID = "GCM_REGISTRATION_ID";
    public static final String USERNAME = "USERNAME";
    public static final String CONTACTS_SYNCED = "CONTACTS_SYNCED";
    public static final String CONTACTS = "CONTACTS_JSON_ARRAY";

    public static byte[] rawHttpGet(String url, boolean useCache, Context context) {
        if (useCache) {
            byte[] image = getImageFromCache(url, context);
            if (image != null) {
                return image;
            }
        }
        try {
            HttpResponse response = new DefaultHttpClient().execute(new HttpGet(url));
            if (response.getStatusLine().getStatusCode() != 200) {
                return null;
            }
            byte[] image = EntityUtils.toByteArray(response.getEntity());
            putImageToCache(url, image, context);
            return image;
        } catch (IOException e) {
            return null;
        }
    }

    public static JSONObject httpGet(String url) {
        try {
            HttpResponse response = new DefaultHttpClient().execute(new HttpGet(url));
            if (response.getStatusLine().getStatusCode() != 200) {
                return null;
            }
            return new JSONObject(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            return null;
        } catch (JSONException e) {
            return null;
        }
    }

    public static JSONObject httpPost(String url, JSONObject data) {
        try {
            HttpPost request = new HttpPost(url);
            request.setHeader("Content-type", "application/json");
            request.setEntity(new StringEntity(data.toString()));
            HttpResponse response = new DefaultHttpClient().execute(request);
            if (response.getStatusLine().getStatusCode() != 200) {
                return null;
            }
            return new JSONObject(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            return null;
        } catch (JSONException e) {
            return null;
        }
    }

    public static JSONObject httpPost(String url, String contentType, byte[] data) {
        try {
            HttpPost request = new HttpPost(url);
            request.setHeader("Content-type", contentType);
            request.setEntity(new ByteArrayEntity(data));
            HttpResponse response = new DefaultHttpClient().execute(request);
            if (response.getStatusLine().getStatusCode() != 200) {
                return null;
            }
            return new JSONObject(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            return null;
        } catch (JSONException e) {
            return null;
        }
    }

    public static void putSetting(String key, String value, Context context) {
        SharedPreferences settings = context.getSharedPreferences("Linchpin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStringSetting(String key, Context context) {
        return context.getSharedPreferences("Linchpin", Context.MODE_PRIVATE).getString(key, null);
    }

    public static void putSetting(String key, boolean value, Context context) {
        SharedPreferences settings = context.getSharedPreferences("Linchpin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBooleanSetting(String key, boolean defaultValue, Context context) {
        return context.getSharedPreferences("Linchpin", Context.MODE_PRIVATE).getBoolean(key, defaultValue);
    }

    public static byte[] getImageFromCache(String url, Context context) {
        SharedPreferences settings = context.getSharedPreferences("pictures", Context.MODE_PRIVATE);
        String base64 = settings.getString(url, null);
        if (base64 == null) {
            return null;
        }
        return Base64.decode(base64, Base64.DEFAULT);
    }

    public static void putImageToCache(String url, byte[] image, Context context) {
        SharedPreferences settings = context.getSharedPreferences("pictures", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(url, Base64.encodeToString(image, Base64.DEFAULT));
        editor.commit();
    }

    public static byte[] getPng(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
        return outputStream.toByteArray();
    }

    public static Bitmap getProfilePicture(String email, Context context) {
        try {
            String url = null;
            JSONArray contacts = new JSONArray(getStringSetting(CONTACTS, context));
            for (int i = 0; i < contacts.length(); i++) {
                if (contacts.getJSONObject(i).getString("email").equalsIgnoreCase(email)) {
                    url = contacts.getJSONObject(i).getString("picture");
                }
            }
            if (url == null) {
                return null;
            }
            byte[] image = getImageFromCache(url, context);
            if (image == null) {
                return null;
            }
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        } catch (JSONException e) {
            return null;
        }
    }

}
