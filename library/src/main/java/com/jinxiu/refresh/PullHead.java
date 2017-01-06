package com.jinxiu.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by apple on 17/1/5.
 * 下拉刷新接口
 */
public abstract class PullHead extends FrameLayout{



    public PullHead(Context context) {
        super(context);
    }

    public PullHead(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //开始下拉刷新
    public abstract void onDownBefore(int scrollY);

    //松开刷新(下拉中，到达有效刷新距离后)
    public abstract  void onDownAfter(int scrollY);

    //准备刷新(从松手后的位置滚动到刷新的位置)
    public abstract void onRefreshScrolling(int scrollY);

    //正在刷新……....
    public abstract void onRefreshDoing(int scrollY);

    //刷新完成
    public abstract void onRefreshCompleteScrolling(int scrollY, boolean isRefreshSuccess);

    //刷新取消后，回到默认状态中（没有超过有效的下拉距离）
    public abstract void onRefreshCancelScrolling(int scrollY);
}
