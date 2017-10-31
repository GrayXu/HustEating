package com.grayxu.husteating.UI;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.MapView;
import com.grayxu.husteating.R;
import com.grayxu.husteating.background.MainMap;

/**
 * Created by Administrator on 2017/10/28.
 */

public class MapFragment extends Fragment{

    private View view;
    private MapView mapView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_map, container, false);
        mapView = view.findViewById(R.id.map);
        initFastEat(view);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。
            MainMap.init(mapView, getActivity());
        }
        return view;
    }

    /**
     * 初始化主界面的浮动按钮，提供快速选择的功能
     */
    private void initFastEat(View view) {
        view.findViewById(R.id.ButtonFastEat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nearestID = MainMap.getNearestID();
                Intent intent = new Intent(getActivity(), InfoActivity.class);
                intent.putExtra("Name", nearestID);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
        MainMap.stopLoc();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
}

