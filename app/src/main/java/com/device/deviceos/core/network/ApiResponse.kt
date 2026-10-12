package com.device.deviceos.core.network

/**
 * 后端统一返回格式
 *
 * 对应SpringBoot:
 *
 * {
 *   code:200,
 *   msg:"success",
 *   data:{}
 * }
 *
 */
data class ApiResponse<T>(
    /**
     * 状态吗
     * */
    val code: Int,
    /**
     * 提示信息
     * */
    val msg: String,
    /**
     * 数据
     * */
    val data: T
)