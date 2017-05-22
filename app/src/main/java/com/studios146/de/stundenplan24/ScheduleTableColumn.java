package com.studios146.de.stundenplan24;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by timgrohmann on 08.05.17.
 *
 */

public class ScheduleTableColumn extends LinearLayout {
    Context context;
    public ScheduleTableColumn(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        inflate(context, R.layout.schedule_tablecolumn, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        View top = (ViewGroup) this.getParent();
        View topTop = (ViewGroup) top.getParent();
        //setMeasuredDimension(topTop.getWidth(), MeasureSpec.getSize(heightMeasureSpec));
    }
}
