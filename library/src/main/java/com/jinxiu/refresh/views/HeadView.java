package com.jinxiu.refresh.views;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jinxiu.refresh.R;
import com.jinxiu.refresh.inter.PullHeader;


/**
 * Created by apple on 17/1/5.
 * 下拉刷新View
 */
public class HeadView extends FrameLayout implements PullHeader {

    private TextView textView;
    String refreshBeforeStr = "下拉刷新";
    String refreshAfterStr = "松开刷新";
    String refreshScrollingStr = "准备刷新";
    String refreshDoingStr = "刷新中";
    String refreshCompleteStr = "刷新完成";
    String refreshCancelStr = "取消刷新";
    private int refreshHeight;

    public HeadView(Context context) {
        super(context);
        View headView = View.inflate(context, R.layout.layout_header, this);
        textView = (TextView) headView.findViewById(R.id.tv);
        refreshHeight = getMeasuredHeight();
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
