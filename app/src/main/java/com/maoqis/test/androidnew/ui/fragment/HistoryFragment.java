package com.maoqis.test.androidnew.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maoqis.test.androidnew.R;
import com.maoqis.test.androidnew.room.BasicApp;
import com.maoqis.test.androidnew.room.DataRepository;
import com.maoqis.test.androidnew.room.db.AppDatabase;
import com.maoqis.test.androidnew.room.db.entity.HistoryWeekItem;
import com.maoqis.test.androidnew.room.db.entity.WeekItem;
import com.maoqis.test.androidnew.ui.adatper.WeekAdapter;
import com.maoqis.test.androidnew.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maoqis on 17/12/31.
 */

public class HistoryFragment extends Fragment {
    private static final String TAG = "WeekFragment";
    private RecyclerView rcvContent;

    private WeekAdapter weekAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_week, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        rcvContent = (RecyclerView) view.findViewById(R.id.rcv_content);


        weekAdapter = new WeekAdapter(new ArrayList<>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvContent.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL));

        rcvContent.setLayoutManager(linearLayoutManager);
        rcvContent.setHasFixedSize(true);
        rcvContent.setAdapter(weekAdapter);
        loadDBData();
    }


    private void loadDBData() {
        BasicApp application = (BasicApp) getActivity().getApplication();
        AppDatabase database = application.getDatabase();

        LiveData<List<HistoryWeekItem>> listLiveData = DataRepository.getInstance(database).history();
        listLiveData.observe(HistoryFragment.this, (weekItemList) -> {
            if (weekItemList == null || weekItemList.isEmpty()) {
                Log.e(TAG, "loadDBData: ", new NullPointerException("history ="));
            }
            if (weekItemList == null) return;
            if (database.getDatabaseCreated() != null) {
                weekAdapter.getData().clear();
                List<WeekItem> lists = new ArrayList<>();
                for (int i = 0; i < weekItemList.size(); i++) {
                    HistoryWeekItem historyWeekItem = weekItemList.get(i);
                    lists.add(historyWeekItem.weekItem);
                }
                weekAdapter.getData().addAll(lists);
                System.out.println(weekItemList);
                weekAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }


}
