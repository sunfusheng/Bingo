package com.sun.bingo.control;

/**
 * Created by sunfusheng on 15/11/19.
 */
public class HttpApi implements UrlManager {

    private static HttpApi mApi;

    private HttpApi() {}

    public static HttpApi getInstance() {
        if (mApi == null) {
            synchronized (HttpApi.class) {
                if (mApi == null) {
                    mApi = new HttpApi();
                }
            }
        }
        return mApi;
    }

}
