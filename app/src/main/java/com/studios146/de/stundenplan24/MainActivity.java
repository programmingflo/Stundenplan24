package com.studios146.de.stundenplan24;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity{
    Context context = this;
    private ScheduleDataSource dataSource;
    String LOG_TAG = "146s/"+MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ServerConnection serverConnection = new ServerConnection(this);

        SharedPreferences sharedPreferences = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String request = sharedPreferences.getString("schoolID", "");
        Boolean school_selected = false;
        if(request.isEmpty()){
            request = "/schools";
            school_selected = false;
        }else{
            request = "/plan/"+request;
            school_selected = true;
        }

        JSONObject jsonPlan = new JSONObject();
        try{
            JSONObject json = serverConnection.execute(request).get();
            if(json == null){
                Log.d("146s","json object null");
            }else {
                Integer status = json.getInt("status");
                Log.d("146s", "status " + status);
                jsonPlan = json.getJSONObject("plan");
            }

        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
            Log.d("com.146s.main", e.toString());
        }

        ScheduleTableLayout scheduleTable = (ScheduleTableLayout) findViewById(R.id.schedule_main);
        scheduleTable.setTimetable(new Timetable(this.context));

        List<Lesson> substituteLessons = convertJSONtoLesson(jsonPlan);
        for(Lesson substituteLesson:substituteLessons) {
            Log.d(LOG_TAG, "substituteLesson: " + substituteLesson.toString());
        }
    }

    private void showAllListEntries(){
        List<Lesson> lessonList = dataSource.getAllLessons();

        ArrayAdapter<Lesson> lessonArrayAdapter = new ArrayAdapter<Lesson>(
                this,R.layout.support_simple_spinner_dropdown_item,lessonList);
    }

    public List<Lesson> convertJSONtoLesson(JSONObject jsonObject){
        List<Lesson> lessonList = new ArrayList<>();
        Lesson lesson;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("lessons");
            for(int i = 0;i<jsonArray.length();i++){
                JSONObject jsonLesson = jsonArray.getJSONObject(i);
                lesson = new Lesson(0,jsonLesson.optString("rawCourse"),
                        jsonLesson.optString("lesson"),jsonLesson.optString("subject"),
                        jsonLesson.optString("teacher"),jsonLesson.optString("room"),
                        jsonLesson.optString("info"));
                lessonList.add(lesson);
            }
        }catch (Exception ex){
            Log.d(LOG_TAG+"/96",ex.getMessage());
            lesson = new Lesson(0,null,null,null,null,null,null);
            lessonList.add(lesson);
        }
        return lessonList;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
