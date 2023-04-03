package com.rwAUiM.mjaymza0mdm2njqz.utils;

import android.text.TextUtils;
import android.util.Log;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpUtils {

    private static OkHttpUtils instance;
    private OkHttpClient okHttpClient;

    private OkHttpUtils() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(15, TimeUnit.SECONDS);
        builder.writeTimeout(15, TimeUnit.SECONDS);
        //添加日志拦截器，输出请求和响应信息
        builder.addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i("OkHttp", message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY));
        okHttpClient = builder.build();
    }

    public static OkHttpUtils getInstance() {
        if (instance == null) {
            synchronized (OkHttpUtils.class) {
                if (instance == null) {
                    instance = new OkHttpUtils();
                }
            }
        }
        return instance;
    }

    public void get(String url, Callback callback) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

    public void post(String url, String json, Callback callback) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

    public void postForm(String url, Map<String, String> params, Callback callback) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }
}
