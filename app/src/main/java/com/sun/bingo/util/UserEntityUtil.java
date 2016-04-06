package com.sun.bingo.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.sun.bingo.R;
import com.sun.bingo.control.manager.ImageManager;

/**
 * Created by sunfusheng on 15/7/29.
 */
public class UserEntityUtil {

    public static void setUserAvatarView(Context context, String url, ImageView imageView) {
        if (imageView == null) return;
        if (!TextUtils.isEmpty(url)) {
            ImageManager.getInstance().loadCircleImage(context, url, imageView);

        } else {
            ImageManager.getInstance().loadCircleResImage(context, R.drawable.ic_user, imageView);
        }
    }

    public static void setTextViewData(TextView textView, String text) {
        if (textView == null) return;
        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
        } else {
            textView.setText("NULL");
        }
    }
}
