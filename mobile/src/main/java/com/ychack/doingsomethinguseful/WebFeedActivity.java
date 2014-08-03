package com.ychack.doingsomethinguseful;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by ristovuorio on 8/2/14.
 */

public class WebFeedActivity extends Activity implements  DataApi.DataListener,
        MessageApi.MessageListener, NodeApi.NodeListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "WebFeedActivity";

    private static final String START_ACTIVITY_PATH = "/start-camera-activity";

    private static final String NEW_IMAGE = "/new-camera-image";

    Activity act;
    Context ctx;
    Bitmap mBitmap;

    private Handler handler = null;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        act = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        handler = new Handler();
        setContentView(R.layout.activity_webfeed);
        final ImageView imageView = (ImageView)findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                new LoadImageTask().execute("http://ychacks-image-service.herokuapp.com/image");
                new StartWearableActivityTask().execute();

                if (mBitmap == null) {
                    return;
                }
                imageView.setImageBitmap(mBitmap);
                new SendImageTask().execute(resizeBitmap(mBitmap));
            }
        });


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mResolvingError) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (!mResolvingError) {
            Wearable.DataApi.removeListener(mGoogleApiClient, this);
            Wearable.MessageApi.removeListener(mGoogleApiClient, this);
            Wearable.NodeApi.removeListener(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }


    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(mStatusChecker, 50);
        }
    };


    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

    }

    byte[] resizeImage(byte[] input) {
        Bitmap original = BitmapFactory.decodeByteArray(input , 0, input.length);
        Bitmap resized = Bitmap.createScaledBitmap(original, 50, 50, true);

        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.JPEG, 100, blob);

        return blob.toByteArray();
    }

    byte[] resizeBitmap(Bitmap input) {
        Bitmap resized = Bitmap.createScaledBitmap(input, 50, 50, true);

        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.JPEG, 100, blob);

        return blob.toByteArray();
    }

    @Override
    public void onPeerConnected(Node node) {
        Log.d(TAG,"node connected " + node.getDisplayName());
    }

    @Override
    public void onPeerDisconnected(Node node) {
        Log.d(TAG,"node disconnected " + node.getDisplayName());
    }

    private void sendStartActivityMessage(String node) {
        Wearable.MessageApi.sendMessage(
                mGoogleApiClient, node, START_ACTIVITY_PATH, new byte[0]).setResultCallback(
                new ResultCallback<MessageApi.SendMessageResult>() {
                    @Override
                    public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                        if (!sendMessageResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Failed to send message with status code: "
                                    + sendMessageResult.getStatus().getStatusCode());
                        }
                    }
                }
        );
    }

    private class StartWearableActivityTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {
            Collection<String> nodes = getNodes();

            for (String node : nodes) {
                sendStartActivityMessage(node);
            }
            return null;
        }
    }

    private void sendImage(String node, byte[] image) {
        Wearable.MessageApi.sendMessage(
                mGoogleApiClient, node, NEW_IMAGE, image).setResultCallback(
                new ResultCallback<MessageApi.SendMessageResult>() {
                    @Override
                    public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                        if (!sendMessageResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Failed to send message with status code: "
                                    + sendMessageResult.getStatus().getStatusCode());
                        }
                    }
                }
        );
    }

    private class SendImageTask extends AsyncTask<byte[], Void, Void> {

        @Override
        protected Void doInBackground(byte[]... args) {
            Collection<String> nodes = getNodes();

            for (String node : nodes) {
                sendImage(node, args[0]);
            }
            return null;
        }
    }

    private Collection<String> getNodes() {
        HashSet<String> results = new HashSet<String>();
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();

        for (Node node : nodes.getNodes()) {
            results.add(node.getId());
        }

        return results;
    }


    @Override //ConnectionCallbacks
    public void onConnected(Bundle connectionHint) {
        String print = "";
        if (connectionHint != null) {
            print = connectionHint.toString();
        }
        Log.d(TAG,"onConnected " + print);
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
        Wearable.NodeApi.addListener(mGoogleApiClient, this);
        handler.postDelayed(mStatusChecker, 1000);
    }

    @Override //ConnectionCallbacks
    public void onConnectionSuspended(int cause) {
        Log.d(TAG,"onConnectionSuspended " + cause);
    }

    /** Request code for launching the Intent to resolve Google Play services errors. */
    private static final int REQUEST_RESOLVE_ERROR = 1000;
    private Boolean mResolvingError = false;

    @Override //OnConnectionFailedListener
    public void onConnectionFailed(ConnectionResult result) {
        Log.d(TAG,"onConnectionFailed " + result.toString());
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            Log.e(TAG, "Connection to Google API client has failed");
            mResolvingError = false;
            Wearable.DataApi.removeListener(mGoogleApiClient, this);
            Wearable.MessageApi.removeListener(mGoogleApiClient, this);
            Wearable.NodeApi.removeListener(mGoogleApiClient, this);
        }
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
