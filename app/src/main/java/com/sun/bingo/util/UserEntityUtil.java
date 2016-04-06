package com.sun.bingo.util;

import android.text.TextUtils;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sun.bingo.R;

/**
 * Created by sunfusheng on 15/7/29.
 */
public class UserEntityUtil {

    private static DisplayImageOptions userImageOptions;

    public static void setUserAvatarView(CircularImageView circularImageView, String path) {
        if (circularImageView == null) return;
        if (!TextUtils.isEmpty(path)) {
            if (userImageOptions == null) {
                userImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                        .showImageOnLoading(R.drawable.ic_user)
                        .showImageForEmptyUri(R.drawable.ic_user)
                        .showImageOnFail(R.drawable.ic_user)
                        .build();
            }
            ImageLoader.getInstance().displayImage(path, circularImageView, userImageOptions);
        } else {
            circularImageView.setImageResource(R.drawable.ic_user);
        }
    }

    public static void setTextViewData(TextView textView, String text) {
        if (textView == null) return;
        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
        } else {
            textView.setText("");
        }
    }
}
