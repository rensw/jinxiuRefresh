package com.jinxiu.refresh.views;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.jinxiu.refresh.PullFoot;
import com.jinxiu.refresh.R;


/**
 * Created by apple on 17/1/5.
 * 加载更多View
 */
public class FootView extends PullFoot {

    private TextView textView;
    String loadBeforeStr = "上拉加载更多";
    String loadAfterStr = "松开加载更多";
    String loadScrollingStr = "准备加载";
    String loadDoingStr = "正在加载……";
    String loadCompleteStr = "加载完成";
    String loadCancelStr = "加载取消";

    public FootView(Context context) {
        super(context);
        View footView = View.inflate(context, R.layout.layout_footer, this);
        textView = (TextView) footView.findViewById(R.id.tv);
    }

    @Override
    public void onUpBefore(int scrollY) {
        textView.setText(loadBeforeStr);
    }

    @Override
    public void onUpAfter(int scrollY) {
        textView.setText(loadAfterStr);
    }

    @Override
    public void onLoadScrolling(int scrollY) {
        textView.setText(loadScrollingStr);
    }

    @Override
    public void onLoadDoing(int scrollY) {
        textView.setText(loadDoingStr);
    }

    @Override
    public void onLoadCompleteScrolling(int scrollY, boolean isLoadSuccess) {
        textView.setText(loadCompleteStr);
    }

    @Override
    public void onLoadCancelScrolling(int scrollY) {
        textView.setText(loadCancelStr);
    }
}
