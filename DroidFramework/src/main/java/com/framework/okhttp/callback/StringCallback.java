package com.framework.okhttp.callback;

import java.io.IOException;

import okhttp3.Response;

public abstract class StringCallback extends OkHttpCallBack<String> {

    @Override
    public String parseResponse(Response response) throws IOException {
        return response.body().string();
    }
}
