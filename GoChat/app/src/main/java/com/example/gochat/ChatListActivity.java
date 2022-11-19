package com.example.gochat;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gochat.fragment.ChatListfragment;
import com.example.gochat.fragment.Communityfragment;
import com.example.gochat.fragment.Relationfragment;
import com.example.gochat.toastutil.ToastUtil;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity {

    private RecyclerView rv_chatlist;
    private LinearLayout ll1,ll2,ll3;
    private ImageView user_image;
    private ChatListfragment fl_chatlist;
    private Relationfragment fl_relation;
    private Communityfragment fl_community;
    private RelativeLayout rl_back_chatlist;
    private RelativeLayout rl_to_personal;
    private ImageView message,relation,community;
    private TextView tv_message,tv_relation,tv_community;
    private Button btn_menu;
    private DrawerLayout drawerLayout;
    private TextView username;
    private ListView lv_menu;
    NotificationManager notificationManager;
    static boolean flag;
    static boolean flag1;
    public static boolean back_flag=false;
    boolean key_back=false;
    static int read=0;
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            flag=false;
            System.out.println("停止搜索线程");
        }
    };
    Runnable runnable1=new Runnable() {
        @Override
        public void run() {
            flag1=false;
            System.out.println("停止搜索线程");
        }
    };
    public static MyOpenHelper myhelper;
    private Handler handler=new Handler();
    private Handler handler1=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        ll1=findViewById(R.id.ll1);
        ll2=findViewById(R.id.ll2);
        ll3=findViewById(R.id.ll3);
        user_image=findViewById(R.id.user_img);
        message=findViewById(R.id.img_message);
        relation=findViewById(R.id.img_relation);
        community=findViewById(R.id.img_community);
        tv_message=findViewById(R.id.tv_message);
        tv_relation=findViewById(R.id.tv_relation);
        tv_community=findViewById(R.id.tv_community);
        btn_menu=findViewById(R.id.btn_menu);
        drawerLayout=findViewById(R.id.drawer_layout);
        rl_back_chatlist=findViewById(R.id.rl_back_chatlist);
        rl_to_personal=findViewById(R.id.rl_to_personal);
        username=findViewById(R.id.user_name);
        lv_menu=findViewById(R.id.lv_menu);
        user_image.setBackground(getDrawable(User.getuserimage(User.sex)));
        username.setText(User.username);
        lv_menu.setAdapter(new menuadapter(this));
        flag=false;
        flag1=false;
        back_flag=false;
        read=0;
        notificationManager=(NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=null;
                switch (position){
                    case 0:
                        back_flag=true;
                        intent=new Intent(ChatListActivity.this,PersonalActivity.class);
                        intent.putExtra("vis",true);
                        intent.putExtra("name",User.username);
                        intent.putExtra("sex",User.sex);
                        intent.putExtra("id",User.account_number);
                        intent.putExtra("sign",User.personal_text);
                        startActivityForResult(intent,1);
                        break;
                    case 1:
                        intent=new Intent(ChatListActivity.this,LoginActivity.class);
                        startActivity(intent);
                        back_flag=true;
                        ChatListActivity.this.finish();
                        break;
                }
            }
        });
        fl_chatlist=ChatListfragment.newInstance();
        fl_relation=Relationfragment.newInstance();
        fl_community=Communityfragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.fl_1,fl_chatlist,"chatlist").commitAllowingStateLoss();
        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.setBackground(getDrawable(R.drawable.message_1));
                tv_message.setTextColor(Color.parseColor("#05CFEA"));
                relation.setBackground(getDrawable(R.drawable.relation_0));
                tv_relation.setTextColor(Color.parseColor("#BFB8B8"));
                community.setBackground(getDrawable(R.drawable.community_0));
                tv_community.setTextColor(Color.parseColor("#BFB8B8"));
                getFragmentManager().beginTransaction().replace(R.id.fl_1,fl_chatlist).commitAllowingStateLoss();
            }
        });
        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relation.setBackground(getDrawable(R.drawable.relation_1));
                tv_relation.setTextColor(Color.parseColor("#05CFEA"));
                message.setBackground(getDrawable(R.drawable.message_0));
                tv_message.setTextColor(Color.parseColor("#BFB8B8"));
                community.setBackground(getDrawable(R.drawable.community_0));
                tv_community.setTextColor(Color.parseColor("#BFB8B8"));
                getFragmentManager().beginTransaction().replace(R.id.fl_1,fl_relation).commitAllowingStateLoss();
            }
        });
        ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                community.setBackground(getDrawable(R.drawable.community_1));
                tv_community.setTextColor(Color.parseColor("#05CFEA"));
                message.setBackground(getDrawable(R.drawable.message_0));
                tv_message.setTextColor(Color.parseColor("#BFB8B8"));
                relation.setBackground(getDrawable(R.drawable.relation_0));
                tv_relation.setTextColor(Color.parseColor("#BFB8B8"));
                getFragmentManager().beginTransaction().replace(R.id.fl_1,fl_community).commitAllowingStateLoss();
            }
        });
        rl_back_chatlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
        rl_to_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back_flag=true;
                Intent intent=new Intent(ChatListActivity.this,PersonalActivity.class);
                intent.putExtra("vis",true);
                intent.putExtra("name",User.username);
                intent.putExtra("sex",User.sex);
                intent.putExtra("id",User.account_number);
                intent.putExtra("sign",User.personal_text);
                startActivityForResult(intent,1);
            }
        });

        myhelper=new MyOpenHelper(this, User.account_number);
        myhelper.getWritableDatabase();
        SQLiteDatabase db=myhelper.getWritableDatabase();
        db.close();
    }

    @Override
    protected void onUserLeaveHint() {
        flag1=true;
        if(flag1&&!back_flag){
            ToastUtil.showMsg(ChatListActivity.this,"GoChat 转至后台运行\n(此过程持续消耗流量）");
            handler1.postDelayed(runnable1,1000*60*10);
        }
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                while (flag1&&!back_flag&&!key_back){
                    try {
                        find_message();
                        Thread.sleep(2500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        super.onUserLeaveHint();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void find_message() throws Exception {
        String str=GetHtml.getHtml("https://www.ddstudy.top/End?id="+User.account_number).split("\\)=")[1].split("\\}]")[0];
//        System.out.println(read);
        if(!str.equals("0")&&Integer.valueOf(str)>read){
            read=Integer.valueOf(str);
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.gochat);
            Intent intent=new Intent(ChatListActivity.this,ChatListActivity.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(ChatListActivity.this,0,intent,0);
            Notification.Builder builder=new Notification.Builder(ChatListActivity.this,"1")
                    .setSmallIcon(R.drawable.gochat)
                    .setContentText("您有未读消息")
                    .setTicker("新的消息")
                    .setContentTitle("新的消息")
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setAutoCancel(true)
                    .setLargeIcon(bitmap)
                    .setFullScreenIntent(pendingIntent,true);
            NotificationChannel channel=new NotificationChannel("1","123", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setLightColor(R.color.white);
            channel.setShowBadge(true);
            notificationManager.createNotificationChannel(channel);
            Notification notification=builder.build();
            notificationManager.notify(1,notification);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            key_back=true;
            flag=true;
            if(flag&&!back_flag){
                ToastUtil.showMsg(ChatListActivity.this,"GoChat 转至后台运行\n(此过程持续消耗流量）");
                handler.postDelayed(runnable,1000*60*10);
            }
            new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    while (flag&&!back_flag){
                        try {
                            find_message();
                            Thread.sleep(2500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    class menuadapter extends BaseAdapter{

        private ArrayList<menu_option> menu_list;
        private Context context;

        public menuadapter(Context context){
            this.context=context;
            menu_list=new ArrayList<menu_option>() ;
            menu_option m1=new menu_option(R.drawable.personal,"个人中心");
            menu_option m2=new menu_option(R.drawable.exit_login,"退出登录");
            menu_list.add(m1);
            menu_list.add(m2);
        }

        @Override
        public int getCount() {
            return menu_list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=View.inflate(context,R.layout.menu,null);
            ImageView imageView=view.findViewById(R.id.menu_img);
            TextView textView=view.findViewById(R.id.menu_name);
            imageView.setBackground(getDrawable(menu_list.get(position).img));
            textView.setText(menu_list.get(position).name);
            return view;
        }
    }
    private class menu_option{
        int img;
        String name;

        public int getImg() {
            return img;
        }

        public String getName() {
            return name;
        }

        public void setImg(int img) {
            this.img = img;
        }

        public void setName(String name) {
            this.name = name;
        }
        public menu_option(int img,String name){
            this.img=img;
            this.name=name;
        }
    }

    public static class User extends Application {
        public static String account_number;
        public static String username;
        public static String password;
        public static String personal_text;
        public static String sex="男";
        public static int getuserimage(String sex){
            if(sex.equals("男")){
                return R.drawable.man;
            }
            else{
                return R.drawable.woman;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fl_chatlist=ChatListfragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.fl_1,fl_chatlist).commitAllowingStateLoss();
    }

    @Override
    protected void onRestart() {
        flag=false;
        flag1=false;
        key_back=false;
        handler1.removeCallbacks(runnable1);
        handler.removeCallbacks(runnable);
        notificationManager.cancel(1);
        super.onRestart();
    }

    @Override
    protected void onResume() {
        flag1=false;
        flag=false;
        key_back=false;
        handler1.removeCallbacks(runnable1);
        handler.removeCallbacks(runnable);
        super.onResume();
    }

    @Override
    public boolean moveTaskToBack(boolean nonRoot) {

        flag=true;
        if(flag&&!back_flag){
            ToastUtil.showMsg(ChatListActivity.this,"GoChat 转至后台运行\n(此过程持续消耗流量）");
            handler.postDelayed(runnable,1000*60*10);
        }
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                while (flag&&!back_flag){
                    try {
                        find_message();
                        Thread.sleep(2500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return super.moveTaskToBack(true);
    }
}