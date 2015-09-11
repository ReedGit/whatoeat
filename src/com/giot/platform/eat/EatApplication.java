package com.giot.platform.eat;

import android.app.Application;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class EatApplication extends Application {

	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "gcj02";
	public LocationClient mLocationClient;
	private LocationListener myLocationListener = new LocationListener();
	public float latitude,longitude; 

	@Override
	public void onCreate() {
		super.onCreate();
		mLocationClient = new LocationClient(this.getApplicationContext());
		myLocationListener = new LocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        initLocation();
	}
	
	private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);//��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
        option.setCoorType(tempcoor);//��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ��
        option.setIgnoreKillProcess(false);//��ѡ��Ĭ��true����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ�ϲ�ɱ��
        mLocationClient.setLocOption(option);
    }

	public class LocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			Log.e("listener",location.getLatitude()+","+location.getLongitude());
			latitude=(float) location.getLatitude();
			longitude=(float) location.getLongitude();

		}
	}

}