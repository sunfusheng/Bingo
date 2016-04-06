package com.sun.bingo.ui.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.sun.bingo.R;
import com.sun.bingo.control.manager.ImageManager;
import com.sun.bingo.widget.ViewPagerFixed;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by sunfusheng on 15/9/6.
 */
public class ImageActivity extends BaseActivity {

    @Bind(R.id.view_pager)
    ViewPagerFixed viewPager;
    @Bind(R.id.toolbar)
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
        viewPager.setAdapter(new CheckImageAdapter(picUrls));
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

        public CheckImageAdapter(String[] picUrls) {
            this.picUrls = picUrls;
            this.size = picUrls.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            ImageManager.getInstance().loadUrlImage(container.getContext(), picUrls[position], photoView);
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
