package com.grayxu.husteating.background;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import com.grayxu.husteating.UI.InfoActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Administrator on 2017/10/11.
 * 这是用来初始化地图的工具类
 */

public class MainMap {

    private static ArrayList<LatLng> locList;
    private static ArrayList<String> nameList;

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
        final AMap aMap = mapView.getMap();

        //批量添加位置，因为是位置，所以不好在外部做
        locList = new ArrayList<>(Arrays.asList(new LatLng(30.511150, 114.419072), new LatLng(30.510734, 114.420424)));

        nameList = new ArrayList<>(Arrays.asList(""));
        final ArrayList<Marker> markerList = new ArrayList<>(Arrays.asList(aMap.addMarker(new MarkerOptions().position(locList.get(0))),
                aMap.addMarker(new MarkerOptions().position(locList.get(1)))));

        final ArrayList<Integer> layoutIDs = new ArrayList<>(Arrays.asList(R.layout.window_e1,R.layout.window_e3));

        aMap.setMyLocationEnabled(true);
        initBlueDot(aMap);//初始化蓝点


        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            //TODO 重构到集合里
            View infoWindow;

            @Override
            public View getInfoWindow(Marker marker) {
                infoWindow = null;

                for (int i = 0; i < markerList.size(); i++) {
                    Marker markerTemp = markerList.get(i);
                    if (markerTemp.equals(marker)){
                        infoWindow = LayoutInflater.from(activity).inflate(layoutIDs.get(i), null);//对应的Layout inflate出来
                        break;
                    }
                }
                Iterator iterator = Canteens.getButtonIDs().iterator();
                while (iterator.hasNext()){
                    int id = (int) iterator.next();
                    Button buttonTemp = infoWindow.findViewById(id);//inflate 后就可以找控件加监听器了
                    if (buttonTemp != null){
                        buttonTemp.setOnClickListener(new CanteenButtonListen());
                    }
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
                    int viewInID = view.getId();

                    Intent intent = new Intent(activity, InfoActivity.class);

//                    Iterator iterator = buttonIDs.iterator();
                    Iterator iterator = Canteens.getButtonIDs().iterator();
                    while (iterator.hasNext()) {
                        int tempID = (int) iterator.next();
                        if (tempID == viewInID) {
                            intent.putExtra("Name", Canteens.getCanteenNames().get(Canteens.getButtonIDs().indexOf(tempID)));//直接打包食堂的名字，效率不高，但几十个绰绰有余（此处的入口是浮动窗口按钮
                            break;
                        }
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
                        Log.v("成功获得自己的经纬度", myLatLng.latitude + " " + myLatLng.longitude);

                        if (isFirst) {
                            aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(myLatLng.latitude, myLatLng.longitude), 17, 0, 0)));
                            isFirst = false;
                            borderCheck(myLatLng);
                        }

                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());

                    }
                } else {
                    Log.e("WTF", "WTF");
                }
            }
        });
        mLocationClient.startLocation();
        Location aMapLocation = aMap.getMyLocation();
        if (aMapLocation != null) {
            Log.d("init MainMap", "aMapLocation被正常创建");
        }
//        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(myLatLng.latitude, myLatLng.longitude), 17, 0, 0)));
        CameraUpdateFactory.zoomTo(19);
    }

    /**
     * 初始化蓝点
     */
    private static void initBlueDot(AMap aMap) {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类（即默认
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位，移动蓝点，但不移动镜头。


        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.getUiSettings().setCompassEnabled(true);
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

    /**
     * 判断是否用户的位置是否在华科内，否则在外部进行提醒
     *
     * @param latLngIn 传入的位置信息
     */
    private static void borderCheck(LatLng latLngIn) {
        double latitude = latLngIn.latitude;
        double longtitude = latLngIn.longitude;

        //这些magic numbers是华科的边界经纬度
        if (latitude > 30.5246628024 || latitude < 30.5029695646 || longtitude > 114.4415760040 || longtitude < 114.3969869614) {
            Toast.makeText(activity, "您怕是不在华科范围内，本应用目前只面向华科食堂开放。", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 供主活动获得最近的食堂ID，方便进入对应的详情界面
     *
     * @return
     */
    public static String getNearestID() {

        //找出最小距离的
        double minDistance = 9999999;
        LatLng nearestLatLng = null;
        for (int i = 0; i < locList.size(); i++) {
            LatLng l = locList.get(i);
            if (myLatLng == null) {
                Log.d("getNearestID", "myLatLng is null");
            } else if (l == null) {
                Log.d("getNearestID", "l is null");
            } else {
                double temp = Math.abs(l.latitude - myLatLng.latitude) + Math.abs(l.longitude - myLatLng.longitude);
                if (temp < minDistance) {
                    minDistance = temp;
                    nearestLatLng = l;
                }
            }
        }

        int index = locList.indexOf(nearestLatLng);
        //TODO: 同距离评判逻辑？？需要新的推荐逻辑
        switch (index) {
            case 0:
                return "E11";
            case 1:
                return "E3";
        }
        return "E12";
    }

    public static void stopLoc() {
        mLocationClient.stopLocation();
    }

}
