package android.support.v4.app;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import com.maoqis.test.androidnew.room.db.entity.Week;
import com.maoqis.test.androidnew.ui.fragment.WeekFragment;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends FragmentPagerAdapter {
    public List<Week> weeks = new ArrayList<>();
    Context context;
    public MyPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putSerializable("week", weeks.get(position));
        return WeekFragment.instantiate(context, WeekFragment.class.getName(), args);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment f = (Fragment) super.instantiateItem(container, position);
        Bundle savedFragmentState = f.mSavedFragmentState;
        if (savedFragmentState != null) {
            savedFragmentState.setClassLoader(f.getClass().getClassLoader());
        }
        return f;
    }


    @Override
    public int getCount() {
        return weeks.size();
    }
}