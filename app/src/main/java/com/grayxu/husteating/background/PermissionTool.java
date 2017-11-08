package com.grayxu.husteating.background;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.grayxu.husteating.UI.MainActivity;

/**
 * Created by Administrator on 2017/11/8.
 */

public class PermissionTool {

    private static Activity activity;
    private static int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 100;
    private static int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 101;
    private static int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 102;

    public static void init(Activity activityIn) {
        activity = activityIn;
    }

    /**
     * 请求权限后回调监听函数的实际执行部分
     * @param requestCode
     * @param grantResults
     */
    public static void afterRequest(int requestCode, int[] grantResults) {
//        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //同意权限,做跳转逻辑
//                Log.i("doNext", "同意权限");
//            } else {
//                //权限拒绝，提示用户开启权限，并关闭当前活动
//                Log.d("doNext", "权限拒绝，提示用户开启权限");
//                Toast.makeText(activity, "因为定位权限未开启故关闭程序", Toast.LENGTH_SHORT).show();
//                activity.finish();
//            }
//        }
//        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //同意权限,做跳转逻辑
//                Log.i("doNext", "同意权限");
//            } else {
//                //权限拒绝，提示用户开启权限，并关闭当前活动
//                Log.i("doNext", "权限拒绝，提示用户开启权限");
//                Toast.makeText(activity, "因为文件权限未开启故关闭程序", Toast.LENGTH_SHORT).show();
//                activity.finish();
//            }
//        }
//        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //同意权限,做跳转逻辑
//                Log.i("doNext", "同意权限,做跳转逻辑");
//            } else {
//                //权限拒绝，提示用户开启权限，并关闭当前活动
//                Log.i("doNext", "权限拒绝，提示用户开启权限");
//                Toast.makeText(activity, "因为获取手机权限未开启故关闭程序", Toast.LENGTH_SHORT).show();
//                activity.finish();
//            }
//        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //同意权限,做跳转逻辑
            Log.i("doNext", "同意权限");
        } else {
            //权限拒绝，提示用户开启权限，并关闭当前活动
            Log.d("doNext", "权限拒绝，提示用户开启权限");
            Toast.makeText(activity, "因为权限未授权故关闭程序", Toast.LENGTH_SHORT).show();
            activity.finish();
        }

//        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION || requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE || requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //同意权限,做跳转逻辑
//                Log.i("doNext", "同意权限");
//            } else {
//                //权限拒绝，提示用户开启权限，并关闭当前活动
//                Log.d("doNext", "权限拒绝，提示用户开启权限");
//                Toast.makeText(activity, "因为权限未授权故关闭程序", Toast.LENGTH_SHORT).show();
//                activity.finish();
//            }
//        }
    }

    /**
     * 检查是否拥有权限，否则进行申请，申请失败则Toast推送原因并直接退出程序。
     */
    public static void checkPermission() {
        boolean getLoc = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean getStorage = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean getState = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;

        if (!getLoc || !getState || !getStorage) {
            Log.i("checkPermission", "有权限没有获得");
            Toast.makeText(activity, "为了程序正常运行，我们需要获得定位等权限的授权", Toast.LENGTH_LONG).show();
        }

        if (!getLoc) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                Toast.makeText(activity, "我们需要定位权限", Toast.LENGTH_LONG).show();
            } else {

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                Log.i("checkPermission", "尝试获得定位权限");
            }
        }

        if (!getStorage) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Toast.makeText(activity, "我们需要文件读写权限", Toast.LENGTH_SHORT).show();
            } else {

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                Log.i("checkPermission", "尝试获得文件权限");
            }
        }

        if (!getState) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_PHONE_STATE)) {

                Toast.makeText(activity, "因为需要识别手机，所以我们需要读取手机状态的权限", Toast.LENGTH_SHORT).show();
            } else {

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                Log.i("checkPermission", "尝试获得手机状态权限");
            }
        }
    }


}
