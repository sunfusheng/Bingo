package com.sun.bingo.ui.fragment;

import com.sun.bingo.adapter.RecyclerViewAdapter;
import com.sun.bingo.entity.BingoEntity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

public class MyBingoFragment extends BaseFragment {

    @Override
    protected void getBingoEntityList() {
        BmobQuery<BingoEntity> newBingoEntities = new BmobQuery<>();
        newBingoEntities.addWhereEqualTo("userEntity", BmobUser.getCurrentUser(getActivity()).getObjectId());
        newBingoEntities.order("-createdAt");
        newBingoEntities.include("userEntity");
        newBingoEntities.findObjects(getActivity(), new FindListener<BingoEntity>() {
            @Override
            public void onSuccess(List<BingoEntity> entities) {
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
