package com.jinxiu.refreshDemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.listView_demo)
    TextView listViewDemo;
    @Bind(R.id.webView_demo)
    TextView webViewDemo;
    @Bind(R.id.ScrollView_demo)
    TextView ScrollViewDemo;
    @Bind(R.id.recycleView_demo)
    TextView recycleViewDemo;
    @Bind(R.id.other_demo)
    TextView otherDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.listView_demo, R.id.webView_demo, R.id.ScrollView_demo, R.id.recycleView_demo, R.id.other_demo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.listView_demo:
                startActivity(new Intent(this, ListViewDemoActivity.class));
                break;
            case R.id.webView_demo:
                startActivity(new Intent(this, WebViewDemoActivity.class));
                break;
            case R.id.ScrollView_demo:
                startActivity(new Intent(this, ScrollViewDemoActivity.class));
                break;
            case R.id.recycleView_demo:
                startActivity(new Intent(this, RecycleViewDemoActivity.class));
                break;
            case R.id.other_demo:
                startActivity(new Intent(this, TextViewViewDemoActivity.class));
                break;
        }
    }
}
