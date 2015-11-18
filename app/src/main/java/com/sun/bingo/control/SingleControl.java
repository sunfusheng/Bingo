package com.sun.bingo.control;

import android.content.Context;

import com.sun.bingo.framework.annotation.AsyncAtomMethod;
import com.sun.bingo.framework.base.BaseControl;
import com.sun.bingo.framework.proxy.MessageProxy;
import com.sun.bingo.util.image.CompressImageHelper;

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
    @AsyncAtomMethod
    public void getCompressImagePath(Context context, String imagePath) {
        String path = CompressImageHelper.compressImageView(context, imagePath);
        mModel.put(1, path);
        sendMessage("getCompressImagePathCallBack");
    }

}
