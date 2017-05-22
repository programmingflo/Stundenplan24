package com.studios146.de.stundenplan24;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Created by timgrohmann on 03.05.2017.
 *
 */

public class ScheduleTableLayout extends LinearLayout {
    private Context context;
    private Timetable timetable;
    private Integer desWidth;

    public ScheduleTableLayout(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        inflate(context,R.layout.layout_schedule,null);
    }

    public void setTimetable(Timetable t){
        this.timetable = t;
        construct();
    }

    public void construct() {


        for (int i = 0; i < 5; i++) {
            ScheduleTableColumn tableColoumn = new ScheduleTableColumn(context,null);

            tableColoumn.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
            tableColoumn.setOrientation(LinearLayout.VERTICAL);

            for (int j = 0; j < 8; j++) {
                LessonCellView view = timetable.getViewForPosition(j);
                view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
                //view.invalidate();
                tableColoumn.addView(view);
            }


            this.addView(tableColoumn);

        }
        this.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /*int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(parentWidth, parentHeight);*/
        View top = (ViewGroup) this.getParent();
        setMeasuredDimension(top.getMeasuredWidth() * 5, MeasureSpec.getSize(heightMeasureSpec));
        Log.d("ViewLayouting",Integer.toString(top.getWidth()));
    }
}
