package com.device.deviceos.ui.login
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.device.deviceos.R
import com.device.deviceos.ui.theme.BackgroundOverlayBottom
import com.device.deviceos.ui.theme.BackgroundOverlayMiddle
import com.device.deviceos.ui.theme.BackgroundOverlayTop
import com.device.deviceos.ui.theme.Blue
import com.device.deviceos.ui.theme.InputBackground
import com.device.deviceos.ui.theme.InputBorder
import com.device.deviceos.ui.theme.LoginButtonBorder
import com.device.deviceos.ui.theme.LoginButtonEnd
import com.device.deviceos.ui.theme.LoginButtonStart
import com.device.deviceos.ui.theme.LoginError
import com.device.deviceos.ui.theme.TextGray
import com.device.deviceos.ui.theme.TextWhite

// 模拟登录账号
private const val USERNAME = "admin"
private const val PASSWORD = "123456"

/**
 * DriveOS 登录页面
 */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {}
) {
    val context = LocalContext.current

    // 用户名
    var username by remember { mutableStateOf("") }

    // 密码
    var password by remember { mutableStateOf("") }

    // 是否显示密码
    var passwordVisible by remember { mutableStateOf(false) }

    // 是否记住账号
    var rememberAccount by remember { mutableStateOf(true) }

    // 错误提示
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 1. 背景图片
        Image(
            painter = painterResource(id = R.drawable.login_bg),
            contentDescription = "登录背景",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 2. 深色渐变蒙层
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            BackgroundOverlayTop,
                            BackgroundOverlayMiddle,
                            BackgroundOverlayBottom
                        )
                    )
                )
        )

        // 3. 页面内容
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 品牌标题
            Spacer(modifier = Modifier.height(300.dp))

            Text(
                text = "DriveOS",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "智 驾 未 来 · 智 能 出 行",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = TextWhite
            )

            // 标题与输入框之间的距离
            Spacer(modifier = Modifier.height(100.dp))

            // 账号输入框
            LoginInput(
                value = username,
                onValueChange = {
                    username = it
                    errorMessage = ""
                },
                hint = "请输入账号/手机号",
                keyboardType = KeyboardType.Text,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "账号",
                        tint = TextGray,
                        modifier = Modifier.size(22.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 密码输入框
            LoginInput(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = ""
                },
                hint = "请输入密码",
                keyboardType = KeyboardType.Password,
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "密码",
                        tint = TextGray,
                        modifier = Modifier.size(21.dp)
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = if (passwordVisible) {
                            Icons.Filled.Visibility
                        } else {
                            Icons.Filled.VisibilityOff
                        },
                        contentDescription = "显示或隐藏密码",
                        tint = TextGray,
                        modifier = Modifier
                            .size(21.dp)
                            .clickable {
                                passwordVisible = !passwordVisible
                            }
                    )
                }
            )

            Spacer(modifier = Modifier.height(15.dp))

            // 记住账号和忘记密码
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = rememberAccount,
                    onCheckedChange = {
                        rememberAccount = it
                    },
                    modifier = Modifier.size(23.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = Blue,
                        uncheckedColor = TextGray,
                        checkmarkColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "记住账号",
                    color = TextWhite,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        rememberAccount = !rememberAccount
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "忘记密码？",
                    color = Color(0xFF28B5FF),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable {
                        Toast.makeText(
                            context,
                            "请联系管理员重置密码",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }

            // 错误提示
            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = errorMessage,
                    color = LoginError,
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(27.dp))

            // 渐变登录按钮
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .clip(RoundedCornerShape(17.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                LoginButtonStart,
                                LoginButtonEnd
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = LoginButtonBorder,
                        shape = RoundedCornerShape(17.dp)
                    )
                    .clickable {
                        when {
                            username.isBlank() || password.isBlank() -> {
                                errorMessage = "请输入账号和密码"
                            }

                            username == USERNAME &&
                                    password == PASSWORD -> {
                                Toast.makeText(
                                    context,
                                    "登录成功",
                                    Toast.LENGTH_SHORT
                                ).show()

                                onLoginSuccess()
                            }

                            else -> {
                                errorMessage = "账号或密码错误"
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "登 录",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(23.dp))

            // 注册入口
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "还没有账号？",
                    color = TextGray,
                    fontSize = 14.sp
                )

                Text(
                    text = " 注册账号",
                    color = TextWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable {
                        Toast.makeText(
                            context,
                            "注册功能暂未开放",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }
}

/**
 * 自定义深色输入框
 */
@Composable
private fun LoginInput(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    keyboardType: KeyboardType,
    visualTransformation: VisualTransformation =
        VisualTransformation.None,
    leadingIcon: @Composable () -> Unit,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(InputBackground)
            .border(
                width = 1.dp,
                color = InputBorder,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 左侧图标
        leadingIcon()

        Spacer(modifier = Modifier.size(12.dp))

        // 输入区域
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            textStyle = TextStyle(
                color = TextWhite,
                fontSize = 15.sp
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            visualTransformation = visualTransformation,
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            color = TextGray,
                            fontSize = 15.sp
                        )
                    }

                    innerTextField()
                }
            }
        )

        // 右侧图标，例如密码显示按钮
        if (trailingIcon != null) {
            Spacer(modifier = Modifier.size(8.dp))
            trailingIcon()
        }
    }
}
