package com.sun.bingo.framework.http.HttpControl;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by sunfusheng on 15/8/19.
 */
public class HttpControl {

    private HttpUtils mHttp = new HttpUtils();
    private UrlManager mUrlManager = new UrlManager();

    /**
     * 获得软件版本信息
     *
     * @param callBack
     * @param <T>
     */
    public <T> void getVersionInfo(RequestCallBack<T> callBack) {
        mHttp.send(HttpRequest.HttpMethod.GET, mUrlManager.getVersionInfoUrl(), callBack);
    }

}
