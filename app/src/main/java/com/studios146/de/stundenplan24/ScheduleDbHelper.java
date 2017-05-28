package com.studios146.de.stundenplan24;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by FM on 03.05.2017.
 *
 */

public class ScheduleDbHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = "146s/" + ScheduleDbHelper.class.getSimpleName();

    public static final String DB_NAME = "schedules";
    public static final int DB_VERSION = 1;

    public static final String TABLE_SCHEDULE = "schedule";


    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_KLASSE = "klasse";
    public static final String COLUMN_TAG = "tag";
    public static final String COLUMN_STUNDE = "stunde";
    public static final String COLUMN_FACH = "fach";
    public static final String COLUMN_LEHRER = "lehrer";
    public static final String COLUMN_RAUM = "raum";


    public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_SCHEDULE +
            "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_KLASSE + " TEXT NOT NULL," +
            COLUMN_TAG + " TEXT NOT NULL," +
            COLUMN_STUNDE + " TEXT NOT NULL," +
            COLUMN_FACH + " TEXT NOT NULL," +
            COLUMN_LEHRER + " TEXT NOT NULL," +
            COLUMN_RAUM + " TEXT NOT NULL)";

    public ScheduleDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE + " angelegt.");
            db.execSQL(SQL_CREATE);
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.d(LOG_TAG,"database is opened");
    }

    @Override
    public synchronized void close() {
        super.close();
        Log.d(LOG_TAG,"database is closed");
    }
}
