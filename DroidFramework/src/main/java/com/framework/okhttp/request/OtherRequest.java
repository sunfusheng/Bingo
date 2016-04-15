package com.framework.okhttp.request;

import android.text.TextUtils;

import com.framework.okhttp.OkHttpProxy;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.internal.http.HttpMethod;

public class OtherRequest extends OkHttpRequest {

    private static MediaType MEDIA_TYPE_PLAIN = MediaType.parse("text/plain;charset=utf-8");
    private RequestBody requestBody;
    private String method;
    private String content;

    public OtherRequest(RequestBody requestBody, String content, String method, String url, Object tag, Map<String, String> params, Map<String, String> headers) {
        super(url, tag, params, headers);
        this.requestBody = requestBody;
        this.method = method;
        this.content = content;
    }

    @Override
    protected RequestBody buildRequestBody() {
        if (requestBody == null && TextUtils.isEmpty(content) && HttpMethod.requiresRequestBody(method)) {
            throw new RuntimeException("requestBody and content should not be null in method: " + method);
        }

        if (requestBody == null && !TextUtils.isEmpty(content)) {
            requestBody = RequestBody.create(MEDIA_TYPE_PLAIN, content);
        }
        return requestBody;
    }

    @Override
    protected Request buildRequest(Request.Builder builder, RequestBody requestBody) {
        if (method.equals(OkHttpProxy.METHOD.PUT)) {
            builder.put(requestBody);
        } else if (method.equals(OkHttpProxy.METHOD.DELETE)) {
            if (requestBody == null)
                builder.delete();
            else
                builder.delete(requestBody);
        } else if (method.equals(OkHttpProxy.METHOD.HEAD)) {
            builder.head();
        } else if (method.equals(OkHttpProxy.METHOD.PATCH)) {
            builder.patch(requestBody);
        }
        return builder.build();
    }

    @Override
    public String toString() {
        if (!TextUtils.isEmpty(content)) {
            return super.toString() + ", requestBody{content=" + content + "} ";
        }
        return super.toString() + ", requestBody{requestBody=" + requestBody.toString() + "} ";
    }
}
