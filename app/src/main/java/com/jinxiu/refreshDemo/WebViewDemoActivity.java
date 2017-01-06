package com.jinxiu.refreshDemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.jinxiu.refresh.inter.OnPullListener;
import com.jinxiu.refresh.views.RefreshLayout;
import com.jinxiu.refreshDemo.views.ProgressWebView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rsw on 17/1/6.
 * ListView DEMO页
 */
public class WebViewDemoActivity extends AppCompatActivity {

    @Bind(R.id.webView)
    ProgressWebView webView;
    @Bind(R.id.refresh_layout)
    RefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        webView.loadUrl("http://101.201.239.59");
        refreshLayout.setIsLoadMore(false);
        refreshLayout.setIsPullEnable(true);
        refreshLayout.setOnPullListener(new OnPullListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webView.reload();
                        refreshLayout.stopRefresh();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.stopLoadMore();
                    }
                }, 2000);
            }
        });
    }
}
