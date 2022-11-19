package com.example.gochat.Find;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gochat.R;

public class FindResultActivity extends AppCompatActivity {

    private TextView tv_name;
    private TextView tv_password;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_result);
        tv_name=findViewById(R.id.tv_name);
        tv_password=findViewById(R.id.tv_password);
        tv_name.setText(getIntent().getStringExtra("name"));
        tv_password.setText(getIntent().getStringExtra("password"));
        back=findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(0);
                FindResultActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            setResult(0);
        }
        return super.onKeyDown(keyCode, event);
    }
}