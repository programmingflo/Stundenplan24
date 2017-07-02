package com.studios146.de.stundenplan24;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Created by timgrohmann on 05.05.17.
 *
 */

final class Timetable implements Serializable{
    private Context context;
    private Lesson[][] lessons = new Lesson[5][8];
    private ScheduleDataSource scheduleDataSource;
    private String LOG_TAG = "146s/Timetable";
    private List<Lesson> substituteLessons;
    //TODO: save old substitute json for actual day

    Timetable(Context c){
        this.context = c;
        this.scheduleDataSource = new ScheduleDataSource(context);
        scheduleDataSource.open();
        if(scheduleDataSource.tableExists("schedule")) {
            Lesson[] lessonsRaw = scheduleDataSource.getAllLessons();
            Log.d(LOG_TAG,"lessonsRaw.length="+lessonsRaw.length);
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
            Log.d(LOG_TAG,"schedule table does not exist");
        }
        scheduleDataSource.close();

        //load substitute lessons
        ServerConnection serverConnection = new ServerConnection(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String request = sharedPreferences.getString("schoolID", "");
        if(request.isEmpty()){
            request = "/schools";
        }else{
            request = "/plan/"+request;
        }

        JSONObject jsonPlan = new JSONObject();
        try{
            JSONObject json = serverConnection.execute(request).get();
            if(json == null){
                Log.d(LOG_TAG,"json object null");
            }else {
                Integer status = json.getInt("status");
                Log.d(LOG_TAG, "status " + status);
                jsonPlan = json.getJSONObject("plan");
            }

        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, e.toString());
        }

        substituteLessons = convertJSONtoLesson(jsonPlan);
        for(Lesson substituteLesson:substituteLessons) {
            Log.d(LOG_TAG, "substituteLesson: " + substituteLesson.toString());
        }
        try {
            Object lock = new Object();
            synchronized (lock) {
                lock.wait(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG,"Constructor has finished");
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
                Log.d(LOG_TAG,"lesson2= "+lesson2.toString());

                //lessonDialog
                final Dialog lessonDialog = new Dialog(context);
                lessonDialog.setContentView(R.layout.dialog_lesson);

                //set content
                TextView klasseView = (TextView) lessonDialog.findViewById(R.id.lesson_klasse);
                klasseView.setText(lesson2.klasse);
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
                        EditText klasseEditView = (EditText) editDialog.findViewById(R.id.lesson_edit_klasse);
                        klasseEditView.setText(lesson2.klasse);
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

                                //TODO: get correct course
                                SharedPreferences preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
                                String generalcourse = preferences.getString("course","");
                                Log.d(LOG_TAG,"generalcourse="+generalcourse);
                                if(generalcourse.isEmpty()){
                                    lesson2.klasse = lesson2.fach;
                                }else{
                                    lesson2.klasse = generalcourse;
                                }

                                if(lesson2.id != 0) {
                                    scheduleDataSource.saveLesson(lesson2);
                                }else{
                                    scheduleDataSource.createLesson(lesson2);
                                }
                                lessons[day][hour] = lesson2;
                                //TODO: refresh gui
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
        Log.d(LOG_TAG,"lesson:"+lesson.toString());
        if(!lesson.klasse.equals("")) {
            for (Lesson substitute : substituteLessons) {
                Log.d(LOG_TAG, "substitute:" + substitute.toString());
                if(substitute.klasse != null) {
                    if (substitute.klasse.contains(lesson.klasse) &&
                            (substitute.tag.equals(lesson.tag) && substitute.stunde.equals(lesson.stunde))) {
                        lesson = substitute;
                        Log.d(LOG_TAG, "lesson[" + (lesson.tag) + "][" + (lesson.stunde) + "] substituted");
                    }
                }
            }
        }
        return lesson;
    }

    private Lesson getLessonForPosition(int day, int hour){
        Lesson lesson = null;
        if(lessons.length != 0) {
            Log.d(LOG_TAG,"lesson["+(day)+"]["+(hour)+"]="+lessons[day][hour]);
            lesson = lessons[day][hour];
        }
        if (lesson == null){
            lesson = new Lesson(0, "1", Integer.toString(day + 1), Integer.toString(hour + 1), "-", "-", "-", "-");
        }
        return lesson;
    }

    private List<Lesson> convertJSONtoLesson(JSONObject jsonObject){
        List<Lesson> lessonList = new ArrayList<>();
        Lesson lesson;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("lessons");
            //TODO: get Day
            String day = String.valueOf(getDay(jsonObject.optString("forDate","1")));
            Log.d(LOG_TAG,"forDay: "+day);
            for(int i = 0;i<jsonArray.length();i++){
                JSONObject jsonLesson = jsonArray.getJSONObject(i);
                lesson = new Lesson(0,jsonLesson.optString("rawCourse","1"),
                        day,
                        jsonLesson.optString("lesson","1"),
                        jsonLesson.optString("subject","1"),
                        jsonLesson.optString("teacher","1"),
                        jsonLesson.optString("room","1"),
                        jsonLesson.optString("info","1"));
                lessonList.add(lesson);
            }
            //JSONObject jsonDay = jsonObject.getJSONObject("forDay");

        }catch (Exception ex){
            Log.d(LOG_TAG,ex.getMessage());
            lesson = new Lesson(0,null,null,null,null,null,null,null);
            lessonList.add(lesson);
        }
        return lessonList;
    }
    private int getDay(String unixString){
        Date date = new Date(Long.parseLong(unixString));
        Log.d(LOG_TAG,"Date from Timestamp="+date);
        SimpleDateFormat sdf = new SimpleDateFormat("u", Locale.US);
        Integer day = Integer.parseInt(sdf.format(date));

        if(day > 5){
           day = 1;
        }

        Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.HOUR_OF_DAY) >= 15){
            day++;
        }
        day = day % 7;

        return day;
    }
}
