package com.sun.bingo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.sun.bingo.constant.ConstantParams;
import com.sun.bingo.control.manager.LocationManager;
import com.sun.bingo.framework.proxy.ControlFactory;
import com.sun.bingo.sharedpreferences.FastJsonSerial;

import cn.bmob.v3.Bmob;
import de.devland.esperandro.Esperandro;
import im.fir.sdk.FIR;

/**
 * Created by sunfusheng on 15/7/18.
 */
public class BingoApplication extends Application {

    public static final String APP_CACHE_DIR = "Bingo/cache";
    private static BingoApplication mAppInstance;
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppInstance = this;
        mAppContext = getApplicationContext();

        Esperandro.setSerializer(new FastJsonSerial());
        ControlFactory.init(this);
        initLocationManager();

        FIR.init(this);
        Bmob.initialize(this, ConstantParams.BMOB_APP_ID);
    }

    public static BingoApplication getInstance() {
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
