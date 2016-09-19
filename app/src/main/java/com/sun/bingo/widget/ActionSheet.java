package com.sun.bingo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sun.bingo.R;
import com.sun.bingo.util.NavigationBarUtil;

public class ActionSheet extends Fragment implements OnClickListener {

    private static final String ARG_CANCEL_VIEW_TITLE = "cancel_view_title";
    private static final String ARG_OTHER_VIEW_TITLES = "other_view_titles";
    private static final String ARG_CANCELABLE_ONTOUCHOUTSIDE = "cancelable_ontouchoutside";
    private static final int CANCEL_VIEW_ID = 100;
    private static final int BG_VIEW_ID = 10;
    private static final int TRANSLATE_DURATION = 200;
    private static final int ALPHA_DURATION = 300;

    private ViewGroup mDecorView;
    private View mRootView;
    private View mBg;
    private LinearLayout mPanel;
    private Attributes mAttrs;
    private int navigationBarHeight = 0; // 导航栏高度

    private boolean isCancel = true;
    private boolean mDismissed = true;
    private static boolean hasTitle = false;
    private ActionSheetListener mListener;

    public void show(FragmentManager manager, String tag) {
        if (!mDismissed) {
            return;
        }
        mDismissed = false;
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void dismiss() {
        if (mDismissed) {
            return;
        }
        mDismissed = true;
        getFragmentManager().popBackStack();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(this);
        ft.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            View focusView = getActivity().getCurrentFocus();
            if (focusView != null) {
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }

        mDecorView = (ViewGroup) getActivity().getWindow().getDecorView();
        mRootView = createView();
        mAttrs = readAttribute();
        createItems();
        mDecorView.addView(mRootView);

        mBg.startAnimation(createAlphaInAnimation());
        mPanel.startAnimation(createTranslationInAnimation());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private View createView() {
        mBg = new View(getActivity());
        mBg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mBg.setBackgroundColor(Color.argb(136, 0, 0, 0));
        mBg.setId(BG_VIEW_ID);
        mBg.setOnClickListener(this);

        mPanel = new LinearLayout(getActivity());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;

        mPanel.setLayoutParams(params);
        mPanel.setOrientation(LinearLayout.VERTICAL);

        if (NavigationBarUtil.hasNavigationBar(getActivity())) {
            navigationBarHeight = NavigationBarUtil.getNavigationBarHeight(getActivity());
        }

        FrameLayout parent = new FrameLayout(getActivity());
        FrameLayout.LayoutParams parentParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        parentParams.bottomMargin = navigationBarHeight;
        parent.setLayoutParams(parentParams);
        parent.addView(mBg);
        parent.addView(mPanel);
        return parent;
    }

    private void createItems() {
        CharSequence[] titles = getOtherTextViewTitles();
        if (titles != null) {
            for (int i = 0; i < titles.length; i++) {
                TextView bt = new TextView(getActivity());
                bt.setId(CANCEL_VIEW_ID + i + 1);
                if (hasTitle && i == 0) {
                    bt.setTextColor(getResources().getColor(R.color.font_black_4));
                    bt.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttrs.actionSheetTextSize * 0.8f);
                    bt.setPadding(40, 40, 40, 40);
                } else {
                    bt.setOnClickListener(this);
                    bt.setTextColor(mAttrs.otherTextViewTextColor);
                    bt.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttrs.actionSheetTextSize);
                }
                bt.setBackgroundDrawable(getOtherTextViewBg(titles, i));
                bt.setText(titles[i]);
                bt.setGravity(Gravity.CENTER);
                if (i > 0) {
                    LinearLayout.LayoutParams params = createTextViewLayoutParams();
                    params.topMargin = mAttrs.otherTextViewSpacing;
                    mPanel.addView(bt, params);
                } else {
                    mPanel.addView(bt);
                }
            }
        }
        TextView bt = new TextView(getActivity());
        bt.getPaint().setFakeBoldText(true);
        bt.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttrs.actionSheetTextSize);
        bt.setId(CANCEL_VIEW_ID);
        bt.setBackgroundDrawable(mAttrs.cancelTextViewBackground);
        bt.setText(getCancelTextViewTitle());
        bt.setGravity(Gravity.CENTER);
        bt.setTextColor(mAttrs.cancelTextViewTextColor);
        bt.setOnClickListener(this);
        LinearLayout.LayoutParams params = createTextViewLayoutParams();
        params.topMargin = mAttrs.cancelTextViewMarginTop;
        mPanel.addView(bt, params);

