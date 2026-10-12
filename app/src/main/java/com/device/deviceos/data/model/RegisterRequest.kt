package com.device.deviceos.data.model

data class RegisterRequest(
    /**
     * 手机号
     */
    val phone:String,
    /**
     * 短信验证码
     */
    val code:String,
    /**
     * 密码
     */
    val password:String
)
