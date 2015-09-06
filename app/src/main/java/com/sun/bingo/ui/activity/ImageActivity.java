package com.sun.bingo.ui.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.apkfuns.logutils.LogUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sun.bingo.R;
import com.sun.bingo.widget.ViewPagerFixed;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by sunfusheng on 15/9/6.
 */
public class ImageActivity extends BaseActivity {

    @InjectView(R.id.view_pager)
    ViewPagerFixed viewPager;

    private int index;
    private String[] picUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.inject(this);

        initSystemBarTint(true, getResources().getColor(android.R.color.transparent));
        initData(savedInstanceState);

    }

    private void initData(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            index = getIntent().getIntExtra("index", 0);
            picUrls = getIntent().getStringArrayExtra("picUrls");
            LogUtils.d("------------> savedInstanceState == null");
        } else {
            index = savedInstanceState.getInt("index", 0);
            picUrls = savedInstanceState.getStringArray("picUrls");
            LogUtils.d("------------> savedInstanceState");
        }

        viewPager.setAdapter(new CheckImageAdapter(picUrls));
        viewPager.setCurrentItem(index);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("index", index);
        outState.putStringArray("picUrls", picUrls);
        LogUtils.d("------------> onSaveInstanceState(Bundle outState)");
    }

    static class CheckImageAdapter extends PagerAdapter {

        private String[] picUrls;
        private int size;
        private DisplayImageOptions picOptions;

        public CheckImageAdapter(String[] picUrls) {
            this.picUrls = picUrls;
            this.size = picUrls.length;

            picOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                    .showImageOnLoading(R.color.font_black_6)
                    .showImageForEmptyUri(R.color.font_black_6)
                    .showImageOnFail(R.color.font_black_6)
                    .build();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            ImageLoader.getInstance().displayImage(picUrls[position], photoView, picOptions);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return size;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
