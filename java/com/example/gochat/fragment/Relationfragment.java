package com.example.gochat.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gochat.Adapter.ChatListAdapter;
import com.example.gochat.Adapter.RelationAdapter;
import com.example.gochat.ChatListActivity;
import com.example.gochat.GetHtml;
import com.example.gochat.PersonalActivity;
import com.example.gochat.R;
import com.example.gochat.SearchActivity;


public class Relationfragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tv_search;
    private RelationAdapter relationAdapter;
    private String user_list[][];
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                relationAdapter=new RelationAdapter(getActivity(), ((String[][]) msg.obj)[0],((String[][]) msg.obj)[1]);
                user_list= (String[][]) msg.obj;
                relationAdapter.setListener(new RelationAdapter.OnItemClickListener(){
                    @Override
                    public void onClick(int position) {
                        ChatListActivity.back_flag=true;
                        Intent intent=new Intent(getActivity(),PersonalActivity.class);
                        intent.putExtra("name",user_list[0][position]);
                        intent.putExtra("sex",user_list[1][position]);
                        intent.putExtra("id",user_list[2][position]);
                        intent.putExtra("sign",user_list[3][position]);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(relationAdapter);
            }
        }
    };

    public static Relationfragment newInstance(){
        Relationfragment relationfragment=new Relationfragment();
        return relationfragment;
    }

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_relation,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.rv_1);
        tv_search=view.findViewById(R.id.tv_search);
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatListActivity.back_flag=true;
                Intent intent=new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new relationdecoration());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String str=GetHtml.getHtml(GetHtml.GET_USER_INFORMATION);
                    String name_list[]=GetHtml.splitHtml(str,"name=",", sex");
                    String sex_list[]=GetHtml.splitHtml(str,"sex=",", password");
                    String id_list[]=GetHtml.splitHtml(str,"id=",", signature");
                    String signature_list[]=GetHtml.splitHtml(str,"signature=","\\}");
                    System.out.println(sex_list[0]);
                    String list[][]=new String[4][];
                    Message message=new Message();
                    message.what=0;
                    list[0]=name_list;
                    list[1]=sex_list;
                    list[2]=id_list;
                    list[3]=signature_list;
                    message.obj=list;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    class relationdecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0,0,0,getResources().getDimensionPixelOffset(R.dimen.dividerHeight));
        }
    }
}
