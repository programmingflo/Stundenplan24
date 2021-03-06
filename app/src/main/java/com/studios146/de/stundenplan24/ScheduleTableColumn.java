package com.studios146.de.stundenplan24;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by timgrohmann on 08.05.17.
 *
 */

public class ScheduleTableColumn extends Fragment {
    /*public ScheduleTableColumn(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        inflate(context, R.layout.schedule_tablecolumn, null);
    }*/

    int day;
    Timetable t;

    void setTimetable (Timetable t){
        this.t = t;
        ViewGroup contentGroup = (ViewGroup) getView();
        if (contentGroup == null) return;
        updateView(contentGroup);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.schedule_tablecolumn, container, false);
        LinearLayout ll = (LinearLayout) v;
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT, 1.0f));
        ll.setOrientation(LinearLayout.VERTICAL);

        ViewGroup contentGroup = (ViewGroup) v;
        updateView(contentGroup);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup contentGroup = (ViewGroup) getView();
        //updateView(contentGroup);
    }

    void updateView(ViewGroup v){
        if (t == null) return;

        for (int j = 0; j < 8; j++) {
            LessonCellView view = t.getViewForPosition(day,j);
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT, 1.0f));
            v.addView(view);
        }
    }
}
