package com.ychack.doingsomethinguseful;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.view.WindowInsets;
import android.widget.TextView;

public class ImageActivity extends Activity {

    private GridViewPager mGridViewPager;
    private ImageGridViewPagerAdapater imageGridViewPagerAdapater;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        final Resources res = getResources();
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        byte[] imageData = getIntent().getExtras().getByteArray("image");
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bm = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        imageGridViewPagerAdapater = new ImageGridViewPagerAdapater(this, getFragmentManager(), bm);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mGridViewPager = (GridViewPager) stub.findViewById(R.id.image_grid_view_pager);

                /*mGridViewPager.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                    @Override
                    public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                        // Adjust page margins:
                        //   A little extra horizontal spacing between pages looks a bit
                        //   less crowded on a round display.
                        final boolean round = insets.isRound();
                        int rowMargin = res.getDimensionPixelOffset(R.dimen.page_row_margin);
                        int colMargin = res.getDimensionPixelOffset(round ?
                                R.dimen.page_column_margin_round : R.dimen.page_column_margin);
                        pager.setPageMargins(rowMargin, colMargin);
                        return insets;
                    }
                });*/
                mGridViewPager.setAdapter(imageGridViewPagerAdapater);
            }
        });


    }


}
