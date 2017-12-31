package com.maoqis.test.androidnew.ui.activity;

import android.arch.lifecycle.LiveData;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.github.thunder413.datetimeutils.DateTimeStyle;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.maoqis.test.androidnew.R;
import com.maoqis.test.androidnew.room.BasicApp;
import com.maoqis.test.androidnew.room.DataRepository;
import com.maoqis.test.androidnew.room.db.entity.Week;
import com.maoqis.test.androidnew.ui.fragment.WeekFragment;
import com.maoqis.test.androidnew.view.DirectionalViewPager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private DirectionalViewPager mViewpager;
    private MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        assignViews();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewpager.setOrientation(DirectionalViewPager.HORIZONTAL);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(adapter);
        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Week week = adapter.weeks.get(position);

                setTitle(week);

                if (week.getLink() != null) {
                    if (adapter.weeks.size() - 1 == position) {
                        Week newWeek = new Week();
                        newWeek.id = week.id + 1;
                        adapter.weeks.add(newWeek);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        BasicApp application = (BasicApp) getApplication();
        DataRepository repository = application.getRepository();
        LiveData<List<Week>> weekId = repository.loadAllLiveWeeks();
        weekId.observe(this, (weeks) -> {

            if (application.getDatabase().getDatabaseCreated() != null) {
                Log.d(TAG, "onChanged() called with: week = [" + weeks + "]");
                adapter.weeks.clear();
                adapter.weeks.addAll(weeks);
                adapter.notifyDataSetChanged();
                if (weeks.size() > 0) {
                    setTitle(weeks.get(0));
                }
                mViewpager.setCurrentItem(weeks.size() - 1);

            } else {
                Log.d(TAG, "db not be created onChanged() called with: week = [" + weeks + "]");
            }


        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void setTitle(Week week) {
        String title = "第" + week.getId() + "期 ";
        Date date = week.getDate();
        if (date != null) {
            String dateString = DateTimeUtils.formatWithStyle(date, DateTimeStyle.SHORT);
            title += dateString;
        }

        getSupportActionBar().setTitle(title);
    }


    private void assignViews() {
        mViewpager = (DirectionalViewPager) findViewById(R.id.viewpager);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        List<Week> weeks = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putSerializable("week", weeks.get(position));
            return WeekFragment.instantiate(getApplicationContext(), WeekFragment.class.getName(), args);

        }

        @Override
        public int getCount() {
            return weeks.size();
        }
    }
}
