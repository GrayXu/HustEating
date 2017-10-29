package com.grayxu.husteating.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.grayxu.husteating.R;
import com.grayxu.husteating.background.Canteens;

import java.util.Iterator;

public class InfoActivity extends AppCompatActivity {

    private String canteenName;
    private String tasteChosen;
    private int moneyChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Intent intent = getIntent();
        canteenName = intent.getStringExtra("Name");
        ((TextView) findViewById(R.id.titleTV)).setText(canteenName);
    }
}
