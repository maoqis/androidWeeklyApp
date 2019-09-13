package com.maoqis.test.androidnew.room.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by maoqis on 17/12/30.
 */
@Entity
public class Week implements Serializable{
    @PrimaryKey
    public long id;
    public Date date;
    public String link;
    @Ignore
    public List<WeekItem> weekItems;
    @Ignore
    public Week() {
    }



    public Week(long id, Date date, String link) {
        this.id = id;
        this.date = date;
        this.link = link;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<WeekItem> getWeekItems() {
        return weekItems;
    }

    public void setWeekItems(List<WeekItem> weekItems) {
        this.weekItems = weekItems;
    }

    @Override
    public String toString() {
        return "Week{" +
                "id=" + id +
                ", date=" + date +
                ", link='" + link + '\'' +
                '}';
    }
}
