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

    LessonCellView getViewForPosition(int position){
        LessonCellView l = new LessonCellView(context, null);
        l.setLesson(getLessonForPosition(position));
        return l;
    }

    Lesson getLessonForPosition(int position){
        return new Lesson(0,"1",Integer.toString(position+1),"Deu","VOG","S207","");
    }
}
