package com.sun.bingo.control;

import android.content.Context;

import com.framework.annotation.AsyncAtomMethod;
import com.framework.base.BaseControl;
import com.framework.proxy.MessageProxy;
import com.sun.bingo.util.image.CompressImageHelper;

/**
 * Created by sunfusheng on 15/11/5.
 */
public class SingleControl extends BaseControl {

    public SingleControl(MessageProxy mMessageCallBack) {
        super(mMessageCallBack);
    }

    // 异步压缩图片, 返回压缩后的Url
    @AsyncAtomMethod
    public void getCompressImagePath(Context context, String imagePath) {
        String path = CompressImageHelper.compressImageView(context, imagePath);
        mModel.put(1, path);
        sendMessage("getCompressImagePathCallBack");
    }

    @AsyncAtomMethod(withCancelableDialog = true)
    public void getSinaUserInfo(String cityname) {
        try {

            sendMessage("getSinaUserInfoCallBack");
        } catch (Exception e) {
            dealWithException(e);
        }
    }

}
