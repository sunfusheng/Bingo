package com.sun.bingo.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.sun.bingo.R;
import com.sun.bingo.entity.AppEntity;
import com.sun.bingo.framework.dialog.ToastTip;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunfusheng on 15/8/17.
 */
public class ShareUtil {

    /**
     * 获得应用的信息
     * @param context
     * @return
     */
    public static AppEntity getAppEntity(Context context) {
        AppEntity entity = new AppEntity();
        PackageManager packageManager = context.getPackageManager();

        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);

            entity.setAppName(applicationInfo.loadLabel(packageManager).toString());
            entity.setPackageName(context.getPackageName());
            entity.setSrcPath(applicationInfo.publicSourceDir);
            entity.setAppIcon(applicationInfo.loadLogo(packageManager));

            entity.setVersionCode(packageInfo.versionCode);
            entity.setVersionName(packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return entity;
    }

    /**
     * 发送安装包
     * @param context
     */
    public static void sendToFriend(Context context) {
        AppEntity entity = getAppEntity(context);
        PackageManager pm = context.getPackageManager();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<>();
        for (int i = 0; i < resInfo.size(); i++) {
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            if(packageName.contains("tencent") || packageName.contains("blue")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(entity.getSrcPath())));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
            }
        }
        LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);
        Intent openInChooser = Intent.createChooser(intentList.remove(0), context.getString(R.string.transfer_apk_to_friend, entity.getAppName()));
        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        context.startActivity(openInChooser);
    }

    /**
     * 分享
     * @param uri 图片Uri
     */
    public static void share(Context context, Uri uri){
        Intent intent=new Intent(Intent.ACTION_SEND);
        if (uri != null){
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        }else {
            intent.setType("text/plain");
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share));
        intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_text));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)));
    }

    /**
     * 反馈
     * @param context
     */
    public static void feedback(Context context) {
        Uri uri = Uri.parse("mailto:sfsheng0322@126.com");
        final Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (infos == null || infos.size() <= 0){
            ToastTip.showToastDialog(context, context.getString(R.string.no_email_app_tip));
            return;
        }
        context.startActivity(intent);
    }

}
