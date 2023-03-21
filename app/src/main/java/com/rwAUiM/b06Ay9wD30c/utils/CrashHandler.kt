package com.rwAUiM.b06Ay9wD30c.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.rwAUiM.b06Ay9wD30c.ui.CrashActivity

/**
 * Create by LinJi
 * 2022-07-29 10:12
 */
@SuppressLint("StaticFieldLeak")
object CrashHandler : Thread.UncaughtExceptionHandler {
    private var mContext: Context? = null
    private var defaultUncaughtExceptionHandler: Thread.UncaughtExceptionHandler? = null

    /* 初始化 */
    fun init(context: Context?) {
        mContext = context
        Thread.setDefaultUncaughtExceptionHandler(this)
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
    }

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        ex.printStackTrace()
        Log.e("APP", "CrashHandler: ", ex)
        CrashActivity.start(mContext!!, ex)
    }
}