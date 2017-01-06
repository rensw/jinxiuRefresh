package com.jinxiu.refresh.inter;

/**
 * Created by apple on 17/1/5.
 * 下拉刷新接口
 */
public interface PullHeader {

    //开始下拉刷新
    void onDownBefore(int scrollY);

    //松开刷新(下拉中，到达有效刷新距离后)
    void onDownAfter(int scrollY);

    //准备刷新(从松手后的位置滚动到刷新的位置)
    void onRefreshScrolling(int scrollY);

    //正在刷新……
    void onRefreshDoing(int scrollY);

    //刷新完成
    void onRefreshCompleteScrolling(int scrollY, boolean isRefreshSuccess);

    //刷新取消后，回到默认状态中（没有超过有效的下拉距离）
    void onRefreshCancelScrolling(int scrollY);

}
