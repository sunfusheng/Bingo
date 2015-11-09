package com.sun.bingo;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.sun.bingo.constant.ConstantParams;
import com.sun.bingo.framework.http.HttpControl.HttpControl;
import com.sun.bingo.framework.proxy.ControlFactory;
import com.sun.bingo.module.LocationManager;
import com.sun.bingo.sharedpreferences.FastJsonSerial;

import cn.bmob.v3.Bmob;
import de.devland.esperandro.Esperandro;
import im.fir.sdk.FIR;

/**
 * Created by sunfusheng on 15/7/18.
 */
public class BingoApplication extends Application {

    public static final String APP_CACHE_DIR = "Bingo/cache";
    private static BingoApplication instance;
    private static HttpControl mHttpControl;

    @Override
    public void onCreate() {
        super.onCreate();

        Esperandro.setSerializer(new FastJsonSerial());
        ControlFactory.init(this); //Control代理工场初始化
        initImageLoader(this);
        initLocationManager();

        FIR.init(this);
        Bmob.initialize(this, ConstantParams.BMOB_APP_ID);
        Fresco.initialize(this);
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());
    }

    public static synchronized BingoApplication getInstance() {
        if (instance == null) {
            instance = new BingoApplication();
        }
        return instance;
    }

    public HttpControl getHttpControl(){
        if(mHttpControl==null){
            mHttpControl=new HttpControl();
        }
        return mHttpControl;
    }

    /**
     * 初始化图片加载管理器
     */
    private void initImageLoader(Context context) {
        // 初始化图片默认display options
        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
                .displayer(new FadeInBitmapDisplayer(1000))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        // 初始化图片加载器
        ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(imageOptions)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(1024 * 1024)
                .memoryCacheSizePercentage(12)
                .memoryCacheExtraOptions(480, 800)
                .threadPoolSize(2)
                .diskCache(new UnlimitedDiskCache(StorageUtils.getOwnCacheDirectory(context, APP_CACHE_DIR)))
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(imageLoaderConfiguration);
    }

    private void initLocationManager() {
        LocationManager locationManager = new LocationManager(this);
        locationManager.startGetLocation();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // 内存不足，清空所有强引用图片
        ImageLoader.getInstance().clearMemoryCache();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ImageLoader.getInstance().destroy();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    
}
