package com.maoqis.test.androidnew.room.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(foreignKeys = @ForeignKey(entity = WeekItem.class, parentColumns = "id", childColumns = "item_id"), indices = {@Index("item_id")})
public class History {
    @PrimaryKey
    public long item_id;
    @ColumnInfo(name = "date")
    public Date date;

    @Override
    public String toString() {
        return "History{" +
                "item_id=" + item_id +
                ", date=" + date +
                '}';
    }
}
