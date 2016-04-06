package com.sun.bingo.wxapi;

import android.app.Activity;
import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by sunfusheng on 16/4/4.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI wxapi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //注册API
        wxapi = WXAPIFactory.createWXAPI(this, "APP_ID");
        wxapi.handleIntent(getIntent(), this);
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp instanceof SendAuth.Resp) {
            SendAuth.Resp newResp = (SendAuth.Resp) resp;

            //获取微信传回的code
            String code = newResp.code;
            Logger.d("log-wx", "code: "+code);
        }

    }

}
