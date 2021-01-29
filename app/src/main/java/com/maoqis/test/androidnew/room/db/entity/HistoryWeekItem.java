package com.maoqis.test.androidnew.room.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.PrimaryKey;

import java.util.Date;

public class HistoryWeekItem {
    @Embedded
    public WeekItem weekItem;
    @PrimaryKey
    public long item_id;
    @ColumnInfo(name = "date")
    public Date date;
}
