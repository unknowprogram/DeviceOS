
package com.device.deviceos.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.device.deviceos.ui.home.HomeScreen
import com.device.deviceos.ui.launch.LaunchScreen
import com.device.deviceos.ui.login.LoginScreen
import com.device.deviceos.ui.register.RegisterScreen
import com.device.deviceos.ui.register.binding.BindVehicleScreen
import com.device.deviceos.ui.register.vehicle.VehicleInfoScreen
import com.device.deviceos.ui.vehicleScreen.VehicleScreen


/**
 * AppNavHost：应用的页面导航管理器
 *
 * 主要职责：
 * 1. 定义应用启动后默认显示哪个页面
 * 2. 将路由与页面关联起来
 * 3. 处理页面之间的跳转
 * 4. 处理返回上一个页面的操作
 *
 * @param navController 导航控制器，由上层创建并传入
 */
@Composable
fun AppNavHost(
    navController: NavHostController
) {
    // 创建导航容器，统一管理应用中的页面
    NavHost(
        navController = navController,
        startDestination = AppRoute.Login
    ) {

        // 登录页
        composable(AppRoute.Login) {
            LoginScreen(
                onLoginSuccess = {
                    // 登录成功，进入闪屏页，并移除登录页，避免返回时回到登录页
                    navController.navigate(AppRoute.Launch) {
                        popUpTo(AppRoute.Login) { inclusive = true }
                    }
                }
            )
        }

        // 注册页
        composable(AppRoute.Register) {
            RegisterScreen(
                onRegisterSuccess = {
                    // 注册成功，进入车辆信息查询，并移除注册页，避免返回时回到注册页
                    navController.navigate(AppRoute.VehicleScreen) {
                        popUpTo(AppRoute.Register) { inclusive = true }
                    }
                }
            )
        }

        //获取车辆信息页面
        composable(AppRoute.VehicleInfo) {
            VehicleInfoScreen(
                onVehicleInfoSuccess = {
                    // 车辆信息获取成功，进入车辆页面，并移除车辆信息页面，避免返回时回到车辆信息页面
                    navController.navigate(AppRoute.VehicleScreen) {
                        popUpTo(AppRoute.VehicleScreen) { inclusive = true }
                    }
                }
            )
        }
        //绑定车辆信息
        composable(AppRoute.BindVehicle) {
            BindVehicleScreen(
                onBindVehicleSuccess = {
                    // 车辆信息绑定成功，进入车辆页面，并移除车辆信息页面，避免返回时回到车辆信息页面
                    navController.navigate(AppRoute.VehicleScreen) {
                        popUpTo(AppRoute.VehicleScreen) { inclusive = true }
                    }
                }
            )
        }

        // 闪屏页
        composable(AppRoute.Launch) {
            LaunchScreen(
                onLaunchFinished = {
                    // 闪屏加载完成，进入首页，并移除闪屏页
                    navController.navigate(AppRoute.Home) {
                        popUpTo(AppRoute.Launch) { inclusive = true }
                    }
                }
            )
        }

        // 首页
        composable(AppRoute.Home) {
            HomeScreen(
                onVehicleClick = {
                    navController.navigate(
                        AppRoute.VehicleScreen
                    )
                }
            )
        }

        // 车辆页面
        composable(AppRoute.VehicleScreen) {
            VehicleScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
