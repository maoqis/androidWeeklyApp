package com.maoqis.test.androidnew.room.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by maoqis on 17/12/30.
 */
@Entity(foreignKeys = @ForeignKey(entity = Week.class, parentColumns = "id", childColumns = "week_id"),indices = {@Index("id"),@Index("week_id")})

public class WeekItem {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String title;
    public String des;
    public String link;
    public String type;
    @ColumnInfo(name = "week_id")
    public long weekId;
    @ColumnInfo(name = "origin_type")
    public int originType;//
    @Ignore
    public WeekItem() {
    }

    public WeekItem(String title, String des, String link, String type, long weekId) {
        this.title = title;
        this.des = des;
        this.link = link;
        this.type = type;
        this.weekId = weekId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getOriginType() {
        return originType;
    }

    public void setOriginType(int originType) {
        this.originType = originType;
    }

    public long getWeekId() {
        return weekId;
    }

    public void setWeekId(long weekId) {
        this.weekId = weekId;
    }

    @Override
    public String toString() {
        return "WeekItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", des='" + des + '\'' +
                ", link='" + link + '\'' +
                ", type='" + type + '\'' +
                ", weekId=" + weekId +
                ", originType=" + originType +
                '}';
    }
}
