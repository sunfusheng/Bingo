package com.sun.bingo.control;

import android.content.Context;

import com.sun.bingo.entity.BingoEntity;
import com.sun.bingo.entity.UserEntity;
import com.sun.bingo.framework.annotation.AsyncMethod;
import com.sun.bingo.framework.base.BaseControl;
import com.sun.bingo.framework.proxy.MessageProxy;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by sunfusheng on 15/7/21.
 */
public class PageControl extends BaseControl {

    public static final int PAGE_SIZE = 10;
    private int pageSize = PAGE_SIZE;

    public PageControl(MessageProxy mMessageCallBack) {
        super(mMessageCallBack);
    }

    /**
     * Bingo首页
     */
    @AsyncMethod
    public void getSquareBingoListData(Context context) {
        pageSize = PAGE_SIZE;
        BmobQuery<BingoEntity> list = new BmobQuery<>();
        list.setLimit(PAGE_SIZE);
        list.order("-createdAt");
        list.include("userEntity");
        list.findObjects(context, new FindListener<BingoEntity>() {
            @Override
            public void onSuccess(List<BingoEntity> entities) {
                mModel.put(1, entities);
                if (entities.size() == 0) {
                    sendMessage("getDataEmpty");
                } else if (entities.size() == PAGE_SIZE) {
                    sendMessage("getDataAdequate");
                } else {
                    sendMessage("getDataInadequate");
                }
            }

            @Override
            public void onError(int i, String s) {
                dealWithExceptionMessage(s);
                sendMessage("getDataFail");
            }
        });
    }

    /**
     * Bingo首页 (More)
     */
    @AsyncMethod
    public void getSquareBingoListDataMore(Context context) {
        BmobQuery<BingoEntity> list = new BmobQuery<>();
        list.setSkip(pageSize);
        list.setLimit(PAGE_SIZE);
        list.order("-createdAt");
        list.include("userEntity");
        list.findObjects(context, new FindListener<BingoEntity>() {
            @Override
            public void onSuccess(List<BingoEntity> entities) {
                pageSize += entities.size();
                mModel.put(2, entities);
                if (entities.size() == 0) {
                    sendMessage("getMoreDataEmpty");
                } else if (entities.size() == PAGE_SIZE) {
                    sendMessage("getMoreDataAdequate");
                } else {
                    sendMessage("getMoreDataInadequate");
                }
            }

            @Override
            public void onError(int i, String s) {
                dealWithExceptionMessage(s);
                sendMessage("getMoreDataFail");
            }
        });
    }

    /**
     * 我的Bingo
     */
    @AsyncMethod
    public void getMyBingoListData(Context context) {
        pageSize = PAGE_SIZE;
        BmobQuery<BingoEntity> list = new BmobQuery<>();
        list.setLimit(PAGE_SIZE);
        list.addWhereEqualTo("userEntity", BmobUser.getCurrentUser(context).getObjectId());
        list.order("-createdAt");
        list.include("userEntity");
        list.findObjects(context, new FindListener<BingoEntity>() {
            @Override
            public void onSuccess(List<BingoEntity> entities) {
                mModel.put(1, entities);
                if (entities.size() == 0) {
                    sendMessage("getDataEmpty");
                } else if (entities.size() == PAGE_SIZE) {
                    sendMessage("getDataAdequate");
                } else {
                    sendMessage("getDataInadequate");
                }
            }

            @Override
            public void onError(int i, String s) {
                dealWithExceptionMessage(s);
                sendMessage("getDataFail");
            }
        });
    }

    /**
     * 我的Bingo (More)
     */
    @AsyncMethod
    public void getMyBingoListDataMore(Context context) {
        BmobQuery<BingoEntity> list = new BmobQuery<>();
        list.setSkip(pageSize);
        list.setLimit(PAGE_SIZE);
        list.addWhereEqualTo("userEntity", BmobUser.getCurrentUser(context).getObjectId());
        list.order("-createdAt");
        list.include("userEntity");
        list.findObjects(context, new FindListener<BingoEntity>() {
            @Override
            public void onSuccess(List<BingoEntity> entities) {
                pageSize += entities.size();
                mModel.put(2, entities);
                if (entities.size() == 0) {
                    sendMessage("getMoreDataEmpty");
                } else if (entities.size() == PAGE_SIZE) {
                    sendMessage("getMoreDataAdequate");
                } else {
                    sendMessage("getMoreDataInadequate");
                }
            }

            @Override
            public void onError(int i, String s) {
                dealWithExceptionMessage(s);
                sendMessage("getMoreDataFail");
            }
        });
    }

    /**
     * 我的收藏
     */
    @AsyncMethod
    public void getFavoriteBingoListData(Context context) {
        UserEntity userEntity = BmobUser.getCurrentUser(context, UserEntity.class);
        List<String> favoriteList = userEntity.getFavoriteList();
        pageSize = PAGE_SIZE;
        BmobQuery<BingoEntity> list = new BmobQuery<>();
        list.setLimit(PAGE_SIZE);
        list.addWhereContainedIn("objectId", favoriteList);
        list.include("userEntity");
        list.findObjects(context, new FindListener<BingoEntity>() {
            @Override
            public void onSuccess(List<BingoEntity> entities) {
                mModel.put(1, entities);
                if (entities.size() == 0) {
                    sendMessage("getDataEmpty");
                } else if (entities.size() == PAGE_SIZE) {
                    sendMessage("getDataAdequate");
                } else {
                    sendMessage("getDataInadequate");
                }
            }

            @Override
            public void onError(int i, String s) {
                dealWithExceptionMessage(s);
                sendMessage("getDataFail");
            }
        });
    }

    /**
     * 我的收藏 (More)
     */
    @AsyncMethod(withDialog = true)
    public void getFavoriteBingoListDataMore(Context context) {
        UserEntity userEntity = BmobUser.getCurrentUser(context, UserEntity.class);
        List<String> favoriteList = userEntity.getFavoriteList();
        BmobQuery<BingoEntity> list = new BmobQuery<>();
        list.setSkip(pageSize);
        list.setLimit(PAGE_SIZE);
        list.addWhereContainedIn("objectId", favoriteList);
        list.include("userEntity");
        list.findObjects(context, new FindListener<BingoEntity>() {
            @Override
            public void onSuccess(List<BingoEntity> entities) {
                pageSize += entities.size();
                mModel.put(2, entities);
                if (entities.size() == 0) {
                    sendMessage("getMoreDataEmpty");
                } else if (entities.size() == PAGE_SIZE) {
                    sendMessage("getMoreDataAdequate");
                } else {
                    sendMessage("getMoreDataInadequate");
                }
            }

            @Override
            public void onError(int i, String s) {
                dealWithExceptionMessage(s);
                sendMessage("getMoreDataFail");
            }
        });
    }
}
