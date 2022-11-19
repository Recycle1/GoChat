package com.example.gochat.fragment;


import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gochat.Adapter.ChatListAdapter;
import com.example.gochat.ChatActivity;
import com.example.gochat.ChatListActivity;
import com.example.gochat.GetHtml;
import com.example.gochat.LoginActivity;
import com.example.gochat.R;
import com.example.gochat.Store_information;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.NOTIFICATION_SERVICE;

public class ChatListfragment extends Fragment {

    private RecyclerView rv_chatlist;
    private LinearLayout l1;
    private boolean sc=true;
    private boolean pause=false;
    ChatListAdapter CLAdapter;
    static int pos;
    String table=null;
    NotificationManager notificationManager;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    ChatListActivity.back_flag=true;
                    Intent intent=new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("userid",((String[])msg.obj)[0]);
                    intent.putExtra("name",((String[])msg.obj)[1]);
                    intent.putExtra("sex",((String[])msg.obj)[2]);
                    intent.putExtra("position",((String[])msg.obj)[3]);
                    startActivityForResult(intent,0);
                    break;
                case 1:
                    ArrayList<String> chatlist=new ArrayList<>();
                    ArrayList<String> typelist=new ArrayList<>();
                    ArrayList<String> textlist=new ArrayList<>();
                    ArrayList<Integer> judgelist=new ArrayList<>();
                    for(int i=0;i<((String[][]) msg.obj)[0].length;i++){
                        chatlist.add(((String[][]) msg.obj)[0][i]);
                        typelist.add(((String[][]) msg.obj)[1][i]);
                        judgelist.add(Integer.valueOf(((String[][]) msg.obj)[2][i]));
                        textlist.add(((String[][]) msg.obj)[3][i]);
                    }
                    CLAdapter.setChatlist(chatlist);
                    CLAdapter.setTypelist(typelist);
                    CLAdapter.setJudgelist(judgelist);
                    CLAdapter.setTextlist(textlist);
                    CLAdapter.notifyDataSetChanged();
                    l1.setVisibility(View.GONE);
                    sc=true;
                    break;
            }
        }
    };

    public static ChatListfragment newInstance(){
        ChatListfragment chatListfragment=new ChatListfragment();
        return chatListfragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chatlist,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv_chatlist=view.findViewById(R.id.rv_1);
        l1=view.findViewById(R.id.l1);
        pause=false;
        notificationManager=(NotificationManager)getActivity().getSystemService(NOTIFICATION_SERVICE);
        rv_chatlist.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_chatlist.addItemDecoration(new chatlistdecoration());
        rv_chatlist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager layoutManager=(LinearLayoutManager)recyclerView.getLayoutManager();
                int first=layoutManager.findFirstCompletelyVisibleItemPosition();
                int visible = layoutManager.getChildCount();
                if(visible>0&&newState==RecyclerView.SCROLL_STATE_DRAGGING&&first==0&&sc){
                    l1.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sc=false;
                            refresh();
                        }
                    }).start();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        CLAdapter= new ChatListAdapter(getActivity(), new ChatListAdapter.OnItemClickListener() {

            @Override
            public void onClick(String name,int position) {
                pos=position;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String str = (GetHtml.getHtml("https://www.ddstudy.top/InfoOutputByName?name="+name).split(", id=")[1]).split(", signature")[0];
                            String str1=(GetHtml.getHtml("https://www.ddstudy.top/InfoOutputByName?name="+name).split("sex=")[1]).split(", password")[0];
                            String u_name[]=new String[4];
                            Message message=new Message();
                            message.what=0;
                            u_name[0]=str;
                            u_name[1]=name;
                            u_name[2]=str1;
                            u_name[3]=String.valueOf(position);
                            message.obj=u_name;
                            handler.sendMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        rv_chatlist.setAdapter(CLAdapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!pause){
                    try {
                        refresh();
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    void refresh(){
                try {
                    String str=GetHtml.getHtml("https://www.ddstudy.top/ContextQQAll?ida1="+ ChatListActivity.User.account_number);
                    String send[]=GetHtml.splitHtml(str,"id1=",", text");
                    String receive[]=GetHtml.splitHtml(str,"id2=",", judge");
                    String judge[]=GetHtml.splitHtml(str,"judge=","\\}");
                    String text[]=GetHtml.splitHtml(str,"text=",", timeQQ");
                    String time[]=GetHtml.splitHtml(str,"timeQQ=",", id2");
                    ArrayList<String> new_chat=new ArrayList<>();
                    ArrayList<String> judge_flag=new ArrayList<>();
                    ArrayList<String> sex=new ArrayList<>();
                    ArrayList<String> new_text=new ArrayList<>();
                    ArrayList<String> new_time=new ArrayList<>();
                    for(int i=0;i<send.length;i++){
                        if(receive[i].equals(ChatListActivity.User.account_number)&&!send[i].equals(ChatListActivity.User.account_number)){
                            boolean flag=true;
                            for(int j=0;j<new_chat.size();j++){
                                if(send[i].equals(new_chat.get(j))){
                                    if(judge[i].equals("0")){
                                        judge_flag.set(j,"0");
                                    }
                                    new_text.set(j,text[i]);
                                    new_time.set(j,time[i].replace("T"," "));
                                    flag=false;
                                    break;
                                }
                            }
                            if(flag){
                                judge_flag.add(judge[i]);
                                new_chat.add(send[i]);
                                new_text.add(text[i]);
                                new_time.add(time[i].replace("T"," "));
                                sex.add("男");
                            }
                        }
                    }
                    String str1=GetHtml.getHtml("https://www.ddstudy.top/InfoOutput/");
                    String c_name[]=GetHtml.splitHtml(str1,"name=",", sex");
                    String c_id[]=GetHtml.splitHtml(str1,", id=",", signature");
                    String c_sex[]=GetHtml.splitHtml(str1,"sex=",", password");
                    String old_chat[]= Store_information.table_names(ChatListActivity.myhelper);
                    boolean f=true;
                    String sql_info[]=null;
                    if(old_chat.length>0){
                        for(int i=0;i< old_chat.length;i++){
                            for(int j=0;j<new_chat.size();j++){
                                if(old_chat[i].length()==10&&(new_chat.get(j).equals(old_chat[i].replace("info","")))){
                                    f=false;
                                    break;
                                }
                            }
                            if(old_chat[i].length()==10&&f){
                                new_chat.add(old_chat[i].replace("info",""));
                                sql_info=Store_information.search_recent(ChatListActivity.myhelper,old_chat[i]);
                                new_text.add(sql_info[0]);
                                new_time.add(sql_info[1]);
                                judge_flag.add("1");
                                sex.add("男");
                            }
                            f=true;
                        }
                    }
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date=null;
                    String change=null;
                    for(int i=0;i<new_chat.size()-1;i++){
                        for(int j=0;j<new_chat.size()-1-i;j++){
                            if(simpleDateFormat.parse(new_time.get(j)).compareTo(simpleDateFormat.parse(new_time.get(j+1)))<0){
                                change=new_text.get(j);
                                new_text.set(j,new_text.get(j+1));
                                new_text.set(j+1,change);
                                change=new_chat.get(j);
                                new_chat.set(j,new_chat.get(j+1));
                                new_chat.set(j+1,change);
                                change=judge_flag.get(j);
                                judge_flag.set(j,judge_flag.get(j+1));
                                judge_flag.set(j+1,change);
                                change=new_time.get(j);
                                new_time.set(j,new_time.get(j+1));
                                new_time.set(j+1,change);
                            }
                        }
                    }
                    for(int i=0;i<new_chat.size();i++){
                        for(int j=0;j<c_id.length;j++){
                            if(new_chat.get(i).equals(c_id[j])){
                                new_chat.set(i,c_name[j]);
                                sex.set(i,c_sex[j]);
                            }
                        }
                    }
                    String chat_information[][]=new String[4][];
                    chat_information[0]=new_chat.toArray(new String[0]);
                    chat_information[1]=sex.toArray(new String[0]);
                    chat_information[2]=judge_flag.toArray(new String[0]);
                    chat_information[3]=new_text.toArray(new String[0]);
                    Message message=new Message();
                    message.what=1;
                    message.obj=chat_information;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
    }

    class chatlistdecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0,0,0,getResources().getDimensionPixelOffset(R.dimen.dividerHeight));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            int p=Integer.valueOf(data.getExtras().getString("position"));
            CLAdapter.judgelist.set(p,1);
            CLAdapter.textlist.set(p,data.getExtras().getString("text"));
            CLAdapter.notifyDataSetChanged();
        }
        pause=false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!pause){
                    try {
                        refresh();
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onStop() {
        pause=true;
        super.onStop();
    }
}
