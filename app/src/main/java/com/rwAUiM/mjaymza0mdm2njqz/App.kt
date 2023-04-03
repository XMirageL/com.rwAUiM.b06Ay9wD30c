package com.rwAUiM.mjaymza0mdm2njqz

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.hjq.toast.Toaster
import com.rwAUiM.mjaymza0mdm2njqz.utils.CrashHandler

/**
 * TODO tip 1：需要为项目准备一个 Application 来继承 BaseApplication
 * 以便在 Activity/Fragment 中享用 Application 级作用域的 event-ViewModel
 * event-ViewModel 的职责仅限于在 "跨页面通信" 的场景下，承担 "唯一可信源"，
 * 所有跨页面的 "状态同步请求" 都交由该可信源在内部决策和处理，并统一分发给所有订阅者页面。
 */
open class App : Application() {
    companion object {
        lateinit var mAppContext: Context
    }

    private var mApplicationProvider: ViewModelProvider? = null
    override fun onCreate() {
        super.onCreate()
        mAppContext = applicationContext
        CrashHandler.init(this)
        Toaster.init(this)
        val conversionListener: AppsFlyerConversionListener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(conversionData: Map<String, Any>) {
                for (attrName in conversionData.keys) {
                    Log.e("AppsFlyer", "attribute: " + attrName + " = " + conversionData[attrName])
                }
            }

            override fun onConversionDataFail(errorMessage: String) {
                Log.e("AppsFlyer", "error getting conversion data: $errorMessage")
            }

            override fun onAppOpenAttribution(conversionData: Map<String, String>) {
                for (attrName in conversionData.keys) {
                    Log.e("AppsFlyer", "attribute: " + attrName + " = " + conversionData[attrName])
                }
            }

            override fun onAttributionFailure(errorMessage: String) {
                Log.e("AppsFlyer", "error onAttributionFailure : $errorMessage")
            }
        }
        AppsFlyerLib.getInstance().init(
            resources.getString(R.string.apps_flyer_dev_key),
            conversionListener,
            applicationContext
        )
        AppsFlyerLib.getInstance().start(this)
    }
}