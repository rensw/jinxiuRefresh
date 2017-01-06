package com.jinxiu.refresh.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.jinxiu.refresh.PullFoot;
import com.jinxiu.refresh.PullHead;

/**
 * Created by rsw on 17/1/5.
 * 下拉刷新类
 */
public abstract class RefreshView extends ViewGroup {

    PullHead header;
    PullFoot footer;
    public int bottomScroll;  //滚动到底部Y轴所需要的滑动值
    public int lastChildIndex; //最后一个child的索引
    public boolean isLoadMore = true;  //加载更多可用
    public boolean isPullEnable = true; //下拉刷新可用

    public RefreshView(Context context) {
        super(context);
    }

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addHeaderView() {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(header, layoutParams);
    }

    public void addFooterView() {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(footer, layoutParams);
    }

    public void setIsLoadMore(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
    }

    public void setIsPullEnable(boolean isPullEnable) {
        this.isPullEnable = isPullEnable;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int contentHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            if (child == header) {
                child.layout(0, -child.getMeasuredHeight(), child.getMeasuredWidth(), 0);
            } else if (child == footer) {
                child.layout(0, contentHeight, child.getMeasuredWidth(), contentHeight + child.getMeasuredHeight());
            } else {
                child.layout(0, contentHeight, child.getMeasuredWidth(), contentHeight + child.getMeasuredHeight());
                if (i <= lastChildIndex) {
                    if (child instanceof ScrollView) {
                        contentHeight += getMeasuredHeight();
                        continue;
                    }
                    contentHeight += getMeasuredHeight();
                }
            }
            bottomScroll = contentHeight - getMeasuredHeight();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        lastChildIndex = getChildCount() - 1;
    }
}
