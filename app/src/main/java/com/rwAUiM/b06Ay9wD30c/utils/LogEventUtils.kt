package com.rwAUiM.b06Ay9wD30c.utils

import android.os.Bundle
import android.util.Log
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener


object LogEventUtils {
    fun statisticalEvent(name: String, content: String, username: String) {
        val bundle = Bundle()
        bundle.putString("event_value", content)
        bundle.putString("username", username)
        val eventValues = HashMap<String, Any>()
        eventValues["content"] = content
        AppsFlyerLib.getInstance().logEvent(
            Utils.app,
            name, eventValues,
            object : AppsFlyerRequestListener {
                override fun onSuccess() {
                    Log.d("statisticalEvent", "Event sent successfully")
                }

                override fun onError(errorCode: Int, errorDesc: String) {
                    Log.d(
                        "statisticalEvent", "Event failed to be sent:\n" +
                                "Error code: " + errorCode + "\n"
                                + "Error description: " + errorDesc
                    )
                }
            }
        )
    }
}