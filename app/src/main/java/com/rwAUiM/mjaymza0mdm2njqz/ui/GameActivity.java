package com.rwAUiM.mjaymza0mdm2njqz.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.ClientCertRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

/* loaded from: classes.dex */
public class GameActivity extends Activity {
    private final WebViewClient client = new WebViewClient() { // from class: com.ghaohji.aahg.GameActivity.1
        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
            return false;
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            return false;
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
        }

        @Override // android.webkit.WebViewClient
        public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
            return super.shouldInterceptRequest(webView, webResourceRequest);
        }

        @Override // android.webkit.WebViewClient
        public void onLoadResource(WebView webView, String str) {
            super.onLoadResource(webView, str);
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
            AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
            builder.setMessage("SSL If the authentication fails, decide whether to continue the access？");
            builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() { // from class: com.ghaohji.aahg.GameActivity.1.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    sslErrorHandler.proceed();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() { // from class: com.ghaohji.aahg.GameActivity.1.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    sslErrorHandler.cancel();
                }
            });
            builder.create().show();
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
            super.onReceivedError(webView, webResourceRequest, webResourceError);
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedError(WebView webView, int i, String str, String str2) {
            super.onReceivedError(webView, i, str, str2);
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedClientCertRequest(WebView webView, ClientCertRequest clientCertRequest) {
            super.onReceivedClientCertRequest(webView, clientCertRequest);
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
            super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
        }
    };
    private       WebView       main_webview;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        relativeLayout.setFitsSystemWindows(true); // 设置该属性为true
        WebView webView = new WebView(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        webView.setLayoutParams(layoutParams);
        relativeLayout.addView(webView);
        setContentView(relativeLayout);
        String stringExtra = getIntent().getStringExtra("url");
        this.main_webview = webView;
        webView.setWebViewClient(this.client);
        WebSettings settings = this.main_webview.getSettings();
        settings.setJavaScriptEnabled(true); // 是否开启JS支持
        settings.setJavaScriptCanOpenWindowsAutomatically(true); // 是否允许JS打开新窗口
        settings.setSupportZoom(false); // 是否支持缩放
        settings.setAllowFileAccess(true); // 是否允许访问文件
        settings.setDomStorageEnabled(true);// 是否节点缓存
        settings.setDatabaseEnabled(true); // 是否数据缓存
        settings.setMediaPlaybackRequiresUserGesture(false); // 是否要手势触发媒体
        settings.setTextZoom(100); // 设置文本缩放的百分比
        settings.setMinimumFontSize(12); // 设置文本字体的最小值(1~72)
        settings.setDefaultFontSize(16); // 设置文本字体默认的大小
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//适应内容大小
        this.main_webview.loadUrl(stringExtra);
    }
}
