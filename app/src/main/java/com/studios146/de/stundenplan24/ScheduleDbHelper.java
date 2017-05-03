package com.studios146.de.stundenplan24;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by FM on 03.05.2017.
 */

public class ScheduleDbHelper extends SQLiteOpenHelper{
    private static final String LOG_TAG = "146s/"+ ScheduleDbHelper.class.getSimpleName();

    public ScheduleDbHelper(Context context){
        super(context,"schedule",null,1);
        Log.d(LOG_TAG,"DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
