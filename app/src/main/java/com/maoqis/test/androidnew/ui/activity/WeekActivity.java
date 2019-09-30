package com.maoqis.test.androidnew.ui.activity;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.MyPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.github.thunder413.datetimeutils.DateTimeStyle;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.maoqis.test.androidnew.R;
import com.maoqis.test.androidnew.room.BasicApp;
import com.maoqis.test.androidnew.room.DataRepository;
import com.maoqis.test.androidnew.room.db.entity.Week;
import com.maoqis.test.androidnew.ui.fragment.WeekFragment;
import com.maoqis.test.androidnew.view.DirectionalViewPager;
import com.maoqis.test.androidnew.view.MyVIewPagerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeekActivity extends AppCompatActivity {
    private static final String TAG = "WeekActivity";
    private MyVIewPagerView mViewpager;
    private MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);
        assignViews();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        mViewpager.setOrientation(DirectionalViewPager.HORIZONTAL);
        adapter = new MyPagerAdapter(this,getSupportFragmentManager());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.week, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.search:
                Intent intent = new Intent(this,SearchActivity.class);
                startActivity(intent);
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
        mViewpager = (MyVIewPagerView) findViewById(R.id.viewpager);
    }


}
