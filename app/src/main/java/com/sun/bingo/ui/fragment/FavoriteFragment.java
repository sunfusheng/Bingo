package com.sun.bingo.ui.fragment;

import android.os.Bundle;

import com.sun.bingo.adapter.RecyclerViewAdapter;
import com.sun.bingo.entity.BingoEntity;
import com.sun.bingo.entity.UserEntity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

public class FavoriteFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void refreshingData() {
        userEntity = BmobUser.getCurrentUser(getActivity(), UserEntity.class);
        List<String> favoriteList = userEntity.getFavoriteList();
        BmobQuery<BingoEntity> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereContainedIn("objectId", favoriteList);
        bmobQuery.include("userEntity");
        bmobQuery.findObjects(getActivity(), new FindListener<BingoEntity>() {
            @Override
            public void onSuccess(List<BingoEntity> entities) {
                recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), entities, RecyclerViewAdapter.CANCEL_FAVORITE));
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

    }
}
