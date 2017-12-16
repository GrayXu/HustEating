package com.grayxu.husteating.UI;

/**
 * Created by Administrator on 2017/12/17.
 */

import android.app.Activity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * 本类用来保存多个活动的引用从而进行手动管理
 * 主要功能为终结splash，终结register and login。
 */
public class ActivityManager {

    private HashMap<String, Activity> activityHashMap;

    private static ActivityManager manager = new ActivityManager();

    public static ActivityManager getActivityManager(){
        return manager;
    }

    /**
     * 为了能够结束splash和login and register，需要添加引用
     * @param name
     * @param newActivity
     */
    public void addActivity(String name, Activity newActivity){
        activityHashMap.put(name, newActivity);
    }

    /**
     * kill splash activity
     */
    public void finishSplash(){
        Activity victim = activityHashMap.remove("SplashActivity");
        if (victim != null){
            victim.finish();
        }
    }

    /**
     * kill login and register activity
     */
    public void finishLoginRegister(){
        Activity login = activityHashMap.remove("LoginActivity");
        Activity register = activityHashMap.remove("RegisterActivity");
        if (login != null){
            login.finish();
        }
        if (register != null){
            register.finish();
        }
    }

}
