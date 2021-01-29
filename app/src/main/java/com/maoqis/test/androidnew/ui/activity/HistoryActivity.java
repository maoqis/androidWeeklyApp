package com.maoqis.test.androidnew.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.maoqis.test.androidnew.R;
import com.maoqis.test.androidnew.ui.fragment.HistoryFragment;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_root, new HistoryFragment());
        fragmentTransaction.commitAllowingStateLoss();
    }
}