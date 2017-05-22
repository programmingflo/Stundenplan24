package com.studios146.de.stundenplan24;

import android.content.Context;

/**
 * Created by timgrohmann on 05.05.17.
 *
 */

final class Timetable {
    private Context context;
    private Lesson[] lessons;

    Timetable(Context c){
        this.context = c;
    }

    LessonCellView getViewForPosition(int day, int hour){
        LessonCellView l = new LessonCellView(context, null);
        l.setLesson(getLessonForPosition(day,hour));
        return l;
    }

    Lesson getLessonForPosition(int day, int hour){
        return new Lesson(0,"1",Integer.toString(hour+1),"Deu","VOG","S207","Info");
    }
}
