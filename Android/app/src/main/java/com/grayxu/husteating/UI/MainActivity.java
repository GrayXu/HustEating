package com.grayxu.husteating.UI;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.grayxu.husteating.background.DataManager;
import com.grayxu.husteating.R;

import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 本类为主活动，管理这个界面下多碎片的切换，以及后台的操作
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, EasyPermissions.PermissionCallbacks {

    private FragmentTransaction fragmentTransaction;
    private SettingFragment settingFragment;
    private MapFragment mapFragment;
    private AlertDialog.Builder builder;
    private static final int num = 123;

    private Toolbar toolbar;

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //动态申请所有所需要的敏感权限
        if (Build.VERSION.SDK_INT >= 23) {
            Log.i("onCreate", "系统为6.0及以上");
            requireSomePermission();
        }

        setContentView(R.layout.activity_main);

        initDrawer();//初始化抽屉

        initDB();//检查是否第一次打开本程序，若是则初始化数据库 (应该有权限后才能初始化？？

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
    private void initDialog() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("HUST Eating");
        builder.setMessage("Email:  grayxu@hust.edu.cn\n欢迎使用者反馈任何信息" + new String(Character.toChars(0x1F64F)));
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
    private void initDrawer() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        //初始化的时候载入toolBar的颜色
        String tasteChosen = preferences.getString("tasteChosen", "辣");
        String color = "0";
        if (tasteChosen.equals("辣")) {
            color = "#FF0000";
        } else if (tasteChosen.equals("清淡")) {
            color = "#BFEFFF";
        } else if (tasteChosen.equals("香")) {
            color = "#EEC900";
        } else if (tasteChosen.equals("甜")) {
            color = "#FFAEB9";
        }
        toolbar.setBackgroundColor(Color.parseColor(color));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);
        if (isLogin){
            View headerLayout = navigationView.inflateHeaderView(R.layout.activity_login);
            String name = sp.getString("name", null);
            String email = sp.getString("name", null);
            ((TextView) headerLayout.findViewById(R.id.TVmail)).setText(email);
            ((TextView) headerLayout.findViewById(R.id.TVname)).setText(name);

        }else{
            View headerLayout = navigationView.inflateHeaderView(R.layout.main_nav_header_nologin);
            Button butLogin = headerLayout.findViewById(R.id.buttonLogin);
            butLogin.setOnClickListener(new View.OnClickListener() {//登录按钮
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

        }
    }

    /**
     * 检查是否第一次打开本程序，若是则初始化数据库
     */
    private void initDB() {
        Log.i("initDB", "检查数据库");
        SharedPreferences preferences = getPreferences(0);
        boolean isFirst = preferences.getBoolean("isFirst", true);

        if (isFirst) {
            Log.i("initDB", "初始化数据库");
            try {
                DataManager.getInstance().init(this);
                preferences.edit().putBoolean("isFirst", false).apply();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        fragmentTransaction = getFragmentManager().beginTransaction();

        if (id == R.id.nav_map) {
            fragmentTransaction.show(mapFragment).hide(settingFragment);
            fragmentTransaction.commit();
            toolbar.setTitle("食堂地图");
        } else if (id == R.id.nav_setting) {
            fragmentTransaction.show(settingFragment).hide(mapFragment);
            fragmentTransaction.commit();
            toolbar.setTitle("用户设置");
        } else if (id == R.id.nav_share) {

            String link = getString(R.string.link);
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText("newPlainTextLabel", link));
            Toast.makeText(this, "已经复制下载链接到剪切板中", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_send) {
            builder.show();
        } else if (id == R.id.nav_message) {
            //做公告更新
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

    //以下为框架申请权限时用到的回调函数
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d("onPermissionsGranted", "onPermissionsGranted");
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d("onPermissionsDenied", "onPermissionsDenied");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(num)
    private void requireSomePermission() {
        String[] perms = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
        };
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            Log.i("requireSomePermission", "Permissions Granted!");
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "",
                    num, perms);
        }
    }
}