package com.device.deviceos.core.network

/**
 * 网络请求状态
 *
 * ViewModel通过这个通知Compose页面
 *
 */
sealed class NetworkResult<T>{
    //请求中
    class Loading<T> : NetworkResult<T>()
    /**
     * 请求成功
     *
     * @param data 返回数据
     */
    data class Success<T>(val data: T) : NetworkResult<T>()

    /**
     * 请求失败
     *
     * @param message 错误信息
     */
    data class Error<T>(val message: String) : NetworkResult<T>()
}