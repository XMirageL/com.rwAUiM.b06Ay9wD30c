package com.rwAUiM.mjaymza0mdm2njqz.brige

import android.util.Log
import android.webkit.JavascriptInterface
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.rwAUiM.mjaymza0mdm2njqz.utils.LogEventUtils.statisticalEvent
import java.lang.Exception


class AndroidInterface {
    @JavascriptInterface
    fun onEvent(data: String) {
        if (data.isEmpty()) {
            return
        }
        try {
            val element = JsonParser.parseString(data)
            val eventObject = element.asJsonObject
            val eventName = eventObject["eventName"].asString
            val eventValueElement = eventObject["eventValue"]
            val eventValue = Gson().fromJson<Map<String, Any>>(
                eventValueElement,
                object : TypeToken<Map<String?, Any?>?>() {}.type
            )
            statisticalEvent(eventName, eventValue)
        } catch (e:Exception) {
            Log.e("AndroidInterface", "onEvent: ", e)
        }
    }
}