package com.example.gochat.Find;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gochat.GetHtml;
import com.example.gochat.LoginActivity;
import com.example.gochat.R;
import com.example.gochat.RegisterActivity;
import com.example.gochat.toastutil.ToastUtil;

public class FindPasswordActivity extends AppCompatActivity {

    private Button back;
    private Button find_next;
    private EditText editText;
    String name;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case  0:
                    ToastUtil.showMsg(FindPasswordActivity.this,"请输入用户名");
                    break;
                case  1:
                    ToastUtil.showMsg(FindPasswordActivity.this,"无此用户名");
                    break;
                case  2:
                    Intent intent=new Intent(FindPasswordActivity.this, FindNextActivity.class);
                    intent.putExtra("name",name);
                    startActivityForResult(intent,0);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        back=findViewById(R.id.btn_back);
        find_next=findViewById(R.id.btn_find_next);
        editText=findViewById(R.id.et_find_account);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindPasswordActivity.this.finish();
            }
        });
        back.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    back.setTextColor(getColor(R.color.black_text));
                }
                else if(event.getAction()==MotionEvent.ACTION_UP){
                    back.setTextColor(getColor(R.color.black));
                }
                return false;
            }
        });
        find_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        name=editText.getText().toString();
                        String str= null;
                        try {
                            str = GetHtml.getHtml("https://www.ddstudy.top/InfoOutputByName?name="+name);
                            if(name.length()==0){
                                handler.sendEmptyMessage(0);
                            }
                            else if(str.equals("1235")||(str.split("<p id=\"weChatNumber\">")[1]).split("</p>")[0].equals("[]")){
                                handler.sendEmptyMessage(1);
                            }
                            else{
                                handler.sendEmptyMessage(2);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==0){
            FindPasswordActivity.this.finish();
        }
    }
}