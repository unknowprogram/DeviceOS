package com.device.deviceos.ui.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
* 闪屏页 ViewModel
*
* 负责：
* 1. 控制闪屏展示时间
* 2. 保存闪屏状态
* 3. 通知 UI 闪屏是否结束
*/
class LaunchViewModel: ViewModel() {
    //定义闪屏最短时间1.5秒
    private val splashDuration = 1500L;

    //内部状态
    private val _isLaunchFinish = MutableStateFlow(false);

    //对外只读状态
    val isLaunchFinish: StateFlow<Boolean> = _isLaunchFinish.asStateFlow();

    //加载进度
    private val _loadProgress = MutableStateFlow(0f);
    val progress: StateFlow<Float> = _loadProgress.asStateFlow();

    init {
        // ViewModel 创建时开始执行闪屏逻辑
        startLaunch()
    }


    /**
     * 启动闪屏页
     * */
    private fun startLaunch() {
        viewModelScope.launch {
            val totalSteps = 100;
            val stepDelay = splashDuration / totalSteps;
            for (step in 1..totalSteps) {
                delay(stepDelay);
                //更新进度
                _loadProgress.value = step / 100f;
            }
            //加载完成
            _isLaunchFinish.value = true;

        }

    }
}