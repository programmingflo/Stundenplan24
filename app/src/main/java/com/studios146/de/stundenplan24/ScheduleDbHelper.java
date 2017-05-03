package com.studios146.de.stundenplan24;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by FM on 03.05.2017.
 *
 */

public class ScheduleDbHelper extends SQLiteOpenHelper{
    private static final String LOG_TAG = "146s/"+ ScheduleDbHelper.class.getSimpleName();

    public static final String SQL_CREATE = "CREATE TABLE schedule " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "klasse TEXT NOT NULL," +
            "stunde TEXT NOT NULL," +
            "fach TEXT NOT NULL," +
            "lehrer TEXT NOT NULL," +
            "raum TEXT NOT NULL";

    public ScheduleDbHelper(Context context){
        super(context,"schedule",null,1);
        Log.d(LOG_TAG,"DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE + " angelegt.");
            db.execSQL(SQL_CREATE);
        }catch (Exception ex){
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
