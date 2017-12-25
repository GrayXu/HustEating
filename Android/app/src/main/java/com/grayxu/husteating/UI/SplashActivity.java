package com.grayxu.husteating.UI;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.grayxu.husteating.R;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGHT = 1300;//停留时间
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityManager.getActivityManager().addActivity("SplashActivity", this);//添加进管理器，方便后面终结
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        GifImageView gifImageView = (GifImageView) findViewById(R.id.gifSplash);
        try {
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.splash);
            gifImageView.setImageDrawable(gifDrawable);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        final String canteenID = intent.getStringExtra("Name");//餐厅的ID
        String task = intent.getStringExtra("task");

        if (task != null){
            handler = new Handler();
            // 延迟SPLASH_DISPLAY_LENGHT时间然后跳转到MainActivity
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this,
                            InfoActivity.class);
                    intent.putExtra("Name",canteenID);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            }, SPLASH_DISPLAY_LENGHT);

        }else{
            handler = new Handler();
            // 延迟SPLASH_DISPLAY_LENGHT时间然后跳转到MainActivity
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            }, SPLASH_DISPLAY_LENGHT);
        }

    }

}
