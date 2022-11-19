package com.example.gochat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {

    private Button btn_search;
    private EditText et_search;
    private ImageView iv_search;
    private TextView tv_search;
    private LinearLayout l1;
    private String name;
    private String sex;
    private String id;
    private String sign;
    private TextView null_search;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            name=((String[])msg.obj)[0];
            sex=((String[])msg.obj)[1];
            id=((String[])msg.obj)[2];
            sign=((String[])msg.obj)[3];
            iv_search.setBackground(getDrawable(ChatListActivity.User.getuserimage(sex)));
            tv_search.setText(name);
            null_search.setVisibility(View.GONE);
            l1.setVisibility(View.VISIBLE);
            l1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(SearchActivity.this,PersonalActivity.class);
                    intent.putExtra("name",name);
                    intent.putExtra("sex",sex);
                    intent.putExtra("id",id);
                    intent.putExtra("sign",sign);
                    startActivity(intent);
                }
            });
        }
    };
    private Handler handler1=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            l1.setVisibility(View.GONE);
            null_search.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        btn_search=findViewById(R.id.btn_search);
        et_search=findViewById(R.id.et_search);
        iv_search=findViewById(R.id.iv_1);
        tv_search=findViewById(R.id.tv_1);
        l1=findViewById(R.id.l1);
        null_search=findViewById(R.id.null_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String s_text=et_search.getText().toString();
                            String s_s=GetHtml.getHtml("https://www.ddstudy.top/InfoOutputByName?name="+s_text);
                            if((s_s.split("<p id=\"weChatNumber\">")[1]).split("</p>")[0].equals("[]")){
                                handler1.sendEmptyMessage(0);
                            }
                            else{
                                String s_message[]=new String[4];
                                String s_name=s_s.split("name=")[1].split(", sex")[0];
                                String s_sex=s_s.split("sex=")[1].split(", password")[0];
                                String s_id=s_s.split(", id=")[1].split(", signature")[0];
                                String s_sign=s_s.split("signature=")[1].split("\\}")[0];
                                s_message[0]=s_name;
                                s_message[1]=s_sex;
                                s_message[2]=s_id;
                                s_message[3]=s_sign;
                                Message message=new Message();
                                message.obj=s_message;
                                handler.sendMessage(message);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            ChatListActivity.back_flag=false;
        }
        return super.onKeyDown(keyCode, event);
    }
}