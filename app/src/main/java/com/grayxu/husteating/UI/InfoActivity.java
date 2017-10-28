package com.grayxu.husteating.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.grayxu.husteating.R;

public class InfoActivity extends AppCompatActivity {

    private String canteenID;
    private String tasteChosen;
    private int moneyChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Intent intent = getIntent();
        canteenID = intent.getStringExtra("Name");
        moneyChosen = intent.getIntExtra("moneyChosen", 10);
        tasteChosen = intent.getStringExtra("tasteChosen");

        ((TextView) findViewById(R.id.infoTV)).setText(canteenID + "\n" + moneyChosen + "\n" + tasteChosen);
    }
}
