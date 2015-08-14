package com.sun.bingo.ui.activity;

import android.os.Bundle;

import com.apkfuns.logutils.LogUtils;
import com.sun.bingo.R;
import com.sun.bingo.adapter.RecyclerViewAdapter;
import com.sun.bingo.entity.BingoEntity;
import com.sun.bingo.entity.UserEntity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by sunfusheng on 15/8/14.
 */
public class FavoriteActivity extends BaseListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar(true, R.string.menu_my_favorite);
    }

    @Override
    protected void getBingoEntityList() {
        userEntity = BmobUser.getCurrentUser(this, UserEntity.class);
        List<String> favoriteList = userEntity.getFavoriteList();
        BmobQuery<BingoEntity> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereContainedIn("objectId", favoriteList);
//        bmobQuery.order("-updatedAt");
        bmobQuery.include("userEntity");
        bmobQuery.findObjects(this, new FindListener<BingoEntity>() {
            @Override
            public void onSuccess(List<BingoEntity> entities) {
//                LogUtils.d(entities);
//                Collections.reverse(entities);
                LogUtils.d(entities);
                recyclerView.setAdapter(new RecyclerViewAdapter(FavoriteActivity.this, entities, RecyclerViewAdapter.CANCEL_FAVORITE));
                completeRefresh();
            }

            @Override
            public void onError(int i, String s) {
                completeRefresh();
            }
        });
    }

}
