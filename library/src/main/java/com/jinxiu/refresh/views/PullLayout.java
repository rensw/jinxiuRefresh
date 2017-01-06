package com.jinxiu.refresh.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.jinxiu.refresh.inter.OnPullListener;
import com.jinxiu.refresh.inter.PullStates;

/**
 * Created by apple on 17/1/5.
 * 下拉滑动 用属性动画来实现
 */
public class PullLayout extends InterceptLayout {

    public OnPullListener onPullListener;
    //layout状态
    public PullStates pullStates = PullStates.DEFAULT;
    //阻尼系数0.5f
    private float damp = 0.5f;
    //恢复动画的执行时间
    public int SCROLL_TIME = 300;
    //是否刷新完成
    private boolean isRefreshSuccess = false;
    //是否加载完成
    private boolean isLoadSuccess = false;

    public PullLayout(Context context) {
        super(context);
    }

    public PullLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OnPullListener getOnPullListener() {
        return onPullListener;
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
                            updateStatus(PullStates.DOWN_AFTER);
                        } else {
                            updateStatus(PullStates.DOWN_BEFORE);
                        }
                    }
                } else if (dy < 0) {   //上拉刷新
                    if (footer != null) {
                        if (!isLoadMore) {
                            return false;
                        }
                        performScroll(dy);
                        if (getScrollY() > bottomScroll + footer.getMeasuredHeight()) {
                            updateStatus(PullStates.UP_AFTER);
                        } else {
                            updateStatus(PullStates.UP_BEFORE);
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
                    case DOWN_BEFORE:
                        scrolltoDefaultStatus(PullStates.REFRESH_CANCEL_SCROLLING);
                        break;
                    case UP_AFTER:
                        scrolltoLoadStatus();
                        break;
                    case UP_BEFORE:
                        scrolltoDefaultStatus(PullStates.LOADMORE_CANCEL_SCROLLING);
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
        performAnimal(start, end, new AnimListener() {
            @Override
            public void onDoing() {
                updateStatus(PullStates.LOADMORE_SCROLLING);
            }

            @Override
            public void onEnd() {
                updateStatus(PullStates.LOADMORE_DOING);
            }
        });
    }

    /**
     * //滚动到加载状态
     */
    private void scrolltoRefreshStatus() {
        int start = getScrollY();
        int end = -header.getMeasuredHeight();
        performAnimal(start, end, new AnimListener() {
            @Override
            public void onDoing() {
                updateStatus(PullStates.REFRESH_SCROLLING);
            }

            @Override
            public void onEnd() {
                updateStatus(PullStates.REFRESH_DOING);
            }
        });
    }

    /**
     * 滚动到默认状态
     */
    private void scrolltoDefaultStatus(final PullStates startStatus) {

        int start = getScrollY();
        int end = 0;
        performAnimal(start, end, new AnimListener() {
            @Override
            public void onDoing() {
                updateStatus(startStatus);
            }

            @Override
            public void onEnd() {
                updateStatus(PullStates.DEFAULT);
            }
        });
    }

    //停止刷新
    public void stopRefresh() {
        isRefreshSuccess = true;
        scrolltoDefaultStatus(PullStates.REFRESH_COMPLETE_SCROLLING);
    }

    //停止加载更多
    public void stopLoadMore() {
        isLoadSuccess = true;
        scrolltoDefaultStatus(PullStates.LOADMORE_COMPLETE_SCROLLING);
    }

    /**
     * 执行动画
     *
     * @param start
     * @param end
     */
    private void performAnimal(int start, int end, final AnimListener listener) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(SCROLL_TIME).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                scrollTo(0, value);
                postInvalidate();
                listener.onDoing();
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

    interface AnimListener {
        void onDoing();

        void onEnd();
    }

    /**
     * 更新刷新状态
     *
     * @param pullStates
     */
    private void updateStatus(PullStates pullStates) {
        this.pullStates = pullStates;
        int scrollY = getScrollY();
        switch (pullStates) {
            case DEFAULT:  //默认
                onDefault();
                break;
            case DOWN_AFTER:
                header.onDownAfter(scrollY);
                break;
            case DOWN_BEFORE:
                header.onDownBefore(scrollY);
                break;
            case REFRESH_SCROLLING:
                header.onRefreshScrolling(scrollY);
                break;
            case REFRESH_DOING:
                header.onRefreshDoing(scrollY);
                if (onPullListener != null)
                    onPullListener.onRefresh();
                break;
            case REFRESH_COMPLETE_SCROLLING:
                header.onRefreshCompleteScrolling(scrollY, isRefreshSuccess);
                break;
            case REFRESH_CANCEL_SCROLLING:
                header.onRefreshCancelScrolling(scrollY);
                break;
            case UP_BEFORE:
                footer.onUpBefore(scrollY);
                break;
            case UP_AFTER:
                footer.onUpAfter(scrollY);
                break;
            case LOADMORE_SCROLLING:
                footer.onLoadScrolling(scrollY);
                break;
            case LOADMORE_DOING:
                footer.onLoadDoing(scrollY);
                if (onPullListener != null)
                    onPullListener.onLoadMore();
                break;
            case LOADMORE_COMPLETE_SCROLLING:
                footer.onLoadCompleteScrolling(scrollY, isLoadSuccess);
                break;
            case LOADMORE_CANCEL_SCROLLING:
                footer.onLoadCancelScrolling(scrollY);
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
}
