package com.maoqis.test.androidnew.room;

import android.arch.lifecycle.LiveData;

import com.maoqis.test.androidnew.room.db.AppDatabase;
import com.maoqis.test.androidnew.room.db.entity.Week;
import com.maoqis.test.androidnew.room.db.entity.WeekItem;

import java.util.List;

/**
 * Repository handling the work with products and comments.
 */
public class DataRepository {

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;

    private DataRepository(final AppDatabase database) {
        mDatabase = database;

    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }



    public LiveData<Week> findWeekId(long i) {
        LiveData<Week> week = mDatabase.weekDao().loadLiveWeekById(i);
        return week;
    }

    public LiveData<List<Week>> loadAllLiveWeeks() {
        LiveData<List<Week>> week = mDatabase.weekDao().loadAllLiveWeeks();
        return week;
    }


    public LiveData<List<WeekItem>> loadWeekItemsByWeekId(long i) {

        return mDatabase.weekDao().loadWeekItemsByWeekId(i);
    }

    public LiveData<List<WeekItem>> findWeekItemBySearchString(String searchString) {
        searchString= "%"+searchString+"%";
        return mDatabase.weekDao().findWeekItemBySearchString(searchString);

    }
}
