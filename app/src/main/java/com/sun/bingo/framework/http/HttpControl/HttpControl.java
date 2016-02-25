package com.sun.bingo.framework.http.HttpControl;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by sunfusheng on 15/8/19.
 */
public class HttpControl {

    private HttpUtils mHttp = new HttpUtils();
    private UrlManager mUrlManager = new UrlManager();

    // 获取软件版本信息
    public <T> void getVersionInfo(RequestCallBack<T> callBack) {
        mHttp.send(HttpRequest.HttpMethod.GET, mUrlManager.getVersionInfoUrl(), callBack);
    }

    // 获取新浪用户信息
    public <T> void getSinaUserInfo(String access_token, String uid, RequestCallBack<T> callBack) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("access_token", access_token);
        params.addQueryStringParameter("uid", uid);
        mHttp.send(HttpRequest.HttpMethod.GET, mUrlManager.getSinaUserInfoUrl(), params, callBack);
    }

}
