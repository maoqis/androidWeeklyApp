package com.maoqis.test.androidnew.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.maoqis.test.androidnew.utils.FileUtils;
import com.maoqis.test.androidnew.R;
import com.maoqis.test.androidnew.room.BasicApp;
import com.maoqis.test.androidnew.room.DataRepository;
import com.maoqis.test.androidnew.room.db.entity.Week;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private boolean needLoad = true;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handleContactPermission();
        findViewById(R.id.rootView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,WeekActivity.class);
                startActivity(intent);
            }
        });

    }

    private void netDo() {
        SharedPreferences sp = this.getSharedPreferences("test", Context.MODE_PRIVATE);
        needLoad = sp.getBoolean("needLoad", true);

        File dir = makeSureDir();
        if (needLoad) {
            FileUtils.copyFilesFassets(this, "weekly", dir.getAbsolutePath() + "/weekly");
            sp.edit().putBoolean("needLoad",false).commit();

        }

        BasicApp application = (BasicApp) getApplication();
        DataRepository repository = application.getRepository();
        LiveData<Week> weekId = repository.findWeekId(160);
        weekId.observe(this, new Observer<Week>() {
            @Override
            public void onChanged(@Nullable Week week) {
                if (application.getDatabase().getDatabaseCreated() != null){
                    Log.d(TAG, "onChanged() called with: week = [" + week + "]");
                }else {
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
