package com.grayxu.husteating.background;

/**
 * Created by Administrator on 2017/10/28.
 */

import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
     * @param timeNow     现在的时间
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

        Log.v("getResult", "当前的时间是" + timeNow);

//        timeNow = 1200; //TODO: 开发过程中，在此处手动调整TimeNow的值
        int moneyMax = -1;
        if (930 > timeNow && timeNow > 650) {
            //早餐时间
            Log.i("getResult", "吃早餐");
            moneyMax = preferences.getInt("moneyBreakfastChosen", 5);

        } else if (1300 > timeNow && timeNow > 1050) {
            //午餐时间
            Log.i("getResult", "吃午餐");
            moneyMax = preferences.getInt("moneyLunchChosen", 10);

        } else if (1900 > timeNow && timeNow > 1650) {
            //晚餐时间
            Log.i("getResult", "吃晚餐");
            moneyMax = preferences.getInt("moneyDinnerChosen", 12);

        } else {//不是饭点时间
            allFoodList.clear();
        }

        List<Food> recommandFoodList = new ArrayList<>();
        if (moneyMax != -1) { //TODO: 推荐算法实现位置
//            Iterator<Food> i = allFoodList.iterator();
            boolean haveStable = false;
            int moneySpend = 0;
//            while (i.hasNext()){
//                Food food = i.next();
//                food.getIsStaple();
//            }
//            Random random = new Random();
//            random.nextInt(allFoodList.size());

            ArrayList<Integer> indexList = getRandomIndex(allFoodList.size());
            for (Integer i :
                    indexList) {

                Food food = allFoodList.get(i);
                if (food.getIsStaple() == 1) {//是主食
                    if (!haveStable) {
                        recommandFoodList.add(food);
                        moneySpend += food.getPrice();
                    }
                } else {
                    if (food.getPrice() < moneyMax - moneySpend){
                        recommandFoodList.add(food);
                    }
                }

            }

        }

        return recommandFoodList;
    }

    /**
     * 返回已经确定要吃的东西,并存入历史记录中
     *
     * @param foodList
     */
    public void confirmFood(ArrayList<Food> foodList) {
        //update local database
    }

    private ArrayList<Integer> getRandomIndex(int size) {
        ArrayList randomNums = new ArrayList();
        for (int i = 0; i < size; i++) {
            randomNums.add(i);
        }
        Collections.shuffle(randomNums);
        return randomNums;
    }

}
