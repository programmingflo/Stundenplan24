package com.studios146.de.stundenplan24;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by timgrohmann on 05.05.17.
 * Provides custom view to use in main GridView
 */

public class LessonCellView extends LinearLayout {
    private Lesson lesson;
    private Context context;

    TextView lessonTextView;
    TextView subjectTextView;
    TextView teacherTextView;
    TextView roomTextView;
    TextView infoTextView;

    public LessonCellView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        inflate(context, R.layout.lesson_cell_view, this);
        this.lessonTextView = (TextView)findViewById(R.id.lessonTextView);
        this.subjectTextView = (TextView)findViewById(R.id.subjectTextView);
        this.teacherTextView = (TextView)findViewById(R.id.teacherTextView);
        this.roomTextView = (TextView)findViewById(R.id.roomTextView);
        this.infoTextView = (TextView)findViewById(R.id.infoTextView);
    }

    public void setLesson(Lesson lesson){
        if(lesson != null){
            this.lesson = lesson;
        }else{
            this.lesson = new Lesson(1,"11.2","0","0","-","-","-","-",false);
        }

        draw();
    }

    public void draw(){
        if(lesson.substituted){
            lessonTextView.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));
            subjectTextView.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));
            teacherTextView.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));
            roomTextView.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));
            infoTextView.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));
        }
        lessonTextView.setText(lesson.stunde);
        subjectTextView.setText(lesson.fach);
        teacherTextView.setText(lesson.lehrer);
        roomTextView.setText(lesson.raum);
        //infoTextView.setText(lesson.info);
        infoTextView.setText("");
    }
}
