package com.sun.bingo.control;

import android.content.Context;

import com.sun.bingo.model.BingoEntity;
import com.sun.bingo.model.UserEntity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by sunfusheng on 16/5/27.
 */
public class BmobControl {

    private Context mContext;
    private UserEntity mineEntity;

    public BmobControl(Context context, UserEntity mineEntity) {
        this.mContext = context;
        this.mineEntity = mineEntity;
    }

    // 收藏、取消收藏操作
    public void favoriteBingo(final BingoEntity bingo, final UpdateListener listener) {
        final String bingoId = bingo.getObjectId();
        String mineId = mineEntity.getObjectId();
        List<String> favoriteUserIds = bingo.getFavoriteUserIds();
        List<String> myFavoriteList = mineEntity.getFavoriteList();

        if (favoriteUserIds == null) favoriteUserIds = new ArrayList<>();
        if (myFavoriteList == null) myFavoriteList = new ArrayList<>();

        if (myFavoriteList.indexOf(bingoId) >= 0) {
            // 取消收藏
            myFavoriteList.remove(bingoId);
            if (favoriteUserIds.indexOf(mineId) >= 0) {
                favoriteUserIds.remove(mineId);
            }
        } else {
            // 收藏
            myFavoriteList.add(bingoId);
            favoriteUserIds.add(mineId);
        }

        mineEntity.setFavoriteList(myFavoriteList);
        bingo.setFavoriteUserIds(favoriteUserIds);

        mineEntity.update(mContext, mineId, new UpdateListener() {
            @Override
            public void onSuccess() {
                bingo.update(mContext, bingoId, listener);
            }

            @Override
            public void onFailure(int i, String s) {
            }
        });
    }

}
