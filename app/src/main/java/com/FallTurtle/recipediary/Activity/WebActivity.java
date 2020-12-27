package com.FallTurtle.recipediary.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import com.FallTurtle.recipediary.databinding.ActivityWebBinding;

public class WebActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityWebBinding binding = ActivityWebBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        int code = intent.getIntExtra("code",-1);
        //웹뷰연결
        webView = binding.webview;
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.setWebChromeClient(new WebChromeClient());

        if(code == 11)
            binding.webview.loadUrl("https://www.google.com/search?q=" + intent.getStringExtra("search"));
        else if(code == 12)
            binding.webview.loadUrl("https://www.youtube.com/results?search_query=" + intent.getStringExtra("search"));
        else
            Toast.makeText(this,"페이지를 불러올 수 없습니다.",Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack())
            webView.goBack();
        return super.onKeyDown(keyCode, event);
    }

}
