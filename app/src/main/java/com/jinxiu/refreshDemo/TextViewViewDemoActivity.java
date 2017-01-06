package com.jinxiu.refreshDemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jinxiu.refresh.inter.OnPullListener;
import com.jinxiu.refresh.views.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rsw on 17/1/6.
 * ListView DEMO页
 */
public class TextViewViewDemoActivity extends AppCompatActivity {

    @Bind(R.id.refresh_layout)
    RefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textview);
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
