package com.example.gochat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.gochat.GetHtml.getHtml;
import static com.example.gochat.GetHtml.splitHtml;

public class communityActivity extends AppCompatActivity {
    private Button button;
    RecyclerView recyclerView;
    int number=5;
    String l;
    GetHtml getHtmlA = new GetHtml();
    MyAdapter myAdapter;
    String names[] = null;
    String says[] = null;
    String times[] = null;
    String img[]=null;
    private boolean flag=false;
    private boolean bottom=false;
    private boolean sc=true;
    private int r_x;
    private int r_y;
    private Button back;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    //myAdapter = new SimpleAdapter(getApplicationContext(),(List<Map<String, Object>>)msg.obj , R.layout.layout_community, new String[]{ "imgtou","names", "sayes","timeD"}, new int[]{ R.id.imgtou,R.id.name, R.id.says,R.id.timeD});
                    //listView.setAdapter(myAdapter);
                    ArrayList<String> namelist=new ArrayList<>();
                    ArrayList<String> saylist=new ArrayList<>();
                    ArrayList<String> timeDlist=new ArrayList<>();
                    ArrayList<String> sexlist=new ArrayList<>();
                    for(int i=0;i<((String[][]) msg.obj)[0].length;i++){
                        namelist.add(((String[][]) msg.obj)[0][i]);
                        saylist.add(((String[][]) msg.obj)[1][i]);
                        timeDlist.add(((String[][]) msg.obj)[2][i]);
                        sexlist.add(((String[][]) msg.obj)[3][i]);
                    }
                    myAdapter.setName(namelist);
                    myAdapter.setSay(saylist);
                    myAdapter.setSex(sexlist);
                    myAdapter.setTimeD(timeDlist);
                    r_x=recyclerView.getScrollX();
                    r_y=recyclerView.getScrollY();
                    myAdapter.notifyDataSetChanged();
                    recyclerView.scrollTo(r_x,r_y);
                    sc=true;
                    break;
            }
        }
    };
    private Handler handler1=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            myAdapter.text_foot="(｡ì _ í｡)已到达底部，人生要懂得适可而止";
            myAdapter.notifyItemChanged(myAdapter.name.size());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        button = findViewById(R.id.buttonD1) ;
        recyclerView = findViewById(R.id.list_view);
        back=findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatListActivity.back_flag=false;
                communityActivity.this.finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(communityActivity.this));
        myAdapter=new MyAdapter(communityActivity.this);
        recyclerView.setAdapter(myAdapter);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //跳转
                Intent intent = new Intent(communityActivity.this,loading_Activity.class);
                startActivityForResult(intent,0);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager layoutManager=(LinearLayoutManager)recyclerView.getLayoutManager();
                int total=layoutManager.getItemCount();
                int last=layoutManager.findLastCompletelyVisibleItemPosition();
                int visible = layoutManager.getChildCount();
                if(visible>0&&newState==RecyclerView.SCROLL_STATE_IDLE&&last==total-1&&sc){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                number+=5;
                                sc=false;
                                Looper.prepare();
                                refresh();
                                Looper.loop();
                            }
                        }).start();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        new Thread() {
            public void run() {
                refresh();
            }
        }.start();
    }

    private void refresh(){
        //从数据库中取出最新发布的五条动态
        //https://www.ddstudy.top/dynamicSender?numberOfDongTai=9
        try {
            l=getHtml("https://www.ddstudy.top/DongTaiCount").split("\\)=")[1].split("\\}")[0];
            String path;
            if(Integer.valueOf(l)<Integer.valueOf(number)){
                path = "https://www.ddstudy.top/dynamicSender?numberOfDongTai=" + l;
                flag=true;
            }
            else{
                path = "https://www.ddstudy.top/dynamicSender?numberOfDongTai=" + number;
            }
            if(!bottom){
                String str=getHtml(path);
                names = splitHtml(str,"name=",", DongTai");
                says = splitHtml(str,", DongTai=",", timeDongTai");
                times = splitHtml(str,"timeDongTai=","\\}");
                img=new String[names.length];
                for(int i=0;i<names.length;i++){
                    String html=getHtml("https://www.ddstudy.top/InfoOutputByName?name="+names[i]);
                    img[i]=(html.split("sex=")[1]).split(", password")[0];
                }
                String info[][]=new String[4][];
                info[0]=names;
                info[1]=says;
                info[2]=times;
                info[3]=img;

                //创建一个simpleAdapter
                Message message=new Message();
                message.what=0;
                message.obj=info;
                handler.sendMessage(message);
                if(flag){
                    bottom=true;
                    handler1.sendEmptyMessage(0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent=new Intent(this,communityActivity.class);
        startActivity(intent);
        finish();
    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        ArrayList<String> name;
        ArrayList<String> say;
        ArrayList<String> timeD;
        ArrayList<String> sex;
        String text_foot;
        Context context;

        MyAdapter(Context context){
            this.context=context;
            name=new ArrayList<>();
            say=new ArrayList<>();
            timeD=new ArrayList<>();
            sex=new ArrayList<>();
            text_foot=new String();
            text_foot="加载更多";
        }

        public void setName(ArrayList<String> name) {
            this.name = name;
        }

        public void setSay(ArrayList<String> say) {
            this.say = say;
        }

        public void setSex(ArrayList<String> sex) {
            this.sex = sex;
        }

        public void setTimeD(ArrayList<String> timeD) {
            this.timeD = timeD;
        }

        @Override
        public int getItemViewType(int position) {
            if(position==name.size()){
                return 1;
            }
            return 0;
        }

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==0){
                return new community_holder(LayoutInflater.from(context).inflate(R.layout.layout_community,parent,false));
            }
            else{
                return new footholder(LayoutInflater.from(context).inflate(R.layout.layout_foot,parent,false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(getItemViewType(position)==0){
                ((community_holder)holder).names.setText(name.get(position));
                ((community_holder)holder).says.setText(say.get(position));
                ((community_holder)holder).timeD.setText(timeD.get(position).replace("T"," "));
                ((community_holder)holder).imgtou.setBackground(getDrawable(ChatListActivity.User.getuserimage(sex.get(position))));
            }
            else{
                ((footholder)holder).tv_foot.setText(text_foot);
            }
        }

        @Override
        public int getItemCount() {
            return name.size()+1;
        }
        class community_holder extends RecyclerView.ViewHolder{

            TextView names;
            TextView says;
            TextView timeD;
            ImageView imgtou;

            public community_holder(View itemView) {
                super(itemView);
                names=itemView.findViewById(R.id.name);
                says=itemView.findViewById(R.id.says);
                timeD=itemView.findViewById(R.id.timeD);
                imgtou=itemView.findViewById(R.id.imgtou);
            }
        }
        class footholder extends RecyclerView.ViewHolder{

            TextView tv_foot;

            public footholder(View itemView) {
                super(itemView);
                tv_foot=itemView.findViewById(R.id.tv_foot);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            ChatListActivity.back_flag=false;
        }
        return super.onKeyDown(keyCode, event);
    }
}