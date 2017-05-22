package com.studios146.de.stundenplan24;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity{
    Context context = this;
    private ScheduleDataSource dataSource;
    String LOG_TAG = "146s/"+MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*set Activity background
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ImageView view = (ImageView) findViewById(R.id.backgroundImageView);
        String backgroundPath = "http://www.carolinum.net/assets/images/5/schlossallee-1bd6a535.jpg";
        String error="";
        try{
            InputStream inputStream = (InputStream) new URL(backgroundPath).getContent();
            Drawable drawable = Drawable.createFromStream(inputStream,"146Programming");
            if(drawable!= null) {
                view.setBackground(drawable);
                view.setScaleType(ImageView.ScaleType.CENTER);
            }
        }catch (IOException ex){
            Log.d(LOG_TAG+"/background",ex.getMessage());
            error = ex.getMessage();
        }*/
        super.onCreate(savedInstanceState);
        //setContentView(view);
        setContentView(R.layout.activity_main);
        //Log.d(LOG_TAG+"/background","error: "+error);

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

        /*HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.scroll);

        scrollView.getChildAt(0).setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));*/

        ViewPager viewPager = (ViewPager) findViewById(R.id.schedule_main);
        SchedulePagerAdapter schedulePagerAdapter = new SchedulePagerAdapter(getSupportFragmentManager());
        schedulePagerAdapter.setTimetable(new Timetable(this.context));

        viewPager.setAdapter(schedulePagerAdapter);
        viewPager.addOnPageChangeListener(
            new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    setTitle(getResources().getStringArray(R.array.weekdays)[position]);
                }
            }
        );


        List<Lesson> substituteLessons = convertJSONtoLesson(jsonPlan);
        for(Lesson substituteLesson:substituteLessons) {
            Log.d(LOG_TAG, "substituteLesson: " + substituteLesson.toString());
        }
    }

    private void showAllListEntries(){
        List<Lesson> lessonList = dataSource.getAllLessons();

        ArrayAdapter<Lesson> lessonArrayAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,lessonList);
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
