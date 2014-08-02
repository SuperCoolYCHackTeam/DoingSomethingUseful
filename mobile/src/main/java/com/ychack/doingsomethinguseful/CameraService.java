package com.ychack.doingsomethinguseful;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class CameraService extends WearableListenerService {

    public static final String START_CAMERA_ACTIVITY = "/start-camera";

    public CameraService() {
        super();
    }

    public void onMessageReceived (MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(START_CAMERA_ACTIVITY)) {
            
        }
    }


    /*
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                Boolean take =
                        DataMap.fromByteArray(event.getDataItem().getData()).get(FIELD_ALARM_ON);
            }
        }
        dataEvents.close();
    }
    */
}

