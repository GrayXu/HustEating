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
    private final static PushManager pushManager = new PushManager();

    private PushManager() {
    }

    /**
     * 外界传入SP，让内部可以获得键值对信息
     *
     * @param preferences
     */
    public static void initSP(SharedPreferences preferences) {
        pushManager.preferences = preferences;
    }


    public static PushManager getInstance() {
        return pushManager;
    }

    /**
     * 获得推荐的结果
     *
     * @param allFoodList 传入的所有符合条件的食物
     * @param timeNow 现在的时间
     *
     * @return 最后确定推荐的食物列表
     */
    public List<Food> getResult(List<Food> allFoodList, int timeNow) {

        //key为当前推荐的日期，方便进行存储今天记录。格式为20171102
        Calendar c = Calendar.getInstance();
        final String TodayKey = String.valueOf(c.get(Calendar.YEAR)) + String.valueOf(c.get(Calendar.MONTH)) + String.valueOf(c.get(Calendar.DATE));
        c.add(c.DATE, 1);
        final String YesterdayKey = String.valueOf(c.get(Calendar.YEAR)) + String.valueOf(c.get(Calendar.MONTH)) + String.valueOf(c.get(Calendar.DATE));
        c.add(c.DATE, 1);
        final String BeforeYKey = String.valueOf(c.get(Calendar.YEAR)) + String.valueOf(c.get(Calendar.MONTH)) + String.valueOf(c.get(Calendar.DATE));

        int totalMeat = preferences.getInt(TodayKey, 0) + preferences.getInt(YesterdayKey, 0) + preferences.getInt(BeforeYKey, 0);//仅三天的吃量
        String taste = preferences.getString("tasteChosen", "辣");
        //MeatIndex的上限设置为20，尽量不会让其超过

        //TODO: 推荐算法实现位置
        Log.i("getResult", "当前的时间是" + timeNow);

        timeNow = 700; //TODO: 开发过程中，在此处手动调整TimeNow的值

        if (930 > timeNow && timeNow > 650){
            //早餐时间
            Log.i("getResult", "吃早餐");
            int moneyMax = preferences.getInt("moneyBreakfastChosen", 5);

        } else if (1300 > timeNow && timeNow > 1050){
            //午餐时间
            Log.i("getResult", "吃午餐");
            int moneyMax = preferences.getInt("moneyLunchChosen", 10);

        } else if (1900 > timeNow && timeNow > 1650) {
            //晚餐时间
            Log.i("getResult", "吃晚餐");
            int moneyMax = preferences.getInt("moneyDinnerChosen", 12);

        } else {//不是饭点时间
            allFoodList.clear();
        }

        return allFoodList;
    }

    /**
     * 返回已经确定要吃的东西
     *
     * @param foodList
     */
    public void confirmFood(ArrayList<Food> foodList) {
        //update local database
    }
}
