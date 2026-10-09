package com.device.deviceos.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

//模拟用户名和密码
val username = "admin"

val password = "123456"

@Composable
fun login(){
    Column(modifier = Modifier.fillMaxSize(),) {
        TextField(value = "", onValueChange = {}, placeholder = { Text("Username") })
        TextField(value = "", onValueChange = {}, placeholder = { Text("Password") })

    }

}