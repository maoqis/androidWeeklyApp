package com.maoqis.test.androidnew.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.maoqis.test.androidnew.R;
import com.maoqis.test.androidnew.net.NetworkComponent;
import com.maoqis.test.androidnew.net.RxException;
import com.maoqis.test.androidnew.room.BasicApp;
import com.maoqis.test.androidnew.room.DataRepository;
import com.maoqis.test.androidnew.room.db.DataGenerator;
import com.maoqis.test.androidnew.room.db.entity.Week;
import com.maoqis.test.androidnew.utils.FileUtils;

import java.io.File;

import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private boolean needLoad = true;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handleContactPermission();

        findViewById(R.id.tv_history).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.tv_enter_weekly).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, WeekActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.tv_test_date).setOnClickListener(view -> {
            int week = 296;
            NetworkComponent.getInstance().loadRxWeek(week)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(responseBody -> {
                        Log.d(TAG, "onNext() called with: responseBody = [" + responseBody + "]");
                        if (responseBody != null) {
                            Week data = DataGenerator.loadWeekFileData(responseBody, week);
                        }


                    }, new RxException<>(e -> {
                        e.printStackTrace();
                    }));

        });
    }

    private void netDo() {
        SharedPreferences sp = this.getSharedPreferences("test", Context.MODE_PRIVATE);
        needLoad = sp.getBoolean("needLoad", true);

        File dir = makeSureDir();
        if (needLoad) {
            FileUtils.copyFilesFassets(this, "weekly", dir.getAbsolutePath() + "/weekly");
            sp.edit().putBoolean("needLoad", false).commit();

        }

        BasicApp application = (BasicApp) getApplication();
        DataRepository repository = application.getRepository();
        LiveData<Week> weekId = repository.findWeekId(160);
        weekId.observe(this, new Observer<Week>() {
            @Override
            public void onChanged(@Nullable Week week) {
                if (application.getDatabase().getDatabaseCreated() != null) {
                    Log.d(TAG, "onChanged() called with: week = [" + week + "]");
                } else {
                    Log.d(TAG, "db not be created onChanged() called with: week = [" + week + "]");
                }

            }
        });


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void handleContactPermission() {
        if (Integer.parseInt(Build.VERSION.SDK) >= 23) {
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    requestContactPermission();//没有权限的话，申请。
                    return;
                }

            } else {
                netDo();
            }
        }

    }

    private void requestContactPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请 WRITE_CONTACTS 权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        handleGrantResults(requestCode, grantResults);
    }

    private void handleGrantResults(int requestCode, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted 获得权限后执行xxx
                netDo();
            } else {
                // Permission Denied 拒绝后xx的操作。
            }
        }
    }

    public File makeSureDir() {
        String dir = Environment.getExternalStorageDirectory() + "/androidNew";
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdir();
            needLoad = true;
        } else {
            if (!file.isDirectory()) {
                file.delete();
                file.mkdir();
                needLoad = true;
            } else {

            }
        }
        return file;
    }
}
