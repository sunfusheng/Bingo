package com.sun.bingo.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.sun.bingo.R;
import com.sun.bingo.control.manager.ImageManager;
import com.sun.bingo.widget.ViewPagerFixed;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by sunfusheng on 15/9/6.
 */
public class ImageActivity extends BaseActivity {

    @BindView(R.id.view_pager)
    ViewPagerFixed viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private int index;
    private int size;
    private String[] picUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initData() {
        index = getIntent().getIntExtra("index", 0);
        picUrls = getIntent().getStringArrayExtra("picUrls");
        size = picUrls.length;
    }

    private void initView() {
        initToolBar(toolbar, true, index + 1 + "/"+size);
        viewPager.setAdapter(new CheckImageAdapter(mContext, picUrls));
        viewPager.setCurrentItem(index);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                toolbar.setTitle(position + 1 + "/" + size);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    static class CheckImageAdapter extends PagerAdapter {

        private String[] picUrls;
        private int size;
        private ImageManager imageManager;

        public CheckImageAdapter(Context context, String[]picUrls) {
            this.picUrls = picUrls;
            this.size = picUrls.length;
            imageManager = new ImageManager(context);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            imageManager.loadUrlImage(picUrls[position], photoView);
            container.addView(photoView);
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
