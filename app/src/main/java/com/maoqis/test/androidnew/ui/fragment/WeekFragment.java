package com.maoqis.test.androidnew.ui.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maoqis.test.androidnew.R;
import com.maoqis.test.androidnew.net.NetworkComponent;
import com.maoqis.test.androidnew.net.RxException;
import com.maoqis.test.androidnew.room.BasicApp;
import com.maoqis.test.androidnew.room.DataRepository;
import com.maoqis.test.androidnew.room.db.AppDatabase;
import com.maoqis.test.androidnew.room.db.DataGenerator;
import com.maoqis.test.androidnew.room.db.entity.Week;
import com.maoqis.test.androidnew.room.db.entity.WeekItem;
import com.maoqis.test.androidnew.ui.adatper.WeekAdapter;
import com.maoqis.test.androidnew.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by maoqis on 17/12/31.
 */

public class WeekFragment extends Fragment {
    private static final String TAG = "WeekFragment";
    public static final String KEY_WEEK = "week";
    private RecyclerView rcvContent;

    private Week mWeek;
    private WeekAdapter weekAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeek = (Week) getArguments().getSerializable(KEY_WEEK);
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

        loadData();
    }

    private void loadData() {

        BasicApp application = (BasicApp) getActivity().getApplication();
        AppDatabase database = application.getDatabase();
        LiveData<Week> weekIdLiveData = DataRepository.getInstance(database).findWeekId(mWeek.getId());
        weekIdLiveData.observe(this, (week) -> {
            if (database.getDatabaseCreated() != null) {
                if (week == null) {
//                    Call<ResponseBody> responseBodyCall = NetworkComponent.getInstance().loadWeek(mWeek.getId());
//                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
//                        @Override
//                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                            ResponseBody body = response.body();
//                            try {
//                                String string = body.string();
//                                DataGenerator.loadWeekFileData(string,mWeek.getId());
//                                Log.d(TAG, "onResponse: "+string);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//                            Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
//                        }
//                    });

                    Flowable<String> stringFlowable = NetworkComponent.getInstance().loadRxWeek(mWeek.getId());
                    Consumer<String> stringConsumer = responseBody -> {
                        Log.d(TAG, "onNext() called with: responseBody = [" + responseBody + "]");
                        if (responseBody != null) {
                            Week data = DataGenerator.loadWeekFileData(responseBody, mWeek.getId());
                            database.weekDao().insertWeeks(data);
                            database.weekDao().insertWeekItemList(data.getWeekItems());
                            loadDBData();
                        }


                    };
                    stringFlowable.subscribeOn(Schedulers.io())
                            .unsubscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io()).subscribe(stringConsumer, new RxException<>(e -> {
                        e.printStackTrace();
                    }));


                } else {
                    mWeek = week;
                    loadDBData();
                }
            }
        });


    }

    private void loadDBData() {
        BasicApp application = (BasicApp) getActivity().getApplication();
        AppDatabase database = application.getDatabase();

        LiveData<List<WeekItem>> listLiveData = DataRepository.getInstance(database).loadWeekItemsByWeekId(mWeek.getId());
        listLiveData.observe(WeekFragment.this, (weekItemList) -> {
            if (weekItemList == null || weekItemList.isEmpty()) {
                Log.e(TAG, "loadDBData: ",new NullPointerException("mWeek ="+mWeek.getId()) );
            }
            if (weekItemList == null) return;
            if (database.getDatabaseCreated() != null) {
                weekAdapter.getData().clear();
                weekAdapter.getData().addAll(weekItemList);
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
