package com.ychack.doingsomethinguseful;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by ristovuorio on 8/2/14.
 */
public class WebFeedActivity extends Activity {
    ImageView mImageView;
    Bitmap mBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_webfeed);
        final ImageView imageView = ((ImageView) findViewById(R.id.imageView));
        mImageView = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new LoadImageTask().execute("http://img2.wikia.nocookie.net/__cb20140628223400/crossoverrp/images/thumb/a/a6/Dickbutt.jpg/480px-Dickbutt.jpg");
                mImageView.setImageBitmap(mBitmap);
            }
        });
    }
    private class LoadImageTask extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... args) {
            try {
                java.net.URL url = new java.net.URL(args[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                mBitmap = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            return null;
        }

        protected void onPostExecute(Long result) {
        }
    }

}
