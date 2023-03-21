package com.rwAUiM.b06Ay9wD30c.ui;


import android.Manifest;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.rwAUiM.b06Ay9wD30c.R;
import com.rwAUiM.b06Ay9wD30c.brige.AndroidInterface;
import com.rwAUiM.b06Ay9wD30c.utils.BarUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class WebActivity extends AppCompatActivity {


    private String   url;
    public  AgentWeb mAgentWeb;
    Button web_btn;
    private       Boolean isCanBack   = false;
    private       Boolean showToolBar = false;
    private final String  TAG         = "WebActivity";

    //这是要注入的javascript，注意：前面的“javascript：”是必须的，后面就是要注入的语句
    private static final String  insertJavaScript     = "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no\">\n";
    private              boolean isGetStepPermissions = false;

    public static class MessageEvent {
        public static final int    GET_LOCATION   = 0x0001;
        public static final int    TO_LOGIN       = 0x0002;
        public static final int    CALLBACK_STEP  = 0x0003;
        public static final int    CALLBACK_STEP2 = 0x0004;
        public              int    code;
        public              Object data;

        public MessageEvent(int code) {
            this.code = code;
        }

        public MessageEvent(int code, Object data) {
            this.code = code;
            this.data = data;
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(MessageEvent event) {
//        switch (event.code) {
//            case MessageEvent.GET_LOCATION:
//                startGetLocation();
//                break;
//            case MessageEvent.TO_LOGIN:
//                DataStoreUtils.getInstance().putValue("TOKEN_DATA", "");
//                Intent intent = new Intent(WebActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//            case MessageEvent.CALLBACK_STEP:
//                mAgentWeb.getJsAccessEntrace().quickCallJs("onGetStep", String.valueOf(event.data));
//                break;
//            case MessageEvent.CALLBACK_STEP2:
//                Map<String, Object> map = new HashMap<>();
//                map.put("todayAllStep", event.data);
//                map.put("todayValidSteps", event.data);
//                map.put("todayCoveredSteps", 0);
//                map.put("todayAllDistance", 0);
//                map.put("lastUpdateDay", 0);
//                mAgentWeb.getJsAccessEntrace().quickCallJs("androidStep", new Gson().toJson(map));
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        EventBus.getDefault().unregister(this);
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.INSTANCE.immerseStatusBar(this);
        setContentView(R.layout.activity_web);
        BarUtils.INSTANCE.setStatusBarLightMode(this, true);
        BarUtils.INSTANCE.setNavBarColor(this, Color.WHITE);
        BarUtils.INSTANCE.setPaddingSmart(this, findViewById(R.id.title_bar));


        //测试
//        url = "file:android_asset/camera.html";
        url = getIntent().getStringExtra("KEY_URL");
        View view = findViewById(R.id.webview2);
        web_btn = findViewById(R.id.web_btn);
        if (url.contains("download") || url.contains(".apk") || url.startsWith("https://play.google.com")
                || url.contains("_apk") || url.startsWith("https://t.me")
                || url.startsWith("w hatsapp://") || url.startsWith("https://wa.me")) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                web_btn.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent((LinearLayout) view, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件。
                .closeIndicator()//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK) //严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
                .interceptUnkownUrl() //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
                .createAgentWeb()//创建AgentWeb。
                .ready()//设置 WebSettings。
                .go(url); //WebView载入该url地址的页面并显示。

        // mAgentWeb初始化完成后即可注入，第一个参数为js中可直接调用的对象名，第二个三处为步骤1中的JAVA对象，yu可以以是任意名字，js与这里统一就行了
        mAgentWeb.getJsInterfaceHolder().addJavaObject("yyyy", new AndroidInterface());

        mAgentWeb.getWebCreator().getWebView().setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        mAgentWeb.getAgentWebSettings().getWebSettings().setJavaScriptEnabled(true); // 是否开启JS支持
        mAgentWeb.getAgentWebSettings().getWebSettings().setJavaScriptCanOpenWindowsAutomatically(true); // 是否允许JS打开新窗口
        mAgentWeb.getAgentWebSettings().getWebSettings().setSupportZoom(false); // 是否支持缩放
        mAgentWeb.getAgentWebSettings().getWebSettings().setAllowFileAccess(true); // 是否允许访问文件
        mAgentWeb.getAgentWebSettings().getWebSettings().setDomStorageEnabled(true);// 是否节点缓存
        mAgentWeb.getAgentWebSettings().getWebSettings().setDatabaseEnabled(true); // 是否数据缓存
        mAgentWeb.getAgentWebSettings().getWebSettings().setMediaPlaybackRequiresUserGesture(false); // 是否要手势触发媒体
        mAgentWeb.getAgentWebSettings().getWebSettings().setTextZoom(100); // 设置文本缩放的百分比
        mAgentWeb.getAgentWebSettings().getWebSettings().setMinimumFontSize(12); // 设置文本字体的最小值(1~72)
        mAgentWeb.getAgentWebSettings().getWebSettings().setDefaultFontSize(16); // 设置文本字体默认的大小
        mAgentWeb.getAgentWebSettings().getWebSettings().setAllowUniversalAccessFromFileURLs(true);
        mAgentWeb.getAgentWebSettings().getWebSettings().setAllowFileAccessFromFileURLs(true);
        mAgentWeb.getAgentWebSettings().getWebSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//适应内容大小
        mAgentWeb.getAgentWebSettings().getWebSettings().setLoadWithOverviewMode(true);
        mAgentWeb.getAgentWebSettings().getWebSettings().setUseWideViewPort(true);


        mAgentWeb.getWebCreator().getWebView().setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.contains("vegas0")) {
                    imgReset();
                }
            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                try {
                    if (failingUrl.contains("download") || failingUrl.contains(".apk") || failingUrl.startsWith("https://play.google.com")
                            || failingUrl.contains("_apk") || failingUrl.startsWith("https://t.me")
                            || failingUrl.startsWith("whatsapp://") || failingUrl.startsWith("https://wa.me")
                            || failingUrl.startsWith("https://line.me") || failingUrl.startsWith("https://zalo.me")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(failingUrl));
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //处理网页加载失败时
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                try {
                    if (request.getUrl().getPath().contains("download") || request.getUrl().getPath().contains(".apk") || request.getUrl().getPath().startsWith("https://play.google.com")
                            || request.getUrl().getPath().contains("_apk") || request.getUrl().getPath().startsWith("https://t.me")
                            || request.getUrl().getPath().startsWith("whatsapp://") || request.getUrl().getPath().startsWith("https://wa.me")
                            || request.getUrl().getPath().startsWith("https://line.me") || request.getUrl().getPath().startsWith("https://zalo.me")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(request.getUrl().getPath()));
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url == null) return false;
                Log.e("WebActivity", "shouldOverrideUrlLoading: " + url);
                try {
                    if (url.contains("download") || url.contains(".apk") || url.contains("_apk")
                            || url.startsWith("https://wa.me") || url.startsWith("whatsapp://")
                            || url.startsWith("https://t.me") || url.startsWith("https://play.google.com")
                            || url.startsWith("https://line.me") || url.startsWith("https://zalo.me") || !url.startsWith("http")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (url.startsWith("http")) {
                    view.loadUrl(url);
                }
                return true;
            }
        });
        mAgentWeb.getWebCreator().getWebView().setWebChromeClient(new WebChromeClient() {

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, true);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = valueCallback;
                requestPermission(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 0x0001);

                return true;
            }
        });
        mAgentWeb.getWebCreator().getWebView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mAgentWeb.getWebCreator().getWebView().canGoBack()) { // 表示按返回键时的操作
                        mAgentWeb.getWebCreator().getWebView().goBack(); // 后退
                        // webview.goForward();//前进
                        return true; // 已处理
                    } else if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (isCanBack) {
                            WebActivity.super.onBackPressed();
                        }
                    }
                }
                return false;
            }
        });
        mAgentWeb.getWebCreator().getWebView().setDownloadListener(new DownloadListener() {

            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Log.e(TAG, "onDownloadStart: " + url);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(
                        DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,    //Download folder
                        url.substring(url.lastIndexOf('/') + 1));                        //Name of file
                DownloadManager dm = (DownloadManager) getSystemService(
                        DOWNLOAD_SERVICE);
                dm.enqueue(request);
            }
        });
        web_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }


    private void startGetLocation() {
        // 获取权限
        requestPermission(new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
        }, REQUEST_CODE_LOCATION);
    }

    private void imgReset() {
        mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                "    img.style.maxWidth = '70%'; img.style.height = 'auto';  " +
                "}" +
                "})()");
    }

    @Override
    public void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();//恢复
        super.onResume();
    }

    @Override
    public void onPause() {
        mAgentWeb.getWebLifeCycle().onPause(); //暂停应用内所有WebView ， 调用mWebView.resumeTimers();/mAgentWeb.getWebLifeCycle().onResume(); 恢复。
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }

    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private Uri                  imageUri;
    private int                  REQUEST_CODE = 1234;

    public void toAppSetting() {
        isGetStepPermissions = true;
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        mIntent.setData(Uri.fromParts("package", WebActivity.this.getPackageName(), null));
        startActivity(mIntent);
        Toast.makeText(WebActivity.this, "Please grant step counting permission.", Toast.LENGTH_SHORT).show();
    }

    /**
     * 调用相机
     */
    private void takePhoto() {
        // 指定拍照存储位置的方式调起相机
        String filePath = Environment.getExternalStorageDirectory() + File.separator
                + Environment.DIRECTORY_PICTURES + File.separator;
        String fileName = "IMG_" + DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        imageUri = Uri.fromFile(new File(filePath + fileName));

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        Intent Photo = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent chooserIntent = Intent.createChooser(Photo, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});

        startActivityForResult(chooserIntent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (mUploadCallbackAboveL != null) {
                chooseAbove(resultCode, data);
            } else {
                Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * Android API >= 21(Android 5.0) 版本的回调处理
     *
     * @param resultCode 选取文件或拍照的返回码
     * @param data       选取文件或拍照的返回结果
     */
    private void chooseAbove(int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            updatePhotos();
            if (data != null) {
                // 这里是针对从文件中选图片的处理
                Uri[] results;
                Uri uriData = data.getData();
                if (uriData != null) {
                    results = new Uri[]{uriData};
                    mUploadCallbackAboveL.onReceiveValue(results);
                } else {
                    mUploadCallbackAboveL.onReceiveValue(null);
                }
            } else {
                mUploadCallbackAboveL.onReceiveValue(new Uri[]{imageUri});
            }
        } else {
            mUploadCallbackAboveL.onReceiveValue(null);
        }
        mUploadCallbackAboveL = null;
    }

    private void updatePhotos() {
        // 该广播即使多发（即选取照片成功时也发送）也没有关系，只是唤醒系统刷新媒体文件
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(imageUri);
        sendBroadcast(intent);
    }


    private       int REQUEST_CODE_PERMISSION = 0x00099;
    private final int REQUEST_CODE_LOCATION   = 0x0002;


    /**
     * 请求权限
     *
     * @param permissions 请求的权限
     * @param requestCode 请求权限的请求码
     */
    public void requestPermission(String[] permissions, int requestCode) {
        this.REQUEST_CODE_PERMISSION = requestCode;
        if (checkPermissions(permissions)) {
            permissionSuccess(REQUEST_CODE_PERMISSION);
        } else {
            List<String> needPermissions = getDeniedPermissions(permissions);
            ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), REQUEST_CODE_PERMISSION);
        }
    }

    /**
     * 检测所有的权限是否都已授权
     *
     * @param permissions
     * @return
     */
    private boolean checkPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    private List<String> getDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }


    /**
     * 系统请求权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (verifyPermissions(grantResults)) {
                permissionSuccess(REQUEST_CODE_PERMISSION);
            } else {
                permissionFail(REQUEST_CODE_PERMISSION);
                showTipsDialog();
            }
        }
    }

    /**
     * 确认所有的权限是否都已授权
     *
     * @param grantResults
     * @return
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 显示提示对话框
     */
    private void showTipsDialog() {
        String title = "The current app lacks the necessary permissions and this feature is temporarily unavailable. If necessary, please click the [OK] button to go to the setting center for authorization authorization.";
        new AlertDialog.Builder(this)
                .setTitle("Hint")
                .setMessage(title)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                }).show();
    }

    /**
     * 启动当前应用设置页面
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    /**
     * 获取权限成功
     *
     * @param requestCode
     */
    public void permissionSuccess(int requestCode) {
        switch (requestCode) {
            default:
                takePhoto();
        }

    }

    /**
     * 权限获取失败
     *
     * @param requestCode
     */
    public void permissionFail(int requestCode) {
        if (mUploadCallbackAboveL != null)
            mUploadCallbackAboveL.onReceiveValue(null);
        mUploadCallbackAboveL = null;
    }


    @Override
    public void onBackPressed() {
        if (isCanBack && !mAgentWeb.getWebCreator().getWebView().canGoBack()) {
            super.onBackPressed();
        }
    }
}