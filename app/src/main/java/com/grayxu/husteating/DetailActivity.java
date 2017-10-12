package com.grayxu.husteating;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        Intent intent = getIntent();
        int canteenNums = intent.getIntExtra("CanteenNums", 0);
        String name = intent.getStringExtra("Name");
        LinearLayout ll = (LinearLayout) findViewById(R.id.setting);


        ((TextView) findViewById(R.id.titleTV)).setText(name);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String[] canteenNames = getResources().getStringArray(R.array.EastOneCanteens);

        if (canteenNums != 0){

        } else {
            ll.removeView(spinner);
        }
    }
}
