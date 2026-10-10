package com.device.deviceos.ui.launch
import com.device.deviceos.ui.launch.LaunchViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.device.deviceos.R

/**
 * DriveOS 闪屏页面
 *
 * @param onLaunchFinished 闪屏结束后的回调
 * @param launchViewModel 闪屏页面 ViewModel
 */
@Composable
fun LaunchScreen(
    onLaunchFinished: () -> Unit,
    launchViewModel: LaunchViewModel = viewModel()
) {
    // 监听加载进度
    val progress by launchViewModel.progress.collectAsStateWithLifecycle()

    // 监听闪屏是否结束
    val isLaunchFinished by
    launchViewModel.isLaunchFinish.collectAsStateWithLifecycle()

    // 加载完成后通知导航层跳转
    LaunchedEffect(isLaunchFinished) {
        if (isLaunchFinished) {
            onLaunchFinished()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // 1. 背景图片
        Image(
            painter = painterResource(id = R.drawable.launch_screen),
            contentDescription = "DriveOS 闪屏背景",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 2. 背景渐变遮罩，让文字更清晰
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0x33101F35),
                            Color(0x22071625),
                            Color(0x99040D18)
                        )
                    )
                )
        )

        // 3. 上方 Logo 和标语
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 0.dp
                )
                .padding(top = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.launch_logo
                ),
                contentDescription = "DriveOS Logo",
                modifier = Modifier
                    .width(330.dp)
                    .height(80.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "DriveOS",
                color = Color(0xFFEAF4FF),
                fontSize = 32.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "智 驾 未 来 · 智 能 出 行",
                color = Color(0xFFEAF4FF),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // 4. 底部加载进度区域
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(
                    start = 48.dp,
                    end = 48.dp,
                    bottom = 56.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 加载文字
            Text(
                text = if (progress >= 1f) {
                    "加载完成"
                } else {
                    "正在加载..."
                },
                color = Color(0xFFB8C9DF),
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 横向进度条
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp),
                color = Color(0xFF168BFF),
                trackColor = Color(0xFF29405A),
                gapSize = 0.dp,
                drawStopIndicator = {}
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 加载百分比
            Text(
                text = "${(progress * 100).toInt()}%",
                color = Color(0xFF9BAFC5),
                fontSize = 12.sp
            )
        }
    }
}
