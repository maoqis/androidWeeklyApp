package com.maoqis.test.androidnew.room.db.subset;


import androidx.room.ColumnInfo;

public class NameTuple {
    @ColumnInfo(name="first_name")
    public String firstName;

    @ColumnInfo(name="last_name")
    public String lastName;
}