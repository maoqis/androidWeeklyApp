package com.maoqis.test.androidnew.room.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.maoqis.test.androidnew.room.db.entity.Week;
import com.maoqis.test.androidnew.room.db.entity.WeekItem;

import java.util.List;

/**
 * Created by bjmaoqisheng on 2017/11/29.
 */
@Dao
public interface WeekDao {
    @Insert
    void insertWeeks(Week... users);


    @Insert
    void insertWeekList(List<WeekItem> userList);

    @Insert
    void insertWeekItemList(List<WeekItem> bookList);



    @Query("SELECT * FROM Week")
    LiveData<List<Week>> loadAllLiveWeeks();


    @Query("SELECT * FROM WeekItem")
    List<WeekItem> loadAllWeekItems();

    @Query("SELECT * from Week where id = :id LIMIT 1")
    LiveData<Week> loadWeekLiveById(long id);
    @Query("SELECT * from Week where id = :id LIMIT 1")
    Week loadWeekById(int id);
    @Query("SELECT * from Week where id = :id LIMIT 1")
    LiveData<Week> loadLiveWeekById(long id);

    /**
     * find week item by week id
     * @param id
     * @return
     */

    @Query("SELECT * from WeekItem where week_id = :id COLLATE NOCASE;")
    LiveData<List<WeekItem>> loadWeekItemsByWeekId(long id);

    /**
     * search
     * @param search
     */
    @Query("SELECT * from WeekItem where title LIKE :search OR des LIKE :search order by week_id desc")
    LiveData<List<WeekItem>> findWeekItemBySearchString(String search);


}
