package com.grayxu.husteating;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

/**
 * Created by Administrator on 2017/10/11.
 * 这是用来初始化地图的工具类
 */

public class MainMap {

    public static void init(final MapView mapView, final Activity activity){

        AMap aMap = mapView.getMap();
        aMap.setMyLocationEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));

        //控制蓝点逻辑
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类（即默认
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        //初始化Marker（应该做批量设置
        LatLng latLngEastOne = new LatLng(30.511150,114.419072);
        final Marker markerEastOne = aMap.addMarker(new MarkerOptions().position(latLngEastOne).title("东一食堂").snippet("两层餐厅"));

        LatLng latLngEastThree = new LatLng(30.510734,114.420424);
        final Marker markerEastThree = aMap.addMarker(new MarkerOptions().position(latLngEastThree).title("北京").snippet("DefaultMarker"));

        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                // 这里做外部信息收集的工具（判断是哪个marker点击进入的即可）
                Intent intent = new Intent(activity, DetailActivity.class);
                if (marker.equals(markerEastOne)){
                    intent.putExtra("CanteenNums", 2);
                    intent.putExtra("Name", "东一食堂");
                }
                if (marker.equals(markerEastThree)){
                    intent.putExtra("CanteenNums", 1);
                    intent.putExtra("Name", "东三清真食堂");
                }
                activity.startActivity(intent);
            }
        });

//        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                if (marker.equals(markerEastThree)){
//                    Log.d("onMarkerClick", "东三食堂被按");
//                    activity.startActivity(new Intent(activity, DetailActivity.class));
//                }
//                if (marker.equals(markerEastOne)){
//                    Log.d("onMarkerClick", "东一食堂被按");
//                    activity.startActivity(new Intent(activity, DetailActivity.class));
//                }
//                return false;
//            }
//        });

    }

}
