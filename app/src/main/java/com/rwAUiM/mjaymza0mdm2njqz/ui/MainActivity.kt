package com.rwAUiM.mjaymza0mdm2njqz.ui

import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rwAUiM.mjaymza0mdm2njqz.R
import com.rwAUiM.mjaymza0mdm2njqz.utils.AdaptScreenUtils
import com.rwAUiM.mjaymza0mdm2njqz.utils.BarUtils


class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.immerseStatusBar(this)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment()).commit()
                    true
                }

                R.id.action_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, MineFragment()).commit()
                    true
                }

                else -> false
            }
        }

//         默认显示主页
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment())
            .commit()
    }

    override fun getResources(): Resources {
        // 屏幕适配 https://www.jianshu.com/p/7da141e682c7
        return AdaptScreenUtils.adaptWidth(super.getResources(), 390)
    }
}