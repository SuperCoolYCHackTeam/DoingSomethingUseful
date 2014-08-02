package com.ychack.doingsomethinguseful;

        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.support.wearable.view.WearableListView;
        import android.util.AttributeSet;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import static com.ychack.doingsomethinguseful.R.layout.*;

/**
 * Created by ristovuorio on 8/2/14.
 */

public class MainListAdapter extends WearableListView.Adapter {
    String[] mListItems = {"a","b","c"};

    public static class ViewHolder extends WearableListView.ViewHolder {
        public TextView mTextView;
        public ViewHolder (TextView v) {
            super((View) v);
            mTextView = v;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        TextView v = new TextView(viewGroup.getContext());
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder viewHolder, int i) {
        ViewHolder viewHolder1 = (ViewHolder) viewHolder;
        viewHolder1.mTextView.setText(mListItems[i]);
    }

    @Override
    public int getItemCount() {
        return mListItems.length;
    }
}
