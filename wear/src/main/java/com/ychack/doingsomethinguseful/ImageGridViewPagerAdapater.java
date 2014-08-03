package com.ychack.doingsomethinguseful;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.ImageReference;
import android.view.Gravity;

class ImageGridViewPagerAdapater extends FragmentGridPagerAdapter {

    private final Context mContext;
    private ImageReference image;

    public ImageGridViewPagerAdapater(Context ctx, FragmentManager fm, Bitmap background) {
        super(fm);
        mContext = ctx;
        image = ImageReference.forBitmap(background);
    }

    @Override
    public Fragment getFragment(int row, int col) {
        String title = null;
        String text = null;
        switch (col){
            case 0:
                title = "Camera";
                text = "Image Preview";
                break;
            case 1:
                title = "Share";
                //text = "Share";
                break;
            case 2:
                title = "Retake";
                text = "You hate it.";
                break;
        }

        CardFragment fragment = CardFragment.create(title, text);

        // Advanced settings
        fragment.setCardGravity(Gravity.BOTTOM);
        fragment.setExpansionEnabled(false);
        return fragment;
    }

    @Override
    public ImageReference getBackground(int row, int column) {
        return image;
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int rowNum) {
        return 3;
    }
}