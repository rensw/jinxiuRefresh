package com.jinxiu.refreshDemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jinxiu.refresh.custom.AndroidVsIosHeaderView;
import com.jinxiu.refresh.inter.OnPullListener;
import com.jinxiu.refresh.views.RefreshLayout;
import com.jinxiu.refreshDemo.adapter.ViewHolder;
import com.jinxiu.refreshDemo.adapter.recyclerview.BaseHeadAdapter;
import com.jinxiu.refreshDemo.views.PullRefreshHeadView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rsw on 17/1/6.
 * ListView DEMO页
 */
public class RecycleViewDemoActivity extends AppCompatActivity {

    @Bind(R.id.recycleView)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    List<String> mList = new ArrayList<>();
    BaseHeadAdapter<String> baseHeadAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mList.addAll(getList());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        baseHeadAdapter=new BaseHeadAdapter<String>(this,android.R.layout.simple_list_item_1,mList) {
            @Override
            public void convert(ViewHolder holder, String s) {
                holder.setText(android.R.id.text1,"第"+holder.getmPosition()+"个"+s);
            }
        };
        recyclerView.setAdapter(baseHeadAdapter);
        AndroidVsIosHeaderView  pullRefreshHeadView=new AndroidVsIosHeaderView(this);
        refreshLayout.setPullHeader(pullRefreshHeadView);
        refreshLayout.setOnPullListener(new OnPullListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mList.clear();
                        mList.addAll(getList());
                        baseHeadAdapter.notifyDataSetChanged();
                        refreshLayout.stopRefresh();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mList.addAll(getList());
                        baseHeadAdapter.notifyDataSetChanged();
                        refreshLayout.stopLoadMore();
                    }
                }, 2000);
            }
        });
    }

    public List<String> getList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add( "item");
        }
        return list;
    }
}
