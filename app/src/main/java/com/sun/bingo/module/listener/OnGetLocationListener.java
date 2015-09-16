package com.sun.bingo.module.listener;

import com.baidu.location.BDLocation;

public interface OnGetLocationListener {

    void onFailed();
    void onSucceed(BDLocation location);
}
