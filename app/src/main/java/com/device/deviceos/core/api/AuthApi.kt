package com.device.deviceos.core.api

import com.device.deviceos.core.network.ApiResponse
import com.device.deviceos.data.model.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi{
    //注册接口
    @POST("v1/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): ApiResponse<Boolean>

}