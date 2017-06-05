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
import android.support.v4.widget.SwipeRefreshLayout;
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
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity{
    public Context context = this;
    ScheduleDataSource dataSource;
    String LOG_TAG = "146s/"+MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new ScheduleDataSource(context);

        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_location);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTimetable();
            }
        });

        loadTimetable();
    }

    public void loadTimetable(){
        ViewPager viewPager = (ViewPager) findViewById(R.id.schedule_main);
        SchedulePagerAdapter schedulePagerAdapter = new SchedulePagerAdapter(getSupportFragmentManager());
        schedulePagerAdapter.setTimetable(new Timetable(this.context));

        Integer day;
        switch (Calendar.DAY_OF_WEEK){
            case Calendar.MONDAY : day = 1; break;
            case Calendar.TUESDAY : day = 2; break;
            case Calendar.WEDNESDAY : day = 3; break;
            case Calendar.THURSDAY : day = 4; break;
            case Calendar.FRIDAY : day = 5; break;
            case Calendar.SATURDAY : day = 1; break;
            case Calendar.SUNDAY : day = 1; break;
            default: day = 1; break;
        }

        viewPager.setAdapter(schedulePagerAdapter);
        viewPager.setCurrentItem(day);
        viewPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        setTitle(getResources().getStringArray(R.array.weekdays)[position]);
                        //TODO: add actual date
                    }
                }
        );
        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_location);
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
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