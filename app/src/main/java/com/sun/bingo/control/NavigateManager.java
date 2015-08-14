package com.sun.bingo.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.sun.bingo.entity.BingoEntity;
import com.sun.bingo.ui.activity.BingoDetailActivity;
import com.sun.bingo.ui.activity.EditNewBingoActivity;
import com.sun.bingo.ui.activity.FavoriteActivity;
import com.sun.bingo.ui.activity.LoginActivity;
import com.sun.bingo.ui.activity.MainActivity;
import com.sun.bingo.ui.activity.ProfileActivity;
import com.sun.bingo.ui.activity.RichEditorActivity;

import java.io.File;

public class NavigateManager {

    public static final int TAKE_PICTURE_REQUEST_CODE = 7;
    public static final int CHOOSE_PICTURE_REQUEST_CODE = 23;
    public static final int PROFILE_REQUEST_CODE = 29;

    //拍照
    public static void gotoTakePicture(Activity activity, String takePicturePath) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), takePicturePath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, TAKE_PICTURE_REQUEST_CODE);
    }

    //从相册选择
    public static void gotoChoosePicture(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activity.startActivityForResult(intent, CHOOSE_PICTURE_REQUEST_CODE);
    }

    public static void gotoEditNewBingoActivity(Activity activity) {
        Intent intent = new Intent(activity, EditNewBingoActivity.class);
        activity.startActivity(intent);
    }

    public static void gotoProfileActivity(Activity activity) {
        Intent intent = new Intent(activity, ProfileActivity.class);
        activity.startActivityForResult(intent, PROFILE_REQUEST_CODE);
    }

    public static void gotoLoginActivity(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    public static void gotoMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    public static void gotoRichEditorActivity(Activity activity) {
        Intent intent = new Intent(activity, RichEditorActivity.class);
        activity.startActivity(intent);
    }

    public static void gotoBingoDetailActivity(Context context, BingoEntity entity) {
        Intent intent = new Intent(context, BingoDetailActivity.class);
        intent.putExtra("entity", entity);
        context.startActivity(intent);
    }

    public static void gotoFavoriteActivity(Activity activity) {
        Intent intent = new Intent(activity, FavoriteActivity.class);
        activity.startActivity(intent);
    }
}
