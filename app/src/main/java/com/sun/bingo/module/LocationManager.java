package com.sun.bingo.module;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.orhanobut.logger.Logger;
import com.sun.bingo.module.listener.OnGetLocationListener;
import com.sun.bingo.sharedpreferences.LocationSharedPreferences;

import de.devland.esperandro.Esperandro;

public class LocationManager {

    private Context mContext;
    private LocationClient mLocationClient;
    private OnGetLocationListener mOnGetLocationListener;
    private MyLocationListener locationListener;

    public LocationManager(Context context){
        mContext = context;
        mLocationClient = new LocationClient(context);
        locationListener = new MyLocationListener();
        initLocationClient();
        mLocationClient.registerLocationListener(locationListener);
    }

    private void initLocationClient() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
        option.setScanSpan(3000);//设置发起定位请求的间隔时间
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    public void startGetLocation(){
        mLocationClient.start();
        mLocationClient.requestLocation();
        Logger.i("开始定位...");
    }

    public void stopGetLocation(){
        mLocationClient.stop();
    }

    public class MyLocationListener implements BDLocationListener {
        LocationSharedPreferences sharedPreference = Esperandro.getPreferences(LocationSharedPreferences.class, mContext);
        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location == null || location.getLocType() != BDLocation.TypeNetWorkLocation && location.getLocType() != BDLocation.TypeGpsLocation) {
                Logger.i("定位失败");
                stopGetLocation();
                if (mOnGetLocationListener != null) {
                    mOnGetLocationListener.onFailed();
                }
                return;
            }

            sharedPreference.addrStr(location.getAddrStr());
            sharedPreference.province(location.getProvince());
            sharedPreference.city(location.getCity());
            sharedPreference.district(location.getDistrict());
            sharedPreference.street(location.getStreet());
            sharedPreference.cityCode(location.getCityCode());
            sharedPreference.streetNumber(location.getStreetNumber());
            sharedPreference.latitude(String.valueOf(location.getLatitude()));
            sharedPreference.longitude(String.valueOf(location.getLongitude()));

            Logger.i("定位成功：" + location.getAddrStr());
            stopGetLocation();
            if (mOnGetLocationListener != null) {
                mOnGetLocationListener.onSucceed(location);
            }
        }
    }

    public void setOnGetLocationListener(OnGetLocationListener onGetLocationListener) {
        this.mOnGetLocationListener = onGetLocationListener;
    }
}
