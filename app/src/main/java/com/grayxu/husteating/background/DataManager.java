package com.grayxu.husteating.background;

/**
 * Created by Administrator on 2017/10/19.
 */

import android.util.Log;

import com.grayxu.husteating.R;
import com.grayxu.husteating.UI.MainActivity;
import com.opencsv.CSVReader;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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

    //这个静态变量存储的是所有食堂的数据
    //键就是食堂的代号，值就是食堂里面所有菜肴的信息（一堆包含菜的属性的字符串数组储存在ArrayList中）
    //内部结构为 键:食堂ID 值:一个二维数组(内部保存多组食物数据)
    private HashMap<String, ArrayList> infoMap = new HashMap<>();

    /**
     * 存储数据进入InfoMap这个HashMap的操作逻辑
     *
     * @param activity 本参数传入以便获得raw文本数据（可能传入流的引用更符合设计模式
     * @throws IOException
     */
    public void init(MainActivity activity) throws IOException {

        InputStream inputStream = activity.getResources().openRawResource(R.raw.foods);
        Reader reader = new InputStreamReader(inputStream);
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list = csvReader.readAll();//读取csv里面的数据

        for (String[] strs : list) {

            if (strs != null) {//防null
                String title = strs[1];
                ArrayList<String[]> canteenInfoList = infoMap.get(title);
                if (canteenInfoList != null) {
                    canteenInfoList.add(strs);
                } else {//第一次添加这个餐厅的信息
                    ArrayList<String[]> newList = new ArrayList<>();
                    newList.add(strs);
                    infoMap.put(title, newList);
                }
            }

        }
        addDataAll();//把HashMap中的食物属性信息存入LiteSQL数据库中
        inputStream.close();
    }

    /**
     * 把HashMap中的食物属性信息存入LiteSQL数据库中
     */
    private void addDataAll() {
        Iterator iteratorMap = infoMap.entrySet().iterator();
        while (iteratorMap.hasNext()) {
            Map.Entry entry = (Map.Entry) iteratorMap.next();
            String canteenID = (String) entry.getKey();
            ArrayList<String[]> foodList = (ArrayList<String[]>) entry.getValue();
            Iterator iteratorList = foodList.iterator();
            Log.i("DataManager Init", "添加餐厅"+canteenID+"的信息");
            while (iteratorList.hasNext()) {//遍历这个ArrayList
                String[] strs = (String[]) iteratorList.next();
                Food food = new Food();
                //一系列数据保存
                food.setCanteen(canteenID);
                food.setName(strs[1]);
                food.setPrice(Float.valueOf(strs[2]));
                food.setMeatIndex(Integer.valueOf(strs[3]));
                food.setHotIndex(Integer.valueOf(strs[4]));
                food.setTaste(strs[5]);
                food.setIsStaple(Integer.valueOf(strs[6]));
                food.save();//保存这张表
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
        Log.d("getFoodsList", "想要查询餐厅ID为"+canteenID+"的食物列表");
        return DataSupport.select("canteen", canteenID).find(Food.class);
    }

}
