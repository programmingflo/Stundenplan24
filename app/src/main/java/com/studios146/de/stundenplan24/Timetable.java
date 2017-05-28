package com.studios146.de.stundenplan24;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by timgrohmann on 05.05.17.
 *
 */

final class Timetable implements Serializable{
    private Context context;
    private Lesson[][] lessons = new Lesson[5][8];
    private ScheduleDataSource scheduleDataSource;

    Timetable(Context c){
        this.context = c;
        this.scheduleDataSource = new ScheduleDataSource(context);
        scheduleDataSource.open();
        if(scheduleDataSource.tableExists("schedule")) {
            Lesson[] lessonsRaw = scheduleDataSource.getAllLessons();
            Log.d("146s/Timetable","lessonsRaw.length="+lessonsRaw.length);
            if(lessonsRaw.length != 0) {
                for (Lesson lesson : lessonsRaw) {
                    lessons[Integer.parseInt(lesson.tag)-1][Integer.parseInt(lesson.stunde)-1] = lesson;
                }
            }else{
                for(int i = 0; i<=4;i++){
                    for(int j = 0; j<=7;j++) {
                        scheduleDataSource.createLesson(new Lesson(0,"-", String.valueOf(i+1), String.valueOf(j+1), "-", "-", "-",""));
                    }
                }
            }
        }else{
            Log.d("146s/Timetable","schedule table does not exist");
        }
        scheduleDataSource.close();
        Log.d("146s/Timetable","Constructor has finished");
    }

    LessonCellView getViewForPosition(final int day, final int hour){
        LessonCellView l = new LessonCellView(context, null);
        Lesson lesson1 = getLessonForPosition(day,hour);
        lesson1 = integrateSubstitution(lesson1);
        l.setLesson(lesson1);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Lesson lesson2 = getLessonForPosition(day,hour);
                Log.d("146s/Timetable","lesson2= "+lesson2.toString());

                //lessonDialog
                final Dialog lessonDialog = new Dialog(context);
                lessonDialog.setContentView(R.layout.dialog_lesson);

                //set content
                TextView fachView = (TextView) lessonDialog.findViewById(R.id.lesson_fach);
                fachView.setText(lesson2.fach);
                TextView raumView = (TextView) lessonDialog.findViewById(R.id.lesson_raum);
                raumView.setText(lesson2.raum);
                TextView lehrerView = (TextView) lessonDialog.findViewById(R.id.lesson_lehrer);
                lehrerView.setText(lesson2.lehrer);
                TextView infoView = (TextView) lessonDialog.findViewById(R.id.lesson_info);
                infoView.setText(lesson2.info);

                //set Buttons
                final ImageButton editButton = (ImageButton) lessonDialog.findViewById(R.id.editButton);
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //editDialog
                        final Dialog editDialog = new Dialog(context);
                        editDialog.setContentView(R.layout.edit_lesson);

                        //set content
                        final EditText fachEditView = (EditText) editDialog.findViewById(R.id.lesson_edit_fach);
                        fachEditView.setText(lesson2.fach);
                        final EditText raumEditView = (EditText) editDialog.findViewById(R.id.lesson_edit_raum);
                        raumEditView.setText(lesson2.raum);
                        final EditText lehrerEditView = (EditText) editDialog.findViewById(R.id.lesson_edit_lehrer);
                        lehrerEditView.setText(lesson2.lehrer);

                        //set Buttons
                        ImageButton saveEditButton = (ImageButton) editDialog.findViewById(R.id.saveEditButton);
                        saveEditButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //save Input
                                lesson2.fach = fachEditView.getText().toString();
                                lesson2.raum = raumEditView.getText().toString();
                                lesson2.lehrer = lehrerEditView.getText().toString();

                                if(lesson2.id != 0) {
                                    scheduleDataSource.saveLesson(lesson2);
                                }else{
                                    scheduleDataSource.createLesson(lesson2);
                                }
                                lessons[day][hour] = lesson2;
                                editDialog.dismiss();
                            }
                        });
                        ImageButton closeEditButton = (ImageButton) editDialog.findViewById(R.id.closeEditButton);
                        closeEditButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editDialog.dismiss();
                            }
                        });
                        editDialog.show();
                    }
                });
                ImageButton closeButton = (ImageButton) lessonDialog.findViewById(R.id.closeButton);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lessonDialog.dismiss();
                    }
                });
                lessonDialog.show();
            }
        });
        return l;
    }

    private Lesson integrateSubstitution(Lesson lesson) {
        return lesson;
    }

    Lesson getLessonForPosition(int day, int hour){
        Lesson lesson = null;
        //TODO: compare with substitutions
        if(lessons.length != 0) {
            Log.d("146s/Timetable","lessons["+(day)+"]["+(hour)+"]="+lessons[day][hour]);
            lesson = lessons[day][hour];
        }
        if (lesson == null){
            lesson = new Lesson(0, "1", Integer.toString(day + 1), Integer.toString(hour + 1), "-", "-", "-", "-");
        }
        return lesson;
    }
}
