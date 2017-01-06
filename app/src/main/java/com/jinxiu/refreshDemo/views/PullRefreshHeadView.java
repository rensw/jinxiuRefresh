package com.jinxiu.refreshDemo.views;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.jinxiu.refresh.PullHead;

/**
 * Created by rsw on 17/1/6.
 * 自定义头部 底部同理
 */
public class PullRefreshHeadView extends PullHead {

    private TextView textView;
    String refreshBeforeStr = "下拉刷新123";
    String refreshAfterStr = "松开刷新123";
    String refreshScrollingStr = "准备刷新123";
    String refreshDoingStr = "刷新中...123";
    String refreshCompleteStr = "刷新完成123";
    String refreshCancelStr = "取消刷新123";

    public PullRefreshHeadView(Context context) {
        super(context);
        View headView = View.inflate(context, com.jinxiu.refresh.R.layout.layout_header, this);
        textView = (TextView) headView.findViewById(com.jinxiu.refresh.R.id.tv);
    }

    @Override
    public void onDownBefore(int scrollY) {
        textView.setText(refreshBeforeStr);
    }

    @Override
    public void onDownAfter(int scrollY) {
        textView.setText(refreshAfterStr);
    }

    @Override
    public void onRefreshScrolling(int scrollY) {
        textView.setText(refreshScrollingStr);
    }

    @Override
    public void onRefreshDoing(int scrollY) {
        textView.setText(refreshDoingStr);
    }

    @Override
    public void onRefreshCompleteScrolling(int scrollY, boolean isRefreshSuccess) {
        textView.setText(refreshCompleteStr);
    }

    @Override
    public void onRefreshCancelScrolling(int scrollY) {
        textView.setText(refreshCancelStr);
    }


}
