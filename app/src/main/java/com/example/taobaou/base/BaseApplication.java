package com.example.taobaou.base;

import android.app.Application;
import android.content.Context;

import com.example.taobaou.utils.SharedPreferenceManager;

public class BaseApplication  extends Application {
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext=getBaseContext();
        SharedPreferenceManager.getInstance().init();
    }
    public static Context getAppContext(){
        return appContext;
    }

}
