package com.rwAUiM.mjaymza0mdm2njqz.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
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
    private static final String TAG       = "GameActivity";
    private final WebViewClient client = new WebViewClient() { // from class: com.ghaohji.aahg.GameActivity.1
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.e(TAG, "shouldOverrideUrlLoading: " + request.getUrl().toString());
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.e(TAG, "onPageStarted: " + url);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.e(TAG, "onPageFinished: " + url);
            super.onPageFinished(view, url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            Log.e(TAG, "onLoadResource: " + url);
            super.onLoadResource(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            Log.e(TAG, "onReceivedError: " + error.getDescription());
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            Log.e(TAG, "onReceivedHttpError: " + errorResponse.getReasonPhrase());
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            Log.e(TAG, "onReceivedSslError: " + error.toString());
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
            Log.e(TAG, "onReceivedClientCertRequest: " + request.toString());
            super.onReceivedClientCertRequest(view, request);
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            Log.e(TAG, "onReceivedHttpAuthRequest: " + host);
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
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
        settings.setJavaScriptEnabled(true); // 是否开启JS支持 **
        settings.setJavaScriptCanOpenWindowsAutomatically(true); // 是否允许JS打开新窗口 **
        settings.setSupportZoom(false); // 是否支持缩放 **
        settings.setLoadsImagesAutomatically(true);// 自动加载 **
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
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//适应内容大小**
        this.main_webview.loadUrl(stringExtra);
    }
}
