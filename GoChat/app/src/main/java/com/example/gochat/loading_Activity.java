package com.example.gochat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gochat.toastutil.ToastUtil;

import static com.example.gochat.GetHtml.touchHtml;

public class loading_Activity extends AppCompatActivity {
    String account;
    private Button bt_1;
    private Button bt_2;
    private EditText editText1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            setResult(0);
            loading_Activity.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        bt_1 = findViewById(R.id.bt1);
        bt_2 = findViewById(R.id.bt2);
        editText1 = (EditText) findViewById(R.id.et1);
        bt_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading_Activity.this.finish();
            }
        });
        bt_2.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    bt_2.setTextColor(getColor(R.color.White_text));
                }
                else if(event.getAction()==MotionEvent.ACTION_UP){
                    bt_2.setTextColor(getColor(R.color.white));
                }
                return false;
            }
        });
        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(start+count==170){
                    ToastUtil.showMsg(loading_Activity.this,"动态最多170个字");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        new Thread() {
//            public void run() {

        bt_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //获取输入的内容
                account = editText1.getText().toString();
                if(account.length()==0){
                    ToastUtil.showMsg(loading_Activity.this,"请输入动态的内容");
                }
                else{
                    String path = "https://www.ddstudy.top/dynamicReceiver?name=" + ChatListActivity.User.username + "&DongTai=" + account;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (touchHtml(path).equals("yes")) {
                                    handler.sendEmptyMessage(0);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
    }
}



