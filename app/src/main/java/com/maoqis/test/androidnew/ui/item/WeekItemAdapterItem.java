package com.maoqis.test.androidnew.ui.item;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.maoqis.test.androidnew.R;
import com.maoqis.test.androidnew.ui.activity.WebActivity;
import com.maoqis.test.androidnew.room.db.entity.WeekItem;

import kale.adapter.item.AdapterItem;

public class WeekItemAdapterItem implements AdapterItem<WeekItem> {
    public static final String KEY_URL = "url";
    public ViewHolder viewHolder;
    @Override
    public int getLayoutResId() {
        return R.layout.item_week;
    }


    @Override
    public void bindViews(View root) {
        viewHolder = new ViewHolder(root);
    }

    @Override
    public void setViews() {
    }

    @Override
    public void handleData(WeekItem model, int position) {
        viewHolder.mTvTitle.setText(model.getTitle());
        viewHolder.mTvWeek.setText(" week:"+model.weekId);
        String des = model.getDes();
        if (TextUtils.isEmpty(des)){
            viewHolder.mTvDes.setVisibility(View.GONE);
        }else {
            viewHolder.mTvDes.setVisibility(View.VISIBLE);

        }
        viewHolder.mTvDes.setText(des);
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra(KEY_URL,model.getLink());
                context.startActivity(intent);
            }
        });
    }

    public static class ViewHolder {
        public View rootView;
        public TextView mTvTitle;
        public TextView mTvDes;
        public TextView mTvWeek;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.mTvTitle = (TextView) rootView.findViewById(R.id.tv_title);
            this.mTvDes = (TextView) rootView.findViewById(R.id.tv_des);
            this.mTvWeek = (TextView) rootView.findViewById(R.id.tv_week);
        }

    }
}