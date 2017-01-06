package com.jinxiu.refresh.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.jinxiu.refresh.PullFoot;
import com.jinxiu.refresh.PullHead;
import com.jinxiu.refresh.inter.OnPullListener;
import com.jinxiu.refresh.inter.RefreshStates;

/**
 * Created by rsw on 17/1/5.
 * 下拉刷新组件
 */
public class RefreshLayout extends InterceptLayout {

    public OnPullListener onPullListener;
    //layout状态
    public RefreshStates pullStates = RefreshStates.DEFAULT;
    //恢复动画的执行时间
    public int SCROLL_TIME = 300;
    //是否刷新完成
    private boolean isRefreshSuccess = false;
    //是否加载完成
    private boolean isLoadSuccess = false;
    PullHead pullHeader;
    PullFoot pullFooter;

    public RefreshLayout(Context context) {
        super(context);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setOnPullListener(OnPullListener onPullListener) {
        this.onPullListener = onPullListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int Y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                //本次移动的距离Y轴
                int dy = Y - lastYmove;
                if (dy > 0) {   //下拉刷新
                    if (header != null) {
                        //开始滑动
                        if (!isPullEnable) {
                            return false;
                        }
                        performScroll(dy);
                        if (Math.abs(getScrollY()) > header.getMeasuredHeight()) {
                            updateStatus(RefreshStates.DOWN_AFTER);
                        } else {
                            updateStatus(RefreshStates.DOWN_START);
                        }
                    }
                } else if (dy < 0) {   //上拉刷新
                    if (footer != null) {
                        if (!isLoadMore) {
                            return false;
                        }
                        performScroll(dy);
                        if (getScrollY() > bottomScroll + footer.getMeasuredHeight()) {
                            updateStatus(RefreshStates.UP_AFTER);
                        } else {
                            updateStatus(RefreshStates.UP_START);
                        }
                    }
                }
                lastYmove = Y;
                break;
            case MotionEvent.ACTION_UP:
                switch (pullStates) {
                    case DOWN_AFTER:
                        scrolltoRefreshStatus();
                        break;
                    case DOWN_START:
                        scrolltoDefaultStatus(RefreshStates.REFRESH_CANCEL_SCROLLING);
                        break;
                    case UP_AFTER:
                        scrolltoLoadStatus();
                        break;
                    case UP_START:
                        scrolltoDefaultStatus(RefreshStates.LOAD_MORE_CANCEL_SCROLLING);
                        break;
                }
                break;
        }
        lastYIntercept = 0;
        postInvalidate();
        return true;
    }

    /**
     * 滚动到上拉状态
     */
    private void scrolltoLoadStatus() {
        int start = getScrollY();
        int end = footer.getMeasuredHeight() + bottomScroll;
        performAnimal(start, end, new AnimalListener() {
            @Override
            public void onLoading() {
                updateStatus(RefreshStates.LOAD_MORE_SCROLLING);
            }

            @Override
            public void onEnd() {
                updateStatus(RefreshStates.LOAD_MORE_DOING);
            }
        });
    }

    /**
     * //滚动到加载状态
     */
    private void scrolltoRefreshStatus() {
        int start = getScrollY();
        int end = -header.getMeasuredHeight();
        performAnimal(start, end, new AnimalListener() {
            @Override
            public void onLoading() {
                updateStatus(RefreshStates.REFRESH_SCROLLING);
            }

            @Override
            public void onEnd() {
                updateStatus(RefreshStates.REFRESH_DOING);
            }
        });
    }

    /**
     * 滚动到默认状态
     */
    private void scrolltoDefaultStatus(final RefreshStates startStatus) {

        int start = getScrollY();
        int end = 0;
        performAnimal(start, end, new AnimalListener() {
            @Override
            public void onLoading() {
                updateStatus(startStatus);
            }

            @Override
            public void onEnd() {
                updateStatus(RefreshStates.DEFAULT);
            }
        });
    }

    //停止刷新
    public void stopRefresh() {
        isRefreshSuccess = true;
        scrolltoDefaultStatus(RefreshStates.REFRESH_COMPLETE_SCROLLING);
    }

    //停止加载更多
    public void stopLoadMore() {
        isLoadSuccess = true;
        scrolltoDefaultStatus(RefreshStates.LOAD_MORE_COMPLETE_SCROLLING);
    }

    /**
     * 执行动画
     *
     * @param start
     * @param end
     */
    private void performAnimal(int start, int end, final AnimalListener listener) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(SCROLL_TIME).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                scrollTo(0, value);
                postInvalidate();
                listener.onLoading();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                listener.onEnd();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    interface AnimalListener {
        void onLoading();

        void onEnd();
    }

    /**
     * 更新刷新状态
     *
     * @param pullStates
     */
    private void updateStatus(RefreshStates pullStates) {
        this.pullStates = pullStates;
        int scrollY = getScrollY();
        switch (pullStates) {
            case DEFAULT:  //默认
                onDefault();
                break;
            case DOWN_AFTER:
                pullHeader.onDownAfter(scrollY);
                break;
            case DOWN_START:
                pullHeader.onDownBefore(scrollY);
                break;
            case REFRESH_SCROLLING:
                pullHeader.onRefreshScrolling(scrollY);
                break;
            case REFRESH_DOING:
                pullHeader.onRefreshDoing(scrollY);
                if (onPullListener != null)
                    onPullListener.onRefresh();
                break;
            case REFRESH_COMPLETE_SCROLLING:
                pullHeader.onRefreshCompleteScrolling(scrollY, isRefreshSuccess);
                break;
            case REFRESH_CANCEL_SCROLLING:
                pullHeader.onRefreshCancelScrolling(scrollY);
                break;
            case UP_START:
                pullFooter.onUpBefore(scrollY);
                break;
            case UP_AFTER:
                pullFooter.onUpAfter(scrollY);
                break;
            case LOAD_MORE_SCROLLING:
                pullFooter.onLoadScrolling(scrollY);
                break;
            case LOAD_MORE_DOING:
                pullFooter.onLoadDoing(scrollY);
                if (onPullListener != null)
                    onPullListener.onLoadMore();
                break;
            case LOAD_MORE_COMPLETE_SCROLLING:
                pullFooter.onLoadCompleteScrolling(scrollY, isLoadSuccess);
                break;
            case LOAD_MORE_CANCEL_SCROLLING:
                pullFooter.onLoadCancelScrolling(scrollY);
                break;
        }
    }

    //默认状态
    private void onDefault() {
        isRefreshSuccess = false;
        isLoadSuccess = false;
    }

    /**
     * 执行滑动
     *
     * @param dy
     */
    private void performScroll(int dy) {
        float damp = 0.5f;
        int dampNum = (int) (-dy * damp);
        scrollBy(0, dampNum);
    }

    @Override
    public void scrollTo(int x, int y) {
        if (isPullDown) {
            if (y > 0)
                y = 0;
        } else {
            if (y < 0)
                y = 0;
        }
        super.scrollTo(x, y);
    }

    public void setPullHeader(PullHead pullHeader) {
        this.pullHeader = pullHeader;
        addHeaderView(pullHeader);
    }

    public void setPullFooter(PullFoot pullFooter) {
        this.pullFooter = pullFooter;
        addFooterView(pullFooter);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    public void init() {
        if(pullHeader==null){
            pullHeader= new HeadView(getContext());
        }
        if(pullFooter==null){
            pullFooter = new FootView(getContext());
        }
        setPullHeader(pullHeader);
        setPullFooter(pullFooter);
    }

}
