package com.studios146.de.stundenplan24;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by FM on 03.05.2017.
 */

public class ScheduleAdapter extends BaseAdapter {
    private Context mContext;
    private Lesson[] mLesson;

    public ScheduleAdapter(Context c, Lesson[] lessons){
        mContext=c;
        mLesson=lessons;
    }
    @Override
    public int getCount() {
        return mLesson.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if(convertView == null){
            textView = new TextView(mContext);
            textView.setLayoutParams(new GridView.LayoutParams());
        }
        return null;
    }
}
