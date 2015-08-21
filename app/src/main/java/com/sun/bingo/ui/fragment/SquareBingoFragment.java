package com.sun.bingo.ui.fragment;

import android.os.Bundle;

import com.sun.bingo.adapter.RecyclerViewAdapter;
import com.sun.bingo.entity.BingoEntity;
import com.sun.bingo.framework.orm.DbHelper;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class SquareBingoFragment extends BaseFragment {

    private DbHelper dbHelper;
    private List<BingoEntity> bingoEntities;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DbHelper(getActivity(), BingoEntity.class);
        bingoEntities = dbHelper.findList(BingoEntity.class);
    }

    @Override
    protected void getBingoEntityList() {
        BmobQuery<BingoEntity> newBingoEntities = new BmobQuery<>();
        newBingoEntities.order("-createdAt");
        newBingoEntities.include("userEntity");
        newBingoEntities.findObjects(getActivity(), new FindListener<BingoEntity>() {
            @Override
            public void onSuccess(List<BingoEntity> entities) {
                if (entities == null || entities.size() == 0) {
                    entities = bingoEntities;
                } else {
                    dbHelper.saveList(entities);
                }
                recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), entities));
                completeRefresh();
            }

            @Override
            public void onError(int i, String s) {
                recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), bingoEntities));
                completeRefresh();
            }
        });
    }
}
