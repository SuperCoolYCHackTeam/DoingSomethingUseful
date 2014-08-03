package com.ychack.doingsomethinguseful;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.Collection;
import java.util.HashSet;

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
                mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("Fire TakePictureTask");
                        new TakePictureTask().execute();
                    }
                });
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
                    BitmapFactory.Options op = new BitmapFactory.Options();
                    op.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                    mImageView.setImageBitmap(bm);
                }
            }
        });
    }

    public void finishThisThing() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                CameraActivity.this.finish();
            }
        });
    }

    private class TakePictureTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {
            for (String node : getNodes()) {
                System.out.println("send");
                Wearable.MessageApi.sendMessage(MainService.mGoogleApiClient, node, "TAKE_PICTURE", null);
            }
            return null;
        }
    }

    private Collection<String> getNodes() {
        HashSet<String> results= new HashSet<String>();
        if (MainService.mGoogleApiClient != null) {
            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(MainService.mGoogleApiClient).await();
            for (Node node : nodes.getNodes()) {
                results.add(node.getId());
            }
        }
        return results;
    }
}
