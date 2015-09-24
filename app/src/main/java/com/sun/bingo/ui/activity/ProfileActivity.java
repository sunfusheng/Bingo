package com.sun.bingo.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;
import com.sun.bingo.BingoApplication;
import com.sun.bingo.R;
import com.sun.bingo.control.NavigateManager;
import com.sun.bingo.framework.eventbus.EventEntity;
import com.sun.bingo.framework.eventbus.EventType;
import com.sun.bingo.util.UserEntityUtil;
import com.sun.bingo.widget.ActionSheet;
import com.sun.bingo.widget.UploadAvatarView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import de.greenrobot.event.EventBus;

/**
 * Created by sunfusheng on 15/7/22.
 */
public class ProfileActivity extends BaseActivity implements View.OnClickListener {


    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.civ_user_avatar)
    UploadAvatarView civUserAvatar;
    @InjectView(R.id.rl_user_avatar)
    RelativeLayout rlUserAvatar;
    @InjectView(R.id.tv_user_sign_title)
    TextView tvUserSignTitle;
    @InjectView(R.id.tv_user_sign)
    TextView tvUserSign;
    @InjectView(R.id.rl_user_sign)
    RelativeLayout rlUserSign;
    @InjectView(R.id.tv_nick_name_title)
    TextView tvNickNameTitle;
    @InjectView(R.id.tv_nick_name)
    TextView tvNickName;
    @InjectView(R.id.rl_nick_name)
    RelativeLayout rlNickName;
    @InjectView(R.id.tv_logout)
    TextView tvLogout;

    private String takePicturePath = "/" + BingoApplication.APP_CACHE_DIR + "/avatar.jpg";
    private String imagePath;

    private final String NICK_NAME = "nick_name";
    private final String USER_SIGN = "user_sign";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.inject(this);

        initData();
        initView();
        initListener();
    }

    private void initData() {

    }

    private void initView() {
        initToolBar(toolbar, true, "个人中心");
        tvLogout.setTextColor(getColorPrimary());

        UserEntityUtil.setUserAvatarView(civUserAvatar, userEntity.getUserAvatar());
        UserEntityUtil.setTextViewData(tvNickName, userEntity.getNickName());
        UserEntityUtil.setTextViewData(tvUserSign, userEntity.getUserSign());
    }

    private void initListener() {
        rlUserAvatar.setOnClickListener(this);
        rlNickName.setOnClickListener(this);
        rlUserSign.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
    }

    public void showSelectAvatarDialog() {
        ActionSheet.createBuilder(this, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("拍照", "从相册选择")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        switch (index) {
                            case 0:
                                NavigateManager.gotoTakePicture(ProfileActivity.this, takePicturePath);
                                break;
                            case 1:
                                NavigateManager.gotoChoosePicture(ProfileActivity.this);
                                break;
                        }
                    }
                }).show();
    }

    private void setImageViewWithPath(ImageView imageView, String imagePath) {
        ImageLoader.getInstance().displayImage("file://" + imagePath, imageView, userImageOptions);
        uploadAvatar(imagePath);
    }

    private void uploadAvatar(String imagePath) {
        BmobProFile.getInstance(this).upload(imagePath, new UploadListener() {

            @Override
            public void onSuccess(String fileName, String url, BmobFile file) {
                Logger.i("fileName：" + fileName);
                Logger.i("url：" + url);
                Logger.i("file.getUrl()：" + file.getUrl());

                userEntity.setUserAvatar(file.getUrl());
                userEntity.update(ProfileActivity.this, userEntity.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        civUserAvatar.setProgressOver();
                        setResult(NavigateManager.PROFILE_REQUEST_CODE);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(ProfileActivity.this, "头像上传失败", Toast.LENGTH_SHORT).show();
                        civUserAvatar.setProgressOver();
                    }
                });
            }

            @Override
            public void onProgress(int progress) {
                civUserAvatar.setProgress(progress);
            }

            @Override
            public void onError(int statusCode, String errorMsg) {
                Toast.makeText(ProfileActivity.this, "文件上传失败", Toast.LENGTH_SHORT).show();
                civUserAvatar.setProgressOver();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case NavigateManager.TAKE_PICTURE_REQUEST_CODE:
                    imagePath = Environment.getExternalStorageDirectory() + takePicturePath;
                    setImageViewWithPath(civUserAvatar, imagePath);
                    break;
                case NavigateManager.CHOOSE_PICTURE_REQUEST_CODE:
                    Uri uri = data.getData();
                    if (uri != null) {
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                            setImageViewWithPath(civUserAvatar, imagePath);
                            cursor.close();
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_user_avatar:
                showSelectAvatarDialog();
                break;
            case R.id.rl_nick_name:
                updateNickName("昵称", NICK_NAME, userEntity.getNickName());
                break;
            case R.id.rl_user_sign:
                updateNickName("个性签名", USER_SIGN, userEntity.getUserSign());
                break;
            case R.id.tv_logout:
                logout();
                break;
        }
    }

    private void logout() {
        new MaterialDialog.Builder(this)
                .content("确认退出登录？")
                .contentColor(getResources().getColor(R.color.font_black_3))
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .negativeColor(getResources().getColor(R.color.font_black_3))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        BmobUser.logOut(ProfileActivity.this);
                        NavigateManager.gotoMainActivity(ProfileActivity.this);
                        finish();
                    }
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                    }
                })
                .show();
    }

    private View view;
    private EditText editText;
    private String inputText;

    private void updateNickName(final String title, final String type, final String content) {
        MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                .title(title)
                .customView(R.layout.material_dialog_input_layout, true)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .negativeColor(getResources().getColor(R.color.font_black_3))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        inputText = editText.getText().toString().trim();
                        if (!TextUtils.isEmpty(inputText)) {
                            switch (type) {
                                case NICK_NAME:
                                    userEntity.setNickName(inputText);
                                    break;
                                case USER_SIGN:
                                    userEntity.setUserSign(inputText);
                                    break;
                            }

                            userEntity.update(ProfileActivity.this, userEntity.getObjectId(), new UpdateListener() {
                                @Override
                                public void onSuccess() {
                                    EventBus.getDefault().post(new EventEntity(EventType.UPDATE_BINGO_LIST));
                                    setResult(NavigateManager.PROFILE_REQUEST_CODE);
                                    switch (type) {
                                        case NICK_NAME:
                                            tvNickName.setText(inputText);
                                            break;
                                        case USER_SIGN:
                                            tvUserSign.setText(inputText);
                                            break;
                                    }
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Toast.makeText(ProfileActivity.this, "修改" + title + "失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).build();

        view = materialDialog.getCustomView();
        editText = ButterKnife.findById(view, R.id.et_dialog_input);
        if (!TextUtils.isEmpty(content)) {
            editText.setText(content);
            editText.setSelection(content.length());
        } else {
            editText.setHint("请输入" + title);
        }
        materialDialog.show();
    }
}
