package com.jinxiu.refreshDemo.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jinxiu.refreshDemo.inter.WebViewLoadComplete;


/**
 * 作者：rsw
 * 介绍：自定义带滚动的webview
 * 时间：2016/3/21
 */
public class ProgressWebView extends WebView {

    public boolean IsLoadingFinish = false;
    private Context context;
    private WebViewLoadComplete webViewLoadCompleteListener;
    private WebSettings settings;
    private WebViewProgressBar progressBar;//进度条的矩形（进度线）
    private Handler handler;

    public void setWebviewLoadCompleteListener(WebViewLoadComplete webViewLoadCompleteListener) {
        this.webViewLoadCompleteListener = webViewLoadCompleteListener;
    }

    public ProgressWebView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        //实例化进度条
        progressBar = new WebViewProgressBar(context);
        //设置进度条的size
        progressBar.setLayoutParams(new ViewGroup.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //刚开始时候进度条不可见
        progressBar.setVisibility(GONE);
        //把进度条添加到webView里面
        addView(progressBar);
        //初始化handle
        handler = new Handler();
        initSetting();
        setWebViewClient(new WebViewClient());
        setWebChromeClient(new WebChromeClient());
        setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
    }

    private void initSetting() {
        settings = getSettings();
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setSavePassword(false);
        settings.setJavaScriptEnabled(true);
        settings.setSaveFormData(true);
        // 设置可以支持缩放
        settings.setSupportZoom(true);
        settings.setDisplayZoomControls(false);
        // 设置出现缩放工具
        settings.setBuiltInZoomControls(true);
        // 自适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        //如果访问的页面中有Javascript，则webView必须设置支持Javascript
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("提示").setPositiveButton("确认", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressBar.setProgress(100);
                handler.postDelayed(runnable, 200);//0.2秒后隐藏进度条
                IsLoadingFinish = true;
                if (webViewLoadCompleteListener != null) {
                    webViewLoadCompleteListener.webViewLoadComplete();
                }
                getSettings().setBlockNetworkImage(false);
            } else if (progressBar.getVisibility() == GONE) {
                progressBar.setVisibility(VISIBLE);
            }
            //设置初始进度10，这样会显得效果真一点，总不能从1开始吧
            if (newProgress < 10) {
                newProgress = 10;
            }
            //不断更新进度
            progressBar.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }

    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return false;
        }

        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // 接受证书
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            String data = "找不到网页";
            view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            settings.setBlockNetworkImage(true);

        }

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
            requestFocus();
            requestFocusFromTouch();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    /**
     * 刷新界面（此处为加载完成后进度消失）
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            progressBar.setVisibility(View.GONE);
        }
    };
}
