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
import com.amap.api.maps.UiSettings;
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
import java.util.Random;

/**
 * Created by Administrator on 2017/10/11.
 * 这是用来初始化地图的工具类
 */

public class MainMap {

    private static ArrayList<LatLng> locList = new ArrayList<>(Arrays.asList(new LatLng(30.511150, 114.419072), new LatLng(30.510734, 114.420424)));//批量添加位置
    //loclist里面保存的依次为东一食堂，东三食堂。
    private static String[][] canteenNames = new String[][]{
            new String[]{"E11", "E12"},
            new String[]{"E3"}};


    private static Activity activity;
    private static LatLng myLatLng;
    private static AMapLocationClient mLocationClient;
    private static boolean isFirst = true;//用来进行刚打开的时候进行地图镜头放大
    private static AMap aMap = null;

    /**
     * 作为工具类的初始方法，必须最早调用
     *
     * @param mapView    需要初始化的地图控件
     * @param activityIn 运行的背景
     */
    public static void init(final MapView mapView, final Activity activityIn) {
        activity = activityIn;
        aMap = mapView.getMap();

        final ArrayList<Marker> markerList = new ArrayList<>(Arrays.asList(
                aMap.addMarker(new MarkerOptions().position(locList.get(0))),
                aMap.addMarker(new MarkerOptions().position(locList.get(1)))));//Marker的列表

        final ArrayList<Integer> layoutIDs = new ArrayList<>(Arrays.asList(R.layout.window_e1, R.layout.window_e3));//Marker对应的layout文件的列表

        aMap.setMyLocationEnabled(true);
        initBlueDot(aMap);//初始化蓝点


        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            View infoWindow;

            @Override
            public View getInfoWindow(Marker marker) {
                infoWindow = null;

                for (int i = 0; i < markerList.size(); i++) {
                    Marker markerTemp = markerList.get(i);
                    if (markerTemp.equals(marker)) {
                        infoWindow = LayoutInflater.from(activity).inflate(layoutIDs.get(i), null);//对应的Layout inflate出来
                        break;
                    }
                }
                Iterator iterator = Canteens.getButtonIDs().iterator();
                while (iterator.hasNext()) {
                    int id = (int) iterator.next();
                    Button buttonTemp = infoWindow.findViewById(id);//inflate 后就可以找控件加监听器了
                    if (buttonTemp != null) {
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
                            moveCamera();
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
     * 外部也需要移动镜头，故留出本函数。当然这么做的前提是要已经初始化地图了
     */
    public static void moveCamera() {
        if (myLatLng == null) {
            Toast.makeText(activity, "没有获取到你的位置信息", Toast.LENGTH_SHORT).show();
        } else {
            aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(myLatLng.latitude, myLatLng.longitude), 17, 0, 0)));
        }
    }

    /**
     * 初始化蓝点
     */
    private static void initBlueDot(AMap aMap) {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类（即默认
        myLocationStyle.interval(60000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位，移动蓝点，但不移动镜头。

        UiSettings uiSettings = aMap.getUiSettings();
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        uiSettings.setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        uiSettings.setCompassEnabled(true);//开启指南针
        uiSettings.setZoomControlsEnabled(false);//关闭定位键，才能有自定义的定位键
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
            Toast.makeText(activity, "怕是您不在华科范围内，本应用目前只面向华科食堂开放。", Toast.LENGTH_LONG).show();
            activity.finish();//TODO： 可以做一个fake location 方便外校进行体验
        }
    }

    /**
     * 供主活动获得推荐的食堂ID，方便数据库进行检索
     *
     * @return 餐厅的ID，比如E11 E12 E3等
     */
    public static String getCanteenResultID() {


        double distanceMin1 = 99998;//最近的食堂距离
        double distanceMin2 = 99999;//第二近的食堂距离

        int indexMin1 = -1;
        int indexMin2 = -1;

        for (int i = 0; i < locList.size(); i++) {
            LatLng l = locList.get(i);
            if (myLatLng == null) {
                Log.e("getCanteenResultID", "myLatLng is null");
            } else if (l == null) {
                Log.e("getCanteenResultID", "l is null");
            } else {
                double distanceNow = Math.abs(l.latitude - myLatLng.latitude) + Math.abs(l.longitude - myLatLng.longitude);//遍历的这个食堂的距离
                if (distanceNow < distanceMin2) {
                    if (distanceNow < distanceMin1) {//如果比两个最小值都要小
                        distanceMin2 = distanceMin1;
                        distanceMin1 = distanceNow;
                        indexMin2 = indexMin1;
                        indexMin1 = i;
                    } else {//夹与最小的与第二小的之间
                        distanceMin2 = distanceNow;
                        indexMin2 = i;
                    }
                }
            }
        }

        Log.d("getCanteenResultID", "获得的最近两个餐厅的Index为" + indexMin1 + "和" + indexMin2);
        if (indexMin1 == -1 || indexMin2 == -1) {
            return null;
        }
        ArrayList<String> namesNear = new ArrayList<>();

        for (int i = 0; i < canteenNames[indexMin1].length; i++) {
            namesNear.add(canteenNames[indexMin1][i]);
        }
        for (int i = 0; i < canteenNames[indexMin2].length; i++) {
            namesNear.add(canteenNames[indexMin2][i]);

        }
        //同距离评判逻辑暂时为随机摇筛子
        Random random = new Random();
        int randomNum = random.nextInt(namesNear.size());
        return namesNear.get(randomNum);
    }

    public static void stopLoc() {
        mLocationClient.stopLocation();
    }

}
