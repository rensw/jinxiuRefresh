package com.jinxiu.refresh.views;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ScrollView;

import java.lang.reflect.Field;

/**
 * Created by apple on 17/1/5.
 * 下拉刷新拦截
 */
public class InterceptLayout extends RefreshView {

    // 用于计算滑动距离的Y坐标中介
    public int lastYmove;
    // 用于判断是否拦截触摸事件的Y坐标中介
    public int lastYIntercept;

    public boolean isPullDown=true;

    public InterceptLayout(Context context) {
        super(context);
    }

    public InterceptLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = false;
        // 记录此次触摸事件的y坐标
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastYmove = y;
                isIntercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (y > lastYIntercept) {  //下拉
                    View child = getFirstVisiableView();
                    if (child == null)
                        isIntercept = false;
                    else if (child instanceof AdapterView) {
                        isIntercept = avPullDownIntercept(child);
                    } else if (child instanceof ScrollView) {
                        isIntercept = svPullDownIntercept(child);
                    } else if (child instanceof RecyclerView) {
                        isIntercept = rvPullDownIntercept(child);
                    } else if (child instanceof WebView) {
                        isIntercept = wvPullDownIntercept(child);
                    } else {
                        isIntercept = true;
                    }
                    isPullDown=true;
                } else if (y < lastYIntercept) { //上拉

                    View child = getLastVisiableView();
                    if (child == null)
                        isIntercept = false;
                    else if (child instanceof AdapterView) {
                        isIntercept = avLoadMoreIntercept(child);
                    } else if (child instanceof ScrollView) {
                        isIntercept = svLoadMoreIntercept(child);
                    } else if (child instanceof RecyclerView) {
                        isIntercept = rvLoadMoreIntercept(child);
                    } else if (child instanceof WebView) {
                        isIntercept = wvLoadMoreIntercept(child);
                    } else {
                        isIntercept = true;
                    }
                    isPullDown=false;
                } else {
                    isIntercept = false;
                }
                break;
        }
        lastYIntercept = y;
        return isIntercept;
    }

    public View getLastVisiableView() {
        for (int i = lastChildIndex; i >= 0; i--) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            } else {
                return child;
            }
        }
        return null;
    }

    public View getFirstVisiableView() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            } else {
                return child;
            }
        }
        return null;
    }

    private boolean avPullDownIntercept(View child) {
        boolean isIntercept = true;
        AdapterView adapterView = (AdapterView) child;
        if (adapterView.getFirstVisiblePosition() != 0 || adapterView.getChildAt(0).getTop() != 0) {
            isIntercept = false;
        }
        return isIntercept;
    }

    private boolean svPullDownIntercept(View child) {
        boolean isIntercept = false;
        ScrollView scrollView = (ScrollView) child;
        if (scrollView.getScrollY() == 0) {
            isIntercept = true;
        }
        return isIntercept;
    }

    private boolean rvPullDownIntercept(View child) {
        RecyclerView recyclerView= (RecyclerView) child;
        if (recyclerView != null) {
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager == null) {
                return true;
            }
            if (manager.getItemCount() == 0) {
                return true;
            }

            if (manager instanceof LinearLayoutManager) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) manager;

                int firstChildTop = 0;
                if (recyclerView.getChildCount() > 0) {
                    // 处理item高度超过一屏幕时的情况
                    View firstVisibleChild = recyclerView.getChildAt(0);
                    if (firstVisibleChild != null && firstVisibleChild.getMeasuredHeight() >= recyclerView.getMeasuredHeight()) {
                        if (android.os.Build.VERSION.SDK_INT < 14) {
                            return !(ViewCompat.canScrollVertically(recyclerView, -1) || recyclerView.getScrollY() > 0);
                        } else {
                            return !ViewCompat.canScrollVertically(recyclerView, -1);
                        }
                    }

                    // 如果RecyclerView的子控件数量不为0，获取第一个子控件的top

                    // 解决item的topMargin不为0时不能触发下拉刷新
                    View firstChild = recyclerView.getChildAt(0);
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) firstChild.getLayoutParams();
                    firstChildTop = firstChild.getTop() - layoutParams.topMargin - getRecyclerViewItemTopInset(layoutParams) - recyclerView.getPaddingTop();
                }

                if (layoutManager.findFirstCompletelyVisibleItemPosition() < 1 && firstChildTop == 0) {
                    return true;
                }
            } else if (manager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;

                int[] out = layoutManager.findFirstCompletelyVisibleItemPositions(null);
                if (out[0] < 1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 通过反射获取RecyclerView的item的topInset
     *
     * @param layoutParams
     * @return
     */
    private static int getRecyclerViewItemTopInset(RecyclerView.LayoutParams layoutParams) {
        try {
            Field field = RecyclerView.LayoutParams.class.getDeclaredField("mDecorInsets");
            field.setAccessible(true);
            // 开发者自定义的滚动监听器
            Rect decorInsets = (Rect) field.get(layoutParams);
            return decorInsets.top;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private boolean avLoadMoreIntercept(View child) {
        boolean isIntercept = false;
        AdapterView adapterView = (AdapterView) child;
        if (adapterView != null && adapterView.getAdapter() != null && adapterView.getChildCount() > 0 && adapterView.getLastVisiblePosition() == adapterView.getAdapter().getCount() - 1) {
            if (adapterView.getChildAt(adapterView.getChildCount() - 1).getBottom() <= adapterView.getMeasuredHeight())
                isIntercept = true;
        }
        return isIntercept;
    }

    private boolean svLoadMoreIntercept(View child) {
        boolean isIntercept = false;
        ScrollView scrollView = (ScrollView) child;
        View scrollChild = scrollView.getChildAt(0);

        if (scrollView.getScrollY() >= (scrollChild.getHeight() - scrollView.getHeight())) {
            isIntercept = true;
        }
        return isIntercept;
    }

    private boolean rvLoadMoreIntercept(View child) {

        boolean isIntercept = false;
        RecyclerView recyclerChild = (RecyclerView) child;
        if (recyclerChild.computeVerticalScrollExtent() + recyclerChild.computeVerticalScrollOffset()
                >= recyclerChild.computeVerticalScrollRange())
            isIntercept = true;
        return isIntercept;
    }

    private boolean wvLoadMoreIntercept(View child) {

        boolean isIntercept = false;
        WebView webView = (WebView) child;
        if (webView.getContentHeight() * 3 == webView.getScrollY() + webView.getHeight())
            isIntercept = true;
        return isIntercept;
    }

    private boolean wvPullDownIntercept(View child) {
        boolean isIntercept = false;
        WebView webView = (WebView) child;
        if (webView.getScrollY() == 0)
            isIntercept = true;
        return isIntercept;
    }
}
