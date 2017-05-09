package com.studios146.de.stundenplan24;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Created by timgrohmann on 03.05.2017.
 *
 */

public class ScheduleTableLayout extends LinearLayout {
    private Context context;
    private Timetable timetable;

    public ScheduleTableLayout(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
    }

    public void setTimetable(Timetable t){
        this.timetable = t;
        construct();
    }

    public void construct() {
        //for (int i = 0; i < 1; i++) {
        //ScheduleTableColumn tableColoumn = new ScheduleTableColumn(context,null);
        for (int j = 0; j < 8; j++) {
            LessonCellView view = timetable.getViewForPosition(j);
            view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1.0f));
            view.invalidate();
            Log.d("vp", "Added view! " + view.toString());
            this.addView(view);

        }
        this.invalidate();
            //this.addView(tableColoumn);
        //}
    }

    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /*int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(parentWidth, parentHeight);
    }*/
}
