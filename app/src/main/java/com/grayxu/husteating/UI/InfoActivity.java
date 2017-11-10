package com.grayxu.husteating.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.grayxu.husteating.R;
import com.grayxu.husteating.background.Canteens;
import com.grayxu.husteating.background.DataManager;
import com.grayxu.husteating.background.Food;
import com.grayxu.husteating.background.PushManager;

import java.io.IOException;
import java.util.List;

public class InfoActivity extends AppCompatActivity {

    private String canteenID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Intent intent = getIntent();
        canteenID = intent.getStringExtra("Name");//餐厅的ID
        String canteenName = Canteens.getCanteenNames().get(Canteens.getCanteenIDs().indexOf(canteenID));//餐厅的名字
        ((TextView) findViewById(R.id.titleTV)).setText(canteenName);

//        String canteenName = new String();
        PushManager.initSP(getSharedPreferences("MainActivity", MODE_PRIVATE));//方便内部获得键值对的信息
        List<Food> foodsResult = null;
        try {
            foodsResult = PushManager.getInstance().getResult(DataManager.getInstance().getFoodsList(canteenID));//获得最后推荐的结果
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (foodsResult != null) {
            //TODO: 将结果输出到界面中
        }

    }
}
