package com.nibm.insuranceapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {

    ImageView imageView;

    public DownLoadImageTask(ImageView imgCameraView) {
        this.imageView=imgCameraView;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String urlOfImage = strings[0];
        Bitmap image = null;
        try{
            InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
            image = BitmapFactory.decodeStream(is);
        }catch(Exception e){ // Catch the download exception
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
