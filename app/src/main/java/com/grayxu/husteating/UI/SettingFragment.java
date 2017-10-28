package com.grayxu.husteating.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.grayxu.husteating.R;

/**
 * 进行具体推荐的详情界面活动
 */
public class SettingFragment extends Fragment implements NumberPicker.OnValueChangeListener, NumberPicker.OnScrollListener, NumberPicker.Formatter {

    final static int MINMONEY = 3;
    final static int MAXMONEY = 50;

    private View view;
    private SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_setting, container, false);
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        load(view);
        return view;
    }

    /**
     * 载入离线缓存
     *
     * @param view 用来获取控件
     */
    private void load(View view) {
        //初始化口味的Spinner
        initSpinner((Spinner) view.findViewById(R.id.spinner));
        //价格选取器的初始化设置
        initNumPicker((NumberPicker) view.findViewById(R.id.moneyPicker));
    }

    /**
     * 初始化口味的Spinner
     */
    private void initSpinner(Spinner spinner) {

        String tasteHistory = preferences.getString("tasteChosen", "辣");//辣是初始值
        int posSpinner = 0;
        if (tasteHistory.equals("辣")){
            posSpinner = 0;
        }else if (tasteHistory.equals("清淡")){
            posSpinner = 1;
        }else if (tasteHistory.equals("香")){
            posSpinner = 2;
        }else if (tasteHistory.equals("甜")){
            posSpinner = 3;
        }
        spinner.setSelection(posSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tasteChosen = String.valueOf(adapterView.getItemAtPosition(i));
                Log.v("onItemClick", "新选择的口味是" + tasteChosen);
                preferences.edit().putString("tasteChosen", tasteChosen).apply();
                //TODO： 根据口味而改变ActionBar的色调
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }


    /**
     * 初始化传入的moneyPicker
     *
     * @param moneyPicker
     */
    private void initNumPicker(NumberPicker moneyPicker) {
        moneyPicker.setFormatter(this);
        moneyPicker.setOnValueChangedListener(this);
        moneyPicker.setOnScrollListener(this);
        moneyPicker.setMaxValue(MAXMONEY);
        moneyPicker.setMinValue(MINMONEY);
        moneyPicker.setValue(preferences.getInt("moneyChosen", 10));//默认为10
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
        preferences.edit().putInt("moneyChosen", i1).apply();//保存picker的值到SP中
        Log.i("onValueChange", "新选择的价格为" + i1);
    }
}
