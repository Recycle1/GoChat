package com.example.gochat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gochat.toastutil.ToastUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    private EditText send_text;
    private Button send;
    private Button back;
    private FrameLayout fl_1;
    private RecyclerView recyclerView;
    private DialogAdapter dialogAdapter;
    private TextView tv_name;
    private String name;
    private String id1= ChatListActivity.User.account_number;
    private String id2;
    private ImageView img;
    private String sex;
    private boolean pause=false;
    private String table;
    private String position;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String friend_text=((String) msg.obj).split("//text#&&#time//")[0];
            String friend_time=((String) msg.obj).split("//text#&&#time//")[1].replace("T"," ");
            dialogAdapter.list_type.add(1);
            dialogAdapter.list.add(friend_text);
            dialogAdapter.notifyDataSetChanged();
            if(dialogAdapter.list.size()!=0){
                recyclerView.smoothScrollToPosition(dialogAdapter.list.size()-1);
            }
            if(friend_time.length()!=19){
                friend_time+=":00";
            }
            Store_information.add_information(ChatListActivity.myhelper,table,"1",friend_text,friend_time);
        }
    };
    private Handler p_handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Intent intent=new Intent(ChatActivity.this,PersonalActivity.class);
            intent.putExtra("vis",true);
            intent.putExtra("name",name);
            intent.putExtra("sex",sex);
            intent.putExtra("id",id2);
            intent.putExtra("sign",(String)msg.obj);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        send_text=findViewById(R.id.et_text);
        send=findViewById(R.id.btn_send);
        back=findViewById(R.id.btn_back);
        recyclerView=findViewById(R.id.rv_1);
        tv_name=findViewById(R.id.tv_name);
        fl_1=findViewById(R.id.fl_1);
        img=findViewById(R.id.touxiang);
        id2=getIntent().getStringExtra("userid");
        name=getIntent().getStringExtra("name");
        tv_name.setText(name);
        sex=getIntent().getStringExtra("sex");
        position=getIntent().getStringExtra("position");
        table="info"+id2;
        Store_information.create_table(ChatListActivity.myhelper,table);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        dialogAdapter=new DialogAdapter(this,sex,Store_information.get_information(ChatListActivity.myhelper,table));
        recyclerView.setAdapter(dialogAdapter);
        if(dialogAdapter.list.size()!=0){
            recyclerView.scrollToPosition(dialogAdapter.list.size()-1);
        }
        img.setBackground(getDrawable(ChatListActivity.User.getuserimage(sex)));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatListActivity.back_flag=false;
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("position",position);
                if(dialogAdapter.list.size()!=0){
                    bundle.putString("text",dialogAdapter.list.get(dialogAdapter.list.size()-1));
                }
                intent.putExtras(bundle);
                setResult(0,intent);
                ChatActivity.this.finish();
            }
        });
        fl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String sign=(GetHtml.getHtml("https://www.ddstudy.top/InfoOutputByName?name="+name).split(", signature=")[1]).split("\\}")[0];
                            Message message=new Message();
                            message.obj=sign;
                            p_handler.sendMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        send_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(start==169){
                    ToastUtil.showMsg(ChatActivity.this,"最多发170个字");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=send_text.getText().toString();
                if(text.length()==0){
                    ToastUtil.showMsg(ChatActivity.this,"请输入内容");
                }
                else{
                    dialogAdapter.list_type.add(0);
                    dialogAdapter.list.add(text);
                    dialogAdapter.notifyDataSetChanged();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String path="https://www.ddstudy.top/ContextQQReceiver?id1="+id1+"&text="+text+"&id2="+id2;
                                URL url=new URL(path);
                                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                                httpURLConnection.setConnectTimeout(5000);
                                httpURLConnection.setRequestMethod("GET");
                                if (httpURLConnection.getResponseCode() != 200) {
                                    //ToastUtil.threadshowMsg(ChatActivity.this,"发送失败");
                                }
                                else{
                                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date date=new Date(System.currentTimeMillis());
                                    Store_information.add_information(ChatListActivity.myhelper,table,"0",text,simpleDateFormat.format(date));
                                }
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    send_text.setText("");
                    if(dialogAdapter.list.size()!=0){
                        recyclerView.smoothScrollToPosition(dialogAdapter.list.size()-1);
                    }
                }
            }
        });
        new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!pause) {
                        String path = "https://www.ddstudy.top/ContextQQ?ids1=" + id2 + "&ids2=" + id1;
                        try {
                            String str = (GetHtml.getHtml(path).split("<p id=\"weChatNumber\">\\[")[1]).split("]</p>")[0];
                            String text_begin[] = str.split("text=");
                            int sum = text_begin.length - 1;
                            String text[] = new String[sum];
                            String time_begin[] = str.split("timeQQ=");
                            String time[] = new String[sum];
                            for (int i = 1; i < sum + 1; i++) {
                                text[i - 1] = text_begin[i].split(", timeQQ")[0];
                                time[i - 1] = time_begin[i].split(", id2")[0];
                            }
                            for (int i = 0; i < text.length; i++) {
                                Message message = new Message();
                                message.obj = text[i]+"//text#&&#time//"+time[i];
                                handler.sendMessage(message);
                                String path1 = "https://www.ddstudy.top/JudgeOne?idj1=" + id2 + "&idj2=" + id1 + "&timej=" + time[i];
                                if (GetHtml.touchHtml(path1).equals("no")) {
                                    ToastUtil.showMsg(ChatActivity.this, "接收失败");
                                }
                            }
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            ChatListActivity.back_flag=false;
            Intent intent=new Intent();
            Bundle bundle=new Bundle();
            bundle.putString("position",position);
            if(dialogAdapter.list.size()!=0){
                bundle.putString("text",dialogAdapter.list.get(dialogAdapter.list.size()-1));
            }
            intent.putExtras(bundle);
            setResult(0,intent);
            ChatActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    static class DialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private ArrayList<String> list;
        private Context context;
        private ArrayList<Integer> list_type;
        private String sex;
        private String text[][];

        public DialogAdapter(Context context,String sex,String text[][]){
            list=new ArrayList<>();
            list_type=new ArrayList<>();
            this.context=context;
            this.sex=sex;
            this.text=text;

            for(int i=0;i<text[0].length;i++){
                list_type.add(Integer.valueOf(text[0][i]));
                list.add(text[1][i]);
            }

        }

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(list_type.get(viewType)==0){
                return new myholder(LayoutInflater.from(context).inflate(R.layout.layout_dialog,parent,false));
            }
            else{
                return new friendholder(LayoutInflater.from(context).inflate(R.layout.layout_dialog_friend,parent,false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(list_type.get(position)==0){
                ((myholder)holder).textView.setText(list.get(position));
                ((myholder)holder).imageView.setBackground(context.getDrawable(ChatListActivity.User.getuserimage(ChatListActivity.User.sex)));
            }
            else {
                ((friendholder)holder).textView.setText(list.get(position));
                ((friendholder)holder).imageView.setBackground(context.getDrawable(ChatListActivity.User.getuserimage(sex)));
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class myholder extends RecyclerView.ViewHolder{

            private TextView textView;
            private ImageView imageView;

            public myholder(View itemView) {
                super(itemView);
                textView=itemView.findViewById(R.id.tv_1);
                imageView=itemView.findViewById(R.id.iv_1);
            }
        }

        class friendholder extends RecyclerView.ViewHolder{

            private TextView textView;
            private ImageView imageView;

            public friendholder(View itemView) {
                super(itemView);
                textView=itemView.findViewById(R.id.tv_2);
                imageView=itemView.findViewById(R.id.iv_2);
            }
        }
    }

    @Override
    protected void onStop() {
        pause=true;
        super.onStop();
    }
}