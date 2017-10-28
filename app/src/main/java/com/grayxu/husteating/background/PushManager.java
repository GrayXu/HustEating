package com.grayxu.husteating.background;

/**
 * Created by Administrator on 2017/10/28.
 */

import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 本类负责推荐信息的输出，以及推荐后的衍生操作（包括存储历史记录）
 */
public class PushManager {

    private SharedPreferences preferences;
    private List<Food> allFoodList;

    private PushManager() {
    }

    public static PushManager getPushManager(SharedPreferences preferences, List<Food> allFoodList) {
        PushManager pushManager = new PushManager();
        pushManager.preferences = preferences;
        pushManager.allFoodList = allFoodList;
        return pushManager;
    }

    public ArrayList<Food> getResult() {
        ArrayList<Food> foodList = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        //key为当前推荐的日期，方便进行存储今天记录。
        final String TodayKey = String.valueOf(c.get(Calendar.YEAR)) + String.valueOf(c.get(Calendar.MONTH)) + String.valueOf(c.get(Calendar.DATE));
        c.add(c.DATE, 1);
        final String YesterdayKey = String.valueOf(c.get(Calendar.YEAR)) + String.valueOf(c.get(Calendar.MONTH)) + String.valueOf(c.get(Calendar.DATE));
        c.add(c.DATE, 1);
        final String BeforeYKey = String.valueOf(c.get(Calendar.YEAR)) + String.valueOf(c.get(Calendar.MONTH)) + String.valueOf(c.get(Calendar.DATE));
        Log.d("检测string key", TodayKey + " " + YesterdayKey + " " + BeforeYKey);

        SharedPreferences.Editor editor = preferences.edit();
        int totalMeat = preferences.getInt(TodayKey, 0) + preferences.getInt(YesterdayKey, 0) + preferences.getInt(BeforeYKey, 0);
        //MeatIndex的上限设置为20，尽量不会让其超过



        return foodList;
    }

    /**
     * 返回已经确定要吃的东西
     * @param foodList
     */
    public void confirmFood(ArrayList<Food> foodList){
        //update data
    }
}
