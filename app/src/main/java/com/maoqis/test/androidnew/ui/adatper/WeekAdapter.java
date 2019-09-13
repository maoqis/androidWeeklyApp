package com.maoqis.test.androidnew.ui.adatper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maoqis.test.androidnew.room.db.entity.WeekItem;
import com.maoqis.test.androidnew.ui.item.WeekItemAdapterItem;

import java.util.List;

import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

/**
 * Created by maoqis on 17/12/31.
 */

public class WeekAdapter extends CommonRcvAdapter<WeekItem> {
    public WeekAdapter(@Nullable List<WeekItem> data) {
        super(data);
    }

    @NonNull
    @Override
    public AdapterItem createItem(Object o) {
        return new WeekItemAdapterItem();
    }

    @Override
    public Object getItemType(WeekItem weekItem) {
        return super.getItemType(weekItem);
    }
}
