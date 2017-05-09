package com.studios146.de.stundenplan24;

import android.content.Context;
import android.util.AttributeSet;
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
        inflate(getContext(), R.layout.schedule_tablecolumn, this);
    }
}
