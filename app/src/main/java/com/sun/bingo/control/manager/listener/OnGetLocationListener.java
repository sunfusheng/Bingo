package com.sun.bingo.control.manager.listener;

import com.baidu.location.BDLocation;

public interface OnGetLocationListener {

    void onFailed();
    void onSucceed(BDLocation location);
}
