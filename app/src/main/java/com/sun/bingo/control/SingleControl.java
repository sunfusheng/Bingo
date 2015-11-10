package com.sun.bingo.control;

import android.content.Context;

import com.sun.bingo.entity.BingoEntity;
import com.sun.bingo.framework.annotation.AsyncMethod;
import com.sun.bingo.framework.base.BaseControl;
import com.sun.bingo.framework.proxy.MessageProxy;
import com.sun.bingo.util.image.CompressImageHelper;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by sunfusheng on 15/11/5.
 */
public class SingleControl extends BaseControl {

    public SingleControl(MessageProxy mMessageCallBack) {
        super(mMessageCallBack);
    }

    /**
     * 异步压缩图片, 返回压缩后的Url
     */
    @AsyncMethod
    public void getCompressImagePath(Context context, String imagePath) {
        String path = CompressImageHelper.compressImageView(context, imagePath);
        mModel.put(1, path);
        sendMessage("getCompressImagePathCallBack");
    }

    /**
     * 我的Bingo
     */
    @AsyncMethod
    public void getUserBingoListData(Context context) {
        BmobQuery<BingoEntity> list = new BmobQuery<>();
        list.addWhereEqualTo("userEntity", BmobUser.getCurrentUser(context).getObjectId());
        list.order("-createdAt");
        list.include("userEntity");
        list.findObjects(context, new FindListener<BingoEntity>() {
            @Override
            public void onSuccess(List<BingoEntity> entities) {
                mModel.put(1, entities);
                if (entities == null || entities.size() == 0) {
                    sendMessage("getDataEmpty");
                } else {
                    sendMessage("getDataAdequate");
                }
            }

            @Override
            public void onError(int i, String s) {
                dealWithExceptionMessage(s);
                sendMessage("getDataFail");
            }
        });
    }

}
