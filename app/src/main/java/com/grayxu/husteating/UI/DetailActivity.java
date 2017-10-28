package com.grayxu.husteating.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.grayxu.husteating.R;

/**
 * 进行具体推荐的详情界面活动
 */
public class DetailActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener, NumberPicker.OnScrollListener, NumberPicker.Formatter {

    final static int MINMONEY = 3;
    final static int MAXMONEY = 50;

    private String tasteChosen;
    private int moneyChosen = 10;//default ->10
    private String canteenID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        Intent intent = getIntent();
        canteenID = intent.getStringExtra("Name");

        //初始化标题
        initTitle(canteenID);

        //初始化口味的Spinner
        initSpinner();

        //价格选取器的初始化设置
        initNumpicker();

        //进入指定餐厅下的餐肴推荐
        findViewById(R.id.buttonInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, InfoActivity.class);
                //添加查询所需的信息
                intent.putExtra("Name", canteenID);
                intent.putExtra("moneyChosen", moneyChosen);
                intent.putExtra("tasteChosen", tasteChosen);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化口味的Spinner
     */
    private void initSpinner() {
        ((Spinner) findViewById(R.id.spinner)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tasteChosen = String.valueOf(adapterView.getItemAtPosition(i));
                Log.v("onItemClick", "新选择的口味是" + tasteChosen);
                //TODO： 根据口味而改变色调

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * 初始化餐厅的名称作为标题
     *
     * @param canteenID 餐厅的代码
     */
    private void initTitle(String canteenID) {
        String canteenName = "";
        if (canteenID.equals("E11")) {
            canteenName = "东一食堂 一楼";
        } else if (canteenID.equals("E12")) {
            canteenName = "东一食堂 二楼";
        } else if (canteenID.equals("E3")) {
            canteenName = "东三清真食堂";
        }
        ((TextView) findViewById(R.id.titleTV)).setText(canteenName);//设置详情界面的标题
    }

    private void initNumpicker(){
        NumberPicker moneyPicker = ((NumberPicker) findViewById(R.id.moneyPicker));
        moneyPicker.setFormatter(this);
        moneyPicker.setOnValueChangedListener(this);
        moneyPicker.setOnScrollListener(this);
        moneyPicker.setMaxValue(MAXMONEY);
        moneyPicker.setMinValue(MINMONEY);
        moneyPicker.setValue(10);//TODO： 可以设置为来自个人历史记录
        moneyPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    @Override
    public String format(int i) {
        return String.valueOf(i);
    }

    @Override
    public void onScrollStateChange(NumberPicker numberPicker, int i) {

    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        DetailActivity.this.moneyChosen = i1;
        Log.i("onValueChange", "新选择的价格为" + DetailActivity.this.moneyChosen);
    }
}
