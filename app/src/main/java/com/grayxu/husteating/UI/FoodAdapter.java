package com.grayxu.husteating.UI;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grayxu.husteating.R;
import com.grayxu.husteating.background.Food;

import java.util.List;

/**
 * Created by Administrator on 2017/11/13.
 * recycler view的适配器
 */

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.myViewHolder> {

    private List<Food> foodShowList;

    static class myViewHolder extends RecyclerView.ViewHolder {
        View weatherView;
        TextView nameTV;
        TextView priceTV;

        public myViewHolder(View itemView) {
            super(itemView);
            weatherView = itemView;
            nameTV = (TextView) itemView.findViewById(R.id.food_name);
            priceTV = (TextView) itemView.findViewById(R.id.food_price);
        }
    }

    public FoodAdapter(List<Food> foodList){
        this.foodShowList = foodList;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        final myViewHolder holder = new myViewHolder(view);

        //recycler view 的监听器填装
        holder.weatherView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
//                Weather weather = weatherList.get(position);
                Log.d("按键事件","你点击了食物次序为" + (position+1));
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        Food food = foodShowList.get(position);
        holder.nameTV.setText(food.getName());
        holder.priceTV.setText(String.valueOf(food.getPrice()));
    }

    @Override
    public int getItemCount() {
        return foodShowList.size();
    }
}
