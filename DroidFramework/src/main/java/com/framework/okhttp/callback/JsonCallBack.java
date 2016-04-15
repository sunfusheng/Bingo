package com.framework.okhttp.callback;

import com.framework.util.FastJsonUtil;

import java.lang.reflect.ParameterizedType;

import okhttp3.Response;

/**
 * Created by sunfusheng on 16/3/2.
 */
public abstract class JsonCallBack<T> extends OkHttpCallBack<T> {

    @Override
    public T parseResponse(Response response) throws Exception {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> clazz = (Class<T>) parameterizedType.getActualTypeArguments()[0];

        String string = response.body().string();
        T obj = FastJsonUtil.parseJson(string, clazz);
        return obj;
    }
}
