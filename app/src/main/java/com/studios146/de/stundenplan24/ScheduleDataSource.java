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
 *
 * Connection with local database
 */

class ScheduleDataSource {
    private static final String LOG_TAG = "146s/"+ ScheduleDbHelper.class.getSimpleName();

    private SQLiteDatabase database;
    private ScheduleDbHelper dbHelper;

    private String[] columns = {
            ScheduleDbHelper.COLUMN_ID,
            ScheduleDbHelper.COLUMN_KLASSE,
            ScheduleDbHelper.COLUMN_TAG,
            ScheduleDbHelper.COLUMN_STUNDE,
            ScheduleDbHelper.COLUMN_FACH,
            ScheduleDbHelper.COLUMN_LEHRER,
            ScheduleDbHelper.COLUMN_RAUM
    };

    ScheduleDataSource(Context context){
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new ScheduleDbHelper(context);
        database = dbHelper.getWritableDatabase();
        dbHelper.onCreate(database);
    }
    void open(){
        dbHelper.close();

        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        //dbHelper.onOpen(database);
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }
    void close(){
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");

    }
    Lesson createLesson(Lesson lesson){
        open();
        ContentValues values = new ContentValues();
        values.put(ScheduleDbHelper.COLUMN_KLASSE, lesson.klasse);
        values.put(ScheduleDbHelper.COLUMN_TAG, lesson.tag);
        values.put(ScheduleDbHelper.COLUMN_STUNDE, lesson.stunde);
        values.put(ScheduleDbHelper.COLUMN_FACH, lesson.fach);
        values.put(ScheduleDbHelper.COLUMN_LEHRER, lesson.lehrer);
        values.put(ScheduleDbHelper.COLUMN_RAUM, lesson.raum);

        long insertId = database.insert(ScheduleDbHelper.TABLE_SCHEDULE, null, values);

        Cursor cursor = database.query(ScheduleDbHelper.TABLE_SCHEDULE, columns,
                ScheduleDbHelper.COLUMN_ID + "=" + insertId,null,null,null,null);
        cursor.moveToFirst();
        Lesson lesson2 = cursorToLesson(cursor);
        cursor.close();

        close();
        return lesson2;
    }
    private Lesson cursorToLesson(Cursor cursor){
        int idIndex = cursor.getColumnIndex(ScheduleDbHelper.COLUMN_ID);
        Log.d(LOG_TAG,"idIndex="+idIndex);
        int idKlasse = cursor.getColumnIndex(ScheduleDbHelper.COLUMN_KLASSE);
        Log.d(LOG_TAG,"idKlasse="+idKlasse);
        int idTag = cursor.getColumnIndex(ScheduleDbHelper.COLUMN_TAG);
        Log.d(LOG_TAG,"idTag="+idTag);
        int idStunde = cursor.getColumnIndex(ScheduleDbHelper.COLUMN_STUNDE);
        Log.d(LOG_TAG,"idStunde="+idStunde);
        int idFach = cursor.getColumnIndex(ScheduleDbHelper.COLUMN_FACH);
        Log.d(LOG_TAG,"idFach="+idFach);
        int idLehrer = cursor.getColumnIndex(ScheduleDbHelper.COLUMN_LEHRER);
        Log.d(LOG_TAG,"idLehrer="+idLehrer);
        int idRaum = cursor.getColumnIndex(ScheduleDbHelper.COLUMN_RAUM);
        Log.d(LOG_TAG,"idRaum="+idRaum);

        long id = cursor.getLong(idIndex);
        String klasse = cursor.getString(idKlasse);
        String tag = String.valueOf(cursor.getInt(idTag));
        String stunde = cursor.getString(idStunde);
        String fach = cursor.getString(idFach);
        String lehrer = cursor.getString(idLehrer);
        String raum = cursor.getString(idRaum);

        return new Lesson(id,klasse,tag,stunde,fach,lehrer,raum,null,false);
    }
    Lesson[] getAllLessons(){
        List<Lesson> lessonList = new ArrayList<>();

        open();

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
        close();

        return lessonList.toArray(new Lesson[lessonList.size()]);
    }
    Lesson saveLesson(Lesson lesson){
        open();
        ContentValues values = new ContentValues();
        values.put(ScheduleDbHelper.COLUMN_KLASSE, lesson.klasse);
        values.put(ScheduleDbHelper.COLUMN_TAG, lesson.tag);
        values.put(ScheduleDbHelper.COLUMN_STUNDE, lesson.stunde);
        values.put(ScheduleDbHelper.COLUMN_FACH, lesson.fach);
        values.put(ScheduleDbHelper.COLUMN_LEHRER, lesson.lehrer);
        values.put(ScheduleDbHelper.COLUMN_RAUM, lesson.raum);

        database.update(ScheduleDbHelper.TABLE_SCHEDULE,values,ScheduleDbHelper.COLUMN_ID + "="+lesson.id,null);

        Cursor cursor = database.query(ScheduleDbHelper.TABLE_SCHEDULE, columns,
                ScheduleDbHelper.COLUMN_ID + "=" + lesson.id,null,null,null,null);
        cursor.moveToFirst();
        Lesson lesson2 = cursorToLesson(cursor);
        cursor.close();

        close();
        return lesson2;
    }
    boolean tableExists(String tableName) {
        Cursor cursor = database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }
}

