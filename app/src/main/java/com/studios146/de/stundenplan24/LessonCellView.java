package com.studios146.de.stundenplan24;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
        this.lesson = lesson;
        draw();
    }

    public void draw(){
        lessonTextView.setText(lesson.stunde);
        subjectTextView.setText(lesson.fach);
        teacherTextView.setText(lesson.lehrer);
        roomTextView.setText(lesson.raum);
        infoTextView.setText(lesson.info);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        View top = (ViewGroup) this.getParent();
        View topTop = (ViewGroup) top.getParent();
        setMeasuredDimension(topTop.getWidth() / 5, MeasureSpec.getSize(heightMeasureSpec));
    }
}
