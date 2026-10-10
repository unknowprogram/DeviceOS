
package com.device.deviceos.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

// 定义底部导航菜单的数据结构
private data class BottomNavItem(
    val router: String,
    val icon: ImageVector,
    val title: String
)

// 定义底部导航菜单
private val bottomNavigationBarItems = listOf(
    // 首页
    BottomNavItem(
        AppRoute.Home,
        Icons.Filled.Home,
        "首页"
    ),

    // 车辆
    BottomNavItem(
        AppRoute.VehicleScreen,
        Icons.Filled.DirectionsCar,
        "车辆"
    ),

    // 行驶
//    BottomNavItem(
//        AppRoute.DrivingScreen,
//        Icons.Filled.Speed,
//        "行驶"
//    ),
//
//    // 告警
//    BottomNavItem(
//        AppRoute.AlarmScreen,
//        Icons.Filled.Notifications,
//        "告警"
//    ),
//
//    // 我的
//    BottomNavItem(
//        AppRoute.MyScreen,
//        Icons.Filled.Person,
//        "我的"
//    )
)

@Composable
fun BottomNavigationBar(
    // 当前路由
    currentRoute: String?,
    // 导航到指定路由
    navigateTo: (String) -> Unit
) {
    // 底部导航栏
    NavigationBar {
        // 遍历所有菜单
        bottomNavigationBarItems.forEach { item ->
            NavigationBarItem(
                // 判断当前菜单是否选中
                selected = currentRoute == item.router,
                // 点击菜单时通知外部进行页面跳转
                onClick = {
                    navigateTo(item.router)
                },
                // 菜单图标
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                // 菜单文字
                label = {
                    Text(text = item.title)
                },
                // 始终显示菜单文字
                alwaysShowLabel = true
            )
        }
    }
}
