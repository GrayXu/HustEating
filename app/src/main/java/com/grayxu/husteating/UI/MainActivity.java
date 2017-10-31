package com.grayxu.husteating.UI;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.grayxu.husteating.background.DataManager;
import com.grayxu.husteating.background.MainMap;
import com.grayxu.husteating.R;

import java.io.IOException;

/**
 * 本类为主活动，管理这个界面下多碎片的切换，以及后台的操作
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentTransaction fragmentTransaction;
    private SettingFragment settingFragment;
    private MapFragment mapFragment;
    private AlertDialog.Builder builder;

    private Toolbar toolbar;
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDrawer();//初始化抽屉
        initDB();//检查是否第一次打开本程序，若是则初始化数据库
        initDialog();//初始化制作者信息包括反馈方式的弹出框
        fragmentTransaction = getFragmentManager().beginTransaction();
        mapFragment = new MapFragment();
        settingFragment = new SettingFragment();
        fragmentTransaction.add(R.id.fragment_content, mapFragment, "MAP");
        fragmentTransaction.add(R.id.fragment_content, settingFragment, "SETTING");
        fragmentTransaction.show(mapFragment).hide(settingFragment);
        fragmentTransaction.commit();

        toolbar.setTitle("食堂地图");
    }

    /**
     * 初始化制作者信息包括反馈方式的弹出框
     */
    private void initDialog(){
        builder = new AlertDialog.Builder(this);
        builder.setTitle("HUST Eating");
        builder.setMessage("Email:  grayxu@hust.edu.cn\n欢迎使用者反馈任何信息"+new String(Character.toChars(0x1F64F)));
        builder.setCancelable(false);
        builder.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    /**
     * 初始化抽屉布局
     */
    private void initDrawer(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String tasteChosen = preferences.getString("tasteChosen", "辣");
        int color = 0;
        if (tasteChosen.equals("辣")){
            color = 16711680;
        } else if (tasteChosen.equals("清淡")){
            color = 12578815;
        } else if (tasteChosen.equals("香")){
            color = 15649024;
        } else if (tasteChosen.equals("甜")){
            color = 16756409;
        }
        toolbar.setBackgroundColor(color);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        fragmentTransaction = getFragmentManager().beginTransaction();

        if (id == R.id.nav_map){
            fragmentTransaction.show(mapFragment).hide(settingFragment);
            fragmentTransaction.commit();
            toolbar.setTitle("食堂地图");
        } else if (id == R.id.nav_setting) {
            fragmentTransaction.show(settingFragment).hide(mapFragment);
            fragmentTransaction.commit();
            toolbar.setTitle("用户设置");
        } else if (id == R.id.nav_share) {
            //TODO: 给复制个下载链接
        } else if (id == R.id.nav_send) {
            builder.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