        mPanel.setBackgroundDrawable(mAttrs.background);
        mPanel.setPadding(mAttrs.padding, mAttrs.padding, mAttrs.padding, mAttrs.padding);
    }

    private Animation createTranslationInAnimation() {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type, 1, type, 0);
        an.setDuration(TRANSLATE_DURATION);
        return an;
    }

    private Animation createAlphaInAnimation() {
        AlphaAnimation an = new AlphaAnimation(0, 1);
        an.setDuration(ALPHA_DURATION);
        return an;
    }

    private Animation createTranslationOutAnimation() {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type, 0, type, 1);
        an.setDuration(TRANSLATE_DURATION);
        an.setFillAfter(true);
        return an;
    }

    private Animation createAlphaOutAnimation() {
        AlphaAnimation an = new AlphaAnimation(1, 0);
        an.setDuration(ALPHA_DURATION);
        an.setFillAfter(true);
        return an;
    }

    public LinearLayout.LayoutParams createTextViewLayoutParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        return params;
    }

    private Drawable getOtherTextViewBg(CharSequence[] titles, int i) {
        if (titles.length == 1) {
            return mAttrs.otherTextViewSingleBackground;
        }
        if (titles.length == 2) {
            switch (i) {
                case 0:
                    return mAttrs.otherTextViewTopBackground;
                case 1:
                    return mAttrs.otherTextViewBottomBackground;
            }
        }
        if (titles.length > 2) {
            if (i == 0) {
                return mAttrs.otherTextViewTopBackground;
            }
            if (i == (titles.length - 1)) {
                return mAttrs.otherTextViewBottomBackground;
            }
            return mAttrs.getOtherTextViewMiddleBackground();
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        mPanel.startAnimation(createTranslationOutAnimation());
        mBg.startAnimation(createAlphaOutAnimation());
        mRootView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDecorView.removeView(mRootView);
            }
        }, ALPHA_DURATION);
        if (mListener != null) {
            mListener.onDismiss(this, isCancel);
        }
        super.onDestroyView();
    }

    private Attributes readAttribute() {
        Attributes attrs = new Attributes(getActivity());
        TypedArray a = getActivity().getTheme().obtainStyledAttributes(null,
                R.styleable.ActionSheet, R.attr.actionSheetStyle, 0);
        Drawable background = a
                .getDrawable(R.styleable.ActionSheet_actionSheetBackground);
        if (background != null) {
            attrs.background = background;
        }
        Drawable cancelTextViewBackground = a
                .getDrawable(R.styleable.ActionSheet_cancelTextViewBackground);
        if (cancelTextViewBackground != null) {
            attrs.cancelTextViewBackground = cancelTextViewBackground;
        }
        Drawable otherTextViewTopBackground = a
                .getDrawable(R.styleable.ActionSheet_otherTextViewTopBackground);
        if (otherTextViewTopBackground != null) {
            attrs.otherTextViewTopBackground = otherTextViewTopBackground;
        }
        Drawable otherTextViewMiddleBackground = a
                .getDrawable(R.styleable.ActionSheet_otherTextViewMiddleBackground);
        if (otherTextViewMiddleBackground != null) {
            attrs.otherTextViewMiddleBackground = otherTextViewMiddleBackground;
        }
        Drawable otherTextViewBottomBackground = a
                .getDrawable(R.styleable.ActionSheet_otherTextViewBottomBackground);
        if (otherTextViewBottomBackground != null) {
            attrs.otherTextViewBottomBackground = otherTextViewBottomBackground;
        }
        Drawable otherTextViewSingleBackground = a
                .getDrawable(R.styleable.ActionSheet_otherTextViewSingleBackground);
        if (otherTextViewSingleBackground != null) {
            attrs.otherTextViewSingleBackground = otherTextViewSingleBackground;
        }
        attrs.cancelTextViewTextColor = a.getColor(
                R.styleable.ActionSheet_cancelTextViewTextColor,
                attrs.cancelTextViewTextColor);
        attrs.otherTextViewTextColor = a.getColor(
                R.styleable.ActionSheet_otherTextViewTextColor,
                attrs.otherTextViewTextColor);
        attrs.padding = (int) a.getDimension(
                R.styleable.ActionSheet_actionSheetPadding, attrs.padding);
        attrs.otherTextViewSpacing = (int) a.getDimension(
                R.styleable.ActionSheet_otherTextViewSpacing,
                attrs.otherTextViewSpacing);
        attrs.cancelTextViewMarginTop = (int) a.getDimension(
                R.styleable.ActionSheet_cancelTextViewMarginTop,
                attrs.cancelTextViewMarginTop);
        attrs.actionSheetTextSize = a.getDimensionPixelSize(R.styleable.ActionSheet_actionSheetTextSize, (int) attrs.actionSheetTextSize);

        a.recycle();
        return attrs;
    }

    private CharSequence getCancelTextViewTitle() {
        return getArguments().getCharSequence(ARG_CANCEL_VIEW_TITLE);
    }

    private CharSequence[] getOtherTextViewTitles() {
        return getArguments().getCharSequenceArray(ARG_OTHER_VIEW_TITLES);
    }

    private boolean getCancelableOnTouchOutside() {
        return getArguments().getBoolean(ARG_CANCELABLE_ONTOUCHOUTSIDE);
    }

    public void setActionSheetListener(ActionSheetListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == BG_VIEW_ID && !getCancelableOnTouchOutside()) {
            return;
        }
        dismiss();
        if (v.getId() != CANCEL_VIEW_ID && v.getId() != BG_VIEW_ID) {
            if (mListener != null) {
                mListener.onOtherTextViewClick(this, v.getId() - CANCEL_VIEW_ID
                        - 1);
            }
            isCancel = false;
        }
    }

    public static Builder createBuilder(Context context, FragmentManager fragmentManager) {
        hasTitle = false;
        return new Builder(context, fragmentManager);
    }

    public static Builder createBuilder(Context context, boolean hasTitleItem, FragmentManager fragmentManager) {
        hasTitle = hasTitleItem;
        return new Builder(context, fragmentManager);
    }

    private static class Attributes {
        private Context mContext;

        public Attributes(Context context) {
            mContext = context;
            this.background = new ColorDrawable(Color.TRANSPARENT);
            this.cancelTextViewBackground = new ColorDrawable(Color.BLACK);
            ColorDrawable gray = new ColorDrawable(Color.GRAY);
            this.otherTextViewTopBackground = gray;
            this.otherTextViewMiddleBackground = gray;
            this.otherTextViewBottomBackground = gray;
            this.otherTextViewSingleBackground = gray;
            this.cancelTextViewTextColor = Color.WHITE;
            this.otherTextViewTextColor = Color.BLACK;
            this.padding = dp2px(20);
            this.otherTextViewSpacing = dp2px(2);
            this.cancelTextViewMarginTop = dp2px(10);
            this.actionSheetTextSize = dp2px(14);
        }

        private int dp2px(int dp){
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    dp, mContext.getResources().getDisplayMetrics());
        }

        public Drawable getOtherTextViewMiddleBackground() {
            if (otherTextViewMiddleBackground instanceof StateListDrawable) {
                TypedArray a = mContext.getTheme().obtainStyledAttributes(null,
                        R.styleable.ActionSheet, R.attr.actionSheetStyle, 0);
                otherTextViewMiddleBackground = a
                        .getDrawable(R.styleable.ActionSheet_otherTextViewMiddleBackground);
                a.recycle();
            }
            return otherTextViewMiddleBackground;
        }

        Drawable background;
        Drawable cancelTextViewBackground;
        Drawable otherTextViewTopBackground;
        Drawable otherTextViewMiddleBackground;
        Drawable otherTextViewBottomBackground;
        Drawable otherTextViewSingleBackground;
        int cancelTextViewTextColor;
        int otherTextViewTextColor;
        int padding;
        int otherTextViewSpacing;
        int cancelTextViewMarginTop;
        float actionSheetTextSize;
    }

    public static class Builder {

        private Context mContext;
        private FragmentManager mFragmentManager;
        private CharSequence mCancelTextViewTitle;
        private CharSequence[] mOtherTextViewTitles;
        private String mTag = "actionSheet";
        private boolean mCancelableOnTouchOutside;
        private ActionSheetListener mListener;

        public Builder(Context context, FragmentManager fragmentManager) {
            mContext = context;
            mFragmentManager = fragmentManager;
        }

        public Builder setCancelTextViewTitle(CharSequence title) {
            mCancelTextViewTitle = title;
            return this;
        }

        public Builder setCancelTextViewTitle(int strId) {
            return setCancelTextViewTitle(mContext.getString(strId));
        }

        public Builder setOtherTextViewTitles(CharSequence... titles) {
            mOtherTextViewTitles = titles;
            return this;
        }

        public Builder setTag(String tag) {
            mTag = tag;
            return this;
        }

        public Builder setListener(ActionSheetListener listener) {
            this.mListener = listener;
            return this;
        }

        public Builder setCancelableOnTouchOutside(boolean cancelable) {
            mCancelableOnTouchOutside = cancelable;
            return this;
        }

        public Bundle prepareArguments() {
            Bundle bundle = new Bundle();
            bundle.putCharSequence(ARG_CANCEL_VIEW_TITLE, mCancelTextViewTitle);
            bundle.putCharSequenceArray(ARG_OTHER_VIEW_TITLES, mOtherTextViewTitles);
            bundle.putBoolean(ARG_CANCELABLE_ONTOUCHOUTSIDE, mCancelableOnTouchOutside);
            return bundle;
        }

        public ActionSheet show() {
            ActionSheet actionSheet = (ActionSheet) Fragment.instantiate(mContext, ActionSheet.class.getName(), prepareArguments());
            actionSheet.setActionSheetListener(mListener);
            actionSheet.show(mFragmentManager, mTag);
            return actionSheet;
        }
    }

    public interface ActionSheetListener {
        void onDismiss(ActionSheet actionSheet, boolean isCancel);
        void onOtherTextViewClick(ActionSheet actionSheet, int index);
    }

}