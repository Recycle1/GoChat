package com.example.gochat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.service.autofill.FieldClassification;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.gochat.toastutil.ToastUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    //文本框
    TextView title;
    TextView Name;
    TextView Password_TextView;
    TextView Sex;
    TextView Password_TextView_again;
    TextView Personal_TextView;
    //输入框
    EditText User_Name;
    EditText Password;
    EditText Password_again;
    EditText Person_text;
    EditText Find_Password;
    //单选按钮
    RadioGroup rg;
    RadioButton man,woman;
    //按钮
    Button back;
    Button Yes;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    ToastUtil.showMsg(RegisterActivity.this,"注册成功");
                    RegisterActivity.this.finish();
                    break;
            }
        }
    };
    private Handler handler1=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                ToastUtil.showMsg(RegisterActivity.this,"注册失败，请重新点击注册");
            }
            else if(msg.what==1){
                ToastUtil.showMsg(RegisterActivity.this,"昵称被占用");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //寻找ID
        title = (TextView) findViewById(R.id.Title);
        Name = (TextView) findViewById(R.id.Name);
        Password_TextView = (TextView) findViewById(R.id.password_TextView);
        Sex = (TextView) findViewById(R.id.Sex);
        Password_TextView_again = (TextView) findViewById(R.id.password_TextView_again);
        Personal_TextView = (TextView) findViewById(R.id.Personal_TextView);
        //EditText
        User_Name = (EditText) findViewById(R.id.User_Name);
        Password = (EditText) findViewById(R.id.pwd);
        Password_again = (EditText) findViewById(R.id.pwd_again);
        Person_text = (EditText) findViewById(R.id.Personal_text);
        Find_Password=(EditText)findViewById(R.id.answer);
        //RadioGroup
        rg = (RadioGroup) findViewById(R.id.rg);
        man = (RadioButton) findViewById(R.id.man);
        woman = (RadioButton) findViewById(R.id.woman);
        //Button
        back = (Button)findViewById(R.id.back);
        Yes = (Button) findViewById(R.id.Yes);
        //监听按钮back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
//                startActivity(intent);
                RegisterActivity.this.finish();
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
        User_Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(start+count==12){
                    System.out.println(start+"l"+before+"we"+count);
                    ToastUtil.showMsg(RegisterActivity.this,"昵称小于等于12位");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(start+count==15){
                    ToastUtil.showMsg(RegisterActivity.this,"密码小于等于15位");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Password_again.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(start+count==15){
                    ToastUtil.showMsg(RegisterActivity.this,"密码小于等于15位");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Person_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(start+count==70){
                    ToastUtil.showMsg(RegisterActivity.this,"个性签名小于等于70位");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //监听按钮Yes
        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strName = User_Name.getText().toString();
                String strPassword = Password.getText().toString();
                String strPassword_again = Password_again.getText().toString();
                String strPerson_text = Person_text.getText().toString();
                String strFind_Password = Find_Password.getText().toString();
                String strSex=new String();
                //判断是否为空
                final int[] flag = {0};
                if(strName.length() == 0 && flag[0] == 0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("错误提示");
                    builder.setMessage("昵称不能为空");
                    builder.setPositiveButton("确定",null);
                    flag[0] = 1;
                    builder.show();
                }
                else if(strName.length() != 0 && flag[0] == 0){
                    String regEx = "[ 《》，。\\_`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
                    Pattern pattern=Pattern.compile(regEx);
                    Matcher matcher=pattern.matcher(strName);
                    if(matcher.find()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setTitle("错误提示");
                        builder.setMessage("昵称不能有特殊符号");
                        builder.setPositiveButton("确定",null);
                        flag[0] = 1;
                        builder.show();
                    }
                }
                if(strPassword.length() == 0 && flag[0] == 0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("错误提示");
                    builder.setMessage("密码不能为空");
                    builder.setPositiveButton("确定",null);
                    flag[0] = 1;
                    builder.show();
                }
                if(strPassword_again.length() == 0 && flag[0] == 0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("错误提示");
                    builder.setMessage("确认密码不能为空");
                    builder.setPositiveButton("确定",null);
                    flag[0] = 1;
                    builder.show();
                }
                if(strPerson_text.length() == 0 && flag[0] == 0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("错误提示");
                    builder.setMessage("个性签名不能为空");
                    builder.setPositiveButton("确定",null);
                    flag[0] = 1;
                    builder.show();
                }
                if(strFind_Password.length() == 0 && flag[0] == 0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("错误提示");
                    builder.setMessage("密保答案不能为空");
                    builder.setPositiveButton("确定",null);
                    flag[0] = 1;
                    builder.show();
                }
                //判断密码两次是否一样
                int pwd = 0;
                if(strPassword.trim().equals(strPassword_again.trim())  && flag[0] == 0)
                {
                    pwd = 1;
                    //传值
                    if(man.isChecked())
                    {
                        strSex="男";
                    }
                    if(woman.isChecked())
                    {
                        strSex="女";
                    }
                    String path_1="https://www.ddstudy.top/InfoReceiver?name="+strName+"&sex="+strSex+"&password="+strPassword+"&id=";
                    String path_2="&signature="+strPerson_text;
                    String path_found="https://www.ddstudy.top/findPasswordInsert?name="+strName+"&answer="+strFind_Password;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String str=GetHtml.getHtml("https://www.ddstudy.top/InfoOutput/");
                                String str1=(GetHtml.getHtml("https://www.ddstudy.top/InfoOutputByName?name="+strName).split("<p id=\"weChatNumber\">")[1]).split("</p>")[0];
                                String strs_id[]=GetHtml.splitHtml(str,"id=",", signature");
                                if(GetHtml.touchHtml(path_found)=="no"){
                                    handler1.sendEmptyMessage(0);
                                }
                                else{
                                    if(str1.equals("[]")){
                                        if(GetHtml.touchHtml(path_1+String.format("%06d",strs_id.length+1)+path_2)=="yes"){
                                            handler.sendEmptyMessage(0);
                                        }
                                        else{
                                            handler1.sendEmptyMessage(0);
                                        }
                                    }
                                    else{
                                        handler1.sendEmptyMessage(1);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    flag[0] = 1;

                }
                if(pwd == 0 && flag[0] == 0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("错误提示");
                    builder.setMessage("两次的密码输入不一致");
                    builder.setPositiveButton("确定",null);
                    flag[0] = 1;
                    builder.show();
                }
            }
        });
    }
}