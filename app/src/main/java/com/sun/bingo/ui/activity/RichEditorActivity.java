package com.sun.bingo.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.sun.bingo.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by sunfusheng on 15/7/29.
 */
public class RichEditorActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.action_undo)
    ImageButton actionUndo;
    @InjectView(R.id.action_redo)
    ImageButton actionRedo;
    @InjectView(R.id.action_bold)
    ImageButton actionBold;
    @InjectView(R.id.action_italic)
    ImageButton actionItalic;
    @InjectView(R.id.action_strikethrough)
    ImageButton actionStrikethrough;
    @InjectView(R.id.action_underline)
    ImageButton actionUnderline;
    @InjectView(R.id.action_heading1)
    ImageButton actionHeading1;
    @InjectView(R.id.action_heading2)
    ImageButton actionHeading2;
    @InjectView(R.id.action_heading3)
    ImageButton actionHeading3;
    @InjectView(R.id.action_heading4)
    ImageButton actionHeading4;
    @InjectView(R.id.action_txt_color)
    ImageButton actionTxtColor;
    @InjectView(R.id.action_bg_color)
    ImageButton actionBgColor;
    @InjectView(R.id.action_indent)
    ImageButton actionIndent;
    @InjectView(R.id.action_outdent)
    ImageButton actionOutdent;
    @InjectView(R.id.action_align_left)
    ImageButton actionAlignLeft;
    @InjectView(R.id.action_align_center)
    ImageButton actionAlignCenter;
    @InjectView(R.id.action_align_right)
    ImageButton actionAlignRight;
    @InjectView(R.id.action_blockquote)
    ImageButton actionBlockquote;
    @InjectView(R.id.action_insert_image)
    ImageButton actionInsertImage;
    @InjectView(R.id.action_insert_link)
    ImageButton actionInsertLink;
    @InjectView(R.id.rich_editor)
    RichEditor richEditor;

    private boolean isTxtColorChanged = false;
    private boolean isBgColorChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rich_editor);
        ButterKnife.inject(this);

        initData();
        initView();
        initListener();
    }

    private void initData() {
    }

    private void initView() {
        initToolBar(toolbar, true, "富媒体编辑");
    }

    private void initListener() {
        richEditor.setEditorHeight(100);
        richEditor.setPlaceholder("开始编辑...");

        actionUndo.setOnClickListener(this);
        actionRedo.setOnClickListener(this);
        actionBold.setOnClickListener(this);
        actionItalic.setOnClickListener(this);
        actionStrikethrough.setOnClickListener(this);
        actionUnderline.setOnClickListener(this);
        actionHeading1.setOnClickListener(this);
        actionHeading2.setOnClickListener(this);
        actionHeading3.setOnClickListener(this);
        actionHeading4.setOnClickListener(this);
        actionTxtColor.setOnClickListener(this);
        actionBgColor.setOnClickListener(this);
        actionIndent.setOnClickListener(this);
        actionOutdent.setOnClickListener(this);
        actionAlignLeft.setOnClickListener(this);
        actionAlignCenter.setOnClickListener(this);
        actionAlignRight.setOnClickListener(this);
        actionBlockquote.setOnClickListener(this);
        actionInsertImage.setOnClickListener(this);
        actionInsertLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_undo:
                richEditor.undo();
                break;
            case R.id.action_redo:
                richEditor.redo();
                break;
            case R.id.action_bold:
                richEditor.setBold();
                break;
            case R.id.action_italic:
                richEditor.setItalic();
                break;
            case R.id.action_strikethrough:
                richEditor.setStrikeThrough();
                break;
            case R.id.action_underline:
                richEditor.setUnderline();
                break;
            case R.id.action_heading1:
                richEditor.setHeading(1);
                break;
            case R.id.action_heading2:
                richEditor.setHeading(2);
                break;
            case R.id.action_heading3:
                richEditor.setHeading(3);
                break;
            case R.id.action_heading4:
                richEditor.setHeading(4);
                break;
            case R.id.action_txt_color:
                richEditor.setTextColor(isTxtColorChanged ? Color.BLACK : Color.RED);
                isTxtColorChanged = !isTxtColorChanged;
                break;
            case R.id.action_bg_color:
                richEditor.setTextBackgroundColor(isBgColorChanged ? Color.TRANSPARENT : Color.YELLOW);
                isBgColorChanged = !isBgColorChanged;
                break;
            case R.id.action_indent:
                richEditor.setIndent();
                break;
            case R.id.action_outdent:
                richEditor.setOutdent();
                break;
            case R.id.action_align_left:
                richEditor.setAlignLeft();
                break;
            case R.id.action_align_center:
                richEditor.setAlignCenter();
                break;

            case R.id.action_align_right:
                richEditor.setAlignRight();
                break;
            case R.id.action_blockquote:
                richEditor.setBlockquote();
                break;
            case R.id.action_insert_image:
                richEditor.insertImage("http://g.hiphotos.baidu.com/image/w%3D230/sign=d88670b3ab773912c4268262c8188675/09fa513d269759ee13e856c5b0fb43166d22df07.jpg",
                        "刘亦菲");
                break;
            case R.id.action_insert_link:
                richEditor.insertLink("http://sfsheng0322.github.io/", "孙福生GitHub");
                break;
        }
    }
}
