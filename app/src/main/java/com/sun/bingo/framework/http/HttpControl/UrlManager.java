package com.sun.bingo.framework.http.HttpControl;

import com.sun.bingo.constant.ConstantParams;

/**
 * Created by sunfusheng on 15/8/19.
 */
public class UrlManager {

    // fir.im 最新软件版本信息
    public String getVersionInfoUrl() {
        return "http://api.fir.im/apps/latest/"+ ConstantParams.FIR_IM_ID+"?api_token="+ConstantParams.FIR_IM_API_TOKEN+"&type=android";
    }

    // 获取新浪用户信息
    public String getSinaUserInfoUrl() {
        return "https://api.weibo.com/2/users/show.json";
    }
}
