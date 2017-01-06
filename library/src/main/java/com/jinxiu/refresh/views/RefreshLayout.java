package com.jinxiu.refresh.views;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by rsw on 17/1/5.
 * 下拉刷新组件
 */
public class RefreshLayout extends PullLayout {

    public RefreshLayout(Context context) {
        super(context);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        HeadView headView = new HeadView(getContext());
        FootView footView = new FootView(getContext());
        addHeader(headView);
        addFooter(footView);
    }

}
