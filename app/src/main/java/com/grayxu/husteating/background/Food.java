package com.grayxu.husteating.background;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/10/19.
 */

public class Food extends DataSupport {

    private String canteen;//属于哪个餐厅
    private String name;//名称
    private float price;//价格
    private int meatIndex;//肉指数 5分满分
    private String taste;//口味
    private int hotIndex;//热门指数（越高越优先）5分满分
    private boolean isStaple;//是否为主食

    public void setCanteen(String canteen) {
        this.canteen = canteen;
    }

    public void setStaple(boolean staple) {
        isStaple = staple;
    }

    public boolean isStaple() {

        return isStaple;
    }

    public String getCanteen() {
        return canteen;
    }

    public void setHotIndex(int hotIndex) {
        this.hotIndex = hotIndex;
    }

    public int getHotIndex() {
        return hotIndex;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public int getMeatIndex() {
        return meatIndex;
    }

    public String getTaste() {
        return taste;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setMeatIndex(int meatIndex) {
        this.meatIndex = meatIndex;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }


}
