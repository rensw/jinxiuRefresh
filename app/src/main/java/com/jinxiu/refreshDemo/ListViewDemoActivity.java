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
public class ListViewDemoActivity extends AppCompatActivity {

    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    List<String> mList = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mList.addAll(getList());
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mList);
        listView.setAdapter(arrayAdapter);
        refreshLayout.setOnPullListener(new OnPullListener() {
            @Override
            public void onRefresh() {
                mList.clear();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mList.addAll(getList());
                        arrayAdapter.notifyDataSetChanged();
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
                        arrayAdapter.notifyDataSetChanged();
                        refreshLayout.stopLoadMore();
                    }
                }, 2000);
            }
        });
    }

    public List<String> getList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i + "item");
        }
        return list;
    }
}
