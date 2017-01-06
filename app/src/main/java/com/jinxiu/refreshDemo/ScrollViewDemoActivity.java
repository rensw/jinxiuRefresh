package com.jinxiu.refreshDemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.jinxiu.refresh.inter.OnPullListener;
import com.jinxiu.refresh.views.RefreshLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rsw on 17/1/6.
 * ListView DEMO页
 */
public class ScrollViewDemoActivity extends AppCompatActivity {

    @Bind(R.id.refresh_layout)
    RefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        refreshLayout.setOnPullListener(new OnPullListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
