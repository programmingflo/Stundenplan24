package com.studios146.de.stundenplan24;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;

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
        Calendar calendar = Calendar.getInstance();
        switch (Calendar.DAY_OF_WEEK){
            case Calendar.MONDAY : day = 0; break;
            case Calendar.TUESDAY : day = 1; break;
            case Calendar.WEDNESDAY : day = 2; break;
            case Calendar.THURSDAY : day = 3; break;
            case Calendar.FRIDAY : day = 4; break;
            case Calendar.SATURDAY : day = 0; break;
            case Calendar.SUNDAY : day = 0; break;
            default: day = 0; break;
        }

        if(calendar.get(Calendar.HOUR_OF_DAY) >= 15){
            day++;
        }
        day = day % 7;
        Log.d(LOG_TAG,"day="+day);

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