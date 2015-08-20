package com.sun.bingo.constant;

public interface ConstantParams {

    /**
     * 测试环境
     */
//    String BMOB_APP_ID = "6c2aab3688f2fdf6f802f93f30c3bfcd";
//    String SMS_LOGIN_VERIFY_CODE = "Bingo登录";

    /**
     * 正式环境
     */
    String BMOB_APP_ID = "52676e9314981e9d2f7a068e71a8e85a";
    String SMS_LOGIN_VERIFY_CODE = "Bingo登录";


    /**
     * https://fir.im
     *
     * id 应用ID，可在"应用管理"->"基本信息"查看
     * short 应用短链接，上传应用时会随机生成，用户可修改
     * api_token 用于识别用户身份, api_token 可以在[用户信息]中生成和刷新
     * release_id Release 表示一个版本的唯一 ID
     */
    String FIR_IM_ID = "55d40a70748aac58a6000047";
    String FIR_IM_API_TOKEN = "071e514d156c894cc3d73ed2d9c8b538";

}
