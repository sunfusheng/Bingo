package com.sun.bingo.ui.fragment;

import android.os.Bundle;

import com.apkfuns.logutils.LogUtils;
import com.sun.bingo.control.PageControl;
import com.sun.bingo.entity.BingoEntity;
import com.sun.bingo.framework.dialog.ToastTip;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class SquareBingoFragment extends BaseFragment<PageControl> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void refreshingData() {
        BmobQuery<BingoEntity> newBingoEntities = new BmobQuery<>();
        newBingoEntities.order("-createdAt");
        newBingoEntities.setLimit(pageCount);
        newBingoEntities.include("userEntity");
        newBingoEntities.findObjects(getActivity(), new FindListener<BingoEntity>() {
            @Override
            public void onSuccess(List<BingoEntity> entities) {
                LogUtils.d("refreshingData: pageCount->"+pageCount+"  entities.size()->"+entities.size());
                mEntities.clear();
                mEntities.addAll(entities);
                mAdapter.notifyDataSetChanged();
                completeRefresh();
            }

            @Override
            public void onError(int i, String s) {
                completeRefresh();
            }
        });
    }

    @Override
    protected void loadingingData() {
        BmobQuery<BingoEntity> newBingoEntities = new BmobQuery<>();
        newBingoEntities.order("-createdAt");
        newBingoEntities.setSkip(pageCount);
        newBingoEntities.include("userEntity");
        newBingoEntities.findObjects(getActivity(), new FindListener<BingoEntity>() {
            @Override
            public void onSuccess(List<BingoEntity> entities) {
                if (entities == null || entities.size() == 0) {
                    ToastTip.showToastDialog(getActivity(), "已全部加载完毕");
                    return;
                }
                LogUtils.d("loadingingData: pageCount->"+pageCount+"  entities.size()->"+entities.size());
                mEntities.addAll(pageCount, entities);
                pageCount += entities.size();
                mAdapter.notifyDataSetChanged();
                completeRefresh();
            }

            @Override
            public void onError(int i, String s) {
                completeRefresh();
            }
        });
    }
}
