package com.example.gochat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    private ImageView img_chat;
    private TextView app_name;
    private int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!this.isTaskRoot()) {
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                finish();
                return;
            }
        }
        img_chat=findViewById(R.id.img_chat);
        app_name=findViewById(R.id.app_name);
        View decorView = getWindow().getDecorView();
        uiFlags |= 0x00001000;
        decorView.setSystemUiVisibility(uiFlags);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                getWindow().getDecorView().setSystemUiVisibility(uiFlags);
            }
        });
        img_chat.animate().rotation(0).setDuration(2000).start();
        app_name.animate().alpha(1).setDuration(2000).start();
        ValueAnimator valueAnimator=ValueAnimator.ofInt(0,100);
        valueAnimator.setDuration(2015);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(Integer.valueOf(String.valueOf(animation.getAnimatedValue()))==100){
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
            }
        });
        valueAnimator.start();
    }
}