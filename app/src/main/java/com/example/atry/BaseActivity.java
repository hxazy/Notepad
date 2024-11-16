package com.example.atry;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;

import com.githang.statusbar.StatusBarCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public abstract class BaseActivity extends AppCompatActivity {

    public final String TAG = "BaseActivity";
    public final String ACTION = "NIGHT_SWITCH";
    protected BroadcastReceiver receiver;
    protected IntentFilter filter;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setNightMode();
        TypedValue typedValue = new TypedValue();
        this.getTheme().resolveAttribute(R.attr.tvBackground, typedValue, true);
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, typedValue.resourceId));

        filter = new IntentFilter();
        filter.addAction(ACTION);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                needRefresh();
            }
        };

        registerReceiver(receiver, filter);
    }

    public boolean isNightMode(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        return sharedPreferences.getBoolean("nightMode", false);
    }

    public void setNightMode(){
        if(isNightMode()) this.setTheme(R.style.NightTheme);
        else setTheme(R.style.DayTheme);

    }

    protected abstract void needRefresh();

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public long calStrToSec(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long secTime = format.parse(date).getTime();
        return secTime;
    }
}
