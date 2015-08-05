package com.sun.bingo.ui.fragment;

import com.apkfuns.logutils.LogUtils;
import com.sun.bingo.adapter.RecyclerViewAdapter;
import com.sun.bingo.entity.NewBingoEntity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

public class MyBingoFragment extends BaseFragment {

    @Override
    protected void getBingoEntityList() {
        BmobQuery<NewBingoEntity> newBingoEntities = new BmobQuery<>();
        newBingoEntities.addWhereEqualTo("userEntity", BmobUser.getCurrentUser(getActivity()).getObjectId());
        newBingoEntities.order("-createdAt");
        newBingoEntities.findObjects(getActivity(), new FindListener<NewBingoEntity>() {
            @Override
            public void onSuccess(List<NewBingoEntity> entities) {
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
