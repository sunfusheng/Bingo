package com.sun.bingo.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sun.bingo.BingoApplication;
import com.sun.bingo.R;
import com.sun.bingo.control.NavigateManager;
import com.sun.bingo.entity.BingoEntity;
import com.sun.bingo.entity.UserEntity;
import com.sun.bingo.framework.dialog.ToastTip;
import com.sun.bingo.framework.eventbus.EventEntity;
import com.sun.bingo.framework.eventbus.EventType;
import com.sun.bingo.util.DateUtil;
import com.sun.bingo.util.KeyBoardUtil;
import com.sun.bingo.util.theme.Selector;
import com.sun.bingo.widget.ActionSheet;
import com.sun.bingo.widget.UploadImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import de.greenrobot.event.EventBus;

/**
 * Created by sunfusheng on 15/7/18.
 */
public class EditNewBingoActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.et_website)
    EditText etWebsite;
    @InjectView(R.id.cv_website)
    CardView cvWebsite;
    @InjectView(R.id.et_describe)
    EditText etDescribe;
    @InjectView(R.id.cv_describe)
    CardView cvDescribe;
    @InjectView(R.id.ll_container)
    LinearLayout llContainer;
    @InjectView(R.id.hs_images)
    HorizontalScrollView hsImages;
    @InjectView(R.id.iv_image)
    ImageView ivImage;
    @InjectView(R.id.tv_commit)
    TextView tvCommit;
    @InjectView(R.id.rl_bottom_layout)
    RelativeLayout rlBottomLayout;

    private String takePicturePath;
    private BingoEntity bingoEntity;
    private List<UploadImageView> uploadImageViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_new_bingo);
        ButterKnife.inject(this);

        initData();
        initView();
        initListener();
    }

    private void initData() {
        bingoEntity = new BingoEntity();
    }

    @SuppressLint("NewApi")
    private void initView() {
        initToolBar(toolbar, true, "新Bingo");
        hsImages.setVisibility(View.GONE);
        rlBottomLayout.setBackgroundColor(getColorPrimary());
        ivImage.setBackground(Selector.createRoundRectShapeSelector(getColorPrimary()));
        tvCommit.setBackground(Selector.createRoundRectShapeSelector(getColorPrimary()));
    }

    private void initListener() {
        ivImage.setOnClickListener(this);
        tvCommit.setOnClickListener(this);
    }

    private void commitNewBingo() {
        String website = etWebsite.getText().toString().trim();
        if (TextUtils.isEmpty(website)) {
            ToastTip.showToastDialog(this, getString(R.string.hint_input_website));
            return;
        }

        String describe = etDescribe.getText().toString().trim();
        if (TextUtils.isEmpty(describe)) {
            ToastTip.showToastDialog(this, getString(R.string.hint_input_describe));
            return;
        }

        bingoEntity.setWebsite(website);
        bingoEntity.setDescribe(describe);
        bingoEntity.setCreateTime(DateUtil.getCurrentMillis());
        userEntity = BmobUser.getCurrentUser(this, UserEntity.class);
        bingoEntity.setUserId(userEntity.getObjectId());
        bingoEntity.setUserEntity(userEntity);

        loadingDialog.show();
        if (bingoEntity.getImageList() != null && bingoEntity.getImageList().size() > 0) {
            List<String> list = bingoEntity.getImageList();
            int size = list.size();
            BmobProFile.getInstance(this).uploadBatch(list.toArray(new String[size]), new UploadBatchListener() {
                @Override
                public void onSuccess(boolean isFinish, String[] fileNames, String[] urls, BmobFile[] files) {
                    if (isFinish) {
                        for (UploadImageView item : uploadImageViews) {
                            item.setProgressFinish();
                        }
                        bingoEntity.setImageList(getBmobUrls(files));
                        bingoEntity.save(EditNewBingoActivity.this, new SaveListener() {
                            @Override
                            public void onSuccess() {
                                resultSuccess();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                resultFail();
                            }
                        });
                    }
                }

                @Override
                public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                    uploadImageViews.get(curIndex - 1).setProgress(curPercent);
                }

                @Override
                public void onError(int statuscode, String errormsg) {
                    resultFail();
                }
            });
        } else {
            bingoEntity.save(EditNewBingoActivity.this, new SaveListener() {
                @Override
                public void onSuccess() {
                    resultSuccess();
                }

                @Override
                public void onFailure(int i, String s) {
                    resultFail();
                }
            });
        }
    }

    private void resultSuccess() {
        EventBus.getDefault().post(new EventEntity(EventType.UPDATE_BINGO_LIST));
        loadingDialog.dismiss();
        finish();
    }

    private void resultFail() {
        loadingDialog.dismiss();
        ToastTip.showToastDialog(EditNewBingoActivity.this, "提交失败，请重试");
    }

    private List<String> getBmobUrls(BmobFile[] files) {
        List<String> list = new ArrayList<>();
        for (BmobFile item : files) {
            list.add(item.getUrl());
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_image:
                if (bingoEntity.getImageList() != null && bingoEntity.getImageList().size() >= 9) {
                    ToastTip.showToastDialog(EditNewBingoActivity.this, "最多上传9张图片哦");
                } else {
                    KeyBoardUtil.hideKeyboard(this);
                    showSelectImageDialog();
                }
                break;
            case R.id.tv_commit:
                commitNewBingo();
                break;
        }
    }

    public void showSelectImageDialog() {
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
                                takePicturePath = "/" + BingoApplication.APP_CACHE_DIR + "/" + DateUtil.getCurrentMillis() + ".jpg";
                                NavigateManager.gotoTakePicture(EditNewBingoActivity.this, takePicturePath);
                                break;
                            case 1:
                                NavigateManager.gotoChoosePicture(EditNewBingoActivity.this);
                                break;
                        }
                    }
                }).show();
    }

    private void updateImageLayout() {
        llContainer.removeAllViews();
        uploadImageViews = new ArrayList<>();
        List<String> list = bingoEntity.getImageList();
        int size = list.size();

        for (int i = 0; i < size; i++) {
            final int position = i;
            View view = LayoutInflater.from(this).inflate(R.layout.item_image_pick_layout, null);
            UploadImageView ivSelectedImage = ButterKnife.findById(view, R.id.iv_selected_image);
            uploadImageViews.add(ivSelectedImage);
            ImageView ivDeleteImage = ButterKnife.findById(view, R.id.iv_delete_image);
            ivDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bingoEntity.getImageList().remove(position);
                    updateImageLayout();
                    if (bingoEntity.getImageList() == null || bingoEntity.getImageList().size() == 0) {
                        hsImages.setVisibility(View.GONE);
                    }
                }
            });
            ImageLoader.getInstance().displayImage("file://" + list.get(i), ivSelectedImage, userImageOptions);
            llContainer.addView(view);
        }

        View view = LayoutInflater.from(this).inflate(R.layout.item_image_pick_layout, null);
        UploadImageView ivSelectedImage = ButterKnife.findById(view, R.id.iv_selected_image);
        ImageView ivDeleteImage = ButterKnife.findById(view, R.id.iv_delete_image);
        ivSelectedImage.setProgressFinish();
        ivDeleteImage.setVisibility(View.INVISIBLE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bingoEntity.getImageList() != null && bingoEntity.getImageList().size() >= 9) {
                    ToastTip.showToastDialog(EditNewBingoActivity.this, "最多上传9张图片哦");
                } else {
                    KeyBoardUtil.hideKeyboard(EditNewBingoActivity.this);
                    showSelectImageDialog();
                }
            }
        });
        ivSelectedImage.setImageResource(R.drawable.ic_add_image);
        llContainer.addView(view);
    }

    private void setImageViewWithPath(String imagePath) {
        List<String> list = bingoEntity.getImageList();
        if (list == null || list.size() == 0) {
            list = new ArrayList<>();
        }
        hsImages.setVisibility(View.VISIBLE);
        list.add(imagePath);
        bingoEntity.setImageList(list);
        updateImageLayout();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case NavigateManager.TAKE_PICTURE_REQUEST_CODE:
                    setImageViewWithPath(Environment.getExternalStorageDirectory() + takePicturePath);
                    break;
                case NavigateManager.CHOOSE_PICTURE_REQUEST_CODE:
                    Uri uri = data.getData();
                    if (uri != null) {
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            setImageViewWithPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                            cursor.close();
                        }
                    }
                    break;
            }
        }
    }
}
