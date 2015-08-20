package com.sun.bingo.framework.http.HttpControl;

import com.sun.bingo.constant.ConstantParams;

/**
 * Created by sunfusheng on 15/8/19.
 */
public class UrlManager {

    /**
     * 获得软件版本信息
     *
     * @return url
     */
    public String getVersionInfoUrl() {
        return "http://api.fir.im/apps/latest/"+ ConstantParams.FIR_IM_ID+"?api_token="+ConstantParams.FIR_IM_API_TOKEN+"&type=android";
    }

    //http://api.fir.im/apps/55d40a70748aac58a6000047/download_token?api_token=071e514d156c894cc3d73ed2d9c8b538

    //http://download.fir.im/apps/55d40a70748aac58a6000047/install?download_token=7983191171eece03139125e41ee31520

}
