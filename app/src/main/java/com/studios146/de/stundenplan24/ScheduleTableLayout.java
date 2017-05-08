package com.studios146.de.stundenplan24;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Created by timgrohmann on 03.05.2017.
 *
 */

public class ScheduleTableLayout extends TableLayout {
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
        for (int i = 0; i < 8; i++) {
            TableRow tableRow = new TableRow(context);
            for (int j = 0; j < 5; j++) {
                LessonCellView view = timetable.getViewForPosition(i);
                tableRow.addView(view);
            }
            addView(tableRow);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(parentWidth, (int) (parentHeight / 8));
    }
}
