package com.maoqis.test.androidnew.room.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.maoqis.test.androidnew.room.db.entity.History;
import com.maoqis.test.androidnew.room.db.entity.HistoryWeekItem;
import com.maoqis.test.androidnew.room.db.entity.Week;
import com.maoqis.test.androidnew.room.db.entity.WeekItem;

import java.util.List;

/**
 * Created by bjmaoqisheng on 2017/11/29.
 */
@Dao
public interface WeekDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeeks(Week... week);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeekList(List<WeekItem> userList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
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
     *
     * @param id
     * @return
     */

    @Query("SELECT * from WeekItem where week_id = :id COLLATE NOCASE;")
    LiveData<List<WeekItem>> loadWeekItemsByWeekId(long id);

    /**
     * search
     *
     * @param search
     */
    @Query("SELECT * from WeekItem where title LIKE :search OR des LIKE :search order by week_id desc")
    LiveData<List<WeekItem>> findWeekItemBySearchString(String search);

    @Transaction
    @Query("SELECT * from WeekItem INNER JOIN  History where WeekItem.id = item_id order by History.date desc")
    LiveData<List<HistoryWeekItem>> history();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistory(History... week);
}
