package com.sun.bingo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.framework.DroidFramework;
import com.sun.bingo.control.manager.LocationManager;

import java.io.File;

/**
 * Created by sunfusheng on 15/7/18.
 */
public class BingoApp extends Application {

    public static final String APP_ROOT_DIR = "Bingo";
    public static final String APP_CACHE_DIR = APP_ROOT_DIR + File.separator + "cache";
    private static BingoApp mAppInstance;
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppInstance = this;
        mAppContext = getApplicationContext();

        DroidFramework.init(this, BuildConfig.DEBUG, BuildConfig.LOG_DEBUG);
        initLocationManager();
    }

    public static BingoApp getInstance() {
        return mAppInstance;
    }

    public static Context getContext() {
        return mAppContext;
    }

    private void initLocationManager() {
        LocationManager locationManager = new LocationManager(this);
        locationManager.startGetLocation();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
