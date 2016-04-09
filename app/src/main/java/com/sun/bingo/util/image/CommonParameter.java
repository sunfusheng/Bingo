package com.sun.bingo.util.image;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import java.io.File;

public class CommonParameter {

    public static String TID;
    //纬度
    public static double sLat;
    //经度
    public static double sLng;
    public static String sLocation;
    /**
     * 城市简写，去掉了结尾字符市
     */
    public static String sCity;
    /**
     * 城市全称，包含结尾字符市
     */
    public static String sCityFull;

    private static int sScreenWidth;
    private static int sScreenHeight;

    public static int getScreenWidth(Context context) {
        if (sScreenWidth == 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            sScreenWidth = wm.getDefaultDisplay().getWidth();
            sScreenHeight = wm.getDefaultDisplay().getHeight();
        }
        return sScreenWidth;
    }

    public static int getScreenHeight(Context context) {
        if (sScreenHeight == 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            sScreenWidth = wm.getDefaultDisplay().getWidth();
            sScreenHeight = wm.getDefaultDisplay().getHeight();
        }
        return sScreenHeight;
    }

    private static String sCACHE_FILE_PATH;

    private static final String sCACHE_FILE_CAMERA = "camera";
    private static final String sCACHE_FILE_THUMBNAIL = "thumbnail";

    // public static final String sCACHE_FILE_ARM = sCACHE_FILE_PATH + "arm";

    /**
     * 相机缓存文件目录
     *
     * @param context
     * @return
     */
    public static String getCameraCacheFile(Context context) {
        if (TextUtils.isEmpty(sCACHE_FILE_PATH)) {
            Log.i("dddd", getCacheFilePath(context));
            sCACHE_FILE_PATH = getCacheFilePath(context);
        }
        String str = sCACHE_FILE_PATH + File.separatorChar + sCACHE_FILE_CAMERA;

        Log.i("dddd", str);
        CommonHelper.mkFiledir(str);
        return str;
    }

    /**
     * 缩略图缓存文件目录
     *
     * @param context
     * @return
     */
    public static String getThumbnailCacheFile(Context context) {
        if (TextUtils.isEmpty(sCACHE_FILE_PATH)) {
            sCACHE_FILE_PATH = getCacheFilePath(context);
        }
        String str = sCACHE_FILE_PATH + File.separatorChar + sCACHE_FILE_THUMBNAIL;
        CommonHelper.mkFiledir(str);
        return str;
    }

    private static String getCacheFilePath(Context context) {
        String sCACHE_FILE_PATH = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String parentPath =
                    Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                            + "Android" + File.separator + "data" + File.separator
                            + context.getPackageName() + File.separator + "cache";
            // CommonHelper.mkFiledir(parentPath);
            sCACHE_FILE_PATH = parentPath + File.separator + "house";
        } else {
            sCACHE_FILE_PATH = context.getCacheDir().getAbsolutePath() + File.separator + "house";
        }
        CommonHelper.mkFiledir(sCACHE_FILE_PATH);
        return sCACHE_FILE_PATH;
    }

}
