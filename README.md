# jinxiuRefresh
Android上下拉刷新组件
  
#  使用
 1.布局文件中添加 RefreshLayout
  ```
  <com.jinxiu.refresh.views.RefreshLayout
        android:id="@+id/refresh_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
          <View
            android:id="@+id/id"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
           />
  </com.jinxiu.refresh.views.RefreshLayout>
  ```
  
 2.设置下拉和上拉加载是否可用及监听  
  
  ```
     refreshLayout.setIsPullEnable(true);
     refreshLayout.setIsLoadMore(false);
     refreshView.setOnPullListener(new OnPullListener() {
            @Override
            public void onRefresh() {
                        refreshView.stopRefresh();
            }

            @Override
            public void onLoadMore() {
                        refreshView.stopLoadMore();
            }
     });
   ```
    
 3.自定义刷新头部和上拉底部 可在HeadView.class和FootView.class直接修改
     
# Preview
![Preview](http://im2.ezgif.com/tmp/ezgif.com-0bca7b6483.gif)
![Preview](http://im2.ezgif.com/tmp/ezgif.com-c42f9aa64d.gif)
![Preview](http://im2.ezgif.com/tmp/ezgif.com-1535adc674.gif)
![Preview](http://im2.ezgif.com/tmp/ezgif.com-2366286641.gif)
![Preview](http://im2.ezgif.com/tmp/ezgif.com-46ea663ae1.gif)
  
