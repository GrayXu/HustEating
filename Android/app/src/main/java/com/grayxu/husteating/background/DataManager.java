package com.grayxu.husteating.background;

/**
 * Created by Administrator on 2017/10/19.
 */

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.csvreader.CsvReader;
import com.grayxu.husteating.R;
import com.grayxu.husteating.UI.MainActivity;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 本类负责创建LitePal数据库，创建对应不同食堂的食物属性表。
 * 第一次启动本程序的时候进行初始化，从csv中获取数据，存入数据库，从而也方便外界去更改
 */
public class DataManager {


    private DataManager() {
    }

    private static class InnerHelper {
        private final static DataManager dataManager = new DataManager();
    }

    //单例模式的实现
    public static DataManager getInstance() throws IOException {

        Connector.getDatabase();
//        dataManager.init();
        return InnerHelper.dataManager;
    }

    /**
     * 存储数据进入InfoMap这个HashMap的操作逻辑
     *
     * @param activity 本参数传入以便获得raw文本数据（可能传入流的引用更符合设计模式
     * @throws IOException
     */
    public void init(MainActivity activity) throws IOException {
        Connector.getDatabase();
        InputStream inputStream = activity.getResources().openRawResource(R.raw.foods);
        CsvReader csvReader = new CsvReader(new InputStreamReader(inputStream));
        ArrayList<String[]> list = new ArrayList<>();
        while (csvReader.readRecord()) {//将csv数据读取到列表中
            list.add(csvReader.getValues());
        }

        csvReader.close();
        inputStream.close();
        addDataAll(list);//把HashMap中的食物属性信息存入LiteSQL数据库中
    }

    /**
     * 把参数中的食物属性信息存入LiteSQL数据库中
     *
     * @param strsList 本质上是一个二维数组，从而把数据存入表中
     */
    private void addDataAll(ArrayList<String[]> strsList) {
        Iterator iterator = strsList.iterator();
        while (iterator.hasNext()) {
            String[] strs = (String[]) iterator.next();
            Food food = new Food();
            food.setCanteen(strs[0]);
            food.setName(strs[1]);
            food.setPrice(Float.valueOf(strs[2]));
            food.setMeatIndex(Integer.valueOf(strs[3]));
            food.setHotIndex(Integer.valueOf(strs[4]));
            food.setTaste(strs[5]);
            food.setIsStaple(Integer.valueOf(strs[6]));
            if (!food.save()) {//若保存不成功
                Log.w("addDataAll", "数据保存失败");
            } else {
                Log.v("addDataAll", food.getName() + "保存成功");
            }
        }
    }

    /**
     * 外部通过本方法来获得指定餐厅的食物列表（经过LitePal完美的包装
     *
     * @param canteenID 传入餐厅的ID
     * @return 返回该餐厅ID对应的食物List
     */
    public List<Food> getFoodsList(String canteenID) {

        //返回指定餐厅的所有事物，通过DataManager内部的推荐算法完成具体的推荐内容
        List listAll = DataSupport.where("canteen = ?", canteenID).find(Food.class);
        Log.i("getFoodsList", "查询了餐厅ID为" + canteenID + "的食物列表，食物数量为" + listAll.size());

        return listAll;
    }

}
