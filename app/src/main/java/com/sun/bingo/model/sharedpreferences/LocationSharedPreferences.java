package com.sun.bingo.model.sharedpreferences;

import de.devland.esperandro.SharedPreferenceActions;
import de.devland.esperandro.SharedPreferenceMode;
import de.devland.esperandro.annotations.SharedPreferences;

/**
 * Created by sunfusheng on 2015/4/23.
 */
@SharedPreferences(name = "location", mode = SharedPreferenceMode.PRIVATE)
public interface LocationSharedPreferences extends SharedPreferenceActions {

    /*纬度*/
    String latitude();
    void latitude(String latitude);
    /*经度*/
    String longitude();
    void longitude(String longitude);

    /*全地址*/
    String addrStr();
    void addrStr(String addrStr);

    /*省*/
    String province();
    void province(String province);
    /*市*/
    String city();
    void city(String city);
    /*区*/
    String district();
    void district(String district);
    /*街道*/
    String street();
    void street(String street);

    /*街道号码*/
    String streetNumber();
    void streetNumber(String streetNumber);
    /*城市ID*/
    String cityCode();
    void cityCode(String cityCode);

}
