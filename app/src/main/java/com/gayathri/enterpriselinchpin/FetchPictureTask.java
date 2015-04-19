package com.gayathri.enterpriselinchpin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class FetchPictureTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView imageView;
    private Context context;

    public FetchPictureTask(ImageView imageView, Context context) {
        this.imageView = imageView;
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        // TODO: cache this.
        byte[] data = C.rawHttpGet(params[0], true, context);
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageView != null) {
            imageView.setImageBitmap(bitmap);
        }
    }
}