package com.rwAUiM.b06Ay9wD30c.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.rwAUiM.b06Ay9wD30c.App
import com.rwAUiM.b06Ay9wD30c.R
import com.rwAUiM.b06Ay9wD30c.SplashActivity
import kotlin.system.exitProcess

class CrashActivity : AppCompatActivity() {
    companion object {
        fun start(context: Context, ex: Throwable) {
            val starter = Intent(context, CrashActivity::class.java)
            starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            starter.putExtra("error", ex.stackTraceToString())
            context.startActivity(starter)
            exitProcess(0)
        }
    }


    private var exceptionInfo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crash)
        Log.e("onCreate: ", "")
        intent.getStringExtra("error")?.apply {
            exceptionInfo = this /* = java.lang.Exception */
        }
        findViewById<EditText>(R.id.edit_msg).setText(exceptionInfo)
        findViewById<Button>(R.id.btn_restart).setOnClickListener {
            restart()
        }
        findViewById<TextView>(R.id.tv_exit).setOnClickListener {
            exitProcess(0)
        }
    }

    fun restart() {
        val starter = Intent(App.mAppContext, SplashActivity::class.java)
        starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        App.mAppContext.startActivity(starter)
        exitProcess(0)
    }
}