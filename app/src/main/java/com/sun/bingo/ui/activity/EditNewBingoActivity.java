package com.sun.bingo.ui.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;
import com.framework.dialog.TipDialog;
import com.framework.dialog.ToastTip;
import com.sun.bingo.BingoApp;
import com.sun.bingo.R;
import com.sun.bingo.control.NavigateManager;
import com.sun.bingo.control.SingleControl;
import com.sun.bingo.model.BingoEntity;
import com.sun.bingo.model.UserEntity;
import com.sun.bingo.model.eventbus.EventEntity;
import com.sun.bingo.model.eventbus.EventType;
import com.sun.bingo.ui.dialog.CommonDialog;
import com.sun.bingo.util.DateUtil;
import com.sun.bingo.util.KeyBoardUtil;
import com.sun.bingo.util.NetWorkUtil;
import com.sun.bingo.util.image.GetPathFromUri4kitkat;
import com.sun.bingo.widget.ActionSheet;
import com.sun.bingo.widget.UploadImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by sunfusheng on 15/7/18.
 */
public class EditNewBingoActivity extends BaseActivity<SingleControl> implements View.OnClickListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.et_website)
    EditText etWebsite;
    @Bind(R.id.cv_website)
    CardView cvWebsite;
    @Bind(R.id.et_describe)
    EditText etDescribe;
    @Bind(R.id.cv_describe)
    CardView cvDescribe;
    @Bind(R.id.ll_container)
    LinearLayout llContainer;
    @Bind(R.id.hs_images)
    HorizontalScrollView hsImages;
    @Bind(R.id.iv_image)
    ImageView ivImage;
    @Bind(R.id.tv_commit)
    TextView tvCommit;
    @Bind(R.id.rl_bottom_layout)
    RelativeLayout rlBottomLayout;

    private String takePicturePath;
    private BingoEntity bingoEntity;
    private List<UploadImageView> uploadImageViews;

    private boolean isShareDes = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_new_bingo);
        ButterKnife.bind(this);

        initData();
        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkClipboard();
    }

    private void initData() {
        bingoEntity = new BingoEntity();
        if (getIntent() != null) {
            if (getIntent().hasExtra("url")) {
                etWebsite.setText(getIntent().getStringExtra("url"));
            }
        }
    }

    @SuppressLint("NewApi")
    private void initView() {
        initToolBar(toolbar, true, "添加文章");
        hsImages.setVisibility(View.GONE);
        setRectShapeViewBackground(rlBottomLayout);
        setRoundRectShapeViewBackground(ivImage);
        setRoundRectShapeViewBackground(tvCommit);
    }

    private void initListener() {
        ivImage.setOnClickListener(this);
        tvCommit.setOnClickListener(this);
    }

    // 检查剪贴板上是否有分享的标题和描述
    private void checkClipboard() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = clipboardManager.getPrimaryClip();
        if (clipData == null) return ;
        ClipData.Item item = clipData.getItemAt(0);
        final String text = item.getText().toString();

        if (!TextUtils.isEmpty(text) && !text.startsWith("http") && isShareDes && !text.equals(getSettingsSharedPreferences().newBingoDes())) {
            TipDialog tipDialog = new TipDialog(this);
            tipDialog.show("将复制的内容粘贴到文章描述处", text, "立即粘贴", "暂不", new MaterialDialog.ButtonCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                    super.onPositive(dialog);
                    isShareDes = false;
                    etDescribe.setText(text);
                }

                @Override
                public void onNegative(MaterialDialog dialog) {
                    super.onNegative(dialog);
                    isShareDes = false;
                    getSettingsSharedPreferences().newBingoDes(text);
                }
            });
        }
    }

    private void commitNewBingo() {
        String website = etWebsite.getText().toString().trim();
        if (TextUtils.isEmpty(website)) {
            ToastTip.show(getString(R.string.hint_input_website));
            return;
        }

        if (!NetWorkUtil.isLinkAvailable(website)) {
            ToastTip.show(getString(R.string.hint_input_website_available));
            return ;
        }

        String describe = etDescribe.getText().toString().trim();
        if (TextUtils.isEmpty(describe)) {
            ToastTip.show(getString(R.string.hint_input_describe));
            return;
        }

        bingoEntity.setWebsite(website);
        bingoEntity.setDescribe(describe);
        bingoEntity.setCreateTime(DateUtil.getCurrentMillis());
        myEntity = BmobUser.getCurrentUser(this, UserEntity.class);
        bingoEntity.setUserId(myEntity.getObjectId());
        bingoEntity.setUserEntity(myEntity);

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
        getSettingsSharedPreferences().newBingoUrl(etWebsite.getText().toString());
        getSettingsSharedPreferences().newBingoDes(etDescribe.getText().toString());
        EventBus.getDefault().post(new EventEntity(EventType.EVENT_TYPE_UPDATE_BINGO_LIST));
        loadingDialog.dismiss();
        CommonDialog.showSuccessDialog(this, new CommonDialog.DismissListener() {
            @Override
            public void onDismiss() {
                finish();
            }
        });
    }

    private void resultFail() {
        loadingDialog.dismiss();
        ToastTip.show("提交失败，请重试");
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
                    ToastTip.show("最多上传9张图片哦");
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
                .setCancelTextViewTitle("取消")
                .setOtherTextViewTitles("拍照", "从相册选择")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                    }

                    @Override
                    public void onOtherTextViewClick(ActionSheet actionSheet, int index) {
                        switch (index) {
                            case 0:
                                takePicturePath = "/" + BingoApp.APP_CACHE_DIR + "/" + DateUtil.getCurrentMillis() + ".jpg";
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
            mImageManager.loadLocalImage(list.get(i), ivSelectedImage);
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
                    ToastTip.show("最多上传9张图片哦");
                } else {
                    KeyBoardUtil.hideKeyboard(EditNewBingoActivity.this);
                    showSelectImageDialog();
                }
            }
        });
        ivSelectedImage.setImageResource(R.drawable.ic_add_image);
        llContainer.addView(view);
    }

    private void handleCompressImageViewWithPath(String imagePath) {
        hsImages.setVisibility(View.VISIBLE);
        List<String> list = bingoEntity.getImageList();
        if (list == null || list.size() == 0) {
            list = new ArrayList<>();
        }
        list.add(imagePath);
        bingoEntity.setImageList(list);
        updateImageLayout();
    }

    private void setImageViewWithPath(String imagePath) {
        mControl.getCompressImagePath(this, imagePath); //异步压缩图片
    }

    public void getCompressImagePathCallBack() {
        String compressImagePath = mModel.get(1);
        if (TextUtils.isEmpty(compressImagePath)) {
            ToastTip.show("请重新选择图片");
        } else {
            handleCompressImageViewWithPath(compressImagePath);
        }
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
                    String imagePath = GetPathFromUri4kitkat.getPath(this, data.getData());
                    if (TextUtils.isEmpty(imagePath)) {
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
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
}
