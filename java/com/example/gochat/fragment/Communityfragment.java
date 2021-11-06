package com.example.gochat.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.gochat.ChatListActivity;
import com.example.gochat.FriendWebActivity;
import com.example.gochat.R;
import com.example.gochat.RegisterActivity;
import com.example.gochat.communityActivity;
import com.example.gochat.loading_Activity;

public class Communityfragment extends Fragment {

    private RelativeLayout community;
    private RelativeLayout wanyun;
    private RelativeLayout more;

    public static Communityfragment newInstance(){
        Communityfragment communityfragment=new Communityfragment();
        return communityfragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_community,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        community=view.findViewById(R.id.rl_friend_community);
        wanyun=view.findViewById(R.id.rl_wanyun);
        more=view.findViewById(R.id.rl_more);
        community.setOnClickListener(new onClick());
        wanyun.setOnClickListener(new onClick());
        more.setOnClickListener(new onClick());
    }

    class onClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rl_friend_community:
                    ChatListActivity.back_flag=true;
                    Intent intent1=new Intent(getActivity(), communityActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.rl_wanyun:
                    ChatListActivity.back_flag=true;
                    Intent intent2=new Intent(getActivity(), FriendWebActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.rl_more:
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("了解更多");
                    builder.setMessage("更多功能敬请期待");
                    builder.setPositiveButton("确定",null);
                    builder.show();
                    break;
            }
        }
    }
}
