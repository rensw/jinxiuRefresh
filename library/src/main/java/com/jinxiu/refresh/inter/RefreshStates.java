package com.jinxiu.refresh.inter;

/**
 * Created by apple on 17/1/5.
 */
public enum RefreshStates {

    DEFAULT,//默认状态

    DOWN_START,//下拉中，到达有效刷新距离前
    DOWN_AFTER,//下拉中，到达有效刷新距离后
    REFRESH_SCROLLING,//放手后，开始刷新前，回到刷新的位置中
    REFRESH_DOING,//正在刷新中
    REFRESH_COMPLETE_SCROLLING,//刷新完成后，回到默认状态中
    REFRESH_CANCEL_SCROLLING,//刷新取消后，回到默认状态中

    UP_START,//上拉中，到达有效刷新距离前
    UP_AFTER,//上拉中，到达有效刷新距离后
    LOAD_MORE_SCROLLING,//放手后，开始加载前，从手势位置回到加载的位置中
    LOAD_MORE_DOING,//正在加载中
    LOAD_MORE_COMPLETE_SCROLLING,//加载完成后，回到默认状态中
    LOAD_MORE_CANCEL_SCROLLING,//加载取消后，回到默认状态中
}
