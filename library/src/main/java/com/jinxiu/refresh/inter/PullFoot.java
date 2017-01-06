package com.jinxiu.refresh.inter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by apple on 17/1/5.
 * 下拉刷新接口
 */
public abstract class PullFoot extends FrameLayout{

    public PullFoot(Context context) {
        super(context);
    }

    public PullFoot(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //加载更多
    public abstract  void onUpBefore(int scrollY);

    //释放加载更多
    public  abstract  void onUpAfter(int scrollY);

    //准备加载更多
    public abstract void onLoadScrolling(int scrollY);

    //加载更多中
    public  abstract  void onLoadDoing(int scrollY);

    //完成加载更多
    public abstract void onLoadCompleteScrolling(int scrollY, boolean isLoadSuccess);

    //加载取消后，回到默认状态中
    public abstract void onLoadCancelScrolling(int scrollY);
}
