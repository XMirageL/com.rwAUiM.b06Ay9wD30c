package com.rwAUiM.mjaymza0mdm2njqz.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import java.lang.reflect.InvocationTargetException

class Utils {
    companion object {
        private var sApplication: Application? = null

        fun openUrlInBrowser(url:String, activity: Activity){
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            activity.startActivity(intent)
        }
        fun init(context: Context) {
            init(context.applicationContext as Application)
        }

        fun init(app: Application) {
            if (sApplication == null) {
                sApplication = app
            } else {
                if (app.javaClass != sApplication?.javaClass) {
                    sApplication = app
                }
            }
        }

        /**
         * Return the context of Application object.
         *
         * @return the context of Application object
         */
        val app: Application
            get() {
                sApplication?.let {
                    return it
                }
                val app: Application = getApplicationByReflect()
                init(app)
                return app
            }

        private fun getApplicationByReflect(): Application {
            try {
                @SuppressLint("PrivateApi") val activityThread =
                    Class.forName("android.app.ActivityThread")
                val thread = activityThread.getMethod("currentActivityThread").invoke(null)
                val app = activityThread.getMethod("getApplication").invoke(thread)
                    ?: throw NullPointerException("u should init first")
                return app as Application
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
            throw NullPointerException("u should init first")
        }
    }
}