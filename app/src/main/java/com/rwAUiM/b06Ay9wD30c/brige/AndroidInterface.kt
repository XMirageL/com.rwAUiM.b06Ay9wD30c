package com.rwAUiM.b06Ay9wD30c.brige

import android.content.Context
import android.webkit.JavascriptInterface
import com.just.agentweb.AgentWeb
import com.rwAUiM.b06Ay9wD30c.utils.LogEventUtils.statisticalEvent

class AndroidInterface {
    @JavascriptInterface
    fun zzzz(name: String?, content: String?, username: String?) {
        statisticalEvent(name ?: "", content ?: "", username ?: "")
    }
}