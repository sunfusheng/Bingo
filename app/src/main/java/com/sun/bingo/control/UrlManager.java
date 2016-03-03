package com.sun.bingo.control;

import com.sun.bingo.constant.ConstantParams;

/**
 * Created by sunfusheng on 15/11/19.
 */
public interface UrlManager {

    // fir.im 最新软件版本信息
    String URL_APP_VERSION = "http://api.fir.im/apps/latest/"+ ConstantParams.FIR_IM_ID+"?api_token="+ConstantParams.FIR_IM_API_TOKEN+"&type=android";

    // 获取新浪用户信息
    String URL_SINA_USER_INFO = "https://api.weibo.com/2/users/show.json";

}
