package com.studios146.de.stundenplan24;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.sql.Time;

/**
 * Created by timgrohmann on 22.05.17.
 * Fragment Adapter for getting views to ViewPager
 */

final class SchedulePagerAdapter extends FragmentPagerAdapter {

    private Timetable t;

    void setTimetable(Timetable t){
        this.t = t;
    }

    SchedulePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Fragment getItem(int position) {
        ScheduleTableColumn column = new ScheduleTableColumn();
        column.day = position;
        column.t = this.t;

        Bundle args = new Bundle();
        args.putSerializable("tt", t);

        column.setArguments(args);

        return column;
    }


}
