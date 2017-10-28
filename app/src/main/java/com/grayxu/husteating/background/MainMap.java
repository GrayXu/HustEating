package com.grayxu.husteating.background;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.grayxu.husteating.R;
import com.grayxu.husteating.UI.DetailActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/11.
 * 这是用来初始化地图的工具类
 */

public class MainMap {

    private static ArrayList<LatLng> locList;
    private static Activity activity;
    private static LatLng myLatLng;
    private static AMapLocationClient mLocationClient;
    private static boolean isFirst = true;//用来进行刚打开的时候进行地图镜头放大

    /**
     * 作为工具类的初始方法，必须最早调用
     *
     * @param mapView    需要初始化的地图控件
     * @param activityIn 运行的北京
     */
    public static void init(final MapView mapView, final Activity activityIn) {
        activity = activityIn;
        locList = new ArrayList<>();
        LatLng latLngEastOne = new LatLng(30.511150, 114.419072);
        LatLng latLngEastThree = new LatLng(30.510734, 114.420424);
        locList.add(latLngEastOne);
        locList.add(latLngEastThree);

        final AMap aMap = mapView.getMap();
        aMap.setMyLocationEnabled(true);

        //控制蓝点逻辑
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类（即默认
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.getUiSettings().setCompassEnabled(true);
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        //初始化Marker（应该做批量设置

        final Marker markerEastOne = aMap.addMarker(new MarkerOptions().position(latLngEastOne));


        final Marker markerEastThree = aMap.addMarker(new MarkerOptions().position(latLngEastThree));

        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            View infoWindow;
            Button buttonE11;
            Button buttonE12;
            Button buttonE3;

            @Override
            public View getInfoWindow(Marker marker) {
                infoWindow = null;
                if (marker.equals(markerEastOne)) {
                    infoWindow = LayoutInflater.from(activity).inflate(R.layout.east_one_info_window, null);
                    buttonE11 = infoWindow.findViewById(R.id.buttonE11);
                    buttonE12 = infoWindow.findViewById(R.id.buttonE12);
                    buttonE11.setOnClickListener(new CanteenButtonListen());
                    buttonE12.setOnClickListener(new CanteenButtonListen());

                } else if (marker.equals(markerEastThree)) {
                    infoWindow = LayoutInflater.from(activity).inflate(R.layout.east_three_info_window, null);
                    buttonE3 = infoWindow.findViewById(R.id.buttonE3);
                    buttonE3.setOnClickListener(new CanteenButtonListen());
                }
                return infoWindow;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }

            //所有按钮的监听器（统一管理）
            class CanteenButtonListen implements View.OnClickListener {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, DetailActivity.class);
                    if (view.equals(buttonE11)) {
                        intent.putExtra("Name", "E11");// 这里做外部信息收集的工具（判断是哪个button点击进入的即可）
                    } else if (view.equals(buttonE12)) {
                        intent.putExtra("Name", "E12");
                    } else if (view.equals(buttonE3)) {
                        intent.putExtra("Name", "E3");
                    }
                    activity.startActivity(intent);
                }
            }

        });

        mLocationClient = new AMapLocationClient(activity.getApplicationContext());
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        myLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());//会一直进行定位并且更新
                        Log.v("成功获得自己的经纬度",myLatLng.latitude+" "+myLatLng.longitude);

                        if (isFirst){
                            aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(myLatLng.latitude, myLatLng.longitude), 17, 0, 0)));
                            isFirst = false;
                        }
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }else {
                    Log.e("WTF","WTF");
                }
            }
        });
        mLocationClient.startLocation();
        Location aMapLocation = aMap.getMyLocation();
        if (aMapLocation != null){
            Log.d("init MainMap", "aMapLocation被正常创建");
        }
//        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(myLatLng.latitude, myLatLng.longitude), 17, 0, 0)));
    }

    /**
     * 供主活动获得最近的食堂ID，方便进入对应的详情界面
     * @return
     */
    public static String getNearestID() {

        //找出最小距离的
        double minDistance = 9999999;
        LatLng nearestLatLng = null;
        for (int i = 0; i < locList.size(); i++) {
            LatLng l = locList.get(i);
            double temp = Math.abs(l.latitude- myLatLng.latitude) + Math.abs(l.longitude- myLatLng.longitude);
            if (temp < minDistance){
                minDistance = temp;
                nearestLatLng = l;
            }
        }
        int index = locList.indexOf(nearestLatLng);
        switch (index){
            case 0: return "E11";//TODO: 同距离如何评判？？
            case 1: return "E3";
        }
        return "E12";
    }

    public static void stopLoc(){
        mLocationClient.stopLocation();
    }


}
