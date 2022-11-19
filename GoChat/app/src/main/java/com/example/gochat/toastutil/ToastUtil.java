package com.example.gochat.toastutil;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtil{
    public static Toast toast;
    public static void showMsg(Context context,String msg){
        if(toast==null){
            toast=Toast.makeText(context,msg,Toast.LENGTH_SHORT);
        }
        else{
            toast.setText(msg);
        }
        toast.show();
    }
}
