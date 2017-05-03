package com.studios146.de.stundenplan24;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by FM on 03.05.2017.
 */

public class ScheduleDataSource {
    private static final String LOG_TAG = "146s/"+ ScheduleDbHelper.class.getSimpleName();

    private SQLiteDatabase database;
    private ScheduleDbHelper dbHelper;

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
}
