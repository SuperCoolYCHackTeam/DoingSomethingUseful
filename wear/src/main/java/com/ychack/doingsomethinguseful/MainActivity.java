package com.ychack.doingsomethinguseful;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.WatchViewStub;
import android.view.ViewGroup;
import com.ychack.doingsomethinguseful.MainListAdapter;

import com.ychack.doingsomethinguseful.MainListView;

public class MainActivity extends Activity {

    private MainListView mMainListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mMainListView = (MainListView) stub.findViewById(R.id.main_list_view);
                mMainListView.setAdapter(new MainListAdapter());
            }
        });
    }
}
