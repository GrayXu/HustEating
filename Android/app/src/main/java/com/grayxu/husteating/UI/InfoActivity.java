package com.grayxu.husteating.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grayxu.husteating.R;
import com.grayxu.husteating.background.Canteens;
import com.grayxu.husteating.background.DataManager;
import com.grayxu.husteating.background.Food;
import com.grayxu.husteating.background.PushManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class InfoActivity extends AppCompatActivity {

    private String canteenID;
    private FoodAdapter foodAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Intent intent = getIntent();
        canteenID = intent.getStringExtra("Name");//餐厅的ID
        Log.d("info onCreate","此时餐厅ID为"+canteenID+",它的index为"+Canteens.getCanteenIDs().indexOf(canteenID));
        String canteenName = Canteens.getCanteenNames().get(Canteens.getCanteenIDs().indexOf(canteenID));//餐厅的名字

        ((TextView) findViewById(R.id.titleTV)).setText(canteenName);

        PushManager.initSP(getSharedPreferences("MainActivity", MODE_PRIVATE));//方便内部获得键值对的信息
        List<Food> foodsResult = null;
        try {
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            foodsResult = PushManager.getInstance().getResult(DataManager.getInstance().getFoodsList(canteenID), hour * 100 + minute);//获得最后推荐的结果
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (foodsResult != null) {
            //TODO: 将结果输出到界面中
            int size = foodsResult.size();
            if (size == 0) {
                //不是饭点，没得推荐
                Toast.makeText(this, "现在已经不是饭点，食堂已经关门了", Toast.LENGTH_SHORT).show();
            } else {
                show(foodsResult);//展示推荐的食物信息
            }
        }
    }


    //展示推荐的食物信息
    private void show(List<Food> foods){

        //初始化布局管理器
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        foodAdapter = new FoodAdapter(foods);
        recyclerView.setAdapter(foodAdapter);//适配器装载

    }

}
