package com.sun.bingo.framework.okhttp.callback;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * Created by sunfusheng on 16/3/2.
 */
public abstract class JsonCallBack<T> extends OkHttpCallBack<T> {

    @Override
    public T parseResponse(Response response) throws Exception {
        Type type = getSuperclassTypeParameter(getClass());
        String string = response.body().string();
        T obj = new Gson().fromJson(string, type);
        return obj;
    }

    private Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameter = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameter.getActualTypeArguments()[0]);
    }
}
