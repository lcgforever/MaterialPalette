package com.chenguang.materialpalette.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.chenguang.materialpalette.R;

public class CopyrightActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, CopyrightActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_copyright);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        WebView webView = (WebView) findViewById(R.id.copyright_web_view);
        webView.loadUrl("file:///android_asset/copyright.html");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
