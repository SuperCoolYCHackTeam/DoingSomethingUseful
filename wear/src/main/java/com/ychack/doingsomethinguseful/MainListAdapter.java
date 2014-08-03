package com.ychack.doingsomethinguseful;

        import android.content.Context;
        import android.content.Intent;
        import android.support.v7.widget.RecyclerView;
        import android.support.wearable.view.WearableListView;
        import android.util.AttributeSet;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.FrameLayout;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import com.ychack.doingsomethinguseful.CameraActivity;
        import com.ychack.doingsomethinguseful.MainActivity;

        import static com.ychack.doingsomethinguseful.R.layout.*;

/**
 * Created by ristovuorio on 8/2/14.
 */

public class MainListAdapter extends WearableListView.Adapter {
    String[] mListHeadlines = {"Main","Camera","Social"};
    Class[] mListClasses = {MainActivity.class, CameraActivity.class, MainActivity.class};

    public static class ViewHolder extends WearableListView.ViewHolder {
        public View mElement;
        public ViewHolder (View v) {
            super((View) v);
            mElement = v;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LinearLayout wrapper = new LinearLayout(viewGroup.getContext());
        wrapper.setPadding(30, 0, 0, 0);
        wrapper.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView v = new TextView(viewGroup.getContext());
        v.setTextSize(42);

        wrapper.addView(v);
        ViewHolder vh = new ViewHolder(wrapper);

        return vh;
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder viewHolder, int i) {
        final ViewHolder viewHolder1 = (ViewHolder) viewHolder;
        TextView tv = (TextView)((ViewGroup)viewHolder1.mElement).getChildAt(0);
        tv.setText(mListHeadlines[i]);
        final int index = i;
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewHolder1.mElement.getContext(), mListClasses[index]);
                viewHolder1.mElement.getContext().startActivity(intent);
//                System.out.println("CLICKETY CLACK " + index);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListHeadlines.length;
    }
}
