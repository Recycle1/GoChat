package com.example.gochat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gochat.Find.FindPasswordActivity;
import com.example.gochat.toastutil.ToastUtil;

public class LoginActivity extends AppCompatActivity {

    private EditText user;
    private EditText password;
    private Button btn_login;
    private Button btn_register;
    private Button btn_find_password;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    ToastUtil.showMsg(LoginActivity.this,"请输入用户名");
                    break;
                case 1:
                    ToastUtil.showMsg(LoginActivity.this,"用户名输入有误");
                    break;
                case 2:
                    ToastUtil.showMsg(LoginActivity.this,"请输入密码");
                    break;
                case 3:
                    ToastUtil.showMsg(LoginActivity.this,"密码有误");
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user=findViewById(R.id.et_account);
        password=findViewById(R.id.et_password);
        btn_login=findViewById(R.id.btn_login);
        btn_register=findViewById(R.id.btn_register);
        btn_find_password=findViewById(R.id.btn_find_password);
        NotificationManagerCompat notification1 = NotificationManagerCompat.from(this);
        boolean isEnabled = notification1.areNotificationsEnabled();
        find_message(isEnabled);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String u_name=user.getText().toString();
                            String u_password=password.getText().toString();

                            if(u_name.length()==0){
                                handler.sendEmptyMessage(0);
                            }
                            else{
                                String str=GetHtml.getHtml("https://www.ddstudy.top/InfoOutputByName?name="+u_name);
                                if(str.equals("1235")||(str.split("<p id=\"weChatNumber\">")[1]).split("</p>")[0].equals("[]")){
                                    handler.sendEmptyMessage(1);
                                }
                                else if(u_password.length()==0){
                                    handler.sendEmptyMessage(2);
                                }
                                //String str=(GetHtml.getHtml("https://www.ddstudy.top/InfoOutput/").split("<p id=\"weChatNumber\">\\[")[1]).split("]</p>")[0];
                                else if((str.split("password=")[1]).split(", id=")[0].equals(u_password)){
                                    ChatListActivity.User.account_number=(str.split(", id=")[1]).split(", signature")[0];
                                    ChatListActivity.User.password=u_password;
                                    ChatListActivity.User.username=u_name;
                                    ChatListActivity.User.sex=(str.split("sex=")[1]).split(", password=")[0];
                                    ChatListActivity.User.personal_text=(str.split("signature=")[1]).split("\\}]")[0];
                                    Intent intent=new Intent(LoginActivity.this,ChatListActivity.class);
                                    startActivity(intent);
                                    LoginActivity.this.finish();
                                }
                                else{
                                    handler.sendEmptyMessage(3);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        btn_find_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(intent);
            }
        });
        btn_find_password.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    btn_find_password.setTextColor(getColor(R.color.black_text));
                }
                else if(event.getAction()==MotionEvent.ACTION_UP){
                    btn_find_password.setTextColor(getColor(R.color.black));
                }
                return false;
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        btn_register.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    btn_register.setTextColor(getColor(R.color.black_text));
                }
                else if(event.getAction()==MotionEvent.ACTION_UP){
                    btn_register.setTextColor(getColor(R.color.black));
                }
                return false;
            }
        });
    }

    void find_message(boolean isEnabled){
        if (!isEnabled) {
            //未打开通知
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("请在“通知”中打开通知权限")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent intent = new Intent();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                intent.putExtra("android.provider.extra.APP_PACKAGE", LoginActivity.this.getPackageName());
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  //5.0
                                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                intent.putExtra("app_package", LoginActivity.this.getPackageName());
                                intent.putExtra("app_uid", LoginActivity.this.getApplicationInfo().uid);
                                startActivity(intent);
                            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {  //4.4
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.setData(Uri.parse("package:" + LoginActivity.this.getPackageName()));
                            } else if (Build.VERSION.SDK_INT >= 15) {
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.fromParts("package", LoginActivity.this.getPackageName(), null));
                            }
                            startActivity(intent);

                        }
                    })
                    .create();
            alertDialog.show();
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        }
    }
}