package com.rwAUiM.b06Ay9wD30c.utils

import android.os.Bundle
import android.util.Log
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.hjq.toast.Toaster


object LogEventUtils {
    fun statisticalEvent(name: String, data: Map<String, Any>) {
        AppsFlyerLib.getInstance().logEvent(
            Utils.app,
            name, data,
            object : AppsFlyerRequestListener {
                override fun onSuccess() {
//                    Toaster.show("Event sent successfully:{$name:$data}")
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