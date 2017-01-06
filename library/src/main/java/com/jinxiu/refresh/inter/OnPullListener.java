package com.jinxiu.refresh.inter;

/**
 * Created by apple on 17/1/5.
 * 刷新监听
 */
public interface OnPullListener {
    //执行刷新
    void onRefresh();

    //执行加载更多
    void onLoadMore();
}
