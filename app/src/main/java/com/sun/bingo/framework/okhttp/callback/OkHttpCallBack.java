package com.sun.bingo.framework.okhttp.callback;


import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sunfusheng on 15/12/8.
 */
public abstract class OkHttpCallBack<T> {

    public void onStart(Request request) {}

    public void inProgress(float progress) {}

    public abstract void onSuccess(T response);

    public abstract void onFailure(Request request, Exception e);

    public abstract T parseNetworkResponse(Response response) throws Exception;

    public abstract void onError(Call call, Exception e);

    public abstract void onResponse(T response);


    public static final OkHttpCallBack DEFAULT_CALLBACK = new OkHttpCallBack() {

        @Override
        public Object parseNetworkResponse(Response response) throws Exception {
            return null;
        }

        @Override
        public void onError(Call call, Exception e) {

        }

        @Override
        public void onResponse(Object response) {

        }

        @Override
        public void onSuccess(Object obj) {}

        @Override
        public void onFailure(Request request, Exception e) {}
    };
}
