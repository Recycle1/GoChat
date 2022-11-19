package com.example.gochat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.gochat.toastutil.ToastUtil;

public class FriendWebActivity extends AppCompatActivity {
    private WebView webView;
    private Button button1;
    private Button button2;
    private Button button3;
    private static RelativeLayout rl_1;
    private static boolean visibility=false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_web);
        webView=findViewById(R.id.webview);
        button1=findViewById(R.id.btn_back);
        button2=findViewById(R.id.btn_refresh);
        button3=findViewById(R.id.btn_top);
        rl_1=findViewById(R.id.rl_1);
        Toast.makeText(this, "长按屏幕隐藏状态栏", Toast.LENGTH_LONG).show();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new MyWebClient());
        webView.loadUrl("https://www.ddstudy.top/");
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(visibility){
                    rl_1.setVisibility(View.GONE);
                    visibility=false;
                }
                else{
                    rl_1.setVisibility(View.VISIBLE);
                    visibility=true;
                }
                return false;
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ChatListActivity.back_flag=false;
                FriendWebActivity.this.finish();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                webView.reload();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                webView.goBack();
            }
        });
    }

    class MyWebClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
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
