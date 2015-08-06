package com.sun.bingo.ui.fragment;

import android.os.Bundle;

import com.apkfuns.logutils.LogUtils;
import com.sun.bingo.adapter.RecyclerViewAdapter;
import com.sun.bingo.entity.BingoEntity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class SquareBingoFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void getBingoEntityList() {
        BmobQuery<BingoEntity> newBingoEntities = new BmobQuery<>();
        newBingoEntities.order("-createdAt");
        newBingoEntities.findObjects(getActivity(), new FindListener<BingoEntity>() {
            @Override
            public void onSuccess(List<BingoEntity> entities) {
                LogUtils.d(entities);
                recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), entities));
                completeRefresh();
            }

            @Override
            public void onError(int i, String s) {
                completeRefresh();
            }
        });
    }
}
