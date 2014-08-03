package com.ychack.doingsomethinguseful;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

public class MainService extends WearableListenerService {
    private final String TAG = "MainService";

    private static final String START_CAMERA_ACTIVITY_PATH = "/start-camera-activity";
    private static final String NEW_IMAGE = "/new-camera-image";


    public MainService() {
    }

    private GoogleApiClient mGoogleApiClient = null;
    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG, "onMessageReceived: " + messageEvent);

        // Check to see if the message is to start an activity
        if (messageEvent.getPath().equals(START_CAMERA_ACTIVITY_PATH)) {
            Intent startIntent = new Intent(this, CameraActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startIntent);
        } else if (messageEvent.getPath().equals(NEW_IMAGE)) {
            if (CameraActivity.instance != null) {
                CameraActivity.instance.updateImage(messageEvent.getData());
            }
        }
    }

    @Override
    public void onPeerConnected(Node peer) {
        Log.d(TAG, "onPeerConnected: " + peer);
    }

    @Override
    public void onPeerDisconnected(Node peer) {
        Log.d(TAG, "onPeerDisconnected: " + peer);
    }
}
