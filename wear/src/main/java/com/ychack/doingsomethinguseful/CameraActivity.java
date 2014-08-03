package com.ychack.doingsomethinguseful;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.view.WatchViewStub;
import android.widget.ImageView;

public class CameraActivity extends Activity {

    public static CameraActivity instance = null;

    private ImageView mImageView = null;
    private Handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        instance = this;
        setContentView(R.layout.activity_camera);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mImageView = (ImageView) stub.findViewById(R.id.camera_image_view);
            }
        });

    }

    @Override
    protected void onDestroy() {
        instance = null;
        super.onDestroy();
    }

    public void updateImage(final byte[] data) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mImageView != null) {
                    System.out.println("Message received! "+data.length);
                    BitmapFactory.Options op = new BitmapFactory.Options();
                    op.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                    mImageView.setImageBitmap(bm);
                    System.gc();
                }
            }
        });

    }
}
