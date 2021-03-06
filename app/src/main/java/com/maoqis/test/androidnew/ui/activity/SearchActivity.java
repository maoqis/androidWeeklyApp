package com.maoqis.test.androidnew.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maoqis.test.androidnew.R;
import com.maoqis.test.androidnew.room.BasicApp;
import com.maoqis.test.androidnew.room.DataRepository;
import com.maoqis.test.androidnew.room.db.entity.WeekItem;
import com.maoqis.test.androidnew.ui.adatper.WeekAdapter;
import com.maoqis.test.androidnew.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    private RecyclerView mRcvContent;
    private WeekAdapter weekAdapter;
    private SearchView mSearchView;
    private MenuItem mSearchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        weekAdapter = new WeekAdapter(new ArrayList<>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRcvContent.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL));

        mRcvContent.setLayoutManager(linearLayoutManager);
        mRcvContent.setHasFixedSize(true);
        mRcvContent.setAdapter(weekAdapter);
        loadData("");


    }

    private void loadData(String searchString) {
        DataRepository repository = BasicApp.getRepository(this.getApplication());
        LiveData<List<WeekItem>> liveData = repository.findWeekItemBySearchString(searchString);
        liveData.observe(this, (weekItems) -> {
            List<WeekItem> data = weekAdapter.getData();
            data.clear();
            data.addAll(weekItems);
            weekAdapter.notifyDataSetChanged();
            mRcvContent.scrollToPosition(0);
        });
    }


    private void initView() {
        mRcvContent = (RecyclerView) findViewById(R.id.rcv_content);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        mSearchItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) mSearchItem.getActionView();
        // 核心是利用SearchView此事件监听用户在搜索框中的输入文字变化，同时根据用户实时输入的文字立即返回相应的搜索结果。
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String queryText) {
                Log.d(TAG, "onQueryTextChange() called with: queryText = [" + queryText + "]");
                loadData(queryText);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String queryText) {
                Log.d(TAG, "onQueryTextSubmit() called with: queryText = [" + queryText + "]");
                return true;
            }
        });
        mSearchItem.expandActionView();
        return super.onCreateOptionsMenu(menu);
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
}
