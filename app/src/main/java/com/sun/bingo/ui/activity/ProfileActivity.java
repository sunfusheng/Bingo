package com.sun.bingo.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.framework.dialog.ToastTip;
import com.orhanobut.logger.Logger;
import com.sun.bingo.BingoApp;
import com.sun.bingo.R;
import com.sun.bingo.control.NavigateManager;
import com.sun.bingo.control.SingleControl;
import com.sun.bingo.model.eventbus.EventEntity;
import com.sun.bingo.model.eventbus.EventType;
import com.sun.bingo.util.UserEntityUtil;
import com.sun.bingo.util.image.GetPathFromUri4kitkat;
import com.sun.bingo.widget.ActionSheet;
import com.sun.bingo.widget.UploadAvatarView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by sunfusheng on 15/7/22.
 */
public class ProfileActivity extends BaseActivity<SingleControl> implements View.OnClickListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.iv_user_avatar)
    UploadAvatarView civUserAvatar;
    @Bind(R.id.rl_user_avatar)
    RelativeLayout rlUserAvatar;
    @Bind(R.id.tv_nick_name_title)
    TextView tvNickNameTitle;
    @Bind(R.id.tv_nick_name)
    TextView tvNickName;
    @Bind(R.id.rl_nick_name)
    RelativeLayout rlNickName;
    @Bind(R.id.tv_user_sign_title)
    TextView tvUserSignTitle;
    @Bind(R.id.tv_user_sign)
    TextView tvUserSign;
    @Bind(R.id.rl_user_sign)
    RelativeLayout rlUserSign;

    private String takePicturePath = "/" + BingoApp.APP_CACHE_DIR + "/avatar.jpg";
    private String imagePath;

    private final String NICK_NAME = "nick_name";
    private final String USER_SIGN = "user_sign";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        initData();
        initView();
        initListener();
    }

    private void initData() {

    }

    private void initView() {
        initToolBar(toolbar, true, "个人中心");

        UserEntityUtil.setUserAvatarView(mContext, myEntity.getUserAvatar(), civUserAvatar);
        UserEntityUtil.setTextViewData(tvNickName, myEntity.getNickName());
        UserEntityUtil.setTextViewData(tvUserSign, myEntity.getUserSign());
    }

    private void initListener() {
        rlUserAvatar.setOnClickListener(this);
        rlNickName.setOnClickListener(this);
        rlUserSign.setOnClickListener(this);
    }

    public void showSelectAvatarDialog() {
        ActionSheet.Builder builder = ActionSheet.createBuilder(mContext, getSupportFragmentManager());
        builder.setOtherButtonTitles("拍照", "从相册选择");
        builder.setCancelButtonTitle("取消");
        builder.setCancelableOnTouchOutside(true);
        builder.setListener(new ActionSheet.ActionSheetListener() {
            @Override
            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {}

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
        });
        builder.show();
    }


    private void setImageViewWithPath(String imagePath) {
        mImageManager.loadCircleLocalImage(imagePath, civUserAvatar);
        mControl.getCompressImagePath(this, imagePath); //异步压缩图片
    }

    public void getCompressImagePathCallBack() {
        String compressImagePath = mModel.get(1);
        if (TextUtils.isEmpty(compressImagePath)) {
            ToastTip.show("请重新选择图片");
        } else {
            uploadAvatar(compressImagePath);
        }
    }

    private void uploadAvatar(String imagePath) {
        BmobProFile.getInstance(this).upload(imagePath, new UploadListener() {

            @Override
            public void onSuccess(String fileName, String url, BmobFile file) {
                Logger.i("fileName：" + fileName);
                Logger.i("url：" + url);
                Logger.i("file.getUrl()：" + file.getUrl());

                myEntity.setUserAvatar(file.getUrl());
                myEntity.update(ProfileActivity.this, myEntity.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        EventBus.getDefault().post(new EventEntity(EventType.EVENT_TYPE_UPDATE_BINGO_LIST));
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
                    setImageViewWithPath(imagePath);
                    break;
                case NavigateManager.CHOOSE_PICTURE_REQUEST_CODE:
                    imagePath = GetPathFromUri4kitkat.getPath(this, data.getData());
                    if (TextUtils.isEmpty(imagePath)) {
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(data.getData(), proj, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                            cursor.close();
                        }
                    }
                    setImageViewWithPath(imagePath);
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
                updateNickName("昵称", NICK_NAME, myEntity.getNickName());
                break;
            case R.id.rl_user_sign:
                updateNickName("个性签名", USER_SIGN, myEntity.getUserSign());
                break;
        }
    }

    private void gotoMain() {
        if (TextUtils.isEmpty(myEntity.getUserAvatar())) {
            ToastTip.show("请上传您的靓照哦");
            return;
        }
        if (TextUtils.isEmpty(myEntity.getNickName())) {
            ToastTip.show("给自己起个昵称吧");
            return;
        }
        if (TextUtils.isEmpty(myEntity.getUserSign())) {
            ToastTip.show("来句个性说说吧");
            return;
        }
        NavigateManager.gotoMainActivity(this);
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
                                    myEntity.setNickName(inputText);
                                    break;
                                case USER_SIGN:
                                    myEntity.setUserSign(inputText);
                                    break;
                            }

                            myEntity.update(ProfileActivity.this, myEntity.getObjectId(), new UpdateListener() {
                                @Override
                                public void onSuccess() {
                                    EventBus.getDefault().post(new EventEntity(EventType.EVENT_TYPE_UPDATE_BINGO_LIST));
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
