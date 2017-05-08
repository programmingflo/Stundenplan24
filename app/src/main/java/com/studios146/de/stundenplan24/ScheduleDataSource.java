package com.studios146.de.stundenplan24;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FM on 03.05.2017.
 */

public class ScheduleDataSource {
    private static final String LOG_TAG = "146s/"+ ScheduleDbHelper.class.getSimpleName();

    private SQLiteDatabase database;
    private ScheduleDbHelper dbHelper;

    private String[] columns = {
            ScheduleDbHelper.COLUMN_ID,
            ScheduleDbHelper.COLUMN_KLASSE,
            ScheduleDbHelper.COLUMN_STUNDE,
            ScheduleDbHelper.COLUMN_FACH,
            ScheduleDbHelper.COLUMN_LEHRER,
            ScheduleDbHelper.COLUMN_RAUM
    };

    public ScheduleDataSource(Context context){
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new ScheduleDbHelper(context);
    }
    public void open(){
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }
    public void close(){
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");

    }
    public Lesson createLesson(String klasse, String stunde, String fach, String lehrer, String raum){
        ContentValues values = new ContentValues();
        values.put(ScheduleDbHelper.COLUMN_ID, "");
        values.put(ScheduleDbHelper.COLUMN_KLASSE, klasse);
        values.put(ScheduleDbHelper.COLUMN_STUNDE, stunde);
        values.put(ScheduleDbHelper.COLUMN_FACH, fach);
        values.put(ScheduleDbHelper.COLUMN_LEHRER, lehrer);
        values.put(ScheduleDbHelper.COLUMN_RAUM, raum);

        long insertId = database.insert(ScheduleDbHelper.TABLE_SCHEDULE, null, values);

        Cursor cursor = database.query(ScheduleDbHelper.TABLE_SCHEDULE, columns,
                ScheduleDbHelper.COLUMN_ID + "=" + insertId,null,null,null,null);
        cursor.moveToFirst();
        Lesson lesson = cursorToLesson(cursor);
        cursor.close();

        return lesson;
    }
    private Lesson cursorToLesson(Cursor cursor){
        int idIndex = cursor.getColumnIndex(ScheduleDbHelper.COLUMN_ID);
        int idKlasse = cursor.getColumnIndex(ScheduleDbHelper.COLUMN_KLASSE);
        int idStunde = cursor.getColumnIndex(ScheduleDbHelper.COLUMN_STUNDE);
        int idFach = cursor.getColumnIndex(ScheduleDbHelper.COLUMN_FACH);
        int idLehrer = cursor.getColumnIndex(ScheduleDbHelper.COLUMN_LEHRER);
        int idRaum = cursor.getColumnIndex(ScheduleDbHelper.COLUMN_RAUM);

        long id = cursor.getLong(idIndex);
        String klasse = cursor.getString(idKlasse);
        String stunde = cursor.getString(idStunde);
        String fach = cursor.getString(idFach);
        String lehrer = cursor.getString(idLehrer);
        String raum = cursor.getString(idRaum);

        Lesson lesson = new Lesson(id,klasse,stunde,fach,lehrer,raum,null);

        return lesson;
    }
    public List<Lesson> getAllLessons(){
        List<Lesson> lessonList = new ArrayList<>();

        Cursor cursor = database.query(ScheduleDbHelper.TABLE_SCHEDULE,columns,null,null,null,null,null);

        cursor.moveToFirst();
        Lesson lesson;

        while (!cursor.isAfterLast()){
            lesson = cursorToLesson(cursor);
            lessonList.add(lesson);
            Log.d(LOG_TAG,"ID: "+lesson.id+", Inhalt: "+lesson.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return lessonList;
    }
}
