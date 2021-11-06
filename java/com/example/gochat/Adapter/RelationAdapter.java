package com.example.gochat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gochat.ChatListActivity;
import com.example.gochat.R;

import java.util.ArrayList;

public class RelationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<String> name;
    private ArrayList<String> sex;
    private Context mContext;
    private OnItemClickListener listener;

    public RelationAdapter(Context context,String str1[],String str2[]){
        this.mContext=context;
        name= new ArrayList<>();
        for(String s:str1){
            name.add(s);
        }
        sex=new ArrayList<>();
        for(String s:str2){
            sex.add(s);
        }
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount(){
        return name.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((relation_layout)holder).textView.setText(name.get(position));
        ((relation_layout)holder).imageView.setBackground(mContext.getDrawable(ChatListActivity.User.getuserimage(sex.get(position))));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new relation_layout(LayoutInflater.from(mContext).inflate(R.layout.layout_relation_adapter,parent,false));
    }

    class relation_layout extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;

        public relation_layout(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.tv_1);
            imageView=itemView.findViewById(R.id.iv_1);
        }

    }
    public interface OnItemClickListener{
        void onClick(int position);
    }
}
