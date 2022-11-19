package com.example.gochat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonalActivity extends AppCompatActivity {

    private TextView name;
    private TextView account;
    private TextView p_text;
    private String user_id;
    private String user_name;
    private String sex;
    private ImageView sex_img;
    private Button back;
    private Button send;
    private boolean vis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        name=findViewById(R.id.nicheng);
        account=findViewById(R.id.zhanghao);
        p_text=findViewById(R.id.gxqm);
        back=findViewById(R.id.btn_back);
        send=findViewById(R.id.btn_send);
        sex_img=findViewById(R.id.touxiang);
        vis=getIntent().getBooleanExtra("vis",false);
        sex=getIntent().getStringExtra("sex");
        user_id=getIntent().getStringExtra("id");
        user_name=getIntent().getStringExtra("name");
        name.setText(user_name);
        sex_img.setBackground(getDrawable(ChatListActivity.User.getuserimage(sex)));
        account.setText(user_id);
        p_text.setText(getIntent().getStringExtra("sign"));
        if(vis||account.getText().toString().equals(ChatListActivity.User.account_number)){
            send.setVisibility(View.GONE);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatListActivity.back_flag=false;
                PersonalActivity.this.finish();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PersonalActivity.this,ChatActivity.class);
                intent.putExtra("userid",user_id);
                intent.putExtra("name",user_name);
                intent.putExtra("sex",sex);
                startActivity(intent);
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