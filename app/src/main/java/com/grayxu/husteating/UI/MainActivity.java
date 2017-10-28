package com.grayxu.husteating.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.amap.api.maps.MapView;
import com.grayxu.husteating.background.DataManager;
import com.grayxu.husteating.background.MainMap;
import com.grayxu.husteating.R;

import java.io.IOException;

/**
 * 主界面，即一开始进入的地图界面，进行食堂的初始选择
 */
public class MainActivity extends AppCompatActivity {

    MapView mapView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。
            MainMap.init(mapView, this);
        }

        initDB();//检查是否第一次打开本程序，若是则初始化数据库
        initFastEat();//初始化主页浮动按钮

    }

    /**
     * 检查是否第一次打开本程序，若是则初始化数据库
     */
    private void initDB() {
        SharedPreferences preferences = getPreferences(0);
        boolean isFirst = preferences.getBoolean("isFirst", false);
        if (isFirst) {
            try {
                DataManager.getDataManger().init(this);
                preferences.edit().putBoolean("isFirst", false).apply();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化主界面的浮动按钮，提供快速选择的功能
     */
    private void initFastEat() {
        findViewById(R.id.ButtonFastEat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nearestID = MainMap.getNearestID();
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                intent.putExtra("Name", nearestID);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
        MainMap.stopLoc();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

}
