package com.example.gochat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gochat.ChatListActivity;
import com.example.gochat.R;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private OnItemClickListener listener;
    public ArrayList<String> chatlist;
    public ArrayList<String> typelist;
    public ArrayList<String> textlist;
    public ArrayList<Integer> judgelist;

    public ChatListAdapter(Context context, OnItemClickListener listener){
        this.context=context;
        this.listener=listener;
        chatlist= new ArrayList<String>();
        typelist= new ArrayList<String>();
        textlist= new ArrayList<String>();
        judgelist= new ArrayList<Integer>();
    }

    public void setChatlist(ArrayList<String> chatlist) {
        this.chatlist = chatlist;
    }

    public void setJudgelist(ArrayList<Integer> judgelist) {
        this.judgelist = judgelist;
    }

    public void setTypelist(ArrayList<String> typelist) {
        this.typelist = typelist;
    }

    public void setTextlist(ArrayList<String> textlist) {
        this.textlist = textlist;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new chatholder(LayoutInflater.from(context).inflate(R.layout.layout_chatlist,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((chatholder)holder).tv_name.setText(chatlist.get(position));
        if(judgelist.get(position)==0){
            ((chatholder)holder).tv_judge.setVisibility(View.VISIBLE);
        }
        else{
            ((chatholder)holder).tv_judge.setVisibility(View.GONE);
        }
        ((chatholder)holder).iv_user.setBackground(context.getDrawable(ChatListActivity.User.getuserimage(typelist.get(position))));
        ((chatholder)holder).tv_text.setText(textlist.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(((chatholder)holder).tv_name.getText().toString(),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatlist.size();
    }

    class chatholder extends RecyclerView.ViewHolder{

        private ImageView iv_user;
        private TextView tv_name;
        private TextView tv_judge;
        private TextView tv_text;

        public chatholder(View itemView) {
            super(itemView);
            iv_user=itemView.findViewById(R.id.iv_user);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_judge=itemView.findViewById(R.id.tv_judge);
            tv_text=itemView.findViewById(R.id.tv_text);
        }
    }

    public interface OnItemClickListener{
        void onClick(String name,int position);
    }
}
