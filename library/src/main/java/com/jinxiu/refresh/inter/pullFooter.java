package com.jinxiu.refresh.inter;

/**
 * Created by apple on 17/1/5.
 * 上啦加载接口
 */
public interface pullFooter {

    //加载更多
    void onUpBefore(int scrollY);

    //释放加载更多
    void onUpAfter(int scrollY);

    //准备加载更多
    void onLoadScrolling(int scrollY);

    //加载更多中
    void onLoadDoing(int scrollY);

    //完成加载更多
    void onLoadCompleteScrolling(int scrollY, boolean isLoadSuccess);

    //加载取消后，回到默认状态中
    void onLoadCancelScrolling(int scrollY);
}
