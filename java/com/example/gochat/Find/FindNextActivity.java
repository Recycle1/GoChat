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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gochat.GetHtml;
import com.example.gochat.R;
import com.example.gochat.toastutil.ToastUtil;

public class FindNextActivity extends AppCompatActivity {

    private Button back;
    private EditText editText;
    private Button find_next;
    private String name;
    private String password;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case  0:
                    ToastUtil.showMsg(FindNextActivity.this,"请输入答案");
                    break;
                case  1:
                    ToastUtil.showMsg(FindNextActivity.this,"答案错误");
                    break;
                case  2:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String str= null;
                            try {
                                str = GetHtml.getHtml("https://www.ddstudy.top/InfoOutputByName?name="+name);
                                password=str.split("password=")[1].split(", id")[0];
                                f_handler.sendEmptyMessage(0);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
            }
        }
    };
    private Handler f_handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Intent intent=new Intent(FindNextActivity.this, FindResultActivity.class);
            intent.putExtra("name",name);
            intent.putExtra("password",password);
            startActivityForResult(intent,0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_next);
        back=findViewById(R.id.btn_back);
        editText=findViewById(R.id.et_answer);
        find_next=findViewById(R.id.btn_find_next);
        name=getIntent().getStringExtra("name");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                FindNextActivity.this.finish();
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
                        String answer=editText.getText().toString();
                        String str= null;
                        try {
                            str = GetHtml.getHtml("https://www.ddstudy.top/findPassword?name="+name+"&answer="+answer);
                            if(answer.length()==0){
                                handler.sendEmptyMessage(0);
                            }
                            else if(str.equals("1235")||(str.split("<p id=\"weChatNumber\">")[1]).split("</p>")[0].equals("[{COUNT(*)=0}]")){
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
            setResult(0);
            FindNextActivity.this.finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            setResult(1);
        }
        return super.onKeyDown(keyCode, event);
    }
}