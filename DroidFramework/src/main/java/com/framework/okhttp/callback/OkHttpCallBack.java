package com.framework.okhttp.callback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sunfusheng on 15/12/8.
 */
public abstract class OkHttpCallBack<T> {

    public void onStart(Request request) {}

    public void onProgress(float progress) {}

    public abstract T parseResponse(Response response) throws Exception;

    public abstract void onSuccess(T response);

    public abstract void onFailure(Call request, Exception e);


    public static final OkHttpCallBack DEFAULT_CALLBACK = new StringCallback() {

        @Override
        public void onSuccess(String response) {}

        @Override
        public void onFailure(Call request, Exception e) {}
    };
}