package com.grayxu.husteating.background;

/**
 * Created by Administrator on 2017/10/19.
 */

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

    private DataManager(){

    }

    //单例模式的实现
    public static DataManager getDataManger() throws IOException {
        DataManager dataManager = new DataManager();
        Connector.getDatabase();
//        dataManager.init();
        return dataManager;
    }

    //这个静态变量存储的是所有食堂的数据
    //键就是食堂的代号，值就是食堂里面所有菜肴的信息（一堆包含菜的属性的字符串数组储存在ArrayList中）
    private HashMap<String, ArrayList> infoMap = new HashMap<>();

    /**
     * 存储数据进入InfoMap这个HashMap的操作逻辑
     * @param activity 本参数传入以便获得raw文本数据（可能传入流的引用更符合设计模式
     * @throws IOException
     */
    public void init(MainActivity activity) throws IOException {

        InputStream inputStream = activity.getResources().openRawResource(R.raw.foods);
        Reader reader = new InputStreamReader(inputStream);
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list = csvReader.readAll();

        for (String[] strs : list) {

            if (strs != null) {//防null
                String title = strs[1];
                ArrayList<String[]> canteenInfoList = infoMap.get(title);
                if (infoMap.get(title) != null) {
                    canteenInfoList.add(strs);
                } else {
                    ArrayList<String[]> tempList = new ArrayList<>();
                    tempList.add(strs);
                    infoMap.put(title, new ArrayList(tempList));
                }
            }

        }
        addDataAll();//把HashMap中的食物属性信息存入LiteSQL数据库中
    }

    /**
     * 把HashMap中的食物属性信息存入LiteSQL数据库中
     */
    private void addDataAll(){
        Iterator iteratorMap = infoMap.entrySet().iterator();
        while(iteratorMap.hasNext()){
            Map.Entry entry = (Map.Entry) iteratorMap.next();
            String canteenName = (String) entry.getKey();
            ArrayList<String[]> foodList = (ArrayList<String[]>) entry.getValue();
            Iterator iteratorList = foodList.iterator();

            while (iteratorList.hasNext()){//遍历这个ArrayList
                String[] strs = (String[]) iteratorList.next();
                Food food = new Food();
                food.setCanteen(canteenName);
                food.setName(strs[1]);
                food.setPrice(Float.valueOf(strs[2]));
                food.setMeatIndex(Integer.valueOf(strs[3]));
                food.setTaste(strs[4]);
                food.save();//保存这张表
            }
        }
    }

    /**
     * 外部通过本方法来获得指定餐厅的食物列表（经过LitePal完美的包装
     * @param canteenName 餐厅的名字
     * @return
     */
    public List<Food> getFoodsList(String canteenName){
        return DataSupport.select("canteen", canteenName).find(Food.class);
    }

}
