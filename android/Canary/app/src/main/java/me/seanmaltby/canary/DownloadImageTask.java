package me.seanmaltby.canary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
{
    ImageView mImage;

    public DownloadImageTask(ImageView image)
    {
        mImage = image;
    }

    @Override
    protected Bitmap doInBackground(String... params)
    {
        InputStream in;
        try
        {
            in = new java.net.URL(params[0]).openStream();
        } catch (IOException e)
        {
            e.printStackTrace();    // TODO handle error gracefully
            return null;
        }
        return BitmapFactory.decodeStream(in);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap)
    {
        if(bitmap != null)
            mImage.setImageBitmap(bitmap);
    }
}
