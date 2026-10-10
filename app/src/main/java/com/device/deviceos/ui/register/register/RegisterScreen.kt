package com.device.deviceos.ui.register

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.device.deviceos.ui.theme.*

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit
) {
    // 保存用户输入的手机号
    var phone by remember { mutableStateOf("") }

    // 保存短信验证码
    var smsCode by remember { mutableStateOf("") }

    // 保存用户设置的密码
    var password by remember { mutableStateOf("") }

    // 控制密码是否显示明文
    var passwordVisible by remember { mutableStateOf(false) }

    // 用户协议是否勾选
    var agreementChecked by remember { mutableStateOf(false) }

    // 控制提示信息
    var message by remember { mutableStateOf("") }

    // 获取验证码的倒计时
    var countdown by remember { mutableIntStateOf(0) }

    // 是否正在提交注册
    var isLoading by remember { mutableStateOf(false) }

    // 仅用于 UI 演示：不代表真实短信服务
    LaunchedEffect(countdown) {
        if (countdown > 0) {
            kotlinx.coroutines.delay(1000)
            countdown--
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DriveOSBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 顶部栏
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "返回",
                tint = DriveOSText,
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        message = "当前为注册演示页面"
                    }
            )

            Text(
                text = "创建账号",
                color = DriveOSText,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.size(28.dp))
        }

        Spacer(modifier = Modifier.height(28.dp))

        // DriveOS Logo 区域
        Box(
            modifier = Modifier
                .size(76.dp)
                .background(
                    color = DriveOSCard,
                    shape = RoundedCornerShape(22.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.DirectionsCar,
                contentDescription = "DriveOS",
                tint = DriveOSAccent,
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "DriveOS",
            color = DriveOSText,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "智能出行助手",
            color = DriveOSSecondaryText,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 三步进度条
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .background(
                            color = if (index == 0) {
                                DriveOSAccent
                            } else {
                                DriveOSBorder
                            },
                            shape = CircleShape
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // 页面标题
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "开启智能出行",
                color = DriveOSText,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "注册账号，连接你的每一次旅程",
                color = DriveOSSecondaryText,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // 手机号
        InputLabel("手机号码")

        DriveOSTextField(
            value = phone,
            onValueChange = {
                if (it.length <= 11 && it.all(Char::isDigit)) {
                    phone = it
                }
            },
            placeholder = "请输入手机号",
            keyboardType = KeyboardType.Phone
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 验证码
        InputLabel("短信验证码")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                DriveOSTextField(
                    value = smsCode,
                    onValueChange = {
                        if (it.length <= 6 && it.all(Char::isDigit)) {
                            smsCode = it
                        }
                    },
                    placeholder = "请输入验证码",
                    keyboardType = KeyboardType.Number
                )
            }

            Button(
                onClick = {
                    if (!phone.matches(Regex("^1[3-9]\\d{9}$"))) {
                        message = "请输入正确的手机号"
                    } else if (countdown == 0) {
                        countdown = 60
                        // 演示模式：模拟发送验证码
                        message = "演示验证码为 123456，请手动输入"
                    }
                },
                enabled = countdown == 0,
                modifier = Modifier.height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DriveOSCard,
                    contentColor = DriveOSAccent,
                    disabledContainerColor = DriveOSCard,
                    disabledContentColor = DriveOSSecondaryText
                ),
                contentPadding = PaddingValues(horizontal = 10.dp)
            ) {
                Text(
                    text = if (countdown > 0) {
                        "${countdown}s"
                    } else {
                        "获取验证码"
                    },
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 密码
        InputLabel("设置密码")

        OutlinedTextField(
            value = password,
            onValueChange = {
                if (it.length <= 20) password = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            placeholder = {
                Text(
                    "请设置 8–20 位密码",
                    color = DriveOSSecondaryText
                )
            },
            singleLine = true,
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                ) {
                    Icon(
                        imageVector = if (passwordVisible) {
                            Icons.Default.Visibility
                        } else {
                            Icons.Default.VisibilityOff
                        },
                        contentDescription = "切换密码显示",
                        tint = DriveOSSecondaryText
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            shape = RoundedCornerShape(12.dp),
            colors = inputColors()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 用户协议
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = agreementChecked,
                onCheckedChange = {
                    agreementChecked = it
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = DriveOSAccent,
                    uncheckedColor = DriveOSSecondaryText,
                    checkmarkColor = DriveOSBackground
                )
            )

            Text(
                text = "我已阅读并同意",
                color = DriveOSSecondaryText,
                fontSize = 12.sp
            )

            Text(
                text = "用户协议",
                color = DriveOSAccent,
                fontSize = 12.sp,
                modifier = Modifier.clickable {
                    message = "这里后续接入用户协议页面"
                }
            )

            Text(
                text = "和",
                color = DriveOSSecondaryText,
                fontSize = 12.sp
            )

            Text(
                text = "隐私政策",
                color = DriveOSAccent,
                fontSize = 12.sp,
                modifier = Modifier.clickable {
                    message = "这里后续接入隐私政策页面"
                }
            )
        }

        // 提示信息
        if (message.isNotEmpty()) {
            Text(
                text = message,
                color = if (
                    message.contains("成功") ||
                    message.contains("演示验证码")
                ) {
                    DriveOSAccent
                } else {
                    DriveOSError
                },
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 注册按钮
        Button(
            onClick = {
                when {
                    !phone.matches(Regex("^1[3-9]\\d{9}$")) -> {
                        message = "请输入正确的手机号"
                    }

                    smsCode != "123456" -> {
                        message = "演示模式下请输入验证码 123456"
                    }

                    password.length !in 8..20 -> {
                        message = "密码长度需要为 8–20 位"
                    }

                    !agreementChecked -> {
                        message = "请先阅读并同意用户协议"
                    }

                    else -> {
                        isLoading = true
                        message = "注册成功，正在进入车辆信息页面"
                        onRegisterSuccess()
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DriveOSAccent,
                contentColor = DriveOSBackground
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = DriveOSBackground,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "注册并继续",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 登录入口占位
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "已有账号？",
                color = DriveOSSecondaryText,
                fontSize = 13.sp
            )

            Text(
                text = "立即登录",
                color = DriveOSAccent,
                fontSize = 13.sp,
                modifier = Modifier.clickable {
                    message = "登录页面后续实现"
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun InputLabel(text: String) {
    Text(
        text = text,
        color = DriveOSSecondaryText,
        fontSize = 13.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )
}

@Composable
private fun DriveOSTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        placeholder = {
            Text(
                text = placeholder,
                color = DriveOSSecondaryText,
                fontSize = 14.sp
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        shape = RoundedCornerShape(12.dp),
        colors = inputColors()
    )
}

@Composable
private fun inputColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = DriveOSText,
    unfocusedTextColor = DriveOSText,
    focusedBorderColor = DriveOSAccent,
    unfocusedBorderColor = DriveOSBorder,
    cursorColor = DriveOSAccent,
    focusedContainerColor = DriveOSCard,
    unfocusedContainerColor = DriveOSCard,
    focusedPlaceholderColor = DriveOSSecondaryText,
    unfocusedPlaceholderColor = DriveOSSecondaryText
)