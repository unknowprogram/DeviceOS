package com.device.deviceos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.device.deviceos.navigation.AppNavHost

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            // 创建导航控制器，统一管理应用内的页面跳转
            val navController = rememberNavController()

            // 应用导航容器：登录页 -> 闪屏页 -> 首页
            AppNavHost(navController = navController)
        }
    }
}
