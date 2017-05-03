package com.sun.bingo.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
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
import com.sun.bingo.util.UrlParse.LinkSourceContent;
import com.sun.bingo.util.UrlParse.LinkViewCallback;
import com.sun.bingo.util.UrlParse.TextCrawler;
import com.sun.bingo.util.image.GetPathFromUri4kitkat;
import com.sun.bingo.widget.ActionSheet;
import com.sun.bingo.widget.UploadImageView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by sunfusheng on 15/7/18.
 */
public class AddBingoActivity extends BaseActivity<SingleControl> implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_url)
    EditText etUrl;
    @BindView(R.id.ll_url)
    LinearLayout llUrl;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.divider_title)
    View dividerTitle;
    @BindView(R.id.et_des)
    EditText etDes;
    @BindView(R.id.ll_des)
    LinearLayout llDes;
    @BindView(R.id.divider_des)
    View dividerDes;
    @BindView(R.id.tv_tag)
    TextView tvTag;
    @BindView(R.id.ll_tag)
    LinearLayout llTag;
    @BindView(R.id.divider_tag)
    View dividerTag;
    @BindView(R.id.iv_selected_image)
    UploadImageView ivSelectedImage;
    @BindView(R.id.iv_delete_image)
    ImageView ivDeleteImage;
    @BindView(R.id.ll_cover_image)
    LinearLayout llCoverImage;
    @BindView(R.id.divider_image)
    View dividerImage;
    @BindView(R.id.ll_bingo_info)
    LinearLayout llBingoInfo;
    @BindView(R.id.tv_from)
    TextView tvFrom;

    private String takePicturePath;
    private BingoEntity bingoEntity;

    private boolean isShareDes = true;
    private TextCrawler textCrawler;
    private LinkSourceContent linkContent;
    private MaterialDialog tagsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bingo);
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
        textCrawler = new TextCrawler();

        if (getIntent() != null) {
            if (getIntent().hasExtra("url")) {
                etUrl.setText(getIntent().getStringExtra("url"));
            }
        }
    }

    private void initView() {
        initToolBar(toolbar, true, "分享文章");

        llDes.setVisibility(View.GONE);
        dividerDes.setVisibility(View.GONE);

        llTag.setVisibility(View.GONE);
        dividerTag.setVisibility(View.GONE);

        llCoverImage.setVisibility(View.GONE);
        dividerImage.setVisibility(View.GONE);

        tvFrom.setVisibility(View.GONE);
        ivSelectedImage.setProgressFinish();
    }

    private void initListener() {
        tvTag.setOnClickListener(this);
        ivSelectedImage.setOnClickListener(this);
        ivDeleteImage.setOnClickListener(this);

        etUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    textCrawler.makePreview(linkViewCallback, s.toString());
                }
            }
        });
    }

    private LinkViewCallback linkViewCallback = new LinkViewCallback() {
        @Override
        public void onBeforeLoading() {
        }

        @Override
        public void onAfterLoading(LinkSourceContent linkSourceContent, boolean isNull) {
            if (linkSourceContent.isSuccess()) {
                linkContent = linkSourceContent;
                KeyBoardUtil.hideKeyboard(mActivity);

                llTag.setVisibility(View.VISIBLE);
                dividerTag.setVisibility(View.VISIBLE);

                llCoverImage.setVisibility(View.VISIBLE);
                dividerImage.setVisibility(View.VISIBLE);

                etTitle.setText(linkSourceContent.getTitle() + "");

                if (TextUtils.isEmpty(linkSourceContent.getDescription())) {
                    llDes.setVisibility(View.GONE);
                    dividerDes.setVisibility(View.GONE);
                } else {
                    StringBuilder des = new StringBuilder();
                    llDes.setVisibility(View.VISIBLE);
                    dividerDes.setVisibility(View.VISIBLE);
                    if (linkSourceContent.getDescription().length() > 60) {
                        des.append(linkSourceContent.getDescription().substring(0, 59)).append("...");
                    }
                    etDes.setText(des.toString());
                }

                if (TextUtils.isEmpty(linkSourceContent.getCannonicalUrl())) {
                    tvFrom.setVisibility(View.GONE);
                } else {
                    tvFrom.setVisibility(View.VISIBLE);
                    tvFrom.setText("来自 " + linkSourceContent.getCannonicalUrl());
                }
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_bingo, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_bingo:
                commitNewBingo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // 检查剪贴板上是否有分享的标题和描述
    private void checkClipboard() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = clipboardManager.getPrimaryClip();
        if (clipData == null) return;
        ClipData.Item item = clipData.getItemAt(0);
        final String text = item.getText().toString();

        if (!TextUtils.isEmpty(text) && !text.startsWith("http") && isShareDes && !text.equals(getSettingsSharedPreferences().newBingoDes())) {
            TipDialog tipDialog = new TipDialog(this);
            tipDialog.show("将复制的内容粘贴到文章描述处", text, "立即粘贴", "暂不", new MaterialDialog.ButtonCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                    super.onPositive(dialog);
                    isShareDes = false;
                    etDes.setText(text);
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
        String website = etUrl.getText().toString().trim();
        if (TextUtils.isEmpty(website)) {
            ToastTip.show(getString(R.string.hint_input_website));
            return;
        }

        if (!NetWorkUtil.isLinkAvailable(website)) {
            ToastTip.show(getString(R.string.hint_input_website_available));
            return;
        }

        String title = etTitle.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            ToastTip.show("请输入文章标题");
            return;
        }

        String describe = etDes.getText().toString().trim();
        if (TextUtils.isEmpty(describe)) {
            ToastTip.show(getString(R.string.hint_input_describe));
            return;
        }

        bingoEntity.setWebsite(website);
        bingoEntity.setTitle(title);
        bingoEntity.setDescribe(describe);
        bingoEntity.setCreateTime(DateUtil.getCurrentMillis());
        myEntity = BmobUser.getCurrentUser(this, UserEntity.class);
        bingoEntity.setUserId(myEntity.getObjectId());
        bingoEntity.setUserEntity(myEntity);
        bingoEntity.setFrom(linkContent != null? linkContent.getCannonicalUrl()+"":"");

        loadingDialog.show();
        if (!TextUtils.isEmpty(bingoEntity.getImage_cover())) {
            ivSelectedImage.setProgressStart();
            BmobProFile.getInstance(this).upload(bingoEntity.getImage_cover(), new UploadListener() {
                @Override
                public void onProgress(int i) {
                    ivSelectedImage.setProgress(i);
                }

                @Override
                public void onSuccess(String s, String s1, BmobFile bmobFile) {
                    ivSelectedImage.setProgressFinish();
                    bingoEntity.setImage_cover(bmobFile.getUrl());
                    bingoEntity.save(AddBingoActivity.this, new SaveListener() {
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

                @Override
                public void onError(int statuscode, String errormsg) {
                    resultFail();
                }
            });
        } else {
            bingoEntity.save(AddBingoActivity.this, new SaveListener() {
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
        getSettingsSharedPreferences().newBingoUrl(etUrl.getText().toString());
        getSettingsSharedPreferences().newBingoDes(etDes.getText().toString());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tag:
                selectBingoTag();
                break;
            case R.id.iv_selected_image:
                showSelectImageDialog();
                break;
            case R.id.iv_delete_image:

                break;
        }
    }

    private void selectBingoTag() {
        if (tagsDialog == null) {
            tagsDialog = new MaterialDialog.Builder(mContext)
                    .title("文章分类")
                    .items(R.array.bingo_tags)
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                            tvTag.setText(text.toString());
                            return true;
                        }
                    })
                    .build();
        }
        tagsDialog.show();
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
                                NavigateManager.gotoTakePicture(AddBingoActivity.this, takePicturePath);
                                break;
                            case 1:
                                NavigateManager.gotoChoosePicture(AddBingoActivity.this);
                                break;
                        }
                    }
                }).show();
    }

    private void setImageViewWithPath(String imagePath) {
        mControl.getCompressImagePath(this, imagePath); //异步压缩图片
    }

    public void getCompressImagePathCallBack() {
        String compressImagePath = mModel.get(1);
        if (TextUtils.isEmpty(compressImagePath)) {
            ToastTip.show("请重新选择图片");
        } else {
            bingoEntity.setImage_cover(compressImagePath);
            mImageManager.loadLocalImage(compressImagePath, ivSelectedImage);
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
