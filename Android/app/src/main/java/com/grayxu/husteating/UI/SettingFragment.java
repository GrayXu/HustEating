package com.grayxu.husteating.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.support.v7.widget.Toolbar;

import com.grayxu.husteating.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * 进行具体推荐的详情界面活动
 */
public class SettingFragment extends Fragment implements NumberPicker.OnValueChangeListener, NumberPicker.OnScrollListener, NumberPicker.Formatter {

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
        initNumPicker(new ArrayList<>(Arrays.asList(
                ((NumberPicker) view.findViewById(R.id.moneyPickerBreakfast)),
                ((NumberPicker) view.findViewById(R.id.moneyPickerLunch)),
                ((NumberPicker) view.findViewById(R.id.moneyPickerDinner))
        )));
    }

    /**
     * 初始化口味的Spinner
     */
    private void initSpinner(Spinner spinner) {

        String tasteHistory = preferences.getString("tasteChosen", "辣");//辣是初始值
        int posSpinner = 0;
        if (tasteHistory.equals("辣")) {
            posSpinner = 0;
        } else if (tasteHistory.equals("清淡")) {
            posSpinner = 1;
        } else if (tasteHistory.equals("香")) {
            posSpinner = 2;
        } else if (tasteHistory.equals("甜")) {
            posSpinner = 3;
        }
        spinner.setSelection(posSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tasteChosen = String.valueOf(adapterView.getItemAtPosition(i));
                Log.v("onItemClick", "新选择的口味是" + tasteChosen);
                preferences.edit().putString("tasteChosen", tasteChosen).apply();

                Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
                String color = "0";
                if (tasteChosen.equals("辣")) {
                    color = "#FF0000";
                } else if (tasteChosen.equals("清淡")) {
                    color = "#BFEFFF";
                } else if (tasteChosen.equals("香")) {
                    color = "#EEC900";
                } else if (tasteChosen.equals("甜")) {
                    color = "#FFAEB9";
                }
                toolbar.setBackgroundColor(Color.parseColor(color));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }


    /**
     * 初始化传入的moneyPicker
     *
     * @param numberPickerArrayList 需要初始化的NumPicker
     */
    private void initNumPicker(ArrayList<NumberPicker> numberPickerArrayList) {
        Iterator iterator = numberPickerArrayList.iterator();
        while (iterator.hasNext()) {
            NumberPicker moneyPicker = (NumberPicker) iterator.next();
            moneyPicker.setFormatter(this);
            moneyPicker.setOnValueChangedListener(this);
            moneyPicker.setOnScrollListener(this);
            moneyPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        }

        numberPickerArrayList.get(0).setMinValue(3);numberPickerArrayList.get(0).setMaxValue(15);//早餐的价格区间
        numberPickerArrayList.get(1).setMinValue(5);numberPickerArrayList.get(1).setMaxValue(30);//午餐的价格区间
        numberPickerArrayList.get(2).setMinValue(5);numberPickerArrayList.get(2).setMaxValue(30);//晚餐的价格区间


        numberPickerArrayList.get(0).setValue(preferences.getInt("moneyBreakfastChosen", 5));//早餐默认为5
        numberPickerArrayList.get(1).setValue(preferences.getInt("moneyLunchChosen", 10));//午饭默认为10
        numberPickerArrayList.get(2).setValue(preferences.getInt("moneyDinnerChosen", 12));//早餐默认为12

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
        //保存picker的值到SP中
        switch (numberPicker.getId()) {
            case R.id.moneyPickerBreakfast:
                preferences.edit().putInt("moneyBreakfastChosen", i1).apply();
            case R.id.moneyPickerLunch:
                preferences.edit().putInt("moneyLunchChosen", i1).apply();
            case R.id.moneyPickerDinner:
                preferences.edit().putInt("moneyDinnerChosen", i1).apply();
        }
    }
}
