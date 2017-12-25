package com.grayxu.husteating.background;

import com.grayxu.husteating.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/10/29.
 * 本类用来进行食堂相关集合数据的集中化管理
 */

public class Canteens {

    private static ArrayList<String> canteenIDs = new ArrayList<>(Arrays.asList("E11", "E12", "E3","Z"));
    private static ArrayList<String> canteenNames = new ArrayList<>(Arrays.asList("东一食堂一楼", "东一食堂二楼", "东三食堂", "紫荆园","集贤楼","西一食堂一楼"));
    private static ArrayList<Integer> buttonIDs = new ArrayList<>(Arrays.asList(R.id.buttonE11, R.id.buttonE12, R.id.buttonE3, R.id.buttonZ, R.id.buttonJX, R.id.buttonW1));//统一管理添加，方便遍历查找

    public static ArrayList<String> getCanteenNames() {
        return canteenNames;
    }

    public static ArrayList<String> getCanteenIDs() {
        return canteenIDs;
    }

    public static ArrayList<Integer> getButtonIDs() {
        return buttonIDs;
    }

}
