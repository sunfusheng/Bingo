package com.sun.bingo.util.image;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import java.io.File;
import java.text.DecimalFormat;

/**
 * 工具类
 * 
 * Created by simba on 2015/7/2.
 */
public class CommonHelper {

    public static SpannableStringBuilder getSpannableStringBuilder(String content, int start,
            int length, int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        ForegroundColorSpan extra_color = new ForegroundColorSpan(color);
        builder.setSpan(extra_color, start, start + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    public static void startPhone(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        // intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }

    /**
     * 创建文件目录
     * 
     * @param filePath
     * @return
     */
    public static boolean mkFiledir(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return false;
    }

    public static String formetFileSize(long fileS) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }
}
